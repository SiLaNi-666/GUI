/**
 * 
 */
package dialogs;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import modelo.Empresa;

/**
 * 
 * @author usuario
 *
 */
public class BajaDialog extends JDialog implements ActionListener {

	JButton aceptar;
	JButton cancelar;
	JLabel identificador;
	JTextField areaIdentificador;
	JPanel panel;
	JPanel panelBotones;
	JLabel texto;

	Empresa empresa;

	public BajaDialog(Empresa empresa) {
		this.empresa = empresa;

		setResizable(false);
		// t�tulo del di�log
		setTitle("Baja Trabajador");
		setSize(300, 200);
		setLayout(new FlowLayout());
		setLocationRelativeTo(null);

		texto = new JLabel("<html>Introduzca el ID del trabajador<br> que desea dar de baja<br><br></html>");
		add(texto);

		panel = new JPanel();
		panelBotones = new JPanel();
		add(panel);
		add(panelBotones);

		identificador = new JLabel("Identificador");
		panel.add(identificador);
		areaIdentificador = new JTextField(15);
		panel.add(areaIdentificador);

		aceptar = new JButton("Aceptar");
		aceptar.addActionListener(this);
		panelBotones.add(aceptar);

		cancelar = new JButton("Cancelar");
		cancelar.addActionListener(this);
		panelBotones.add(cancelar);
		// Visible
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if (e.getSource() == aceptar) {
			int respuesta = JOptionPane.showConfirmDialog(null, "�Desea dar de baja el trabajador?", "Borrar",
					JOptionPane.YES_NO_OPTION);
			switch (respuesta) {
			case JOptionPane.YES_OPTION:
				try {
					// Operaciones en caso afirmativo
					if (empresa.bajaTrabajador(Integer.parseInt(areaIdentificador.getText()))) {
						JOptionPane.showMessageDialog(this, "El trabajador se ha eliminado correctamente");
					} else {
						JOptionPane.showMessageDialog(null, "El trabajador no se encuentra en la lista", "Error",
								JOptionPane.ERROR_MESSAGE);
					}

					break;
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "El ID debe ser un n�mero entero", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			case JOptionPane.NO_OPTION:
				// Operaciones en caso negativo
				break;
			}
		} else if (e.getSource() == cancelar) {
			dispose();
		}

	}

}
