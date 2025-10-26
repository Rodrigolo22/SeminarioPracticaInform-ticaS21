package SeminarioPractica.controller;

import SeminarioPractica.*;
import SeminarioPractica.service.DataStore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MisSolicitudesController {

    @FXML private TableView<Solicitud> tblSolicitudes;
    @FXML private TableColumn<Solicitud, Number> colId;
    @FXML private TableColumn<Solicitud, String> colObra;
    @FXML private TableColumn<Solicitud, String> colFecha;
    @FXML private TableColumn<Solicitud, String> colPrioridad;
    @FXML private TableColumn<Solicitud, String> colEstado;
    @FXML private TableColumn<Solicitud, String> colDescripcion;

    private final ObservableList<Solicitud> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Usuario u = Session.getUsuario();
        if (u == null) { SceneRouter.goLogin(); return; }

        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));
        colObra.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getObra() != null ? c.getValue().getObra().getNombre() : "-"));
        colFecha.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFecha() != null ? c.getValue().getFecha().toString() : ""));
        colPrioridad.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getPrioridad() != null ? c.getValue().getPrioridad().name() : ""));
        colEstado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEstado() != null ? c.getValue().getEstado().name() : ""));
        colDescripcion.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescripcion() != null ? c.getValue().getDescripcion() : ""));

        data.setAll(DataStore.getSolicitudesDe(u));
        tblSolicitudes.setItems(data);
    }

    @FXML
    private void actualizar() {
        Usuario u = Session.getUsuario();
        if (u != null) data.setAll(DataStore.getSolicitudesDe(u));
    }

    @FXML
    private void volver() { SceneRouter.goEncargadoHome(); }
}
