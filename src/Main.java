import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import environnements.Terrain;
import environnements.Cellule.EnumCellule;
import fourmis.FourmiReine;

/**
 * 
 * @author pev
 * @version 20150413
 *
 */
public class Main extends Applet implements Runnable {
	
	/* =======================DOUBLE BUFFERING============================ */
	
	/* ************************************************ */
	/* Tout ce qui est nécessaire au double buffering 
	 * pris sur : http://www.developer.com/repository/softwaredev/content/article/2000/06/20/SDtravisdblbuf/DoubleBufferApplet.java */
	/* ************************************************ */
	private int width=-1;
	private int height=-1;
	// The offscreen image
	private Image offscreen;

	// switch: are we double buffering or not?
	private boolean dbon = false;

	// Use this to turn double buffering on and off
	protected void setDoubleBuffering( boolean dbon ) {
		this.dbon = dbon;
		if (!dbon) {
			offscreen = null;
		}
	}

	// Depending on the value of our switch, we either call our
	// special code, or just call the default code
	@Override
	public void update( Graphics g ) {
		if (dbon) {
			updateDoubleBufffered( g );
		} else { 
			super.update( g );
		}
	}

	// Do the drawing to an offscreen buffer -- maybe
	private void updateDoubleBufffered( Graphics g ) {

		// Let's make sure we have an offscreen buffer, and that
		// it's the right size.  If the applet has been resized,
		// our buffer will be the wrong size and we need to make
		// a new one
		Dimension d = getSize();
		if (offscreen == null || width!=d.width || height!=d.height || offscreen==null) {
			width = d.width;
			height = d.height;
			if (width>0 || height>0) {
				offscreen = createImage( width, height );
			} else offscreen = null;
		}
	
		// If we still don't have one, give up
		if (offscreen == null) return;
	
		// Get the off-screen graphics object
		Graphics gg = offscreen.getGraphics();
	
		// Clear the off-screen graphics object
		gg.setColor( getBackground() );
		gg.fillRect( 0, 0, width, height );
		gg.setColor( getForeground() );
	
		// Draw to the off-screen graphics object
		paint( gg );
	
		// We don't need this Graphics object anymore
		gg.dispose();
	
		// Finally, we transfer the newly-drawn stuff right to the
		// screen
		g.drawImage( offscreen, 0, 0, null );
	}
	
	/* ************************************************ */

	/* ===========================ATRB================================ */
	
	private static final long serialVersionUID = 1L;

	// Terrain courant
	private Terrain monTerrain;
	private Image terrainCourant;
	
	// Parametres de lancement
	ArrayList<FourmiReine> lesFourmilieres = new ArrayList<FourmiReine>();
	int lignes = 40;
	int colonnes = 40;
	int nbReine = 3; //mini : 1
	int nbOeufParReine = 15; //mini : 5
	double probaOeufChef = 0.1;
    
	// Definition des images
	private int tailleImage = 16; //px
	
	// Definition des Terrains (visuel seulement)
	private Image   imgTerrainDesert;
	private Image   imgTerrainEau;
	private Image   imgTerrainJungle;
	private Image   imgTerrainNeutre;
	private Image   imgTerrainContour;
	
	// Definition des Sols
	private Image   imgSolNulle;
	private Image   imgSolArbre;
	private Image   imgSolBoue;
	private Image   imgSolPlante;
	private Image   imgSolTerrier;
	private Image   imgSolFourmiliere;
		
	// Definition des Fourmis
    private Image   imgRougeSoldat;
	private Image   imgRougeChef;
	private Image   imgRougeReine;
	private Image   imgJauneSoldat;
	private Image   imgJauneChef;
	private Image   imgJauneReine;
	private Image   imgBleuSoldat;
	private Image   imgBleuChef;
	private Image   imgBleuReine;
	private Image   imgVertSoldat;
	private Image   imgVertChef;
	private Image   imgVertReine;
	
	// Definition des Autres
	private Image   imgTombe;
	private Image   imgTombeReine;
	private Image   imgCombat;
	
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
		this.setDoubleBuffering(true);
						
