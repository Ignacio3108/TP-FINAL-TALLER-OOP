package estructuraTP.modelo;

import java.util.ArrayList;

public class Comuna {
	ArrayList<Barrio> barrios = new ArrayList<>();
	
	private int numero;
	private String sede;
	private String junta;
	
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

	public String getJunta() {
		return junta;
	}

	public void setJunta(String junta) {
		this.junta = junta;
	}

	public Comuna(int numero, String sede, String junta) {
		super();
		this.numero = numero;
		this.sede = sede;
		this.junta = junta;
	}
	
	//methods
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
