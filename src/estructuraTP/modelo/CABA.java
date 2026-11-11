package estructuraTP.modelo;

import java.util.ArrayList;

public class CABA {
	
	ArrayList<Comuna> comunas = new ArrayList<>();
	
	public ArrayList<Comuna> atencionPrioritaria() {
		ArrayList<Comuna> prioritarias = new ArrayList<>();
		
		for(Comuna i : comunas) {
			if(i.percCriticidad() >= 50) {
				prioritarias.add(i);
			}
		}
		return prioritarias;
	}
}
