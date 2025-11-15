package SeminarioPractica.controller;

import SeminarioPractica.SceneRouter;
import SeminarioPractica.Session;
import SeminarioPractica.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AdminHomeController {

    @FXML private Label lblUsuario;

    @FXML
    public void initialize() {
        Usuario u = Session.getUsuario();
        if (u != null) {
            String texto = u.getNombre();
            if (u.getApellido() != null) {
                texto += " " + u.getApellido();
            }
            lblUsuario.setText(texto);
        }
    }

    @FXML
    private void irABMEncargados() {
        SceneRouter.goAbmEncargados();
    }

    @FXML
    private void irABMObras() {
        SceneRouter.goAbmObras();
    }

    @FXML
    private void cerrarSesion() {
        Session.clear();
        SceneRouter.goLogin();
    }
}
