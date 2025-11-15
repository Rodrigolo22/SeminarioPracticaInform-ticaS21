package SeminarioPractica.controller;

// Importa las clases necesarias del proyecto y de JavaFX
import SeminarioPractica.SceneRouter;
import SeminarioPractica.Session;
import SeminarioPractica.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controlador de la vista principal del rol "Encargado de Obra".
 * Maneja la pantalla inicial una vez que el usuario ENCARGADO inicia sesión.
 */
public class EncargadoHomeController {

    // Etiqueta que muestra el nombre y apellido del usuario logueado
    @FXML private Label lblUsuario;

    // Método de inicialización automática del controlador.
    @FXML
    public void initialize() {
        Usuario u = Session.getUsuario();  // Obtiene el usuario actual en sesión
        lblUsuario.setText(u != null ? (u.getNombre() + " " + u.getApellido()) : "-");
    }

    /**
     * Acción del botón que redirige a la vista "Nueva Solicitud".
     */
    @FXML
    private void irNuevaSolicitud() {
        SceneRouter.goNuevaSolicitud();
    }

    /**
     * Permite al encargado consultar sus solicitudes.
     */
    @FXML
    private void irMisSolicitudes() {
        SceneRouter.goMisSolicitudes();
    }

    /**
     * Acción del botón "Cerrar Sesión".
     */
    @FXML
    private void cerrarSesion() {
        Session.clear();
        SceneRouter.goLogin();
    }
}
