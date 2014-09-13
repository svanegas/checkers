package damas;

import java.util.Vector;

/**
 * Clase que representa la inteligencia artificial (Máquina) a través del algorítmo
 * de búsqueda de árboles llamado Minimax, se realizará con un nivel máximo de busqueda
 * de posibilidades de 3.
 * @author Santiago Vanegas.
 */
public class MiniMax {
    Tablero info;
    Vector hijos;
    
    /**
     * Método constructor.
     * @param color Color de piezas de la máquina.
     */
    public MiniMax() {
        hijos = new Vector();
    }
    
    /**
     * Método constructor.
     * @param tablero Tablero de partida para analizar la jugada.
     * @param color Color de piezas de la máquina.
     */
    public MiniMax(Tablero tablero) {
        info = tablero;
        hijos = new Vector();
    }
    
    /**
     * Método constructor.
     * @param tablero Tablero de partida para analizar la jugada.
     * @param hijo Jugada posible, se agrega como hijo.
     * @param color Color de piezas de la máquina.
     */
    public MiniMax(Tablero tablero, MiniMax hijo) {
        info = tablero;
        hijos = new Vector();
        hijos.add(hijo);
    }
    
    /**
     * Método que maximiza la jugada de la máquina, siempre debe lograr buscar
     * la jugada que más convenga, a partir de la cantidad mayor de piezas 
     * contrincantes capturadas.
     * Tanto éste método como el Min son recursivos y trabajan a la par.
     * @param jugada Tablero el cual es la jugada inicial.
     * @return instancia de Tablero con la ficha movida.
     */
    public double Max(Tablero jugada) {
        if(jugada.getNivel() == 3) {
            jugada.cambiarTurno();
            jugada.actualizarValor();
            return jugada.valor;
        }
        double valor = Double.NEGATIVE_INFINITY;
        jugada.mejoresMovimientos();
        Vector tempHijos = jugada.getHijos(); //Cuando este listo probar asignando a info de parametro /\
        //hijos = jugada.getHijos();
        for(int i = 0; i < tempHijos.size(); i++) {
            Tablero siguiente = (Tablero) tempHijos.get(i);
            siguiente.cambiarTurno();
            valor = Math.max(valor, Min(siguiente));
        }
        jugada.valor = valor;
        return valor;
    }
    
    /**
     * Método que minimiza la jugada del oponente, siempre debe lograr buscar
     * una jugada en la que el oponente tenga la menor cantidad de fichas comidas.
     * Tanto éste método como el Max son recursivos y trabajan a la par.
     * @param jugada Tablero el cual es la jugada inicial.
     * @return instancia de Tablero con la ficha movida.
     */
    public double Min(Tablero jugada) {
        if(jugada.getNivel() == 3) {
            jugada.cambiarTurno();
            jugada.actualizarValor();
            return jugada.valor;
        }
        double valor = Double.POSITIVE_INFINITY;
        jugada.mejoresMovimientos();
        Vector tempHijos = jugada.getHijos(); //Cuando este listo probar asignando a info de parametro /\
        //hijos = jugada.getHijos();
        for(int i = 0; i < tempHijos.size(); i++) {
            Tablero siguiente = (Tablero) tempHijos.get(i);
            siguiente.cambiarTurno();
            valor = Math.min(valor, Max(siguiente));
        }
        jugada.valor = valor;
        return valor;
    }
    
    /**
     * Método que inicia el cálculo de posibilidades de la máquina a partir de un tablero de juego
     * se debe seleccionar las mejores jugadas (Maxmizadas) y por mayoría de piezas capturadas
     * se utiliza un random para escoger una de las clasificadas.
     * @param jugada Tablero inicial
     * @return Tablero final (Movimiento realizado)
     */
    public Tablero jugar(Tablero jugada) {
        Tablero aJugar = null;
        double valor = Max(jugada);
        hijos = jugada.getHijos(valor);
        if(hijos.size() > 0) {
            int random = (int)(Math.random()*hijos.size());
            aJugar = (Tablero)hijos.get(random);
        }
        info = aJugar;
        return info;
    }

    public boolean buscarHijo(MiniMax hijo) {
        if(hijos.indexOf(hijo) != -1) return true;
        return false;
    }
}
