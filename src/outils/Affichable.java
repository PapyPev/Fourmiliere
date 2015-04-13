package outils;

import fourmis.FourmiReine;

/**
 * 
 * @author pev
 * @version 20150413
 *
 */
public interface Affichable {

	int getPosX();
	int getPosY();
	boolean isAffichable();
	FourmiReine getFkFourmiReine();
	
}
