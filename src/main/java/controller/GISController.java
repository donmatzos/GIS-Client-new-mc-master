package controller;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import lombok.Getter;
import model.GISModel;
import server.ServerEnum;
import view.GISView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.sql.SQLException;

import static javafx.scene.Cursor.CLOSED_HAND;
import static javafx.scene.Cursor.DEFAULT;
import static javafx.scene.Cursor.H_RESIZE;
import static javafx.scene.Cursor.OPEN_HAND;

public class GISController
{
    private static final double ZOOM_FACTOR = 1.3;
    private static final int DELTA = 20;
    private static final int ROTATION = 90;

    @Getter
    private final ActionHandler actionHandler = new GISController.ActionHandler();
    @Getter
    private final ChangeHandler changeHandler = new GISController.ChangeHandler();
    @Getter
    private final MouseHandler mouseHandler = new GISController.MouseHandler();
    @Getter
    private final KeyHandler keyHandler = new GISController.KeyHandler();
    @Getter
    private final ScrollHandler scrollHandler = new GISController.ScrollHandler();
    private final GISModel model;
    private final GISView view;

    public GISController(final GISModel model, final GISView view)
    {
        this.model = model;
        this.view = view;
    }

    /*
     ************************************ inner action handler class ************************************
     */

    public class ActionHandler implements EventHandler<ActionEvent>
    {
        private boolean showPoi = false;

