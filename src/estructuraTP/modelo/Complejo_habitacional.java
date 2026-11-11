package estructuraTP.modelo;

public class Complejo_habitacional extends Barrio {

	private int cantidadMonob;
	private Boolean consorcio;
	
	public int getCantidadMonob() {
		return cantidadMonob;
	}

	public void setCantidadMonob(int cantidadMonob) {
		this.cantidadMonob = cantidadMonob;
	}

	public Boolean getConsorcio() {
		return consorcio;
	}

	public void setConsorcio(Boolean consorcio) {
		this.consorcio = consorcio;
	}

	public Complejo_habitacional(String nombre, int superficie, int cantidadMonob, Boolean consorcio) {
		super(nombre, superficie);
		this.cantidadMonob = cantidadMonob;
		this.consorcio = consorcio;
	}

	//methods
	public int densidad() {
		return cantidadPersonas()/getCantidadMonob();
	}

	public boolean criticidad() {
		return !consorcio;
	}
}
