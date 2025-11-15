package SeminarioPractica.controller;

import SeminarioPractica.*;
import SeminarioPractica.service.DataStore;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SolicitudDetalleController {

    @FXML private Label lblObra;
    @FXML private Label lblSolicitante;
    @FXML private Label lblFecha;
    @FXML private Label lblPrioridad;
    @FXML private Label lblEstado;

    @FXML private TableView<DetalleSolicitud> tblDetalle;
    @FXML private TableColumn<DetalleSolicitud, String> colDescripcion;
    @FXML private TableColumn<DetalleSolicitud, Number> colCantidad;
    @FXML private TableColumn<DetalleSolicitud, String> colUnidad;

    private final ObservableList<DetalleSolicitud> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Solicitud s = Session.getSolicitudSeleccionada();
        if (s == null) {
            // si por alguna razón no hay solicitud seleccionada, volvemos al login
            SceneRouter.goLogin();
            return;
        }

        // Cabecera
        lblObra.setText(s.getObra() != null ? s.getObra().getNombre() : "-");
        if (s.getSolicitante() != null) {
            lblSolicitante.setText(s.getSolicitante().getNombre() + " " + s.getSolicitante().getApellido());
        } else {
            lblSolicitante.setText("-");
        }
        lblFecha.setText(s.getFecha() != null ? s.getFecha().toString() : "-");
        lblPrioridad.setText(s.getPrioridad() != null ? s.getPrioridad().name() : "-");
        lblEstado.setText(s.getEstado() != null ? s.getEstado().name() : "-");

        // Tabla
        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescripcion()));
        colCantidad.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getCantidad()));
        colUnidad.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUnidad()));

        data.setAll(DataStore.getDetalleDeSolicitud(s.getId()));
        tblDetalle.setItems(data);
    }

    @FXML
    private void volver() {
        Usuario u = Session.getUsuario();
        if (u != null && u.getRol() == Rol.ENCARGADO) {
            SceneRouter.goMisSolicitudes();
        } else if (u != null && u.getRol() == Rol.LOGISTICA) {
            SceneRouter.goLogisticaHome();
        } else {
            SceneRouter.goLogin();
        }
    }
}
