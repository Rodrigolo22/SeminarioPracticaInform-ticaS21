package SeminarioPractica.controller;

import SeminarioPractica.Rol;
import SeminarioPractica.SceneRouter;
import SeminarioPractica.Session;
import SeminarioPractica.Usuario;
import SeminarioPractica.service.DataStore;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 Controlador del formulario de inicio de sesión.
 Valida credenciales y redirige al menú correspondiente según el rol.
 */
public class LoginController {

    @FXML private TextField txtEmail;       // Campo de texto para el correo
    @FXML private PasswordField txtPassword; // Campo de texto para la contraseña

    @FXML
    private void ingresar() {
        // Obtiene los valores de los campos, evitando nulos
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();
        String pass  = txtPassword.getText() == null ? "" : txtPassword.getText().trim();

        // Validación básica
        if (email.isEmpty() || pass.isEmpty()) {
            alert("Ingrese correo y contraseña.");
            return;
        }

        // Verificación de usuario en DataStore, el almacenamiento temporal.
        Usuario u = DataStore.login(email, pass);
        if (u == null) {
            alert("Credenciales inválidas o usuario inactivo.");
            return;
        }

        // Guarda el usuario en sesión
        Session.setUsuario(u);

        // Redirige según el rol
        if (u.getRol() == Rol.ADMIN) {
            SceneRouter.goAdminHome();
        } else if (u.getRol() == Rol.ENCARGADO) {
            SceneRouter.goEncargadoHome();
        } else if (u.getRol() == Rol.LOGISTICA) {
            SceneRouter.goLogisticaHome();
        } else {
            alert("Rol no soportado: " + u.getRol());
            SceneRouter.goLogin();
        }
    }

    @FXML
    private void salir() {
        // Cierra la aplicación.
        javafx.application.Platform.exit();
    }

    // Muestra una alerta simple en pantalla
    private void alert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).show();
    }
}
