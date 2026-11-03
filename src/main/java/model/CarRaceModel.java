package model;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 *
 * @author Liam Bohdjalian
 */
public class CarRaceModel {
    public enum RaceState {
        READY,
        RUNNING,
        PAUSED,
        FINISHED
    }

    private final List<Car> cars;
    private final List<Car> finishedCars;
    private RaceState state;
    private double trackLength;
    private double startX;

    public CarRaceModel() {
        this.cars = new ArrayList<>();
        this.finishedCars = new ArrayList<>();
        this.state = RaceState.READY;
        this.startX = 50.0;
        this.trackLength = 700.0;
        initializeDefaultCars();
    }
    
    /**
     * Initializes the 4 cars with each color, number, and lane
     */
    public void initializeCars() {
        cars.clear();
        finishedCars.clear();

        cars.add(new Car("Sedan", 23, Color.RED, 3.2, startX, 40.0));
        cars.add(new Car("Coupe", 45, Color.BLUE, 3.6, startX, 100.0));
        cars.add(new Car("Hatchback", 78, Color.GREEN, 3.4, startX, 160.0));
        cars.add(new Car("Sports", 88, Color.YELLOW, 3.8, startX, 220.0));

        this.state = RaceState.READY;
    }
}
