package view;

import javafx.scene.layout.Pane;
import model.Car;

/**
 *
 * @author Liam Bohdjalian
 */
public class CarShapeNode extends Pane {
    
    private final Car car;
    private final Pane frontWheelGroup;
    private final Pane rearWheelGroup;
    private double wheelRotationAngle = 0.0;

    public CarShapeNode(Car car) {
        this.car = car;
        this.frontWheelGroup = new Pane();
        this.rearWheelGroup = new Pane();
        setPrefSize(110.0, 42.0);
    }
}
