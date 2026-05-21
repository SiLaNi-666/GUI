package dialogs;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import modelo.*;
import Dao.AccesoTrabajador;
import Excepciones.BDException;

public class ModificarDialog extends JDialog implements ActionListener, ItemListener {
    JComboBox<Integer> comboIds; // El combo de arriba de la foto
    JTextField areaDni, areaNombre, areaApellidos, areaDireccion, areaTelefono;
    JComboBox<String> comboPuesto;
    JButton aceptar, cancelar;
    JLabel labelError;

    Empresa empresa;
    ArrayList<Trabajador> listaTrabajadores; // Para guardar los datos locales provisionales

    public ModificarDialog(JFrame padre, Empresa empresa) {
        super(padre, true);
        this.empresa = empresa;

        setTitle("Modifica Trabajador");
        setSize(480, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(padre);

        // --- PANEL SUPERIOR: SELECCIÓN DE ID ---
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));

        JTextArea textoExplicativo = new JTextArea("Introduzca el ID del trabajador\ny los datos que desea modificar.");
        textoExplicativo.setEditable(false);
        textoExplicativo.setBackground(UIManager.getColor("Label.background"));
        textoExplicativo.setFont(new Font("Arial", Font.PLAIN, 12));
        panelSuperior.add(textoExplicativo);

        panelSuperior.add(new JLabel("   Identificador "));
        comboIds = new JComboBox<>();
        comboIds.addItemListener(this); // Escucha cuando el usuario cambia de ID
        panelSuperior.add(comboIds);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL: FORMULARIO ---
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        panelFormulario.add(new JLabel("DNI"));
        areaDni = new JTextField();
        panelFormulario.add(areaDni);

        panelFormulario.add(new JLabel("Nombre"));
        areaNombre = new JTextField();
        panelFormulario.add(areaNombre);

        panelFormulario.add(new JLabel("Apellidos"));
        areaApellidos = new JTextField();
        panelFormulario.add(areaApellidos);

        panelFormulario.add(new JLabel("Dirección"));
        areaDireccion = new JTextField();
        panelFormulario.add(areaDireccion);

        panelFormulario.add(new JLabel("Teléfono"));
        areaTelefono = new JTextField();
        panelFormulario.add(areaTelefono);

        panelFormulario.add(new JLabel("Puesto"));
        String[] puestos = {"Direccion", "Administracion", "Ventas", "Produccion", "Arquitecto"};
        comboPuesto = new JComboBox<>(puestos);
        panelFormulario.add(comboPuesto);

        add(panelFormulario, BorderLayout.CENTER);

        // --- PANEL INFERIOR: BOTONES Y ERROR ---
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        aceptar = new JButton("Guardar"); // Tu botón de confirmación
        aceptar.addActionListener(this);
        panelBotones.add(aceptar);

        cancelar = new JButton("Cancelar");
        cancelar.addActionListener(this);
        panelBotones.add(cancelar);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        labelError = new JLabel(" ", SwingConstants.CENTER);
        labelError.setForeground(Color.BLACK); // Texto negro de aviso largo como la foto
        labelError.setFont(new Font("Arial", Font.PLAIN, 11));
        panelInferior.add(labelError, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        // Cargar los IDs disponibles de la Base de Datos al abrir
        cargarTrabajadoresDesdeBD();

        setVisible(true);
    }

    private void cargarTrabajadoresDesdeBD() {
        try {
            // Obtenemos todos los trabajadores reales de la BBDD
            listaTrabajadores = (ArrayList<Trabajador>) AccesoTrabajador.consultarTrabajadores();

            for (Trabajador t : listaTrabajadores) {
                comboIds.addItem(t.getIdentificador()); // Llenamos el JComboBox con los IDs
            }

            // Forzamos a que cargue los datos del primero de la lista
            actualizarCamposTexto();

        } catch (BDException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar identificadores: " + e.getMessage());
        }
    }

    // Este método lee el ID seleccionado y rellena los cuadros blancos automáticamente
    private void actualizarCamposTexto() {
        if (comboIds.getSelectedItem() == null) return;

        int idSeleccionado = (int) comboIds.getSelectedItem();

        for (Trabajador t : listaTrabajadores) {
            if (t.getIdentificador() == idSeleccionado) {
                areaDni.setText(t.getDni());
                areaNombre.setText(t.getNombre());
                areaApellidos.setText(t.getApellidos());
                areaDireccion.setText(t.getDireccion());
                areaTelefono.setText(t.getTelefono());
                comboPuesto.setSelectedItem(t.getPuesto());
                break;
            }
        }
    }

    // Se activa cuando cambias el ID en el desplegable de arriba
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            restaurarColoresCampos();
            labelError.setText(" ");
            actualizarCamposTexto(); // Cambia los datos del formulario al cambiar el ID
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == aceptar) {
            restaurarColoresCampos();
            labelError.setText(" ");

            String dni = areaDni.getText().trim();
            String nombre = areaNombre.getText().trim();
            String apellidos = areaApellidos.getText().trim();
            String direccion = areaDireccion.getText().trim();
            String telefono = areaTelefono.getText().trim();
            String puesto = comboPuesto.getSelectedItem().toString();

            // VALIDACIONES EXIGIDAS EN LA FOTO
            if (dni.isEmpty() || nombre.isEmpty() || apellidos.isEmpty()) {
                labelError.setText("DNI, Nombre y Apellidos son obligatorios.");
                return;
            }

            // Validación exacta del teléfono de tu captura (9 números, sin letras)
            if (!telefono.matches("\\d{9}")) {
                areaTelefono.setBackground(Color.RED); // ¡FONDO ROJO SÓLIDO COMO TU FOTO!
                areaTelefono.setForeground(Color.WHITE); // Texto blanco para que resalte en el rojo
                labelError.setText("<html><center>El teléfono debe constar de 9 caracteres numéricos<br>y no debe contener letras.</center></html>");
                return;
            }

            try {
                int idSeleccionado = (int) comboIds.getSelectedItem();
                Trabajador tModificado = new Trabajador(idSeleccionado, dni, nombre, apellidos, direccion, telefono, puesto);

                if (AccesoTrabajador.actualizarTrabajador(tModificado)) {
                    JOptionPane.showMessageDialog(this, "Trabajador actualizado.");
                    dispose();
                }
            } catch (BDException ex) {
                JOptionPane.showMessageDialog(this, "Error en BD: " + ex.getMessage());
            }
        } else if (e.getSource() == cancelar) {
            dispose();
        }
    }

    private void restaurarColoresCampos() {
        areaTelefono.setBackground(Color.WHITE);
        areaTelefono.setForeground(Color.BLACK);
    }
}
