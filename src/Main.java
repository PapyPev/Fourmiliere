import java.util.ArrayList;
import java.util.Random;

import environnements.Terrain;
import fourmis.FourmiReine;
import fourmis.FourmiReine.EnumPheromone;
import static environnements.Cellule.EnumCellule;


public class Main {
	
	public static void main(String[] args) {
		
		ArrayList<FourmiReine> lesFourmilieres = new ArrayList<FourmiReine>();
		
		int lignes = 30;
		int colonnes = 30;
		int nbReine = 1;
		int nbOeufParReine = 5; //mini : 5
		double probaOeufChef = 0.1;
		
		// Initialisation du terrain
		Terrain monTerrain = new Terrain(1, lignes, colonnes);

		// Initialisation des Fourmilieres / Reines
		for (int i = 0; i < nbReine; i++) {
			
			Random r1 = new Random();
			Random r2 = new Random();
			int initX,initY = 0;
			initX = 0 + r1.nextInt(lignes-0);
			initY = 0 + r2.nextInt(colonnes-0);
						
			// Creation de la FourmiReine
			FourmiReine uneFourmiReine = new FourmiReine(i, monTerrain, false, 100, 100, initX, initY, 0, 0);
			lesFourmilieres.add(uneFourmiReine);
			
			// Implantation de la Fourmiliere
			monTerrain.getIndexTerrain().get(uneFourmiReine.getPosY()).get(uneFourmiReine.getPosX()).setTypeCellule(EnumCellule.FOURMILIERE);
			
			// Ponte des oeufs
			uneFourmiReine.pondreDesOeufs(nbOeufParReine, probaOeufChef);
			
		}
		
		// Eclosion des oeufs
		for (int i = 0; i < lesFourmilieres.size(); i++){
			lesFourmilieres.get(i).eclosion();
			
		}
		
		// Informer aux fourmis d'aller chercher de la nourriture
		for (int i = 0; i < lesFourmilieres.size(); i++){
			lesFourmilieres.get(i).informerParPheromone(EnumPheromone.NOURRITURE);
		}

		
		
/*		System.out.println("Attente avant depart : None");
		System.out.println("Exploration Start : None");
		System.out.println("Compter nombre de Nourriture : None");
		
		System.out.println("----------- Terrain -----------");
		
		for (int i = 0; i < monTerrain.getNbLigne(); i++) {
			for (int j = 0; j < monTerrain.getNbColonne(); j++) {
				
				boolean celluleVide = true;
				for (int k = 0; k < lesFourmilieres.size(); k++) {
					if (lesFourmilieres.get(k).getPosX() == i && lesFourmilieres.get(k).getPosY() == j) {
						System.out.print("["+lesFourmilieres.get(k).getIdFourmi()+"]");
						celluleVide = false;
					}
				}
				if (celluleVide) {
					System.out.print("[ ]");
				}
				
			}
			System.out.print("\n");
		}
		
		System.out.println("-------- Statistiques/R --------");
		
		int nbChef = (int)Math.round(nbOeufParReine*probaOeufChef);
		int nbSoldat = (int)Math.round((double)(nbOeufParReine-nbChef)/(double)nbChef);
		System.out.println("Nombre de Reine: " + nbReine);
		System.out.println("Nombre d'Oeufs: " + nbOeufParReine);
		System.out.println("Nombre de Chefs: " + nbChef);
		System.out.println("Nombre de Soldats: "+ nbSoldat);

		System.out.println("------------ Ending ------------");

*/
		
	}

}
