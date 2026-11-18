package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import model.Car;

/**
 * @author Liam Bohdjalian
 */
public class CarRaceView extends BorderPane {

    public static final double TRACK_WIDTH = 760.0;
    public static final double TRACK_HEIGHT = 270.0;

    private final Pane racetrackPane = new Pane();
    private final List<CarShapeNode> carNodes = new ArrayList<>();

    private final Button startButton = createControlButton("Start");
    private final Button pauseButton = createControlButton("Pause");
    private final Button resetButton = createControlButton("Reset");
    private final Slider speedSlider = new Slider(0.5, 3.0, 1.0);
    private final Label speedValueLabel = createLabel("1.0x", "speed-label");

    private final Label statusHeaderLabel = createLabel("Ready to Race!", "status-header");
    private final Label statusSubMessageLabel = createLabel("Press Start to begin the simulation.", "status-submessage");
    private final VBox leaderboardBox = new VBox(4);

    public CarRaceView(List<Car> cars) {
        URL cssUrl = getClass().getResource("/styles.css");
        if (cssUrl != null) {
            getStylesheets().add(cssUrl.toExternalForm());
        }
        setPadding(new Insets(14));

        setTop(createTopSection(cars));
        setCenter(createBottomSection());
    }

    /**
     * Builds the top racetrack area including start and arrival banners.
     */
    private HBox createTopSection(List<Car> cars) {
        racetrackPane.setPrefSize(TRACK_WIDTH, TRACK_HEIGHT);
        racetrackPane.setMinSize(TRACK_WIDTH, TRACK_HEIGHT);
        racetrackPane.setMaxSize(TRACK_WIDTH, TRACK_HEIGHT);
        racetrackPane.getStyleClass().add("racetrack");

        drawTrackLanes(cars.size());
        populateCarsOnTrack(cars);

        HBox trackSection = new HBox(
                4,
                createVerticalBanner("STARTING"),
                racetrackPane,
                createVerticalBanner("ARRIVAL")
        );
        
        trackSection.setAlignment(Pos.CENTER);
        trackSection.setPadding(new Insets(6, 0, 14, 0));
        return trackSection;
    }

    /**
     * Builds the bottom control panel and race results panel.
     */
    private HBox createBottomSection() {
        HBox buttonRow = new HBox(12, startButton, pauseButton, resetButton);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        speedSlider.setPrefWidth(210);
        speedSlider.setBlockIncrement(0.25);

        HBox sliderRow = new HBox(12, createLabel("Simulation Speed:", "speed-label"), speedSlider, speedValueLabel);
        sliderRow.setAlignment(Pos.CENTER_LEFT);

        VBox controlsCard = new VBox(22, buttonRow, sliderRow);
        controlsCard.setPrefSize(420, 225);
        controlsCard.setPadding(new Insets(18));
        controlsCard.getStyleClass().add("card-panel");

        statusSubMessageLabel.setWrapText(true);

        VBox resultsCard = new VBox(6, statusHeaderLabel, statusSubMessageLabel, leaderboardBox);
        resultsCard.setPrefSize(445, 225);
        resultsCard.setPadding(new Insets(14, 18, 14, 18));
        resultsCard.getStyleClass().add("card-panel");

        HBox bottomSection = new HBox(14, controlsCard, resultsCard);
        bottomSection.setAlignment(Pos.CENTER);
        return bottomSection;
    }

    private VBox createVerticalBanner(String word) {
        VBox banner = new VBox(3);
        banner.setAlignment(Pos.CENTER);
        banner.setPrefSize(46, TRACK_HEIGHT);
        banner.getStyleClass().add("vertical-banner");

        for (char c : word.toCharArray()) {
            banner.getChildren().add(createLabel(String.valueOf(c), "banner-letter"));
        }
        return banner;
    }

    private void drawTrackLanes(int laneCount) {
        double laneHeight = TRACK_HEIGHT / laneCount;
        for (int i = 1; i < laneCount; i++) {
            double y = i * laneHeight;
            Line divider = new Line(10, y, TRACK_WIDTH - 10, y);
            divider.getStyleClass().add("lane-divider");
            divider.getStrokeDashArray().addAll(14.0, 12.0);
            racetrackPane.getChildren().add(divider);
        }
    }

    private void populateCarsOnTrack(List<Car> cars) {
        carNodes.clear();
        double laneHeight = TRACK_HEIGHT / cars.size();

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            car.setX(18.0);
            car.setY((i * laneHeight) + (laneHeight - 42.0) / 2.0);

            CarShapeNode node = new CarShapeNode(car);
            node.setLayoutX(car.getX());
            node.setLayoutY(car.getY());

            carNodes.add(node);
            racetrackPane.getChildren().add(node);
        }
    }

    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    private Button createControlButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(118, 46);
        btn.getStyleClass().add("control-btn");
        return btn;
    }

    public void updateRaceStatus(String header, String subMessage) {
        statusHeaderLabel.setText(header);
        statusSubMessageLabel.setText(subMessage);
    }

    public void displayFinalResults(Car winner, List<Car> rankedCars) {
        updateRaceStatus("Race Finished!", String.format("Congratulations! Car #%d (%s) is the winner!", winner.getNumber(), winner.getColorName()));

        leaderboardBox.getChildren().clear();
        leaderboardBox.getChildren().add(createLabel("Final Results:", "results-title"));

        String[] order = {"1st", "2nd", "3rd", "4th", "5th"};

        for (int i = 0; i < rankedCars.size(); i++) {
            Car car = rankedCars.get(i);
            String place = order[Math.min(i, order.length - 1)];

            Label placeLabel = createLabel(place, "result-place");
            placeLabel.setPrefWidth(40);

            Label carName = createLabel("Car #" + car.getNumber(), "result-car-name");
            carName.setPrefWidth(70);

            Label carColorLabel = createLabel(car.getColorName(), "result-car-color");
            carColorLabel.setTextFill(car.getColor());

            HBox row = new HBox(10, placeLabel, carName, carColorLabel);
            row.setAlignment(Pos.CENTER_LEFT);
            leaderboardBox.getChildren().add(row);
        }
    }

    public void resetResultsPanel() {
        leaderboardBox.getChildren().clear();
        updateRaceStatus("Ready to Race!", "Press Start to begin the simulation.");
    }

    public List<CarShapeNode> getCarNodes() {
        return carNodes;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getPauseButton() {
        return pauseButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Slider getSpeedSlider() {
        return speedSlider;
    }

    public Label getSpeedValueLabel() {
        return speedValueLabel;
    }
}