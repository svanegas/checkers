package damas;

/**
 * Clase para trabajar con mayor facilidad el aspecto de la posición de una ficha en el tablero de juego.
 * @author Santiago Vanegas.
 */
public class Posicion {
    
    /** Representa la coordenada en X. */
    public int posX;
    /** Representa la coordenada en Y. */
    public int posY;
    
    /**
     * Método constructor.
     * @param x Coordenada en X.
     * @param y Coordenada en Y.
     */
    public Posicion(int x, int y) {
        posX = x;
        posY = y;
    }
    
    /**
     * Método para determinar si una posición dada es correcta, es decir, si 
     * la posición no excede los límites del tablero y si hace referencia a una
     * casilla en la que se pueda jugar. 
     * @return true en caso de que sea válida, false de lo contrario.
     */
    public boolean esCorrecta() {
        if(posX < 0 || posY < 0 || posX > 7 || posY > 7) return false;
        if((posX + posY)%2 == 0) return false;
        return true;
    }
    
    /**
     * Método para determinar el valor de una determinada posición a partir de
     * la heurística, si la posición está más externa tendrá más valor.
     * @return Un entero con el valor de la posición.
     */
    public int valor() {
        if(posX == 0 || posY == 0 || posX == 7 || posY == 7)
            return 4;
        else if(posX == 1 || posY == 1 || posX == 6 || posY == 6)
            return 3;
        else if(posX == 2 || posY == 2 || posX == 5 || posY == 5)
            return 2;
        else if(posX == 3 || posY == 3 || posX == 4 || posY == 4)
            return 1;
        else
            return 0;
    }
}
