
package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import dialogs.AltaDialog;
import dialogs.BajaDialog;
import dialogs.ListarDialog;
import dialogs.ModificarDialog;
import ficheros.FicheroDatos;
import modelo.Empresa;
import modelo.Trabajador;

/**
 *
 * @author usuario
 *
 */
public class EmpresaGUI extends JFrame implements ActionListener {

	Empresa empresa;

	JButton altaTrabajador;
	JButton bajaTrabajador;
	JButton modificaTrabajador;
	JButton buscaTrabajador;
	JButton listarTrabajadores;
	JButton salir;

	public EmpresaGUI() {
		super("Gestión de personal");

		// Carga los trabajadores leidos de un fichero a memoria
		ArrayList<Trabajador> trabaj = FicheroDatos.obtenerTrabajadores("ficheroDatos\\empresa.dat");
		empresa = new Empresa(trabaj);
		// Justo después de crear la empresa, inyectamos los datos del fichero en MySQL
		Dao.AccesoTrabajador.volcarFicheroABBDD(trabaj);

		// Tamaño JFrame
		setSize(800, 750);
		// Cerrar al salir
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(3, 2));
		setLocationRelativeTo(null);
		// Creación de los botones y se añaden al JFrame
		// Se añade una imagen para cada botón
		altaTrabajador = new JButton("Añadir Trabajador");
		altaTrabajador.addActionListener(this);
		altaTrabajador.setIcon(new ImageIcon("images/addUser.png"));
		add(altaTrabajador);

		bajaTrabajador = new JButton("Borrar Trabajador");
		bajaTrabajador.addActionListener(this);
		bajaTrabajador.setIcon(new ImageIcon("images/removeUser.png"));
		add(bajaTrabajador);

		modificaTrabajador = new JButton("Modificar Trabajador");
		modificaTrabajador.addActionListener(this);
		modificaTrabajador.setIcon(new ImageIcon("images/editUser.png"));
		add(modificaTrabajador);

		buscaTrabajador = new JButton("Buscar Trabajador");
		buscaTrabajador.addActionListener(this);
		buscaTrabajador.setIcon(new ImageIcon("images/searchUser.png"));
		add(buscaTrabajador);

		listarTrabajadores = new JButton("Listar Trabajadores");
		listarTrabajadores.addActionListener(this);
		listarTrabajadores.setIcon(new ImageIcon("images/list.png"));
		add(listarTrabajadores);

		salir = new JButton("Salir");
		salir.addActionListener(this);
		salir.setIcon(new ImageIcon("images/exit.png"));
		add(salir);
		// Visible
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Para responder a los clicks del usuario en cada botón (ActionEvent)
		// nuestra clase hace de oyente de eventos por eso implementa ActionListener
		// e implementa el método actionPerformed() pasando como parámetro un
		// ActionEvent.
		if (e.getSource() == altaTrabajador) {
			new AltaDialog(empresa);
		} else if (e.getSource() == bajaTrabajador) {
			new BajaDialog(empresa);
		} else if (e.getSource() == modificaTrabajador) {
			new ModificarDialog(this, empresa);
		} else if (e.getSource() == buscaTrabajador) {
			new ListarDialog(empresa);
		} else if (e.getSource() == listarTrabajadores) {
			new ListarDialog(empresa);
		}
		// Cuando se sale se vuelca a fichero.
		else if (e.getSource() == salir) {
			FicheroDatos.escribirTrabajadores("ficheroDatos\\empresa.dat", empresa.getTrabajadores());
			System.exit(0);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new EmpresaGUI();
	}

}
