package vallegrande.edu.pe.demo.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla Impreta.
 */
public class PedidoDAO {

    /** Inserta un pedido y devuelve el id generado. */
    public int registrar(Pedido p) throws SQLException {
        String sql = "INSERT INTO Impreta (nombre_cliente, tipo_trabajo, cantidad, formato, "
                + "fecha_pedido, fecha_entrega, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNombreCliente());
            ps.setString(2, p.getTipoTrabajo());
            ps.setInt(3, p.getCantidad());
            ps.setString(4, p.getFormato());
            ps.setDate(5, Date.valueOf(p.getFechaPedido()));
            ps.setDate(6, Date.valueOf(p.getFechaEntrega()));
            ps.setString(7, p.getEstado());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdPedido(rs.getInt(1));
                }
            }
            return p.getIdPedido();
        }
    }

    /** Devuelve todos los pedidos ordenados por id. */
    public List<Pedido> listar() throws SQLException {
        String sql = "SELECT * FROM Impreta ORDER BY id_pedido";
        List<Pedido> lista = new ArrayList<>();

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    /** Busca por cliente, tipo de trabajo o estado. */
    public List<Pedido> buscar(String filtro) throws SQLException {
        String sql = "SELECT * FROM Impreta WHERE nombre_cliente LIKE ? OR tipo_trabajo LIKE ? "
                + "OR estado LIKE ? ORDER BY id_pedido";
        List<Pedido> lista = new ArrayList<>();
        String patron = "%" + filtro + "%";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, patron);
            ps.setString(2, patron);
            ps.setString(3, patron);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    /** Actualiza un pedido existente. Devuelve true si modificó una fila. */
    public boolean actualizar(Pedido p) throws SQLException {
        String sql = "UPDATE Impreta SET nombre_cliente = ?, tipo_trabajo = ?, cantidad = ?, "
                + "formato = ?, fecha_pedido = ?, fecha_entrega = ?, estado = ? WHERE id_pedido = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombreCliente());
            ps.setString(2, p.getTipoTrabajo());
            ps.setInt(3, p.getCantidad());
            ps.setString(4, p.getFormato());
            ps.setDate(5, Date.valueOf(p.getFechaPedido()));
            ps.setDate(6, Date.valueOf(p.getFechaEntrega()));
            ps.setString(7, p.getEstado());
            ps.setInt(8, p.getIdPedido());

            return ps.executeUpdate() > 0;
        }
    }

    /** Elimina un pedido por id. Devuelve true si eliminó una fila. */
    public boolean eliminar(int idPedido) throws SQLException {
        String sql = "DELETE FROM Impreta WHERE id_pedido = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            return ps.executeUpdate() > 0;
        }
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setIdPedido(rs.getInt("id_pedido"));
        p.setNombreCliente(rs.getString("nombre_cliente"));
        p.setTipoTrabajo(rs.getString("tipo_trabajo"));
        p.setCantidad(rs.getInt("cantidad"));
        p.setFormato(rs.getString("formato"));
        p.setFechaPedido(rs.getDate("fecha_pedido").toLocalDate());
        p.setFechaEntrega(rs.getDate("fecha_entrega").toLocalDate());
        p.setEstado(rs.getString("estado"));
        return p;
    }
}
