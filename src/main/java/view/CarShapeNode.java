package view;

import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Car;

/**
 *
 * @author Liam Bohdjalian
 */
public class CarShapeNode extends Pane {
    
    private final Car car;
    private final Pane frontWheelPane;
    private final Pane rearWheelPane;
    private double wheelRotationAngle = 0.0;

    public CarShapeNode(Car car) {
        this.car = car;
        this.frontWheelPane = new Pane();
        this.rearWheelPane = new Pane();
        
        setPrefSize(110.0, 42.0);
        
        buildCarShapes();
    }
    
    /**
     * Combines all nodes for carShape
     */
    private void buildCarShapes() {
        Color bodyColor = car.getColor();

        Polygon lowerBody = new Polygon(
            0.0, 22.0,
            12.0, 12.0,
            95.0, 12.0,
            110.0, 20.0,
            110.0, 32.0,
            0.0, 32.0
        );
        lowerBody.setFill(bodyColor);
        lowerBody.setStroke(bodyColor.darker());
        lowerBody.setStrokeWidth(1.0);

        Polygon roof = new Polygon(
            22.0, 12.0,
            38.0, 0.0,
            74.0, 0.0,
            88.0, 12.0
        );
        roof.setFill(bodyColor);
        roof.setStroke(bodyColor.darker());
        roof.setStrokeWidth(1.0);

        Rectangle rearWindow = new Rectangle(39.0, 2.0, 17.0, 9.0);
        rearWindow.setFill(Color.LIGHTSKYBLUE);

        Rectangle frontWindow = new Rectangle(58.0, 2.0, 17.0, 9.0);
        frontWindow.setFill(Color.LIGHTSKYBLUE);

        Rectangle headlight = new Rectangle(106.0, 18.0, 4.0, 6.0);
        headlight.setArcWidth(2.0);
        headlight.setArcHeight(2.0);
        headlight.setFill(Color.LIGHTGOLDENRODYELLOW);

        Rectangle taillight = new Rectangle(0.0, 16.0, 3.0, 6.0);
        taillight.setFill(Color.DARKRED);

        Rectangle numberPlate = new Rectangle(42.0, 15.0, 28.0, 14.0);
        numberPlate.setArcWidth(4.0);
        numberPlate.setArcHeight(4.0);
        numberPlate.setFill(Color.WHITE);
        numberPlate.setStroke(Color.DARKGRAY);
        numberPlate.setStrokeWidth(0.8);

        Text numberText = new Text(String.valueOf(car.getNumber()));
        numberText.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        numberText.setFill(Color.BLACK);
        numberText.setTextAlignment(TextAlignment.CENTER);
        numberText.setTextOrigin(VPos.CENTER);
        numberText.setX(42.0 + (28.0 - numberText.getLayoutBounds().getWidth()) / 2.0);
        numberText.setY(22.0);

        BuildWheelShapes(rearWheelPane, 26.0, 32.0, 10.0);
        BuildWheelShapes(frontWheelPane, 84.0, 32.0, 10.0);

        getChildren().addAll(
            lowerBody,
            roof,
            rearWindow,
            frontWindow,
            headlight,
            taillight,
            numberPlate,
            numberText,
            rearWheelPane,
            frontWheelPane
        );
    }

    /**
     * Constructs a wheel unit with outer rubber, inner rim, and spokes.
     *
     * @param wheelPane Pane container for wheel elements.
     * @param centerX   X position on the car body.
     * @param centerY   Y position on the car body.
     * @param radius    Tire radius.
     */
    private void BuildWheelShapes(Pane wheelPane, double centerX, double centerY, double radius) {
        wheelPane.setLayoutX(centerX);
        wheelPane.setLayoutY(centerY);

        Circle tire = new Circle(0, 0, radius);
        tire.setFill(Color.rgb(35, 35, 35));
        tire.setStroke(Color.BLACK);
        tire.setStrokeWidth(1.2);

        Circle rim = new Circle(0, 0, radius * 0.62);
        rim.setFill(Color.LIGHTGRAY);

        Circle centerCap = new Circle(0, 0, 2.0);
        centerCap.setFill(Color.DARKGRAY);

        Line spokeH = new Line(-radius * 0.58, 0, radius * 0.58, 0);
        spokeH.setStroke(Color.BLACK);
        spokeH.setStrokeWidth(1.2);

        Line spokeV = new Line(0, -radius * 0.58, 0, radius * 0.58);
        spokeV.setStroke(Color.BLACK);
        spokeV.setStrokeWidth(1.2);

        wheelPane.getChildren().addAll(tire, rim, spokeH, spokeV, centerCap);
    }
}
