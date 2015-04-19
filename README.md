#La Fourmiliere#
IMERIR2A - 20150419 - Cours de Java

###Objectifs : 
* Modélisation objet
* Notion de Design Pattern (Observer/Observable)
* Notions de Thread
* Notions d'interface
* Notions d'Applet

###Contexte : 

Nous allons modéliser une version très simplifiée de fourmilière. Dans le but de pouvoir faire évoluer le modèle, certaines fonctionnalités resterons floues. L'idée est la suivante :
- Une reine pond une grande quantité de fourmis soldats et a (au moins) une fourmi­chef.
- La reine donne le signal d'éclosion par phéromones.
- Les fourmis soldats parcourent le terrain à la recherche de nourriture,
et la ramènent à la fourmi­chef.
- Les fourmis soldats ont une durée de vie limitée.
- Lorsque toutes le fourmis soldats sont mortes, leur fourmi chef meurt
et la reine peut aller fonder une autre fourmilière.


###TODO
- Combat : Executer un combat si deux fourmis sont sur la meme cellule
- Fourmi Chef : Faire fourmiller les chefs autours de la fourmiliere
- Fourmi Soldat : Finir la recherche de nourriture
- Fourmi Chef/Soldat : Definir les actions pour le pheromone d'attaque
- Applet : Verifier l'affichage des tombes
- Applet : Ajouter des boutons de controle pour envoyer des pheromones