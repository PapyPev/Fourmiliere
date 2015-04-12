package fourmis;
import java.util.ArrayList;
import java.util.Random;

import outils.*;
import environnements.Terrain;

/**
 * 
 * @author pev, fred
 * @category cours java
 * @version 20150331
 *
 */
public class FourmiReine extends Fourmi implements Runnable {
	
	/* ===========================ATRB================================ */

	/*
	 * ATTENTION : si ajout d'un nouveau type, modifier les updates des
	 * fourmis chef et soldat
	 */
	public static enum EnumPheromone {RIEN, NOURRITURE, ATTAQUER, VIVRE, MOURIR}; //attribut observable
	private EnumPheromone typePheromone;
	private ArrayList<Oeuf> oeufAEclore = new ArrayList<>();
	private Pheromone unPheromone;
	
	private ArrayList<Affichable> mesFourmis = new ArrayList<>();
	private int VIE_SOLDAT = 50; //nb deplacements
	private double PROBA_VIE_SOLDAT = 0.1; // +/- x%
	
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
		this.unPheromone = new Pheromone();
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
		this.unPheromone = new Pheromone();
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
	
	public ArrayList<Affichable> getMesFourmis() {
		return mesFourmis;
	}
	public void setMesFouris(ArrayList<Affichable> mesFourmis) {
		this.mesFourmis = mesFourmis;
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
					this.getFkTerrain(), false, this.VIE_SOLDAT*2, 50, this.getPosX(), this.getPosY(), 
					0, 0, this, 0, nbOeufsSoldatParChef);
			
			// Ajout des oeufs a eclore dans l'AL
			this.oeufAEclore.add(uneFourmiChef);
			//this.mesFourmis.add(uneFourmiChef);
			
			// Ajout d'un observer vers la reine pour les chefs
			this.unPheromone.addObserver(uneFourmiChef);
			
			nbOeufsConstruits++;
			
			int nbSoldatAConstruirePourCeChef = 0;
			
			// Creation des fourmis soldats
			while (nbSoldatAConstruirePourCeChef < nbOeufsSoldatParChef) {
				
				// Vie aleatoire bornee d'un soldat
				int vieMin = (int)Math.round(this.VIE_SOLDAT-(this.VIE_SOLDAT*this.PROBA_VIE_SOLDAT));
				int vieMax = (int)Math.round(this.VIE_SOLDAT+(this.VIE_SOLDAT*this.PROBA_VIE_SOLDAT));

				Random r = new Random();
				int vieSoldat = vieMin + r.nextInt(vieMax-vieMin);

				FourmiSoldat uneFourmiSoldat = new FourmiSoldat(nbOeufsConstruits, this.typeFourmi, 
						this.getFkTerrain(), false, vieSoldat, 50, this.getPosX(), this.getPosY(), 
						0, 0, uneFourmiChef);
				
				// Ajouter des oeufs a eclore dans l'AL
				this.oeufAEclore.add(uneFourmiSoldat);
				this.mesFourmis.add(uneFourmiSoldat);
				
				// Ajout d'un observer vers le chef pour les soldats
				this.unPheromone.addObserver(uneFourmiSoldat);
				
				nbSoldatAConstruirePourCeChef++;
				nbOeufsConstruits++;
				
			}
			
		}
		
	}
	
	/**
	 * Methode permettant d'informer les fourmi Observer d'un message
	 * @param messagePheromone : message a envoyer aux fourmis
	 */
	public void informerParPheromone(EnumPheromone messagePheromone){
		this.unPheromone.setMessage(messagePheromone);
	}
	
	/**
	 * Methode permettant d'appeller la fonction d'eclosion de la classe Oeufs.
	 * La methode de la classe oeufs permet de lancer un thread par oeufs.
	 * @return 
	 */
	public synchronized void eclosion(){
		
		// Eclore les oeufs en creant un thread
		for (int i = 0; i < this.oeufAEclore.size(); i++) {
			oeufAEclore.get(i).eclosion();
		}
		
		// Informer les fourmis de bouger aleatoirement
		this.informerParPheromone(EnumPheromone.VIVRE);
	}
	
	/**
	 * 
	 */
	@Override
	public void run(){
				
		// Tant qu'elle a de la vie (FouriReine Vie = 100)
		while(this.getDureeVie() > 0){
			
			try {
				this.seDeplacer(this.getPosX(), this.getPosY());
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			if (this.getDureeVie() == 90) {
				this.informerParPheromone(EnumPheromone.RIEN); // deplacement random
			}
			
			if (this.getDureeVie() == 50) {
				this.informerParPheromone(EnumPheromone.NOURRITURE);
			}
			
			// Attendre 1sec pour chaque action de la reine
			try {
				System.out.println("QUEEN"+this.getIdFourmi()+":"+getDureeVie());
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		// Fin du thread
		System.out.println(this.getIdFourmi() + "R: Je suis mort");
		
	}
	

}
