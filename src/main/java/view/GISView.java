package view;

import controller.GISController;
import model.GISModel;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import utils.BufferedImageUtils;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class GISView extends Application implements DataObserver
{
    public static final String CANVAS_ID = "CANVAS";
    public static final String LINZ_SERVER_ID = "SERVER_A";
    public static final String HGB_SERVER_ID = "SERVER_B";
    public static final String OSM_SERVER_ID = "SERVER_C";
    public static final String LOAD_BTN_ID = "LOAD";
    public static final String ZTF_BTN_ID = "ZTF";
    public static final String ZOOM_IN_BTN_ID = "ZOOM_IN";
    public static final String ZOOM_OUT_BTN_ID = "ZOOM_OUT";
    public static final String SCROLL_UP_BTN_ID = "SCROLL_UP";
    public static final String SCROLL_DOWN_BTN_ID = "SCROLL_DOWN";
    public static final String SCROLL_LEFT_BTN_ID = "SCROLL_LEFT";
    public static final String SCROLL_RIGHT_BTN_ID = "SCROLL_RIGHT";
    public static final String ROTATE_LEFT_BTN_ID = "ROTATE_LEFT";
    public static final String ROTATE_RIGHT_BTN_ID = "ROTATE_RIGHT";
    public static final String OVERLAY_ID = "OVERLAY";
    public static final String SCALING_ID = "SCALE";
    public static final String POI_ID = "POI";
    public static final String STICKY_ID = "STICKY";

    private GISController controller = null;
    private BufferedImage image;
    public Scene scene = null;
    private final int width = 640;
    private final int height = 480;
    private boolean startDrag = false;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void init()
    {
        GISModel model = new GISModel();
        model.setWidth(width);
        model.setHeight(height);
        controller = new GISController(model,this);
        model.addMapObserver(this);
    }

    @Override
    public void start(final Stage primaryStage)
    {
        BorderPane root = new BorderPane();
        FlowPane buttonPane = setupButtonPane();
        MenuBar menuBar = setupMenuBar();
        StackPane canvasPane = setupCanvasPane();

        finalizeSetup(primaryStage, root, buttonPane, menuBar, canvasPane);
    }


    public void repaint()
    {
        Image drawImage = BufferedImageUtils.convertToFxImage(image);
        Canvas canvas = (Canvas) scene.lookup("#"+CANVAS_ID);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        graphicsContext.restore();
        startDrag = false;
        graphicsContext.drawImage(drawImage,0,0);
    }

    @Override
    public void update(final BufferedImage image)
    {
        this.image = image;
        repaint();
    }

    @Override
    public void updateScaling(final int scaleFactor)
    {
        TextField scaleTextField = (TextField)scene.lookup("#" + GISView.SCALING_ID);
        scaleTextField.setText("1:" + scaleFactor);
    }

    public void drawXOR(final Rectangle rectangle)
    {
        Canvas canvas = (Canvas)scene.lookup("#" + OVERLAY_ID);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0,0,scene.getWidth(),scene.getHeight());
        graphicsContext.strokeRect(rectangle.getMinX(), rectangle.getMinY(), rectangle.getWidth(), rectangle.getHeight());
    }

    public void clearXOR()
    {
        Canvas canvas = (Canvas)scene.lookup("#" + OVERLAY_ID);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, scene.getWidth(), scene.getHeight());
    }

    public void translate(final double deltaX, final double deltaY)
    {
        Canvas canvas = (Canvas)scene.lookup("#" + CANVAS_ID);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        int width = (int)canvas.getWidth();
        int height = (int)canvas.getHeight();
        int delta = 2;
        executeTranslateGraphicsOperations(deltaX, deltaY, graphicsContext, width, height, delta);
    }

    /*
     ************************************ private helper methods ************************************
     */

    private FlowPane setupButtonPane()
    {
        FlowPane buttonPane = new FlowPane();
        buttonPane.setStyle("-fx-background-color:#335170; -fx-padding:5");

        createAndSetBasicButtons(buttonPane);
        createAndSetNavigationButtons(buttonPane);
        createAndSetScaleTextField(buttonPane);
        //point of interest button
        createAndSetMiscButton("POI", POI_ID, buttonPane);
        //sticky
        createAndSetMiscButton("Sticky", STICKY_ID, buttonPane);
        //rotation buttons TODO get rotation working
//        createAndSetRotateButtons(buttonPane);
        return buttonPane;
    }

    private void createAndSetBasicButtons(final FlowPane buttonPane)
    {
        Button btnLoad = createBasicButton("Load", LOAD_BTN_ID, 10, 50);
        Button zoomToFit = createBasicButton("ZTF", ZTF_BTN_ID, 10, 50);
        Button zoomIn = createBasicButton("+", ZOOM_IN_BTN_ID, 6, 28);
        Button zoomOut = createBasicButton("-", ZOOM_OUT_BTN_ID, 6, 28);

        finalizeBasicButtons(buttonPane, btnLoad, zoomToFit, zoomIn, zoomOut);
    }

    private Button createBasicButton(final String text, final String id, final int padding, final int prefWidth)
    {
        Button btnLoad = new Button(text);
        btnLoad.setId(id);
        btnLoad.setPadding(new Insets(padding));
        btnLoad.setPrefWidth(prefWidth);
        return btnLoad;
    }

    private void finalizeBasicButtons(final FlowPane buttonPane, Button ... buttons)
    {
        for (Button button : buttons)
        {
            button.setOnAction(controller.getActionHandler());
            buttonPane.getChildren().add(button);
        }
        buttonPane.setHgap(15);
    }

    private void createAndSetNavigationButtons(final FlowPane buttonPane)
    {
        GridPane navigationPane = new GridPane();

        Button btnNorth = createNavigationButton("N", SCROLL_UP_BTN_ID);
        Button btnSouth = createNavigationButton("S", SCROLL_DOWN_BTN_ID);
        Button btnEast = createNavigationButton("E", SCROLL_RIGHT_BTN_ID);
        Button btnWest = createNavigationButton("W", SCROLL_LEFT_BTN_ID);

        finalizeNavigationButtons(buttonPane, navigationPane, btnNorth, btnSouth, btnEast, btnWest);
    }

    private Button createNavigationButton(final String text, final String id)
    {
        Button btnNorth = new Button(text);
        btnNorth.setId(id);
        btnNorth.setPrefWidth(28);
        btnNorth.setOnAction(controller.getActionHandler());
        return btnNorth;
    }

    private void finalizeNavigationButtons(final FlowPane buttonPane, final GridPane navigationPane,
                                           final Button btnNorth, final Button btnSouth,
                                           final Button btnEast, final Button btnWest)
    {
        navigationPane.add(btnNorth,1,0,1,1);
        navigationPane.add(btnEast,2,1,1,1);
        navigationPane.add(btnWest,0,1,1,1);
        navigationPane.add(btnSouth,1,2,1,1);
        buttonPane.getChildren().add(navigationPane);
    }

    private void createAndSetScaleTextField(final FlowPane buttonPane)
    {
        TextField scaleField = new TextField();
        scaleField.setId(SCALING_ID);
        scaleField.setPromptText("1 : unknown");
        scaleField.setPrefWidth(120);
        scaleField.setAlignment(Pos.CENTER);
        scaleField.setOnKeyPressed(controller.getKeyHandler());
        buttonPane.getChildren().add(scaleField);
    }

    private void createAndSetMiscButton(final String POI, final String idPoi, final FlowPane buttonPane)
    {
        Button poi = new Button(POI);
        poi.setId(idPoi);
        poi.setPadding(new Insets(10));
        poi.setOnAction(controller.getActionHandler());
        buttonPane.getChildren().add(poi);
    }

    private void createAndSetRotateButtons(final FlowPane buttonPane)
    {
        Button rotateLeft = createRotateButton("R Left", ROTATE_LEFT_BTN_ID);
        Button rotateRight = createRotateButton("R Right", ROTATE_RIGHT_BTN_ID);

        buttonPane.getChildren().add(rotateLeft);
        buttonPane.getChildren().add(rotateRight);
    }

    private Button createRotateButton(final String R_Left, final String idBtnRotateLeft)
    {
        Button rotateLeft = new Button(R_Left);
        rotateLeft.setId(idBtnRotateLeft);
        rotateLeft.setPrefWidth(65);
        rotateLeft.setPadding(new Insets(6));
        rotateLeft.setOnAction(controller.getActionHandler());
        return rotateLeft;
    }

    private MenuBar setupMenuBar()
    {
        MenuBar menuBar = new MenuBar();

        Menu serverMenu = new Menu("Connect...");
        RadioMenuItem linzMenuItem = createMenuItem("Linz Server", LINZ_SERVER_ID);
        RadioMenuItem hgbMenuItem = createMenuItem("HGB Server", HGB_SERVER_ID);
        RadioMenuItem osmMenuItem = createMenuItem("OSM Server", OSM_SERVER_ID);
        hgbMenuItem.setSelected(true);
        finalizeMenuItems(serverMenu, linzMenuItem, hgbMenuItem, osmMenuItem);

        menuBar.getMenus().add(serverMenu);
        return menuBar;
    }

    private void finalizeMenuItems(final Menu menu, final RadioMenuItem ... menuItems)
    {
        ToggleGroup toggleGroup = new ToggleGroup();
        for (RadioMenuItem item : menuItems)
        {
            toggleGroup.getToggles().add(item);
            menu.getItems().add(item);
        }
    }

    private RadioMenuItem createMenuItem(final String text, final String id)
    {
        RadioMenuItem linzMenuItem = new RadioMenuItem(text);
        linzMenuItem.setId(id);
        linzMenuItem.setOnAction(controller.getActionHandler());
        return linzMenuItem;
    }

    private StackPane setupCanvasPane()
    {
        StackPane canvasPane = new StackPane();
        applyCanvasSetup(canvasPane);
        createOverlay(canvasPane);
        return canvasPane;
    }

    private void applyCanvasSetup(final StackPane canvasPane)
    {
        canvasPane.setMinSize(0,0);
        canvasPane.setStyle("-fx-background-color:white");
        Canvas canvas = new Canvas();
        canvas.setId(CANVAS_ID);
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());
        canvasPane.getChildren().add(canvas);
        setActionHandlers(canvasPane);
    }

    private void createOverlay(final StackPane canvasPane)
    {
        Canvas overlay = new Canvas();
        overlay.setId(OVERLAY_ID);
        overlay.widthProperty().bind(canvasPane.widthProperty());
        overlay.heightProperty().bind(canvasPane.heightProperty());
        overlay.setOnMouseDragged(controller.getMouseHandler());
        canvasPane.getChildren().add(overlay);
    }


    private void setActionHandlers(final StackPane canvasPane)
    {
        canvasPane.setOnMousePressed(controller.getMouseHandler());
        canvasPane.setOnMouseReleased(controller.getMouseHandler());
        canvasPane.widthProperty().addListener(controller.getChangeHandler());
        canvasPane.heightProperty().addListener(controller.getChangeHandler());
        canvasPane.setOnScroll(controller.getScrollHandler());
    }

    private void finalizeSetup(final Stage primaryStage, final BorderPane root, final FlowPane buttonPane,
                               final MenuBar menuBar, final StackPane canvasPane)
    {
        root.setTop(menuBar);
        root.setBottom(buttonPane);
        root.setCenter(canvasPane);
        primaryStage.setTitle("GIS-Client");
        scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void executeTranslateGraphicsOperations(final double deltaX, final double deltaY,
                                                    final GraphicsContext graphicsContext, final int width,
                                                    final int height, final int delta)
    {
        graphicsContext.clearRect(0, delta, width, height);
        graphicsContext.clearRect(0, height - delta, width, height);

        if (!startDrag)
        {
            startDrag = true;
            graphicsContext.save();
        }

        graphicsContext.translate(deltaX, deltaY);
        Image fxImage = BufferedImageUtils.convertToFxImage(image);
        graphicsContext.drawImage(fxImage,0,0);
    }
}
