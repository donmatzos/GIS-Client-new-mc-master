package gps.components.satellites;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public class BeidouSatellite extends SatelliteInfo
{

    public BeidouSatellite(final int id, final double verticalAngularDistance, final double horizontalAngularDistance,
                            final int snr)
    {
        super(id, verticalAngularDistance, horizontalAngularDistance, snr);
    }


    @Override
    public void draw(final Graphics2D graphics, final Point point)
    {
        Polygon poly = new Polygon();
        poly.addPoint(point.x,point.y+(SATELLITE_SIZE/2));
        poly.addPoint(point.x-SATELLITE_SIZE,point.y);
        poly.addPoint(point.x+SATELLITE_SIZE,point.y);
        poly.addPoint(point.x-(SATELLITE_SIZE/2),point.y-(SATELLITE_SIZE/2));
        poly.addPoint(point.x+(SATELLITE_SIZE/2),point.y-(SATELLITE_SIZE/2));
        graphics.setColor(getStrokeColor());
        graphics.drawPolygon(poly);
        graphics.setColor(getFilLColor());
        graphics.fillPolygon(poly);
        graphics.setColor(getStrokeColor());
        graphics.drawString(String.valueOf(id),point.x-(SATELLITE_SIZE/4),point.y+(SATELLITE_SIZE/4));
    }
}
