package model.components.presentation;

import java.awt.Color;

public class DummyDrawingContext extends DrawingContext
{
    public DummyDrawingContext()
    {
        initSchemata();
    }

    @Override
    protected void initSchemata()
    {
        context.put(233,new PresentationSchema(Color.BLACK, Color.WHITE));
        context.put(931,new PresentationSchema(Color.BLACK, Color.RED));
        context.put(932,new PresentationSchema(Color.RED, Color.ORANGE));
        context.put(1101,new PresentationSchema(Color.GREEN, Color.MAGENTA));
    }
}
