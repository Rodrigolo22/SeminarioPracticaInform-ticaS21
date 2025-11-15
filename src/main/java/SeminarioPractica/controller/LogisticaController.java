package SeminarioPractica.controller;

import SeminarioPractica.*;
import SeminarioPractica.service.DataStore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LogisticaController {

    @FXML private TableView<Solicitud> tblSolicitudes;

    @FXML private TableColumn<Solicitud, Number> colId;
    @FXML private TableColumn<Solicitud, String> colObra;
    @FXML private TableColumn<Solicitud, String> colSolicitante;
    @FXML private TableColumn<Solicitud, String> colFecha;
    @FXML private TableColumn<Solicitud, String> colPrioridad;
    @FXML private TableColumn<Solicitud, String> colEstado;
    @FXML private TableColumn<Solicitud, String> colDescripcion;

    @FXML private ComboBox<Estado> cboNuevoEstado;

    private final ObservableList<Solicitud> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Columnas
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));

        colObra.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getObra() != null ? c.getValue().getObra().getNombre() : "-"
        ));

        colSolicitante.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getSolicitante() != null
                        ? (c.getValue().getSolicitante().getNombre() + " " +
                        c.getValue().getSolicitante().getApellido())
                        : "-"
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


        // Combo de estados disponibles
        cboNuevoEstado.getItems().setAll(
                Estado.APROBADA,
                Estado.EN_PROCESO,
                Estado.EN_CAMINO_OBRA,
                Estado.ENTREGADO
        );

        cboNuevoEstado.getSelectionModel().select(Estado.APROBADA);

        actualizar();
        tblSolicitudes.setItems(data);

        // Doble clic muestra Detalle
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
        data.setAll(DataStore.getSolicitudesPendientes());
    }


    @FXML
    private void cambiarEstado() {
        Solicitud s = tblSolicitudes.getSelectionModel().getSelectedItem();
        if (s == null) { warn("Seleccione una solicitud."); return; }

        Estado nuevo = cboNuevoEstado.getValue();
        if (nuevo == null) { warn("Seleccione un estado."); return; }

        Estado anterior = s.getEstado();

        DataStore.cambiarEstadoSolicitud(s.getId(), nuevo);

        var usuarioActual = Session.getUsuario();
        if (usuarioActual != null) {
            String comentario = "Cambio de estado desde "
                    + (anterior != null ? anterior.getDescripcion() : "N/A")
                    + " a " + nuevo.getDescripcion()
                    + " por Logística.";
            DataStore.registrarLogSolicitud(
                    s.getId(),
                    usuarioActual.getId(),
                    anterior,
                    nuevo,
                    comentario
            );
        }

        actualizar();
    }


    @FXML

    private void rechazar() {
        Solicitud s = tblSolicitudes.getSelectionModel().getSelectedItem();
        if (s == null) { warn("Seleccione una solicitud."); return; }

        Estado anterior = s.getEstado();
        Estado nuevo = Estado.RECHAZADA;

        DataStore.cambiarEstadoSolicitud(s.getId(), nuevo);

        var usuarioActual = Session.getUsuario();
        if (usuarioActual != null) {
            String comentario = "Solicitud rechazada por Logística.";
            DataStore.registrarLogSolicitud(
                    s.getId(),
                    usuarioActual.getId(),
                    anterior,
                    nuevo,
                    comentario
            );
        }

        actualizar();
    }
    @FXML
    private void verHistorial() {
        Solicitud s = tblSolicitudes.getSelectionModel().getSelectedItem();
        if (s == null) {
            warn("Seleccione una solicitud.");
            return;
        }
        Session.setSolicitudSeleccionada(s);
        SceneRouter.goHistorialSolicitud();
    }


    @FXML
    private void volver() { SceneRouter.goLogin(); }

    private void warn(String m){
        new Alert(Alert.AlertType.WARNING, m, ButtonType.OK).show();
    }
}
