package SeminarioPractica.controller;

import SeminarioPractica.Obra;
import SeminarioPractica.SceneRouter;
import SeminarioPractica.service.DataStore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ObraABMController {

    @FXML private TableView<Obra> tblObras;
    @FXML private TableColumn<Obra, Number> colId;
    @FXML private TableColumn<Obra, String> colNombre;
    @FXML private TableColumn<Obra, String> colUbicacion;
    @FXML private TableColumn<Obra, String> colActiva;

    @FXML private TextField txtNombre;
    @FXML private TextField txtUbicacion;
    @FXML private CheckBox chkActiva;

    private final ObservableList<Obra> data = FXCollections.observableArrayList();
    private Obra obraSeleccionada;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getId()));

        colNombre.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombre()));

        colUbicacion.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getUbicacion()));

        colActiva.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isActiva() ? "SI" : "NO"));

        tblObras.setItems(data);

        // Cuando selecciona una obra en la tabla se carga en el formulario
        tblObras.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, nueva) -> {
            obraSeleccionada = nueva;
            if (nueva != null) {
                txtNombre.setText(nueva.getNombre());
                txtUbicacion.setText(nueva.getUbicacion());
                chkActiva.setSelected(nueva.isActiva());
            } else {
                limpiarFormulario();
            }
        });

        refrescar();
    }

    private void refrescar() {
        data.setAll(DataStore.getTodasLasObras());
        tblObras.getSelectionModel().clearSelection();
        obraSeleccionada = null;
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtUbicacion.clear();
        chkActiva.setSelected(true);
    }

    // Botón "Nueva"
    @FXML
    private void nuevaObra() {
        tblObras.getSelectionModel().clearSelection();
        obraSeleccionada = null;
        limpiarFormulario();
        txtNombre.requestFocus();
    }

    // Botón "Guardar" (alta o modificación)
    @FXML
    private void guardarObra() {
        String nombre = txtNombre.getText() != null ? txtNombre.getText().trim() : "";
        String ubicacion = txtUbicacion.getText() != null ? txtUbicacion.getText().trim() : "";
        boolean activa = chkActiva.isSelected();

        if (nombre.isEmpty()) {
            warn("El nombre de la obra es obligatorio.");
            return;
        }

        if (obraSeleccionada == null) {
            // Alta nueva
            DataStore.insertarObra(nombre, ubicacion, activa);
        } else {
            // Modificación
            DataStore.actualizarObra(obraSeleccionada.getId(), nombre, ubicacion, activa);
        }

        refrescar();
    }

    // Botón "Desactivar" (baja lógica)
    @FXML
    private void eliminarObra() {
        Obra seleccionada = tblObras.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            warn("Seleccione una obra para desactivar.");
            return;
        }

        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Desactivar la obra \"" + seleccionada.getNombre() + "\"?",
                ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);

        if (a.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            DataStore.desactivarObra(seleccionada.getId());
            refrescar();
        }
    }

    // Botón "Volver"
    @FXML
    private void volver() {
        SceneRouter.goAdminHome();
    }

    private void warn(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.show();
    }
}
