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
 * @author pev
 * @version 20150413
 *
 */
public class FourmiSoldat extends Fourmi implements Affichable, Combattant, Runnable, Observer{
	
	/* ===========================ATRB================================ */
	
	private int DIST_MAX_REINE = 2; //nb de cellule autours de la reine apres naissance
	private int DIST_VISUELLE = 1; // nb de cellule vues autours de cette fourmi
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
	 * @param pointDeForce : point de vie
	 * @param posX : position sur le terrain, ligne
	 * @param posY : position sur le terrain, colonne
	 * @param qtNourritureTransportee : qt nourriture transportee
	 * @param qtNourritureTransportable : qt nourriture transportable
	 * @param fkFourmiChef : reference a la fourmi chef
	 */
	public FourmiSoldat(int idFourmi, EnumFourmi typeFourmi, Terrain fkTerrain,
			boolean combattant, int dureeVie, int pointDeForce, int posX, int posY,
			int qtNourritureTransportee, int qtNourritureTransportable, FourmiChef fkFourmiChef) {
		super(idFourmi, typeFourmi, fkTerrain, combattant, dureeVie, pointDeForce, 
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
	
	/**
	 * Methode permettant a une fourmi de se deplacer en deposant des pheromones
	 * @param x
	 * @param y
	 * @throws InterruptedException
	 */
	public synchronized void seDeplacerPheromone(int x, int y) throws InterruptedException{
		this.seDeplacer(x, y);
		this.getFkFourmiChef().getFkFourmiReine().getFkTerrain().getACellule(x, y).setPresencePheromone(true);
	}
	
	@Override
	public String toString() {
		return "FourmiSoldat [fkFourmiChef=" + fkFourmiChef + ", toString()="
				+ super.toString() + "]";
	}
	
	/**
	 * Methode permettant de recuperer la quantite de nourriture presente
	 * sur la cellule en cours
	 */
	public synchronized void prendreNourriture(){
		
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
		
		System.out.println("finPrendreNourriture");
		
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
		System.out.println("Depot");
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
	 * @throws InterruptedException : gestion des erreurs
	 */
	public void retournerVoirSaReine() throws InterruptedException{
				
		int deplacementX;
		int deplacementY;
		
		// Tant qu'on n'est pas sur la fourmiliere/reine
		while (this.getPosX() != this.getFkFourmiChef().getFkFourmiReine().getPosX() 
			&& this.getPosY() != this.getFkFourmiChef().getFkFourmiReine().getPosY()
			&& this.getDureeVie() > 0) {
			
			deplacementX=0;
			deplacementY=0;
			
			if(this.getPosX() > this.getFkFourmiChef().getFkFourmiReine().getPosX()){
				deplacementX = -1;
			}
			
			if (this.getPosX() < this.getFkFourmiChef().getFkFourmiReine().getPosX()) {
				deplacementX = 1;
			}
			
			if(this.getPosY() > this.getFkFourmiChef().getFkFourmiReine().getPosY()){
				deplacementY = -1;
			}
			
			if (this.getPosY() < this.getFkFourmiChef().getFkFourmiReine().getPosY()) {
				deplacementY = 1;
			}
			
			this.seDeplacer(this.getPosX()+deplacementX, this.getPosY()+deplacementY);
			
		}
		
	}
	
	/**
	 * Methode permettant de retourner aux cotes de son chef
	 * @throws InterruptedException : gestion des erreurs
	 */
	public synchronized void retournerVoirSonChef() throws InterruptedException{
		
		int deplacementX;
		int deplacementY;
		
		// Tant qu'on n'est pas sur la fourmiliere/reine
		while (this.getPosX() != this.getFkFourmiChef().getPosX() 
			&& this.getPosY() != this.getFkFourmiChef().getPosY()
			&& this.getDureeVie() > 0) {
			
			deplacementX=0;
			deplacementY=0;
			
			if(this.getPosX() > this.getFkFourmiChef().getPosX()){
				deplacementX = -1;
			}
			
			if (this.getPosX() < this.getFkFourmiChef().getPosX()) {
				deplacementX = 1;
			}
			
			if(this.getPosY() > this.getFkFourmiChef().getPosY()){
				deplacementY = -1;
			}
			
			if (this.getPosY() < this.getFkFourmiChef().getPosY()) {
				deplacementY = 1;
			}
			
			this.seDeplacerPheromone(this.getPosX()+deplacementX, this.getPosY()+deplacementY);
			
		}
				
	}
	
	/* ===========================PHEROMONE============================= */
	
	/**
	 * Methode permettant a une fourmi de fourmiller aleatoirement
	 * @throws InterruptedException : gestion des erreurs
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
	 * @throws InterruptedException  : gestion des erreurs
	 */
	public synchronized void pheromoneVivre() throws InterruptedException{
		
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
				|| nextDeplacementX >= this.getFkFourmiReine().getFkTerrain().getNbLigne()
				|| (nextDeplacementX > this.getFkFourmiChef().getFkFourmiReine().getPosX()+this.DIST_MAX_REINE)
				|| nextDeplacementX < this.getFkFourmiChef().getFkFourmiReine().getPosX()-this.DIST_MAX_REINE) {
			nextDeplacementX = this.getPosX();
			
		}
		if (nextDeplacementY < 0 
				|| nextDeplacementY >= this.getFkFourmiReine().getFkTerrain().getNbColonne()
				|| (nextDeplacementY > this.getFkFourmiChef().getFkFourmiReine().getPosY()+this.DIST_MAX_REINE)
				|| nextDeplacementY < this.getFkFourmiChef().getFkFourmiReine().getPosY()-this.DIST_MAX_REINE) {
			nextDeplacementY = this.getPosY();

		}
		
		// Deplacement
		this.seDeplacer(nextDeplacementX, nextDeplacementY);
		
	}
	
	/**
	 * Methode permettant a une fourmi d'aller chercher de la nourriture
	 * @throws InterruptedException : gestion des erreurs
	 */
	public void pheromoneNourriture() throws InterruptedException{
		
		// TODO : pas operationnel
		
		while(pheromoneCourant == EnumPheromone.NOURRITURE && this.getDureeVie() > 0){
		
			int posX = this.getPosX();
			int posY = this.getPosY();
			int decalX = -this.DIST_VISUELLE;
			int decalY = -this.DIST_VISUELLE;
			int nextPosX = posX;
			int nextPosY = posY;
			
			boolean presencePheromone = false;
			boolean presenceNourriture = false;		
					
			// Verification presence pheromone ou nourriture
			while(!presencePheromone && !presenceNourriture && decalX <= this.DIST_VISUELLE){
				while(!presencePheromone && !presenceNourriture && decalY <= this.DIST_VISUELLE){
					
					nextPosX = posX+decalX;
					nextPosY = posY+decalY;
									
					// Sortie en ligne
					if(nextPosX < 0 || nextPosX >= this.getFkTerrain().getNbLigne()){
						nextPosX = posX;
					}
					
					// Sortie en colonne
					if(nextPosY < 0 || nextPosY >= this.getFkTerrain().getNbColonne()){
						nextPosY = posY;
					}
					
					// Verification presence Pheromone
					if (this.getFkTerrain().getACellule(nextPosX, nextPosY).isPresencePheromone()) {
						presencePheromone = true;
					}
					
					// Verification presence Nourriture
					if (this.getFkTerrain().getACellule(nextPosX, nextPosY).getQtNourritureCourante() > 0) {
						presencePheromone = true;
					}
										
					decalY = decalY+1;
				}
				
				decalX = decalX +1;
			}
						
			// --- Presence de Pheromone ou Nourriture -------------------------------------
			if(presencePheromone || presenceNourriture){
				
				int depX = 0;
				int depY = 0;
				
				// Tant qu'on n'est pas sur la cellule de dest
				while (posX != nextPosX && this.getDureeVie() > 0) {
					while (posY != nextPosY && this.getDureeVie() > 0) {
												
						if (posX < nextPosX) {
							depX = 1;
						}
						if (posX > nextPosX) {
							depX = -1;
						}
						if (posY < nextPosY) {
							depY = 1;
						}
						if (posY > nextPosY) {
							depY = -1;
						}
						
						this.seDeplacer(posX+depX, posY+depY);
						posX = this.getPosX();
						posY = this.getPosY();
						
					}
					
				}
								
			} 
			
			// --- Pas de Presence particuliere --------------------------------------------
			else {
								
				nextPosX = this.getPosX();
				nextPosY = this.getPosY();
				
				// Valeur de deplacement aleatoire a +/- 1
				Random r1 = new Random();
				Random r2 = new Random();
				int rand1 = r1.nextInt(2 + 1) -1;
				int rand2 = r2.nextInt(2 + 1) -1;
				
				nextPosX = posX + rand1;
				nextPosY = posY + rand2;
				
				// Sortie en ligne
				if(nextPosX < 0 || nextPosX >= this.getFkTerrain().getNbLigne()){
					nextPosX = posX;
				}
				
				// Sortie en colonne
				if(nextPosY < 0 || nextPosY >= this.getFkTerrain().getNbColonne()){
					nextPosY = posY;
				}
				
				this.seDeplacer(nextPosX, nextPosY);
				
			}
			
			// --- Verification Cellule en cours --------------------------------------------
			if (this.getFkTerrain().getACellule(posX, posY).getQtNourritureCourante() > 0) {
				
				System.out.println("Bouffe");
								
				// On recupere la nourriture
				this.prendreNourriture();
				
				// On retourne voir son chef
				this.retournerVoirSonChef();
				
				// On depose la nourriture
				this.deposerNourriture();
				
			} else {
				
				this.getFkTerrain().getACellule(this.getPosX(), this.getPosY()).setPresencePheromone(false);
				
			}
		
		} // end while general
		
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
					try {
						this.pheromoneNourriture();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
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
	public boolean isAffichable() {
		return this.getDureeVie() > 0;
	}

	@Override
	public FourmiReine getFkFourmiReine() {
		return this.getFkFourmiChef().getFkFourmiReine();
	}

	@Override
	public int getPointDeForce() {
		return this.getPointDeForce();
	}
	
}
