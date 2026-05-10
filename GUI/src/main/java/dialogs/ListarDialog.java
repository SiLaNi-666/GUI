/**
 * 
 */
package dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import BD.ConfigMySQL;
import Dao.AccesoTrabajador;
import Excepciones.BDException;
import modelo.Empresa;
import modelo.Trabajador;

/**
 * 
 * @author usuario
 *
 */
public class ListarDialog extends JDialog implements ActionListener {

	Empresa empresa;
	JTable tabla;
	JButton cerrar;
	DefaultTableModel modelo;
	JButton modificar;

	public ListarDialog(Empresa empresa) {
		this.empresa = empresa;

		setResizable(false);
		// t�tulo del di�log
		setTitle("Listado Trabajadores");
		// tama�o
		setSize(750, 700);
		setLayout(new FlowLayout());
		// colocaci�n en el centro de la pantalla
		setLocationRelativeTo(null);

		// Crea un JTable, cada fila será un trabajador
		// 1. Mantenemos las columnas
		String[] columnas = { "Identificador", "DNI", "Nombre", "Apellidos", "Dirección", "Teléfono", "Puesto" };

		// 2. Creamos el modelo VACÍO (null)
		modelo = new DefaultTableModel(null, columnas);
		tabla = new JTable(modelo);

		// 3. ¡AQUÍ EL CAMBIO! Llamamos a la BBDD y rellenamos fila a fila
		try {
			// Pedimos la lista al DAO
			ArrayList<Trabajador> lista = (ArrayList<Trabajador>) AccesoTrabajador.consultarTrabajadores();

			// Recorremos la lista y añadimos al modelo
			for (Trabajador t : lista) {
				Object[] fila = {
						t.getIdentificador(),
						t.getDni(),
						t.getNombre(),
						t.getApellidos(),
						t.getDireccion(),
						t.getTelefono(),
						t.getPuesto()
				};
				modelo.addRow(fila); // Esta línea añade el trabajador a la tabla visual
			}
		} catch (BDException e) {
			JOptionPane.showMessageDialog(this, "Error al leer la base de datos: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		//                              recargarTabla();
		// Mete la tabla en un JCrollPane
		JScrollPane jsp = new JScrollPane(tabla);
		jsp.setPreferredSize(new Dimension(700, 600));
		add(jsp);

		//BOTON DE CERRAR Y MODIFICAR
		cerrar = new JButton("Cerrar");
		cerrar.addActionListener(this);
		add(cerrar);
		modificar = new JButton("Modificar");
		modificar.addActionListener(this);
		add(modificar); // Ponlo al lado del botón cerrar

		setVisible(true);
	}

	/**
	 * Metodo para rellenar la tabla de trabajadores para su uso en Eliminar Trabajador
	 * *Falta terminar el metodo e implementarlo en BajaDialog*
     */
	public void rellenarTabla() {
		modelo.setRowCount(0); // Esto limpia la tabla antes de cargarla
		try {
			// Usamos List para que no haya problemas de casteo
			java.util.List<Trabajador> listaTrabajadores = AccesoTrabajador.consultarTrabajadores();

			for (Trabajador t : listaTrabajadores) {
				Object[] fila = {
						t.getIdentificador(),
						t.getDni(),
						t.getNombre(),
						t.getApellidos(),
						t.getDireccion(),
						t.getTelefono(),
						t.getPuesto()
				};
				modelo.addRow(fila);
			}
		} catch (BDException e) {
			JOptionPane.showMessageDialog(this, "Error al cargar: " + e.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == cerrar) {
			dispose();
		}

		if (e.getSource() == modificar) {
			int fila = tabla.getSelectedRow();
			if (fila == -1) {
				JOptionPane.showMessageDialog(this, "Por favor, selecciona un trabajador de la tabla");
			} else {
				// Extraemos los datos de la fila (columna a columna)
				int id = (int) modelo.getValueAt(fila, 0);
				String dni = (String) modelo.getValueAt(fila, 1);
				String nom = (String) modelo.getValueAt(fila, 2);
				String ape = (String) modelo.getValueAt(fila, 3);
				String dir = (String) modelo.getValueAt(fila, 4);
				String tel = (String) modelo.getValueAt(fila, 5);
				String pue = (String) modelo.getValueAt(fila, 6);

				// Creamos un objeto con esos datos
				Trabajador aux = new Trabajador(id, dni, nom, ape, dir, tel, pue);

				// Abrimos el nuevo diálogo de modificación pasándole el trabajador y el 'this' (esta ventana)
				new ModificarDialog(empresa, aux, this);
			}
		}
	}
}
