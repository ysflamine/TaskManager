package controller;

import model.Task;
import model.TaskStatus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import dao.TaskDAOImpl;
import java.time.LocalDate;

public class TaskFormController {

    @FXML private TextField tituloField;
    @FXML private TextArea descripcionArea;
    @FXML private ComboBox<TaskStatus> estadoCombo;
    @FXML private Spinner<Integer> prioridadSpinner;
    @FXML private TextField responsableField;
    @FXML private DatePicker fechaLimitePicker;
    @FXML private Button guardarButton;
    @FXML private Button cancelarButton;

    private Task task;
    private TaskDAOImpl taskDAO = new TaskDAOImpl();
    private MainController mainController;

    @FXML
    public void initialize() {
        // configurar prioridad (1-5)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5);
        prioridadSpinner.setValueFactory(valueFactory);
        prioridadSpinner.setEditable(true);

        // configurar combobox estados
        estadoCombo.getItems().setAll(TaskStatus.values());
        estadoCombo.getSelectionModel().selectFirst();
    }

    public void setTask(Task task) {
        this.task = task;
        if (task != null) {
            tituloField.setText(task.getTitulo());
            descripcionArea.setText(task.getDescripcion());
            estadoCombo.setValue(task.getEstado());
            prioridadSpinner.getValueFactory().setValue(task.getPrioridad());
            responsableField.setText(task.getResponsable());
            if (task.getFechaLimite() != null) {
                fechaLimitePicker.setValue(task.getFechaLimite());
            }
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void guardarTarea() {
        if (!validarCampos()) return;

        String titulo = tituloField.getText().trim();
        String descripcion = descripcionArea.getText().trim();
        TaskStatus estado = estadoCombo.getValue();
        int prioridad = prioridadSpinner.getValue();
        String responsable = responsableField.getText().trim();
        LocalDate fechaLimite = fechaLimitePicker.getValue();

        if (task == null) {
            //nueva tarea
            Task nueva = new Task(titulo, descripcion, prioridad, estado, responsable, fechaLimite);
            taskDAO.guardar(nueva);
        } else {
            //editar tarea existente
            task.setTitulo(titulo);
            task.setDescripcion(descripcion);
            task.setEstado(estado);
            task.setPrioridad(prioridad);
            task.setResponsable(responsable);
            task.setFechaLimite(fechaLimite);
            taskDAO.actualizar(task);
        }

        cerrarVentana();
    }

    private boolean validarCampos() {
        if (tituloField.getText().trim().isEmpty()) {
            mainController.mostrarError("El título es obligatorio");
            tituloField.requestFocus();
            return false;
        }
        return true;
    }

    @FXML
    public void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) guardarButton.getScene().getWindow();
        stage.close();
    }
}