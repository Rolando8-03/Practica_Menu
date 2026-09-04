package ni.edu.uam.practica_menu;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.util.Optional;

public class InventarioController {

    // Campos del formulario

    @FXML
    private TextField codigoTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField categoriaTextField;

    @FXML
    private TextField precioTextField;

    @FXML
    private TextField existenciaTextField;

    // Tabla y columnas

    @FXML
    private TableView<Producto> productosTableView;

    @FXML
    private TableColumn<Producto, String> codigoTableColumn;

    @FXML
    private TableColumn<Producto, String> nombreTableColumn;

    @FXML
    private TableColumn<Producto, String> categoriaTableColumn;

    @FXML
    private TableColumn<Producto, Double> precioTableColumn;

    @FXML
    private TableColumn<Producto, Integer> existenciaTableColumn;

    // Etiqueta de mensajes

    @FXML
    private Label estadoLabel;

    // MenuItem utilizados para los atajos

    @FXML
    private MenuItem nuevoMenuItem;

    @FXML
    private MenuItem guardarMenuItem;

    @FXML
    private MenuItem salirMenuItem;

    // Lista temporal de productos

    private final ObservableList<Producto> productos =
            FXCollections.observableArrayList();

    // Guarda el producto que se está editando

    private Producto productoEnEdicion;

    @FXML
    private void initialize() {

        configurarColumnas();
        configurarAtajos();

        productosTableView.setItems(productos);

        // Cinco productos requeridos para las comprobaciones

        productos.addAll(
                new Producto(
                        "P001",
                        "Café molido",
                        "Bebidas",
                        185.50,
                        25
                ),
                new Producto(
                        "P002",
                        "Arroz 1 libra",
                        "Alimentos",
                        28.00,
                        60
                ),
                new Producto(
                        "P003",
                        "Jabón líquido",
                        "Limpieza",
                        95.75,
                        18
                ),
                new Producto(
                        "P004",
                        "Aceite vegetal",
                        "Alimentos",
                        82.50,
                        30
                ),
                new Producto(
                        "P005",
                        "Vaso térmico",
                        "Hogar",
                        210.00,
                        12
                )
        );

        mostrarEstado(
                "Aplicación lista. Hay cinco productos registrados.",
                false
        );
    }

    private void configurarColumnas() {

        codigoTableColumn.setCellValueFactory(
                new PropertyValueFactory<>("codigo")
        );

        nombreTableColumn.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        categoriaTableColumn.setCellValueFactory(
                new PropertyValueFactory<>("categoria")
        );

        precioTableColumn.setCellValueFactory(
                new PropertyValueFactory<>("precio")
        );

        existenciaTableColumn.setCellValueFactory(
                new PropertyValueFactory<>("existencia")
        );
    }

    private void configurarAtajos() {

        nuevoMenuItem.setAccelerator(
                new KeyCodeCombination(
                        KeyCode.N,
                        KeyCombination.CONTROL_DOWN
                )
        );

        guardarMenuItem.setAccelerator(
                new KeyCodeCombination(
                        KeyCode.G,
                        KeyCombination.CONTROL_DOWN
                )
        );

        salirMenuItem.setAccelerator(
                new KeyCodeCombination(
                        KeyCode.Q,
                        KeyCombination.CONTROL_DOWN
                )
        );
    }

    // NUEVO
    // Este método es utilizado por el MenuBar y el ToolBar.

    @FXML
    private void nuevoProducto() {

        limpiarFormulario();

        productosTableView
                .getSelectionModel()
                .clearSelection();

        mostrarEstado(
                "Nuevo registro: complete los datos del producto.",
                false
        );

        codigoTextField.requestFocus();
    }

    // LIMPIAR
    // Este método es utilizado por el menú Archivo y el formulario.

    @FXML
    private void limpiarCampos() {

        limpiarFormulario();

        mostrarEstado(
                "Los campos fueron limpiados.",
                false
        );

        codigoTextField.requestFocus();
    }

    // GUARDAR
    // Este método es utilizado por el MenuBar, ToolBar y formulario.

