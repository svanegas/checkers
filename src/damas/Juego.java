
package damas;

import java.util.Vector;

/**
 * Método que contiene el main, se desarrolla todo el juego, contiene una máquina, jugador y tablero.
 * @author Santiago Vanegas.
 */
public class Juego {
    
    /** Atributo que representa la máquina. */
    private MiniMax maquina;
    /** Atributo que representa el jugador. */
    private Jugador jugador;
    /** Atributo que representa el tablero de juego. */
    private Tablero tablero;
    /** Atributo que representa la interfaz gráfica. */
    private Interfaz interfaz;
    
    private int movimientos;
    
    public int numJugada;
    
    /**
     * Método constructor.
     */
    public Juego() {
        maquina = new MiniMax();
        jugador = new Jugador();
        tablero = new Tablero(1);
        //interfaz = new Interfaz();
        interfaz = new Interfaz(this);
        interfaz.inicializar();
        //interfaz.setJuego(this);
        numJugada = 1;
        //interfaz = new Interfaz(tablero);
    }
    
    /**
     * Método main, se instancia una objeto de la clase Juego y se llama al método jugar.
     */
    public static void main(String [] args) {
        Juego juego1 = new Juego();
        juego1.jugar();

        //new Interfaz();
    }
    
    /**
     * Método que realiza cada uno de los turnos de máquina y jugador.
     * Se invoca el método jugar en su respectivo momento, se cambia el turno,
     * se verifican cuando las piezas pueden ser damas.
     */
    public void jugar() {
        //tablero = new Tablero(1);
        tablero.inicializarTablero();
        //tablero.inicializarPersonalizado();
        while(true) {
            if(tablero.getTurno().turno == 1) {
                if(tablero.fichasTurno() == 0 || !puedeMover()) {
                    interfaz.finJuego("la máquina");
                    break;
                }
                Vector botonesABloquear = jugador.capturas(tablero);
                if(botonesABloquear.size() > 0) {
                    interfaz.bloquearBotones(botonesABloquear, tablero);
                }
                else {
                    interfaz.bloquearBotones(tablero);
                }

                tablero = interfaz.escucharJugada();
                
                if(tablero == null && interfaz.tablasOn) {
                    interfaz.finJuego("tablas");
                    break;
                }
                if(tablero == null && interfaz.rendirOn) {
                    interfaz.finJuego("rendir");
                    break;
                }
                
                if(tablero.getPiezasCapturadas() > 0) {
                    if(!tablero.puedeComer(tablero.getPiezaMovida())) {
                        tablero.cambiarTurno();
                        
                    }
                    interfaz.setMovimientos(0);
                }
                else {
                    tablero.cambiarTurno();
                    interfaz.setMovimientos(interfaz.getMovimientos()+1);
                }
                
                /*if(tablero.promoverPiezas()) {
                    interfaz.imprimirConsola(numJugada++, true); 
                }
                else {
                    interfaz.imprimirConsola(numJugada++, false); 
                }*/
                tablero.reset();
            }
            else if(tablero.getTurno().turno == 2) {
                if(tablero.fichasTurno() == 0 || !puedeMover()) {
                    interfaz.finJuego("el jugador");
                    break;
                }
                interfaz.bloquearBotones(tablero);
                tablero.evaluandoMinMax = true;
                tablero = maquina.jugar(tablero);
                tablero.evaluandoMinMax = false;
                
                if(tablero.getCapturadasPiezas().size() > 0) {
                    interfaz.setMovimientos(0);
                }
                else {
                    interfaz.setMovimientos(interfaz.getMovimientos()+1);
                }

                interfaz.setTablero(tablero);
                
                if(tablero.promoverPiezas()) {
                    tablero.cambiarTurno();
                    interfaz.setTablero(tablero);
                    interfaz.imprimirConsola(numJugada++, true); 
                    tablero.cambiarTurno();
                }
                else {
                    tablero.cambiarTurno();
                    interfaz.setTablero(tablero);
                    interfaz.imprimirConsola(numJugada++, false); 
                    tablero.cambiarTurno();
                }
                interfaz.pintarJugada(tablero);
                tablero.reset();
                //tablero.cambiarTurno();
            }
        } 
    }
    
    public void iniciarNuevo() {
        maquina = new MiniMax();
        jugador = new Jugador();
        tablero = new Tablero(1);
        interfaz.botonesNuevoJuego();
        this.jugar();
    }
    
    public boolean puedeMover() {
        Vector movimientos = new Vector();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(tablero.getTurno().turno == 1 && (tablero.tablero[i][j].tipo == 2 || tablero.tablero[i][j].tipo == 4)) {
                    Pieza temp = new Pieza(tablero.tablero[i][j].tipo, new Posicion(i,j));
                    if(tablero.posiblesMovimientos(temp).size() > 0) {
                        return true;
                    }
                }
                else if (tablero.getTurno().turno == 2 && (tablero.tablero[i][j].tipo == 1 || tablero.tablero[i][j].tipo == 3)) {
                    Pieza temp = new Pieza(tablero.tablero[i][j].tipo, new Posicion(i,j));
                    if(tablero.posiblesMovimientos(temp).size() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
}
