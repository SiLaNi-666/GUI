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

import javax.swing.JButton;
import javax.swing.JDialog;

import javax.swing.JScrollPane;
import javax.swing.JTable;
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
		String[] columnas = { "Identificador", "DNI", "Nombre", "Apellidos", "Direcci�n", "Tel�fono", "Puesto" };
		String[][] datos = empresa.listarTrabajadores();
		modelo = new DefaultTableModel(null, columnas);
		tabla = new JTable(modelo);
		//                              recargarTabla();
		// Mete la tabla en un JCrollPane
		JScrollPane jsp = new JScrollPane(tabla);
		jsp.setPreferredSize(new Dimension(700, 600));
		add(jsp);

		cerrar = new JButton("Cerrar");
		cerrar.addActionListener(this);
		add(cerrar);

		setVisible(true);
	}

	/**
	 * Metodo para rellenar la tabla de trabajadores para su uso en Eliminar Trabajador
	 * *Falta terminar el metodo e implementarlo en BajaDialog*
	 * @param e the event to be processed
	 */
	/*
	public void rellenarTabla(){
		modelo.setRowCount(0); //Limpiar la tabla
		ArrayList<Trabajador> listaTrabajadores = AccesoTrabajador.consultarTrabajadores();

		//Recorrer la list y añadir los datos a la tabla
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
	}
	*/

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == cerrar) {
			dispose();
		}
	}

}
