package model;

import model.components.GeoObject;
import model.components.Matrix;
import model.components.POIObject;
import model.components.presentation.DrawingContext;
import model.components.presentation.PresentationSchema;
import server.OSMServer;
import server.ServerConnection;
import server.ServerEnum;
import view.DataObserver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.Vector;

public class GISModel
{
    private DataObserver observer = null;
    private Vector<GeoObject> geoObjects = new Vector<>();
    private Vector<POIObject> poiObjects = new Vector<>();
    private BufferedImage image = null;
    private int width = 640;
    private int height = 480;
    private Matrix translationMatrix;
    private DrawingContext drawingContext;
    private OSMServer server = new ServerConnection();
    private Rectangle worldRectangleSticky = null;

    public void initCanvas() 
    {
        if(image == null) 
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, width, height);
    }

    /**
     * loadData()
     * This method accesses the image and draws every object stored in the polygon list
     */
    public void loadData() throws SQLException
    {
        String areaQuery = "";
        if (worldRectangleSticky != null) 
        {
            areaQuery = getAreaQuery();
        }
        if (server.isConnected())
        {
            handleServerConnection(areaQuery);
            drawingContext = server.getContext();
        }
    }

    public void loadPoiData() throws SQLException
    {
        if (poiObjects == null) 
        {
            createPoiObjects();
        }
        else
        {
            emptyPoi();
        }
        repaint();
    }
    
    public void emptyPoi()
    {
        poiObjects = null;
    }
    
    public void connectToServer(final ServerEnum destination)
    {
        if (server.isConnected())
        {
            server.close();
        }
        server = new ServerConnection(destination);
    }

    public void repaint() throws SQLException 
    {
        initCanvas();
        loadData();
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.BLACK);
        handleGeoObjects((Graphics2D) graphics);
        handlePoiObjects(graphics);
        observer.update(image);
    }

    public void addMapObserver(final DataObserver mapObserver)
    {
        observer = mapObserver;
    }

    public void setWidth(final int value) 
    {
        width = value;
        image = null;
    }

    public void setHeight(final int value) 
    {
        height = value;
        image = null;
    }

    public void zoomToFit() 
    {
        Rectangle world = getMapBounds(geoObjects);
        Rectangle win = new Rectangle(width-5, height-5);
        translationMatrix = Matrix.zoomToFit(world, win);
        observer.updateScaling(calculateScale());
    }

    public void zoom(final double factor)
    {
        zoom(new Point(width/2,height/2), factor);
    }

    public void zoomRect(final Rectangle windowBounds)
    {
        if (translationMatrix != null)
        {
            Rectangle worldBounds = translationMatrix.invert().multiply(windowBounds);
            if (worldBounds != null)
            {
                translationMatrix = Matrix.zoomToFit(worldBounds, new Rectangle(width, height));
            }
            observer.updateScaling(calculateScale());
        }
    }

    public void zoomToScale(final int scaleFactor)
    {
        double zoomFactor = ((double)calculateScale()/scaleFactor);
        zoom(zoomFactor);
        observer.updateScaling(scaleFactor);
    }
    
    public void scrollHorizontal(final int delta)
    {
        Matrix translate = Matrix.translate(delta, 0);
        updateTranslationMatrix(translate);
    }

    public void scrollVertical(final int delta)
    {
        Matrix translate = Matrix.translate(0, delta);
        updateTranslationMatrix(translate);
    }

    public void rotate(final double alpha)
    {
        Rectangle bounds = new Rectangle(width, height);
        Matrix translateToOrigin = Matrix.translate(-(int)bounds.getCenterX(),-(int)bounds.getCenterY());
        Matrix rotate = Matrix.rotate(alpha);
        Matrix translateToPoint = Matrix.translate((int)bounds.getCenterX(),(int)bounds.getCenterY());
        if (translationMatrix != null)
        {
            translationMatrix =
                    translateToPoint.multiply(rotate.multiply(translateToOrigin.multiply(translationMatrix)));
        }
    }

    public Point getMapPoint(final Point point)
    {
        if (translationMatrix != null)
        {
            Matrix inverse = translationMatrix.invert();
            System.out.println(inverse.multiply(point));
            return inverse.multiply(point);
        }
        return new Point();
    }

    public void setWorldSticky()
    {
        if (worldRectangleSticky == null && translationMatrix != null)
        {
            worldRectangleSticky = new Rectangle(0, 0, width, height);
            Matrix bufferMatrix = translationMatrix.invert();
            worldRectangleSticky = bufferMatrix.multiply(worldRectangleSticky);
            System.out.println("Sticky activated.");
        }
        else
        {
            worldRectangleSticky = null;
            System.out.println("Sticky deactivated.");
        }
    }

    /*
     ************************************ private helper methods ************************************
     */
    
    private void handleServerConnection(final String areaQuery) throws SQLException
    {
        switch (server.getDestination())
        {
            case LINZ -> handleLinzServer(areaQuery);
            case HGB -> handleHgbServer(areaQuery);
            case OSM -> handleOsmServer();
        }
    }

    private void handleOsmServer() throws SQLException
    {
        geoObjects = server.extractData("SELECT * FROM bundeslaender");
    }

    private void handleHgbServer(final String areaQuery) throws SQLException
    {
        geoObjects = new Vector<>();
        geoObjects.addAll(server.extractData("SELECT * FROM osm_place" + areaQuery));
        geoObjects.addAll(server.extractData("SELECT * FROM osm_landuse" + areaQuery));
        geoObjects.addAll(server.extractData("SELECT * FROM osm_natural" + areaQuery));
        geoObjects.addAll(server.extractData("SELECT * FROM osm_building" + areaQuery));
        geoObjects.addAll(server.extractData("SELECT * FROM osm_leisure" + areaQuery));
        geoObjects.addAll(server.extractData("SELECT * FROM osm_amenity" + areaQuery));
//        geoObjects.addAll(server.extractData("SELECT * FROM osm_boundary"));
//        geoObjects.addAll(server.extractData("SELECT * FROM osm_waterway" + areaQuery));
//        hole query 
//        geoObjects.addAll(server.extractData("SELECT * FROM osm_building WHERE id='8207560';"));
    }

    private void handleLinzServer(final String areaQuery) throws SQLException
    {
        geoObjects = new Vector<>();
        geoObjects.addAll(server.extractData("SELECT * FROM osm_landuse" + areaQuery));
//        geoObjects.addAll(server.extractData("SELECT * FROM osm_waterway" + areaQuery));
//        geoObjects.addAll(server.extractData("SELECT * FROM osm_highway" + areaQuery));
//        geoObjects.addAll(server.extractData("SELECT * FROM osm_amenity" + areaQuery));
//        geoObjects.addAll(server.extractData("SELECT * FROM osm_natural" + areaQuery));
        geoObjects.addAll(server.extractData("SELECT * FROM osm_building" + areaQuery));
    }

    private String getAreaQuery()
    {
        return " AS a WHERE a.geom && ST_MakeEnvelope(" +
                worldRectangleSticky.getX() + "," +
                worldRectangleSticky.getY() + "," +
                (worldRectangleSticky.getX()+worldRectangleSticky.getWidth()) + "," +
                (worldRectangleSticky.getY()+worldRectangleSticky.getHeight()) + ")";
    }

    private void createPoiObjects()
    {
        poiObjects = new Vector<>();
        poiObjects.add(new POIObject(new Point(54048498,580517634)));
        poiObjects.add(new POIObject(new Point(53993194,580466584)));
        poiObjects.add(new POIObject(new Point(54110893,580436096)));
        poiObjects.add(new POIObject(new Point(54044244,580387173)));
        poiObjects.add(new POIObject(new Point(54024391,580441768)));
    }

    private void handleGeoObjects(final Graphics2D graphics)
    {
        for (GeoObject object : geoObjects)
        {
            PresentationSchema schema;
            if (drawingContext != null)
            {
                schema = drawingContext.getSchema(object.getType());
            }
            else
            {
                schema = DrawingContext.getDefaultSchema();
            }
            schema.paint(graphics, object, translationMatrix);
//            too scared to remove this 
//            for (AGeoObjectPart obj : object.getPoly()) 
//            {
//                obj.draw(g,mTranslationMatrix,schema);
//            }

        }
    }

    private void handlePoiObjects(final Graphics graphics)
    {
        if (poiObjects != null)
        {
            for (POIObject poi : poiObjects)
            {
                poi.paint(graphics, translationMatrix);
            }
        }
    }
    
    private Rectangle getMapBounds(final Vector<GeoObject> mapObjects)
    {
        Rectangle retRect = null;
        for (GeoObject object : mapObjects) 
        {
            Rectangle bounds = object.getBounds();
            if (bounds != null) 
            {
                if (retRect == null) 
                {
                    retRect = object.getBounds();
                } 
                else 
                {
                    retRect = retRect.union(object.getBounds());
                }
            }
        }
        return retRect;
    }

    private int calculateScale() 
    {
        Point2D.Double vector = new Point2D.Double(0,1.0);
        Point2D.Double vector_transformed = translationMatrix.multiply(vector);
        double screenLength = (72/2.54); // Length of 1cm on the screen (at 72 DPI)
        double vectorLength = Math.sqrt(Math.pow(vector.getX(),2)+Math.pow(vector.getY(),2));
        double transformedVectorLength = 
                Math.sqrt(Math.pow(vector_transformed.getX(), 2) + Math.pow(vector_transformed.getY(), 2));
        return (int) ((screenLength + vectorLength) / transformedVectorLength);
    }

    public void zoom(final Point point, final double factor)
    {
        translationMatrix = Matrix.zoomPoint(translationMatrix, point, factor);
        observer.updateScaling(calculateScale());
    }

    private void updateTranslationMatrix(final Matrix translate)
    {
        if(translationMatrix != null)
        {
            translationMatrix = translate.multiply(translationMatrix);
        }
    }
}
