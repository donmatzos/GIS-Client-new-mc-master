package model.components;

import lombok.AllArgsConstructor;
import model.components.presentation.PresentationSchema;

import java.awt.Graphics2D;
import java.awt.Point;

@AllArgsConstructor
public class GeoPoint extends GeoObjectPart
{
    Point geometry;
    @Override
    void draw(final Graphics2D graphics2D, final Matrix matrix, final PresentationSchema schema)
    {
        graphics2D.setColor(schema.getFillColor());
        graphics2D.drawOval(geometry.x, geometry.y, 4, 4);
    }
}
