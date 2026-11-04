package estructuraTP.modelo;

public class Trazado_formal extends Barrio {

	private String cantidadEdif;
	private float percAsfalto;
	
	public String getCantidadEdif() {
		return cantidadEdif;
	}

	public void setCantidadEdif(String cantidadEdif) {
		this.cantidadEdif = cantidadEdif;
	}

	public float getPercAsfalto() {
		return percAsfalto;
	}

	public void setPercAsfalto(float percAsfalto) {
		this.percAsfalto = percAsfalto;
	}

	public Trazado_formal(String nombre, int superficie, String cantidadEdif, float percAsfalto) {
		super(nombre, superficie);
		this.cantidadEdif = cantidadEdif;
		this.percAsfalto = percAsfalto;
	}

	//methods
	public int densidad() {
		
	}

	public boolean criticidad() {
		
	}

	public int cantidadPersonas() {
		
	}
}
