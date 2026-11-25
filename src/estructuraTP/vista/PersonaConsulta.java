package estructuraTP.vista;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import estructuraTP.dao.PersonaDAO;
import estructuraTP.modelo.Persona;

public class PersonaConsulta extends JPanel {

	private static final long serialVersionUID = 1L;

	// Formato en que se muestran y se ingresan las fechas de nacimiento.
	// STRICT hace que una fecha imposible (por ejemplo 31/02/2000) sea invlida.
	static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
			.withResolverStyle(ResolverStyle.STRICT);

	private JTable table;
	private DefaultTableModel modeloTabla;
	private JTextField txtBuscar;

	// Todas las personas y, en la misma posición, el nombre de su barrio.
	private ArrayList<Persona> personas;
	private ArrayList<String> barriosDePersonas = new ArrayList<String>();

	/**
	 * Create the panel.
	 */
	public PersonaConsulta() {

		setLayout(null);

		JLabel lblBuscar = new JLabel("Nombre completo");
		lblBuscar.setBounds(10, 18, 110, 14);
		add(lblBuscar);

		txtBuscar = new JTextField();
		txtBuscar.setBounds(125, 15, 170, 20);
		add(txtBuscar);
		// Búsqueda predictiva: la tabla se filtra a medida que se escribe.
		txtBuscar.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				filtrarTabla();
			}
		});

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filtrarTabla();
			}
		});
		btnBuscar.setBounds(305, 14, 100, 23);
		add(btnBuscar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 50, 410, 240);
		add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		modeloTabla = new DefaultTableModel(
				new Object[] { "DNI", "Nombre", "Situación laboral", "Fecha de nacimiento", "Barrio" }, 0);
		table.setModel(modeloTabla);
		// Las celdas son de solo lectura (no se pueden editar con doble clic).
		table.setDefaultEditor(Object.class, null);

		cargarTabla();

		JButton btnNuevo = new JButton("Agregar");
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				// Le paso null en el caso que sea una alta. Caso contrario la persona y su barrio.
				marco.setContentPane(new PersonaAltaModificacion(null, null));
				marco.validate();
			}
		});
		btnNuevo.setBounds(430, 50, 110, 23);
		add(btnNuevo);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Seleccione una persona de la tabla.");
					return;
				}
				PersonaDAO pDao = new PersonaDAO();
				int dni = (int) table.getValueAt(table.getSelectedRow(), 0);
				if (!pDao.eliminar(dni)) {
					JOptionPane.showMessageDialog(null, "No se pudo eliminar la persona.");
				}
				cargarTabla();
			}
		});
		btnEliminar.setBounds(430, 84, 110, 23);
		add(btnEliminar);

		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Seleccione una persona de la tabla.");
					return;
				}
				PersonaDAO pDao = new PersonaDAO();
				int dni = (int) table.getValueAt(table.getSelectedRow(), 0);
				Persona personaAModificar = pDao.buscarPorDni(dni);
				String nombreBarrio = pDao.buscarNombreBarrio(dni);

				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				marco.setContentPane(new PersonaAltaModificacion(personaAModificar, nombreBarrio));
				marco.validate();
			}
		});
		btnModificar.setBounds(430, 118, 110, 23);
		add(btnModificar);

	}

	// Lee todas las personas de la base y vuelve a aplicar el filtro de búsqueda.
	private void cargarTabla() {
		PersonaDAO pDao = new PersonaDAO();
		personas = pDao.traerTodas();
		barriosDePersonas.clear();
		for (Persona p : personas) {
			barriosDePersonas.add(pDao.buscarNombreBarrio(p.getDni()));
		}
		filtrarTabla();
	}

	// Muestra solo las personas cuyo nombre coincide por aproximación con el texto.
	private void filtrarTabla() {
		// Vacio la tabla
		modeloTabla.setRowCount(0);
		for (int i = 0; i < personas.size(); i++) {
			Persona p = personas.get(i);
			if (p.coincideConNombre(txtBuscar.getText())) {
				modeloTabla.addRow(new Object[] { p.getDni(), p.getNombre(), p.getSituacion(),
						p.getNacimiento().format(FORMATO_FECHA), barriosDePersonas.get(i) });
			}
		}
	}

}
