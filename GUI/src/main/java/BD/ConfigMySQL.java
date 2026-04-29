package BD;

import Excepciones.BDException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigMySQL {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URLBD = "jdbc:mysql://localhost:3306/empresa";

    private static final String usuario = "prompt";
    private static final String contraseña = "case";

    /**
     * Abre conexi�n con la base de datos mysql
     * @return
     * @throws BDException
     */
    public static Connection abrirConexion() throws BDException {
        Connection conexion = null;

        try {
            // Carga el driver
            Class.forName(DRIVER);
            // Abre conexi�n
            conexion = DriverManager.getConnection(URLBD, usuario, contraseña);
        }
        catch (ClassNotFoundException e) {
            throw new BDException(BDException.ERROR_CARGAR_DRIVER + e.getMessage());
        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
        }

        return conexion;

    }

    /**
     * Cierra conexi�n con la base de datos
     * @param conexion
     * @throws BDException
     */
    public static void cerrarConexion(Connection conexion) throws BDException {

        try {
            conexion.close();
        } catch (SQLException e) {
            throw new BDException(BDException.ERROR_CERRAR_CONEXION + e.getMessage() );
        }


    }

}
