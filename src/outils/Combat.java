package outils;

/**
 * 
 * @author guillaume
 * @version 201504013
 *
 */
public class Combat <T extends Combattant, K extends Combattant>{
	
	T fourmi1;
	K fourmi2;
	
	public Combat( T fourmi1, K fourmi2){
		
		System.out.println("Combat: {" + fourmi1.getClass() + fourmi1.getIdFourmi() + " | " + fourmi2.getClass() + fourmi2.getIdFourmi() + "}");
		
		// Verification pas du meme type de fourmi
		if(fourmi1.getTypeFourmi() != fourmi2.getTypeFourmi()){
			
			// Verification que les deux ont encore des points de vie
			if(fourmi1.getPointDeVie() > 0 && fourmi2.getPointDeVie() > 0){
				
				// On retire autant de point de vie qu'il y a de force a l'adverssaire
				fourmi1.setPointDeVie(fourmi1.getPointDeVie()-fourmi2.getPointDeForce());
				fourmi2.setPointDeVie(fourmi2.getPointDeVie()-fourmi1.getPointDeForce());
				
			}
			
		}
		
	}

}
