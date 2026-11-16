package estructuraTP.modelo;

public class Trazado_formal extends Barrio {

	private int cantidadEdif;
	private float percAsfalto;
	
	public int getCantidadEdif() {
		return cantidadEdif;
	}

	public void setCantidadEdif(int cantidadEdif) {
		this.cantidadEdif = cantidadEdif;
	}

	public float getPercAsfalto() {
		return percAsfalto;
	}

	public void setPercAsfalto(float percAsfalto) {
		this.percAsfalto = percAsfalto;
	}

	public Trazado_formal(String nombre, int superficie, int cantidadEdif, float percAsfalto) {
		super(nombre, superficie);
		this.cantidadEdif = cantidadEdif;
		this.percAsfalto = percAsfalto;
	}

	//methods
	public int densidad() {
		return cantidadPersonas() / cantidadEdif;
	}

	public boolean criticidad() {
		return percAsfalto < 95;
	}
}
