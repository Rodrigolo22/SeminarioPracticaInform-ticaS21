package SeminarioPractica.controller;

import SeminarioPractica.*;
import SeminarioPractica.service.DataStore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 Controla la vista de Logística:
 Lista solicitudes PENDIENTES
 Permite APROBAR / RECHAZAR
 */
public class LogisticaController {

    // Tabla y columnas de la vista
    @FXML private TableView<Solicitud> tblSolicitudes;
    @FXML private TableColumn<Solicitud, Number> colId;
    @FXML private TableColumn<Solicitud, String> colObra;
    @FXML private TableColumn<Solicitud, String> colSolicitante;
    @FXML private TableColumn<Solicitud, String> colFecha;
    @FXML private TableColumn<Solicitud, String> colPrioridad;
    @FXML private TableColumn<Solicitud, String> colEstado;
    @FXML private TableColumn<Solicitud, String> colDescripcion;


    private final ObservableList<Solicitud> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Mapea cada columna con el dato a mostrar (usando lambdas y propiedades simples)
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        colObra.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getObra() != null ? c.getValue().getObra().getNombre() : "-"
        ));
        colSolicitante.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getSolicitante() != null
                        ? (c.getValue().getSolicitante().getNombre() + " " + c.getValue().getSolicitante().getApellido())
                        : "-"
        ));
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFecha() != null ? c.getValue().getFecha().toString() : ""
        ));
        colPrioridad.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getPrioridad() != null ? c.getValue().getPrioridad().name() : ""
        ));
        colEstado.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getEstado() != null ? c.getValue().getEstado().name() : ""
        ));
        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDescripcion() != null ? c.getValue().getDescripcion() : ""
        ));

        // Carga inicial: sólo solicitudes PENDIENTES
        actualizar();
        tblSolicitudes.setItems(data);
    }

    @FXML
    private void actualizar() {
        // Refresca la tabla con lo que devuelva DataStore (estado PENDIENTE)
        data.setAll(DataStore.getSolicitudesPendientes());
    }

    @FXML
    private void aprobar() {
        // Toma la fila seleccionada y cambia el estado a APROBADA
        Solicitud s = tblSolicitudes.getSelectionModel().getSelectedItem();
        if (s == null) { warn("Seleccione una solicitud."); return; }
        DataStore.cambiarEstadoSolicitud(s.getId(), Estado.APROBADA);
        actualizar();
    }

    @FXML
    private void rechazar() {
        // Toma la fila seleccionada y cambia el estado a RECHAZADA
        Solicitud s = tblSolicitudes.getSelectionModel().getSelectedItem();
        if (s == null) { warn("Seleccione una solicitud."); return; }
        DataStore.cambiarEstadoSolicitud(s.getId(), Estado.RECHAZADA);
        actualizar();
    }

    @FXML
    private void volver() {
        // Vuelve al login
        SceneRouter.goLogin();
    }

    // Alerta simple reutilizable
    private void warn(String m){
        new Alert(Alert.AlertType.WARNING, m, ButtonType.OK).show();
    }
}
