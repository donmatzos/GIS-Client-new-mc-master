package model.components;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import model.components.presentation.PresentationSchema;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Vector;

@AllArgsConstructor
@ToString
public class GeoObject
{
    @Getter
    private final String id;
    @Getter
    private final int type;
    @Getter
    private final Vector<GeoObjectPart> geoObjectList;

    public GeoObject()
    {
        id = null;
        type = 0;
        geoObjectList = new Vector<>();
    }

    public GeoObject(String id, int type, GeoObjectPart poly)
    {
        this.id = id;
        this.type = type;
        geoObjectList = new Vector<>();
        geoObjectList.add(poly);
    }

    public Rectangle getBounds() {
        Rectangle retRect = null;
        for (GeoObjectPart part : geoObjectList) {
            if (part instanceof GeoObjectArea) {
                if (retRect == null) {
                    retRect = ((GeoObjectArea)part).geometry.getBounds();
                }
                retRect.union(((GeoObjectArea)part).geometry.getBounds());
            }
        }
        return retRect;
    }

    public void paint(final Graphics graphics, final Matrix matrix, final PresentationSchema schema)
    {
        for (GeoObjectPart part : geoObjectList)
        {
            part.draw((Graphics2D)graphics, matrix, schema);
        }
    }
}
