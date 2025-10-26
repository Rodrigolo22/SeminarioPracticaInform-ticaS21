package SeminarioPractica;

// Importación de las clases para iniciar la aplicación
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Inicializa el gestor de navegación centralizado (SceneRouter)
            // que se encarga de manejar los cambios de escena dentro de la aplicación.
            SceneRouter.init(stage);

            // Carga el archivo FXML correspondiente a la vista de inicio de sesión (LoginView.fxml)
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/SeminarioPractica/view/LoginView.fxml")
            );

            // Crea una nueva escena (ventana) a partir del archivo FXML cargado
            Scene scene = new Scene(loader.load());

            // Aplica la hoja de estilos CSS personalizada para dar formato visual a la interfaz
            scene.getStylesheets().add(
                    getClass().getResource("/SeminarioPractica/view/styles.css").toExternalForm()
            );

            // Configura las propiedades principales de la ventana (Stage)
            stage.setTitle("Sistema Andina - TP3");   // Título de la ventana
            stage.setScene(scene);                    // Asigna la escena cargada
            stage.setResizable(false);                // Bloquea el cambio de tamaño de la ventana
            stage.centerOnScreen();                   // Centra la ventana en la pantalla
            stage.show();                             // Muestra la ventana al usuario

        } catch (Exception ex) {
            // En caso de que ocurra algún error durante la carga de la vista inicial,
            // se muestra un cuadro de diálogo con el detalle de la excepción.
            Alert a = new Alert(Alert.AlertType.ERROR,
                    "No se pudo iniciar la aplicación.\n\nDetalle: " + ex.getMessage(),
                    ButtonType.OK);
            a.setHeaderText("Error al cargar la vista inicial");
            a.showAndWait();

            // Imprime el error completo en la consola para facilitar la depuración.
            ex.printStackTrace();
        }
    }

    /**
     Método main()
     Es el punto de entrada estándar de cualquier aplicación Java.
     Llama al método launch() que se encarga de iniciar la aplicación JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
