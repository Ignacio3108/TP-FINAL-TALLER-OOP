package estructuraTP.modelo;

import java.util.ArrayList;

public class Comuna {
	ArrayList<Barrio> barrios = new ArrayList<>();
	
	private int numero;
	private String sede;
	
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getSede() {
		return sede;
	}

	public void setSede(String sede) {
		this.sede = sede;
	}

	public Comuna(int numero, String sede) {
		super();
		this.numero = numero;
		this.sede = sede;
	}
	
	//methods
	public void agregarBarrio(Barrio barrio) {
		barrios.add(barrio);
	}

	public int cantidadBarrios() {
		return barrios.size();
	}

	public int percCriticidad() {
		int barriosCriticos = 0;
		for(Barrio i : barrios) {
			if(i.criticidad()) {
				barriosCriticos++;
			}
		}
		return (barriosCriticos / barrios.size())*100;
	}
}
