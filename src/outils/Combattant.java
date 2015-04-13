package outils;

import fourmis.Fourmi.EnumFourmi;

/**
 * 
 * @author pev
 * @version 20150413
 *
 */
public interface Combattant {
	
	int getIdFourmi();
	
	int getPointDeVie();
	void setPointDeVie(int pointDeVie);
	
	int getPointDeForce();
	
	EnumFourmi getTypeFourmi();
	
	
	
}
