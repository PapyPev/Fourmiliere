package fourmis;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import environnements.Terrain;
import fourmis.FourmiReine.EnumPheromone;
import outils.Affichable;
import outils.Combattant;

/**
 * 
 * @author pev, fred
 * @category cours java
 * @version 20150331
 *
 */
public class FourmiSoldat extends Fourmi implements Affichable, Combattant, Runnable, Observer{
	
	/* ===========================ATRB================================ */
	
	private int DIST_MAX_REINE = 2; //nb de cellule autours de la reine apres naissance
	private int DIST_VISUELLE = 2; // nb de cellule vues autours de cette fourmi
	private FourmiChef fkFourmiChef;
	private EnumPheromone pheromoneCourant;
	
	/* ===========================CONST=============================== */

	/**
	 * Constructeur d'une fourmi soldat
	 * @param idFourmi : identifiant de la fourmi
	 * @param typeFourmi : couleur de la fourmi
	 * @param fkTerrain : terrainde reference
	 * @param combattant : si la fourmi est combattante ou non
	 * @param dureeVie : duree de vie en seconde
	 * @param pointDeVie : point de vie
	 * @param posX : position sur le terrain, ligne
	 * @param posY : position sur le terrain, colonne
	 * @param qtNourritureTransportee : qt nourriture transportee
	 * @param qtNourritureTransportable : qt nourriture transportable
	 * @param fkFourmiChef : reference a la fourmi chef
	 */
	public FourmiSoldat(int idFourmi, EnumFourmi typeFourmi, Terrain fkTerrain,
			boolean combattant, int dureeVie, int pointDeVie, int posX, int posY,
			int qtNourritureTransportee, int qtNourritureTransportable, FourmiChef fkFourmiChef) {
		super(idFourmi, typeFourmi, fkTerrain, combattant, dureeVie, pointDeVie, 
				posX, posY, qtNourritureTransportee, qtNourritureTransportable);
		this.fkFourmiChef = fkFourmiChef;
		this.pheromoneCourant = EnumPheromone.RIEN;
	}
	
	/* ==========================G/S================================== */

	public FourmiChef getFkFourmiChef() {
		return fkFourmiChef;
	}
	public void setFkFourmiChef(FourmiChef fkFourmiChef) {
		this.fkFourmiChef = fkFourmiChef;
	}
	
	public EnumPheromone getPheromoneCourant() {
		return pheromoneCourant;
	}
	public void setPheromoneCourant(EnumPheromone pheromoneCourant) {
		this.pheromoneCourant = pheromoneCourant;
	}
	
	/* ===========================ACTIONS============================= */
	
	@Override
	public String toString() {
		return "FourmiSoldat [fkFourmiChef=" + fkFourmiChef + ", toString()="
				+ super.toString() + "]";
	}
	
	/**
	 * Methode permettant de recuperer la quantite de nourriture presente
	 * sur la cellule en cours
	 */
	public void prendreNourriture(){

		// Recupere la qt de nourriture sur la cellule
		int qtCell = this.getFkTerrain().getACellule(this.getPosX(), this.getPosY()).getQtNourritureCourante();
		
		// S'il y a de la nourriture
		if(qtCell > 0){
			
			// Si on peut porter de la nourriture
			if (this.getQtNourritureTransportee() < this.getQtNourritureTransportable()){
				
				int qtNourriturePrenableParFourmi = this.getQtNourritureTransportable() - this.getQtNourritureTransportee();
				
				// si on peut transporter plus que ce qu'il y a sur la cellule
				if (qtNourriturePrenableParFourmi >= qtCell) {
					
					// on prend tout ce qu'il y a sur la cellule
					this.getFkTerrain().getACellule(this.getPosX(), this.getPosY()).setQtNourritureCourante(0);
					
					// on l'ajoute a la fourmi
					this.setQtNourritureTransportee(qtCell);
					
				} else {
					
					int qtRestanteCellule = qtCell - this.getQtNourritureTransportable();
					
					// on decremente la quantite sur la cellule
					this.getFkTerrain().getACellule(this.getPosX(), this.getPosY()).setQtNourritureCourante(qtRestanteCellule);
					
					// on ajoute des pheromones sur cette cellule
					this.getFkTerrain().getACellule(this.getPosX(), this.getPosY()).setPresencePheromone(true);
					
					// on ajoute la quantite maximale que peut porter la fourmi
					this.setQtNourritureTransportee(this.getQtNourritureTransportable());
					
				}
				
			}

		}
		
	}

