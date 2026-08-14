package dao;

import model.Task;
import model.TaskStatus;
import db.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskDAOImpl implements TaskDAO {

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Task> obtenerTodas() {
        List<Task> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas ORDER BY id";

        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tareas.add(mapearTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tareas;
    }

    @Override
    public Task obtenerPorId(int id) {
        String sql = "SELECT * FROM tareas WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTask(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void guardar(Task tarea) {
        String sql = "INSERT INTO tareas (titulo, descripcion, prioridad, estado, responsable, fecha_limite) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tarea.getTitulo());
            pstmt.setString(2, tarea.getDescripcion());
            pstmt.setInt(3, tarea.getPrioridad());
            pstmt.setString(4, tarea.getEstado().name());
            pstmt.setString(5, tarea.getResponsable());
            pstmt.setDate(6, tarea.getFechaLimite() != null ? Date.valueOf(tarea.getFechaLimite()) : null);

            pstmt.executeUpdate();
            System.out.println("DEBUG: Tarea insertada en BD correctamente");

            // para obtner el id generado
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tarea.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar(Task tarea) {
        String sql = "UPDATE tareas SET titulo = ?, descripcion = ?, prioridad = ?, estado = ?, responsable = ?, fecha_limite = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, tarea.getTitulo());
            pstmt.setString(2, tarea.getDescripcion());
            pstmt.setInt(3, tarea.getPrioridad());
            pstmt.setString(4, tarea.getEstado().name());
            pstmt.setString(5, tarea.getResponsable());
            pstmt.setDate(6, tarea.getFechaLimite() != null ? Date.valueOf(tarea.getFechaLimite()) : null);
            pstmt.setInt(7, tarea.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM tareas WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> filtrarPorEstado(TaskStatus estado) {
        List<Task> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas WHERE estado = ? ORDER BY id";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, estado.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tareas.add(mapearTask(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tareas;
    }

    //metodo privado para mapear un resultset a un objeto Task
    private Task mapearTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitulo(rs.getString("titulo"));
        task.setDescripcion(rs.getString("descripcion"));
        task.setPrioridad(rs.getInt("prioridad"));
        task.setEstado(TaskStatus.valueOf(rs.getString("estado")));
        task.setResponsable(rs.getString("responsable"));

        Date fechaLimite = rs.getDate("fecha_limite");
        if (fechaLimite != null) {
            task.setFechaLimite(fechaLimite.toLocalDate());
        }

        Timestamp fechaCreacion = rs.getTimestamp("fecha_creacion");
        if (fechaCreacion != null) {
            task.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }

        return task;
    }
    @Override
    public List<Task> buscarPorTexto(String texto) {
        List<Task> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas WHERE LOWER(titulo) LIKE LOWER(?) OR LOWER(descripcion) LIKE LOWER(?) ORDER BY id";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            String busqueda = "%" + texto + "%";
            pstmt.setString(1, busqueda);
            pstmt.setString(2, busqueda);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tareas.add(mapearTask(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tareas;
    }
    @Override
    public List<Task> obtenerProximasAVencer(int dias) {
        List<Task> tareas = new ArrayList<>();
        String sql = "SELECT * FROM tareas WHERE fecha_limite BETWEEN CURRENT_DATE AND CURRENT_DATE + (? * INTERVAL '1 day') ORDER BY fecha_limite";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, dias);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tareas.add(mapearTask(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tareas;
    }
}