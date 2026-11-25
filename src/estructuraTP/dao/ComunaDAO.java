package estructuraTP.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import estructuraTP.modelo.Barrio;
import estructuraTP.modelo.Comuna;

public class ComunaDAO {
	private final static String URL = "jdbc:mysql://localhost:3306/tp_final_26";
	private final static String USR = "root";
	private final static String PSS = "admin";

	public boolean guardar(Comuna comuna) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "INSERT INTO `comunas` (`numero`, `sede`) VALUES (?, ?);";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, comuna.getNumero());
			stmt.setString(2, comuna.getSede());

			filasAfectadas = stmt.executeUpdate();

		} catch (SQLException e) {
			// Si el número de comuna ya existe se viola el índice único y no se guarda.
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return filasAfectadas == 0 ? false : true;
	}

	public boolean eliminar(int numero) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "DELETE FROM comunas WHERE numero = ?;";

			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, numero);

			filasAfectadas = stmt.executeUpdate();

		} catch (SQLException e) {
			// Si la comuna tiene barrios asociados la clave foránea impide borrarla.
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return filasAfectadas == 0 ? false : true;
	}

	public boolean actualizar(Comuna comunaActualizada, int numeroActual) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "UPDATE comunas SET `numero` = ?, `sede` = ? WHERE `numero` = ?;";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, comunaActualizada.getNumero());
			stmt.setString(2, comunaActualizada.getSede());
			stmt.setInt(3, numeroActual);

			filasAfectadas = stmt.executeUpdate();

		} catch (SQLException e) {
			// Si el nuevo número de comuna ya existe se viola el índice único y no se guarda.
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return filasAfectadas == 0 ? false : true;
	}

	public ArrayList<Comuna> traerTodas() {
		ArrayList<Comuna> comunas = new ArrayList<Comuna>();

		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT numero, sede FROM comunas ORDER BY numero";
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				comunas.add(new Comuna(rs.getInt("numero"), rs.getString("sede")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// Cada comuna incluye los barrios que tiene asociados (de los tres tipos).
		for (Comuna comuna : comunas) {
			cargarBarrios(comuna);
		}

		return comunas;
	}

	private void cargarBarrios(Comuna comuna) {
		for (Barrio b : new TrazadoFormalDAO().traerPorComuna(comuna.getNumero())) {
			comuna.agregarBarrio(b);
		}
		for (Barrio b : new UrbanizacionPlanificadaDAO().traerPorComuna(comuna.getNumero())) {
			comuna.agregarBarrio(b);
		}
		for (Barrio b : new ComplejoHabitacionalDAO().traerPorComuna(comuna.getNumero())) {
			comuna.agregarBarrio(b);
		}
	}

	public Comuna buscarPorNumero(int numero) {
		Comuna comuna = null;
		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT numero, sede FROM comunas WHERE numero = ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, numero);
			ResultSet rs = stmt.executeQuery();

			// El número de comuna es único, por eso alcanza con un if.
			if (rs.next()) {
				comuna = new Comuna(rs.getInt("numero"), rs.getString("sede"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return comuna;
	}

	// Devuelve la comuna a la que está asociado un barrio (tabla intermedia comunas_barrios).
	public Comuna buscarPorBarrio(String nombreBarrio) {
		Comuna comuna = null;
		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT c.numero, c.sede FROM comunas c "
					+ "JOIN comunas_barrios cb ON cb.id_comuna = c.id_comuna "
					+ "JOIN barrios b ON b.id_barrio = cb.id_barrio "
					+ "WHERE b.nombre = ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, nombreBarrio);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				comuna = new Comuna(rs.getInt("numero"), rs.getString("sede"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return comuna;
	}

}
