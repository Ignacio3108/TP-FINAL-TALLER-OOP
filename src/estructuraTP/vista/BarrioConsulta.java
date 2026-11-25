package estructuraTP.vista;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import estructuraTP.dao.ComplejoHabitacionalDAO;
import estructuraTP.dao.ComunaDAO;
import estructuraTP.dao.TrazadoFormalDAO;
import estructuraTP.dao.UrbanizacionPlanificadaDAO;
import estructuraTP.modelo.Barrio;
import estructuraTP.modelo.Complejo_habitacional;
import estructuraTP.modelo.Comuna;
import estructuraTP.modelo.Trazado_formal;
import estructuraTP.modelo.Urbanizacion_planificada;

public class BarrioConsulta extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel modeloTabla;

	/**
	 * Create the panel.
	 */
	public BarrioConsulta() {

		setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(31, 32, 380, 250);
		add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		modeloTabla = new DefaultTableModel(new Object[] { "Nombre", "Tipo", "Superficie (ha)" }, 0);
		table.setModel(modeloTabla);
		// Las celdas son de solo lectura (no se pueden editar con doble clic).
		table.setDefaultEditor(Object.class, null);

		cargarTabla();

		JButton btnNuevo = new JButton("Agregar");
		btnNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				// Le paso null en el caso que sea una alta. Caso contrario el barrio y su comuna.
				marco.setContentPane(new BarrioAltaModificacion(null, null));
				marco.validate();
			}
		});
		btnNuevo.setBounds(430, 32, 110, 23);
		add(btnNuevo);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Seleccione un barrio de la tabla.");
					return;
				}
				String nombre = (String) table.getValueAt(table.getSelectedRow(), 0);
				String tipo = (String) table.getValueAt(table.getSelectedRow(), 1);

				boolean eliminado;
				if (tipo.equals(TrazadoFormalDAO.TIPO)) {
					eliminado = new TrazadoFormalDAO().eliminar(nombre);
				} else if (tipo.equals(UrbanizacionPlanificadaDAO.TIPO)) {
					eliminado = new UrbanizacionPlanificadaDAO().eliminar(nombre);
				} else {
					eliminado = new ComplejoHabitacionalDAO().eliminar(nombre);
				}
				if (!eliminado) {
					JOptionPane.showMessageDialog(null,
							"No se pudo eliminar el barrio. Puede tener personas asociadas.");
				}
				cargarTabla();
			}
		});
		btnEliminar.setBounds(430, 66, 110, 23);
		add(btnEliminar);

		JButton btnModificar = new JButton("Modificar");
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Seleccione un barrio de la tabla.");
					return;
				}
				String nombre = (String) table.getValueAt(table.getSelectedRow(), 0);
				String tipo = (String) table.getValueAt(table.getSelectedRow(), 1);

				Barrio barrioAModificar;
				if (tipo.equals(TrazadoFormalDAO.TIPO)) {
					barrioAModificar = new TrazadoFormalDAO().buscarPorNombre(nombre);
				} else if (tipo.equals(UrbanizacionPlanificadaDAO.TIPO)) {
					barrioAModificar = new UrbanizacionPlanificadaDAO().buscarPorNombre(nombre);
				} else {
					barrioAModificar = new ComplejoHabitacionalDAO().buscarPorNombre(nombre);
				}
				Comuna comunaActual = new ComunaDAO().buscarPorBarrio(nombre);

				JFrame marco = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
				marco.setContentPane(new BarrioAltaModificacion(barrioAModificar, comunaActual));
				marco.validate();
			}
		});
		btnModificar.setBounds(430, 100, 110, 23);
		add(btnModificar);
		
		
		//TAREA DE EVALUACION
		JButton btnNewButton = new JButton("DETALLES DE TIPO DE BARRIO");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (table.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(null, "Seleccione un barrio de la tabla.");
					return;
				}
				String nombre = (String) table.getValueAt(table.getSelectedRow(), 0);
				String tipo = (String) table.getValueAt(table.getSelectedRow(), 1);
				
				if(tipo == "Trazado formal") {
					
					TrazadoFormalDAO tfDAO = new TrazadoFormalDAO();
					Trazado_formal TFselect = tfDAO.buscarPorNombre(nombre);
					JOptionPane.showMessageDialog(null, "CantidadEfificios: " + TFselect.getCantidadEdif());
					JOptionPane.showMessageDialog(null, "%Asfalto: " + TFselect.getPercAsfalto());
				}
				else if(tipo == "Urbanizacion planificada") {
					UrbanizacionPlanificadaDAO upDAO = new UrbanizacionPlanificadaDAO();
					Urbanizacion_planificada UPselect = upDAO.buscarPorNombre(nombre);
					JOptionPane.showMessageDialog(null, "Conexion de Agua: " + UPselect.getConexionAgua());
					JOptionPane.showMessageDialog(null, "Nro de ley: " + UPselect.getNroLey());
				}
				else if(tipo == "Complejo habitacional") {
					ComplejoHabitacionalDAO chDAO = new ComplejoHabitacionalDAO();
					Complejo_habitacional CHselect = chDAO.buscarPorNombre(nombre);
					JOptionPane.showMessageDialog(null, "Cantidad monob: " + CHselect.getCantidadMonob());
					JOptionPane.showMessageDialog(null, "Consorcio: " + CHselect.getConsorcio());
				}
			}
		});
		btnNewButton.setBounds(430, 136, 188, 23);
		add(btnNewButton);

	}
//ESTA MIERDA ME VA A VOLVER LOCO
	private void cargarTabla() {
		// Vacio la tabla
		modeloTabla.setRowCount(0);
		for (Trazado_formal b : new TrazadoFormalDAO().traerTodos()) {
			modeloTabla.addRow(new Object[] { b.getNombre(), TrazadoFormalDAO.TIPO, b.getSuperficie() });
		}
		for (Urbanizacion_planificada b : new UrbanizacionPlanificadaDAO().traerTodos()) {
			modeloTabla.addRow(new Object[] { b.getNombre(), UrbanizacionPlanificadaDAO.TIPO, b.getSuperficie() });
		}
		for (Complejo_habitacional b : new ComplejoHabitacionalDAO().traerTodos()) {
			modeloTabla.addRow(new Object[] { b.getNombre(), ComplejoHabitacionalDAO.TIPO, b.getSuperficie() });
		}
	}
}
