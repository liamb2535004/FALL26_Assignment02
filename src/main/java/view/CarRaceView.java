package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import model.Car;

/**
 * @author Liam Bohdjalian
 */
public class CarRaceView extends BorderPane {

    public static final double TRACK_WIDTH = 760.0;
    public static final double TRACK_HEIGHT = 270.0;

    private final Pane racetrackPane = new Pane();
    private final List<CarShapeNode> carNodes = new ArrayList<>();

    public CarRaceView(List<Car> cars) {
        URL cssUrl = getClass().getResource("/styles.css");
        if (cssUrl != null) {
            getStylesheets().add(cssUrl.toExternalForm());
        }
        setPadding(new Insets(14));
    }
}