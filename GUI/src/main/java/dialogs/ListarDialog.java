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
	JTextField campoBusqueda;
	JButton buscar;
	JButton modificar;
	JButton btnExportarCSV;
	JButton btnExportarJSON;



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

		// Busca donde creas los botones modificar y cerrar, y añade esto:
		btnExportarCSV = new JButton("Exportar CSV");
		btnExportarCSV.addActionListener(this); // El cable para que funcione
		panelInferior.add(btnExportarCSV);

		btnExportarJSON = new JButton("Exportar JSON");
		btnExportarJSON.addActionListener(this); // El cable para que funcione
		panelInferior.add(btnExportarJSON);

		add(panelInferior, BorderLayout.SOUTH); // Los botones se quedan abajo

		setVisible(true);
	}

	/**
	 * Metodo para rellenar la tabla de trabajadores para su uso en Eliminar Trabajador
	 * *Falta terminar el metodo e implementarlo en BajaDialog*
     */
	public void rellenarTabla() {
		modelo.setRowCount(0); // Borra la tabla vieja
		try {
			ArrayList<Trabajador> lista = (ArrayList<Trabajador>) AccesoTrabajador.consultarTrabajadores();
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
				modelo.addRow(fila); // Dibuja todos los trabajadores reales de la BD
			}
		} catch (BDException e) {
			JOptionPane.showMessageDialog(this, "Error al recargar: " + e.getMessage());
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
				JOptionPane.showMessageDialog(this, "Por favor, selecciona un trabajador de la tabla para modificar.");
			} else {
				// Obtenemos los valores de la fila que el usuario ha pinchado
				int id = (int) modelo.getValueAt(fila, 0);
				String dni = (String) modelo.getValueAt(fila, 1).toString();
				String nom = (String) modelo.getValueAt(fila, 2).toString();
				String ape = (String) modelo.getValueAt(fila, 3).toString();
				String dir = (String) modelo.getValueAt(fila, 4).toString();
				String tel = (String) modelo.getValueAt(fila, 5).toString();
				String pue = (String) modelo.getValueAt(fila, 6).toString();

				// Construimos el objeto temporal
				Trabajador aux = new Trabajador(id, dni, nom, ape, dir, tel, pue);

				// Abrimos la ventana que acabamos de crear pasándole los datos
// Busca donde tenías: new ModificarDialog(empresa, aux, this);
// Y cámbialo por esto:
				new ModificarDialog((JFrame) SwingUtilities.getWindowAncestor(this), empresa);			}
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
						Object[] fila = {
								t.getIdentificador(),
								t.getDni(),
								t.getNombre(),
								t.getApellidos(),
								t.getDireccion(),
								t.getTelefono(),
								t.getPuesto()
						};
						modelo.addRow(fila); // Añadimos solo al encontrado
					} else {
						JOptionPane.showMessageDialog(this, "No se ha encontrado ningún trabajador con ese ID/DNI");
					}
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
				}
			}
		}

		if (e.getSource() == btnExportarCSV) {
			JFileChooser selectorArchivo = new JFileChooser();
			selectorArchivo.setDialogTitle("Guardar archivo CSV");

			// Sugerimos un nombre de archivo por defecto
			selectorArchivo.setSelectedFile(new java.io.File("trabajadores.csv"));

			// 2. Abrimos la ventana de "Guardar"
			int seleccion = selectorArchivo.showSaveDialog(this);

			// 3. Si el usuario hace clic en "Guardar" (Aceptar)
			if (seleccion == JFileChooser.APPROVE_OPTION) {
				try {
					// Obtenemos la ruta absoluta que ha elegido el usuario
					String rutaElegida = selectorArchivo.getSelectedFile().getAbsolutePath();

					// Forzamos a que termine en .csv si el usuario se olvidó de escribirlo
					if (!rutaElegida.toLowerCase().endsWith(".csv")) {
						rutaElegida += ".csv";
					}

					// Llamamos a tu método del DAO pasándole la ruta elegida
					AccesoTrabajador.exportarACSV(rutaElegida);

					JOptionPane.showMessageDialog(this, "¡Archivo CSV guardado con éxito!",
							"Exportación Completada", JOptionPane.INFORMATION_MESSAGE);
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		if (e.getSource() == btnExportarJSON) {
			// 1. Creamos el selector de archivos
			JFileChooser selectorArchivo = new JFileChooser();
			selectorArchivo.setDialogTitle("Guardar archivo JSON");

			// Sugerimos un nombre de archivo por defecto
			selectorArchivo.setSelectedFile(new java.io.File("trabajadores.json"));

			// 2. Abrimos la ventana de "Guardar"
			int seleccion = selectorArchivo.showSaveDialog(this);

			// 3. Si el usuario hace clic en "Guardar"
			if (seleccion == JFileChooser.APPROVE_OPTION) {
				try {
					// Obtenemos la ruta absoluta elegida
					String rutaElegida = selectorArchivo.getSelectedFile().getAbsolutePath();

					// Forzamos la extensión .json
					if (!rutaElegida.toLowerCase().endsWith(".json")) {
						rutaElegida += ".json";
					}

					// Llamamos a tu método del DAO con la ruta dinámica
					AccesoTrabajador.exportarAJSON(rutaElegida);

					JOptionPane.showMessageDialog(this, "¡Archivo JSON guardado con éxito!",
							"Exportación Completada", JOptionPane.INFORMATION_MESSAGE);
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
}