	/**
	 * Methode permettant de deposer la quantite de nourriture transportee 
	 * tout en avertissant le chef
	 */
	public void deposerNourriture(){
		// Incremente la nourriture du chef
		this.getFkFourmiChef().setQtNourritureRecoltee(this.getFkFourmiChef().getQtNourritureRecoltee() + this.getQtNourritureTransportee());
		// Remet a zero la quantite de la fourmi
		this.setQtNourritureTransportee(0);
	}
	
	/**
	 * Methode permettant de crer un thread par fourmi soldat
	 * et de leur donner une vie independante
	 */
	@Override
	public void eclosion() {
		Thread nouveauThread = new Thread(this);
		nouveauThread.start(); //appel la fonction run()
	}
	
	/**
	 * Methode permettant de retourner aux cotes de sa reine
	 * @throws InterruptedException 
	 */
	public void retournerVoirSaReine() throws InterruptedException{
		
		// Tant qu'on n'est pas sur la fourmiliere/reine
		while (this.getPosX() != this.getFkFourmiChef().getFkFourmiReine().getPosX() 
			&& this.getPosY() != this.getFkFourmiChef().getFkFourmiReine().getPosY()) {
			
			// Si on est x > reine et y > reine : bas droit
			if (this.getPosX() > this.getFkFourmiChef().getFkFourmiReine().getPosX()
					&& this.getPosY() > this.getFkFourmiChef().getFkFourmiReine().getPosY()) {
				this.seDeplacer(this.getPosX()-1, this.getPosY()-1);
				
			} else {
				
				// Si x < reine et y > reine : bas gauche
				if (this.getPosX() < this.getFkFourmiChef().getFkFourmiReine().getPosX()
					&& this.getPosY() > this.getFkFourmiChef().getFkFourmiReine().getPosY()) {
					this.seDeplacer(this.getPosX()+1, this.getPosY()-1);
					
				} else {
					
					// Si x < reine et y < reine : haut gauche
					if (this.getPosX() < this.getFkFourmiChef().getFkFourmiReine().getPosX()
						&& this.getPosY() < this.getFkFourmiChef().getFkFourmiReine().getPosY()) {
						this.seDeplacer(this.getPosX()+1, this.getPosY()+1);
						
					} else {
						
						// Si x > reine et y < reine : haut droit
						this.seDeplacer(this.getPosX()-1, this.getPosY()+1);
						
					}
				}
			}
			
		}
		
	}
	
	/**
	 * Methode permettant de retourner aux cotes de son chef
	 * @throws InterruptedException 
	 */
	public void retournerVoirSonChef() throws InterruptedException{
		
		// Tant qu'on n'est pas sur la fourmiliere/reine
		while (this.getPosX() != this.getFkFourmiChef().getPosX() 
			&& this.getPosY() != this.getFkFourmiChef().getPosY()) {
			
			// Si on est x > reine et y > reine : bas droit
			if (this.getPosX() > this.getFkFourmiChef().getPosX()
					&& this.getPosY() > this.getFkFourmiChef().getPosY()) {
				this.seDeplacer(this.getPosX()-1, this.getPosY()-1);
			} else {
				
				// Si x < reine et y > reine : bas gauche
				if (this.getPosX() < this.getFkFourmiChef().getPosX()
					&& this.getPosY() > this.getFkFourmiChef().getPosY()) {
					this.seDeplacer(this.getPosX()+1, this.getPosY()-1);
					
				} else {
					
					// Si x < reine et y < reine : haut gauche
					if (this.getPosX() < this.getFkFourmiChef().getPosX()
						&& this.getPosY() < this.getFkFourmiChef().getPosY()) {
						this.seDeplacer(this.getPosX()+1, this.getPosY()+1);
						
					} else {
						
						// Si x > reine et y < reine : haut droit
						this.seDeplacer(this.getPosX()+1, this.getPosY()-1);
						
					}
				}
			}
			
		}
		
	}
	
