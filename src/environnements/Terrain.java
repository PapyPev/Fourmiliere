package environnements;

import java.util.ArrayList;
import java.util.Random;

import environnements.Cellule.EnumCellule;

/**
 * 
 * @author pev
 * @category cours java
 * @version 20150328
 *
 */
public class Terrain {
	
	/* ===========================ATRB================================ */
	
	private int idTerrain;
	private int nbLigne;
	private int nbColonne;
	private ArrayList<ArrayList<Cellule>> indexTerrain = new ArrayList<ArrayList<Cellule>>();
	
	/* ===========================CONST=============================== */

	/**
	 * Constructeur d'un terrain
	 * @param idTerrain
	 * @param nbLigne
	 * @param nbColonne
	 */
	public Terrain(int idTerrain, int nbLigne, int nbColonne){
		this.idTerrain = idTerrain;
		this.nbLigne = nbLigne;
		this.nbColonne = nbColonne;
		this.indexTerrain = initialiserTerrainRandom(nbLigne, nbColonne);
	}
	
	/**
	 * Constructeur d'un terrain
	 * @param idTerrain : identifiant du terrain
	 * @param nbLigne : nombre de cellule en ligne
	 * @param nbColonne : nombre de celule en colonne
	 * @param typeCellule : type de cellule du terrain
	 */
	public Terrain(int idTerrain, int nbLigne, int nbColonne, EnumCellule typeCellule){
		this.idTerrain = idTerrain;
		this.nbLigne = nbLigne;
		this.nbColonne = nbColonne;
		this.indexTerrain = initialiserTerrain(nbLigne, nbColonne, typeCellule);
	}
	
	/* ==========================G/S================================== */
	
	public int getIdTerrain() {
		return idTerrain;
	}
	public void setIdTerrain(int idTerrain) {
		this.idTerrain = idTerrain;
	}
	
	public int getNbLigne() {
		return nbLigne;
	}
	public void setNbLigne(int nbLigne) {
		this.nbLigne = nbLigne;
	}
	
	public int getNbColonne() {
		return nbColonne;
	}
	public void setNbColonne(int nbColonne) {
		this.nbColonne = nbColonne;
	}
	
	public ArrayList<ArrayList<Cellule>> getIndexTerrain() {
		return indexTerrain;
	}
	public void setIndexTerrain(ArrayList<ArrayList<Cellule>> indexTerrain) {
		this.indexTerrain = indexTerrain;
	}
	
	/* ===========================ACTIONS============================= */

	/**
	 * Methode permettant de generer un terrain avec des cellules typees
	 * @param nbLigne : nombre de cellule en ligne
	 * @param nbColonne : nombre de cellule en colonne
	 * @param typeCellule : type du terrain
	 * @return
	 */
	public ArrayList<ArrayList<Cellule>> initialiserTerrain(int nbLigne, int nbColonne, EnumCellule typeCellule){
		//TODO : Init terrain : faire un init sur une cellule
		
		// Configuration du Terrain a retourner
		ArrayList<ArrayList<Cellule>> indexTerrain = new ArrayList<ArrayList<Cellule>>();
		
		int rand;
		EnumCellule nextCell;
		
		// Ligne
		for (int j = 0; j < nbColonne; j++) {
			
			// Colonne a l'interieur
			ArrayList<Cellule> tmpLigne = new ArrayList<>();
			
			for (int i = 0; i < nbLigne; i++) {
				// avec juste un id, cree un terrain avec des cellule types
				
				Random r1 = new Random();
				rand = 0 + r1.nextInt(3-0);
				if (rand == 0) {
					nextCell = typeCellule;
				} else {
					nextCell = EnumCellule.NULLE;
				}
				Cellule nouvelleCellule = new Cellule(i, nextCell);
				tmpLigne.add(nouvelleCellule);
			}
			
			indexTerrain.add(tmpLigne);
			
		}
		
		return indexTerrain;
	}
	
