package gps.components.satellites;

import java.awt.Graphics2D;
import java.awt.Point;

public class GalileoSatellite extends SatelliteInfo
{
    public GalileoSatellite(final int id, final double verticalAngularDistance, final double horizontalAngularDistance,
                            final int snr)
    {
        super(id, verticalAngularDistance, horizontalAngularDistance, snr);
    }

    @Override
    public void draw(final Graphics2D graphics, final Point point)
    {
        graphics.setColor(getStrokeColor());
        graphics.drawRect(point.x-(SATELLITE_SIZE/2), point.y-(SATELLITE_SIZE/2), SATELLITE_SIZE, SATELLITE_SIZE);
        graphics.setColor(getFilLColor());
        graphics.fillRect(point.x-(SATELLITE_SIZE/2), point.y-(SATELLITE_SIZE/2), SATELLITE_SIZE, SATELLITE_SIZE);
        graphics.setColor(getStrokeColor());
        graphics.drawString(String.valueOf(id), point.x-(SATELLITE_SIZE/4), point.y+(SATELLITE_SIZE/4));
    }
}
