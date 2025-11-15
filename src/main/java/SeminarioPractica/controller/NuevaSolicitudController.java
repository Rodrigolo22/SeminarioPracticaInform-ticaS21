package SeminarioPractica.controller;

import SeminarioPractica.*;
import SeminarioPractica.service.DataStore;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la vista NuevaSolicitudView.fxml
 * Permite al Encargado crear una nueva solicitud de recursos
 * seleccionando la obra, prioridad y detallando los ítems requeridos.
 */
public class NuevaSolicitudController {

    // Cabecera
    @FXML private ComboBox<Obra> cboObra;
    @FXML private ComboBox<Prioridad> cboPrioridad;

    // Detalle de ítems
    @FXML private ComboBox<String> cboTipo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNota;

    @FXML private TableView<ItemLinea> tblItems;
    @FXML private TableColumn<ItemLinea, String> colTipo;
    @FXML private TableColumn<ItemLinea, String> colNombre;
    @FXML private TableColumn<ItemLinea, Number> colCant;
    @FXML private TableColumn<ItemLinea, String> colNota;

    private final ObservableList<ItemLinea> dataItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Cargar obras activas desde BD
        List<Obra> obras = DataStore.getObras();
        cboObra.setItems(FXCollections.observableArrayList(obras));

        // Cargar prioridades desde enum
        cboPrioridad.setItems(FXCollections.observableArrayList(Prioridad.values()));

        // Tipos de recurso (material / herramienta)
        cboTipo.setItems(FXCollections.observableArrayList("MATERIAL", "HERRAMIENTA"));

        // Configuración de columnas de la tabla
        colTipo.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().tipo()));
        colNombre.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().nombre()));
        colCant.setCellValueFactory(cd -> new SimpleDoubleProperty(cd.getValue().cantidad()));
        colNota.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().nota()));

        tblItems.setItems(dataItems);
    }

    @FXML
    private void agregarItem() {
        String tipo = cboTipo.getValue();
        String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        String cantStr = txtCantidad.getText() != null ? txtCantidad.getText().trim() : "";
        String nota = txtNota.getText() != null ? txtNota.getText().trim() : "";

        if (tipo == null || tipo.isBlank()) {
            warn("Seleccione el tipo de recurso (Material / Herramienta).");
            return;
        }
        if (nombre.isEmpty()) {
            warn("Ingrese el nombre o descripción del recurso.");
            return;
        }
        if (cantStr.isEmpty()) {
            warn("Ingrese la cantidad.");
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(cantStr.replace(",", "."));
            if (cantidad <= 0) {
                warn("La cantidad debe ser mayor a cero.");
                return;
            }
        } catch (NumberFormatException e) {
            warn("Cantidad inválida. Ingrese un número válido.");
            return;
        }

        ItemLinea item = new ItemLinea(tipo, nombre, cantidad, nota);
        dataItems.add(item);

        // Limpiar campos de detalle
        txtNombre.clear();
        txtCantidad.clear();
        txtNota.clear();
        cboTipo.getSelectionModel().clearSelection();
    }

    @FXML
    private void quitarItem() {
        ItemLinea seleccionado = tblItems.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            warn("Seleccione una fila de la tabla para quitar.");
            return;
        }
        dataItems.remove(seleccionado);
    }

    @FXML
    private void guardar() {
        Usuario solicitante = Session.getUsuario();
        if (solicitante == null) {
            warn("No hay un usuario en sesión. Vuelva a iniciar sesión.");
            SceneRouter.goLogin();
            return;
        }

        Obra obra = cboObra.getValue();
        if (obra == null) {
            warn("Seleccione la obra para la cual se realiza la solicitud.");
            return;
        }

        Prioridad prioridad = cboPrioridad.getValue();
        if (prioridad == null) {
            warn("Seleccione la prioridad de la solicitud.");
            return;
        }

        if (dataItems.isEmpty()) {
            warn("Agregue al menos un ítem a la solicitud.");
            return;
        }

        // Resumen textual de los ítems para el campo descripcion
        String resumen = dataItems.stream()
                .map(it -> it.tipo() + " - " + it.nombre() + " x " + it.cantidad() +
                        (it.nota().isEmpty() ? "" : " (" + it.nota() + ")"))
                .collect(Collectors.joining(" | "));

        Solicitud solicitud = new Solicitud();
        solicitud.setObra(obra);
        solicitud.setSolicitante(solicitante);
        solicitud.setFecha(LocalDate.now());
        solicitud.setPrioridad(prioridad);
        solicitud.setEstado(Estado.PENDIENTE);
        solicitud.setDescripcion(resumen);

        // Detalles individuales
        for (ItemLinea it : dataItems) {
            String desc = it.nombre();
            if (!it.nota().isEmpty()) {
                desc = desc + " (" + it.nota() + ")";
            }
            // Unidad genérica porque la vista no pide otra cosa
            DetalleSolicitud det = new DetalleSolicitud(it.tipo(), desc, it.cantidad(), "UNID");
            solicitud.getDetalles().add(det);
        }

        DataStore.addSolicitud(solicitud);

        new Alert(Alert.AlertType.INFORMATION,
                "Solicitud guardada correctamente.",
                ButtonType.OK).showAndWait();

        SceneRouter.goEncargadoHome();
    }

    @FXML
    private void volver() {
        SceneRouter.goEncargadoHome();
    }

    private void warn(String m){
        new Alert(Alert.AlertType.WARNING, m, ButtonType.OK).show();
    }

    // DTO para la tabla
    public static class ItemLinea {
        private final String tipo;
        private final String nombre;
        private final double cantidad;
        private final String nota;

        public ItemLinea(String tipo, String nombre, double cantidad, String nota) {
            this.tipo = tipo;
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.nota = nota == null ? "" : nota;
        }

        public String tipo() { return tipo; }
        public String nombre() { return nombre; }
        public double cantidad() { return cantidad; }
        public String nota() { return nota; }
    }
}
