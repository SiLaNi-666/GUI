/**
 *
 */
package dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Excepciones.BDException;
import modelo.Empresa;
import modelo.Trabajador;

/**
 *
 * @author usuario
 *
 */
public class AltaDialog extends JDialog implements ActionListener, ItemListener {

	/**
	 * Elementos del JFrame
	 */
	//JLabel etiquetaIdentificador; BORRADOS POR AUTOINCREMENTAL
	//JTextField areaIdentificador;
	JLabel etiquetaDni;
	JTextField areaDni;
	JLabel etiquetaNombre;
	JTextField areaNombre;
	JLabel etiquetaApellidos;
	JTextField areaApellidos;
	JLabel etiquetaDireccion;
	JTextField areaDireccion;
	JLabel etiquetaTelefono;
	JTextField areaTelefono;
	JLabel etiquetaPuesto;
	JComboBox comboPuesto;
	JButton aceptar;
	JButton cancelar;
	/**
	 * Variables a las que se pasar� el contenido de los JTextField y del combo box
	 */
	int id = 0;
	String dni = "";
	String nombre = "";
	String apellidos = "";
	String direccion = "";
	String telefono = "";
	String puesto = "";

	//JPanel pIdentificador; BORRADO POR AUTOINCREMENTAL
	JPanel pDni;
	JPanel pNombre;
	JPanel pApellidos;
	JPanel pDireccion;
	JPanel pTelefono;
	JPanel pPuesto;
	JPanel pBotones;

	Empresa empresa;

	public AltaDialog(Empresa empresa) {
		this.empresa = empresa;
		setResizable(false);
		// t�tulo del di�log
		setTitle("Alta Trabajador");
		setSize(300, 350);
		setLayout(new FlowLayout());

		setLocationRelativeTo(null);

		// una fila por JPanel
		//pIdentificador = new JPanel(); BORRADO POR AUTOINCREMENTAL
		pDni = new JPanel();
		pNombre = new JPanel();
		pApellidos = new JPanel();
		pDireccion = new JPanel();
		pTelefono = new JPanel();
		pPuesto = new JPanel();
		pBotones = new JPanel();

		// Se crean los elementos y se añaden.  BORRADO POR AUTOINCREMENTAL
		//etiquetaIdentificador = new JLabel("Identificador");
		//areaIdentificador = new JTextField(15);
		// Se añaden al JPanel
		//pIdentificador.add(etiquetaIdentificador);
		//pIdentificador.add(areaIdentificador);

		// Se crean los elementos y se añaden
		etiquetaDni = new JLabel("DNI                 ");
		areaDni = new JTextField(15);
		// Se añaden al JPanel
		pDni.add(etiquetaDni);
		pDni.add(areaDni);

		// Se crean los elementos y se añaden
		etiquetaNombre = new JLabel("Nombre         ");
		areaNombre = new JTextField(15);
		// Se añaden al JPanel
		pNombre.add(etiquetaNombre);
		pNombre.add(areaNombre);

		// Se crean los elementos y se a�aden
		etiquetaApellidos = new JLabel("Apellidos      ");
		areaApellidos = new JTextField(15);
		// Se añaden al JPanel
		pApellidos.add(etiquetaApellidos);
		pApellidos.add(areaApellidos);

		// Se crean los elementos y se añaden
		etiquetaDireccion = new JLabel("Direccion      ");
		areaDireccion = new JTextField(15);
		// Se añaden al JPanel
		pDireccion.add(etiquetaDireccion);
		pDireccion.add(areaDireccion);

		// Se crean los elementos y se a�aden
		etiquetaTelefono = new JLabel("Telefono       ");
		areaTelefono = new JTextField(15);
		// Se añaden al JPanel
		pTelefono.add(etiquetaTelefono);
		pTelefono.add(areaTelefono);

		// Se crean los elementos y se añaden
		etiquetaPuesto = new JLabel("Puesto                         ");
		pPuesto.add(etiquetaPuesto);
		// lista desplegable
		comboPuesto = new JComboBox();
		comboPuesto.addItem("Elija Puesto");
		comboPuesto.addItem("Programador");
		comboPuesto.addItem("Analista");
		comboPuesto.addItem("Arquitecto");
		comboPuesto.addItem("Jefe de Proyecto");
		comboPuesto.addItemListener(this);
		pPuesto.add(comboPuesto);

		// Añadir al JDialog los JPanel
		//add(pIdentificador); BORRADO POR AUTOINCREMENTAL
		add(pDni);
		add(pNombre);
		add(pApellidos);
		add(pDireccion);
		add(pTelefono);
		add(pPuesto);

		aceptar = new JButton("Aceptar");
		aceptar.addActionListener(this);
		pBotones.add(aceptar);

		cancelar = new JButton("Cancelar");
		cancelar.addActionListener(this);
		pBotones.add(cancelar);

		add(pBotones);

		// Visible
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		puesto = comboPuesto.getSelectedItem().toString();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == aceptar) {
			dni = areaDni.getText();
			nombre = areaNombre.getText();
			apellidos = areaApellidos.getText();
			direccion = areaDireccion.getText();
			telefono = areaTelefono.getText();
			// En actionPerformed, dentro del if(e.getSource() == aceptar)
			if (comprobarErrores()) {
				try {
					Trabajador t = new Trabajador(0, dni, nombre, apellidos, direccion, telefono, puesto);
					if (empresa.altaTrabajador(t)) {
						// OPCIONAL: Guardar en el .dat aquí mismo para seguridad total
						ficheros.FicheroDatos.escribirTrabajadores("ficheroDatos\\empresa.dat", empresa.getTrabajadores());

						JOptionPane.showMessageDialog(null, "Datos introducidos correctamente en BD y Fichero");
						dispose();
					} else {
						JOptionPane.showMessageDialog(null, "El trabajador ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (BDException ex) {
					JOptionPane.showMessageDialog(null, "Error al conectar con la BD: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
				}
			}

		} else if (e.getSource() == cancelar) {
			dispose();
		}

	}

	/**
	 * M�todo que comprueba si no hay ning�n campo vac�o o si la longitud de los
	 * campos es la correcta
	 *
	 * @return
	 */
	public boolean comprobarErrores() {
		if (dni.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe introducir el DNI del trabajador. CAMPO OBLIGATORIO", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (dni.length() != 9){
			JOptionPane.showMessageDialog(null, "El DNI debe tener longitud 9", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (!dni.matches("[0-9]{8}[A-Za-z]")){
			JOptionPane.showMessageDialog(null,"Los valores del DNI no son correctos", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (nombre.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe introducir el nombre del trabajador. CAMPO OBLIGATORIO", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (apellidos.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Debe introducir los apellidos del trabajador. CAMPO OBLIGATORIO", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (direccion.equals("")) {
			JOptionPane.showMessageDialog(null, "Debe introducir la direcci�n del trabajador", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (telefono.trim().isEmpty()){
			JOptionPane.showMessageDialog(null, "Debe introducrir el telefono del trabajador. CAMPO OBLIGATORIO", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (telefono.length() != 9) {
			JOptionPane.showMessageDialog(null, "El tel�fono debe tener longitud 9", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (puesto.equals("") || puesto.equals("Elija Puesto")) {
			JOptionPane.showMessageDialog(null, "Debe introducir el puesto del trabajador. CAMPO OBLIGATORIO", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

}