	/**
	 * Methode permettant de generer un terrain avec des cellules aleatoires
	 * @param nbLigne nombre de ligne du terrain
	 * @param nbColonne nombre de colonne du terrain
	 * @return un indexTerrain
	 */
	public ArrayList<ArrayList<Cellule>> initialiserTerrainRandom(int nbLigne, int nbColonne){
		//TODO : Init terrain : faire du random sur les cellules
		
		// Configuration du Terrain a retourner
		ArrayList<ArrayList<Cellule>> indexTerrain = new ArrayList<ArrayList<Cellule>>();
		
		// Ligne
		for (int j = 0; j < nbColonne; j++) {
			
			// Colonne a l'interieur
			ArrayList<Cellule> tmpLigne = new ArrayList<>();
			
			for (int i = 0; i < nbLigne; i++) {
				// avec juste un id, cree un terrain avec des cellule random
				Cellule nouvelleCellule = new Cellule(i);
				tmpLigne.add(nouvelleCellule);
			}
			
			indexTerrain.add(tmpLigne);
			
		}
		
		return indexTerrain;
	}
	
	/**
	 * Methode permettant d'appauvrir tout le terrain en retirant toute les quantite
	 * de nourriture
	 */
	public void appauvrirToutLeTerrain(){
		for (int i = 0; i < this.nbLigne; i++) {
			for (int j = 0; j < this.nbColonne; j++) {
				this.indexTerrain.get(j).get(i).setQtNourritureCourante(0);
			}
		}
	}
	
	/**
	 * Methode permettant d'appauvrir aleatoirement le terrain en retirant
	 * toute quantite de nourriture presente sur un nombre de cellule donnee
	 * @param nbCelluleAAppauvrir nombre de cellule a appauvrir
	 */
	public void appauvrirAleatoirementLeTerrain(int  nbCelluleAAppauvrir){
		Random r = new Random();
		int qtCelluleTest = 0;
		int i = 0;
		int j = 0;
		while (qtCelluleTest < nbCelluleAAppauvrir) {
			i = 0 + r.nextInt(this.nbLigne-0); // random entre 0 et nbLigne
			j = 0 + r.nextInt(this.nbColonne-0); // random entre 0 et nbColonne
			this.indexTerrain.get(j).get(i).setQtNourritureCourante(0);
			qtCelluleTest++;
		}
	}
	
	/**
	 * Methode permettant de retirer toute quantite de pheromone sur le terrain
	 */
	public void ventilerToutLeTerrain(){
		for (int i = 0; i < this.nbLigne; i++) {
			for (int j = 0; j < this.nbColonne; j++) {
				if (this.indexTerrain.get(j).get(i).isPresencePheromone()) {
					this.indexTerrain.get(j).get(i).setPresencePheromone(false);
				}
			}
		}
	}
	
	/**
	 * Methode permettant de retirer aleatoirement sur un nombre de cellule donnee
	 * toute quantite de pheromone
	 * @param nbCelluleAVentiler nombre de cellule a ventiler
	 */
	public void ventilerAleatoirementTerrain(int nbCelluleAVentiler){
		Random r = new Random();
		int qtCelluleTest = 0;
		int i = 0;
		int j = 0;
		while (qtCelluleTest < nbCelluleAVentiler) {
			i = 0 + r.nextInt(this.nbLigne-0); // random entre 0 et nbLigne
			j = 0 + r.nextInt(this.nbColonne-0); // random entre 0 et nbColonne
			this.indexTerrain.get(j).get(i).setPresencePheromone(false);
			qtCelluleTest++;
		}
	}
	
	/**
	 * Methode retournant une cellule en position x|y
	 * @param x : position en ligne
	 * @param y : position en colonne
	 * @return une cellule
	 */
	public Cellule getACellule(int x, int y){
		return this.indexTerrain.get(y).get(x);
	}

	@Override
	public String toString() {
		String index = "";
		
		for (int i = 0; i < this.nbLigne; i++) {
			for (int j = 0; j < this.nbColonne; j++) {
				index = index + "["+ i + "|" + j +"]";
			}
			index = index + "\n";
		}
		
		return "Terrain [idTerrain=" + idTerrain + "]" + "\n" + index;
	}
	
}
