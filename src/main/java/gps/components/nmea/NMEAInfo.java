package gps.components.nmea;

import gps.components.satellites.SatelliteInfo;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

@ToString
public class NMEAInfo
{
    @Getter
    double longitude;
    @Getter
    double latitude;
    @Getter
    double time;
    @Getter
    int amountSatellites;
    @Getter
    double pdop;
    @Getter
    double hdop;
    @Getter
    double vdop;
    @Getter
    int fixQuality;
    @Getter
    double height;
    @Getter
    ArrayList<Integer> satelliteIds;
    @Getter
    ArrayList<SatelliteInfo> satelliteInfos;

    public NMEAInfo()
    {
        satelliteInfos = new ArrayList<>();
        satelliteIds = new ArrayList<>();
    }

    public String getTimeString()
    {
        String value = Double.toString(time);
        if(value.length() > 5)
        {
            int hours = Integer.parseInt(value.substring(0, 2));
            int minutes = Integer.parseInt(value.substring(2, 4));
            int seconds = Integer.parseInt(value.substring(4, 6));
            return hours + ":" + minutes + ":" + seconds;
        }
        return "00:00:00";
    }

    public void checkIfSatellitesAreUsedForCalculation()
    {
        for (SatelliteInfo info: satelliteInfos)
        {
            info.setUsedForCalculation(satelliteIds.contains(info.getId()));
        }
    }

}
