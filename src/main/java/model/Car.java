package model;

import javafx.scene.paint.Color;

/**
 *
 * @author Liam Bohdjalian
 */
public class Car {
    private String type;
    private int number;
    private Color color;
    private double speed;
    private double x;
    private double y;

    public Car(String type, int number, Color color, double speed, double initialX, double initialY) {
        this.type = type;
        this.number = number;
        this.color = color;
        this.speed = speed;
        this.x = initialX;
        this.y = initialY;
    }
}
