package fourmis;

import java.util.Observable;
import java.util.Observer;

import environnements.Terrain;
import fourmis.FourmiReine.EnumPheromone;
import outils.Affichable;
import outils.Combattant;

/**
 * 
 * @author pev
 * @version 20150413
 *
 */
public class FourmiChef extends Fourmi implements Affichable, Combattant, Runnable, Observer{
	
	/* ===========================ATRB================================ */

	private FourmiReine fkFourmiReine;
	private int qtNourritureRecoltee;
	private EnumPheromone pheromoneCourant;
	private int nbSoldatVivant;
	
	/* ===========================CONST================================ */

	/**
	 * Constructeur d'une fourmi Chef
	 * @param idFourmi : identifiant de la fourmi
	 * @param typeFourmi : couleur de la fourmi
	 * @param fkTerrain : terrainde reference
	 * @param combattant : si la fourmi est combattante ou non
	 * @param dureeVie : duree de vie en seconde
	 * @param pointDeForce : point de force
	 * @param posX : position sur le terrain, ligne
	 * @param posY : position sur le terrain, colonne
	 * @param qtNourritureTransportee : qt nourriture transportee
	 * @param qtNourritureTransportable : qt nourriture transportable
	 * @param fkFourmiReine : reference a la fourmi reine
	 * @param qtNourritureRecoltee : compte le nombre de nourriture recoltee
	 * @param nbSoldatVivant : nombre de soldat vivant

	 */
	public FourmiChef(int idFourmi, EnumFourmi typeFourmi, Terrain fkTerrain,
			boolean combattant, int dureeVie, int pointDeForce, int posX, int posY,
			int qtNourritureTransportee, int qtNourritureTransportable, FourmiReine fkFourmiReine, 
			int qtNourritureRecoltee, int nbSoldatVivant) {
		super(idFourmi, typeFourmi, fkTerrain, combattant, dureeVie, pointDeForce, 
				posX, posY, qtNourritureTransportee, qtNourritureTransportable);
		this.fkFourmiReine = fkFourmiReine;
		this.qtNourritureRecoltee = qtNourritureRecoltee;
		this.pheromoneCourant = EnumPheromone.RIEN;
		this.nbSoldatVivant = nbSoldatVivant;
	}
	
	/* ==========================G/S================================== */

	public FourmiReine getFkFourmiReine() {
		return fkFourmiReine;
	}
	public void setFkFourmiReine(FourmiReine fkFourmiReine) {
		this.fkFourmiReine = fkFourmiReine;
	}
	
	public int getQtNourritureRecoltee() {
		return qtNourritureRecoltee;
	}
	public void setQtNourritureRecoltee(int qtNourritureRecoltee) {
		this.qtNourritureRecoltee = qtNourritureRecoltee;
	}
	
	public EnumPheromone getPheromoneCourant() {
		return pheromoneCourant;
	}
	public void setPheromoneCourant(EnumPheromone pheromoneCourant) {
		this.pheromoneCourant = pheromoneCourant;
	}
	
	public int getNbSoldatVivant() {
		return this.nbSoldatVivant;
	}
	public void setNbSoldatVivant(int nbSoldatVivant) {
		this.nbSoldatVivant = nbSoldatVivant;
	}
	
	/* ===========================ACTIONS============================= */
	
	@Override
	public String toString() {
		return "FourmiChef [fkFourmiReine=" + fkFourmiReine
				+ ", qtNourritureRecoltee=" + qtNourritureRecoltee
				+ ", toString()=" + super.toString() + "]";
	}

	/**
	 * Fonction qui met a jour le nombre de soldat vivant
	 */
	public synchronized void miseAJourNbSoldatVivants(){
		// Decremente le nombre 
		this.nbSoldatVivant --;
		// Debloque le wait du thread en cours
		notify(); 
	}

	/**
	 * Methode permettant de crer un thread par fourmi chef
	 * et de leur donner une vie independante
	 */
	@Override
	public void eclosion() {
		System.out.println("Eclosion Chef" + this.getIdFourmi());
		Thread nouveauThread = new Thread(this);
		nouveauThread.start(); // Lance le thread en executant la fonction run
	}
	
	/**
	 * Methode permettant a ue fourmi soldat de vivre son existance
	 */
	@Override
	public synchronized void run() {
		// Si le nombre de soldat est superieur a zero
		while(this.nbSoldatVivant > 0){
			// Action a effectuer en fonction du pheromone en cours
			switch(this.pheromoneCourant){
				case RIEN:
				case VIVRE:
				case NOURRITURE:
				case ATTAQUER:
					break; // Ne rien faire
				case MOURIR:
					this.nbSoldatVivant = 0; // Fonction qui tue le thread en cours de la fourmi
					break;
				default:
					System.out.println("WARNING: Update Chef, Message pheromone inconnu.");
					this.setPheromoneCourant(EnumPheromone.RIEN);
					break;
			}
			
			// Attend dans le thread un notify pour continuer
			try{
				wait();
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		// Fin du thread
		System.out.println(this.getIdFourmi() + "C: Je suis mort");
	}

	/**
	 * Methode permettant d'actualiser les actions de la fourmi selon le pheromone
	 * de la mere
	 */
	@Override
	public synchronized void update(Observable o, Object arg) {
				
		// Cast de l'objet recu au bon format
		EnumPheromone pheromoneRecu = (EnumPheromone)arg;
		
		if(pheromoneRecu != null){
			// Met a jour le message courant
			this.setPheromoneCourant(pheromoneRecu);
			// Debloque le wait du thread en cours
			notify(); 
			
		} else{
			System.out.println("WARNING: Update Chef, Message pheromone null.");
		}
		
	}
	
	@Override
	public boolean isAffichable() {
		return this.getDureeVie() > 0;
	}

	@Override
	public int getPointDeForce() {
		return this.getPointDeForce();
	}

}
