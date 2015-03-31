package fourmis;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import environnements.Terrain;
import fourmis.FourmiReine.EnumPheromone;

/**
 * 
 * @author pev, fred
 * @category cours java
 * @version 20150331
 *
 */
public class FourmiChef extends Fourmi implements Runnable, Observer{
	
	/* ===========================ATRB================================ */

	private int DIST_MAX_REINE = 4; //nb de cellule autours de la reine apres naissance
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
	 * @param pointDeVie : point de vie
	 * @param posX : position sur le terrain, ligne
	 * @param posY : position sur le terrain, colonne
	 * @param qtNourritureTransportee : qt nourriture transportee
	 * @param qtNourritureTransportable : qt nourriture transportable
	 * @param fkFourmiReine : reference a la fourmi reine
	 * @param qtNourritureRecoltee : compte le nombre de nourriture recoltee
	 * @param nbSoldatVivant : nombre de soldat vivant

	 */
	public FourmiChef(int idFourmi, EnumFourmi typeFourmi, Terrain fkTerrain,
			boolean combattant, int dureeVie, int pointDeVie, int posX, int posY,
			int qtNourritureTransportee, int qtNourritureTransportable, FourmiReine fkFourmiReine, 
			int qtNourritureRecoltee, int nbSoldatVivant) {
		super(idFourmi, typeFourmi, fkTerrain, combattant, dureeVie, pointDeVie, 
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
	 * Fonction qui met � jour le nombre de soldat vivant
	 * @param val
	 */
	public void updateNbSoldat(int val){
		// D�cr�mente le nombre 
		this.nbSoldatVivant -= val;
	}

	/**
	 * Methode permettant de crer un thread par fourmi chef
	 * et de leur donner une vie independante
	 */
	@Override
	public void eclosion() {
		System.out.println("Eclosion Chef" + this.getIdFourmi());
		Thread nouveauThread = new Thread(this);
		nouveauThread.start();
		//appel la fonction run
		
	}
	
	/**
	 * Methode permettant de retourner a cote de la fourmiliere
	 */
	public void retournerFourmiliere(){
		// TODO : determiner l'endroit ou on se situe et retourne a la fourmiliere
		
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
	 * Methode permettant a une fourmi d'arreter son existance
	 */
	public void pheromoneMourir(){
		// TODO : mettre fin au thread de la fourmi Soldat
		// TODO : informer la fourmi chef qu'on est mort
	}

	/**
	 * Methode permettant a ue fourmi soldat de vivre son existance
	 */
	@Override
	public synchronized void run() {
		// TODO : verifier si ca fonctionne autours de la fourmiliere

		while(true){
			
			switch(this.pheromoneCourant){
				case RIEN:
					// TODO : ne rien faire sur place
					break;
				case VIVRE:
				case NOURRITURE:
				case ATTAQUER:
					// TODO : si pas a cote de la fourmiliere, revenir sur position Reine
					this.pheromoneVivre();
					break;
				case MOURIR:
					// TODO : fourmi doit mourir, quitter le thread ?
					this.pheromoneMourir();
					break;
				default:
					System.out.println("WARNING: Update Chef, Message pheromone inconnu.");
					this.setPheromoneCourant(EnumPheromone.RIEN);
					break;
			
			}
			
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
				
		// Cast de l'objet recu au bon format
		EnumPheromone pheromoneRecu = (EnumPheromone)arg;
		
		if(pheromoneRecu != null){
			
			// Met a jour le message courant
			this.setPheromoneCourant(pheromoneRecu);
			notify(); // Debloque le wait du thread en cours
			System.out.println("Chef:" + pheromoneRecu);
			
		} else{
			System.out.println("WARNING: Update Chef, Message pheromone null.");
		}
		
	}

}
