package vallegrande.edu.pe.demo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import vallegrande.edu.pe.demo.model.Pedido;
import vallegrande.edu.pe.demo.model.PedidoDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PedidoController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtCliente;
    @FXML
    private TextField txtTrabajo;
    @FXML
    private TextField txtCantidad;
    @FXML
    private ComboBox<String> cmbFormato;
    @FXML
    private DatePicker dpFechaPedido;
    @FXML
    private DatePicker dpFechaEntrega;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Label lblMensaje;
    @FXML
    private Button btnEliminar;
    @FXML
    private TableView<Pedido> tablaPedidos;

    private final PedidoDAO dao = new PedidoDAO();
    private final ObservableList<Pedido> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cmbFormato.setItems(FXCollections.observableArrayList(
                "A4", "A3", "A5", "Oficio", "Personalizado"));
        cmbEstado.setItems(FXCollections.observableArrayList(
                "Pendiente", "En proceso", "Terminado", "Entregado", "Anulado"));

        tablaPedidos.setItems(datos);
        tablaPedidos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tablaPedidos.getSelectionModel().selectedItemProperty().addListener(
                (obs, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        cargarFormulario(seleccionado);
                    }
                });

        txtBuscar.textProperty().addListener((obs, anterior, actual) -> {
            if (actual == null || actual.isBlank()) {
                cargarTabla();
            }
        });

        cargarTabla();
        limpiarFormulario();
    }

    @FXML
    private void onRegistrar() {
        if (!validar()) {
            return;
        }
        try {
            dao.registrar(leerFormulario());
            mostrarMensaje("Pedido registrado correctamente.", false);
            cargarTabla();
            limpiarFormulario();
        } catch (SQLException e) {
            mostrarError("No se pudo registrar", e);
        }
    }

    @FXML
    private void onActualizar() {
        if (txtId.getText().isBlank()) {
            mostrarMensaje("Seleccione un pedido de la tabla para actualizar.", true);
            return;
        }
        if (!validar()) {
            return;
        }
        try {
            Pedido p = leerFormulario();
            p.setIdPedido(Integer.parseInt(txtId.getText()));
            if (dao.actualizar(p)) {
                mostrarMensaje("Pedido actualizado correctamente.", false);
                cargarTabla();
                limpiarFormulario();
            } else {
                mostrarMensaje("No se encontró el pedido a actualizar.", true);
            }
        } catch (SQLException e) {
            mostrarError("No se pudo actualizar", e);
        }
    }

    @FXML
    private void onEliminar() {
        Pedido seleccionado = tablaPedidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("Seleccione un pedido de la tabla para eliminar.", true);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Eliminar el pedido N° " + seleccionado.getIdPedido()
                + " de " + seleccionado.getNombreCliente() + "?");

        if (confirmacion.showAndWait().orElse(javafx.scene.control.ButtonType.CANCEL)
                != javafx.scene.control.ButtonType.OK) {
            return;
        }

        try {
            if (dao.eliminar(seleccionado.getIdPedido())) {
                mostrarMensaje("Pedido eliminado correctamente.", false);
                cargarTabla();
                limpiarFormulario();
            } else {
                mostrarMensaje("No se encontró el pedido a eliminar.", true);
            }
        } catch (SQLException e) {
            mostrarError("No se pudo eliminar", e);
        }
    }

    @FXML
    private void onBuscar() {
        String filtro = txtBuscar.getText().trim();
        if (filtro.isEmpty()) {
            cargarTabla();
            return;
        }
        try {
            datos.setAll(dao.buscar(filtro));
            mostrarMensaje("Resultados encontrados: " + datos.size(), false);
        } catch (SQLException e) {
            mostrarError("No se pudo buscar", e);
        }
    }

    @FXML
    private void onMostrarTodos() {
        txtBuscar.clear();
        cargarTabla();
        mostrarMensaje("Mostrando todos los pedidos.", false);
    }

    @FXML
    private void onLimpiar() {
        limpiarFormulario();
        mostrarMensaje("Formulario listo para un nuevo pedido.", false);
    }

    private void cargarTabla() {
        try {
            List<Pedido> lista = dao.listar();
            datos.setAll(lista);
        } catch (SQLException e) {
            mostrarError("No se pudo conectar con MySQL.\n"
                    + "Verifique que el contenedor esté iniciado (docker start mysql-imprenta).", e);
        }
    }

    private Pedido leerFormulario() {
        Pedido p = new Pedido();
        p.setNombreCliente(txtCliente.getText().trim());
        p.setTipoTrabajo(txtTrabajo.getText().trim());
        p.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
        p.setFormato(cmbFormato.getValue());
        p.setFechaPedido(dpFechaPedido.getValue());
        p.setFechaEntrega(dpFechaEntrega.getValue());
        p.setEstado(cmbEstado.getValue());
        return p;
    }

    private boolean validar() {
        if (txtCliente.getText().isBlank()) {
            mostrarMensaje("Ingrese el nombre del cliente.", true);
            return false;
        }
        if (txtTrabajo.getText().isBlank()) {
            mostrarMensaje("Ingrese el tipo de trabajo.", true);
            return false;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            mostrarMensaje("La cantidad debe ser un número entero.", true);
            return false;
        }
        if (cantidad <= 0) {
            mostrarMensaje("La cantidad debe ser mayor que cero.", true);
            return false;
        }

        if (cmbFormato.getValue() == null) {
            mostrarMensaje("Seleccione el formato.", true);
            return false;
        }
        if (dpFechaPedido.getValue() == null || dpFechaEntrega.getValue() == null) {
            mostrarMensaje("Seleccione la fecha de pedido y la fecha de entrega.", true);
            return false;
        }
        if (dpFechaEntrega.getValue().isBefore(dpFechaPedido.getValue())) {
            mostrarMensaje("La fecha de entrega no puede ser anterior a la fecha de pedido.", true);
            return false;
        }
        if (cmbEstado.getValue() == null) {
            mostrarMensaje("Seleccione el estado del pedido.", true);
            return false;
        }
        return true;
    }

    private void cargarFormulario(Pedido p) {
        txtId.setText(String.valueOf(p.getIdPedido()));
        txtCliente.setText(p.getNombreCliente());
        txtTrabajo.setText(p.getTipoTrabajo());
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        cmbFormato.setValue(p.getFormato());
        dpFechaPedido.setValue(p.getFechaPedido());
        dpFechaEntrega.setValue(p.getFechaEntrega());
        cmbEstado.setValue(p.getEstado());
        lblMensaje.setText("");
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtCliente.clear();
        txtTrabajo.clear();
        txtCantidad.clear();
        cmbFormato.setValue(null);
        cmbEstado.setValue(null);
        dpFechaPedido.setValue(LocalDate.now());
        dpFechaEntrega.setValue(LocalDate.now());
        txtBuscar.clear();
        tablaPedidos.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        txtCliente.requestFocus();
    }

    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.getStyleClass().removeAll("mensaje-ok", "mensaje-error");
        lblMensaje.getStyleClass().add(esError ? "mensaje-error" : "mensaje-ok");
    }

    private void mostrarError(String titulo, SQLException e) {
        mostrarMensaje(titulo + " (" + e.getMessage() + ")", true);

        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error de base de datos");
        alerta.setHeaderText(titulo);
        alerta.setContentText(e.getMessage());
        alerta.showAndWait();
    }
}
