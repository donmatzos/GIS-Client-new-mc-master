package model.components;

import lombok.AllArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.util.Objects;

@AllArgsConstructor
public class POIObject extends GeoObject
{
    private Point point;

    public void paint(Graphics graphics, Matrix matrix)
    {
        try
        {
            Image img = ImageIO.read(
                    Objects.requireNonNull(getClass().getResource("resources/img/point-of-interest.png")));
            Point pointToDraw;
            if (matrix != null) {
                pointToDraw = matrix.multiply(point);

            } else {
                pointToDraw = point;
            }
            graphics.drawImage(img,pointToDraw.x,pointToDraw.y,32,32,null);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
