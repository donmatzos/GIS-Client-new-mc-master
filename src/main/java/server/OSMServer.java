package server;

import model.components.GeoObject;
import model.components.presentation.DrawingContext;

import java.sql.SQLException;
import java.util.Vector;

public interface OSMServer
{
    Vector<GeoObject> extractData(String _query) throws SQLException;
    DrawingContext getContext();
    ServerEnum getDestination();
    boolean isConnected();
    void close();
}
