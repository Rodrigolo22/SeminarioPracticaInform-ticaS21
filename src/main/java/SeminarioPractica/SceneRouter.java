package SeminarioPractica;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 Clase auxiliar que maneja la navegación entre pantallas (FXML).
 Centraliza el cambio de vistas dentro del mismo Stage principal.
 */
public class SceneRouter {

    // Stage principal de la aplicación.
    private static Stage stage;

    /**
     Inicializa el Stage que usará toda la app.
     Se lo llama una sola vez desde MainFX.start().
     */
    public static void init(Stage s) {
        stage = s;
    }

    /**
     Carga un archivo FXML y lo muestra en pantalla.
     Aplica el CSS general y define el título de la ventana.
     */
    private static void go(String fxml, String title) {
        try {
            // Carga el layout desde el recurso indicado
            Parent root = FXMLLoader.load(SceneRouter.class.getResource("/SeminarioPractica/view/" + fxml));

            // Crea una nueva escena con el contenido cargado
            Scene sc = new Scene(root);

            // Aplica hoja de estilos si existe
            String css = "/SeminarioPractica/view/styles.css";
            if (SceneRouter.class.getResource(css) != null) {
                sc.getStylesheets().add(SceneRouter.class.getResource(css).toExternalForm());
            }

            // Configura la ventana principal
            stage.setTitle(title);
            stage.setScene(sc);
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            // Si ocurre un error al cargar la vista, imprime el detalle
            e.printStackTrace();
            throw new RuntimeException("Error al cargar vista " + fxml + ": " + e.getMessage());
        }
    }

    // Métodos públicos para acceder a las distintas pantallas
    public static void goLogin()          { go("LoginView.fxml", "Login"); }
    public static void goAdminHome()      { go("AdminHomeView.fxml", "Inicio - ADMIN"); }
    public static void goEncargadoHome()  { go("EncargadoHomeView.fxml", "Inicio - ENCARGADO"); }
    public static void goMisSolicitudes() { go("MisSolicitudesView.fxml", "Mis Solicitudes"); }
    public static void goNuevaSolicitud() { go("NuevaSolicitudView.fxml", "Nueva Solicitud"); }
    public static void goLogisticaHome()  { go("LogisticaHomeView.fxml", "Logística - Solicitudes"); }
}
