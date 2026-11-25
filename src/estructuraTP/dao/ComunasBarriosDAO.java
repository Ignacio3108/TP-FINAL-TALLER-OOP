package estructuraTP.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Maneja la tabla intermedia comunas_barrios. Es de uso interno de los DAO de
 * barrios: no abre conexiones propias, usa la que le pasan para que todo
 * ocurra dentro de la misma transacción.
 */
class ComunasBarriosDAO {

	static void asociar(Connection c, String nombreBarrio, int numeroComuna) throws SQLException {
		String sql = "INSERT INTO `comunas_barrios` (`id_barrio`, `id_comuna`) VALUES "
				+ "((SELECT id_barrio FROM barrios WHERE nombre = ?), (SELECT id_comuna FROM comunas WHERE numero = ?));";
		PreparedStatement stmt = c.prepareStatement(sql);
		stmt.setString(1, nombreBarrio);
		stmt.setInt(2, numeroComuna);
		// Si la comuna no existe el subselect devuelve NULL y MySQL rechaza el INSERT.
		stmt.executeUpdate();
	}

	static void desasociar(Connection c, String nombreBarrio) throws SQLException {
		String sql = "DELETE FROM comunas_barrios WHERE id_barrio = (SELECT id_barrio FROM barrios WHERE nombre = ?);";
		PreparedStatement stmt = c.prepareStatement(sql);
		stmt.setString(1, nombreBarrio);
		stmt.executeUpdate();
	}

	static void deshacer(Connection c) {
		if (c != null) {
			try {
				c.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
