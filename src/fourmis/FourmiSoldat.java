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
public class FourmiSoldat extends Fourmi implements Runnable, Observer{
	
	/* ===========================ATRB================================ */
	
	private int DIST_MAX_REINE = 4; //nb de cellule autours de la reine apres naissance
	private FourmiChef fkFourmiChef;
	private EnumPheromone pheromoneCourant;
	
	/* ===========================CONST=============================== */

	/**
	 * Constructeur d'une fourmi soldat
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
	 * @param fkFourmiChef : reference a la fourmi chef
	 */
	public FourmiSoldat(int idFourmi, EnumFourmi typeFourmi, Terrain fkTerrain,
			boolean combattant, int dureeVie, int pointDeVie, int posX, int posY,
			int qtNourritureTransportee, int qtNourritureTransportable, FourmiChef fkFourmiChef) {
		super(idFourmi, typeFourmi, fkTerrain, combattant, dureeVie, pointDeVie, 
				posX, posY, qtNourritureTransportee, qtNourritureTransportable);
		this.fkFourmiChef = fkFourmiChef;
		this.pheromoneCourant = EnumPheromone.RIEN;
	}
	
	/* ==========================G/S================================== */

	public FourmiChef getFkFourmiChef() {
		return fkFourmiChef;
	}
	public void setFkFourmiChef(FourmiChef fkFourmiChef) {
		this.fkFourmiChef = fkFourmiChef;
	}
	
	public EnumPheromone getPheromoneCourant() {
		return pheromoneCourant;
	}
	public void setPheromonecourant(EnumPheromone pheromoneCourant) {
		this.pheromoneCourant = pheromoneCourant;
	}

	/* ===========================ACTIONS============================= */
	
	@Override
	public String toString() {
		return "FourmiSoldat [fkFourmiChef=" + fkFourmiChef + ", toString()="
				+ super.toString() + "]";
	}
	
	/**
	 * Methode permettant de recuperer la quantite de nourriture presente
	 * sur la cellule en cours
	 */
	public void prendreNourriture(){
		// TODO : recuperer la nourriture sur la cellule en cours
		// incrementer la qt transportee et reduire la qt de cellule
		
		// Recupere la qt de nourriture sur la cellule
		int qtCell = this.getFkTerrain().getACellule(this.getPosX(), this.getPosY()).getQtNourritureCourante();
		
		// S'il y a de la nourriture
		if(qtCell > 0){
			
			// Si on peut porter de la nourriture
			if (this.getQtNourritureTransportee() < this.getQtNourritureTransportable()){
				
				int qtNourriturePrenableParFourmi = this.getQtNourritureTransportable() - this.getQtNourritureTransportee();
				
				// si on peut transporter plus que ce qu'il y a sur la cellule
				if (qtNourriturePrenableParFourmi >= qtCell) {
					
					// on prend tout ce qu'il y a sur la cellule
					this.getFkTerrain().getACellule(this.getPosX(), this.getPosY()).setQtNourritureCourante(0);
					
					// on l'ajoute a la fourmi
					this.setQtNourritureTransportee(qtCell);
					
				} else {
					
					int qtRestanteCellule = qtCell - this.getQtNourritureTransportable();
					
					// on decremente la quantite sur la cellule
					this.getFkTerrain().getACellule(this.getPosX(), this.getPosY()).setQtNourritureCourante(qtRestanteCellule);
					
					// on ajoute des pheromones sur cette cellule
					this.getFkTerrain().getACellule(this.getPosX(), this.getPosY()).setPresencePheromone(true);
					
					// on ajoute la quantite maximale que peut porter la fourmi
					this.setQtNourritureTransportee(this.getQtNourritureTransportable());
					
				}
				
			}

		}
		
	}

	/**
	 * Methode permettant de revenir a la fourmiliere et de deposer
	 * la quantite de nourriture transportee tout en avertissant le chef
	 */
	public void deposerNourriture(){
		// TODO : une fois a la fourmiliere, incrementer qt chef
	}
	
	/**
	 * Methode permettant de crer un thread par fourmi soldat
	 * et de leur donner une vie independante
	 */
	@Override
	public void eclosion() {
		Thread nouveauThread = new Thread(this);
		nouveauThread.start(); //appel la fonction run()
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
				&& (nextDeplacementX <= (this.getFkFourmiChef().getFkFourmiReine().getPosX()+this.DIST_MAX_REINE)
					|| nextDeplacementX <= (this.getFkFourmiChef().getFkFourmiReine().getPosX()-this.DIST_MAX_REINE))
				) {
			this.setPosX(nextDeplacementX);
			
			if (nextDeplacementY < this.getFkTerrain().getNbColonne() 
					&& nextDeplacementY >= 0 
					&& (nextDeplacementY <= (this.getFkFourmiChef().getFkFourmiReine().getPosY()+this.DIST_MAX_REINE)
						|| nextDeplacementY <= (this.getFkFourmiChef().getFkFourmiReine().getPosY()-this.DIST_MAX_REINE))
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
	public void pheromoneMourir(){
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
		int maxMouvements = 10;

		while(i < maxMouvements){
			
			switch(this.pheromoneCourant){
				case RIEN:
					// TODO : une fourmi chef ne doit pas chercher de nourriture
					System.out.println("Chef : RIEN");
					break;
				case VIVRE:
					// TODO : tourner autours de la fourmiliere
					System.out.println("Chef : VIVRE");
					this.pheromoneVivre();
					break;
				case MOURIR:
					// TODO : fourmi doit mourir, quitter le thread ?
					System.out.println("Chef : MOURIR");
					this.pheromoneMourir();
					break;
				case ATTAQUER:
					// TODO : une fourmi chef ne doit pas attaquer
					System.out.println("Chef : ATTAQUER");
					this.pheromoneAttaquer();
					break;
				case NOURRITURE:
					// TODO : une fourmi chef ne doit pas chercher de nourriture
					System.out.println("Chef : NOURRITURE");
					this.pheromoneNourriture();
					break;
				default:
					System.out.println("WARNING: Update Chef, Message pheromone inconnu.");
					break;
			
			}
			
			i++;
						
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Tester les differents messages recu
		
		// Cast de l'objet recu au bon format
		EnumPheromone pheromoneRecu = (EnumPheromone)arg;
		
		if(pheromoneRecu != null){
			
			// Met a jour le message courant
			setPheromonecourant(pheromoneRecu);
			
		} else{
			System.out.println("WARNING: Update Soldat, Message pheromone null.");
		}
		
	}
	
}
