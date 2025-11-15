package SeminarioPractica.controller;

import SeminarioPractica.SceneRouter;
import SeminarioPractica.Usuario;
import SeminarioPractica.service.DataStore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EncargadoABMController {

    @FXML private TableView<Usuario> tblEncargados;
    @FXML private TableColumn<Usuario, Number> colId;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colApellido;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colActivo;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private CheckBox chkActivo;

    private final ObservableList<Usuario> data = FXCollections.observableArrayList();
    private Usuario encargadoSeleccionado;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getId()));

        colNombre.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombre()));

        colApellido.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getApellido()));

        colEmail.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEmail()));

        colActivo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().isActivo() ? "SI" : "NO"));

        tblEncargados.setItems(data);

        // Cuando selecciono una fila, cargo los datos en el formulario
        tblEncargados.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, nueva) -> {
            encargadoSeleccionado = nueva;
            if (nueva != null) {
                txtNombre.setText(nueva.getNombre());
                txtApellido.setText(nueva.getApellido());
                txtEmail.setText(nueva.getEmail());
                chkActivo.setSelected(nueva.isActivo());
                txtPassword.clear();
            } else {
                limpiarFormulario();
            }
        });

        refrescar();
    }

    private void refrescar() {
        data.setAll(DataStore.getEncargados());
        tblEncargados.getSelectionModel().clearSelection();
        encargadoSeleccionado = null;
        limpiarFormulario();
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtApellido.clear();
        txtEmail.clear();
        txtPassword.clear();
        chkActivo.setSelected(true);
    }

    // Botón "Nuevo"
    @FXML
    private void nuevoEncargado() {
        tblEncargados.getSelectionModel().clearSelection();
        encargadoSeleccionado = null;
        limpiarFormulario();
        txtNombre.requestFocus();
    }

    // Botón "Guardar" (alta o modificación)
    @FXML
    private void guardarEncargado() {
        String nombre    = safe(txtNombre.getText());
        String apellido  = safe(txtApellido.getText());
        String email     = safe(txtEmail.getText());
        String password  = safe(txtPassword.getText());
        boolean activo   = chkActivo.isSelected();

        if (nombre.isEmpty()) {
            warn("El nombre es obligatorio.");
            return;
        }
        if (email.isEmpty()) {
            warn("El email es obligatorio.");
            return;
        }

        if (encargadoSeleccionado == null) {
            // Alta nueva → exigimos contraseña
            if (password.isEmpty()) {
                warn("La contraseña es obligatoria para un nuevo encargado.");
                return;
            }
            DataStore.insertarEncargado(nombre, apellido, email, password, activo);
        } else {
            // Modificación si el password viene vacío, no se cambia
            DataStore.actualizarEncargado(
                    encargadoSeleccionado.getId(),
                    nombre,
                    apellido,
                    email,
                    password,  // puede ser vacío → DataStore decide si lo cambia o no
                    activo
            );
        }

        refrescar();
    }

    // Botón "Desactivar" baja lógica
    @FXML
    private void eliminarEncargado() {
        Usuario sel = tblEncargados.getSelectionModel().getSelectedItem();
        if (sel == null) {
            warn("Seleccione un encargado para desactivar.");
            return;
        }

        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Desactivar al encargado \"" + sel.getNombre() + " " + sel.getApellido() + "\"?",
                ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);

        if (a.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            DataStore.desactivarEncargado(sel.getId());
            refrescar();
        }
    }

    @FXML
    private void volver() {
        SceneRouter.goAdminHome();
    }

    private void warn(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.show();
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
