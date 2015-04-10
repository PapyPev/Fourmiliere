import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import environnements.Terrain;
import environnements.Cellule.EnumCellule;
import fourmis.FourmiReine;
import fourmis.FourmiReine.EnumPheromone;


public class Main extends Applet implements Runnable {

	/**
	 * Serial ID et attributs
	 */
	private static final long serialVersionUID = 1L;
	
	Terrain monTerrain;
	
	// Parametres de lancement
	ArrayList<FourmiReine> lesFourmilieres = new ArrayList<FourmiReine>();
	int lignes = 50;
	int colonnes = 50;
	int nbReine = 3;
	int nbOeufParReine = 30; //mini : 5
	double probaOeufChef = 0.1;
    
	// Definition des images
	private int tailleImage = 10; //px
	
	// Definition des Sols
	private Image   imgSolNulle;
	private Image   imgSolArbre;
	private Image   imgSolBoue;
	private Image   imgSolPlante;
	private Image   imgSolTerrier;
	private Image   imgSolFourmiliere;
		
	// Definition des Fourmis
    private Image   imgNoirSoldat;
	private Image   imgNoirChef;
	private Image   imgNoirReine;
	private Image   imgJauneSoldat;
	private Image   imgJauneChef;
	private Image   imgJauneReine;
	private Image   imgBleuSoldat;
	private Image   imgBleuChef;
	private Image   imgBleuReine;
	
	
    private Thread  thread;   //thread de controle
    
