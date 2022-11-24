package gps.components.view;

import gps.components.nmea.NMEAInfo;

public interface PositionUpdateListener
{
    void update(final NMEAInfo info);
}
