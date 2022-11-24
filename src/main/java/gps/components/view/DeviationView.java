package gps.components.view;

import gps.components.nmea.NMEAInfo;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.components.Matrix;
import utils.BufferedImageUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class DeviationView extends BaseView
{
    public DeviationView(final Stage stage, final Scene scene, final Pane root)
    {
        super(stage, scene, root);
    }

    private final static String CANVAS_ID = "DEVIATION_CANVAS";

    private Vector<Point> points;
    private BufferedImage image;

    @Override
    public void init()
    {
        Canvas canvas = new Canvas(root.getWidth(), root.getHeight());
        canvas.setId(CANVAS_ID);
        canvas.heightProperty().bind(root.heightProperty());
        canvas.widthProperty().bind(root.widthProperty());
        points = new Vector<>();
        root.getChildren().add(canvas);
    }

    @Override
    public void update(final NMEAInfo info)
    {
        checkImage(info);

        Rectangle window = getWindow();
        Canvas canvas = (Canvas) scene.lookup("#" + CANVAS_ID);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Graphics2D graphics = image.createGraphics();

        configureGraphics(graphicsContext, graphics);

        int size = Math.min(window.width, window.height);
        Rectangle ovalBounds = getOvalBounds(window, graphics, size);
        Rectangle mapBounds = getMapBounds(points);

        handleDrawing(graphics, size, ovalBounds, mapBounds);

        WritableImage writable = BufferedImageUtils.convertToWritableImage(image);
        graphicsContext.drawImage(writable, 0, 0);
    }

    private void checkImage(final NMEAInfo info)
    {
        if (image == null)
        {
            image = new BufferedImage((int) root.getWidth(),(int) root.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        if (info.getLatitude() > 0 && info.getLongitude() > 0)
        {
            // System.out.println("lat="+info.latitude+";lon="+info.longitude);
            int multiplier = 1000000;
            points.add(new Point((int) (info.getLongitude() * multiplier),(int) (info.getLatitude() * multiplier)));
        }
    }

    private Rectangle getOvalBounds(final Rectangle window, final Graphics2D graphics, int size)
    {
        graphics.drawOval(window.x, window.y, size, size);
        return new Rectangle(window.x , window.y, size, size);
    }

    private void handleDrawing(final Graphics2D graphics, final int size, final Rectangle ovalBounds, final Rectangle mapBounds)
    {
        if (mapBounds != null)
        {
            double alpha = 45;
            int radius = size /2;
            double deltaX = Math.cos(Math.toRadians(alpha))*radius;
            double deltaY = Math.sin(Math.toRadians(alpha))*radius;
            Point startPoint =
                    new Point((int)(ovalBounds.getCenterX() - deltaX), (int) (ovalBounds.getCenterY() - deltaY));
            Rectangle innerBounds = new Rectangle(startPoint);
            innerBounds.add(new Point((int) (startPoint.x + (2*deltaX)),(int) (startPoint.y + (2*deltaY))));

            Matrix transformationsMatrix = Matrix.zoomToFit(mapBounds, innerBounds);

            int[] xpoints = new int[points.size()];
            int[] ypoints = new int[points.size()];

            int iterator = 0;
            int dotSize = 5;
            for (Point p : points)
            {
                Point point = transformationsMatrix.multiply(p);

                System.out.println("Point=" + point);
                xpoints[iterator] = point.x;
                ypoints[iterator]= point.y;
                graphics.setColor(Color.BLACK);
                graphics.fillOval(point.x,point.y,dotSize,dotSize);
                iterator++;
            }
            graphics.drawPolyline(xpoints,ypoints,xpoints.length);
        }
    }

    private Rectangle getMapBounds(final Vector<Point> points)
    {
        Rectangle world = null;
        for (Point point : points)
        {
            if (world == null)
            {
                world = new Rectangle(point);
            }
            else
            {
                world.add(point);
            }

        }
        return world;
    }
}