    @Override
	public void start() {
		//necessaire pour demarrer l'animation
		if (thread==null){
			thread=new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		// Actualisation de l'interface graphique
		repaint();
	}
	
	/**
	 * Initialisations, appelee automatiquement
	 */
	@Override
	public void init() {
		
		// Definition de la fenetre d'affichage
		this.setSize(lignes*tailleImage, colonnes*tailleImage);
		
		// Chargement des types de terrain
		imgSolNulle=getImage(getCodeBase(),"./img2/solNulle.png");
		imgSolArbre=getImage(getCodeBase(),"./img2/solArbre.png");
		imgSolBoue=getImage(getCodeBase(),"./img2/solBoue.png");
		imgSolFourmiliere=getImage(getCodeBase(),"./img2/solFourmiliere.png");
		imgSolPlante=getImage(getCodeBase(),"./img2/solPlante.png");
		imgSolTerrier=getImage(getCodeBase(),"./img2/solTerrier.png");
	 	
		// Chargement des types d'images fourmi Jaune
		imgJauneSoldat=getImage(getCodeBase(),"./img2/jaune/soldat.png");
		imgJauneChef=getImage(getCodeBase(),"./img2/jaune/chef.png");
		imgJauneReine=getImage(getCodeBase(),"./img2/jaune/reine.png");

		// Chargement des types d'images fourmi bleu
		imgBleuSoldat=getImage(getCodeBase(),"./img2/bleu/soldat.png");
		imgBleuChef=getImage(getCodeBase(),"./img2/bleu/chef.png");
		imgBleuReine=getImage(getCodeBase(),"./img2/bleu/reine.png");

		// Chargement des types d'images fourmi noir
		imgNoirSoldat=getImage(getCodeBase(),"./img2/noir/soldat.png");
		imgNoirChef=getImage(getCodeBase(),"./img2/noir/chef.png");
		imgNoirReine=getImage(getCodeBase(),"./img2/noir/reine.png");
		
		// Initialisation du terrain - Pas besoin de type si random
		monTerrain = new Terrain(1, lignes, colonnes, EnumCellule.PLANTE);
		//monTerrain = new Terrain(1, lignes, colonnes);
		

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
		
		//Eclosion des oeufs
		for (int i = 0; i < lesFourmilieres.size(); i++){
			lesFourmilieres.get(i).eclosion();
		}
		
		// Informer aux fourmis d'aller chercher de la nourriture
		for (int i = 0; i < lesFourmilieres.size(); i++){
			lesFourmilieres.get(i).informerParPheromone(EnumPheromone.NOURRITURE);
		}

		
		
	}
	
	/**
	 * Methode appellee pour redessiner l'interface, appelee par repaint()
	 */
	@Override
	public  void  paint(Graphics g) {
		
		while(true){
		
			// Affichage du terrain selon le type de cellule
			for (int i = 0; i < monTerrain.getNbLigne(); i++) {
				for (int j = 0; j < monTerrain.getNbColonne(); j++) {
					switch(monTerrain.getACellule(i, j).getTypeCellule()){
						case ARBRE:
							if(monTerrain.getACellule(i, j).getQtNourritureCourante() > 0){
								g.drawImage(imgSolArbre,i*tailleImage,j*tailleImage,this);
							} else{
								g.drawImage(imgSolArbre,i*tailleImage,j*tailleImage,this);
							}
							break;
						case BOUE:
							if(monTerrain.getACellule(i, j).getQtNourritureCourante() > 0){
								g.drawImage(imgSolBoue,i*tailleImage,j*tailleImage,this);
							} else{
								g.drawImage(imgSolBoue,i*tailleImage,j*tailleImage,this);
							}
							break;
						case PLANTE:
							if(monTerrain.getACellule(i, j).getQtNourritureCourante() > 0){
								g.drawImage(imgSolPlante,i*tailleImage,j*tailleImage,this);
							} else{
								g.drawImage(imgSolPlante,i*tailleImage,j*tailleImage,this);
							}
							break;
						case TERRIER:
							if(monTerrain.getACellule(i, j).getQtNourritureCourante() > 0){
								g.drawImage(imgSolTerrier,i*tailleImage,j*tailleImage,this);
							} else{
								g.drawImage(imgSolTerrier,i*tailleImage,j*tailleImage,this);
							}
							break;
						case FOURMILIERE:
							g.drawImage(imgSolFourmiliere,i*tailleImage,j*tailleImage,this);
							break;
						case NULLE:
							g.drawImage(imgSolNulle,i*tailleImage,j*tailleImage,this);
							break;
					}
				}
			}
			
			// Affichage des fourmis
			for (int i = 0; i < lesFourmilieres.size(); i++) {
				int posX, posY;
				
				// Affichage Reine
				posX = lesFourmilieres.get(i).getPosX();
				posY = lesFourmilieres.get(i).getPosY();
				switch(lesFourmilieres.get(i).getTypeFourmi()){
					case BLEU:
						g.drawImage(imgBleuReine,posX*tailleImage,posY*tailleImage,this);
						break;
					case JAUNE:
						g.drawImage(imgJauneReine,posX*tailleImage,posY*tailleImage,this);
						break;
					case NOIR:
						g.drawImage(imgNoirReine,posX*tailleImage,posY*tailleImage,this);
						break;
				}
				
				// Affichage des fourmis de la reine
				for (int j = 0; j < lesFourmilieres.get(i).getMesFourmis().size(); j++) {
					
					// Si elles ont encore leur point de vie
					if (lesFourmilieres.get(i).getMesFourmis().get(j).isAffichable()) {
						posX = lesFourmilieres.get(i).getMesFourmis().get(j).getPosX();
						posY = lesFourmilieres.get(i).getMesFourmis().get(j).getPosY();
						switch(lesFourmilieres.get(i).getMesFourmis().get(j).getFkFourmiReine().getTypeFourmi()){
						case BLEU:
							g.drawImage(imgBleuSoldat,posX*tailleImage,posY*tailleImage,this);
							break;
						case JAUNE:
							g.drawImage(imgJauneSoldat,posX*tailleImage,posY*tailleImage,this);
							break;
						case NOIR:
							g.drawImage(imgNoirSoldat,posX*tailleImage,posY*tailleImage,this);
							break;
					}
					}
					
				}
			} // end affichage fourmi
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} // end while

			
	}

}
