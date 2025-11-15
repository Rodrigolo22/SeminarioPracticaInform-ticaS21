package SeminarioPractica.controller;

import SeminarioPractica.*;
import SeminarioPractica.service.DataStore;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import SeminarioPractica.Usuario;
import SeminarioPractica.Session;
import SeminarioPractica.SceneRouter;


public class SolicitudHistorialController {

    @FXML private Label lblId;
    @FXML private Label lblObra;
    @FXML private Label lblSolicitante;
    @FXML private Label lblEstadoActual;

    @FXML private TableView<SolicitudLog> tblHistorial;
    @FXML private TableColumn<SolicitudLog, String> colFecha;
    @FXML private TableColumn<SolicitudLog, String> colUsuario;
    @FXML private TableColumn<SolicitudLog, String> colEstadoAnt;
    @FXML private TableColumn<SolicitudLog, String> colEstadoNuevo;
    @FXML private TableColumn<SolicitudLog, String> colComentario;

    private final ObservableList<SolicitudLog> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Solicitud s = Session.getSolicitudSeleccionada();
        if (s == null) {
            new Alert(Alert.AlertType.ERROR, "No se ha seleccionado ninguna solicitud.", ButtonType.OK).show();
            return;
        }

        lblId.setText(String.valueOf(s.getId()));
        lblObra.setText(s.getObra() != null ? s.getObra().getNombre() : "-");

        if (s.getSolicitante() != null) {
            lblSolicitante.setText(s.getSolicitante().getNombre() + " " + s.getSolicitante().getApellido());
        } else {
            lblSolicitante.setText("-");
        }

        lblEstadoActual.setText(
                s.getEstado() != null ? s.getEstado().getDescripcion() : "-"
        );

        colFecha.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFecha() != null ? c.getValue().getFecha().toString() : ""
        ));
        colUsuario.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getUsuarioNombre() != null ? c.getValue().getUsuarioNombre() : ""
        ));
        colEstadoAnt.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getEstadoAnterior() != null ? c.getValue().getEstadoAnterior().getDescripcion() : ""
        ));
        colEstadoNuevo.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getEstadoNuevo() != null ? c.getValue().getEstadoNuevo().getDescripcion() : ""
        ));
        colComentario.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getComentario() != null ? c.getValue().getComentario() : ""
        ));

        data.setAll(DataStore.getHistorialSolicitud(s.getId()));
        tblHistorial.setItems(data);
    }

    @FXML
    private void volver() {
        Usuario u = Session.getUsuario();
        if (u == null || u.getRol() == null) {
            // Si por algún motivo no hay sesión, volvemos al login
            SceneRouter.goLogin();
            return;
        }

        switch (u.getRol()) {
            case LOGISTICA -> SceneRouter.goLogisticaHome();
            case ADMIN     -> SceneRouter.goAdminHome();
            case ENCARGADO -> SceneRouter.goMisSolicitudes();
            default        -> SceneRouter.goMisSolicitudes();
        }
    }

}
