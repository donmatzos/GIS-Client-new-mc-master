package utils;

import model.components.GeoLine;
import model.components.GeoObject;
import model.components.GeoObjectArea;
import model.components.GeoObjectPart;
import model.components.GeoPoint;
import org.postgis.Geometry;
import org.postgis.LineString;
import org.postgis.LinearRing;
import org.postgis.MultiPolygon;
import org.postgis.PGbox2d;
import org.postgis.PGgeometry;
import org.postgresql.PGConnection;

import java.awt.Point;
import java.awt.Polygon;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public final class ServerUtils
{
    public static Connection init(final String ulr)
    {
        return getConnection(ulr);
    }

    public static Vector<GeoObject> extractData(final Connection connection, final String query)
    {
        /* Create a statement and execute a select query. */
        Vector<GeoObject> geoObjectVector = new Vector<>();
        fillVectorWithExtractedData(connection, query, geoObjectVector);
        return geoObjectVector;
    }

    /*
     ************************************ private helper methods ************************************
     */

    private static Connection getConnection(final String ulr)
    {
        Connection connection = null;
        try
        {
            /* Load the JDBC driver and establish a connection. */
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(ulr, "geo", "geo");
            /* Add the geometry types to the connection. */
            PGConnection pgConnection = (PGConnection) connection;
            pgConnection.addDataType("geometry", PGgeometry.class);
            pgConnection.addDataType("box2d", PGbox2d.class);
        }
        catch (SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return connection;
    }

    private static void fillVectorWithExtractedData(final Connection connection, final String query,
                                                    final Vector<GeoObject> geoObjectVector)
    {
        try
        {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                String id = resultSet.getString("id");
                int type = resultSet.getInt("type");
                PGgeometry geometry = (PGgeometry) resultSet.getObject("geom");
                extractAndFillGeometryData(geoObjectVector, id, type, geometry);
            }
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void extractAndFillGeometryData(final Vector<GeoObject> geoObjectVector, final String id,
                                                   final int type, final PGgeometry geometry) throws SQLException
    {
        switch (geometry.getGeoType())
        {
            case Geometry.POINT ->
            {
                fillPointData(geoObjectVector, id, type, geometry);
            }
            case Geometry.LINESTRING ->
            {
                fillLinestringData(geoObjectVector, id, type, geometry);
            }
            case Geometry.POLYGON ->
            {
                fillPolygonData(geoObjectVector, id, type, geometry);
            }
            case Geometry.MULTIPOLYGON ->
            {
                fillMultipolygonData(geoObjectVector, id, type, geometry);
            }
            default -> {}
        }
    }

    private static void fillPointData(final Vector<GeoObject> geoObjectVector, final String id, final int type,
                                      final PGgeometry geometry) throws SQLException
    {
        String getDataString = geometry.toString();
        org.postgis.Point p = new org.postgis.Point(getDataString);
        geoObjectVector.add(new GeoObject(id, type, new GeoPoint(new Point((int) p.x, (int) p.y))));
    }

    private static void fillLinestringData(final Vector<GeoObject> geoObjectVector, final String id, final int type,
                                           final PGgeometry geometry) throws SQLException
    {
        String geoDataString = geometry.toString();
        LineString l = new LineString(geoDataString);
        Point[] pts = new Point[l.numPoints()];
        for (int i = 0; i < l.numPoints(); i++)
        {
            pts[i] = new Point((int) l.getPoint(i).x, (int) l.getPoint(i).y);
        }
        geoObjectVector.add(new GeoObject(id, type, new GeoLine(pts)));
    }

    private static void fillPolygonData(final Vector<GeoObject> geoObjectVector, final String id, final int type,
                                        final PGgeometry geometry) throws SQLException
    {
        String geoDataString = geometry.toString();
        org.postgis.Polygon p = new org.postgis.Polygon(geoDataString);
        geoObjectVector.add(new GeoObject(id, type, addGeoArea(p)));
    }

    private static void fillMultipolygonData(final Vector<GeoObject> geoObjectVector, final String id, final int type,
                                             final PGgeometry geometry) throws SQLException
    {
        String geoDataString = geometry.toString();
        MultiPolygon polys = new MultiPolygon(geoDataString);
        for (org.postgis.Polygon p : polys.getPolygons())
        {
            geoObjectVector.add(new GeoObject(id, type, addGeoArea(p)));
        }
    }

    private static Polygon addPolygon(final LinearRing ring)
    {
        Polygon poly = new Polygon();
        for (int i = 0; i < ring.numPoints(); i++)
        {
            org.postgis.Point pPG = ring.getPoint(i);
            poly.addPoint((int) pPG.x, (int) pPG.y);
        }
        return poly;
    }

    private static GeoObjectArea addGeoArea(final org.postgis.Polygon polygon)
    {
        GeoObjectArea area = null;
        if (polygon.numRings() >= 1)
        {
            Polygon poly = addPolygon(polygon.getRing(0));
            // Ring 0 --> main polygon ... rest should be holes
            area = new GeoObjectArea(poly);
            if (polygon.numRings() > 1)
            {
                area.holes = new GeoObjectPart[polygon.numRings() - 1];
                for (int i = 1; i < polygon.numRings(); i++)
                {
                    area.holes[i-1] = new GeoObjectArea(addPolygon(polygon.getRing(i)));
                }
            }
        }
        return area;
    }
}
