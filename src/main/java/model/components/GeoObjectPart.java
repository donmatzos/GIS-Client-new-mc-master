package model.components;

import model.components.presentation.PresentationSchema;

import java.awt.Graphics2D;

public abstract class GeoObjectPart
{
    public GeoObjectPart[] holes;

    abstract void draw(Graphics2D graphics2D, Matrix matrix, PresentationSchema schema);
}
