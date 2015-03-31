package fourmis;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import environnements.Terrain;
import fourmis.FourmiReine.EnumPheromone;

/**
 * 
 * @author pev
 * @category cours java
 * @version 20150328
 *
 */
public class FourmiChef extends Fourmi implements Runnable, Observer{
	
	/* ===========================ATRB================================ */

	private int DIST_MAX_REINE = 4; //nb de cellule autours de la reine apres naissance
	private FourmiReine fkFourmiReine;
	private int qtNourritureRecoltee;
	
	/* ===========================CONST================================ */

	/**
	 * Constructeur d'une fourmi Chef
	 * @param idFourmi : identifiant de la fourmi
	 * @param typeFourmi : couleur de la fourmi
	 * @param fkTerrain : terrainde reference
	 * @param combattant : si la fourmi est combattante ou non
	 * @param dureeVie : duree de vie en seconde
	 * @param pointDeVie : point de vie
	 * @param posX : position sur le terrain, ligne
	 * @param posY : position sur le terrain, colonne
	 * @param qtNourritureTransportee : qt nourriture transportee
	 * @param qtNourritureTransportable : qt nourriture transportable
	 * @param fkFourmiReine : reference a la fourmi reine
	 * @param qtNourritureRecoltee : compte le nombre de nourriture recoltee
	 */
	public FourmiChef(int idFourmi, EnumFourmi typeFourmi, Terrain fkTerrain,
			boolean combattant, int dureeVie, int pointDeVie, int posX, int posY,
			int qtNourritureTransportee, int qtNourritureTransportable, FourmiReine fkFourmiReine, 
			int qtNourritureRecoltee) {
		super(idFourmi, typeFourmi, fkTerrain, combattant, dureeVie, pointDeVie, 
				posX, posY, qtNourritureTransportee, qtNourritureTransportable);
		this.fkFourmiReine = fkFourmiReine;
		this.qtNourritureRecoltee = qtNourritureRecoltee;
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
	
	/* ===========================ACTIONS============================= */
	
	/**
	 * 
	 */
	public void calculerQtNourritureRecoltee(){
		// TODO : soit par thread soit par ajout a chaque fois qu'il y a 
		// un retour en fourmiliere
	}
	
	@Override
	public String toString() {
		return "FourmiChef [fkFourmiReine=" + fkFourmiReine
				+ ", qtNourritureRecoltee=" + qtNourritureRecoltee
				+ ", toString()=" + super.toString() + "]";
	}

	/**
	 * 
	 */
	public void calculerNbSoldatsVivants(){
		// TODO : calculer le nombre de fourmi de la meme espece
	}
	
	/**
	 * 
	 */
	public void calculerNbSoldatsMorts(){
		// TODO : calculer soit par un increment lors des callback thread
		// soit par un thread d'ecoute
	}

	/**
	 * Methode permettant de crer un thread par fourmi chef
	 * et de leur donner une vie independante
	 */
	@Override
	public void eclosion() {
		
		Thread nouveauThread = new Thread(this);
		nouveauThread.start();
		//appel la fonction run
		
	}
	
	/**
	 * Methode permettant a une fourmi de vivre a une distance
	 * de la fourmiliere
	 */
	public void pheromoneVivre(){
		// mouvements aleatoire autours de la fourmiliere
		Random r1 = new Random();
		Random r2 = new Random();
		int deplacementX = r1.nextInt(1 + 1) -1;
		int deplacementY = r2.nextInt(1 + 1) -1;
		
		int nextDeplacementX = this.getPosX()+deplacementX;
		int nextDeplacementY = this.getPosY()+deplacementY;
		
		System.out.println("FS"+this.getIdFourmi()+": " + deplacementX +" "+ deplacementY);
		
		// test si on est encore sur le terrain ou a +/- de distance
		// de la fourmilliere (de la reine)
		if (nextDeplacementX < this.getFkTerrain().getNbLigne() 
				&& nextDeplacementX >= 0 
				&& (nextDeplacementX <= (this.getFkFourmiReine().getPosX()+this.DIST_MAX_REINE)
					|| nextDeplacementX <= (this.getFkFourmiReine().getPosX()-this.DIST_MAX_REINE))
				) {
			this.setPosX(nextDeplacementX);
			
			if (nextDeplacementY < this.getFkTerrain().getNbColonne() 
					&& nextDeplacementY >= 0 
					&& (nextDeplacementY <= (this.getFkFourmiReine().getPosY()+this.DIST_MAX_REINE)
						|| nextDeplacementY <= (this.getFkFourmiReine().getPosY()-this.DIST_MAX_REINE))
					) {
				this.setPosY(nextDeplacementY);
				
			}
		}
	}
	
	/**
	 * Methode permettant de retourner a cote de la fourmiliere
	 */
	public void retournerFourmiliere(){
		// TODO : determiner l'endroit ou on se situe et retourne a la fourmiliere
		
	}
	
	/**
	 * Methode permettant a une fourmi d'arreter son existance
	 */
	public void pheromoneMort(){
		// TODO : mettre fin au thread de la fourmi Soldat
		// TODO : informer la fourmi chef qu'on est mort
	}
	
	/**
	 * Methode permettant a une fourmi d'aller chercher de la nourriture
	 */
	public void pheromoneNourriture(){
		// TODO : parcourir la map et ramener la nourriture si plein
		// puis repartir a la recherche
	}
	
	/**
	 * Methode permettant a une fourmi si elle est soldat d'attaquer
	 */
	public void pheromoneAttaquer(){
		// TODO : si une fourmi est sur la cellule en cours, attaquer
	}

	/**
	 * Methode permettant a ue fourmi soldat de vivre son existance
	 */
	@Override
	public synchronized void run() {
		// TODO : verifier si ca fonctionne autours de la fourmiliere
		
		int i = 0;
		int maxMouvements = 4;

		while(i < maxMouvements){
			
			this.pheromoneVivre();
			
			i++;
						
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Tester les differents messages recu
		
		if(arg != null){
			// Cast de l'objet recu au bon format
			EnumPheromone pheromoneRecu = (EnumPheromone)arg;
		
			switch (pheromoneRecu){
				case VIVRE:
					// TODO : tourner autours de la fourmiliere
					System.out.println("VIVRE");
					break;
				case MOURIR:
					// TODO : fourmi doit mourir, quitter le thread ?
					System.out.println("MOURIR");
					break;
				case ATTAQUER:
					// TODO : une fourmi chef ne doit pas attaquer
					System.out.println("ATTAQUER");
					break;
				case NOURRITURE:
					// TODO : une fourmi chef ne doit pas chercher de nourriture
					System.out.println("NOURRITURE");
					break;
				default:
					System.out.println("WARNING: Update Chef, Message pheromone inconnu.");
					break;
			}
			
		} else {
			System.out.println("WARNING: Update Chef, Message pheromone est nul.");
		}
		
	}

}