		// Chargement des types de terrain (visuel seulement)
		imgTerrainDesert=getImage(getCodeBase(),"./img/terrain/desert.png");
		imgTerrainEau=getImage(getCodeBase(),"./img/terrain/eau.png");
		imgTerrainJungle=getImage(getCodeBase(),"./img/terrain/jungle.png");
		imgTerrainNeutre=getImage(getCodeBase(),"./img/terrain/neutre.png");
		imgTerrainContour=getImage(getCodeBase(),"./img/terrain/contour.png");
		
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

		// Chargement des types d'images fourmi rouge
		imgRougeSoldat=getImage(getCodeBase(),"./img/fourmi/rouge/soldat.png");
		imgRougeChef=getImage(getCodeBase(),"./img/fourmi/rouge/chef.png");
		imgRougeReine=getImage(getCodeBase(),"./img/fourmi/rouge/reine.png");
		
		// Chargement des types d'images fourmi vert
		imgVertSoldat=getImage(getCodeBase(),"./img/fourmi/vert/soldat.png");
		imgVertChef=getImage(getCodeBase(),"./img/fourmi/vert/chef.png");
		imgVertReine=getImage(getCodeBase(),"./img/fourmi/vert/reine.png");
		
		// Chargement des types d'images autre
		imgTombe=getImage(getCodeBase(),"./img/autre/tombe.png");
		imgTombeReine=getImage(getCodeBase(),"./img/autre/tombereine.png");
		imgCombat=getImage(getCodeBase(),"./img/autre/combat.png");
		
		// Definition du terrain courant (visuel seulement) - Terrain random
		Random r0 = new Random();
		int valeurRandom = 0 + r0.nextInt(3-0);
		switch(valeurRandom){
			case 0:
				terrainCourant = imgTerrainJungle;
				break;
			case 1:
				terrainCourant = imgTerrainDesert;
				break;
			case 2:
				terrainCourant = imgTerrainEau;
				break;
			default:
				terrainCourant = imgTerrainNeutre;
				break;
		}
		
		
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
			FourmiReine uneFourmiReine = new FourmiReine(i, monTerrain, false, 200, 100, initX, initY, 0, 0);
			Thread nouveauThread = new Thread(uneFourmiReine);
			nouveauThread.start();
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
		//for (int i = 0; i < lesFourmilieres.size(); i++){
		//	lesFourmilieres.get(i).informerParPheromone(EnumPheromone.VIVRE); // autours fourmiliere
		//}
		
	}
	
	/* ===========================IHM============================= */
	
	/**
	 * Methode appellee pour redessiner l'interface, appelee par repaint()
	 */
	@Override
	public  void  paint(Graphics g) {
		
		while(true){
			
			
			// --- Affichage du terrain -------------------------
			for (int i = 0; i < monTerrain.getNbLigne(); i++) {
				for (int j = 0; j < monTerrain.getNbColonne(); j++) {
					g.drawImage(terrainCourant,i*tailleImage,j*tailleImage,this);
				}
			}
			
			// --- Affichage des cellules -------------------------
			for (int i = 0; i < monTerrain.getNbColonne(); i++) {
				for (int j = 0; j < monTerrain.getNbLigne(); j++) {
					
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
					
					// Affichage des pheromones
					if (monTerrain.getACellule(i, j).isPresencePheromone()) {
						g.setColor(Color.PINK);
						g.fillOval(i*tailleImage, j*tailleImage, 10, 10);
					}
					
					
				
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
						case ROUGE:
							g.drawImage(imgRougeReine,fourmiReine.getPosX()*tailleImage,
									fourmiReine.getPosY()*tailleImage,this);
							break;
						case VERT:
							g.drawImage(imgVertReine,fourmiReine.getPosX()*tailleImage,
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
								case ROUGE:
									g.drawImage(imgRougeSoldat,fourmiReine.getMesFourmis().get(k).getPosX()*tailleImage,
											fourmiReine.getMesFourmis().get(k).getPosY()*tailleImage,this);
									break;
								case VERT:
									g.drawImage(imgVertSoldat,fourmiReine.getMesFourmis().get(k).getPosX()*tailleImage,
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
