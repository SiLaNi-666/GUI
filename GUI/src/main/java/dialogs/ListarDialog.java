/**
 * 
 */
package dialogs;

import java.awt.*;
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
	JButton buscar;
	JTextField campoBusqueda;

	public ListarDialog(Empresa empresa) {
		this.empresa = empresa;

		setResizable(false);
		setTitle("Listado Trabajadores");
		setSize(750, 750); // Un poco más de alto para que quepa el buscador

		// 1. CAMBIO CLAVE: Usamos BorderLayout para organizar por zonas (Norte, Centro, Sur)
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);

		// --- ZONA NORTE: EL BUSCADOR ---
		JPanel panelBusqueda = new JPanel(new FlowLayout());
		panelBusqueda.add(new JLabel("DNI/ID:"));
		campoBusqueda = new JTextField(15); // La cajita para escribir
		panelBusqueda.add(campoBusqueda);
		buscar = new JButton("Buscar");
		buscar.addActionListener(this);
		panelBusqueda.add(buscar);

		add(panelBusqueda, BorderLayout.NORTH); // Añadir arriba

		// --- ZONA CENTRAL: LA TABLA ---
		String[] columnas = { "Identificador", "DNI", "Nombre", "Apellidos", "Dirección", "Teléfono", "Puesto" };
		modelo = new DefaultTableModel(null, columnas);
		tabla = new JTable(modelo);

		// Rellenamos la tabla (usando tu lógica de BBDD)
		try {
			ArrayList<Trabajador> lista = (ArrayList<Trabajador>) AccesoTrabajador.consultarTrabajadores();
			for (Trabajador t : lista) {
				Object[] fila = { t.getIdentificador(), t.getDni(), t.getNombre(), t.getApellidos(), t.getDireccion(), t.getTelefono(), t.getPuesto() };
				modelo.addRow(fila);
			}
		} catch (BDException e) {
			JOptionPane.showMessageDialog(this, "Error al leer la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
		}

		JScrollPane jsp = new JScrollPane(tabla);
		add(jsp, BorderLayout.CENTER); // La tabla se expande en el centro

		// --- ZONA SUR: LOS BOTONES DE ACCIÓN ---
		JPanel panelInferior = new JPanel(new FlowLayout());

		modificar = new JButton("Modificar");
		modificar.addActionListener(this);
		panelInferior.add(modificar);

		cerrar = new JButton("Cerrar");
		cerrar.addActionListener(this);
		panelInferior.add(cerrar);

		add(panelInferior, BorderLayout.SOUTH); // Los botones se quedan abajo

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

		if (e.getSource() == buscar) {
			String texto = campoBusqueda.getText();
			if (texto.isEmpty()) {
				rellenarTabla(); // Si el campo está vacío, mostramos todos otra vez
			} else {
				try {
					Trabajador t = AccesoTrabajador.buscarTrabajador(texto);
					if (t != null) {
						modelo.setRowCount(0); // Borramos la tabla actual
						Object[] fila = { t.getIdentificador(), t.getDni(), t.getNombre(),
								t.getApellidos(), t.getDireccion(), t.getTelefono(), t.getPuesto() };
						modelo.addRow(fila); // Añadimos solo al encontrado
					} else {
						JOptionPane.showMessageDialog(this, "No se ha encontrado ningún trabajador con ese ID/DNI");
					}
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
				}
			}
		}
	}
}
