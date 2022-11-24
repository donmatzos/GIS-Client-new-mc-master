package server;

import model.components.GeoObject;
import model.components.presentation.DrawingContext;
import model.components.presentation.OSMDrawingContext;
import utils.ServerUtils;

import java.sql.Connection;
import java.util.Vector;

public class ServerConnection implements OSMServer
{
    public Connection connection;
    private ServerEnum destination = ServerEnum.HGB;
    private final DrawingContext drawingContext = new OSMDrawingContext();

    /**
     * Default connection to HGB server
     */
    public ServerConnection()
    {
        connection = ServerUtils.init(identifyUrl(destination));
    }

    /**
     * Uses an Enum to identify the correct connection
     * @param destination to determine which server to connect to
     */
    public ServerConnection(final ServerEnum destination)
    {
        this.destination = destination;
        connection = ServerUtils.init(identifyUrl(destination));
    }

    @Override
    public Vector<GeoObject> extractData(final String query)
    {
        return ServerUtils.extractData(connection, query);
    }

    @Override
    public DrawingContext getContext()
    {
        return drawingContext;
    }

    @Override
    public ServerEnum getDestination()
    {
        return destination;
    }

    @Override
    public boolean isConnected()
    {
        return connection != null;
    }

    @Override
    public void close()
    {
        try
        {
            connection.close();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    private String identifyUrl(final ServerEnum destination)
    {
        return switch (destination)
            {
                case LINZ -> "jdbc:postgresql://localhost:5432/osm_lnz";
                case HGB -> "jdbc:postgresql://localhost:5432/osm_hgb";
                case OSM -> "jdbc:postgresql://localhost:5432/osm";
            };
    }
}
