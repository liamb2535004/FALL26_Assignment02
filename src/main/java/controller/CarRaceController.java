package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
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
    private Timeline slideshowTimeline;
    private MediaPlayer mediaPlayer;
    private int currentSlideIndex;

    public CarRaceController(CarRaceModel model, CarRaceView view) {
        this.model = model;
        this.view = view;
        this.raceAnimations = new ArrayList<>();
        this.random = new Random();
        this.currentSlideIndex = 0;
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
            view.updateRaceStatus("⏸️ Race Paused", "Press Start to resume the race.");
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