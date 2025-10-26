package SeminarioPractica.controller;

import SeminarioPractica.Estado;
import SeminarioPractica.Obra;
import SeminarioPractica.Prioridad;
import SeminarioPractica.SceneRouter;
import SeminarioPractica.Session;
import SeminarioPractica.Solicitud;
import SeminarioPractica.Usuario;
import SeminarioPractica.service.DataStore;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class NuevaSolicitudController {

    // Cabecera
    @FXML private ComboBox<Obra> cboObra;
    @FXML private ComboBox<Prioridad> cboPrioridad;

    // Línea libre
    @FXML private ComboBox<String> cboTipo; // "EPP","Herramienta","Material"
    @FXML private TextField txtNombre;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNota;

    // Tabla
    @FXML private TableView<ItemLinea> tblItems;
    @FXML private TableColumn<ItemLinea,String> colTipo, colNombre, colNota;
    @FXML private TableColumn<ItemLinea,Number> colCant;

    private final ObservableList<ItemLinea> items = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // combos
        cboObra.getItems().setAll(DataStore.getObras());
        cboPrioridad.getItems().setAll(Prioridad.values());
        if (!cboPrioridad.getItems().isEmpty()) {
            cboPrioridad.getSelectionModel().select(Prioridad.MEDIA);
        }

        cboTipo.getItems().addAll("EPP", "Herramienta", "Material");
        cboTipo.getSelectionModel().selectFirst();

        // tabla
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().tipo()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().nombre()));
        colCant.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().cantidad()));
        colNota.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().nota()));

        tblItems.setItems(items);
    }

    @FXML
    private void agregarItem() {
        String tipo = selectedText(cboTipo);
        String nombre = txtNombre.getText().trim();
        String nota = txtNota.getText().trim();
        double cant;

        try {
            cant = Double.parseDouble(txtCantidad.getText().trim());
        } catch (Exception e) {
            warn("Cantidad inválida. Ingrese números (ej: 10 o 10.5).");
            return;
        }

        if (nombre.isEmpty() || cant <= 0) {
            warn("Ingrese nombre y una cantidad mayor a 0.");
            return;
        }

        items.add(new ItemLinea(tipo, nombre, cant, nota));
        limpiarLinea();
    }

    @FXML
    private void quitarItem() {
        ItemLinea sel = tblItems.getSelectionModel().getSelectedItem();
        if (sel != null) items.remove(sel);
    }

    @FXML
    private void guardar() {
        Obra obra = cboObra.getValue();
        Prioridad prio = cboPrioridad.getValue();
        Usuario solicitante = Session.getUsuario();

        if (solicitante == null) { SceneRouter.goLogin(); return; }
        if (obra == null) { warn("Seleccione una obra."); return; }
        if (items.isEmpty()) { warn("Agregue al menos un ítem a la solicitud."); return; }

        String resumen = items.stream()
                .map(it -> it.tipo() + " - " + it.nombre() + " x " + it.cantidad()
                        + (it.nota().isEmpty() ? "" : " (" + it.nota() + ")"))
                .collect(Collectors.joining(" | "));

        Solicitud s = new Solicitud();
        s.setObra(obra);
        s.setSolicitante(solicitante);
        s.setFecha(LocalDate.now());
        s.setPrioridad(prio);
        s.setEstado(Estado.PENDIENTE);
        s.setDescripcion(resumen);

        DataStore.addSolicitud(s);

        new Alert(Alert.AlertType.INFORMATION, "Solicitud guardada.", ButtonType.OK).showAndWait();
        SceneRouter.goMisSolicitudes();
    }

    @FXML private void volver() { SceneRouter.goEncargadoHome(); }

    // helpers
    private void limpiarLinea() {
        txtNombre.clear(); txtCantidad.clear(); txtNota.clear();
        cboTipo.getSelectionModel().selectFirst();
    }
    private static String selectedText(ComboBox<String> cb){
        String v = cb.getSelectionModel().getSelectedItem();
        return v == null ? "" : v;
    }
    private void warn(String m){ new Alert(Alert.AlertType.WARNING, m, ButtonType.OK).show(); }

    // DTO para la tabla
    public static class ItemLinea {
        private final String tipo, nombre, nota;
        private final double cantidad;
        public ItemLinea(String tipo, String nombre, double cantidad, String nota) {
            this.tipo = tipo; this.nombre = nombre; this.cantidad = cantidad; this.nota = nota == null ? "" : nota;
        }
        public String tipo() { return tipo; }
        public String nombre() { return nombre; }
        public double cantidad() { return cantidad; }
        public String nota() { return nota; }
    }
}
