package estructuraTP.modelo;

import java.text.Normalizer;
import java.time.LocalDate;

public class Persona {
	private int dni;
	private String nombre;
	private LocalDate nacimiento;
	private String situacion;
	
	public int getDni() {
		return dni;
	}
	public void setDni(int dni) {
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public LocalDate getNacimiento() {
		return nacimiento;
	}
	public void setNacimiento(LocalDate nacimiento) {
		this.nacimiento = nacimiento;
	}
	public String getSituacion() {
		return situacion;
	}
	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}
	
	public Persona(int dni, String nombre, LocalDate nacimiento, String situacion) {
		this.dni = dni;
		this.nombre = nombre;
		this.nacimiento = nacimiento;
		this.situacion = situacion;
	}
	
	//methods
	// Búsqueda por aproximación: ignora mayúsculas y tildes, y cada palabra escrita
	// tiene que estar contenida en el nombre (sin importar el orden).
	public boolean coincideConNombre(String texto) {
		String nombreNormalizado = normalizar(nombre);
		for (String palabra : normalizar(texto).split("\\s+")) {
			if (!nombreNormalizado.contains(palabra)) {
				return false;
			}
		}
		return true;
	}
	
	private static String normalizar(String texto) {
		return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase().trim();
	}
	
}
