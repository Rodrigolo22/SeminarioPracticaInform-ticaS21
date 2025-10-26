package SeminarioPractica.controller;

// Importa las clases necesarias del proyecto y de JavaFX
import SeminarioPractica.SceneRouter;
import SeminarioPractica.Session;
import SeminarioPractica.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 Controlador de la vista principal del rol "Encargado de Obra".
 Esta clase se encarga de manejar las acciones de la pantalla inicial
 una vez que el usuario con rol ENCARGADO inicia sesión.
 Permite mostrar el nombre del usuario actual y navegar hacia las distintas
 secciones disponibles para su perfil: crear nuevas solicitudes, consultar
 solicitudes previas o cerrar sesión.
 */
public class EncargadoHomeController {

    // Etiqueta que muestra el nombre y apellido del usuario logueado */
    @FXML private Label lblUsuario;


     //Método de inicialización automática del controlador.


    @FXML
    public void initialize() {
        Usuario u = Session.getUsuario();  // Obtiene el usuario actual en sesión
        lblUsuario.setText(u != null ? (u.getNombre() + " " + u.getApellido()) : "-");
    }

    /**
     Acción del botón que redirige a la vista "Nueva Solicitud".
     Llama al método del SceneRouter encargado de cargar la pantalla
     correspondiente al formulario de creación de solicitudes.
     */
    @FXML
    private void irNuevaSolicitud() {
        SceneRouter.goNuevaSolicitud();
    }

    /**
     Permite al encargado consultar sus solicitudes.
     Navega hacia la vista donde se listan las solicitudes cargadas por
     el usuario actual (rol Encargado).
     */
    @FXML
    private void irMisSolicitudes() {
        SceneRouter.goMisSolicitudes();
    }

    /**
     Acción del botón "Cerrar Sesión".
     Limpia la sesión activa mediante Session.clear() y redirige
     al usuario nuevamente a la pantalla de login.
     */
    @FXML
    private void cerrarSesion() {
        Session.clear();
        SceneRouter.goLogin();
    }
}
