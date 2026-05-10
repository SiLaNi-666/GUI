package Dao;

import BD.ConfigMySQL;
import Excepciones.BDException;
import modelo.Trabajador;

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
                insertarTrabajador(t);
            } catch (BDException e) {
                System.out.println("El trabajador " + t.getDni() + " ya estaba en la BD o hubo un error.");
            }
        }
    }

    //Hacer metodo para ir linea por linea del ArrayList e insertar esas lineas


}
