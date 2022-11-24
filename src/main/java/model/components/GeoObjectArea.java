package model.components;

import lombok.AllArgsConstructor;
import model.components.presentation.PresentationSchema;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Area;

@AllArgsConstructor
public class GeoObjectArea extends GeoObjectPart
{
    Polygon geometry;

    @Override
    void draw(final Graphics2D graphics2D, final Matrix matrix, final PresentationSchema schema)
    {
        Polygon polygon = handlePolygonMultiplication(geometry, matrix);
        configureGraphics(graphics2D, schema, polygon);
        Area polygonShape = new Area(polygon);
        handleHoles(polygonShape, matrix);
        graphics2D.fill(polygonShape);
    }



    /*
     ************************************ private helper methods ************************************
     */

    private void configureGraphics(final Graphics2D graphics2D, final PresentationSchema schema, final Polygon polygon)
    {
        graphics2D.setColor(schema.getLineColor());
        graphics2D.setStroke(new BasicStroke(schema.getLineWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics2D.drawPolygon(polygon);
        graphics2D.setColor(schema.getFillColor());
    }

    private Polygon handlePolygonMultiplication(Polygon polygon, final Matrix matrix)
    {
        if (matrix != null)
        {
            return matrix.multiply(geometry);
        }
        return polygon;
    }

    private void handleHoles(final Area polygonShape, final Matrix matrix)
    {
        if (holes != null)
        {
            for (GeoObjectPart hole : holes)
            {
                if (hole instanceof GeoObjectArea)
                {
                    Polygon polyHole = ((GeoObjectArea) hole).geometry;
                    polyHole = handlePolygonMultiplication(polyHole, matrix);
                    polygonShape.subtract(new Area(polyHole));
                }
            }
        }
    }
}
