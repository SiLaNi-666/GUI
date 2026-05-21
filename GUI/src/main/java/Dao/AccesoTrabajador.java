package Dao;

import BD.ConfigMySQL;
import Excepciones.BDException;
import modelo.Trabajador;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AccesoTrabajador {   //QUITAR EL IDENTIFICADOR EN LAS CONSULTAS YA QUE ES AUTOINCREMENTAL

    /**
     * Metodo para insertar un trabajador en la base de datos
     * @param trabajador
     * @return
     * @throws BDException
     */
    public static boolean insertarTrabajador (Trabajador trabajador) throws BDException{
        Connection conexion = null;
        int insertado = 0;

        try{

            conexion = ConfigMySQL.abrirConexion();
            String insercion = "INSERT INTO Trabajador (dni, nombre, apellidos, direccion, telefono, puesto) " +
                    "VALUES (?,?,?,?,?,?);";
            PreparedStatement sentencia = conexion.prepareStatement(insercion);
            int identificador = trabajador.getIdentificador();
            String dni = trabajador.getDni();
            String nombre = trabajador.getNombre();
            String apellidos = trabajador.getApellidos();
            String direccion = trabajador.getDireccion();
            String telefono = trabajador.getTelefono();
            String puesto = trabajador.getPuesto();

            sentencia.setString(1, dni);
            sentencia.setString(2, nombre);
            sentencia.setString(3, apellidos);
            sentencia.setString(4, direccion);
            sentencia.setString(5, telefono);
            sentencia.setString(6, puesto);

            insertado = sentencia.executeUpdate();

        }catch (SQLException e){
            actualizarTrabajador(trabajador);
        }catch (BDException e){
            throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
        }
        finally {
            if(conexion != null){
                ConfigMySQL.cerrarConexion(conexion);
            }
        }
        return insertado > 0;
    }

    /**
     * Metodo para eliminar un trabajador de la base de datos
     * @param identificador
     * @return
     * @throws BDException
     */
    public static boolean eliminarTrabajador (int identificador) throws BDException{
        Connection conexion = null;
        int filasAfectadas;

        try{
            conexion = ConfigMySQL.abrirConexion();
            String eliminar = "DELETE FROM Trabajador WHERE identificador = ?;";
            PreparedStatement sentencia = conexion.prepareStatement(eliminar);

            sentencia.setInt(1, identificador);
            filasAfectadas = sentencia.executeUpdate();

        }catch (SQLException e){
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
        finally {
            if(conexion != null){
                ConfigMySQL.cerrarConexion(conexion);
            }
        }
        return filasAfectadas > 0;
    }

    public static boolean actualizarTrabajador (Trabajador trabajador) throws BDException{
        Connection conexion = null;
        int filasActualizadas;

        try{
            conexion = ConfigMySQL.abrirConexion();
            String actualizar = "UPDATE Trabajador SET dni = ?, nombre = ?, " +
                    "apellidos = ?, direccion = ?, telefono = ?, puesto = ? WHERE identificador = ?";
            PreparedStatement sentencia = conexion.prepareStatement(actualizar);

            int identificador = trabajador.getIdentificador();
            String dni = trabajador.getDni();
            String nombre = trabajador.getNombre();
            String apellidos = trabajador.getApellidos();
            String direccion = trabajador.getDireccion();
            String telefono = trabajador.getTelefono();
            String puesto = trabajador.getPuesto();

            sentencia.setString(1, dni);
            sentencia.setString(2, nombre);
            sentencia.setString(3, apellidos);
            sentencia.setString(4, direccion);
            sentencia.setString(5, telefono);
            sentencia.setString(6, puesto);
            sentencia.setInt(7, identificador);

            filasActualizadas = sentencia.executeUpdate();

        }catch (SQLException e){
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
        finally {
            if(conexion != null){
                ConfigMySQL.cerrarConexion(conexion);
            }
        }
        return filasActualizadas > 0;
    }

    public static List<Trabajador> consultarTrabajadores() throws BDException{
        Connection conexion = null;
        List<Trabajador> listaTrabajadores = new ArrayList<>();

        try{
            conexion = ConfigMySQL.abrirConexion();
            String consulta = "SELECT * FROM Trabajador";
            PreparedStatement sentencia = conexion.prepareStatement(consulta);
            ResultSet rs = sentencia.executeQuery();

            while (rs.next()){
                int identificador = rs.getInt("Identificador");
                String dni = rs.getString("DNI");
                String nombre = rs.getString("Nombre");
                String apellidos = rs.getString("Apellidos");
                String direccion = rs.getString("Direccion");
                String telefono = rs.getString("Telefono");
                String puesto = rs.getString("Puesto");

                Trabajador trabajador = new Trabajador(identificador, dni, nombre, apellidos, direccion, telefono, puesto);
                listaTrabajadores.add(trabajador);
            }
        }catch (SQLException e){
            throw new BDException(BDException.ERROR_QUERY + e.getMessage());
        }
        finally {
            if (conexion != null) {
                ConfigMySQL.cerrarConexion(conexion);
            }
        }

        return listaTrabajadores;
    }

    /**
     * Metodo para volcar los datos de un fichero a la base de datos
     * @param lista
     */
    public static void volcarFicheroABBDD(List<Trabajador> lista) {
        for (Trabajador t : lista) {
            try {
                // Buscamos si ya está para no duplicar por error
                if (buscarTrabajador(t.getDni()) == null) {
                    insertarTrabajador(t);
                    System.out.println("Trabajador " + t.getDni() + " volcado con éxito.");
                }
            } catch (BDException e) {
                System.out.println("No se pudo volcar el DNI " + t.getDni() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Para buscar un trabajador por su DNI o por su Identificador en el ListarDialog.
     * @param criterio
     * @return
     * @throws BDException
     */
    public static Trabajador buscarTrabajador(String criterio) throws BDException {
        Connection conexion = null;
        Trabajador t = null;

        try {
            conexion = ConfigMySQL.abrirConexion();
            // Buscamos si coincide con el DNI o con el Identificador exacto
            String sql = "SELECT * FROM Trabajador WHERE dni = ? OR identificador = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, criterio);
            sentencia.setString(2, criterio);

            ResultSet rs = sentencia.executeQuery();

            if (rs.next()) {
                // Si lo encuentra, creamos el objeto con sus datos reales de la BD
                t = new Trabajador(
                        rs.getInt("Identificador"),
                        rs.getString("DNI"),
                        rs.getString("Nombre"),
                        rs.getString("Apellidos"),
                        rs.getString("Direccion"),
                        rs.getString("Telefono"),
                        rs.getString("Puesto")
                );
            }
        } catch (SQLException e) {
            throw new BDException("Error en la búsqueda de la base de datos: " + e.getMessage());
        } finally {
            if (conexion != null) {
                ConfigMySQL.cerrarConexion(conexion);
            }
        }
        return t; // Devuelve el trabajador encontrado, o null si no existe
    }

    public static void exportarACSV(String rutaArchivo) throws BDException {
        Connection conexion = null;
        try {
            conexion = ConfigMySQL.abrirConexion();
            String sql = "SELECT * FROM Trabajador";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            ResultSet rs = sentencia.executeQuery();

            // Usamos BufferedWriter para escribir el archivo de texto
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
                // 1. Escribimos la cabecera del CSV separados por comas (o punto y coma)
                bw.write("Identificador,DNI,Nombre,Apellidos,Direccion,Telefono,Puesto");
                bw.newLine();

                // 2. Recorremos los trabajadores y los escribimos fila a fila
                while (rs.next()) {
                    String fila = rs.getInt("Identificador") + ","
                            + rs.getString("DNI") + ","
                            + rs.getString("Nombre") + ","
                            + rs.getString("Apellidos") + ","
                            + rs.getString("Direccion") + ","
                            + rs.getString("Telefono") + ","
                            + rs.getString("Puesto");
                    bw.write(fila);
                    bw.newLine();
                }
            } catch (IOException e) {
                throw new BDException("Error al escribir el archivo CSV: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new BDException("Error de base de datos al exportar CSV: " + e.getMessage());
        } finally {
            ConfigMySQL.cerrarConexion(conexion);
        }
    }

    public static void exportarAJSON(String rutaArchivo) throws BDException {
        Connection conexion = null;
        try {
            conexion = ConfigMySQL.abrirConexion();
            String sql = "SELECT * FROM Trabajador";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            ResultSet rs = sentencia.executeQuery();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
                bw.write("["); // Inicio del array JSON
                bw.newLine();

                boolean esPrimero = true;

                while (rs.next()) {
                    if (!esPrimero) {
                        bw.write(","); // Separador entre objetos JSON
                        bw.newLine();
                    }
                    esPrimero = false;

                    // Construimos el formato JSON manualmente {"clave": "valor"}
                    String jsonObjeto = "  {\n"
                            + "    \"identificador\": " + rs.getInt("Identificador") + ",\n"
                            + "    \"dni\": \"" + rs.getString("DNI") + "\",\n"
                            + "    \"nombre\": \"" + rs.getString("Nombre") + "\",\n"
                            + "    \"apellidos\": \"" + rs.getString("Apellidos") + "\",\n"
                            + "    \"direccion\": \"" + rs.getString("Direccion") + "\",\n"
                            + "    \"telefono\": \"" + rs.getString("Telefono") + "\",\n"
                            + "    \"puesto\": \"" + rs.getString("Puesto") + "\"\n"
                            + "  }";

                    bw.write(jsonObjeto);
                }

                bw.newLine();
                bw.write("]"); // Fin del array JSON
            } catch (IOException e) {
                throw new BDException("Error al escribir el archivo JSON: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new BDException("Error de base de datos al exportar JSON: " + e.getMessage());
        } finally {
            ConfigMySQL.cerrarConexion(conexion);
        }
    }

}
