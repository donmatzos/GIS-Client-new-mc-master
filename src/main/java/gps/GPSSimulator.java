package gps;

import gps.components.view.DataView;
import gps.components.view.DeviationView;
import gps.components.view.SatelliteView;
import gps.components.view.TimeView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import gps.components.nmea.NMEAParser;

public class GPSSimulator extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage)
    {
        NMEAParser parser = new NMEAParser();

        BorderPane root = new BorderPane();
        FlowPane timePane = new FlowPane();
        timePane.setStyle("-fx-background-color: yellow;");
        timePane.setAlignment(Pos.CENTER);
        FlowPane paneData = new FlowPane();
        paneData.setStyle("-fx-background-color: lightblue;");
        root.topProperty().setValue(timePane);
        root.bottomProperty().setValue(paneData);

        GridPane paneCenterGrid = new GridPane();

        StackPane paneSat = new StackPane();

        paneSat.setStyle("-fx-background-color: lightgrey;");
        StackPane paneDev = new StackPane();

        paneDev.setStyle("-fx-background-color: lightgrey;");

        paneCenterGrid.add(paneSat,0,0);
        paneCenterGrid.add(paneDev,1,0);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(50);
        paneCenterGrid.getColumnConstraints().add(column1);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(50);

        paneCenterGrid.getColumnConstraints().add(column2);

        GridPane.setHgrow(paneSat, Priority.ALWAYS);
        GridPane.setVgrow(paneSat, Priority.ALWAYS);
        GridPane.setHgrow(paneDev, Priority.ALWAYS);
        GridPane.setVgrow(paneDev, Priority.ALWAYS);

        root.centerProperty().setValue(paneCenterGrid);

        Scene scene = new Scene(root,640,480);
        TimeView timeView = new TimeView(primaryStage, scene, timePane);
        timeView.init();
        DataView dataView = new DataView(primaryStage, scene, paneData);
        dataView.init();
        DeviationView deviationView = new DeviationView(primaryStage, scene, paneDev);
        deviationView.init();
        SatelliteView satView = new SatelliteView(primaryStage, scene, paneSat);
        satView.init();

        parser.addObserver(timeView);
        parser.addObserver(dataView);
        parser.addObserver(deviationView);
        parser.addObserver(satView);
        parser.start();
        primaryStage.setTitle("GNSS-Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
