package SeminarioPractica;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);   // Arranca la aplicación JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        // Registro stage en el router y muestro la vista de login
        SceneRouter.setStage(primaryStage);
        SceneRouter.goLogin();
    }
}