    @FXML
    private void guardarProducto() {

        String codigo = codigoTextField.getText().trim();
        String nombre = nombreTextField.getText().trim();
        String categoria = categoriaTextField.getText().trim();

        String precioTexto = precioTextField
                .getText()
                .trim()
                .replace(",", ".");

        String existenciaTexto = existenciaTextField
                .getText()
                .trim();

        if (codigo.isEmpty()
                || nombre.isEmpty()
                || categoria.isEmpty()
                || precioTexto.isEmpty()
                || existenciaTexto.isEmpty()) {

            mostrarAdvertencia(
                    "Campos incompletos",
                    "Debe completar todos los datos del producto."
            );

            return;
        }

        double precio;
        int existencia;

        try {

            precio = Double.parseDouble(precioTexto);
            existencia = Integer.parseInt(existenciaTexto);

        } catch (NumberFormatException error) {

            mostrarAdvertencia(
                    "Datos incorrectos",
                    "El precio debe ser decimal y la existencia debe ser un número entero."
            );

            return;
        }

        if (precio <= 0) {

            mostrarAdvertencia(
                    "Precio incorrecto",
                    "El precio debe ser mayor que cero."
            );

            return;
        }

        if (existencia < 0) {

            mostrarAdvertencia(
                    "Existencia incorrecta",
                    "La existencia no puede ser negativa."
            );

            return;
        }

        if (codigoYaExiste(codigo)) {

            mostrarAdvertencia(
                    "Código repetido",
                    "Ya existe otro producto con el código " + codigo + "."
            );

            return;
        }

        if (productoEnEdicion == null) {

            Producto nuevoProducto = new Producto(
                    codigo,
                    nombre,
                    categoria,
                    precio,
                    existencia
            );

            productos.add(nuevoProducto);

            productosTableView
                    .getSelectionModel()
                    .select(nuevoProducto);

            mostrarEstado(
                    "Producto guardado correctamente.",
                    false
            );

        } else {

            productoEnEdicion.setCodigo(codigo);
            productoEnEdicion.setNombre(nombre);
            productoEnEdicion.setCategoria(categoria);
            productoEnEdicion.setPrecio(precio);
            productoEnEdicion.setExistencia(existencia);

            productosTableView.refresh();

            mostrarEstado(
                    "Producto actualizado correctamente.",
                    false
            );
        }

        limpiarFormulario();
    }

    // EDITAR
    // MenuBar, ToolBar y ContextMenu llaman al mismo método.

    @FXML
    private void editarProducto() {

        Producto productoSeleccionado =
                productosTableView
                        .getSelectionModel()
                        .getSelectedItem();

        if (productoSeleccionado == null) {

            mostrarAdvertencia(
                    "Sin selección",
                    "Seleccione un producto de la tabla para editarlo."
            );

            return;
        }

        productoEnEdicion = productoSeleccionado;

        codigoTextField.setText(
                productoSeleccionado.getCodigo()
        );

        nombreTextField.setText(
                productoSeleccionado.getNombre()
        );

        categoriaTextField.setText(
                productoSeleccionado.getCategoria()
        );

        precioTextField.setText(
                String.valueOf(productoSeleccionado.getPrecio())
        );

        existenciaTextField.setText(
                String.valueOf(productoSeleccionado.getExistencia())
        );

        mostrarEstado(
                "Editando " + productoSeleccionado.getNombre()
                        + ". Presione Guardar para aplicar los cambios.",
                false
        );

        codigoTextField.requestFocus();
    }

    // ELIMINAR
    // MenuBar, ToolBar y ContextMenu llaman al mismo método.

    @FXML
    private void eliminarProducto() {

        Producto productoSeleccionado =
                productosTableView
                        .getSelectionModel()
                        .getSelectedItem();

        if (productoSeleccionado == null) {

            mostrarAdvertencia(
                    "Sin selección",
                    "Seleccione un producto de la tabla para eliminarlo."
            );

            return;
        }

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmacion.setTitle("Confirmar eliminación");

        confirmacion.setHeaderText(
                "¿Desea eliminar este producto?"
        );

        confirmacion.setContentText(
                productoSeleccionado.getCodigo()
                        + " - "
                        + productoSeleccionado.getNombre()
        );

        Optional<ButtonType> respuesta =
                confirmacion.showAndWait();

        if (respuesta.isPresent()
                && respuesta.get() == ButtonType.OK) {

            productos.remove(productoSeleccionado);

            if (productoSeleccionado == productoEnEdicion) {
                limpiarFormulario();
            }

            mostrarEstado(
                    "Producto eliminado correctamente.",
                    false
            );

        } else {

            mostrarEstado(
                    "La eliminación fue cancelada.",
                    false
            );
        }
    }