	/* ===========================PHEROMONE============================= */
	
	/**
	 * Methode permettant a une fourmi de fourmiller aleatoirement
	 * @throws InterruptedException 
	 */
	public void pheromoneRien() throws InterruptedException{
		
		// Valeur de deplacement aleatoire a +/- 1
		Random r1 = new Random();
		Random r2 = new Random();
		int deplacementX = r1.nextInt(2 + 1) -1;
		int deplacementY = r2.nextInt(2 + 1) -1;
		
		// Calcul du prochain deplacement
		int nextDeplacementX = this.getPosX()+deplacementX;
		int nextDeplacementY = this.getPosY()+deplacementY;
		
		// Verification du deplacement
		if (nextDeplacementX < 0 || nextDeplacementX > this.getFkFourmiReine().getFkTerrain().getNbLigne()) {
			nextDeplacementX = this.getPosX();
		}
		if (nextDeplacementY < 0 || nextDeplacementY > this.getFkFourmiReine().getFkTerrain().getNbColonne()) {
			nextDeplacementY = this.getPosY();
		}
		
		// Deplacement
		this.seDeplacer(nextDeplacementX, nextDeplacementY);
						
	}
	
	/**
	 * Methode permettant a une fourmi de vivre a une distance
	 * de la fourmiliere
	 * @throws InterruptedException 
	 */
	public synchronized void pheromoneVivre() throws InterruptedException{
		
		// TODO : vivre a la distance de la fourmiliere
		
		// Valeur de deplacement aleatoire a +/- 1
		Random r1 = new Random();
		Random r2 = new Random();
		int deplacementX = r1.nextInt(2 + 1) -1;
		int deplacementY = r2.nextInt(2 + 1) -1;
		
		// Calcul du prochain deplacement
		int nextDeplacementX = this.getPosX()+deplacementX;
		int nextDeplacementY = this.getPosY()+deplacementY;
		
		// Verification du deplacement
		if (nextDeplacementX < 0 
				|| nextDeplacementX > this.getFkFourmiReine().getFkTerrain().getNbLigne()
				|| (nextDeplacementX > this.getFkFourmiChef().getFkFourmiReine().getPosX()+this.DIST_MAX_REINE)
				|| nextDeplacementX < this.getFkFourmiChef().getFkFourmiReine().getPosX()-this.DIST_MAX_REINE) {
			nextDeplacementX = this.getPosX();
			
		}
		if (nextDeplacementY < 0 
				|| nextDeplacementY > this.getFkFourmiReine().getFkTerrain().getNbColonne()
				|| (nextDeplacementY > this.getFkFourmiChef().getFkFourmiReine().getPosY()+this.DIST_MAX_REINE)
				|| nextDeplacementY < this.getFkFourmiChef().getFkFourmiReine().getPosY()-this.DIST_MAX_REINE) {
			nextDeplacementY = this.getPosY();

		}
		
		// Deplacement
		this.seDeplacer(nextDeplacementX, nextDeplacementY);
		
	}
	
