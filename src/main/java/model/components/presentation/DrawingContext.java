package model.components.presentation;

import java.awt.Color;
import java.util.Hashtable;

public abstract class DrawingContext
{
    protected Hashtable<Integer,PresentationSchema> context = null;

    protected DrawingContext() {
        context = new Hashtable<>();
        initSchemata();
    }

    public PresentationSchema getSchema(int _type)
    {
        return context.getOrDefault(_type,getDefaultSchema());
    }

    public static PresentationSchema getDefaultSchema()
    {
        return new PresentationSchema(Color.BLACK,Color.WHITE);
    }

    protected abstract void initSchemata();
}
