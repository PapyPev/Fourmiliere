package environnements;

import java.util.Random;

/**
 * 
 * @author pev
 * @category cours java
 * @version 20150328
 *
 */
public class Cellule {
	
	/* ===========================ATRB================================ */

	private int idCellule;
	/*
	 * Attention : si ajout d'un typeCellule, modifier les fonctions : 
	 * setQtNourritureSelontypeCell & setDureeRechargeSelonTypeCell
	 */
	public static enum EnumCellule {ARBRE, BOUE, PLANTE, TERRIER, FOURMILIERE, NULLE};
	private EnumCellule typeCellule;
	private int qtNourritureNative;
	private int qtNourritureCourante;
	private int dureeRecharge; // en seconde
	private boolean presencePheromone;
	private boolean presenceCombattant;
	
	/* ===========================CONST================================ */
	
	/**
	 * Constructeur d'une cellue specifique
	 * @param idCellule
	 * @param typeCellule
	 * @param qtNourritureNative
	 * @param qtNourritureCourante
	 * @param dureeRecharge
	 * @param presencePheromone
	 * @param presenceCombattant
	 */
	public Cellule(int idCellule, EnumCellule typeCellule,
			int qtNourritureNative, int qtNourritureCourante,
			int dureeRecharge, boolean presencePheromone,
			boolean presenceCombattant) {
		super();
		this.idCellule = idCellule;
		this.typeCellule = typeCellule;
		this.qtNourritureNative = qtNourritureNative;
		this.qtNourritureCourante = qtNourritureCourante;
		this.dureeRecharge = dureeRecharge;
		this.presencePheromone = presencePheromone;
		this.presenceCombattant = presenceCombattant;
	}
	
	/**
	 * Constructeur d'une cellule aleatoire
	 * @param idCellule : identifiant de la cellule
	 */
	public Cellule(int idCellule) {
		this.idCellule = idCellule;
		this.typeCellule = setRandomEnumCellule();
		this.setQtNourritureSelontypeCell(this.typeCellule);
		this.qtNourritureCourante = qtNourritureNative;
		this.setDureeRechargeSelonTypeCell(this.typeCellule);
		this.presencePheromone = false;
		this.presenceCombattant = false;
	}
	
	/* ==========================G/S=================================== */

	public int getIdCellule() {
		return idCellule;
	}
	public void setIdCellule(int idCellule) {
		this.idCellule = idCellule;
	}
	
	public EnumCellule getTypeCellule() {
		return typeCellule;
	}
	public void setTypeCellule(EnumCellule typeCellule) {
		this.typeCellule = typeCellule;
	}
	
	public int getQtNourritureNative() {
		return qtNourritureNative;
	}
	public void setQtNourritureNative(int qtNourritureNative) {
		this.qtNourritureNative = qtNourritureNative;
	}
	
	public synchronized int getQtNourritureCourante() {
		return qtNourritureCourante;
	}
	public synchronized void setQtNourritureCourante(int qtNourritureCourante) {
		this.qtNourritureCourante = qtNourritureCourante;
	}
	
	public int getDureeRecharge() {
		return dureeRecharge;
	}
	public void setDureeRecharge(int dureeRecharge) {
		this.dureeRecharge = dureeRecharge;
	}
	
	public boolean isPresencePheromone() {
		return presencePheromone;
	}
	public void setPresencePheromone(boolean presencePheromone) {
		this.presencePheromone = presencePheromone;
	}
	
	public boolean isPresenceCombattant() {
		return presenceCombattant;
	}
	public void setPresenceCombattant(boolean presenceCombattant) {
		this.presenceCombattant = presenceCombattant;
	}
	
	/* ===========================ACTIONS============================= */
	
	@Override
	public String toString() {
		return "Cellule [idCellule=" + idCellule + ", typeCellule="
				+ typeCellule + ", qtNourritureNative=" + qtNourritureNative
				+ ", qtNourritureCourante=" + qtNourritureCourante
				+ ", dureeRecharge=" + dureeRecharge + ", presencePheromone="
				+ presencePheromone + ", presenceCombattant="
				+ presenceCombattant + "]";
	}
	
	/**
	 * Mathode permettant de recuperer un type aleatoire de Cellule
	 * @return un type de cellule aleatoire
	 */
	public EnumCellule setRandomEnumCellule(){
		
		Random r = new Random();
		EnumCellule uneEnumCellule = null;
		int nbElement = 0;
		int valeurRandom = 0;
		
		nbElement = EnumCellule.values().length; //taille de l'enum
		valeurRandom = 0 + r.nextInt(nbElement-0); // random entre 0 et nbElement
		
		// value retourne un tableau
		uneEnumCellule = EnumCellule.values()[valeurRandom];
		
		return uneEnumCellule;
	}

	/**
	 * Methode permettant de générer la quantité de nourriture native
	 * selon le type de cellule renseigné
	 * @param typeDeLaCellule une cellule specifique
	 * @return une quantite de nourriture native
	 */
	public void setQtNourritureSelontypeCell(EnumCellule typeDeLaCellule){
				
		switch (typeDeLaCellule){
			case ARBRE:
				this.qtNourritureNative = 50;
				break;
			case BOUE:
				this.qtNourritureNative = 5;
				break;
			case PLANTE:
				this.qtNourritureNative = 15;
				break;
			case TERRIER:
				this.qtNourritureNative = 20;
				break;
			case FOURMILIERE:
				this.qtNourritureNative = 0;
				break;
			case NULLE:
				this.qtNourritureNative = 0;
				break;
		}
		
	}
	
	/**
	 * Methode permettant d'obtenir la duree (en seconde) 
	 * de recharge en nourriture en fonction du type de cellule
	 * @param typeDeLaCellule une cellule specifique
	 * @return une duree de recharge
	 */
	public void setDureeRechargeSelonTypeCell(EnumCellule typeDeLaCellule){
				
		switch (typeDeLaCellule){
			case ARBRE:
				this.dureeRecharge = 60;
				break;
			case BOUE:
				this.dureeRecharge = 15;
				break;
			case PLANTE:
				this.dureeRecharge = 25;
				break;
			case TERRIER:
				this.dureeRecharge = 5;
				break;
			case FOURMILIERE:
				this.dureeRecharge = 0;
				break;
			case NULLE:
				this.dureeRecharge = 0;
				break;
		}
		
	}

}
