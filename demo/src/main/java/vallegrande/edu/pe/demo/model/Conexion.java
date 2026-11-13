package vallegrande.edu.pe.demo.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexión JDBC a MySQL (contenedor Docker).
 */
public class Conexion {

    private static final String SERVIDOR = "localhost";
    private static final String PUERTO = "3306";
    private static final String BASE_DATOS = "imprenta_db";
    private static final String USUARIO = "root";
    private static final String CLAVE = "123456";

    private static final String URL = "jdbc:mysql://" + SERVIDOR + ":" + PUERTO + "/" + BASE_DATOS
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Lima"
            + "&characterEncoding=UTF-8";

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
