package gps.components.view;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

@AllArgsConstructor
public abstract class BaseView implements PositionUpdateListener
{
    final Stage stage;
    final Scene scene;
    final Pane root;

    abstract void init();

    protected Rectangle getWindow()
    {
        int padding = 10;
        Rectangle rectangle = new Rectangle((int)root.getWidth()-2*padding,(int)root.getHeight()-2*padding);
        rectangle.x = padding;
        rectangle.y = padding;
        return rectangle;
    }

    protected void configureGraphics(final GraphicsContext graphicsContext, final Graphics2D graphics)
    {
        graphicsContext.clearRect(0, 0, root.getWidth(), root.getHeight());
        graphics.setBackground(Color.LIGHT_GRAY);
        graphics.setPaint(Color.BLACK);
        graphics.clearRect(0, 0, (int) root.getWidth(), (int) root.getHeight());
    }
}
