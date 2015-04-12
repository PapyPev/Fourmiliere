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

	/* ===========================ATRB================================ */
	
	private static final long serialVersionUID = 1L;
	
	// Terrain courant
	private Terrain monTerrain;
	private Image terrainCourant;
	
	// Parametres de lancement
	ArrayList<FourmiReine> lesFourmilieres = new ArrayList<FourmiReine>();
	int lignes = 30;
	int colonnes = 30;
	int nbReine = 3;
	int nbOeufParReine = 20; //mini : 5
	double probaOeufChef = 0.1;
    
	// Definition des images
	private int tailleImage = 20; //px
	
	// Definition des Terrains (visuel seulement)
	private Image   imgTerrainDesert;
	private Image   imgTerrainEau;
	private Image   imgTerrainJungle;
	
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
	
	// Definition des Autres
	private Image   imgTombe;
	private Image   imgTombeReine;
	
	// Thread de controle
    private Thread  thread;
    
    /* ===========================START============================= */
    
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
		
		// Chargement des types de terrain (visuel seulement)
		imgTerrainDesert=getImage(getCodeBase(),"./img/terrain/desert.png");
		imgTerrainEau=getImage(getCodeBase(),"./img/terrain/eau.png");
		imgTerrainJungle=getImage(getCodeBase(),"./img/terrain/jungle.png");
		
		// Chargement des types de cellule
		imgSolNulle=getImage(getCodeBase(),"./img/sol/nulle.png");
		imgSolArbre=getImage(getCodeBase(),"./img/sol/arbre.png");
		imgSolBoue=getImage(getCodeBase(),"./img/sol/boue.png");
		imgSolFourmiliere=getImage(getCodeBase(),"./img/sol/fourmiliere.png");
		imgSolPlante=getImage(getCodeBase(),"./img/sol/plante.png");
		imgSolTerrier=getImage(getCodeBase(),"./img/sol/terrier.png");
	 	
		// Chargement des types d'images fourmi Jaune
		imgJauneSoldat=getImage(getCodeBase(),"./img/fourmi/jaune/soldat.png");
		imgJauneChef=getImage(getCodeBase(),"./img/fourmi/jaune/chef.png");
		imgJauneReine=getImage(getCodeBase(),"./img/fourmi/jaune/reine.png");

		// Chargement des types d'images fourmi bleu
		imgBleuSoldat=getImage(getCodeBase(),"./img/fourmi/bleu/soldat.png");
		imgBleuChef=getImage(getCodeBase(),"./img/fourmi/bleu/chef.png");
		imgBleuReine=getImage(getCodeBase(),"./img/fourmi/bleu/reine.png");

		// Chargement des types d'images fourmi noir
		imgNoirSoldat=getImage(getCodeBase(),"./img/fourmi/noir/soldat.png");
		imgNoirChef=getImage(getCodeBase(),"./img/fourmi/noir/chef.png");
		imgNoirReine=getImage(getCodeBase(),"./img/fourmi/noir/reine.png");
		
		// Chargement des types d'images autre
		imgTombe=getImage(getCodeBase(),"./img/autre/tombe.png");
		imgTombeReine=getImage(getCodeBase(),"./img/autre/tombereine.png");
		
		// Definition du terrain courant (visuel seulement)
		terrainCourant = imgTerrainDesert;
		
		// Initialisation du terrain - Pas besoin de type si random
		//monTerrain = new Terrain(1, lignes, colonnes, EnumCellule.PLANTE);
		monTerrain = new Terrain(1, lignes, colonnes);

		// Initialisation des Fourmilieres / Reines
		for (int i = 0; i < nbReine; i++) {
			
			Random r1 = new Random();
			Random r2 = new Random();
			int initX,initY = 0;
			initX = 0 + r1.nextInt(lignes-0);
			initY = 0 + r2.nextInt(colonnes-0);
						
			// Creation de la FourmiReine
			FourmiReine uneFourmiReine = new FourmiReine(i, monTerrain, false, 100, 100, initX, initY, 0, 0);
			Thread nouveauThread = new Thread(uneFourmiReine);
			nouveauThread.start();
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
			lesFourmilieres.get(i).informerParPheromone(EnumPheromone.VIVRE); // autours fourmiliere
		}
		
	}
	
	/* ===========================IHM============================= */
	
	/**
	 * Methode appellee pour redessiner l'interface, appelee par repaint()
	 */
	@Override
	public  void  paint(Graphics g) {
		
		while(true){
			
			//g.setColor(Color.PINK);

			//g.fillOval(i*13, j*10, FR.getTerrain().getCase(i, j).getNourriture()/10, FR.getTerrain().getCase(i, j).getNourriture()/10);
			
			// --- Affichage du terrain -------------------------
			for (int i = 0; i < monTerrain.getNbLigne(); i++) {
				for (int j = 0; j < monTerrain.getNbColonne(); j++) {
					g.drawImage(terrainCourant,i*tailleImage,j*tailleImage,this);
				}
			}
			
			// --- Affichage des cellules -------------------------
			for (int i = 0; i < monTerrain.getNbLigne(); i++) {
				for (int j = 0; j < monTerrain.getNbColonne(); j++) {
					
					switch(monTerrain.getACellule(i, j).getTypeCellule()){
						case ARBRE:
							if(monTerrain.getACellule(i, j).getQtNourritureCourante() > 0){
								g.drawImage(imgSolArbre,i*tailleImage,j*tailleImage,this);
							} else{
								g.drawImage(imgSolNulle,i*tailleImage,j*tailleImage,this);
							}
							break;
						case BOUE:
							if(monTerrain.getACellule(i, j).getQtNourritureCourante() > 0){
								g.drawImage(imgSolBoue,i*tailleImage,j*tailleImage,this);
							} else{
								g.drawImage(imgSolNulle,i*tailleImage,j*tailleImage,this);
							}
							break;
						case PLANTE:
							if(monTerrain.getACellule(i, j).getQtNourritureCourante() > 0){
								g.drawImage(imgSolPlante,i*tailleImage,j*tailleImage,this);
							} else{
								g.drawImage(imgSolNulle,i*tailleImage,j*tailleImage,this);
							}
							break;
						case TERRIER:
							if(monTerrain.getACellule(i, j).getQtNourritureCourante() > 0){
								g.drawImage(imgSolTerrier,i*tailleImage,j*tailleImage,this);
							} else{
								g.drawImage(imgSolNulle,i*tailleImage,j*tailleImage,this);
							}
							break;
						case FOURMILIERE:
							g.drawImage(imgSolFourmiliere,i*tailleImage,j*tailleImage,this);
							break;
						case NULLE:
							g.drawImage(imgSolNulle,i*tailleImage,j*tailleImage,this);
							break;
					} // end switch
				
				}
			}
			
			// --- Affichage des fourmis -------------------------
			for (FourmiReine fourmiReine : lesFourmilieres) {
				
				// Verification la vie des reines
				if (fourmiReine.getDureeVie()>0) {
					
					// Affichage des reines
					switch(fourmiReine.getTypeFourmi()){
						case BLEU:
							g.drawImage(imgBleuReine,fourmiReine.getPosX()*tailleImage,
									fourmiReine.getPosY()*tailleImage,this);
							break;
						case JAUNE:
							g.drawImage(imgJauneReine,fourmiReine.getPosX()*tailleImage,
									fourmiReine.getPosY()*tailleImage,this);
							break;
						case NOIR:
							g.drawImage(imgNoirReine,fourmiReine.getPosX()*tailleImage,
									fourmiReine.getPosY()*tailleImage,this);
							break;
					} // end switch
					
					// Affichage des fourmis de la reine
					for (int k = 0; k < fourmiReine.getMesFourmis().size(); k++) {
						
						// Verification de la vie de la fourmi
						if (fourmiReine.getMesFourmis().get(k).isAffichable()) {
							switch(fourmiReine.getMesFourmis().get(k).getFkFourmiReine().getTypeFourmi()){
								case BLEU:
									g.drawImage(imgBleuSoldat,fourmiReine.getMesFourmis().get(k).getPosX()*tailleImage,
											fourmiReine.getMesFourmis().get(k).getPosY()*tailleImage,this);
									break;
								case JAUNE:
									g.drawImage(imgJauneSoldat,fourmiReine.getMesFourmis().get(k).getPosX()*tailleImage,
											fourmiReine.getMesFourmis().get(k).getPosY()*tailleImage,this);
									break;
								case NOIR:
									g.drawImage(imgNoirSoldat,fourmiReine.getMesFourmis().get(k).getPosX()*tailleImage,
											fourmiReine.getMesFourmis().get(k).getPosY()*tailleImage,this);
									break;
							} // end switch
						} else {
							g.drawImage(imgTombe,fourmiReine.getMesFourmis().get(k).getPosX()*tailleImage,
									fourmiReine.getMesFourmis().get(k).getPosY()*tailleImage,this);
						}
						
						
					} // end for
					
				} 
				
				// 
				else {
					g.drawImage(imgTombeReine,fourmiReine.getPosX()*tailleImage,fourmiReine.getPosY()*tailleImage,this);
				}
				
			}

			// Attendre un peu avant de rectualiser l'affichage
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} // end while

			
	}

}
