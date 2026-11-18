package estructuraTP.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//import estructuraTP.modelo.Barrio;
//import estructuraTP.modelo.Trazado_formal;
import estructuraTP.modelo.Complejo_habitacional;
//import estructuraTP.modelo.Urbanizacion_planificada;

public class ComplejoHabitacionalDAO {
	private final static String URL = "jdbc:mysql://localhost:3306/tp_final_26";
	private final static String USR = "root";
	private final static String PSS = "admin";

	public boolean guardar(Complejo_habitacional complejoHabitacional) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "INSERT INTO `barrios` (`tipo`, `nombre`, `hectareas`, `cantidad_ph`, `porcentaje_asfalto`, `numero_ley`, `agua_potable`, `cantidad_monoblocks`, `consorcio` ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, "Urbanizacion Planificada");
			stmt.setString(2, complejoHabitacional.getNombre());
			stmt.setInt(3, complejoHabitacional.getSuperficie());
			stmt.setString(4, null);
			stmt.setString(5, null);
			stmt.setString(6, null);
			stmt.setString(7, null);
			stmt.setInt(8, complejoHabitacional.getCantidadMonob());
			stmt.setBoolean(9, complejoHabitacional.getConsorcio());


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

	public boolean eliminar(String nombre) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "DELETE FROM barrios WHERE nombre = ?;";

			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, nombre);

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

	public boolean actualizar(Complejo_habitacional CoHaActualizado, int idActual) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "UPDATE barrios SET `tipo` = ?, `nombre` = ?, `hectareas` = ?, `cantidad_ph` = ?, `porcentaje_asfalto` = ?, `numero_ley` = ?, `agua_potable` = ?, `cantidad_monoblocks` = ?, `consorcio` = ? WHERE `id_barrio` = ?;";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, "Urbanizacion Planificada");
			stmt.setString(2, CoHaActualizado.getNombre());
			stmt.setInt(3, CoHaActualizado.getSuperficie());
			stmt.setString(4, null);
			stmt.setString(5, null);
			stmt.setString(6, null);
			stmt.setString(7, null);
			stmt.setInt(8, CoHaActualizado.getCantidadMonob());
			stmt.setBoolean(9, CoHaActualizado.getConsorcio());
			stmt.setInt(10, idActual);

			filasAfectadas = stmt.executeUpdate();

		} catch (SQLException e) {
			// Si el nuevo número de comuna ya existe se viola el índice único y no se
			// guarda.
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

	public ArrayList<Complejo_habitacional> traerTodas() {
		ArrayList<Complejo_habitacional> complejosHabitacionales = new ArrayList<Complejo_habitacional>();

		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre, hectareas, cantidad_monoblocks, consorcio FROM barrios ORDER BY id_barrio";
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				complejosHabitacionales.add(new Complejo_habitacional(
				        rs.getString("nombre"),
				        rs.getInt("hectareas"),
				        rs.getInt("cantidad_monoblocks"),
				        rs.getBoolean("consorcio")));
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

		return complejosHabitacionales;
	}

	public Complejo_habitacional buscarPorNumero(int numero) {
		Complejo_habitacional complejoHabitacional = null;
		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre, hectareas, cantidad_monoblocks, consorcio FROM barrios WHERE id_barrio = ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, numero);
			ResultSet rs = stmt.executeQuery();

			// El número de comuna es único, por eso alcanza con un if.
			if (rs.next()) {
				complejoHabitacional = new Complejo_habitacional(
				        rs.getString("nombre"),
				        rs.getInt("hectareas"),
				        rs.getInt("cantidad_monoblocks"),
				        rs.getBoolean("consorcio"));
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

		return complejoHabitacional;
	}

}
