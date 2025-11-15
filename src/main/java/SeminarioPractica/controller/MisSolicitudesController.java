package SeminarioPractica.controller;

import SeminarioPractica.*;
import SeminarioPractica.service.DataStore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MisSolicitudesController {

    @FXML private TableView<Solicitud> tblSolicitudes;

    @FXML private TableColumn<Solicitud, Number>  colId;
    @FXML private TableColumn<Solicitud, String> colObra;
    @FXML private TableColumn<Solicitud, String> colFecha;
    @FXML private TableColumn<Solicitud, String> colPrioridad;
    @FXML private TableColumn<Solicitud, String> colEstado;
    @FXML private TableColumn<Solicitud, String> colDescripcion;

    private final ObservableList<Solicitud> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colId.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getId()));

        colObra.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getObra() != null ? c.getValue().getObra().getNombre() : "-"
        ));

        colFecha.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFecha() != null ? c.getValue().getFecha().toString() : ""
        ));

        colPrioridad.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getPrioridad() != null ? c.getValue().getPrioridad().name() : ""
        ));

        colEstado.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getEstado() != null ? c.getValue().getEstado().getDescripcion() : ""
        ));

        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDescripcion() != null ? c.getValue().getDescripcion() : ""
        ));

        tblSolicitudes.setItems(data);

        actualizar();

        // doble clic abre detalle
        tblSolicitudes.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tblSolicitudes.getSelectionModel().isEmpty()) {
                Solicitud s = tblSolicitudes.getSelectionModel().getSelectedItem();
                Session.setSolicitudSeleccionada(s);
                SceneRouter.goSolicitudDetalle();
            }
        });
    }

    @FXML
    private void actualizar() {
        Usuario u = Session.getUsuario();
        if (u == null) {
            new Alert(Alert.AlertType.ERROR, "Sesión expirada. Vuelva a iniciar sesión.", ButtonType.OK).show();
            SceneRouter.goLogin();
            return;
        }
        data.setAll(DataStore.getSolicitudesDe(u));
    }

    @FXML
    private void verHistorial() {
        Solicitud s = tblSolicitudes.getSelectionModel().getSelectedItem();
        if (s == null) {
            new Alert(Alert.AlertType.WARNING, "Seleccione una solicitud.", ButtonType.OK).show();
            return;
        }
        Session.setSolicitudSeleccionada(s);
        SceneRouter.goHistorialSolicitud();
    }

    @FXML
    private void volver() {
        Usuario u = Session.getUsuario();

        if (u == null || u.getRol() == null) {
            // si por alguna razón no hay sesión, como fallback al login
            SceneRouter.goLogin();
            return;
        }

        switch (u.getRol()) {
            case ENCARGADO -> SceneRouter.goEncargadoHome();
            case LOGISTICA -> SceneRouter.goLogisticaHome();
            case ADMIN     -> SceneRouter.goAdminHome();
            default        -> SceneRouter.goLogin();
        }
    }
}
