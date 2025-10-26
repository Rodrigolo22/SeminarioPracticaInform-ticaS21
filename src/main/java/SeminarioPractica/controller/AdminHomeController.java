package SeminarioPractica.controller;

import SeminarioPractica.SceneRouter;
import SeminarioPractica.Session;
import SeminarioPractica.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class AdminHomeController {
    @FXML private Label lblUsuario;

    @FXML
    public void initialize() {
        Usuario u = Session.getUsuario();
        lblUsuario.setText(u != null ? (u.getNombre() + " " + u.getApellido()) : "-");
    }

    // Por ahora muestra un aviso de próxima implementacion.
    @FXML private void irABMEncargados() { enConstruccion("ABM Encargados"); }
    @FXML private void irABMObras()      { enConstruccion("ABM Obras"); }


    @FXML
    private void cerrarSesion() {
        Session.clear();
        SceneRouter.goLogin();
    }

    private void enConstruccion(String modulo) {
        Alert a = new Alert(Alert.AlertType.INFORMATION,
                modulo + " aún no implementado. Próximo paso de cara a la entrega final.",
                ButtonType.OK);
        a.setHeaderText(null);
        a.show();
    }
}
