package model.components.presentation;

import model.components.GeoObject;
import model.components.Matrix;

import java.awt.Color;
import java.awt.Graphics2D;

public class PresentationSchema
{
    private final Color lineColor;
    private final Color fillColor;
    private final float lineWidth = 2.0f;

    public PresentationSchema(final Color lineColor, final Color fillColor)
    {
        this.lineColor = lineColor;
        this.fillColor = fillColor;
        //add line width if necessary
    }

    public Color getLineColor()
    {
        return lineColor;
    }

    public Color getFillColor()
    {
        return fillColor;
    }

    public float getLineWidth()
    {
        return lineWidth;
    }

    public void paint(Graphics2D graphics2D, GeoObject geoObject, Matrix matrix)
    {
        if (lineColor != null && fillColor != null && matrix != null)
        {
            geoObject.paint(graphics2D,matrix,this);
        }
    }
}