	/**
	 * Methode permettant a une fourmi d'aller chercher de la nourriture
	 */
	public void pheromoneNourriture(){
		// TODO : parcourir la map et ramener la nourriture si plein puis repartir a la recherche
		
		// Tant que le pheromone courant est la recherche de nourriture
		while(pheromoneCourant == EnumPheromone.NOURRITURE){
			
			boolean isPheromone = false;
			int nextPosX = -1-this.DIST_VISUELLE;
			int nextPosY = -1-this.DIST_VISUELLE;
		
			// Regarder a DIST_VUE s'il y a une presence de pheromone
			// Parcours des cellules autours de la fourmi : ligne
			while(isPheromone == true || nextPosX > this.DIST_VISUELLE){
				nextPosX++;
				
				// Parcours des cellules autours de la fourmi : colonne
				while(isPheromone == true || nextPosY > this.DIST_VISUELLE){
					nextPosY++;
					
					// Detection de la presence de pheromone
					if(this.getFkTerrain().getACellule(this.getPosX()+nextPosX, this.getPosY()+nextPosY).isPresencePheromone()){
						isPheromone = true;
					}
					
				}
				
			}
			
			// Il y a presence de pheromone autours
			if (isPheromone) {
				
				// TODO : Suivre le chemin des pheromones (methode ?)
				
			}
			
			// Il n'y a pas de pheromone autours
			else {
				
				// Recherche la cellule ideale a DIST_VUE cellules autours (qt++)
				int qtNourritureMemorisee = 0;
				int nextPosXMemorisee = 0;
				int nextPosYMemorisee = 0;
				
				for (int i = (-1-this.DIST_VISUELLE); i < this.DIST_VISUELLE; i++) {
					for (int j = (-1-this.DIST_VISUELLE); j < this.DIST_VISUELLE; j++) {
						
						int qtNourritureVue = this.getFkTerrain().getACellule(this.getPosX()+i, this.getPosY()+j).getQtNourritureCourante();
						
						// Si la quantite de nourriture a (+i, +j) est superieure a la quantite deja vue
						if(qtNourritureMemorisee < this.getFkTerrain().getACellule(this.getPosX()+i, this.getPosY()+j).getQtNourritureCourante()){
							
							// memorisation des informations
							qtNourritureMemorisee = qtNourritureVue;
							nextPosXMemorisee = i;
							nextPosYMemorisee = j;
							
						}
						
					}
				}
				
				// Si il y a une cellule ideale
				if (nextPosXMemorisee != 0 || nextPosYMemorisee != 0) {
					
					// On se deplace sur la cellule
					
					
					
				}
				
				// S'il n'y a pas de cellule ideale
				else {
					// TODO : se deplacer random +/- 1 sur x et y
					// TODO : relancer l'algo
				}
				
			}

			
			// Recuperer la nourriture
			
			// Si qtRestante = 0
				// retourner a la fourmiliere
			// Sinon
				// retourner a la fourmiliere en deposant des pheromones
		
			// avertir le chef de la qt
			
			
		}
		
	}
	
	/**
	 * Methode permettant a une fourmi, si elle est soldat, d'attaquer
	 */
	public void pheromoneAttaquer(){
		// TODO : si une fourmi est sur la cellule en cours, attaquer
	}

	/* ===========================VIVRE============================= */
	
	/**
	 * Methode permettant a une fourmi soldat de vivre son existance
	 */
	@Override
	public synchronized void run() {
		
		while(this.getDureeVie() > 0){
			
			switch(this.pheromoneCourant){
				case RIEN:
					try {
						this.pheromoneRien();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					break;
				case VIVRE:
					try {
						this.retournerVoirSaReine();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					try {
						this.pheromoneVivre();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				case MOURIR:
					this.setDureeVie(0);
					break;
				case ATTAQUER:
					this.pheromoneAttaquer();
					break;
				case NOURRITURE:
					this.pheromoneNourriture();
					break;
				default:
					System.out.println("WARNING: Update Soldat, Message pheromone inconnu.");
					this.setPheromoneCourant(EnumPheromone.RIEN);
					break;
			
			}
						
		}
		
		// Fin du thread
		System.out.println(this.getIdFourmi() + "S: Je suis mort");
		
		// Decremente le nombre de soldat vivant, informe le chef
		this.getFkFourmiChef().miseAJourNbSoldatVivants();
		
	}

	@Override
	public void update(Observable o, Object arg) {
		
		// Cast de l'objet recu au bon format
		EnumPheromone pheromoneRecu = (EnumPheromone)arg;
		
		if(pheromoneRecu != null){
			
			// Met a jour le message courant
			this.setPheromoneCourant(pheromoneRecu);
			
			
		} else{
			System.out.println("WARNING: Update Soldat, Message pheromone null.");
		}
		
	}
	
	@Override
	public void combattantAttaquer(){
		// TODO : soldat, attaquer
	}

	@Override
	public boolean isAffichable() {
		return this.getDureeVie() > 0;
	}

	@Override
	public FourmiReine getFkFourmiReine() {
		return this.getFkFourmiChef().getFkFourmiReine();
	}
	
}
