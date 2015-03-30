package fourmis;
import interfaces.*;

import java.util.ArrayList;
import java.util.Random;

import environnements.Terrain;

/**
 * 
 * @author pev
 * @category cours java
 * @version 20150328
 *
 */
public class FourmiReine extends Fourmi implements Runnable {
	
	/* ===========================ATRB================================ */

	private static enum EnumPheromone {NOURRITURE, ATTAQUER, VIVRE, MOURIR}; //attribut observable
	private EnumPheromone typePheromone;
	private ArrayList<Oeuf> oeufAEclore = new ArrayList<>();
	private Pheromone unPheromone;
	
	/* ===========================CONST================================ */
	
	/**
	 * Constructeur de la fourmi reine
	 * @param idFourmi : identifiant d'une fourmi
	 * @param typeFourmi : couleur de la fourmi
	 * @param fkTerrain : association au terrain
	 * @param combattant : si la fourmi est une combattante
	 * @param dureeVie : duree de vie en seconde d'une fourmi
	 * @param pointDeVie : point de vie
	 * @param posX : position sur le terrain, ligne
	 * @param posY : position sur le terrain, colonne
	 * @param qtNourritureTransportee : qt de nourriture transportee
	 * @param qtNourritureTransportable : qt de nourriture transportable
	 */
	public FourmiReine(int idFourmi, EnumFourmi typeFourmi, Terrain fkTerrain,
			boolean combattant, int dureeVie, int pointDeVie, int posX, int posY,
			int qtNourritureTransportee, int qtNourritureTransportable) {
		super(idFourmi, typeFourmi, fkTerrain, combattant, dureeVie, pointDeVie, 
				posX, posY, qtNourritureTransportee, qtNourritureTransportable);
	}
	
	/**
	 * Constructeur d'une fourmi reine type aleatoire
	 * @param idFourmi : identifiant d'une fourmi
	 * @param fkTerrain : association au terrain
	 * @param combattant : si la fourmi est une combattante
	 * @param dureeVie : duree de vie en seconde d'une fourmi
	 * @param pointDeVie : point de vie
	 * @param posX : position sur le terrain, ligne
	 * @param posY : position sur le terrain, colonne
	 * @param qtNourritureTransportee : qt de nourriture transportee
	 * @param qtNourritureTransportable : qt de nourriture transportable
	 */
	public FourmiReine(int idFourmi, Terrain fkTerrain,
			boolean combattant, int dureeVie, int pointDeVie, int posX, int posY,
			int qtNourritureTransportee, int qtNourritureTransportable) {
		super(idFourmi, setRandomEnumCellule(), fkTerrain, combattant, dureeVie, 
				pointDeVie, posX, posY, qtNourritureTransportee, qtNourritureTransportable);
	}
	
	/* ==========================G/S================================== */

	public EnumPheromone getTypePheromone() {
		return typePheromone;
	}

	public void setTypePheromone(EnumPheromone typePheromone) {
		this.typePheromone = typePheromone;
	}

	public ArrayList<Oeuf> getOeufAEclore() {
		return oeufAEclore;
	}

	public void setOeufAEclore(ArrayList<Oeuf> oeufsAEclore) {
		this.oeufAEclore = oeufsAEclore;
	}
	
	/* ===========================ACTIONS============================= */

	@Override
	public String toString() {
		return "FourmiReine [typePheromone=" + typePheromone + ", oeufAEclore="
				+ oeufAEclore + ", toString()=" + super.toString() + "]";
	}
	
	/**
	 * Methode permettant de generer aleatoirement un type de fourmi
	 * @return une couleur de fourmi aleatoire
	 */
	public static EnumFourmi setRandomEnumCellule(){
		
		Random r = new Random();
		EnumFourmi uneEnumFourmi = null;
		int nbElement = 0;
		int valeurRandom = 0;
		
		nbElement = EnumFourmi.values().length; //taille de l'enum
		valeurRandom = 0 + r.nextInt(nbElement-0); // random entre 0 et nbElement
		
		// value retourne un tableau
		uneEnumFourmi = EnumFourmi.values()[valeurRandom];
		
		return uneEnumFourmi;
	}
	
	/**
	 * Methode permettant de pondre des oeufs et creer les chefs et soldats
	 * @param nbOeufsAConstruire : nombre d'oeuf a construire
	 * @param probaChef : probabilite d'etre un chef parmi les oeufs a construire
	 */
	public void pondreDesOeufs(int nbOeufsAConstruire, double probaChef){

		// Calcul du nombre de fourmi chef
		int nbOeufsChef = (int)Math.round(nbOeufsAConstruire*probaChef);
		int nbOeufsSoldatParChef = (int)Math.round((double)(nbOeufsAConstruire-nbOeufsChef)/(double)nbOeufsChef);
		int nbOeufsConstruits = 0;
		
		// Pour le nombre d'oeuf a faire
		while(nbOeufsConstruits < nbOeufsAConstruire){
			
			// Creation des fourmi chef
			FourmiChef uneFourmiChef = new FourmiChef(nbOeufsConstruits, this.typeFourmi, 
					this.getFkTerrain(), false, 100, 50, this.getPosX(), this.getPosY(), 
					0, 0, this, 0);
			
			// Ajout des oeufs a eclore dans l'AL
			this.oeufAEclore.add(uneFourmiChef);
			
			// TODO : Ajout d'un observer vers la reine pour les chefs
			//this.unPheromone.addObserver(uneFourmiChef);
			
			nbOeufsConstruits++;
			
			int nbSoldatAConstruirePourCeChef = 0;
			
			// Creation des fourmis soldats
			while (nbSoldatAConstruirePourCeChef < nbOeufsSoldatParChef) {

				FourmiSoldat uneFourmiSoldat = new FourmiSoldat(nbOeufsConstruits, this.typeFourmi, 
						this.getFkTerrain(), false, 30, 10, this.getPosX(), this.getPosY(), 
						0, 0, uneFourmiChef);
				
				// Ajouter des oeufs a eclore dans l'AL
				this.oeufAEclore.add(uneFourmiSoldat);
				
				// TODO : Ajout d'un observer vers le chef pour les soldats
				//this.unPheromone.addObserver(uneFourmiChef);
				
				nbSoldatAConstruirePourCeChef++;
				nbOeufsConstruits++;
				
			}
			
		}
		
	}
	
	/**
	 * Methode permettant d'appeller la fonction d'eclosion de la classe Oeufs.
	 * La methode de la classe oeufs permet de lancer un thread par oeufs.
	 */
	public void eclosion(){
		for (int i = 0; i < this.oeufAEclore.size(); i++) {
			oeufAEclore.get(i).eclosion();
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void run(){
		// TODO : Euh...
	}
	

}
