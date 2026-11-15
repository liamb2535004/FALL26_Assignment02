package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.scene.media.MediaPlayer;
import model.CarRaceModel;
import view.CarRaceView;

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
}