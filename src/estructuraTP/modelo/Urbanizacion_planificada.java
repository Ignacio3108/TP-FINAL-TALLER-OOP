package estructuraTP.modelo;

public class Urbanizacion_planificada extends Barrio {

	private String nroLey;
	private Boolean conexionAgua;
	
	public String getNroLey() {
		return nroLey;
	}

	public void setNroLey(String nroLey) {
		this.nroLey = nroLey;
	}

	public Boolean getConexionAgua() {
		return conexionAgua;
	}

	public void setConexionAgua(Boolean conexionAgua) {
		this.conexionAgua = conexionAgua;
	}

	public Urbanizacion_planificada(String nombre, int superficie, String nroLey, Boolean conexionAgua) {
		super(nombre, superficie);
		this.nroLey = nroLey;
		this.conexionAgua = conexionAgua;
	}

	//methods
	public int densidad() {
		return cantidadPersonas()/getSuperficie();
	}

	public boolean criticidad() {
		return !conexionAgua;
	}
}
