package interfaces;

import java.util.Observable;

/**
 * 
 * @author pev, fred
 * @category cours java
 * @version 20150331
 *
 */
public class Pheromone extends Observable{

	/* ===========================ATRB================================ */

	private Object message;
	
	/* ===========================CONST================================ */

	public Pheromone(){
		super();
	}
	
	/* ==========================G/S================================== */
	
	public Object getMessage(){
		return this.message;
	}
	
	public void setMessage(Object message){
		this.message = message;
		notifierObserver();
	}
	
	/* ===========================ACTIONS============================= */
	
	/**
	 * Methode permettant de notifier les Observers lors d'un changement d'etat
	 */
	private void notifierObserver(){
		setChanged(); //annonce a Observable qu'il y un changement
		notifyObservers(this.message); //annonce aux Observeurs qu'il y a eu une modification
	}

}
