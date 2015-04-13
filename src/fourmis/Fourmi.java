package fourmis;
import outils.Oeuf;
import environnements.Terrain;

/**
 * 
 * @author pev
 * @version 20150328
 *
 */
public abstract class Fourmi implements Oeuf{
	
	/* ===========================ATRB================================ */

	
	private int idFourmi;
	public static enum EnumFourmi {BLEU, ROUGE, JAUNE, VERT};
	public EnumFourmi typeFourmi;
	private Terrain fkTerrain;
	private boolean combattant;
	private int dureeVie;
	private int pointDeVie;
	private int posX;
	private int posY;
	private int qtNourritureTransportee;
	private int qtNourritureTransportable;
	
	/**
	 * Constructeur par defaut d'une fourmi
	 * @param idFourmi : identifiant d'une fourmi
	 * @param typeFourmi : couleur de la fourmi
	 * @param fkTerrain : association au terrain
	 * @param combattant : si la fourmi est une combattante
	 * @param dureeVie : duree de vie en seconde d'une fourmi
	 * @param pointDeForce : point de vie d'une fourmi
	 * @param posX : position sur le terrain, ligne
	 * @param posY : position sur le terrain, colonne
	 * @param qtNourritureTransportee : qt de nourriture transportee
	 * @param qtNourritureTransportable : qt de nourriture transportable
	 */
	public Fourmi(int idFourmi, EnumFourmi typeFourmi, Terrain fkTerrain,
			boolean combattant, int dureeVie, int pointDeForce, int posX, int posY,
			int qtNourritureTransportee, int qtNourritureTransportable) {
		this.idFourmi = idFourmi;
		this.typeFourmi = typeFourmi;
		this.fkTerrain = fkTerrain;
		this.combattant = combattant;
		this.dureeVie = dureeVie;
		this.pointDeVie = pointDeForce;
		this.posX = posX;
		this.posY = posY;
		this.qtNourritureTransportee = qtNourritureTransportee;
		this.qtNourritureTransportable = qtNourritureTransportable;
	}
	
	/* ==========================G/S================================== */
	
	public int getIdFourmi() {
		return idFourmi;
	}
	public void setIdFourmi(int idFourmi) {
		this.idFourmi = idFourmi;
	}
	
	public EnumFourmi getTypeFourmi() {
		return typeFourmi;
	}
	public void setTypeFourmi(EnumFourmi typeFourmi) {
		this.typeFourmi = typeFourmi;
	}
	
	public Terrain getFkTerrain() {
		return fkTerrain;
	}
	public void setFkTerrain(Terrain fkTerrain) {
		this.fkTerrain = fkTerrain;
	}
	
	public boolean isCombattant() {
		return combattant;
	}
	public void setCombattant(boolean combattant) {
		this.combattant = combattant;
	}
	
	public int getDureeVie() {
		return dureeVie;
	}
	public void setDureeVie(int pointDeVie) {
		this.pointDeVie = pointDeVie;
	}
	
	public int getPointDeVie() {
		return pointDeVie;
	}
	public void setPointDeVie(int pointDeVie) {
		this.pointDeVie = pointDeVie;
	}
	
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public int getQtNourritureTransportee() {
		return qtNourritureTransportee;
	}
	public void setQtNourritureTransportee(int qtNourritureTransportee) {
		this.qtNourritureTransportee = qtNourritureTransportee;
	}
	
	public int getQtNourritureTransportable() {
		return qtNourritureTransportable;
	}
	public void setQtNourritureTransportable(int qtNourritureTransportable) {
		this.qtNourritureTransportable = qtNourritureTransportable;
	}
	
	/* ===========================ACTIONS============================== */
	
	@Override
	public String toString() {
		return "Fourmi [idFourmi=" + idFourmi + ", typeFourmi=" + typeFourmi
				+ ", fkTerrain=" + fkTerrain + ", combattant=" + combattant
				+ ", dureeVie=" + dureeVie + ", pointDeVie=" + pointDeVie 
				+ ", posX=" + posX + ", posY=" + posY + ", qtNourritureTransportee=" 
				+ qtNourritureTransportee + "qtNourritureTransportable=" 
				+ qtNourritureTransportable+"]";
	}
	
	/**
	 * Methode permettant de se deplacer aux coordonnees x, y
	 * @param x : coordonnees en ligne
	 * @param y : coordonnees en colonne
	 * @throws InterruptedException : gestion des erreurs
	 */
	public synchronized void seDeplacer(int x, int y) throws InterruptedException{
		this.posX = x;
		this.posY = y;
		this.dureeVie--;
		wait(500);
	}

}
