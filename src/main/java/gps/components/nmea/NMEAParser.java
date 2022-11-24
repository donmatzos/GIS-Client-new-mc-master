package gps.components.nmea;

import gps.components.GNSSSimulator;
import gps.components.satellites.BeidouSatellite;
import gps.components.satellites.GLONASSSatellite;
import gps.components.satellites.GNSSSatellite;
import gps.components.satellites.GPSSatellite;
import gps.components.satellites.GalileoSatellite;
import gps.components.satellites.SatelliteInfo;
import javafx.application.Platform;

import java.io.FileNotFoundException;
import java.io.IOException;

public class NMEAParser extends NMEAObserver
{
    private GNSSSimulator simulator;
    private NMEAInfo oldInfo;
    private NMEAInfo currentInfo;

    public NMEAParser() {
        try
        {
            simulator = new GNSSSimulator("resources/nmea/NMEA-hgb-static-gps.nmea", 1000, "$GPGGA");
            oldInfo = new NMEAInfo();
            currentInfo = new NMEAInfo();
        }
        catch (final FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public NMEAParser(final NMEAEnum nmeaType) {
        try
        {
            simulator = getSimulatorFromEnum(nmeaType);
            oldInfo = new NMEAInfo();
            currentInfo = new NMEAInfo();
        }
        catch (final FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void start()
    {
        Thread thread = new Thread(this::parseData);
        thread.start();
    }

    public NMEAInfo parseLine(final String line, final NMEAInfo info)
    {
        String[] strings = line.split(",");
        if (strings.length > 0)
        {
            String identifierSat = strings[0].substring(1,3);
            String identifier = strings[0].substring(3,6);
            handleIdentifier(info, strings, identifierSat, identifier);
        }
        return info;
    }

    /*
     ************************************ private helper methods ************************************
     */

    private GNSSSimulator getSimulatorFromEnum(final NMEAEnum nmeaType) throws FileNotFoundException
    {
        return switch (nmeaType)
        {
            case EWD_PC -> new GNSSSimulator("files/NMEA-engerwitzdorf-pluscity.nmea", 1000, "$GPGGA");
            case POS -> new GNSSSimulator("files/NMEA-position-static.nmea", 1000, "$GPGGA");
            case FAULTY -> new GNSSSimulator("files/NMEA-faulty.nmea", 1000, "$GPGGA");
            default -> new GNSSSimulator("files/NMEA-hgb-static-gps.nmea", 1000, "$GPGGA");
        };
    }

    private void parseData()
    {
        try
        {
            String line;
            while ((line = simulator.readLine()) != null && !line.isEmpty())
            {
                if (line.contains(simulator.getFilter()))
                {
                    oldInfo = currentInfo;
                    currentInfo = new NMEAInfo();
                    Platform.runLater(() -> updateObservers(oldInfo));
                }
                currentInfo = parseLine(line,currentInfo);
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }

    private void handleIdentifier(final NMEAInfo info, final String[] strings,
                                  final String identifierSat, final String identifier)
    {
        switch (identifier)
        {
            case "GGA" -> handleGga(info, strings);
            case "GSA" -> handleGsa(info, strings);
            case "GSV" -> handleGsv(info, strings, identifierSat);
        }
    }

    private void handleGga(final NMEAInfo info, final String[] strings)
    {
        if (!strings[1].isEmpty())
        {
            info.time = Double.parseDouble(strings[1]);
        }
        if (!strings[2].isEmpty())
        {
            info.latitude = convertToDecimalGrade(strings[2]);
        }
        if (!strings[4].isEmpty())
        {
            info.longitude = convertToDecimalGrade(strings[4]);
        }
        if (!strings[6].isEmpty())
        {
            info.fixQuality = Integer.parseInt(strings[6]);
        }
        if (!strings[7].isEmpty())
        {
            info.amountSatellites = Integer.parseInt(strings[7]);
        }
        if (!strings[9].isEmpty())
        {
            info.height = Double.parseDouble(strings[9]);
        }
    }

    private void handleGsa(final NMEAInfo info, final String[] strings)
    {
        if (strings.length > 17)
        {
            for (int i = 3; i < 15; i++)
            {
                if (strings[i]!=null && !strings[i].isEmpty())
                {
                    info.satelliteIds.add(Integer.parseInt(strings[i]));
                }
            }
            if (!strings[15].isEmpty())
            {
                info.pdop = Double.parseDouble(strings[15]);
            }
            if (!strings[16].isEmpty())
            {
                info.hdop = Double.parseDouble(strings[16]);
            }
            if (!strings[17].isEmpty())
            {
                String sVdop = strings[17].split("\\*")[0];
                if(!sVdop.isEmpty())
                {
                    info.vdop = Double.parseDouble(sVdop);
                }
            }
        }
    }

    private void handleGsv(final NMEAInfo info, final String[] strings, final String identifierSat)
    {
        for (int i = 4; i + 3 < strings.length ; i = i + 4)
        {
            if (!strings[i].isEmpty()
                && !strings[i+1].isEmpty()
                && !strings[i+2].isEmpty())
            {
                int snr = 0;
                if (strings[i+3].contains("*"))
                {
                    String sSNR = strings[i+3].split("\\*")[0];
                    if (!sSNR.isEmpty())
                    {
                        snr = Integer.parseInt(sSNR);
                    }
                }
                else
                {
                    if (!strings[i+3].isEmpty())
                    {
                        snr = Integer.parseInt(strings[i + 3]);
                    }
                }
                SatelliteInfo satInfo = getSatelliteInfoFromIdentifier(identifierSat, Integer.parseInt(strings[i]),
                        Double.parseDouble(strings[i + 1]), Double.parseDouble(strings[i + 2]), snr);
                if (satInfo != null)
                {
                    info.satelliteInfos.add(satInfo);
                }
            }
        }
    }

    private SatelliteInfo getSatelliteInfoFromIdentifier(final String identifier, final int id, final double verAngDist,
                                                         final double horAngDist, final int snr){
        return switch (identifier)
        {
            case "GA" -> new GalileoSatellite(id, verAngDist, horAngDist, snr);
            case "GP" -> new GPSSatellite(id, verAngDist, horAngDist, snr);
            case "GL" -> new GLONASSSatellite(id, verAngDist, horAngDist, snr);
            case "BD" -> new BeidouSatellite(id, verAngDist, horAngDist, snr);
            case "GN" -> new GNSSSatellite(id, verAngDist, horAngDist, snr);
            default -> null;
        };
    }

    private Double convertToDecimalGrade(final String value){
        if (value.isEmpty())
        {
            return 0.0;
        }
        double grade = Double.parseDouble(value.substring(0, 2));
        double min = Double.parseDouble(value.substring(2));
        return (min/60)+grade;
    }

}
