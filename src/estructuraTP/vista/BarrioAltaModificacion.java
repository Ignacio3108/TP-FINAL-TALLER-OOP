package estructuraTP.vista;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import estructuraTP.dao.ComplejoHabitacionalDAO;
import estructuraTP.dao.ComunaDAO;
import estructuraTP.dao.TrazadoFormalDAO;
import estructuraTP.dao.UrbanizacionPlanificadaDAO;
import estructuraTP.modelo.Barrio;
import estructuraTP.modelo.Complejo_habitacional;
import estructuraTP.modelo.Comuna;
import estructuraTP.modelo.Trazado_formal;
import estructuraTP.modelo.Urbanizacion_planificada;

public class BarrioAltaModificacion extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField txtNombre;
	private JTextField txtSuperficie;
	private JComboBox<String> cmbComuna;
	private JComboBox<String> cmbTipo;

	// Campos de Trazado formal
	private JLabel lblCantidadEdif;
	private JTextField txtCantidadEdif;
	private JLabel lblPercAsfalto;
	private JTextField txtPercAsfalto;

	// Campos de Urbanización planificada
	private JLabel lblNroLey;
	private JTextField txtNroLey;
	private JCheckBox chkConexionAgua;

	// Campos de Complejo habitacional
	private JLabel lblCantidadMonob;
	private JTextField txtCantidadMonob;
	private JCheckBox chkConsorcio;

	// Las comunas del combo, en el mismo orden en que aparecen.
	private ArrayList<Comuna> comunas;

	/**
	 * Create the panel.
	 * 
	 * @param barrioAModificar null si es un alta
	 * @param comunaActual     comuna del barrio a modificar (null si es un alta)
	 */
	public BarrioAltaModificacion(Barrio barrioAModificar, Comuna comunaActual) {
		setLayout(null);

		// ---- Campos comunes a todos los barrios
		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(39, 28, 150, 14);
		add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(200, 25, 180, 20);
		add(txtNombre);

		JLabel lblSuperficie = new JLabel("Superficie (hectáreas)");
		lblSuperficie.setBounds(39, 58, 150, 14);
		add(lblSuperficie);

		txtSuperficie = new JTextField();
		txtSuperficie.setBounds(200, 55, 180, 20);
		add(txtSuperficie);

		JLabel lblComuna = new JLabel("Comuna");
		lblComuna.setBounds(39, 88, 150, 14);
		add(lblComuna);

		cmbComuna = new JComboBox<String>();
		cmbComuna.setBounds(200, 85, 180, 22);
		add(cmbComuna);
		comunas = new ComunaDAO().traerTodas();
		for (Comuna c : comunas) {
			cmbComuna.addItem(c.getNumero() + " - " + c.getSede());
		}

		JLabel lblTipo = new JLabel("Tipo de barrio");
		lblTipo.setBounds(39, 118, 150, 14);
		add(lblTipo);

		cmbTipo = new JComboBox<String>();
		cmbTipo.addItem(TrazadoFormalDAO.TIPO);
		cmbTipo.addItem(UrbanizacionPlanificadaDAO.TIPO);
		cmbTipo.addItem(ComplejoHabitacionalDAO.TIPO);
		cmbTipo.setBounds(200, 115, 180, 22);
		cmbTipo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarCamposDelTipo();
			}
		});
		add(cmbTipo);

		// ---- Campos específicos. Los tres tipos comparten las mismas dos filas:
		// solo se ven los del tipo elegido.
		lblCantidadEdif = new JLabel("Edificios de prop. horizontal");
		lblCantidadEdif.setBounds(39, 160, 160, 14);
		add(lblCantidadEdif);
		txtCantidadEdif = new JTextField();
		txtCantidadEdif.setBounds(200, 157, 180, 20);
		add(txtCantidadEdif);

		lblPercAsfalto = new JLabel("% de asfalto en calles");
		lblPercAsfalto.setBounds(39, 190, 160, 14);
		add(lblPercAsfalto);
		txtPercAsfalto = new JTextField();
		txtPercAsfalto.setBounds(200, 187, 180, 20);
		add(txtPercAsfalto);

		lblNroLey = new JLabel("N° de ley de integración");
		lblNroLey.setBounds(39, 160, 160, 14);
		add(lblNroLey);
		txtNroLey = new JTextField();
		txtNroLey.setBounds(200, 157, 180, 20);
		add(txtNroLey);
		chkConexionAgua = new JCheckBox("Conexión formal a red de agua potable");
		chkConexionAgua.setBounds(39, 186, 300, 23);
		add(chkConexionAgua);

		lblCantidadMonob = new JLabel("Cantidad de monoblocks");
		lblCantidadMonob.setBounds(39, 160, 160, 14);
		add(lblCantidadMonob);
		txtCantidadMonob = new JTextField();
		txtCantidadMonob.setBounds(200, 157, 180, 20);
		add(txtCantidadMonob);
		chkConsorcio = new JCheckBox("Consorcio de administración activo");
		chkConsorcio.setBounds(39, 186, 300, 23);
		add(chkConsorcio);

		if (barrioAModificar != null) {
			// Completo los campos con los datos del barrio.
			txtNombre.setText(barrioAModificar.getNombre());
			txtSuperficie.setText("" + barrioAModificar.getSuperficie());

			if (comunaActual != null) {
				cmbComuna.setSelectedItem(comunaActual.getNumero() + " - " + comunaActual.getSede());
			}

			if (barrioAModificar instanceof Trazado_formal) {
				Trazado_formal t = (Trazado_formal) barrioAModificar;
				cmbTipo.setSelectedItem(TrazadoFormalDAO.TIPO);
				txtCantidadEdif.setText("" + t.getCantidadEdif());
				txtPercAsfalto.setText("" + t.getPercAsfalto());
			} else if (barrioAModificar instanceof Urbanizacion_planificada) {
				Urbanizacion_planificada u = (Urbanizacion_planificada) barrioAModificar;
				cmbTipo.setSelectedItem(UrbanizacionPlanificadaDAO.TIPO);
				txtNroLey.setText(u.getNroLey());
				chkConexionAgua.setSelected(u.getConexionAgua());
			} else {
				Complejo_habitacional ch = (Complejo_habitacional) barrioAModificar;
				cmbTipo.setSelectedItem(ComplejoHabitacionalDAO.TIPO);
				txtCantidadMonob.setText("" + ch.getCantidadMonob());
				chkConsorcio.setSelected(ch.getConsorcio());
			}
		}
		mostrarCamposDelTipo();

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				marco.setContentPane(new BarrioConsulta());
				marco.validate();
			}
		});
		btnVolver.setBounds(291, 240, 89, 23);
		add(btnVolver);

		JButton btnGuardarModificar = new JButton();
		btnGuardarModificar.setText(barrioAModificar == null ? "Guardar" : "Modificar");
		btnGuardarModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cmbComuna.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null, "No hay comunas cargadas para asociar al barrio.");
					return;
				}
				Comuna comuna = comunas.get(cmbComuna.getSelectedIndex());

				Barrio barrio = armarBarrio();
				if (barrio == null) {
					// Los datos ingresados no son válidos; armarBarrio ya avisó.
					return;
				}

				boolean guardado;
				if (barrioAModificar == null) {
					// Es un alta.
					guardado = guardar(barrio, comuna);
				} else {
					// Es una modificación.
					guardado = actualizar(barrio, comuna, barrioAModificar.getNombre());
				}
				if (!guardado) {
					JOptionPane.showMessageDialog(null, "No se pudo guardar. Verifique que el nombre del barrio no exista.");
					return;
				}
				// Vuelvo al panel de consulta
				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				marco.setContentPane(new BarrioConsulta());
				marco.validate();
			}
		});
		btnGuardarModificar.setBounds(180, 240, 100, 23);
		add(btnGuardarModificar);

	}

	// Muestra solo los campos específicos del tipo elegido en el combo.
	private void mostrarCamposDelTipo() {
		String tipo = (String) cmbTipo.getSelectedItem();
		boolean esTrazadoFormal = tipo.equals(TrazadoFormalDAO.TIPO);
		boolean esUrbanizacion = tipo.equals(UrbanizacionPlanificadaDAO.TIPO);
		boolean esComplejo = tipo.equals(ComplejoHabitacionalDAO.TIPO);

		lblCantidadEdif.setVisible(esTrazadoFormal);
		txtCantidadEdif.setVisible(esTrazadoFormal);
		lblPercAsfalto.setVisible(esTrazadoFormal);
		txtPercAsfalto.setVisible(esTrazadoFormal);

		lblNroLey.setVisible(esUrbanizacion);
		txtNroLey.setVisible(esUrbanizacion);
		chkConexionAgua.setVisible(esUrbanizacion);

		lblCantidadMonob.setVisible(esComplejo);
		txtCantidadMonob.setVisible(esComplejo);
		chkConsorcio.setVisible(esComplejo);
	}

	// Valida los campos y crea el barrio del tipo elegido. Devuelve null si hay
	// algún dato inválido.
	private Barrio armarBarrio() {
		String nombre = txtNombre.getText().trim();
		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Ingrese el nombre del barrio.");
			return null;
		}

		int superficie;
		try {
			superficie = Integer.parseInt(txtSuperficie.getText().trim());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "La superficie debe ser un número entero de hectáreas.");
			return null;
		}
		if (superficie <= 0) {
			JOptionPane.showMessageDialog(null, "La superficie debe ser mayor a cero.");
			return null;
		}

		String tipo = (String) cmbTipo.getSelectedItem();

		if (tipo.equals(TrazadoFormalDAO.TIPO)) {
			int cantidadEdif;
			float percAsfalto;
			try {
				cantidadEdif = Integer.parseInt(txtCantidadEdif.getText().trim());
				percAsfalto = Float.parseFloat(txtPercAsfalto.getText().trim().replace(',', '.'));
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null,
						"La cantidad de edificios debe ser un número entero y el porcentaje de asfalto un número.");
				return null;
			}
			if (cantidadEdif < 0 || percAsfalto < 0 || percAsfalto > 100) {
				JOptionPane.showMessageDialog(null,
						"La cantidad de edificios no puede ser negativa y el asfalto debe estar entre 0 y 100.");
				return null;
			}
			return new Trazado_formal(nombre, superficie, cantidadEdif, percAsfalto);

		} else if (tipo.equals(UrbanizacionPlanificadaDAO.TIPO)) {
			String nroLey = txtNroLey.getText().trim();
			if (nroLey.isEmpty()) {
				JOptionPane.showMessageDialog(null, "Ingrese el número de ley de integración.");
				return null;
			}
			return new Urbanizacion_planificada(nombre, superficie, nroLey, chkConexionAgua.isSelected());

		} else {
			int cantidadMonob;
			try {
				cantidadMonob = Integer.parseInt(txtCantidadMonob.getText().trim());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "La cantidad de monoblocks debe ser un número entero.");
				return null;
			}
			if (cantidadMonob < 0) {
				JOptionPane.showMessageDialog(null, "La cantidad de monoblocks no puede ser negativa.");
				return null;
			}
			return new Complejo_habitacional(nombre, superficie, cantidadMonob, chkConsorcio.isSelected());
		}
	}

	// Cada tipo de barrio tiene su DAO: se elige según la clase del objeto.
	private boolean guardar(Barrio barrio, Comuna comuna) {
		if (barrio instanceof Trazado_formal) {
			return new TrazadoFormalDAO().guardar((Trazado_formal) barrio, comuna);
		} else if (barrio instanceof Urbanizacion_planificada) {
			return new UrbanizacionPlanificadaDAO().guardar((Urbanizacion_planificada) barrio, comuna);
		}
		return new ComplejoHabitacionalDAO().guardar((Complejo_habitacional) barrio, comuna);
	}

	private boolean actualizar(Barrio barrio, Comuna comuna, String nombreActual) {
		if (barrio instanceof Trazado_formal) {
			return new TrazadoFormalDAO().actualizar((Trazado_formal) barrio, comuna, nombreActual);
		} else if (barrio instanceof Urbanizacion_planificada) {
			return new UrbanizacionPlanificadaDAO().actualizar((Urbanizacion_planificada) barrio, comuna, nombreActual);
		}
		return new ComplejoHabitacionalDAO().actualizar((Complejo_habitacional) barrio, comuna, nombreActual);
	}

}
