package dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import modelo.*;
import Dao.AccesoTrabajador;
import Excepciones.BDException;

public class ModificarDialog extends JDialog implements ActionListener {
    JTextField areaDni, areaNombre, areaApellidos, areaDireccion, areaTelefono;
    JComboBox<String> comboPuesto;
    JButton aceptar, cancelar;
    Empresa empresa;
    Trabajador trabajador;
    ListarDialog ventanaPadre;

    public ModificarDialog(Empresa empresa, Trabajador t, ListarDialog padre) {
        this.empresa = empresa;
        this.trabajador = t;
        this.ventanaPadre = padre;

        setTitle("Modificar Trabajador");
        setSize(400, 450);
        setLayout(new GridLayout(8, 2));
        setLocationRelativeTo(null);

        // Etiquetas y Campos
        add(new JLabel("DNI:"));
        areaDni = new JTextField(t.getDni()); // CARGA AUTOMÁTICA
        add(areaDni);

        add(new JLabel("Nombre:"));
        areaNombre = new JTextField(t.getNombre()); // CARGA AUTOMÁTICA
        add(areaNombre);

        add(new JLabel("Apellidos:"));
        areaApellidos = new JTextField(t.getApellidos());
        add(areaApellidos);

        add(new JLabel("Dirección:"));
        areaDireccion = new JTextField(t.getDireccion());
        add(areaDireccion);

        add(new JLabel("Teléfono:"));
        areaTelefono = new JTextField(t.getTelefono());
        add(areaTelefono);

        add(new JLabel("Puesto:"));
        String[] puestos = {"Direccion", "Administracion", "Ventas", "Produccion"};
        comboPuesto = new JComboBox<>(puestos);
        comboPuesto.setSelectedItem(t.getPuesto()); // CARGA AUTOMÁTICA
        add(comboPuesto);

        aceptar = new JButton("Guardar Cambios");
        aceptar.addActionListener(this);
        add(aceptar);

        cancelar = new JButton("Cancelar");
        cancelar.addActionListener(this);
        add(cancelar);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == aceptar) {
            try {
                // Actualizamos el objeto con los nuevos datos de los cuadros de texto
                trabajador.setDni(areaDni.getText());
                trabajador.setNombre(areaNombre.getText());
                trabajador.setApellidos(areaApellidos.getText());
                trabajador.setDireccion(areaDireccion.getText());
                trabajador.setTelefono(areaTelefono.getText());
                trabajador.setPuesto(comboPuesto.getSelectedItem().toString());

                // 1. Modificar en Base de Datos
                if (AccesoTrabajador.actualizarTrabajador(trabajador)) {
                    JOptionPane.showMessageDialog(this, "Actualizado en BBDD");

                    // 2. Refrescar la tabla del listado para ver el cambio (US3 avanzada)
                    ventanaPadre.rellenarTabla();
                    dispose();
                }
            } catch (BDException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        } else {
            dispose();
        }
    }
}