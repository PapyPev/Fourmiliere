import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import environnements.Terrain;
import environnements.Cellule.EnumCellule;
import fourmis.FourmiReine;


public class Main extends Applet implements Runnable {

	/**
	 * Serial ID et attributs
	 */
	private static final long serialVersionUID = 1L;
	
	Terrain monTerrain;
	
	// Parametres de lancement
	ArrayList<FourmiReine> lesFourmilieres = new ArrayList<FourmiReine>();
	int lignes = 30;
	int colonnes = 30;
	int nbReine = 1;
	int nbOeufParReine = 5; //mini : 5
	double probaOeufChef = 0.1;
    
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
		this.setSize(lignes*22, colonnes*22);
		
		// Chargement des types de terrain
		imgSolNulle=getImage(getCodeBase(),"./img/solNulle.png");
		imgSolArbre=getImage(getCodeBase(),"./img/solArbre.png");
		imgSolBoue=getImage(getCodeBase(),"./img/solBoue.png");
		imgSolFourmiliere=getImage(getCodeBase(),"./img/solFourmiliere.png");
		imgSolPlante=getImage(getCodeBase(),"./img/solPlante.png");
		imgSolTerrier=getImage(getCodeBase(),"./img/solTerrier.png");
	 	
		// Chargement des types d'images
		imgNoirSoldat=getImage(getCodeBase(),"./img/noirSoldat.png");
		imgNoirChef=getImage(getCodeBase(),"./img/noirChef.png");
		imgNoirReine=getImage(getCodeBase(),"./img/noirReine.png");
		imgSolFourmiliere=getImage(getCodeBase(),"./img/solFourmiliere.png");
		
		// Initialisation du terrain - Pas besoin de type si random
		monTerrain = new Terrain(1, lignes, colonnes, EnumCellule.PLANTE);
		

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
	}
	
	/**
	 * Methode appellee pour redessiner l'interface, appelee par repaint()
	 */
	@Override
	public  void  paint(Graphics g) {
		
		// Affichage du terrain selon le type de cellule
		for (int i = 0; i < monTerrain.getNbLigne(); i++) {
			for (int j = 0; j < monTerrain.getNbColonne(); j++) {
				switch(monTerrain.getACellule(i, j).getTypeCellule()){
					case ARBRE:
						g.drawImage(imgSolArbre,lignes+i*20,colonnes+j*20,this);
						break;
					case BOUE:
						g.drawImage(imgSolBoue,lignes+i*20,colonnes+j*20,this);
						break;
					case PLANTE:
						g.drawImage(imgSolPlante,lignes+i*20,colonnes+j*20,this);
						break;
					case TERRIER:
						g.drawImage(imgSolTerrier,lignes+i*20,colonnes+j*20,this);
						break;
					case FOURMILIERE:
						g.drawImage(imgSolFourmiliere,lignes+i*20,colonnes+j*20,this);
						break;
					case NULLE:
						g.drawImage(imgSolNulle,lignes+i*20,colonnes+j*20,this);
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
			g.drawImage(imgNoirReine,posX*20,posY*20,this);
			
			// Affichage des fourmis de la reine
			for (int j = 0; j < lesFourmilieres.get(i).getMesFourmis().size(); j++) {
				
				// Si elles ont encore leur point de vie
				if (lesFourmilieres.get(i).getMesFourmis().get(j).getDureeVie() > 0) {
					posX = lesFourmilieres.get(i).getMesFourmis().get(j).getPosX();
					posY = lesFourmilieres.get(i).getMesFourmis().get(j).getPosY();
					g.drawImage(imgNoirSoldat,posX*20,posY*20,this);
				}
			}
		}
		

			
	}

}
