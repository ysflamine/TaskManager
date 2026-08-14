package dao;

import model.Task;
import model.TaskStatus;
import java.util.List;

//TaskDAO define los métodos que tendrá el DAO
public interface TaskDAO {
    List<Task> obtenerTodas();
    Task obtenerPorId(int id);
    void guardar(Task tarea);
    void actualizar(Task tarea);
    void eliminar(int id);
    List<Task> filtrarPorEstado(TaskStatus estado);
    List<Task> buscarPorTexto(String texto);
    List<Task> obtenerProximasAVencer(int dias);
}