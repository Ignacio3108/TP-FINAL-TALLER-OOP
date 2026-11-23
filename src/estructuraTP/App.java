package estructuraTP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import estructuraTP.vista.BarrioConsulta;
import estructuraTP.vista.ComunaConsulta;
import estructuraTP.vista.PersonaConsulta;

public class App {

	public static void main(String[] args) {
		// Único JFrame de toda la aplicación.
		JFrame marco = new JFrame("CensoCABA");
		marco.setVisible(true);
		marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		marco.setBounds(0, 0, 600, 400);

		JMenuBar barraMenu = new JMenuBar();
		JMenu menuComunas = new JMenu("Comunas");
		JMenuItem itemComunas = new JMenuItem("Administrar comunas");
		itemComunas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				marco.setContentPane(new ComunaConsulta());
				marco.validate();
			}
		});
		menuComunas.add(itemComunas);
		barraMenu.add(menuComunas);

		JMenu menuBarrios = new JMenu("Barrios");
		JMenuItem itemBarrios = new JMenuItem("Administrar barrios");
		itemBarrios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				marco.setContentPane(new BarrioConsulta());
				marco.validate();
			}
		});
		menuBarrios.add(itemBarrios);
		barraMenu.add(menuBarrios);

		JMenu menuPersonas = new JMenu("Personas");
		JMenuItem itemPersonas = new JMenuItem("Administrar personas");
		itemPersonas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				marco.setContentPane(new PersonaConsulta());
				marco.validate();
			}
		});
		menuPersonas.add(itemPersonas);
		barraMenu.add(menuPersonas);
		marco.setJMenuBar(barraMenu);

		marco.setContentPane(new ComunaConsulta());
		marco.validate();
	}

}
