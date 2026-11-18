package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import model.Car;
import model.CarRaceModel;
import view.CarRaceView;
import view.CarShapeNode;

/**
 * @author Liam Bohdjalian
 */
public class CarRaceController {

    private final CarRaceModel model;
    private final CarRaceView view;
    private final List<ParallelTransition> raceAnimations;
    private final Random random;

    private MediaPlayer mediaPlayer;

    public CarRaceController(CarRaceModel model, CarRaceView view) {
        this.model = model;
        this.view = view;
        this.raceAnimations = new ArrayList<>();
        this.random = new Random();

        initializeAudio();
        bindControlEvents();
    }

    /**
     * Initializes the background audio player from the resource file.
     */
    private void initializeAudio() {
        try {
            URL audioUrl = getClass().getResource("/audio/race_theme.mp3");
            if (audioUrl != null) {
                Media sound = new Media(audioUrl.toExternalForm());
                mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setCycleCount(1);
            }
        } catch (Exception e) {
            System.err.println("Audio file could not be loaded: " + e.getMessage());
        }
    }

    /**
     * Attaches event handlers to Start, Pause, Reset buttons, and the speed slider.
     */
    private void bindControlEvents() {
        view.getStartButton().setOnAction(e -> startRace());
        view.getPauseButton().setOnAction(e -> pauseRace());
        view.getResetButton().setOnAction(e -> resetRace());

        view.getSpeedSlider().valueProperty().addListener((obs, oldVal, newVal) -> {
            double rate = newVal.doubleValue();
            view.getSpeedValueLabel().setText(String.format("%.1fx", rate));
            for (ParallelTransition anim : raceAnimations) {
                anim.setRate(rate);
            }
        });
    }

    /**
     * Starts or resumes the race animations and background audio.
     */
    public void startRace() {
        if (model.getState() == CarRaceModel.RaceState.FINISHED) {
            return;
        }

        if (mediaPlayer != null && mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            mediaPlayer.play();
        }

        if (model.getState() == CarRaceModel.RaceState.PAUSED) {
            model.start();
            view.updateRaceStatus("Race in Progress!", "Cars are racing toward the arrival line.");
            for (ParallelTransition anim : raceAnimations) {
                if (anim.getStatus() == Animation.Status.PAUSED) {
                    anim.play();
                }
            }
            return;
        }

        if (model.getState() == CarRaceModel.RaceState.READY) {
            model.start();
            view.updateRaceStatus("Race in Progress!", "Cars are racing toward the arrival line.");
            buildAndPlayRaceAnimations();
        }
    }

    /**
     * Builds TranslateTransition and RotateTransition animations for every car
     * and plays them simultaneously.
     */
    private void buildAndPlayRaceAnimations() {
        raceAnimations.clear();
        double currentRate = view.getSpeedSlider().getValue();
        double finishDistance = CarRaceView.TRACK_WIDTH - 135.0;

        double avgSpeed = model.getCars().stream()
                .mapToDouble(Car::getSpeed)
                .average()
                .orElse(100.0);

        double targetBaseSeconds = 10.0;

        for (CarShapeNode carNode : view.getCarNodes()) {
            Car car = carNode.getCar();

            double speedRatio = avgSpeed / Math.max(car.getSpeed(), 1.0);
            double randomFactor = 0.88 + (0.24 * random.nextDouble());
            double durationSeconds = targetBaseSeconds * speedRatio * randomFactor;

            TranslateTransition moveTransition = new TranslateTransition(Duration.seconds(durationSeconds), carNode);
            moveTransition.setFromX(0.0);
            moveTransition.setToX(finishDistance);
            moveTransition.setInterpolator(Interpolator.LINEAR);

            carNode.translateXProperty().addListener((obs, oldVal, newVal) -> {
                car.setX(model.getStartX() + newVal.doubleValue());
            });

            RotateTransition frontWheelSpin = new RotateTransition(Duration.seconds(0.18), carNode.getFrontWheelPane());
            frontWheelSpin.setByAngle(360.0);
            frontWheelSpin.setCycleCount(Animation.INDEFINITE);
            frontWheelSpin.setInterpolator(Interpolator.LINEAR);

            RotateTransition rearWheelSpin = new RotateTransition(Duration.seconds(0.18), carNode.getRearWheelPane());
            rearWheelSpin.setByAngle(360.0);
            rearWheelSpin.setCycleCount(Animation.INDEFINITE);
            rearWheelSpin.setInterpolator(Interpolator.LINEAR);

            ParallelTransition carAnimation = new ParallelTransition(moveTransition, frontWheelSpin, rearWheelSpin);
            carAnimation.setRate(currentRate);

            moveTransition.setOnFinished(event -> {
                carAnimation.stop();
                carNode.setTranslateX(finishDistance);
                handleCarFinished(car);
            });

            raceAnimations.add(carAnimation);
            carAnimation.play();
        }
    }

    /**
     * Records finishing order in the model and displays results once all cars finish.
     *
     * @param car The car that just crossed the finish line.
     */
    private void handleCarFinished(Car car) {
        model.registerFinishedCar(car);

        if (model.isFinished()) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            view.displayFinalResults(model.getWinner(), model.getFinalResults());
        }
    }

    /**
     * Pauses all active car animations and pauses the race audio.
     */
    public void pauseRace() {
        if (model.getState() == CarRaceModel.RaceState.RUNNING) {
            model.pause();
            view.updateRaceStatus("Race Paused", "Press Start to resume the race.");
            for (ParallelTransition anim : raceAnimations) {
                if (anim.getStatus() == Animation.Status.RUNNING) {
                    anim.pause();
                }
            }
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }
    }

    /**
     * Stops all animations and audio, resets car positions to the start line,
     * and clears the results board.
     */
    public void resetRace() {
        for (ParallelTransition anim : raceAnimations) {
            anim.stop();
        }
        raceAnimations.clear();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.seek(Duration.ZERO);
        }

        for (CarShapeNode carNode : view.getCarNodes()) {
            carNode.reset();
        }

        model.resetRace();
        view.resetResultsPanel();
    }
}