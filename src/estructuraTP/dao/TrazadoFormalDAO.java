package estructuraTP.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

//import estructuraTP.modelo.Barrio;
import estructuraTP.modelo.Trazado_formal;
//import estructuraTP.modelo.Complejo_habitacional;
//import estructuraTP.modelo.Urbanizacion_planificada;

public class TrazadoFormalDAO {
	private final static String URL = "jdbc:mysql://localhost:3306/tp_final_26";
	private final static String USR = "root";
	private final static String PSS = "admin";

	public boolean guardar(Trazado_formal trazadoFormal) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "INSERT INTO `barrios` (`tipo`, `nombre`, `hectareas`, `cantidad_ph`, `porcentaje_asfalto`, `numero_ley`, `agua_potable`, `cantidad_monoblocks`, `consorcio` ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, "Trazado Formal");
			stmt.setString(2, trazadoFormal.getNombre());
			stmt.setInt(3, trazadoFormal.getSuperficie());
			stmt.setInt(4, trazadoFormal.getCantidadEdif());
			stmt.setFloat(5, trazadoFormal.getPercAsfalto());
			stmt.setString(6, null);
			stmt.setString(7, null);
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

	public boolean actualizar(Trazado_formal TrFmActualizado, String nombre) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "UPDATE barrios SET `tipo` = ?, `nombre` = ?, `hectareas` = ?, `cantidad_ph` = ?, `porcentaje_asfalto` = ?, `numero_ley` = ?, `agua_potable` = ?, `cantidad_monoblocks` = ?, `consorcio` = ? WHERE `nombre` = ?;";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, "Trazado Formal");
			stmt.setString(2, TrFmActualizado.getNombre());
			stmt.setInt(3, TrFmActualizado.getSuperficie());
			stmt.setInt(4, TrFmActualizado.getCantidadEdif());
			stmt.setFloat(5, TrFmActualizado.getPercAsfalto());
			stmt.setString(6, null);
			stmt.setString(7, null);
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

	public ArrayList<Trazado_formal> traerTodas() {
		ArrayList<Trazado_formal> trazadosFormales = new ArrayList<Trazado_formal>();

		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre, hectareas, cantidad_ph, porcentaje_asfalto FROM barrios ORDER BY id_barrio";
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				trazadosFormales.add(new Trazado_formal(
				        rs.getString("nombre"),
				        rs.getInt("hectareas"),
				        rs.getInt("cantidad_ph"),
				        rs.getFloat("porcentaje_asfalto")));
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

		return trazadosFormales;
	}

	public Trazado_formal buscarPorNumero(int numero) {
		Trazado_formal trazadoFormal = null;
		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre, hectareas, cantidad_ph, porcentaje_asfalto FROM barrios WHERE id_barrio = ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, numero);
			ResultSet rs = stmt.executeQuery();

			// El número de comuna es único, por eso alcanza con un if.
			if (rs.next()) {
				trazadoFormal = new Trazado_formal(
				        rs.getString("nombre"),
				        rs.getInt("hectareas"),
				        rs.getInt("cantidad_ph"),
				        rs.getFloat("porcentaje_asfalto"));
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

		return trazadoFormal;
	}

}
