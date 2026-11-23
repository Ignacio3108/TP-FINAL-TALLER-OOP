package estructuraTP.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import estructuraTP.modelo.Barrio;
import estructuraTP.modelo.Persona;

public class PersonaDAO {
	private final static String URL = "jdbc:mysql://localhost:3306/tp_final_26";
	private final static String USR = "root";
	private final static String PSS = "admin";

	// La situación laboral y el barrio se buscan por su nombre (atributo del mundo
	// real), sin manejar ids en Java.
	private final static String SUBSELECT_SITUACION = "(SELECT id_situacion_laboral FROM situaciones_laborales WHERE nombre = ?)";
	private final static String SUBSELECT_BARRIO = "(SELECT id_barrio FROM barrios WHERE nombre = ?)";

	public boolean guardar(Persona persona, Barrio barrio) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "INSERT INTO `personas` (`dni`, `nombre`, `nacimiento`, `id_situacion_laboral`, `id_barrio`) VALUES (?, ?, ?, "
					+ SUBSELECT_SITUACION + ", " + SUBSELECT_BARRIO + ");";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, String.valueOf(persona.getDni()));
			stmt.setString(2, persona.getNombre());
			stmt.setDate(3, java.sql.Date.valueOf(persona.getNacimiento()));
			stmt.setString(4, persona.getSituacion());
			stmt.setString(5, barrio.getNombre());

			filasAfectadas = stmt.executeUpdate();

		} catch (SQLException e) {
			// Si el DNI ya existe se viola el índice único y no se guarda.
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

	public boolean eliminar(int dni) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "DELETE FROM personas WHERE dni = ?;";

			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, String.valueOf(dni));

			filasAfectadas = stmt.executeUpdate();

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
		return filasAfectadas == 0 ? false : true;
	}

	public boolean actualizar(Persona personaActualizada, Barrio barrio, int dniActual) {
		Connection c = null;
		int filasAfectadas = 0;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "UPDATE personas SET `dni` = ?, `nombre` = ?, `nacimiento` = ?, `id_situacion_laboral` = "
					+ SUBSELECT_SITUACION + ", `id_barrio` = " + SUBSELECT_BARRIO + " WHERE `dni` = ?;";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, String.valueOf(personaActualizada.getDni()));
			stmt.setString(2, personaActualizada.getNombre());
			stmt.setDate(3, java.sql.Date.valueOf(personaActualizada.getNacimiento()));
			stmt.setString(4, personaActualizada.getSituacion());
			stmt.setString(5, barrio.getNombre());
			stmt.setString(6, String.valueOf(dniActual));

			filasAfectadas = stmt.executeUpdate();

		} catch (SQLException e) {
			// Si el nuevo DNI ya existe se viola el índice único y no se guarda.
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

	public ArrayList<Persona> traerTodas() {
		ArrayList<Persona> personas = new ArrayList<Persona>();

		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT p.dni, p.nombre, p.nacimiento, s.nombre AS situacion FROM personas p "
					+ "JOIN situaciones_laborales s ON s.id_situacion_laboral = p.id_situacion_laboral ORDER BY p.nombre";
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				personas.add(new Persona(Integer.parseInt(rs.getString("dni")), rs.getString("nombre"),
						rs.getDate("nacimiento").toLocalDate(), rs.getString("situacion")));
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

		return personas;
	}

	public Persona buscarPorDni(int dni) {
		Persona persona = null;
		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT p.dni, p.nombre, p.nacimiento, s.nombre AS situacion FROM personas p "
					+ "JOIN situaciones_laborales s ON s.id_situacion_laboral = p.id_situacion_laboral WHERE p.dni = ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, String.valueOf(dni));
			ResultSet rs = stmt.executeQuery();

			// El DNI es único, por eso alcanza con un if.
			if (rs.next()) {
				persona = new Persona(Integer.parseInt(rs.getString("dni")), rs.getString("nombre"),
						rs.getDate("nacimiento").toLocalDate(), rs.getString("situacion"));
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

		return persona;
	}

	// Nombre del barrio al que está asociada la persona (null si no se encuentra).
	public String buscarNombreBarrio(int dni) {
		String nombreBarrio = null;
		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT b.nombre FROM barrios b JOIN personas p ON p.id_barrio = b.id_barrio WHERE p.dni = ?";
			PreparedStatement stmt = c.prepareStatement(sql);
			stmt.setString(1, String.valueOf(dni));
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				nombreBarrio = rs.getString("nombre");
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

		return nombreBarrio;
	}

	// Situaciones laborales precargadas en la base de datos.
	public ArrayList<String> traerSituaciones() {
		ArrayList<String> situaciones = new ArrayList<String>();

		Connection c = null;
		try {
			c = DriverManager.getConnection(URL, USR, PSS);
			String sql = "SELECT nombre FROM situaciones_laborales ORDER BY id_situacion_laboral";
			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				situaciones.add(rs.getString("nombre"));
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

		return situaciones;
	}

}
