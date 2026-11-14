package liamb.assignment02;

import controller.CarRaceController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.CarRaceModel;
import view.CarRaceView;

/**
 *
 * @author Liam Bohdjalian
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        CarRaceModel model = new CarRaceModel();
        CarRaceView view = new CarRaceView(model.getCars());
        CarRaceController controller = new CarRaceController(model, view);

        Scene scene = new Scene(view, 900, 540);
        stage.setTitle("Car Race Simulation");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}