/**
 * 
 */
package modelo;

import Dao.AccesoTrabajador;
import Excepciones.BDException;

import java.util.ArrayList;

/**
 * @author alumno
 *
 */
public class Empresa {
	
	ArrayList <Trabajador> trabajadores;
	private int proximoId;

	/**
	 * @param trabajadores
	 */
	public Empresa(ArrayList<Trabajador> trabajadores) {
		this.trabajadores = trabajadores;
		// Calcular el próximo ID (máximo actual + 1)
		this.proximoId = calcularProximoId();
	}
	
	/**
	 * Calcula el próximo ID disponible
	 * @return
	 */
	private int calcularProximoId() {
		int maxId = 0;
		for (Trabajador t : trabajadores) {
			if (t.getIdentificador() > maxId) {
				maxId = t.getIdentificador();
			}
		}
		return maxId + 1;
	}
	
	/**
	 * Comprueba si un trabajador est� en la lista por DNI
	 * @param t
	 * @return
	 */
	public boolean esta(Trabajador t){
		for(int i=0; i<trabajadores.size(); i++){
			if(trabajadores.get(i).getDni().equalsIgnoreCase(t.getDni())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Devuelve la posici�n en la que se encuentra un trabajador
	 * busc�ndolo por dni
	 * @param t
	 * @return
	 */
	public int devolverPosicion(int codigo){
		for(int i=0; i<trabajadores.size(); i++){
			if (trabajadores.get(i).getIdentificador() == codigo){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Si el trabajador no est� en la lista, lo a�ade con ID autoincremental
	 * @param t
	 */
	public boolean altaTrabajador(Trabajador t) throws BDException { // Añade el throws
		if(!esta(t)) {
			// 1. Intentamos guardar en la Base de Datos primero
			boolean insertadoBD = AccesoTrabajador.insertarTrabajador(t);

			if (insertadoBD) {
				// 2. Si la BD lo acepta, lo guardamos en la lista local
				t.setIdentificador(proximoId); // Opcional si usas autoincremental real
				proximoId++;
				trabajadores.add(t);
				return true;
			}
		}
		return false;
	}
	/**
	 * Da de baja un trabajador busc�ndolo por c�digo
	 * @param t
	 */
	public boolean bajaTrabajador(int codigo){
		int posicion = devolverPosicion(codigo);
		if(posicion > -1){
			trabajadores.remove(posicion);
			return true;
		}
		else return false;
	}
	/**
	 * Devuelve un trabajador
	 * @param dni
	 * @return
	 */
	public Trabajador buscarTrabajador(int codigo){		
		for(int i=0; i<trabajadores.size(); i++){
			if(trabajadores.get(i).getIdentificador() == codigo){
				return( trabajadores.get(i)); 
			}
		}
		return null;
	}
	/**
	 * Permite modificar el valor de los atributos de un objeto Trabajador
	 * @param dni
	 * @return
	 */
	public void modificarTrabajador(int codigo, String dni, String nombre, String apellidos, String direccion, String telefono, String puesto){
		int posicion = devolverPosicion(codigo);
		trabajadores.get(posicion).setDni(dni);
		trabajadores.get(posicion).setNombre(nombre);
		trabajadores.get(posicion).setApellidos(apellidos);
		trabajadores.get(posicion).setDireccion(direccion);
		trabajadores.get(posicion).setTelefono(telefono);
		trabajadores.get(posicion).setPuesto(puesto);
	}
	
	/**
	 * Devuelve una matriz que se utilizar� para listar los trabajadores
	 * @return
	 */
	public String[][] listarTrabajadores(){
		String [][] datos = new String[trabajadores.size()][7];
		for (int i=0; i<trabajadores.size(); i++){
			String[] fila = new String [7];
			
			fila[0] = Integer.toString(trabajadores.get(i).getIdentificador());
			fila[1] = trabajadores.get(i).getDni();
			fila[2] = trabajadores.get(i).getNombre();
			fila[3] = trabajadores.get(i).getApellidos();
			fila[4] = trabajadores.get(i).getDireccion();
			fila[5] = trabajadores.get(i).getTelefono();
			fila[6] = trabajadores.get(i).getPuesto();
			
			datos[i] = fila;
		}
		return datos;
	}

	public ArrayList<Trabajador> getTrabajadores() {
		return trabajadores;
	}

	public void setTrabajadores(ArrayList<Trabajador> trabajadores) {
		this.trabajadores = trabajadores;
	}
}