        @Override
        public void handle(final ActionEvent actionEvent)
        {
            Object source = actionEvent.getSource();
            if (source instanceof MenuItem item)
            {
                handleMenuItemInput(item.getId());
            }
            else if (source instanceof Button button)
            {
                try
                {
                    handleButtonInput(button.getId());
                }
                catch (final Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        /*
         ************************************ private inner helper methods ************************************
         */

        private void handleMenuItemInput(final String itemId)
        {
            switch (itemId)
            {
                case GISView.LINZ_SERVER_ID -> handleServerConnection("CONNECTING TO LINZ", ServerEnum.LINZ);
                case GISView.HGB_SERVER_ID -> handleServerConnection("CONNECTING TO HGB", ServerEnum.HGB);
                case GISView.OSM_SERVER_ID -> handleServerConnection("CONNECTING TO OSM", ServerEnum.OSM);
                default -> {}
            }
        }

        private void handleServerConnection(final String log, final ServerEnum server)
        {
            System.out.println(log);
            model.connectToServer(server);
        }

        private void handleButtonInput(final String buttonId) throws SQLException
        {
            switch (buttonId)
            {
                case GISView.LOAD_BTN_ID -> handleLoadButton();
                case GISView.ZTF_BTN_ID -> handleZoomToFit();
                case GISView.ZOOM_IN_BTN_ID -> handleZoom("ZOOM_IN PRESSED", ZOOM_FACTOR);
                case GISView.ZOOM_OUT_BTN_ID -> handleZoom("ZOOM_OUT PRESSED", 1 / ZOOM_FACTOR);
                case GISView.SCROLL_UP_BTN_ID -> handleVerticalScrolling("SCROLL_UP PRESSED", DELTA);
                case GISView.SCROLL_DOWN_BTN_ID -> handleVerticalScrolling("SCROLL_DOWN PRESSED", -DELTA);
                case GISView.SCROLL_LEFT_BTN_ID -> handleHorizontalScrolling("SCROLL_LEFT PRESSED", DELTA);
                case GISView.SCROLL_RIGHT_BTN_ID -> handleHorizontalScrolling("SCROLL_RIGHT PRESSED", -DELTA);
                case GISView.ROTATE_LEFT_BTN_ID -> handleRotation("ROTATE_LEFT PRESSED", Math.toRadians(ROTATION));
                case GISView.ROTATE_RIGHT_BTN_ID -> handleRotation("ROTATE_RIGHT PRESSED", -Math.toRadians(ROTATION));
                case GISView.POI_ID -> handlePoi();
                case GISView.STICKY_ID -> handleSticky();
                default -> {}
            }
        }

        private void handleLoadButton() throws SQLException
        {
            System.out.println("LOAD PRESSED");
            model.repaint();
        }

        private void handleZoomToFit() throws SQLException
        {
            System.out.println("ZTF PRESSED");
            model.zoomToFit();
            model.repaint();
        }

        private void handleZoom(final String message, final double zoomFactor) throws SQLException
        {
            System.out.println(message);
            model.zoom(zoomFactor);
            model.repaint();
        }

        private void handleVerticalScrolling(final String message, final int delta) throws SQLException
        {
            System.out.println(message);
            model.scrollVertical(delta);
            model.repaint();
        }

        private void handleHorizontalScrolling(final String message, final int delta) throws SQLException
        {
            System.out.println(message);
            model.scrollHorizontal(delta);
            model.repaint();
        }

        private void handleRotation(final String message, final double rotation) throws SQLException
        {
            System.out.println(message);
            model.rotate(rotation);
            model.repaint();
        }

        private void handlePoi() throws SQLException
        {
            if (!showPoi)
            {
                model.loadPoiData();
            }
            else
            {
                model.emptyPoi();
            }
            model.repaint();
            showPoi = !showPoi;
        }

        private void handleSticky() throws SQLException
        {
            model.setWorldSticky();
            model.repaint();
        }
    }

    /*
     ************************************ inner change handler class ************************************
     */

    public class ChangeHandler implements ChangeListener<Number>
    {
        @Override
        public void changed(final ObservableValue<? extends Number> observable, final Number oldValue,
                            final Number newValue)
        {
            if (observable instanceof ReadOnlyDoubleProperty doubleProperty)
            {
                handleChange(doubleProperty);
            }
        }

        /*
         ************************************ private inner helper methods ************************************
         */

        private void handleChange(final ReadOnlyDoubleProperty doubleProperty)
        {
            double value = doubleProperty.doubleValue();
            String name = doubleProperty.getName();
            if (name.equalsIgnoreCase("width"))
            {
                model.setWidth((int) value);
            }
            else if (name.equalsIgnoreCase("height"))
            {
                model.setHeight((int) value);
            }
            try
            {
                model.repaint();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    /*
     ************************************ inner mouse handler class ************************************
     */

    public class MouseHandler implements EventHandler<MouseEvent> {
        Point2D.Double coordinatesPressed = null;
        Point2D.Double coordinatesReleased = null;
        Point2D.Double currentCoordinates = null;
        Rectangle zoomRectangle = null;
        boolean ctrlIsPressed = false;

        @Override
        public void handle(final MouseEvent mouseEvent) {
            EventType<MouseEvent> type = (EventType<MouseEvent>) mouseEvent.getEventType();
            switch (type.toString())
            {
                case "MOUSE_PRESSED" -> handlePressed(mouseEvent);
                case "MOUSE_RELEASED" -> handleReleased(mouseEvent);
                case "MOUSE_DRAGGED" -> handleDragged(mouseEvent);
                default -> {}
            }
        }

        /*
         ************************************ private inner helper methods ************************************
         */

        private void handlePressed(final MouseEvent mouseEvent)
        {
            coordinatesPressed = new Point2D.Double(mouseEvent.getX(), mouseEvent.getY());
            currentCoordinates = new Point2D.Double(mouseEvent.getX(), mouseEvent.getY());
            handlePressedEvent(mouseEvent);
            handleClipboard(mouseEvent);
        }

        private void handlePressedEvent(final MouseEvent mouseEvent)
        {
            if (mouseEvent.getButton() == MouseButton.PRIMARY)
            {
                view.scene.setCursor(H_RESIZE);
            }
            if (mouseEvent.getButton() == MouseButton.SECONDARY)
            {
                view.scene.setCursor(OPEN_HAND);
            }
        }

        private void handleReleased(final MouseEvent mouseEvent)
        {
            coordinatesReleased = new Point2D.Double(mouseEvent.getX(), mouseEvent.getY());
            handleReleasedEvent(mouseEvent);
            try
            {
                model.repaint();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            view.scene.setCursor(DEFAULT);
            view.clearXOR();
        }

        private void handleReleasedEvent(final MouseEvent mouseEvent)
        {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && zoomRectangle != null && zoomRectangle.width > 80)
            {
                model.zoomRect(zoomRectangle);
                zoomRectangle = null;
            }
            if (mouseEvent.getButton() == MouseButton.SECONDARY)
            {
                model.scrollHorizontal((int) (coordinatesReleased.getX() - coordinatesPressed.getX()));
                model.scrollVertical((int) (coordinatesReleased.getY() - coordinatesPressed.getY()));
            }
        }

        private void handleDragged(final MouseEvent mouseEvent)
        {
            if (mouseEvent.getButton() == MouseButton.PRIMARY)
            {
                zoomRectangle = new Rectangle(new Point((int) coordinatesPressed.getX(), (int) coordinatesPressed.getY()));
                zoomRectangle.add(new Point((int) mouseEvent.getX(), (int) mouseEvent.getY()));
                view.drawXOR(zoomRectangle);
            }
            if (mouseEvent.getButton() == MouseButton.SECONDARY)
            {
                view.scene.setCursor(CLOSED_HAND);
                double deltaX = (mouseEvent.getX() - currentCoordinates.getX());
                double deltaY = (mouseEvent.getY() - currentCoordinates.getY());
                view.translate(deltaX, deltaY);
                currentCoordinates = new Point2D.Double(mouseEvent.getX(), mouseEvent.getY());
            }
        }

        private void handleClipboard(final MouseEvent mouseEvent)
        {
            StringBuilder builder = new StringBuilder();
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = clip.getContents(mouseEvent);
            handleIsControlDown(mouseEvent, builder, transferable);
            Point p = model.getMapPoint(new Point((int)mouseEvent.getX(), (int)mouseEvent.getY()));
            builder.append("(").append(p.x).append(",").append(p.y).append(")\n");
            clip.setContents(new StringSelection(builder.toString()), new StringSelection(builder.toString()));
        }

        private void handleIsControlDown(final MouseEvent mouseEvent, final StringBuilder builder,
                                         final Transferable transferable)
        {
            if (mouseEvent.isControlDown())
            {
                if (ctrlIsPressed)
                {
                    try
                    {
                        String oldClip = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                        builder.append(oldClip);
                    }
                    catch (UnsupportedFlavorException | IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                ctrlIsPressed = true;
            }
            else
            {
                ctrlIsPressed = false;
            }
        }

    }

    /*
     ************************************ inner key handler class ************************************
     */

    public class KeyHandler implements EventHandler<KeyEvent>
    {
        @Override
        public void handle(final KeyEvent keyEvent)
        {
            EventType<KeyEvent> type = keyEvent.getEventType();
            Object source = keyEvent.getSource();
            if (source instanceof TextField textField)
            {
                handleTextFieldInput(keyEvent, type, textField);
            }
        }

        /*
         ************************************ private inner helper methods ************************************
         */

        private void handleTextFieldInput(final KeyEvent keyEvent, final EventType<KeyEvent> type, final TextField textField)
        {
            if(textField.getId().equals(GISView.SCALING_ID))
            {
                if (type == KeyEvent.KEY_PRESSED && keyEvent.getCode() == KeyCode.ENTER)
                {
                    try
                    {
                        int scaleFactor = Integer.parseInt(textField.getText());
                        model.zoomToScale(scaleFactor);
                        model.repaint();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*
     ************************************ inner scroll handler class ************************************
     */

    public class ScrollHandler implements EventHandler<ScrollEvent> {

        @Override
        public void handle(final ScrollEvent scrollEvent)
        {
            handleScrollEvent(scrollEvent);
            try
            {
                model.repaint();
            }
            catch (final SQLException e)
            {
                e.printStackTrace();
            }

        }

        /*
         ************************************ private inner helper methods ************************************
         */

        private void handleScrollEvent(final ScrollEvent scrollEvent)
        {
            if (scrollEvent.getDeltaY() > 0)
            {
                model.zoom(ZOOM_FACTOR);
            }
            else if (scrollEvent.getDeltaY() < 0)
            {
                model.zoom(1/ZOOM_FACTOR);
            }
        }
    }
}
