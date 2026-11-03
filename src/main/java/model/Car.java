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
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Returns a string for car colors
     *
     * @return Color name as a string
     */
    public String getColorName() {
        if (Color.RED.equals(color)) return "Red";
        if (Color.BLUE.equals(color)) return "Blue";
        if (Color.GREEN.equals(color)) return "Green";
        if (Color.YELLOW.equals(color)) return "Yellow";
        return "Custom";
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Moves the car along the x-axis by a given distance
     *
     * @param deltaX The distance to advance along the track
     */
    public void move(double deltaX) {
        this.x += deltaX;
    }

    /**
     * Resets the car position back to starting coordinates
     *
     * @param startX Starting x coordinate.
     * @param startY Starting y coordinate.
     */
    public void resetPosition(double startX, double startY) {
        this.x = startX;
        this.y = startY;
    }

    @Override
    public String toString() {
        return String.format("Car #%d (%s, %s)", number, getColorName(), type);
    }
}
