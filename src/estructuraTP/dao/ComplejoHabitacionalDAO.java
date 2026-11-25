package estructuraTP.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import estructuraTP.modelo.Comuna;
import estructuraTP.modelo.Complejo_habitacional;

public class ComplejoHabitacionalDAO {
	private final static String URL = "jdbc:mysql://localhost:3306/tp_final_26";
	private final static String USR = "root";
	private final static String PSS = "admin";

	// Valor que se guarda en la columna barrios.tipo para este tipo de barrio.
	public final static String TIPO = "Complejo habitacional";

	private final static String COLUMNAS = "`tipo` = ?, `nombre` = ?, `hectareas` = ?, `cantidad_ph` = ?, `porcentaje_asfalto` = ?, `numero_ley` = ?, `agua_potable` = ?, `cantidad_monoblocks` = ?, `consorcio` = ?";

	public boolean guardar(Complejo_habitacional complejoHabitacional, Comuna comuna) {
		Connection c = null;
		boolean guardado = false;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			// El barrio y su comuna se guardan juntos o no se guarda nada.
			c.setAutoCommit(false);
			String sql = "INSERT INTO `barrios` (`tipo`, `nombre`, `hectareas`, `cantidad_ph`, `porcentaje_asfalto`, `numero_ley`, `agua_potable`, `cantidad_monoblocks`, `consorcio`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement stmt = c.prepareStatement(sql);
			cargarParametros(stmt, complejoHabitacional);

			stmt.executeUpdate();
			ComunasBarriosDAO.asociar(c, complejoHabitacional.getNombre(), comuna.getNumero());
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

	public boolean actualizar(Complejo_habitacional complejoHabitacionalActualizado, Comuna comuna, String nombreActual) {
		Connection c = null;
		boolean actualizado = false;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			c.setAutoCommit(false);
			// No se filtra por tipo: el UPDATE pisa todas las columnas, así que también
			// sirve si el barrio cambia de tipo.
			String sql = "UPDATE barrios SET " + COLUMNAS + " WHERE `nombre` = ?;";
			PreparedStatement stmt = c.prepareStatement(sql);
			cargarParametros(stmt, complejoHabitacionalActualizado);
			stmt.setString(10, nombreActual);

			if (stmt.executeUpdate() > 0) {
				ComunasBarriosDAO.desasociar(c, complejoHabitacionalActualizado.getNombre());
				ComunasBarriosDAO.asociar(c, complejoHabitacionalActualizado.getNombre(), comuna.getNumero());
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

	public ArrayList<Complejo_habitacional> traerTodos() {
		ArrayList<Complejo_habitacional> barrios = new ArrayList<Complejo_habitacional>();

		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre, hectareas, cantidad_monoblocks, consorcio FROM barrios WHERE tipo = ? ORDER BY nombre";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, TIPO);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				barrios.add(new Complejo_habitacional(rs.getString("nombre"), rs.getInt("hectareas"), rs.getInt("cantidad_monoblocks"), rs.getBoolean("consorcio")));
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

	public Complejo_habitacional buscarPorNombre(String nombre) {
		Complejo_habitacional complejoHabitacional = null;
		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre, hectareas, cantidad_monoblocks, consorcio FROM barrios WHERE nombre = ? AND tipo = ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, nombre);
			stmt.setString(2, TIPO);
			ResultSet rs = stmt.executeQuery();

			// El nombre del barrio es único, por eso alcanza con un if.
			if (rs.next()) {
				complejoHabitacional = new Complejo_habitacional(rs.getString("nombre"), rs.getInt("hectareas"), rs.getInt("cantidad_monoblocks"), rs.getBoolean("consorcio"));
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

	// Barrios de este tipo que pertenecen a la comuna con ese número.
	public ArrayList<Complejo_habitacional> traerPorComuna(int numeroComuna) {
		ArrayList<Complejo_habitacional> barrios = new ArrayList<Complejo_habitacional>();

		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT b.nombre, b.hectareas, b.cantidad_monoblocks, b.consorcio FROM barrios b "
					+ "JOIN comunas_barrios cb ON cb.id_barrio = b.id_barrio "
					+ "JOIN comunas c ON c.id_comuna = cb.id_comuna "
					+ "WHERE c.numero = ? AND b.tipo = ? ORDER BY b.nombre";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setInt(1, numeroComuna);
			stmt.setString(2, TIPO);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				barrios.add(new Complejo_habitacional(rs.getString("nombre"), rs.getInt("hectareas"), rs.getInt("cantidad_monoblocks"), rs.getBoolean("consorcio")));
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

	// ESTRUCTURE ASI PARA MA ORDEN.
	// Las columnas que no corresponden a este tipo de barrio se guardan en NULL.
	private void cargarParametros(PreparedStatement stmt, Complejo_habitacional complejoHabitacional) throws SQLException {
		stmt.setString(1, TIPO);
		stmt.setString(2, complejoHabitacional.getNombre());
		stmt.setInt(3, complejoHabitacional.getSuperficie());
		stmt.setNull(4, Types.INTEGER);
		stmt.setNull(5, Types.FLOAT);
		stmt.setNull(6, Types.VARCHAR);
		stmt.setNull(7, Types.BIT);
		stmt.setInt(8, complejoHabitacional.getCantidadMonob());
		stmt.setBoolean(9, complejoHabitacional.getConsorcio());
	}

}