    // VER DETALLE
    // Esta opción se ejecuta desde el ContextMenu.

    @FXML
    private void verDetalleProducto() {

        Producto productoSeleccionado =
                productosTableView
                        .getSelectionModel()
                        .getSelectedItem();

        if (productoSeleccionado == null) {

            mostrarAdvertencia(
                    "Sin selección",
                    "Seleccione un producto para consultar su detalle."
            );

            return;
        }

        Alert detalle = new Alert(
                Alert.AlertType.INFORMATION
        );

        detalle.setTitle("Detalle del producto");

        detalle.setHeaderText(
                productoSeleccionado.getNombre()
        );

        detalle.setContentText(
                "Código: "
                        + productoSeleccionado.getCodigo()
                        + "\nNombre: "
                        + productoSeleccionado.getNombre()
                        + "\nCategoría: "
                        + productoSeleccionado.getCategoria()
                        + "\nPrecio: C$ "
                        + String.format(
                        "%.2f",
                        productoSeleccionado.getPrecio()
                )
                        + "\nExistencia: "
                        + productoSeleccionado.getExistencia()
        );

        detalle.showAndWait();

        mostrarEstado(
                "Se consultó el detalle de "
                        + productoSeleccionado.getNombre() + ".",
                false
        );
    }

    // SALIR

    @FXML
    private void salirAplicacion() {

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirmacion.setTitle("Salir");

        confirmacion.setHeaderText(
                "¿Desea cerrar la aplicación?"
        );

        confirmacion.setContentText(
                "Los productos se almacenan temporalmente."
        );

        Optional<ButtonType> respuesta =
                confirmacion.showAndWait();

        if (respuesta.isPresent()
                && respuesta.get() == ButtonType.OK) {

            Platform.exit();
        }
    }

    // ACERCA DE

    @FXML
    private void mostrarAcercaDe() {

        Alert informacion = new Alert(
                Alert.AlertType.INFORMATION
        );

        informacion.setTitle("Acerca de");

        informacion.setHeaderText(
                "Distribuidora El Güegüense"
        );

        informacion.setContentText(
                "Aplicación JavaFX para administrar productos.\n\n"
                        + "Asignatura: Programación de Aplicaciones "
                        + "de Escritorio\n"
                        + "Autor: Rolando Enrique Mayorga Mena"
        );

        informacion.showAndWait();
    }

    // Comprueba que el código no esté repetido.

    private boolean codigoYaExiste(String codigo) {

        for (Producto producto : productos) {

            boolean esOtroProducto =
                    producto != productoEnEdicion;

            boolean mismoCodigo =
                    producto.getCodigo()
                            .equalsIgnoreCase(codigo);

            if (esOtroProducto && mismoCodigo) {
                return true;
            }
        }

        return false;
    }

    // Limpia todos los controles del formulario.

    private void limpiarFormulario() {

        codigoTextField.clear();
        nombreTextField.clear();
        categoriaTextField.clear();
        precioTextField.clear();
        existenciaTextField.clear();

        productoEnEdicion = null;
    }

    // Muestra una alerta y actualiza la etiqueta inferior.

    private void mostrarAdvertencia(
            String titulo,
            String mensaje
    ) {

        mostrarEstado(mensaje, true);

        Alert alerta = new Alert(
                Alert.AlertType.WARNING
        );

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Cambia el mensaje mostrado en la interfaz.

    private void mostrarEstado(
            String mensaje,
            boolean esError
    ) {

        estadoLabel.setText(mensaje);

        if (esError) {

            estadoLabel.setStyle(
                    "-fx-text-fill: #b42318;"
            );

        } else {

            estadoLabel.setStyle(
                    "-fx-text-fill: #176b4d;"
            );
        }
    }
}