# Fourmiliere - Java
Cours de Java : développer une pseudo-fourmiliere intégrant à la fois des interfaces, des Thread et une interface graphique, le tout en utilisant un design pattern (Blackbord ou Observable/Observer, ici on a choisit le second).

Pour que le projet soit cohérent il doit être composé :
- D'un terrain
- De cellule composant le terrain
- De fourmis (Reine, Chef et Soldat)

Les interractions sont les suivantes :
- Une fourmi Reine est instanciée
- Une fourmi Reine pond des oeufs dont elle ne connait pas le contenu (Chef et Soldat)
- Une fourmi Reine fait éclore les oeufs via une interface
- Chaque oeuf éclot doit créer un Thread
- Les fourmis Chef doivent rester autours de leur fourmi Reine
- Les fourmis Chef connaissent leur fourmi Reine
- Les fourmis Soldat connaissent leur fourmi Chef
- ... (en cours)
