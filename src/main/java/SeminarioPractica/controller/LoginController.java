package SeminarioPractica.controller;

import SeminarioPractica.Rol;
import SeminarioPractica.SceneRouter;
import SeminarioPractica.Session;
import SeminarioPractica.Usuario;
import SeminarioPractica.service.DataStore;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    @FXML
    private void initialize() {

    }

    @FXML
    private void ingresar() {

        String email = txtEmail.getText() != null ? txtEmail.getText().trim() : "";
        String password = txtPassword.getText() != null ? txtPassword.getText().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Debe ingresar email y contraseña.",
                    ButtonType.OK).show();
            return;
        }

        Usuario u = DataStore.login(email, password);

        if (u == null) {
            new Alert(Alert.AlertType.ERROR,
                    "Credenciales incorrectas o usuario inactivo.",
                    ButtonType.OK).show();
            return;
        }

        // Guardar en sesión
        Session.setUsuario(u);

        // Redirección por rol
        if (u.getRol() == Rol.ADMIN) {
            SceneRouter.goAdminHome();
        } else if (u.getRol() == Rol.ENCARGADO) {
            SceneRouter.goEncargadoHome();
        } else if (u.getRol() == Rol.LOGISTICA) {
            SceneRouter.goLogisticaHome();
        } else {
            new Alert(Alert.AlertType.ERROR,
                    "El rol del usuario no está configurado.",
                    ButtonType.OK).show();
        }
    }

    @FXML
    private void salir() {
        // Handler para el botón con onAction="#salir" en LoginView.fxml
        Platform.exit();
    }
}
