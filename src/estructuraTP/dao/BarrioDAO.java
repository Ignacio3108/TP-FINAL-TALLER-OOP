package estructuraTP.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import estructuraTP.modelo.Barrio;
import estructuraTP.modelo.Trazado_formal;
import estructuraTP.modelo.Complejo_habitacional;
import estructuraTP.modelo.Urbanizacion_planificada;


//EN PROCESO - NI TERMINE EL PRIMER GUARDADO PARA TRAZADO FORMAL.

public class BarrioDAO {
	private final static String URL = "jdbc:mysql://localhost:3306/tp_final_26";
	private final static String USR = "root";
	private final static String PSS = "admin";

	public boolean guardarTrazadoFormal(Trazado_formal trazadoFormal) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "INSERT INTO `barrios` (`tipo`, `nombre`, `hectareas`, `porcentaje_asfalto`, `cantidad_ph`, ) VALUES (´trazado formal´, ?, ?, ?, ?);";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, Trazado_formal.get());
			stmt.setString(2, barrio.getSede());

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

		return comunas;
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

}
