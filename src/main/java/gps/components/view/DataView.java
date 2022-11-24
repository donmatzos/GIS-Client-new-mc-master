package gps.components.view;

import gps.components.nmea.NMEAInfo;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DataView extends BaseView
{
    private final static String ALT_TEXT_ID = "TEXT_ALT";
    private final static String LAT_TEXT_ID = "TEXT_LAT";
    private final static String LON_TEXT_ID = "TEXT_LON";
    private final static String PDOP_TEXT_ID = "TEXT_PDOP";
    private final static String HDOP_TEXT_ID = "TEXT_HDOP";
    private final static String VDOP_TEXT_ID = "TEXT_VDOP";

    public DataView(final Stage stage, final Scene scene, final Pane root)
    {
        super(stage, scene, root);
    }

    @Override
    public void init()
    {
        GridPane grid = new GridPane();

        configureDataViewGrid(grid);

        root.getChildren().add(grid);
    }

    @Override
    public void update(final NMEAInfo info)
    {
        if (info != null)
        {
            Text textLat = (Text) scene.lookup("#" + LAT_TEXT_ID);
            textLat.setText(getDoubleMax6String(info.getLatitude()));

            Text textLon = (Text) scene.lookup("#" + LON_TEXT_ID);
            textLon.setText(getDoubleMax6String(info.getLongitude()));

            Text textAlt = (Text) scene.lookup("#" + ALT_TEXT_ID);
            textAlt.setText(getDoubleMax6String(info.getHeight()));

            Text textPdop = (Text) scene.lookup("#" + PDOP_TEXT_ID);
            textPdop.setText(String.valueOf(info.getPdop()));

            Text textHdop = (Text) scene.lookup("#" + HDOP_TEXT_ID);
            textHdop.setText(String.valueOf(info.getHdop()));

            Text textVdop = (Text) scene.lookup("#" + VDOP_TEXT_ID);
            textVdop.setText(String.valueOf(info.getVdop()));
        }
    }

    /*
     ************************************ private helper methods ************************************
     */

    private void configureDataViewGrid(final GridPane grid)
    {
        configureText(LAT_TEXT_ID, "Latitude: ", grid, 0, 0, 1, 0);
        configureText(LON_TEXT_ID, "Longitude: ", grid, 0, 1, 1, 1);
        configureText(ALT_TEXT_ID, "Altitude: ", grid, 0, 2, 1, 2);
        configureText(PDOP_TEXT_ID, "PDOP: ", grid, 2, 0, 3, 0);
        configureText(HDOP_TEXT_ID, "HDOP: ", grid, 2, 1, 3, 1);
        configureText(VDOP_TEXT_ID, "VDOP: ", grid, 2, 2, 3, 2);
        grid.setHgap(10);
    }

    private void configureText(final String textId, final String text, final GridPane grid,
                               final int colIndex, final int colIndex2, final int colIndex3, final int colIndex4)
    {
        Text id = new Text();
        id.setId(textId);
        Text title = new Text(text);
        title.setStyle("-fx-font-weight: bold");
        grid.add(title, colIndex, colIndex2);
        grid.add(id, colIndex3, colIndex4);
    }

    private String getDoubleMax6String(double value)
    {
        return String.valueOf(round(value,6));
    }

    private double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(places, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

}
