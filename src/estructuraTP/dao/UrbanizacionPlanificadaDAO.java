package estructuraTP.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import estructuraTP.modelo.Comuna;
import estructuraTP.modelo.Urbanizacion_planificada;

public class UrbanizacionPlanificadaDAO {
	private final static String URL = "jdbc:mysql://localhost:3306/tp_final_26";
	private final static String USR = "root";
	private final static String PSS = "admin";

	// Valor que se guarda en la columna barrios.tipo para este tipo de barrio.
	public final static String TIPO = "Urbanizacion planificada";

	private final static String COLUMNAS = "`tipo` = ?, `nombre` = ?, `hectareas` = ?, `cantidad_ph` = ?, `porcentaje_asfalto` = ?, `numero_ley` = ?, `agua_potable` = ?, `cantidad_monoblocks` = ?, `consorcio` = ?";

	public boolean guardar(Urbanizacion_planificada urbanizacionPlanificada, Comuna comuna) {
		Connection c = null;
		boolean guardado = false;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			// El barrio y su comuna se guardan juntos o no se guarda nada.
			c.setAutoCommit(false);
			String sql = "INSERT INTO `barrios` (`tipo`, `nombre`, `hectareas`, `cantidad_ph`, `porcentaje_asfalto`, `numero_ley`, `agua_potable`, `cantidad_monoblocks`, `consorcio`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = c.prepareStatement(sql);
			cargarParametros(stmt, urbanizacionPlanificada);

			stmt.executeUpdate();
			ComunasBarriosDAO.asociar(c, urbanizacionPlanificada.getNombre(), comuna.getNumero());
			c.commit();
			guardado = true;

		} catch (SQLException e) {
			// Si el nombre del barrio ya existe se viola el índice único y no se guarda.
			e.printStackTrace();
			ComunasBarriosDAO.deshacer(c);
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return guardado;
	}

	public boolean eliminar(String nombre) {
		Connection c = null;
		boolean eliminado = false;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			c.setAutoCommit(false);
			// Primero se borra la asociación con la comuna y después el barrio.
			ComunasBarriosDAO.desasociar(c, nombre);

			String sql = "DELETE FROM barrios WHERE nombre = ? AND tipo = ?;";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, nombre);
			stmt.setString(2, TIPO);

			if (stmt.executeUpdate() > 0) {
				c.commit();
				eliminado = true;
			} else {
				ComunasBarriosDAO.deshacer(c);
			}

		} catch (SQLException e) {
			// Si el barrio tiene personas asociadas la clave foránea impide borrarlo.
			e.printStackTrace();
			ComunasBarriosDAO.deshacer(c);
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return eliminado;
	}

	public boolean actualizar(Urbanizacion_planificada urbanizacionPlanificadaActualizado, Comuna comuna, String nombreActual) {
		Connection c = null;
		boolean actualizado = false;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			c.setAutoCommit(false);
			// No se filtra por tipo: el UPDATE pisa todas las columnas, así que también
			// sirve si el barrio cambia de tipo.
			String sql = "UPDATE barrios SET " + COLUMNAS + " WHERE `nombre` = ?;";
			PreparedStatement stmt = c.prepareStatement(sql);
			cargarParametros(stmt, urbanizacionPlanificadaActualizado);
			stmt.setString(10, nombreActual);

			if (stmt.executeUpdate() > 0) {
				ComunasBarriosDAO.desasociar(c, urbanizacionPlanificadaActualizado.getNombre());
				ComunasBarriosDAO.asociar(c, urbanizacionPlanificadaActualizado.getNombre(), comuna.getNumero());
				c.commit();
				actualizado = true;
			} else {
				ComunasBarriosDAO.deshacer(c);
			}

		} catch (SQLException e) {
			// Si el nuevo nombre ya existe se viola el índice único y no se guarda.
			e.printStackTrace();
			ComunasBarriosDAO.deshacer(c);
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return actualizado;
	}

	public ArrayList<Urbanizacion_planificada> traerTodos() {
		ArrayList<Urbanizacion_planificada> barrios = new ArrayList<Urbanizacion_planificada>();

		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre, hectareas, numero_ley, agua_potable FROM barrios WHERE tipo = ? ORDER BY nombre";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, TIPO);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				barrios.add(new Urbanizacion_planificada(rs.getString("nombre"), rs.getInt("hectareas"), rs.getString("numero_ley"), rs.getBoolean("agua_potable")));
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

		return barrios;
	}

	public Urbanizacion_planificada buscarPorNombre(String nombre) {
		Urbanizacion_planificada urbanizacionPlanificada = null;
		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre, hectareas, numero_ley, agua_potable FROM barrios WHERE nombre = ? AND tipo = ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, nombre);
			stmt.setString(2, TIPO);
			ResultSet rs = stmt.executeQuery();

			// El nombre del barrio es único, por eso alcanza con un if.
			if (rs.next()) {
				urbanizacionPlanificada = new Urbanizacion_planificada(rs.getString("nombre"), rs.getInt("hectareas"), rs.getString("numero_ley"), rs.getBoolean("agua_potable"));
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

	// Barrios de este tipo que pertenecen a la comuna con ese número.
	public ArrayList<Urbanizacion_planificada> traerPorComuna(int numeroComuna) {
		ArrayList<Urbanizacion_planificada> barrios = new ArrayList<Urbanizacion_planificada>();

		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT b.nombre, b.hectareas, b.numero_ley, b.agua_potable FROM barrios b "
					+ "JOIN comunas_barrios cb ON cb.id_barrio = b.id_barrio "
					+ "JOIN comunas c ON c.id_comuna = cb.id_comuna "
					+ "WHERE c.numero = ? AND b.tipo = ? ORDER BY b.nombre";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, numeroComuna);
			stmt.setString(2, TIPO);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				barrios.add(new Urbanizacion_planificada(rs.getString("nombre"), rs.getInt("hectareas"), rs.getString("numero_ley"), rs.getBoolean("agua_potable")));
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

		return barrios;
	}

	// Parámetros 1 a 9, en el orden de las columnas de la tabla barrios.
	// Las columnas que no corresponden a este tipo de barrio se guardan en NULL.
	private void cargarParametros(PreparedStatement stmt, Urbanizacion_planificada urbanizacionPlanificada) throws SQLException {
		stmt.setString(1, TIPO);
		stmt.setString(2, urbanizacionPlanificada.getNombre());
		stmt.setInt(3, urbanizacionPlanificada.getSuperficie());
		stmt.setNull(4, Types.INTEGER);
		stmt.setNull(5, Types.FLOAT);
		stmt.setString(6, urbanizacionPlanificada.getNroLey());
		stmt.setBoolean(7, urbanizacionPlanificada.getConexionAgua());
		stmt.setNull(8, Types.INTEGER);
		stmt.setNull(9, Types.BIT);
	}

}
