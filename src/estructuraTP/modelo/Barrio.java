package estructuraTP.modelo;

import java.util.ArrayList;

public abstract class Barrio {
	ArrayList<Persona> personas = new ArrayList<>();
	
	private String nombre;
	private int superficie;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getSuperficie() {
		return superficie;
	}
	public void setSuperficie(int superficie) {
		this.superficie = superficie;
	}
	
	public Barrio(String nombre, int superficie) {
		this.nombre = nombre;
		this.superficie = superficie;
	}
	
	//methods
	public abstract int densidad();
	public abstract boolean criticidad();
	
	public int cantidadPersonas() {		
		return personas.size();
	}
}
