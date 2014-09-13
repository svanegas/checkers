package damas;

/**
 * Clase que representa una pieza de juego, puede ser negra, roja, dama negra o dama roja.
 * @author Santiago Vanegas.
 */
public class Pieza {
       
    /** Atributo que hace referencia a la posición de la pieza en el tablero de juego. */
    private Posicion pos;
    /**Atributo que hace referencia a la posición anterior de la pieza en el tablero de juego. */
    private Posicion anterior;
    /** Atributo que hace referencia a si la pieza está capturada o no. */
    private boolean capturada;
    /** Atributo que hace referencia al tipo de pieza, (NEGRA(1), ROJA(2), DAMA NEGRA(3), DAMA ROJA(4)). */
    private int tipo;
    
   /**
    * Método constructor.
    * @param tipo Color de la pieza (Tipo) NEGRA(1), ROJA(2), DAMA NEGRA(3), DAMA ROJA(4).
    * @param pos Posición de la pieza instanciada.
    */
    public Pieza(int tipo, Posicion pos) {
       this.pos = pos;
       capturada = false;
       this.tipo = tipo;
    }

    /**
     * Método para guardar la posición en la anterior.
     */
    public void actualizarPos() {
        anterior = new Posicion(pos.posX, pos.posY);
    }
    
    public void actualizarPos(Posicion pos) {
        anterior = pos;
    }
    
    /**
     * @return La posición de la pieza.
     */
    public Posicion getPos() {
        return pos;
    }

    /**
     * @param pos La posición a poner a la pieza.
     */
    public void setPos(Posicion pos) {
        this.pos = pos;
    }

    /**
     * @return true si está capturada, falso de lo contrario.
     */
    public boolean isCapturada() {
        return capturada;
    }

    /**
     * @param capturada nuevo valor si la pieza está capturada o no.
     */
    public void setCapturada(boolean capturada) {
        this.capturada = capturada;
    }

    /**
     * @return un entero con el tipo de la pieza.
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * @param tipo nuevo tipo de la pieza.
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Posicion getAnterior() {
        return anterior;
    }

    public void setAnterior(Posicion anterior) {
        this.anterior = anterior;
    }
}
