package damas;

import java.util.Vector;
import java.util.Scanner;

/**
 * Clase utilizada para el desarrollo del jugador.
 * @author Santiago Vanegas.
 */
public class Jugador {
    
    Tablero info;
    Vector hijos;
    
    public Jugador() {
        hijos = new Vector();
    }
    
    public Jugador(Tablero tablero) {
        info = tablero;
        hijos = new Vector();
    }
    
    public Jugador(Tablero tablero, Jugador hijo) {
        info = tablero;
        hijos = new Vector();
        hijos.add(hijo);
    }
    
    public boolean buscarHijo(Jugador hijo) {
        if(hijos.indexOf(hijo) != -1) return true;
        return false;
    }
    
    /**
     * Método que desarrolla el turno del jugador, el jugador tendrá diferentes opciones de movimiento,
     * si alguna pieza puede comer, deberá hacerlo obligatoriamente.
     * @param tablero tablero a partir del cual se deberá hacer la jugada.
     * @return una instancia de Tablero con la jugada que se efectuó.
     */
    
    public Tablero jugar(Tablero tablero){
        /*Vector piezasConCaptura = capturas();
        if(piezasConCaptura.size() > 0) {
            capturas = piezasConCaptura;
        }*/
        info = tablero.clonar();
        hijos.clear();
        Scanner in = new Scanner(System.in);
        Tablero jugada = tablero.clonar();
        System.out.println("El juego es el siguiente, escriba a donde desea moverse");
        jugada.imprimir();
        System.out.println("Las siguientes son las posibilidades de movimiento");
        
        boolean hayCapturada = false;
        Vector jugadasConCapturada = new Vector();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(jugada.tablero[i][j].tipo == 2 || jugada.tablero[i][j].tipo == 4){
                    Pieza pieza = new Pieza(jugada.tablero[i][j].tipo, new Posicion(i,j));
                    if(jugada.puedeComer(pieza)) {
                        hayCapturada = true;
                        hijos.clear();
                        Vector temp = jugada.posiblesMovimientos(pieza);
                        for(int k = 0; k < temp.size(); k++) {
                            Tablero tab = (Tablero) temp.get(k);
                            if(tab.getPiezasCapturadas() > 0) {
                                jugadasConCapturada.add(tab);
                            }
                        }
                    }
                    else if(!hayCapturada) {
                        Vector temp = jugada.posiblesMovimientos(pieza);
                        for(int k = 0; k < temp.size(); k++) {
                            hijos.add(temp.get(k));
                        }
                    }
                }
            }
        }
        
        /**
         * Si hay alguna pieza que pueda capturar, se debe hacer elección de únicamente las piezas que puedan hacerlo.
         */
        if(hayCapturada) {
            for(int i = 0; i < jugadasConCapturada.size(); i++) {
                System.out.println("Posibilidad Nº "+i);
                System.out.println("=====================================");
                Tablero temp = (Tablero) jugadasConCapturada.get(i);
                temp.imprimir();
                System.out.println("=====================================");
            }
            System.out.println("Seleccione la opción que desee");
            int rpta = in.nextInt();
            Tablero aJugar = (Tablero) jugadasConCapturada.get(rpta);
            aJugar.cambiarTurno(); //Cambia el turno pa que una vez llegado a la clase Juego vuelva a quedar pal jugador. (Si no da, quitar)
            return aJugar;
        }
        else {
            for(int i = 0; i < hijos.size(); i++) {
                System.out.println("Posibilidad Nº "+i);
                System.out.println("=====================================");
                Tablero temp = (Tablero) hijos.get(i);
                temp.imprimir();
                System.out.println("=====================================");
            }
            System.out.println("Seleccione la opción que desee");
            int rpta = in.nextInt();
            Tablero aJugar = (Tablero) hijos.get(rpta);
            return aJugar;
        }
        
    }
    
    public Vector capturas(Tablero tablero) {
        Vector capturas = new Vector();
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                boolean puedeComer = false;
                if(tablero.tablero[i][j].tipo == 2 || tablero.tablero[i][j].tipo == 4) {
                    puedeComer = tablero.puedeComer(new Pieza(tablero.tablero[i][j].tipo, new Posicion(i, j)));
                    if(puedeComer) {
                        capturas.add(new Pieza(tablero.tablero[i][j].tipo, new Posicion(i, j)));
                    }
                }
            }
        }
        return capturas;
    }
}
