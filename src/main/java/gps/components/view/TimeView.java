package gps.components.view;

import gps.components.nmea.NMEAInfo;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TimeView extends BaseView
{
    private static final String TIME_ID = "TIME";

    public TimeView(Stage stage, Scene scene, Pane root)
    {
        super(stage, scene, root);
    }


    @Override
    public void init()
    {
        Text textTime = new Text();
        textTime.setId(TIME_ID);
        root.getChildren().add(textTime);
    }

    @Override
    public void update(final NMEAInfo info)
    {
        Text textTime =(Text) scene.lookup("#" + TIME_ID);
        if (info != null)
        {
            textTime.setText(info.getTimeString());
        }
    }
}
