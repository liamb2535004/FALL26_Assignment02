package model;

import java.util.ArrayList;
import java.util.Collections;
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
        initializeCars();
    }
    
    /**
     * Initializes the 4 cars with each color, number, and lane
     */
    public void initializeCars() {
        cars.clear();
        finishedCars.clear();

        cars.add(new Car("red car", 23, Color.RED, 3.2, startX, 40.0));
        cars.add(new Car("blue car", 45, Color.BLUE, 3.6, startX, 100.0));
        cars.add(new Car("green car", 78, Color.GREEN, 3.4, startX, 160.0));
        cars.add(new Car("yellow car", 88, Color.YELLOW, 3.8, startX, 220.0));

        this.state = RaceState.READY;
    }
    
    /**
     * Registers a car as having crossed the finish line in arrival order.
     *
     * @param car The car that reached the finish line
     */
    public void registerFinishedCar(Car car) {
        if (!finishedCars.contains(car)) {
            finishedCars.add(car);
            if (finishedCars.size() == cars.size()) {
                this.state = RaceState.FINISHED;
            }
        }
    }
    
    /**
     * Resets the entire race simulation.
     */
    public void resetRace() {
        finishedCars.clear();
        for (Car car : cars) {
            car.setX(startX);
        }
        this.state = RaceState.READY;
    }

    /**
     * Sets the state to RUNNING if currently READY or PAUSED.
     */
    public void start() {
        if (state == RaceState.READY || state == RaceState.PAUSED) {
            this.state = RaceState.RUNNING;
        }
    }

    /**
     * Pauses the simulation if running.
     */
    public void pause() {
        if (state == RaceState.RUNNING) {
            this.state = RaceState.PAUSED;
        }
    }
    
    public Car getWinner() {
        if (!finishedCars.isEmpty()) {
            return finishedCars.get(0);
        }
        return null;
    }

    public List<Car> getFinalResults() {
        return Collections.unmodifiableList(finishedCars);
    }

    public List<Car> getCars() {
        return Collections.unmodifiableList(cars);
    }

    public RaceState getState() {
        return state;
    }

    public void setState(RaceState state) {
        this.state = state;
    }

    public double getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(double trackLength) {
        this.trackLength = trackLength;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public boolean isFinished() {
        return state == RaceState.FINISHED;
    }
}
