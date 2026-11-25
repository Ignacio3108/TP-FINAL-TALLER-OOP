package estructuraTP.vista;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import estructuraTP.dao.ComunaDAO;
import estructuraTP.modelo.Comuna;
import java.awt.Color;

public class ComunaAltaModificacion extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtNumero;
	private JTextField txtSede;
	private JButton btnGuardarModificar;

	/**
	 * Create the panel.
	 * 
	 * @param comunaAModificar null si es un alta
	 */
	public ComunaAltaModificacion(Comuna comunaAModificar) {
		setBackground(new Color(255, 0, 255));
		setLayout(null);

		JLabel lblNumero = new JLabel("Numero");
		lblNumero.setBounds(39, 48, 60, 14);
		add(lblNumero);

		txtNumero = new JTextField();
		txtNumero.setBounds(110, 45, 150, 20);
		add(txtNumero);
		txtNumero.setColumns(10);

		JLabel lblSede = new JLabel("Sede");
		lblSede.setBounds(39, 73, 60, 14);
		add(lblSede);

		txtSede = new JTextField();
		txtSede.setBounds(110, 70, 150, 20);
		add(txtSede);
		txtSede.setColumns(10);

		if (comunaAModificar != null) {
			// Completo los campos de textos.
			txtNumero.setText("" + comunaAModificar.getNumero());
			txtSede.setText(comunaAModificar.getSede());
		}

		JButton btnVolver = new JButton("Volver");
		btnVolver.setBackground(new Color(255, 0, 128));
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				marco.setContentPane(new ComunaConsulta());
				marco.validate();
			}
		});
		btnVolver.setBounds(171, 130, 89, 23);
		add(btnVolver);

		btnGuardarModificar = new JButton();
		btnGuardarModificar.setBackground(new Color(255, 0, 128));
		btnGuardarModificar.setText(comunaAModificar == null ? "Guardar" : "Modificar");
		btnGuardarModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int numero;
				try {
					numero = Integer.parseInt(txtNumero.getText().trim());
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "El número de comuna debe ser un nro entero.");
					return;
				}
				if (txtSede.getText().trim().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Ingrese la sede de la comuna.");
					return;
				}
				Comuna comuna = new Comuna(numero, txtSede.getText().trim());

				ComunaDAO cDao = new ComunaDAO();
				boolean guardada;
				if (comunaAModificar == null) {
					// Es un alta.
					guardada = cDao.guardar(comuna);
				} else {
					// Es una modificación.
					guardada = cDao.actualizar(comuna, comunaAModificar.getNumero());
				}
				if (!guardada) {
					JOptionPane.showMessageDialog(null,
							"No se pudo guardar. Verifique que el nmero de comuna no exista.");
					return;
				}
				// Vuelvo al panel de consulta
				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				marco.setContentPane(new ComunaConsulta());
				marco.validate();

			}
		});
		btnGuardarModificar.setBounds(60, 130, 100, 23);
		add(btnGuardarModificar);

	}

}
