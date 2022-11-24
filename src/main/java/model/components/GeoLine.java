package model.components;

import lombok.AllArgsConstructor;
import model.components.presentation.PresentationSchema;

import java.awt.Graphics2D;
import java.awt.Point;

@AllArgsConstructor
public class GeoLine extends GeoObjectPart
{
    Point[] geometry;

    @Override
    void draw(final Graphics2D graphics2D, final Matrix matrix, final PresentationSchema schema)
    {
        int[] coordsX = new int[geometry.length];
        int[] coordsY = new int[geometry.length];
        for (int i = 0; i < geometry.length; i++) {
            coordsX[i] = geometry[i].x;
            coordsY[i] = geometry[i].y;
        }
        graphics2D.setColor(schema.getFillColor());
        graphics2D.drawPolyline(coordsX,coordsY,geometry.length);
    }
}
