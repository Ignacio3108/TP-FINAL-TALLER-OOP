package estructuraTP.vista;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import estructuraTP.dao.ComunaDAO;
import estructuraTP.modelo.Comuna;
import java.awt.Color;

public class ComunaConsulta extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel modeloTabla;

	/**
	 * Create the panel.
	 */
	public ComunaConsulta() {
		setBackground(new Color(0, 128, 255));

		setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 32, 340, 250);
		add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		modeloTabla = new DefaultTableModel(new Object[] { "Número", "Sede", "Cantidad de barrios" }, 0);
		table.setModel(modeloTabla);
		// Las celdas son de solo lectura (no se pueden editar con doble clic).
		table.setDefaultEditor(Object.class, null);

		cargarTabla();

		JButton btnNuevo = new JButton("Agregar");
		btnNuevo.setBackground(new Color(255, 0, 0));
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				// Le paso null en el caso que sea una alta. Caso contrario el objeto que quiero modificar.
				marco.setContentPane(new ComunaAltaModificacion(null));
				marco.validate();
			}
		});
		btnNuevo.setBounds(400, 32, 110, 23);
		add(btnNuevo);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.setBackground(new Color(255, 255, 0));
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Seleccione una comuna de la tabla.");
					return;
				}
				ComunaDAO cDao = new ComunaDAO();
				int numero = (int) table.getValueAt(table.getSelectedRow(), 0);
				if (!cDao.eliminar(numero)) {
					JOptionPane.showMessageDialog(null,
							"No se pudo eliminar la comuna. Puede tener barrios asociados.");
				}
				cargarTabla();
			}
		});
		btnEliminar.setBounds(400, 66, 110, 23);
		add(btnEliminar);

		JButton btnModificar = new JButton("Modificar");
		btnModificar.setBackground(new Color(128, 255, 0));
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Seleccione una comuna de la tabla.");
					return;
				}
				ComunaDAO cDao = new ComunaDAO();
				int numero = (int) table.getValueAt(table.getSelectedRow(), 0);
				Comuna comunaAModificar = cDao.buscarPorNumero(numero);
				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				marco.setContentPane(new ComunaAltaModificacion(comunaAModificar));
				marco.validate();
			}
		});
		btnModificar.setBounds(400, 100, 110, 23);
		add(btnModificar);

	}

	private void cargarTabla() {
		// Vacio la tablia
		modeloTabla.setRowCount(0);
		ComunaDAO cDao = new ComunaDAO();
		ArrayList<Comuna> comunas = cDao.traerTodas();
		for (Comuna c : comunas) {
			modeloTabla.addRow(new Object[] { c.getNumero(), c.getSede(), c.cantidadBarrios() });
		}
	}

}
