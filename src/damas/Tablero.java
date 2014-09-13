package damas;


import java.util.Vector;

/**
 * Clase que hace referencia al tablero de juego, puede llamarse tambien como
 * cada una de las probabilidades de movimiento en un respectivo turno de juego
 * el jugador y la máquina poseen su color de pieza, se construye un árbol
 * debido a que ésta clase contiene un vector de Tableros, los cuales son los hijos,
 * es decir, las probabilidades de movimiento a partir de cada instancia.
 * @author Santiago Vanegas.
 */
public class Tablero {
    
    /**
     * La enumeración Turno contiene 1 para el jugador y 2 para la máquina
     */
    enum Turno {
        JUGADOR(1), MÁQUINA(2);
        
        public int turno;
        
        Turno(int t) {
            turno = t;
        }
    }
    
    /**
     * La enumeración Casilla contiene 0 para representar vacío en el tablero,
     * 1 para piezas negras, 2 para rojas, 3 para damas negras y 4 para damas rojas.
     */
    enum Casilla {
        VACÍO(0), NEGRA(1), ROJA(2), DAMA_NEGRA(3), DAMA_ROJA(4);
        
        public int tipo;
        
        Casilla(int t) {
            tipo = t;
        }
    }
    
    /** Éste atributo representa el tablero de juego por medio de una matriz de 
     * tipo Casilla, debido a que el tablero puede contener espacios vacíos, damas o
     * piezas normales.
     */
    public Casilla [][] tablero;
    
    /**
     * Éste atributo representa la pieza que fue movida, siempre y cuando el tablero
     * no sea el inicial (Se utiliza desde el nivel 1 hasta el terminal).
     */
    private Pieza piezaMovida;
    
    /**
     * Éste atributo representa la cantidad de piezas capturadas en dicha jugada.
     */
    private int piezasCapturadas;
    
    /**
     * Éste atributo es un vector de tableros, el cual contiene los tableros ó jugadas
     * que son posibles capturas, (Éste atributo es usado en el método mejores movimientos.
     */
    private Vector tablerosCaptura = new Vector();
    
    /**
     * Éste atributo es un vector que va guardando las piezas que fueron capturadas.
     */
    private Vector capturadasPiezas = new Vector();
    
    public boolean evaluandoMinMax = false;
    /**
     * Éste atributo contiene la información del turno actual a partir de la enumeración.
     */
    private Turno turno;
    
    /**
     * Éste atributo contiene el valor del tablero ó jugada dependiendo la herística.
     */
    public double valor;
    
    /**
     * Éste atributo representa el nivel en el que se encuentra la jugada en el árbol.
     */
    private int nivel;
    
    /**
     * Éste atributo es un vector con las posibles jugadas que se generan
     * a partir de ésta, es decir, sus sucesores.
     */
    private Vector hijos;
    
    /**
     * Método constructor.
     */
    public Tablero() {
        tablero = new Casilla [8][8];
        turno = null;
        piezaMovida = null;
        hijos = new Vector();
        piezasCapturadas = 0;
        //tablerosCaptura = new Vector();
        //capturadasPiezas = new Vector();
    }
    
    /**
     * Método constructor.
     * @param turno representa de quién es el turno al iniciar un juego.
     */
    public Tablero(int turno) {
        tablero = new Casilla [8][8];
        if(turno == 1)
            this.turno = Turno.JUGADOR;
        else
            this.turno = Turno.MÁQUINA;
        //piezaMovida = null;
        piezasCapturadas = 0;
        //tablerosCaptura = new Vector();
        hijos = new Vector();
        //capturadasPiezas = new Vector();
    }
    
