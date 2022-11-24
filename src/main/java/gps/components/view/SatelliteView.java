package gps.components.view;

import gps.components.nmea.NMEAInfo;
import gps.components.satellites.SatelliteInfo;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.BufferedImageUtils;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class SatelliteView extends BaseView
{
    private final static String CANVAS_ID = "SAT_CANVAS";

    private BufferedImage image;

    public SatelliteView(final Stage stage, final Scene scene, final Pane root)
    {
        super(stage, scene, root);
    }

    @Override
    public void init()
    {
        Canvas canvas = new Canvas(root.getWidth(),root.getHeight());
        canvas.setId(CANVAS_ID);
        canvas.heightProperty().bind(root.heightProperty());
        canvas.widthProperty().bind(root.widthProperty());
        root.getChildren().add(canvas);
    }

    @Override
    public void update(final NMEAInfo info)
    {
        if(image == null)
        {
            image = new BufferedImage((int)root.getWidth(),(int)root.getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        info.checkIfSatellitesAreUsedForCalculation();
        Canvas canvas = (Canvas) scene.lookup("#" + CANVAS_ID);
        Rectangle window = getWindow();
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        Graphics2D graphics = image.createGraphics();

        configureGraphics(graphicsContext, graphics);

        handleDrawing(info, window, graphics);

        WritableImage writable = BufferedImageUtils.convertToWritableImage(image);
        graphicsContext.drawImage(writable,0,0);
    }

    private void handleDrawing(final NMEAInfo info, final Rectangle window, final Graphics2D graphics)
    {
        int size = Math.min(window.width, window.height);
        graphics.drawOval(window.x, window.y,size,size);
        Rectangle ovalRect = new Rectangle(window.x, window.y,size,size);

        for(SatelliteInfo satelliteInfo : info.getSatelliteInfos())
        {
            Point p = calculateSatellitePosition(satelliteInfo, size/2.0,
                    new Point((int) ovalRect.getCenterX(), (int) ovalRect.getCenterY()));
            satelliteInfo.draw(graphics,p);
        }
    }

    private Point calculateSatellitePosition(final SatelliteInfo satInfo, final double radius, final Point center)
    {
        double alpha = satInfo.getVerticalAngularDistance();
        double radiusInnerCircle = Math.cos(Math.toRadians(alpha)) * radius;
        double alphaInner = satInfo.getHorizontalAngularDistance() + 270;
        double deltaX = Math.cos(Math.toRadians(alphaInner))*radiusInnerCircle;
        double deltaY = Math.sin(Math.toRadians(alphaInner))*radiusInnerCircle;

        return new Point((int) (deltaX+center.x), (int) (deltaY+center.y));
    }

}
