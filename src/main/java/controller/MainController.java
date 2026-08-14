package controller;

import dao.TaskDAOImpl;
import javafx.application.Platform;
import model.Task;
import model.TaskStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class MainController {

    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, Integer> colId;
    @FXML private TableColumn<Task, String> colTitulo;
    @FXML private TableColumn<Task, String> colEstado;
    @FXML private TableColumn<Task, Integer> colPrioridad;
    @FXML private TableColumn<Task, String> colResponsable;
    @FXML private TableColumn<Task, String> colFechaLimite;

    @FXML private ComboBox<TaskStatus> filterCombo;
    @FXML private TextField searchField;
    @FXML private TextArea detailArea;   

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML private Label lblAviso;


    private TaskDAOImpl taskDAO = new TaskDAOImpl();
    private ObservableList<Task> taskList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        //columnas
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colPrioridad.setCellValueFactory(new PropertyValueFactory<>("prioridad"));
        colResponsable.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        colFechaLimite.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));

        //combobox de filtros
        filterCombo.getItems().setAll(TaskStatus.values());
        filterCombo.getSelectionModel().selectFirst();

        //cargar todas las tareas
        cargarTareas();

        //filtra en tiempo real
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filtrarPorTexto(newVal);
        });

        // filtra por estado
        filterCombo.setOnAction(event -> filtrarTareas());

        // muestradetalles al seleccionar una fila
        taskTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        mostrarDetalles(newSelection);
                    } else {
                        detailArea.clear();
                    }
                    boolean selected = newSelection != null;
                    editButton.setDisable(!selected);
                    deleteButton.setDisable(!selected);
                }
        );

        //notificación de tareas que estan proximas a finalizar
        Platform.runLater(() -> {
            mostrarNotificacionesVencimiento();
        });
    }

    public void cargarTareas() {
        List<Task> tareas = taskDAO.obtenerTodas();
        taskList.setAll(tareas);
        taskTable.setItems(taskList);
        mostrarNotificacionesVencimiento();
    }

    private void filtrarTareas() {
        TaskStatus estadoSeleccionado = filterCombo.getValue();
        if (estadoSeleccionado == null) {
            cargarTareas();
            return;
        }
        List<Task> filtradas = taskDAO.filtrarPorEstado(estadoSeleccionado);
        taskList.setAll(filtradas);
        taskTable.setItems(taskList);
        mostrarNotificacionesVencimiento();
    }

    private void filtrarPorTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            cargarTareas();
            return;
        }
        List<Task> todas = taskDAO.obtenerTodas();
        List<Task> filtradas = todas.stream()
                .filter(t -> t.getTitulo().toLowerCase().contains(texto.toLowerCase()))
                .toList();
        taskList.setAll(filtradas);
        taskTable.setItems(taskList);
    }

    private void mostrarDetalles(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append("Título: ").append(task.getTitulo()).append("\n");
        sb.append("Descripción: ").append(task.getDescripcion()).append("\n");
        sb.append("Estado: ").append(task.getEstado()).append("\n");
        sb.append("Prioridad: ").append(task.getPrioridad()).append("\n");
        sb.append("Responsable: ").append(task.getResponsable()).append("\n");
        sb.append("Fecha límite: ").append(task.getFechaLimite()).append("\n");
        sb.append("Fecha creación: ").append(task.getFechaCreacion());
        detailArea.setText(sb.toString());
    }

    public void mostrarNotificacionesVencimiento() {
        List<Task> proximas = taskDAO.obtenerProximasAVencer(3);
        if (proximas.isEmpty()) {
            lblAviso.setVisible(false);
            return;
        }

        // Determinar urgencia
        long hoy = LocalDate.now().toEpochDay();
        boolean urgente = proximas.stream().anyMatch(t -> t.getFechaLimite().toEpochDay() - hoy <= 1);

        lblAviso.setText("Tienes " + proximas.size() + " tarea(s) que vencen en los próximos 3 días.");
        lblAviso.setVisible(true);
        if (urgente) {
            lblAviso.setStyle("-fx-text-fill: #d9534f; -fx-font-weight: bold; -fx-font-size: 14px;");
        } else {
            lblAviso.setStyle("-fx-text-fill: #f0ad4e; -fx-font-weight: bold;");
        }

        // Mostrar lista en panel de detalles
        StringBuilder sb = new StringBuilder("Tareas próximas a vencer:\n");
        for (Task t : proximas) {
            sb.append("• ").append(t.getTitulo())
                    .append(" (Vence: ").append(t.getFechaLimite()).append(")\n");
        }
        detailArea.setText(sb.toString());
    }

    @FXML
    private void abrirFormularioNuevo() {
        mostrarFormulario(null);
    }

    @FXML
    private void abrirFormularioEditar() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            mostrarFormulario(selected);
        }
    }

    @FXML
    public void eliminarTarea() {
        Task selected = taskTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText("¿Estás seguro de eliminar la tarea?");
        alert.setContentText("Título: " + selected.getTitulo());

        if (alert.showAndWait().get() == ButtonType.OK) {
            taskDAO.eliminar(selected.getId());
            cargarTareas();
            filtrarTareas();
        }
    }

    private void mostrarFormulario(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/task-form.fxml"));
            Parent root = loader.load();

            TaskFormController controller = loader.getController();
            controller.setTask(task);
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle(task == null ? "Nueva Tarea" : "Editar Tarea");
            stage.setScene(new Scene(root, 500, 500));
            stage.showAndWait();

            cargarTareas();
            filtrarTareas();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo abrir el formulario");
        }
    }

    public void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}