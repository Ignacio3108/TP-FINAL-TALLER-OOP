package estructuraTP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import estructuraTP.vista.ComunaConsulta;

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
		marco.setJMenuBar(barraMenu);

		marco.setContentPane(new ComunaConsulta());
		marco.validate();
	}

}
