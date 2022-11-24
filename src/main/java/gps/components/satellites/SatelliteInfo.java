package gps.components.satellites;

import lombok.Getter;
import lombok.ToString;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

@ToString
public abstract class SatelliteInfo
{
    @Getter
    protected int id;
    @Getter
    protected double horizontalAngularDistance;
    @Getter
    protected double verticalAngularDistance;
    protected int signalToNoiseRatio;
    protected final int SATELLITE_SIZE = 25;
    private boolean isUsedForCalculation = false;

    public SatelliteInfo(final int id, final double verticalAngularDistance, final double horizontalAngularDistance,
                         final int snr)
    {
        this.id = id;
        this.horizontalAngularDistance = horizontalAngularDistance;
        this.verticalAngularDistance = verticalAngularDistance;
        this.signalToNoiseRatio = snr;
    }

    public void setUsedForCalculation(final boolean usedForCalculation)
    {
        isUsedForCalculation = usedForCalculation;
    }

    public Color getFilLColor()
    {
        if(isUsedForCalculation)
        {
            return Color.GREEN;
        }
        else
        {
            return signalToNoiseRatio > 0 ? Color.BLUE : Color.RED;
        }
    }

    public Color getStrokeColor()
    {
        return Color.BLACK;
    }

    public abstract void draw(final Graphics2D g2d, final Point p);
}
