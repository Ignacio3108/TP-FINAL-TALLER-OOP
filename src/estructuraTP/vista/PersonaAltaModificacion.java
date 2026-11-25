package estructuraTP.vista;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import estructuraTP.dao.ComplejoHabitacionalDAO;
import estructuraTP.dao.PersonaDAO;
import estructuraTP.dao.TrazadoFormalDAO;
import estructuraTP.dao.UrbanizacionPlanificadaDAO;
import estructuraTP.modelo.Barrio;
import estructuraTP.modelo.Persona;

public class PersonaAltaModificacion extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtDni;
	private JTextField txtNombre;
	private JTextField txtNacimiento;
	private JComboBox<String> cmbSituacion;
	private JComboBox<String> cmbBarrio;

	// Los barrios del combo, en el mismo orden en que aparecen.
	private ArrayList<Barrio> barrios = new ArrayList<Barrio>();

	/**
	 * Create the panel.
	 * 
	 * @param personaAModificar  null si es un alta
	 * @param nombreBarrioActual nombre del barrio de la persona a modificar (null si es un alta)
	 */
	public PersonaAltaModificacion(Persona personaAModificar, String nombreBarrioActual) {
		setLayout(null);

		JLabel lblDni = new JLabel("DNI");
		lblDni.setBounds(39, 28, 150, 14);
		add(lblDni);

		txtDni = new JTextField();
		txtDni.setBounds(200, 25, 180, 20);
		add(txtDni);

		JLabel lblNombre = new JLabel("Nombre completo");
		lblNombre.setBounds(39, 58, 150, 14);
		add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(200, 55, 180, 20);
		add(txtNombre);

		JLabel lblNacimiento = new JLabel("Fecha de nac. (dd/mm/aaaa)");
		lblNacimiento.setBounds(39, 88, 160, 14);
		add(lblNacimiento);

		txtNacimiento = new JTextField();
		txtNacimiento.setBounds(200, 85, 180, 20);
		add(txtNacimiento);

		JLabel lblSituacion = new JLabel("Situación laboral");
		lblSituacion.setBounds(39, 118, 150, 14);
		add(lblSituacion);

		cmbSituacion = new JComboBox<String>();
		cmbSituacion.setBounds(200, 115, 180, 22);
		add(cmbSituacion);
		for (String situacion : new PersonaDAO().traerSituaciones()) {
			cmbSituacion.addItem(situacion);
		}

		JLabel lblBarrio = new JLabel("Barrio");
		lblBarrio.setBounds(39, 148, 150, 14);
		add(lblBarrio);

		cmbBarrio = new JComboBox<String>();
		cmbBarrio.setBounds(200, 145, 180, 22);
		add(cmbBarrio);
		// Los barrios existentes, de los tres tipos.
		barrios.addAll(new TrazadoFormalDAO().traerTodos());
		barrios.addAll(new UrbanizacionPlanificadaDAO().traerTodos());
		barrios.addAll(new ComplejoHabitacionalDAO().traerTodos());
		for (Barrio b : barrios) {
			cmbBarrio.addItem(b.getNombre());
		}

		if (personaAModificar != null) {
			// Completo los campos con los datos de la persona.
			txtDni.setText("" + personaAModificar.getDni());
			txtNombre.setText(personaAModificar.getNombre());
			txtNacimiento.setText(personaAModificar.getNacimiento().format(PersonaConsulta.FORMATO_FECHA));
			cmbSituacion.setSelectedItem(personaAModificar.getSituacion());
			if (nombreBarrioActual != null) {
				cmbBarrio.setSelectedItem(nombreBarrioActual);
			}
		}

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				marco.setContentPane(new PersonaConsulta());
				marco.validate();
			}
		});
		btnVolver.setBounds(291, 200, 89, 23);
		add(btnVolver);

		JButton btnGuardarModificar = new JButton();
		btnGuardarModificar.setText(personaAModificar == null ? "Guardar" : "Modificar");
		btnGuardarModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dni;
				try {
					dni = Integer.parseInt(txtDni.getText().trim());
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "El DNI debe ser un nri entero, sin puntos.");
					return;
				}
				if (dni <= 0) {
					JOptionPane.showMessageDialog(null, "El DNI debe ser mayor a 0.");
					return;
				}
				String nombre = txtNombre.getText().trim();
				if (nombre.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Ingrese el nombre completo.");
					return;
				}
				LocalDate nacimiento;
				try {
					nacimiento = LocalDate.parse(txtNacimiento.getText().trim(), PersonaConsulta.FORMATO_FECHA);
				} catch (DateTimeParseException ex) {
					JOptionPane.showMessageDialog(null, "La fecha de nacimiento debe tener el formato dd/mm/aaaa.");
					return;
				}
				if (nacimiento.isAfter(LocalDate.now())) {
					JOptionPane.showMessageDialog(null, "La fecha de nacimiento no puede ser futura.");
					return;
				}
				if (cmbBarrio.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null, "No hay barrios cargados para asociar a la persona che.");
					return;
				}
				if (cmbSituacion.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null, "No hay sit laborales cargadas.");
					return;
				}
				Barrio barrio = barrios.get(cmbBarrio.getSelectedIndex());
				Persona persona = new Persona(dni, nombre, nacimiento, (String) cmbSituacion.getSelectedItem());

				PersonaDAO pDao = new PersonaDAO();
				boolean guardada;
				if (personaAModificar == null) {
					// Es un alta.
					guardada = pDao.guardar(persona, barrio);
				} else {
					// Es una modificación.
					guardada = pDao.actualizar(persona, barrio, personaAModificar.getDni());
				}
				if (!guardada) {
					JOptionPane.showMessageDialog(null, "No se pudo guardar. Verifica que el DNI no exista.");
					return;
				}
				// Vuelvo al panel de consulta
				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				marco.setContentPane(new PersonaConsulta());
				marco.validate();
			}
		});
		btnGuardarModificar.setBounds(180, 200, 100, 23);
		add(btnGuardarModificar);

	}

}
