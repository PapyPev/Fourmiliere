package interfaces;

import java.util.Observable;

/**
 * 
 * @author pev
 * @category cours java
 * @version 20150328
 *
 */
public class Pheromone extends Observable{

	/* ===========================ATRB================================ */

	private String message;
	
	/* ===========================CONST================================ */

	public Pheromone(){
		super();
	}
	
	/* ==========================G/S================================== */
	
	public String getMessage(){
		return this.message;
	}
	
	public void setMessage(String message){
		this.message = message;
		notifierObserver();
	}
	
	/* ===========================ACTIONS============================= */
	
	/**
	 * Methode permettant de notifier les Observers lors d'un changement d'etat
	 */
	private void notifierObserver(){
		setChanged(); //annonce a Observable qu'il y un changement
		notifyObservers(); //annonce aux Observeurs qu'il y a eu une modification
	}

}
