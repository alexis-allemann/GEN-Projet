# Réalisation d'un jeu du Chibre (Jass)

Projet réalisé dans le cadre du cours de génie logiciel (GEN) de l'[HEIG-VD](https://heig-vd.ch/).

## Présentation du jeu

### Généralités

Ce jeu se joue à 4 joueurs, en 2 équipes de 2, avec un jeu de 36 cartes, allant de l'as au 6.

### L'ordre des cartes

Dans la couleur d'atout : Valet - 9 - As - Roi - Dame - 10 - 8 - 7 - 6.

> Le valet d'atout se nomme le bauer, le 9 d'atout se nomme le nell.

Dans toutes les autres couleurs c'est l'ordre habituel, soit :
As - Roi - Dame - Valet - 10 -9 - 8 - 7 - 6.

### <a id="points">Points par carte</a>

| Carte     | Atout | Normal |
| --------- | :---: | :----: |
| As        |  11   |   11   |
| Roi       |   4   |   4    |
| Dame      |   3   |   3    |
| Valet     |  20   |   2    |
| 10        |  10   |   10   |
| 9         |  14   |   0    |
| 6 - 7 - 8 |   0   |   0    |

### Distribution

Le système mélange les cartes et les distribues aux joueurs (9 cartes par joueur).

### Détermination de l'atout

Au premier tour, le joueur ayant le 7 de carreau fait atout, et devient le donneur du tour suivant. Aux tours suivants, c'est la personne située après le donneur qui fera atout.

> "Faire atout" signifie "choisir la couleur d'atout". Si le joueur ne peut pas ou ne veut pas choisir, il a la possibilité de "chibrer" et son partenaire doit obligatoirement choisir une couleur d'atout à sa place.

C'est le joueur à qui devait faire atout qui entame (même si c'est son partenaire qui a choisi l'atout).

### Annonces

Le système va rechercher si un joueur à une annonce dans son jeu, si oui il l'affiche lors du tours du joueur.

### Annonces possibles

| Nom de l'annonce  | Points | Description                                                  |
| ----------------- | ------ | ------------------------------------------------------------ |
| Schtöckr          | 20     | Le joueur possède le roi et la dame d'atout. Annoncé lorsque l'on pose la deuxième carte et peut être combiné avec une autre annonce). |
| 3 cartes          | 20     | Suite de 3 cartes dans la même couleur                       |
| 4 cartes          | 50     | Suite de 4 cartes dans la même couleur                       |
| Carré de 4 cartes | 100    | Quatre carte de couleurs différentes mais de valeur identique |
| Suite             | 100    | Suite de plus de 4 cartes dans la même couleur               |
| Carré de neufs    | 150    | Ce sont les 4 neufs                                          |
| Carré de valets   | 200    | Ce sont les 4 valets                                         |

Si les 2 équipes ont des annonces lors du premier tour, seul les points des joueurs de l'équipe ayant la meilleure annonce sont enregistrés.

### Décompte des points

A la fin de chaque tour, les points que chaque équipe à remportés sont enregistrés selon la [valeurs des cartes](#points) remportées.

Le dernier pli vaut 5 points supplémentaires.

### Fin de la partie

La première équipe à atteindre 1000 points remporte la partie.

## Points techniques de l'implémentation

:electric_plug: Les joueurs joueront <u>en réseau</u> en utilisant la programmation TCP. Il y aura un serveur et des utilisateurs.

:arrow_up_down: Les <u>threads</u> seront utilisés afin de gérer les parties, les actions des joueurs ainsi que la communication au travers du réseau TCP.

:computer: <u>Git</u> sera utilisé afin de pouvoir gérer le code source et le développement d'une manière plus générale. Un repository [GitHub](https://github.com/) en ligne est utilisé comme "remote repository".

:100: Nous utiliserons [Travis](https://travis-ci.com/) relié à GitHub afin de faire le développement des tests unitaires pour suivre une méthodologie de développement TDD (Test Driven Development).

:bar_chart: Pour la gestion du projet, nous travaillons en mode Scrum à l'aide de [IceScrum](https://www.icescrum.com/fr/).
