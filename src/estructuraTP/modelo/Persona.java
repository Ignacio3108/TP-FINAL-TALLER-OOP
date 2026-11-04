package estructuraTP.modelo;

public class Persona {
	private int dni;
	private String nombre;
	private String nacimiento;
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
	public String getNacimiento() {
		return nacimiento;
	}
	public void setNacimiento(String nacimiento) {
		this.nacimiento = nacimiento;
	}
	public String getSituacion() {
		return situacion;
	}
	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}
	
	public Persona(int dni, String nombre, String nacimiento, String situacion) {
		this.dni = dni;
		this.nombre = nombre;
		this.nacimiento = nacimiento;
		this.situacion = situacion;
	}
	
}