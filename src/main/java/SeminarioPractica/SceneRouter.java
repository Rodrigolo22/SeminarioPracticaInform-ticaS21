package SeminarioPractica;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneRouter {

    private static Stage stage;

    public static void setStage(Stage s) {
        stage = s;
    }

    private static void go(String fxml, String title) {
        try {

            String path = "/SeminarioPractica/view/" + fxml;

            URL url = SceneRouter.class.getResource(path);
            if (url == null) {
                throw new IllegalStateException("No se encontró el recurso FXML: " + path);
            }

            Parent root = FXMLLoader.load(url);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar la vista " + fxml, e);
        }
    }

    public static void goLogin() {
        go("LoginView.fxml", "TP3 - Login");
    }

    public static void goEncargadoHome() {
        go("EncargadoHomeView.fxml", "Inicio - Encargado");
    }

    public static void goNuevaSolicitud() {
        go("NuevaSolicitudView.fxml", "Nueva Solicitud");
    }

    public static void goMisSolicitudes() {
        go("MisSolicitudesView.fxml", "Mis Solicitudes");
    }

    public static void goLogisticaHome() {
        go("LogisticaHomeView.fxml", "Logística - Solicitudes");
    }

    public static void goAdminHome() {
        go("AdminHomeView.fxml", "Inicio - ADMIN");
    }

    public static void goSolicitudDetalle() {
        go("SolicitudDetalleView.fxml", "Detalle de Solicitud");
    }

    public static void goAbmObras() {
        go("ObraABMView.fxml", "ABM de Obras");
    }

    public static void goAbmEncargados() {
        go("EncargadoABMView.fxml", "ABM de Encargados");
    }

    public static void goHistorialSolicitud() {
        go("SolicitudHistorialView.fxml", "Historial de Solicitud");
    }


}
