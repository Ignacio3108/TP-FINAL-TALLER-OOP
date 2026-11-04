package estructuraTP.modelo;

public class Complejo_habitacional extends Barrio {

	private String cantidadMonob;
	private Boolean consorcio;
	
	public String getCantidadMonob() {
		return cantidadMonob;
	}

	public void setCantidadMonob(String cantidadMonob) {
		this.cantidadMonob = cantidadMonob;
	}

	public Boolean getConsorcio() {
		return consorcio;
	}

	public void setConsorcio(Boolean consorcio) {
		this.consorcio = consorcio;
	}

	public Complejo_habitacional(String nombre, int superficie, String cantidadMonob, Boolean consorcio) {
		super(nombre, superficie);
		this.cantidadMonob = cantidadMonob;
		this.consorcio = consorcio;
	}

	//methods
	public int densidad() {
		
	}

	public boolean criticidad() {
		
	}

	public int cantidadPersonas() {
		
	}
}