    public void inicializarPersonalizado() {
        int z = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                    tablero[i][j] = Casilla.VACÍO;
            }
        }
        tablero[2][1] = Casilla.DAMA_ROJA;
        tablero[3][2] = Casilla.NEGRA;
        tablero[5][4] = Casilla.NEGRA;
        tablero[5][6] = Casilla.NEGRA;
        tablero[3][6] = Casilla.NEGRA;
        tablero[7][0] = Casilla.NEGRA;
    }
    
    /**
     * Éste método inicializa la matriz que representa el tablero, siempre se
     * inicializa para un juego inicial común, las piezas negras van arriba.
     */
    public void inicializarTablero() {
        int z = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((i == 0 || i == 2) && (j%2 == 1)) {
                    tablero[i][j] = Casilla.NEGRA;
                    Posicion pos = new Posicion(i, j);
                }
                else if(i == 1 && j%2 == 0) {
                    tablero[i][j] = Casilla.NEGRA;
                    Posicion pos = new Posicion(i, j);
                }
                else if((i == 5 || i == 7) && (j%2 == 0)) {
                    tablero[i][j] = Casilla.ROJA;
                    Posicion pos = new Posicion(i, j);
                }
                else if((i == 6) && (j%2 == 1)) {
                    tablero[i][j] = Casilla.ROJA;
                    Posicion pos = new Posicion(i, j);
                }
                else {
                    tablero[i][j] = Casilla.VACÍO;
                }
            }
        }
    }
    
    /**
     * Método que calcula los posibles movimientos de cierta pieza en éste tablero
     * @param pieza pieza a calcular los movmientos
     * @return vector con los movimientos posibles por la pieza.
     */
    public Vector posiblesMovimientos(Pieza pieza) {
        Vector movimientos = new Vector();
        Posicion pos = pieza.getPos();
        if(pieza.getTipo() == 1) { //Si la pieza es negra
            int direccion = 1;
            for(int i = 0; i < 2; i++) {
                Posicion aMover = new Posicion(pos.posX + 1, pos.posY + direccion);
                if(aMover.esCorrecta()) {
                    if(tablero[aMover.posX][aMover.posY] == Casilla.VACÍO || tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                        Tablero temp;
                        temp = (Tablero) this.clonar();
                        temp.tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                        temp.tablero[aMover.posX][aMover.posY] = Casilla.NEGRA;
                        Pieza movida = new Pieza(temp.tablero[aMover.posX][aMover.posY].tipo, new Posicion(aMover.posX, aMover.posY));
                        temp.piezaMovida = movida;
                        temp.piezaMovida.actualizarPos(new Posicion(pos.posX, pos.posY)); //Pilas que esto lo agregué para que funcione la jugada que hace el jugador en jugador (Para la interfaz).
                        //temp.nivel = this.nivel + 1;
                        movimientos.add(temp);
                    }
                    /*if(tablero[aMover.posX][aMover.posY] == Casilla.ROJA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_ROJA) {
                        aMover.posX += 1;
                        aMover.posY += direccion;
                        if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                            Tablero temp;
                            temp = (Tablero) this.clonar();
                            temp.tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                            temp.tablero[pos.posX + 1][pos.posY + direccion] = Casilla.VACÍO;
                            temp.tablero[aMover.posX][aMover.posY] = Casilla.NEGRA;
                            Pieza movida = new Pieza(temp.tablero[aMover.posX][aMover.posY].tipo, new Posicion(aMover.posX, aMover.posY));
                            temp.piezaMovida = movida;
                            temp.piezaMovida.actualizarPos(new Posicion(pos.posX, pos.posY)); //Pilas que esto lo agregué para que funcione la jugada que hace el jugador en jugador (Para la interfaz).
                            //temp.nivel = this.nivel + 1;
                            temp.piezasCapturadas++;
                            temp.capturadasPiezas.add(new Pieza(tablero[pos.posX+1][pos.posY+direccion].tipo, new Posicion(pos.posX+1, pos.posY+direccion)));
                            movimientos.add(temp);
                        }
                    }*/
                }
                direccion = -1;
            }
            return movimientos;
        }
        if(pieza.getTipo() == 2) { //Si la pieza es roja
            int direccion = 1;
            for(int i = 0; i < 2; i++) {
                Posicion aMover = new Posicion(pos.posX - 1, pos.posY + direccion);
                if(aMover.esCorrecta()) {
                    if(tablero[aMover.posX][aMover.posY] == Casilla.VACÍO || tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                        Tablero temp;
                        temp = (Tablero) this.clonar();
                        temp.tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                        temp.tablero[aMover.posX][aMover.posY] = Casilla.ROJA;
                        Pieza movida = new Pieza(temp.tablero[aMover.posX][aMover.posY].tipo, new Posicion(aMover.posX, aMover.posY));
                        temp.piezaMovida = movida;
                        temp.piezaMovida.actualizarPos(new Posicion(pos.posX, pos.posY)); //Pilas que esto lo agregué para que funcione la jugada que hace el jugador en jugador (Para la interfaz).
                        //temp.nivel = this.nivel + 1;
                        movimientos.add(temp);
                    }
                    if((tablero[aMover.posX][aMover.posY] == Casilla.NEGRA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_NEGRA) && !evaluandoMinMax) {
                        aMover.posX -= 1;
                        aMover.posY += direccion;
                        if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                            Tablero temp;
                            temp = (Tablero) this.clonar();
                            temp.tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                            temp.tablero[pos.posX - 1][pos.posY + direccion] = Casilla.VACÍO;
                            temp.tablero[aMover.posX][aMover.posY] = Casilla.ROJA;
                            Pieza movida = new Pieza(temp.tablero[aMover.posX][aMover.posY].tipo, new Posicion(aMover.posX, aMover.posY));
                            temp.piezaMovida = movida;
                            temp.piezaMovida.actualizarPos(new Posicion(pos.posX, pos.posY)); //Pilas que esto lo agregué para que funcione la jugada que hace el jugador en jugador (Para la interfaz).
                            //temp.nivel = this.nivel + 1;
                            temp.piezasCapturadas++;
                            temp.capturadasPiezas.add(new Pieza(tablero[pos.posX-1][pos.posY+direccion].tipo, new Posicion(pos.posX-1, pos.posY+direccion)));
                            movimientos.add(temp);
                        }
                    }
                }
                direccion = -1;
            }
            return movimientos;
        }
        if(pieza.getTipo() == 3) { //Si la pieza es dama negra
            int direccion = 1;
            int dirX = 1;
            for(int i = 0; i < 4; i++) {
                Posicion aMover = new Posicion(pos.posX + dirX, pos.posY + direccion);
                if(aMover.esCorrecta()) {
                    if(tablero[aMover.posX][aMover.posY] == Casilla.VACÍO || tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                        Tablero temp;
                        temp = (Tablero) this.clonar();
                        temp.tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                        temp.tablero[aMover.posX][aMover.posY] = Casilla.DAMA_NEGRA;
                        Pieza movida = new Pieza(temp.tablero[aMover.posX][aMover.posY].tipo, new Posicion(aMover.posX, aMover.posY));
                        temp.piezaMovida = movida;
                        temp.piezaMovida.actualizarPos(new Posicion(pos.posX, pos.posY)); //Pilas que esto lo agregué para que funcione la jugada que hace el jugador en jugador (Para la interfaz).
                        //temp.nivel = this.nivel + 1;
                        movimientos.add(temp);
                    }
                    /*if(tablero[aMover.posX][aMover.posY] == Casilla.ROJA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_ROJA) {
                        aMover.posX += dirX;
                        aMover.posY += direccion;
                        if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                            Tablero temp;
                            temp = (Tablero) this.clonar();
                            temp.tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                            temp.tablero[pos.posX + dirX][pos.posY + direccion] = Casilla.VACÍO;
                            temp.tablero[aMover.posX][aMover.posY] = Casilla.DAMA_NEGRA;
                            Pieza movida = new Pieza(temp.tablero[aMover.posX][aMover.posY].tipo, new Posicion(aMover.posX, aMover.posY));
                            temp.piezaMovida = movida;
                            temp.piezaMovida.actualizarPos(new Posicion(pos.posX, pos.posY)); //Pilas que esto lo agregué para que funcione la jugada que hace el jugador en jugador (Para la interfaz).
                            //temp.nivel = this.nivel + 1;
                            temp.piezasCapturadas++;
                            temp.capturadasPiezas.add(new Pieza(tablero[pos.posX+dirX][pos.posY+direccion].tipo, new Posicion(pos.posX+dirX, pos.posY+direccion)));
                            movimientos.add(temp);
                        }
                    }*/
                }
                direccion = -1;
                if(i == 1) {
                    dirX = -1;
                    direccion = 1;
                }
            }
            return movimientos;
        }
        if(pieza.getTipo() == 4) { //Si la pieza es dama roja
            int direccion = 1;
            int dirX = 1;
            for(int i = 0; i < 4; i++) {
                Posicion aMover = new Posicion(pos.posX + dirX, pos.posY + direccion);
                if(aMover.esCorrecta()) {
                    if(tablero[aMover.posX][aMover.posY] == Casilla.VACÍO || tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                        Tablero temp;
                        temp = this.clonar();
                        temp.tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                        temp.tablero[aMover.posX][aMover.posY] = Casilla.DAMA_ROJA;
                        Pieza movida = new Pieza(temp.tablero[aMover.posX][aMover.posY].tipo, new Posicion(aMover.posX, aMover.posY));
                        temp.piezaMovida = movida;
                        temp.piezaMovida.actualizarPos(new Posicion(pos.posX, pos.posY)); //Pilas que esto lo agregué para que funcione la jugada que hace el jugador en jugador (Para la interfaz).
                        //temp.nivel = this.nivel + 1;
                        movimientos.add(temp);
                    }
                    if((tablero[aMover.posX][aMover.posY] == Casilla.NEGRA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_NEGRA) && !evaluandoMinMax) {
                        aMover.posX += dirX;
                        aMover.posY += direccion;
                        if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                            Tablero temp;
                            temp = this.clonar();
                            temp.tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                            temp.tablero[pos.posX + dirX][pos.posY + direccion] = Casilla.VACÍO;
                            temp.tablero[aMover.posX][aMover.posY] = Casilla.DAMA_ROJA;
                            Pieza movida = new Pieza(temp.tablero[aMover.posX][aMover.posY].tipo, new Posicion(aMover.posX, aMover.posY));
                            temp.piezaMovida = movida;
                            temp.piezaMovida.actualizarPos(new Posicion(pos.posX, pos.posY)); //Pilas que esto lo agregué para que funcione la jugada que hace el jugador en jugador (Para la interfaz).
                            //temp.nivel = this.nivel + 1;
                            temp.piezasCapturadas++;
                            temp.capturadasPiezas.add(new Pieza(tablero[pos.posX+dirX][pos.posY+direccion].tipo, new Posicion(pos.posX+dirX, pos.posY+direccion)));
                            movimientos.add(temp);
                        }
                    }
                }
                direccion = -1;
                if(i == 1) {
                    dirX = -1;
                    direccion = 1;
                }
            }
            return movimientos;
        }
        return movimientos;
    }
    
    /**
     * Método para calcular si una determinada ficha puede capturar a una contrincante o no.
     * @param pieza a determinar si puede capturar.
     * @return true si puede capturar, false de lo contrario.
     */
    public boolean puedeComer(Pieza pieza) {
        Posicion pos = pieza.getPos();
        if(pieza.getTipo() == 1) { //Si la pieza es negra
            int direccion = 1;
            for(int i = 0; i < 2; i++) {
                Posicion aMover = new Posicion(pos.posX + 1, pos.posY + direccion);
                if(aMover.esCorrecta()) {
                    if(tablero[aMover.posX][aMover.posY] == Casilla.ROJA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_ROJA) {
                        aMover.posX += 1;
                        aMover.posY += direccion;
                        if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                            return true;
                        }
                    }
                }
                direccion = -1;
            }
            return false;
        }
        if(pieza.getTipo() == 2) { //Si la pieza es roja
            int direccion = 1;
            for(int i = 0; i < 2; i++) {
                Posicion aMover = new Posicion(pos.posX - 1, pos.posY + direccion);
                if(aMover.esCorrecta()) {
                    if(tablero[aMover.posX][aMover.posY] == Casilla.NEGRA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_NEGRA) {
                        aMover.posX -= 1;
                        aMover.posY += direccion;
                        if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                            return true;
                        }
                    }
                }
                direccion = -1;
            }
            return false;
        }
        if(pieza.getTipo() == 3) { //Si la pieza es dama negra
            int direccion = 1;
            int dirX = 1;
            for(int i = 0; i < 4; i++) {
                Posicion aMover = new Posicion(pos.posX + dirX, pos.posY + direccion);
                if(aMover.esCorrecta()) {
                    if(tablero[aMover.posX][aMover.posY] == Casilla.ROJA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_ROJA) {
                        aMover.posX += dirX;
                        aMover.posY += direccion;
                        if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                            return true;
                        }
                    }
                }
                direccion = -1;
                if(i == 1) {
                    dirX = -1;
                    direccion = 1;
                }
            }
            return false;
        }
        if(pieza.getTipo() == 4) { //Si la pieza es dama roja
            int direccion = 1;
            int dirX = 1;
            for(int i = 0; i < 4; i++) {
                Posicion aMover = new Posicion(pos.posX + dirX, pos.posY + direccion);
                if(aMover.esCorrecta()) {
                    if(tablero[aMover.posX][aMover.posY] == Casilla.NEGRA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_NEGRA) {
                        aMover.posX += dirX;
                        aMover.posY += direccion;
                        if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                            return true;
                        }
                    }
                }
                direccion = -1;
                if(i == 1) {
                    dirX = -1;
                    direccion = 1;
                }
            }
            return false;
        }
        return false;
    }
    
    /**
     * Método para determinar si una captura es repetida, éste método es utilizado en 
     * calcularCapturas().
     * @param jugada Tablero el cual se comparará si está en los posibles tableros con captura.
     * @return true si está en los posibles tableros con captura, false de lo contrario.
     */
    public boolean capturaRepetida(Tablero jugada) {
        for(int i = 0; i < tablerosCaptura.size(); i++) {
            Tablero temp = (Tablero) tablerosCaptura.get(i);
            for(int j = 0; j < 8; j++) {
                for(int k = 0; k < 8; k++) {
                    if(temp.tablero[j][k].tipo != this.tablero[j][k].tipo) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * Método recursivo que determina la mayor cantidad de piezas que se pueden capturar
     * en una determinada jugada, se almacenarán en un vector y serán utilizadas finalmente
     * para determinar cuál jugada usar.
     * @param pieza pieza a determinar la mayor cantidad de capturas.
     * @param capturas cantidad total de capturas de la pieza, al invocarlo debe estar en 0.
     * @param capturadas vector de las piezas que han sido capturadas.
     */
    public void calcularCapturas(Pieza pieza, int capturas, Vector capturadas) {
        if(!puedeComer(pieza)) {
            Tablero jugada;
            jugada = this.clonar();
            if(capturas > piezasCapturadas) {
                piezasCapturadas = capturas;
                tablerosCaptura.clear();
                jugada.piezasCapturadas = capturas;
                jugada.capturadasPiezas  = (Vector) capturadas.clone();
                //jugada.piezaMovida = pieza; // SI NO DA HAY QUE CLONAR
                
                //Clonamos todo de la ficha.
                jugada.piezaMovida = new Pieza(pieza.getTipo(), pieza.getPos());
                jugada.piezaMovida.setAnterior(new Posicion(pieza.getPos().posX, pieza.getPos().posY));
                
                
                //jugada.piezaMovida.setAnterior(pieza.getAnterior()); //Lo agregué para que depronto de lo de la aneterior.
                tablerosCaptura.add(jugada);
            }
            else if(capturas != 0 && capturas == piezasCapturadas && !(capturaRepetida(jugada))) {
                jugada.capturadasPiezas = (Vector) capturadas.clone();
                //jugada.piezaMovida = pieza; // SI NO DA HAY QUE CLONAR
                
                //Clonamos todo de la ficha.
                jugada.piezaMovida = new Pieza(pieza.getTipo(), pieza.getPos());
                jugada.piezaMovida.setAnterior(new Posicion(pieza.getPos().posX, pieza.getPos().posY));
                
                
                
                //jugada.piezaMovida.setAnterior(pieza.getAnterior()); //Lo agregué para que depronto de lo de la aneterior.
                tablerosCaptura.add(jugada);
            }
        }
        else {
            Posicion pos = pieza.getPos();
                if(pieza.getTipo() == 1) { //Si la pieza es negra
                    int direccion = 1;
                    for(int j = 0; j < 2; j++) {
                        Posicion aMover = new Posicion(pos.posX + 1, pos.posY + direccion);
                        if(aMover.esCorrecta()) {
                            if(tablero[aMover.posX][aMover.posY] == Casilla.ROJA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_ROJA) {
                                aMover.posX += 1;
                                aMover.posY += direccion;
                                if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                                    Pieza victima = new Pieza(tablero[pos.posX +1][pos.posY + direccion].tipo, new Posicion(pos.posX + 1, pos.posY + direccion));
                                    victima.setAnterior(new Posicion(pos.posX+1, pos.posY+direccion));
                                    tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                                    tablero[pos.posX + 1][pos.posY + direccion] = Casilla.VACÍO;
                                    tablero[aMover.posX][aMover.posY] = Casilla.NEGRA;
                                    //pieza.setAnterior(new Posicion(pos.posX, pos.posY)); //Esto todavía nosé si agregarlo.
                                    pieza.setPos(aMover); //Esto se hace para que la ficha quede con la informacion de la posicion, o si no solo queda el tablero con dicha info.
                                    
                                    capturadas.add(victima);
                                    calcularCapturas(pieza, capturas + 1, capturadas);
                                    capturadas.remove(capturadas.size()-1); // No sé para qué sirve.
                                    
                                    tablero[pos.posX][pos.posY] = Casilla.NEGRA;
                                    if(victima.getTipo() == 2)
                                        tablero[pos.posX + 1][pos.posY + direccion] = Casilla.ROJA;
                                    else
                                        tablero[pos.posX + 1][pos.posY + direccion] = Casilla.DAMA_ROJA;
                                    tablero[aMover.posX][aMover.posY] = Casilla.VACÍO;
                                    pieza.setPos(pos);
                                }
                            }
                        }
                        direccion = -1;
                    }
                }
                else if(pieza.getTipo() == 2) { //Si la pieza es roja
                    int direccion = 1;
                    for(int j = 0; j < 2; j++) {
                        Posicion aMover = new Posicion(pos.posX - 1, pos.posY + direccion);
                        if(aMover.esCorrecta()) {
                            if(tablero[aMover.posX][aMover.posY] == Casilla.NEGRA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_NEGRA) {
                                aMover.posX -= 1;
                                aMover.posY += direccion;
                                if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                                    Pieza victima = new Pieza(tablero[pos.posX - 1][pos.posY + direccion].tipo, new Posicion(pos.posX - 1, pos.posY + direccion));
                                    victima.setAnterior(new Posicion(pos.posX-1, pos.posY+direccion));
                                    tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                                    tablero[pos.posX - 1][pos.posY + direccion] = Casilla.VACÍO;
                                    tablero[aMover.posX][aMover.posY] = Casilla.ROJA;
                                    //pieza.setAnterior(new Posicion(pos.posX, pos.posY)); //Esto todavía nosé si agregarlo.
                                    pieza.setPos(aMover);
                                    
                                    capturadas.add(victima);
                                    calcularCapturas(pieza, capturas + 1, capturadas);
                                    capturadas.remove(capturadas.size()-1); // No sé para qué sirve.
                                    
                                    tablero[pos.posX][pos.posY] = Casilla.ROJA;
                                    if(victima.getTipo() == 1)
                                        tablero[pos.posX - 1][pos.posY + direccion] = Casilla.NEGRA;
                                    else
                                        tablero[pos.posX - 1][pos.posY + direccion] = Casilla.DAMA_NEGRA;
                                    tablero[aMover.posX][aMover.posY] = Casilla.VACÍO;
                                    pieza.setPos(pos);
                                }
                            }
                        }
                        direccion = -1;
                    }
                }
                else if(pieza.getTipo() == 3) { //Si la pieza es dama negra
                    int direccion = 1;
                    int dirX = 1;
                    for(int j = 0; j < 4; j++) {
                        Posicion aMover = new Posicion(pos.posX + dirX, pos.posY + direccion);
                        if(aMover.esCorrecta()) {
                            if(tablero[aMover.posX][aMover.posY] == Casilla.ROJA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_ROJA) {
                                aMover.posX += dirX;
                                aMover.posY += direccion;
                                if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                                    Pieza victima = new Pieza(tablero[pos.posX + dirX][pos.posY + direccion].tipo, new Posicion(pos.posX + dirX, pos.posY + direccion));
                                    victima.setAnterior(new Posicion(pos.posX+dirX, pos.posY+direccion));
                                    tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                                    tablero[pos.posX + dirX][pos.posY + direccion] = Casilla.VACÍO;
                                    tablero[aMover.posX][aMover.posY] = Casilla.DAMA_NEGRA;
                                    //pieza.setAnterior(new Posicion(pos.posX, pos.posY)); //Esto todavía nosé si agregarlo.
                                    pieza.setPos(aMover);
                                    
                                    capturadas.add(victima);
                                    calcularCapturas(pieza, capturas + 1, capturadas);
                                    capturadas.remove(capturadas.size()-1); // No sé para qué sirve.

                                    tablero[pos.posX][pos.posY] = Casilla.DAMA_NEGRA;
                                    if(victima.getTipo() == 2)
                                        tablero[pos.posX + dirX][pos.posY + direccion] = Casilla.ROJA;
                                    else
                                        tablero[pos.posX + dirX][pos.posY + direccion] = Casilla.DAMA_ROJA;
                                    tablero[aMover.posX][aMover.posY] = Casilla.VACÍO;
                                    pieza.setPos(pos);
                                }
                            }
                        }
                        direccion = -1;
                        if(j == 1) {
                            dirX = -1;
                            direccion = 1;
                        }
                    }
                }
                else { //Si la pieza es dama roja
                    int direccion = 1;
                    int dirX = 1;
                    for(int j = 0; j < 4; j++) {
                        Posicion aMover = new Posicion(pos.posX + dirX, pos.posY + direccion);
                        if(aMover.esCorrecta()) {
                            if(tablero[aMover.posX][aMover.posY] == Casilla.NEGRA || tablero[aMover.posX][aMover.posY] == Casilla.DAMA_NEGRA) {
                                aMover.posX += dirX;
                                aMover.posY += direccion;
                                if(aMover.esCorrecta() && tablero[aMover.posX][aMover.posY] == Casilla.VACÍO) {
                                    Pieza victima = new Pieza(tablero[pos.posX + dirX][pos.posY + direccion].tipo, new Posicion(pos.posX + dirX, pos.posY + direccion));
                                    victima.setAnterior(new Posicion(pos.posX + dirX, pos.posY + direccion));
                                    tablero[pos.posX][pos.posY] = Casilla.VACÍO;
                                    tablero[pos.posX + dirX][pos.posY + direccion] = Casilla.VACÍO;
                                    tablero[aMover.posX][aMover.posY] = Casilla.DAMA_ROJA;
                                    //pieza.setAnterior(new Posicion(pos.posX, pos.posY)); //Esto todavía nosé si agregarlo.
                                    pieza.setPos(aMover);
                                    
                                    capturadas.add(victima);
                                    calcularCapturas(pieza, capturas + 1, capturadas);
                                    capturadas.remove(capturadas.size()-1); // No sé para qué sirve.
                                    
                                    tablero[pos.posX][pos.posY] = Casilla.DAMA_ROJA;
                                    if(victima.getTipo() == 1)
                                        tablero[pos.posX + dirX][pos.posY + direccion] = Casilla.NEGRA;
                                    else
                                        tablero[pos.posX + dirX][pos.posY + direccion] = Casilla.DAMA_NEGRA;
                                    tablero[aMover.posX][aMover.posY] = Casilla.VACÍO;
                                    pieza.setPos(pos);
                                }
                            }
                        }
                        direccion = -1;
                        if(j == 1) {
                            dirX = -1;
                            direccion = 1;
                        }
                    }
                }
            }
    }
    
    /**
     * Método que determina cuáles son los mejores movimientos que se pueden realizar
     * a partir de éste tablero, los almacena en el atributo hijos.
     */
    public void mejoresMovimientos() {
        boolean hayCapturada = false;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(turno == Turno.JUGADOR && (tablero[i][j] == Casilla.ROJA || tablero[i][j] == Casilla.DAMA_ROJA)) {
                    Posicion pos = new Posicion(i,j);
                    Pieza pieza = new Pieza(tablero[i][j].tipo, pos);
                    Vector movimientos;
                    
                    Posicion anteriorPos = pieza.getPos();
                    pieza.setAnterior(new Posicion(anteriorPos.posX, anteriorPos.posY));
                    
                    piezasCapturadas = 0;
                    tablerosCaptura.clear();
                    
                    if(puedeComer(pieza)) {
                        hayCapturada = true;
                        hijos.clear();
                        calcularCapturas(pieza, 0, new Vector());
                        for(int k = 0; k < tablerosCaptura.size(); k++) {
                            Tablero temp = (Tablero) tablerosCaptura.get(k);
                            temp.setNivel(this.nivel + 1);
                            
                            //Lo siguiente lo agregue para ver si da
                            Pieza movida = temp.getPiezaMovida();
                            movida.actualizarPos(anteriorPos);
                            temp.setPiezaMovida(movida);
                            //-------------------------------
                            
                            hijos.add(temp);
                        }
                        
                    }
                    else if (!hayCapturada){
                        movimientos = posiblesMovimientos(pieza);
                        for(int k = 0; k < movimientos.size(); k++) {
                            Tablero temp = (Tablero) movimientos.get(k);
                            temp.setNivel(this.nivel + 1);
                            //El nivel antes estaba agregado en posibles movimientos, lo pasé para acá.
                            hijos.add(temp);
                        }
                    }
                    
                }
                else if(turno == Turno.MÁQUINA && (tablero[i][j] == Casilla.NEGRA || tablero[i][j] == Casilla.DAMA_NEGRA)) {
                    Posicion pos = new Posicion(i,j);
                    Pieza pieza = new Pieza(tablero[i][j].tipo, pos);
                    Vector movimientos;
                    
                    Posicion anteriorPos = pieza.getPos();
                    pieza.setAnterior(new Posicion(anteriorPos.posX, anteriorPos.posY));
                    
                    piezasCapturadas = 0;
                    tablerosCaptura.clear();
                    
                    if(puedeComer(pieza)) {
                        hayCapturada = true;
                        hijos.clear();
                        calcularCapturas(pieza, 0, new Vector());
                        for(int k = 0; k < tablerosCaptura.size(); k++) {
                            Tablero temp = (Tablero) tablerosCaptura.get(k);
                            temp.setNivel(this.nivel + 1); 
                            
                            //Lo siguiente lo agregue para ver si da
                            Pieza movida = temp.getPiezaMovida();
                            movida.actualizarPos(anteriorPos);
                            temp.setPiezaMovida(movida);
                            //-------------------------------
                            
                            hijos.add(temp);
                        }
                        
                    }
                    else if (!hayCapturada){
                        movimientos = posiblesMovimientos(pieza);
                        for(int k = 0; k < movimientos.size(); k++) {
                            //Quitar agregar nivel en metodo posiblesMovimientos queda pendiente.
                            Tablero temp = (Tablero) movimientos.get(k);
                            temp.setNivel(this.nivel + 1);
                            hijos.add(temp);
                        }
                    }
                    
                }
            }
        }
    }
    
    /**
     * Método par aimprimir en consola el tablero.
     */
    public void imprimir() {
        System.out.println("    | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |");
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(tablero[i][j] == Casilla.NEGRA) {
                    if(j == 7) {
                        System.out.println("| N |");
                        System.out.println("-------------------------------------");
                    }
                    else if(j == 0)
                        System.out.print("| "+i+" "+"| N ");
                    else
                        System.out.print("| N ");
                }
                else if(tablero[i][j] == Casilla.DAMA_NEGRA) {
                    if(j == 7) {
                        System.out.println("| M |");
                        System.out.println("-------------------------------------");
                    }
                    else if(j == 0)
                        System.out.print("| "+i+" "+"| M ");
                    else 
                        System.out.print("| M ");
                }
                else if(tablero[i][j] == Casilla.ROJA) {
                    if(j == 7) {
                        System.out.println("| R |");
                        System.out.println("-------------------------------------");
                    }
                    else if(j == 0)
                        System.out.print("| "+i+" "+"| R ");
                    else 
                        System.out.print("| R ");
                }
                else if(tablero[i][j] == Casilla.DAMA_ROJA) {
                    if(j == 7) {
                        System.out.println("| F |");
                        System.out.println("-------------------------------------");
                    }
                    else if(j == 0)
                        System.out.print("| "+i+" "+"| F ");
                    else 
                        System.out.print("| F ");
                }
                else {
                    if(j == 7) {
                        System.out.println("|   |");
                        System.out.println("-------------------------------------");
                    }
                    else if(j == 0)
                        System.out.print("| "+i+" "+"|   ");
                    else 
                        System.out.print("|   ");
                }
            }
        }
    }
    
    /**
     * Método que se ejecuta cada vez que se cambia de turno, para comprobar si
     * hay piezas que pueden ser damas.
     */
    public boolean promoverPiezas() {
        boolean prom = false;
        for(int i = 0; i < 8; i++) {
            if(tablero[0][i] == Casilla.ROJA) {
                tablero[0][i] = Casilla.DAMA_ROJA;
                prom = true;
            }
            if(tablero[7][i] == Casilla.NEGRA) {
                tablero[7][i] = Casilla.DAMA_NEGRA;
                prom = true;
            }
        }
        return prom;
    }
    
    /**
     * Método que cambia el turno de juego.
     */
    public void cambiarTurno() {
        if(turno == Turno.JUGADOR) turno = Turno.MÁQUINA;
        else turno = Turno.JUGADOR;
    }
    
    /**
     * Método que actualiza el valor del tablero o jugada dependiendo de la heurísitca,
     * cada posición en un tablero tiene un peso, y cada ficha posee un valor.
     */
    public void actualizarValor() {
        double suma = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Posicion pos = new Posicion(i,j);
                if(pos.esCorrecta() && this.tablero[i][j] != Casilla.VACÍO) {
                    if(turno == Turno.JUGADOR && (this.tablero[i][j].tipo == 2 || this.tablero[i][j].tipo == 4)) {
                        int valorPieza;
                        if(this.tablero[i][j].tipo == 4) {
                            valorPieza = 3;
                        }
                        else {
                            valorPieza = 1;
                        }
                        suma = suma + valorPieza * pos.valor();
                    }
                    if(turno == Turno.MÁQUINA && (this.tablero[i][j].tipo == 1 || this.tablero[i][j].tipo == 3)) {
                        int valorPieza;
                        if(this.tablero[i][j].tipo == 3) {
                            valorPieza = 3;
                        }
                        else {
                            valorPieza = 1;
                        }
                        suma = suma + valorPieza * pos.valor();
                    }
                }
            }
        }
        valor = (fichasTurno() * suma) + suma;
    }
    
    /**
     * Método que retorna la cantidad de fichas que hay en juego en el tablero
     * del jugador o máquina (De quien sea el turno)
     * @return un entero que dice la cantidad de fichas.
     */
    public int fichasTurno() {
        int cont = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                Posicion pos = new Posicion(i,j);
                if(turno == Turno.JUGADOR) {
                    if(pos.esCorrecta() && (tablero[i][j].tipo == 2 || tablero[i][j].tipo == 4)) {
                        cont++;
                    }
                }
                if(turno == Turno.MÁQUINA) {
                    if(pos.esCorrecta() && (tablero[i][j].tipo == 1 || tablero[i][j].tipo == 3)) {
                        cont++;
                    }
                }
            }
        }
        return cont;
    }
    
    /**
     * Método que crea una instancia de ésta clase y clona la jugada, retornándola así.
     * @return tablero nuevo, totalmente igual al principal.
     */
    public Tablero clonar() {
        Tablero nuevo = new Tablero();
        //nuevo.setNivel(this.getNivel());
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                nuevo.tablero[i][j] = this.tablero[i][j];
            }
        }
        //nuevo.setTurno(this.getTurno());
        return nuevo;  
    }
    
    /**
     * Método que pone valores de atributos por defecto.
     */
    public void reset() {
        nivel = 0;
        hijos.clear();
        capturadasPiezas.clear();
        piezasCapturadas = 0;
        tablerosCaptura.clear();
        piezaMovida = null;
        valor = 0;
    }
    
    public void modificarTabla(Posicion pos, int tipo) {
        if(tipo == 1) {
            tablero[pos.posX][pos.posY] = Casilla.NEGRA;
        }
        else if(tipo == 2) {
            tablero[pos.posX][pos.posY] = Casilla.ROJA;
        }
        else if(tipo == 3) {
            tablero[pos.posX][pos.posY] = Casilla.DAMA_NEGRA;
        }
        else if(tipo == 4) {
            tablero[pos.posX][pos.posY] = Casilla.DAMA_ROJA;
        }
        else {
            tablero[pos.posX][pos.posY] = Casilla.VACÍO;
        }
    }
    
    public int getNivel() {
        return nivel;
    }

    public Turno getTurno() {
        return turno;
    }

    public int getPiezasCapturadas() {
        return piezasCapturadas;
    }
    
    public Vector getHijos() {
        return hijos;
    }
    
    public Vector getHijos(double valor) {
        Vector tempHijos = new Vector();
        for(int i = 0; i < hijos.size(); i++) {
            Tablero temp = (Tablero) hijos.get(i);
            if(temp.valor == valor) {
                tempHijos.add(temp);
            }
        }
        return tempHijos;
    }
    
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Pieza getPiezaMovida() {
        return piezaMovida;
    }

    public void setPiezaMovida(Pieza piezaMovida) {
        this.piezaMovida = piezaMovida;
    }

    public Vector getCapturadasPiezas() {
        return capturadasPiezas;
    }

    public void setCapturadasPiezas(Vector capturadasPiezas) {
        this.capturadasPiezas = capturadasPiezas;
    }

    public void setPiezasCapturadas(int piezasCapturadas) {
        this.piezasCapturadas = piezasCapturadas;
    }  
}
