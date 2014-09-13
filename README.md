Juego de Damas
========

Este juego fue desarrollado en el semestre 2012-2 de la carrera ingeniería de sistemas en la Universidad EAFIT.
Específicamente para la materia *Estructuras de Datos y Algoritmos I*.

Descripción
-----
Para el desarrollo del juego se implementó una método de decisión el cual se encarga que el mismo sea más eficiente conteniendo la información perfecta, dicho método es llamado **Minimax**, el cual consta de elegir el mejor movimiento para un jugador suponiendo que su adversario escogerá el peor para sí.

Para trabajarlo se utilizo un **árbol de juego**, donde se generaran todos los nodos hasta llegar a un estado terminal, cada nodo contiene una posibilidad de movimiento representado en un valor numérico, y de allí se calcula el valor de los nodos superiores a partir del nivel en el cual se encuentra el nodo (Hay niveles MAX y niveles MIN en el árbol). Una vez hecho esto, se elige la jugada tomando en cuenta los valores que han llegado al nivel más alto. La evaluación nodal se realiza con una función conocida como **evaluación heurística**, es comúnmente utilizada por juegos de estrategia como el Ajedrez. Dicha evaluación heurística estima el valor numérico a las posibilidades de movimiento, se debe tomar la decisión por cual sub-árbol continuar. Si bien, se debe tener en cuenta que los métodos anteriormente mencionados no pueden usar mucho recurso (eficiencia), por lo que la búsqueda explora los nodos del árbol de una forma veloz, mejorando así el rendimiento del juego.

Capturas de pantalla
----

* Pantalla principal

![alt img](https://github.com/svanegas/checkers/blob/master/screenshots/home.png)

* Iniciando una ronda de juego

![alt img](https://github.com/svanegas/checkers/blob/master/screenshots/new_game.png)

* Juego
![alt img](https://github.com/svanegas/checkers/blob/master/screenshots/gameplay.png)

* Mover una ficha
![alt img](https://github.com/svanegas/checkers/blob/master/screenshots/move.png)
