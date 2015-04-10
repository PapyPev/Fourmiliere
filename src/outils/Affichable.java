package outils;

import fourmis.FourmiReine;

/**
 * 
 * @author pev, fred
 * @category cours java
 * @version 20150410
 *
 */
public interface Affichable {

	int getPosX();
	int getPosY();
	boolean isAffichable();
	FourmiReine getFkFourmiReine();
	
}
