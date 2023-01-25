# Information

Projet réalisé dans le cadre du cours Pratique de la programmation orientée-objet ([CS-208](https://edu.epfl.ch/coursebook/fr/pratique-de-la-programmation-orientee-objet-CS-108) dispensé à l'EPFL.

Auteurs: Cristian Safta, Jérémy Chaverot.

Date: Février-Juin 2021.

# tCHu - Présentation du projet (cr. Michel Schinz)

Le projet de cette année, nommé tCHu et prononcé « tchou », est un jeu fortement inspiré de la version suisse des [Aventuriers du Rail](https://fr.wikipedia.org/wiki/Les_Aventuriers_du_Rail), un célèbre jeu de société. Notre variante se joue à deux, sur une carte représentant un réseau ferroviaire reliant différentes villes de Suisse et des pays voisins.

La figure ci-dessous montre une partie de tCHu en cours entre une joueuse nommée Ada et un joueur nommé Charles. Les différentes parties de l'interface graphique ont été numérotées de 1 à 6 pour faciliter la description qui suit.

La vue présentée ci-dessous est celle d'Ada. Celles de Charles est similaire, seuls diffèrent les éléments propres à chaque joueur affichés au bas (numéros 5 et 6).

![tchu-game-window](https://user-images.githubusercontent.com/100281310/214697231-ebe5e4d8-0bc3-4a1d-97b2-b359ed50e3cd.png)

Les joueurs de tCHu ont pour but de réaliser un certain nombre d'objectifs, donnés par les billets en leur possession, affichés en bas à gauche de l'écran (numéro 5). Par exemple, le second billet visible dans la liste d'Ada est :

Berne - Coire (10)

Ce billet permet à Ada de gagner 10 points si elle parvient à relier les villes de Berne et de Coire avant la fin de la partie. Si elle n'y parvient pas, elle perd ces 10 points.

Afin de relier ces deux villes, Ada doit s'emparer d'un certain nombre de routes qui, mises bout à bout, forment un trajet continu allant de Berne à Coire. Une route relie deux gares voisines du réseau, et le moyen le plus simple de relier Berne à Coire consiste donc à s'emparer des quatre routes suivantes :

  1. Berne - Lucerne,
  2. Lucerne - Schwyz,
  3. Schwyz - Wassen,
  4. Wassen - Coire.

En consultant la carte (numéro 3), on remarque qu'à ce stade de la partie, Ada s'est déjà emparée de ces quatre routes. Cela se voit au fait qu'elles sont occupées par des wagons bleu clair — la couleur correspondant à Ada —, symbolisés par des rectangles colorés ornés de disques blancs.

Pour s'emparer de ces routes, Ada a utilisé des cartes colorées et des wagons qu'elle avait en main. Les cartes dont elle dispose actuellement sont visibles en bas de l'écran (numéro 6), tandis que le nombre de wagons qui lui restent (cinq) est visible dans les statistiques en haut à gauche (numéro 1).

Avec les cartes et les wagons dont elle dispose actuellement, Ada pourrait par exemple s'emparer de la route Kreuzlingen - Saint-Gall, au nord-est de la carte. En effet, cette route comporte une seule case verte, et un joueur désirant s'en emparer doit donc avoir en main au moins une carte verte et un wagon, ce qui est le cas pour Ada.

Pour obtenir de nouvelles cartes, les joueurs peuvent en tirer deux à chaque tour. Chacune de ces cartes peut être soit l'une des cinq cartes disposées face visible à droite du plateau, soit la carte du sommet de la pioche, dont la face n'est pas visible (numéro 4).

Les joueurs peuvent également obtenir de nouveaux billets lors d'un tour, en tirant les trois premiers de la pioche pour en garder au moins un. C'est d'ailleurs ce que Charles est en train de faire dans la partie montrée à la figure 1, comme on l'apprend en lisant les informations liées à son déroulement, présentées à gauche de l'écran (numéro 2).

La partie se termine lorsqu'un joueur possède deux wagons ou moins. Dès cet instant, chaque joueur joue encore un tour, après quoi le décompte des points est effectué. Le joueur en ayant obtenu le plus grand nombre est déclaré vainqueur.

La description du jeu donnée ci-dessus est encore incomplète, certains éléments comme les tunnels ou les cartes locomotive ayant été omis pour ne pas alourdir la présentation. Ces éléments et les règles qui y correspondent seront décrits dans le cadre des différentes étapes.

# Version américaine

La dernière étape du projet étant libre, nous avons décidé de traverser les océans, et d'implémenter une version américaine de tCHu. De plus nous avons ajouté des animations, des sons, la possibilité de jouer de 2 à 8 joueurs, de discuter via un chat textuel, et la mise en évidence du plus long trajet à la fin de la partie.

<img width="1545" alt="game-window-usa" src="https://user-images.githubusercontent.com/100281310/214703820-3f7f80a9-23e7-4460-a2e9-151e36fe2bc2.png">
