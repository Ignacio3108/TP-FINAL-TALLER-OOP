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
//import estructuraTP.modelo.Complejo_habitacional;
import estructuraTP.modelo.Urbanizacion_planificada;

public class UrbanizacionPlanificadaDAO {
	private final static String URL = "jdbc:mysql://localhost:3306/tp_final_26";
	private final static String USR = "root";
	private final static String PSS = "admin";

	public boolean guardar(Urbanizacion_planificada urbanizacionPlanificada) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "INSERT INTO `barrios` (`tipo`, `nombre`, `hectareas`, `cantidad_ph`, `porcentaje_asfalto`, `numero_ley`, `agua_potable`, `cantidad_monoblocks`, `consorcio` ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, "Urbanizacion Planificada");
			stmt.setString(2, urbanizacionPlanificada.getNombre());
			stmt.setInt(3, urbanizacionPlanificada.getSuperficie());
			stmt.setString(4, null);
			stmt.setString(5, null);
			stmt.setString(6, urbanizacionPlanificada.getNroLey());
			stmt.setBoolean(7, urbanizacionPlanificada.getConexionAgua());
			stmt.setString(8, null);
			stmt.setString(9, null);


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

	public boolean actualizar(Urbanizacion_planificada UbPlActualizado, String nombre) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "UPDATE barrios SET `tipo` = ?, `nombre` = ?, `hectareas` = ?, `cantidad_ph` = ?, `porcentaje_asfalto` = ?, `numero_ley` = ?, `agua_potable` = ?, `cantidad_monoblocks` = ?, `consorcio` = ? WHERE `id_barrio` = ?;";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, "Urbanizacion Planificada");
			stmt.setString(2, UbPlActualizado.getNombre());
			stmt.setInt(3, UbPlActualizado.getSuperficie());
			stmt.setString(4, null);
			stmt.setString(5, null);
			stmt.setString(6, UbPlActualizado.getNroLey());
			stmt.setBoolean(7, UbPlActualizado.getConexionAgua());
			stmt.setString(8, null);
			stmt.setString(9, null);
			stmt.setString(10, nombre);

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

	public ArrayList<Urbanizacion_planificada> traerTodas() {
		ArrayList<Urbanizacion_planificada> urbanizacionesPlanificadas = new ArrayList<Urbanizacion_planificada>();

		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre, hectareas, numero_ley, agua_potable FROM barrios ORDER BY id_barrio";
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				urbanizacionesPlanificadas.add(new Urbanizacion_planificada(
				        rs.getString("nombre"),
				        rs.getInt("hectareas"),
				        rs.getString("numero_ley"),
				        rs.getBoolean("agua_potable")));
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

		return urbanizacionesPlanificadas;
	}

	public Urbanizacion_planificada buscarPorNumero(int numero) {
		Urbanizacion_planificada urbanizacionPlanificada = null;
		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre, hectareas, numero_ley, agua_potable FROM barrios WHERE id_barrio = ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, numero);
			ResultSet rs = stmt.executeQuery();

			// El número de comuna es único, por eso alcanza con un if.
			if (rs.next()) {
				urbanizacionPlanificada = new Urbanizacion_planificada(
				        rs.getString("nombre"),
				        rs.getInt("hectareas"),
				        rs.getString("numero_ley"),
				        rs.getBoolean("agua_potable"));
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

		return urbanizacionPlanificada;
	}

}
