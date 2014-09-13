package damas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 *
 * @author svanega4
 */
public class Interfaz extends JFrame {

    private JLabel fondo;
    private JLabel marco;
    
    private JLabel titulo;
    private JButton[][] casilla;
    private JLabel[] filas;
    private JLabel[] columnas;
    private JLabel bug;
    
    private JRadioButton boton;
    private JTextArea consola;
    private JScrollPane barra;
    private boolean oculta;
    
    private Tablero tablero;
    
    private boolean rojaPresionada;
    private boolean rojaMovida;
    private Pieza piezaPresionada;
    
    private Vector vectorCapturas;
    
    private JButton tablas;
    private int movimientos;
    public boolean tablasOn;
    
    private JButton rendirse;
    public boolean rendirOn;
    
    private Juego juego;
    private JButton nuevoJuego;
    private boolean nuevoJuegoOn;
    
    private JButton salir;
    private boolean salirOn;
    


    public Interfaz() {
        inicializar();
    }
    
    public Interfaz(Juego juego) {
        this.juego = juego;
        //inicializar();
    } 
    
    public Interfaz(Tablero tablero) {
        tablero = new Tablero(1);
        this.tablero = tablero;
        tablero.inicializarPersonalizado();
        inicializarPersonalizado();
    }

    public void inicializar() {

        titulo = new JLabel();
        titulo.setOpaque(true);
        titulo.setForeground(Color.red);
        titulo.setBounds(140, 25, 300, 60);
        titulo.setIcon(new ImageIcon(getClass().getResource("/recursos/titulo.png")));
        titulo.setBackground(Color.red);
        getContentPane().add(titulo);
        
        consola = new JTextArea();
        consola.setEditable(false);
        consola.setFont(new Font("Lucida Console", Font.PLAIN, 11));
        barra = new JScrollPane(consola);
        
        barra.setBounds(540, 570, 340, 60);
        oculta = true;
        barra.setLocation(1000, 1000);
        getContentPane().add(barra); 
        
        vectorCapturas = new Vector();     
        
        boton = new JRadioButton("Consola");
        boton.setBounds(660, 590, 80, 20);
        boton.setFont(new Font("Lucida Console", Font.PLAIN, 11));
        boton.setForeground(Color.WHITE);
        boton.setOpaque(false);
        boton.setSelected(false);
        boton.setFocusPainted(false);
        boton.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            //complemento.moverConsola(boton);
                            moverConsola();
                        }
        });
        getContentPane().add(boton);
        
        tablas = new JButton();
        tablas.setBounds(660, 200, 150, 50);
        tablas.setIcon(new ImageIcon(getClass().getResource("/recursos/tablas.png")));
        tablas.setOpaque(false);
        tablas.setSelected(false);
        tablas.setFocusPainted(false);
        tablas.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            int confirm = 1;
                            confirm = JOptionPane.showOptionDialog(null, "¿Desea ofrecer tablas?", "Tablas", 2, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "Cancelar"}, null);
                            if(confirm == 0) {
                                if(tablas()) {
                                    tablasOn = true;
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "La máquina no ha aceptado el empate.");
                                }
                            }
                        }
        });
        getContentPane().add(tablas);
        
        rendirse = new JButton();
        rendirse.setBounds(660, 300, 150, 50);
        rendirse.setIcon(new ImageIcon(getClass().getResource("/recursos/rendirse.png")));
        rendirse.setOpaque(false);
        rendirse.setSelected(false);
        rendirse.setFocusPainted(false);
        rendirse.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            int confirm = 1;
                            confirm = JOptionPane.showOptionDialog(null, "¿Estás seguro?", "Rendirse", 2, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "Cancelar"}, null);
                            if(confirm == 0) {
                                rendirOn = true;
                            }
                        }
        });
        getContentPane().add(rendirse);
        
        nuevoJuego = new JButton();
        nuevoJuego.setBounds(660, 100, 150, 50);
        nuevoJuego.setIcon(new ImageIcon(getClass().getResource("/recursos/nuevo.png")));
        nuevoJuego.setOpaque(false);
        nuevoJuego.setSelected(false);
        nuevoJuego.setEnabled(false);
        nuevoJuego.setVisible(false);
        nuevoJuego.setFocusPainted(false);
        nuevoJuego.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            nuevoJuegoOn = true;
                        }
        });
        getContentPane().add(nuevoJuego);
        
        salir = new JButton();
        salir.setBounds(660, 200, 150, 50);
        salir.setIcon(new ImageIcon(getClass().getResource("/recursos/salir.png")));
        salir.setOpaque(false);
        salir.setSelected(false);
        salir.setEnabled(false);
        salir.setVisible(false);
        salir.setFocusPainted(false);
        salir.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            salirOn = true;
                        }
        });
        getContentPane().add(salir);

        filas = new JLabel[8];
        for(int i = 0; i < 8; i++) {
            String s = convertirFila(""+(i), false);
            filas[i] = new JLabel(s);
            filas[i].setBounds(10, 132+(60*i), 30, 30);
            filas[i].setFont(new Font("Verdana", Font.BOLD, 14));
            filas[i].setForeground(Color.WHITE);
            getContentPane().add(filas[i]);
        }
        
        columnas = new JLabel[8];
        for(int i = 0; i < 8; i++) {
            String s = convertirFila(""+(i), true);
            columnas[i] = new JLabel(s);
            columnas[i].setBounds(75+(60*i), 620, 30, 30);
            columnas[i].setFont(new Font("Verdana", Font.BOLD, 14));
            columnas[i].setForeground(Color.WHITE);
            getContentPane().add(columnas[i]);
        }
        
        casilla = new JButton[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((i == 0 || i == 2) && (j%2 == 1)) {
                    casilla[i][j] = new JButton();
                    casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
                    getContentPane().add(casilla[i][j]);
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
                    final JButton temp = casilla[i][j];
                    final Posicion pos = new Posicion(i,j);
                    casilla[i][j].addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                System.out.println("Presioné una negra");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                if(!rojaPresionada) {
                                    rojaPresionada = true;
                                    temp.setEnabled(true);
                                    piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                    piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                    pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                }
                                else {
                                    rojaPresionada = false;
                                    try {
                                        if(vectorCapturas.size() > 0) {
                                            bloquearBotones(vectorCapturas, tablero);
                                        }
                                        else {
                                            desbloquearBotones();
                                        }
                                    }
                                    catch (Exception ex) {
                                        System.out.println("Error: "+ex.getMessage());
                                    }
                                }
                            }
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                System.out.println("Presioné un espacio blanco");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                if(rojaPresionada) {
                                    if(vectorCapturas.size() > 0) {
                                        Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                        Vector moves = tablero.posiblesMovimientos(capturadora);
                                        for(int i = 0; i < moves.size(); i++) {
                                            Tablero temp = (Tablero) moves.get(i);
                                            if(temp.getCapturadasPiezas().size() > 0) {
                                                Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                            }
                                        }
                                        tablero.setPiezasCapturadas(1);
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                    else {
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                }
                            }
                        }
                    });

                }
                else if(i == 1 && j%2 == 0) {
                    casilla[i][j] = new JButton();
                    casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    getContentPane().add(casilla[i][j]);
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
                    final JButton temp = casilla[i][j];
                    final Posicion pos = new Posicion(i,j);
                    casilla[i][j].addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                System.out.println("Presioné una negra");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                if(!rojaPresionada) {
                                    rojaPresionada = true;
                                    temp.setEnabled(true);
                                    piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                    piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                    pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                }
                                else {
                                    rojaPresionada = false;
                                    try {
                                        if(vectorCapturas.size() > 0) {
                                            bloquearBotones(vectorCapturas, tablero);
                                        }
                                        else {
                                            desbloquearBotones();
                                        }
                                    }
                                    catch (Exception ex) {
                                        System.out.println("Error: "+ex.getMessage());
                                    }
                                }
                            }
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                System.out.println("Presioné un espacio blanco");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                if(rojaPresionada) {
                                    if(vectorCapturas.size() > 0) {
                                        Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                        Vector moves = tablero.posiblesMovimientos(capturadora);
                                        for(int i = 0; i < moves.size(); i++) {
                                            Tablero temp = (Tablero) moves.get(i);
                                            if(temp.getCapturadasPiezas().size() > 0) {
                                                Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                            }
                                        }
                                        tablero.setPiezasCapturadas(1);
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                    else {
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                }
                            }
                        }
                    });

                }
                else if((i == 5 || i == 7) && (j%2 == 0)) {
                    casilla[i][j] = new JButton();
                    casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    getContentPane().add(casilla[i][j]);
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
                    final JButton temp = casilla[i][j];
                    final Posicion pos = new Posicion(i,j);
                    casilla[i][j].addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                System.out.println("Presioné una negra");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                if(!rojaPresionada) {
                                    rojaPresionada = true;
                                    temp.setEnabled(true);
                                    piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                    piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                    pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                }
                                else {
                                    rojaPresionada = false;
                                    try {
                                        if(vectorCapturas.size() > 0) {
                                            bloquearBotones(vectorCapturas, tablero);
                                        }
                                        else {
                                            desbloquearBotones();
                                        }
                                    }
                                    catch (Exception ex) {
                                        System.out.println("Error: "+ex.getMessage());
                                    }

                                }
                            }
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                System.out.println("Presioné un espacio blanco");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                if(rojaPresionada) {
                                    if(vectorCapturas.size() > 0) {
                                        Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                        Vector moves = tablero.posiblesMovimientos(capturadora);
                                        for(int i = 0; i < moves.size(); i++) {
                                            Tablero temp = (Tablero) moves.get(i);
                                            if(temp.getCapturadasPiezas().size() > 0) {
                                                Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                            }
                                        }
                                        tablero.setPiezasCapturadas(1);
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                    else {
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                }
                            }
                        }
                    });

                }
                else if((i == 6) && (j%2 == 1)) {
                    casilla[i][j] = new JButton();
                    casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    getContentPane().add(casilla[i][j]);
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
                    final JButton temp = casilla[i][j];
                    final Posicion pos = new Posicion(i,j);
                    casilla[i][j].addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                System.out.println("Presioné una negra");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                if(!rojaPresionada) {
                                    rojaPresionada = true;
                                    temp.setEnabled(true);
                                    piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                    piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                    pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                }
                                else {
                                    rojaPresionada = false;
                                    try {
                                        if(vectorCapturas.size() > 0) {
                                            bloquearBotones(vectorCapturas, tablero);
                                        }
                                        else {
                                            desbloquearBotones();
                                        }
                                    }
                                    catch (Exception ex) {
                                        System.out.println("Error: "+ex.getMessage());
                                    }
                                }
                            }
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                System.out.println("Presioné un espacio blanco");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                if(rojaPresionada) {
                                    if(vectorCapturas.size() > 0) {
                                        Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                        Vector moves = tablero.posiblesMovimientos(capturadora);
                                        for(int i = 0; i < moves.size(); i++) {
                                            Tablero temp = (Tablero) moves.get(i);
                                            if(temp.getCapturadasPiezas().size() > 0) {
                                                Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                            }
                                        }
                                        tablero.setPiezasCapturadas(1);
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                    else {
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                }
                            }
                        }
                    });
                }
                else {
                    if((i+j)%2 == 0) {
                        casilla[i][j] = new JButton();
                        casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                        casilla[i][j].setRolloverEnabled(false);
                        casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")));
                        casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        getContentPane().add(casilla[i][j]);
                        casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")));
                        final JButton temp = casilla[i][j];
                        final Posicion pos = new Posicion(i,j);
                        casilla[i][j].addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                                if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                    System.out.println("Presioné una negra");
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                    if(!rojaPresionada) {
                                        rojaPresionada = true;
                                        temp.setEnabled(true);
                                        piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                        piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                        pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                    }
                                    else {
                                        rojaPresionada = false;
                                        try {
                                            if(vectorCapturas.size() > 0) {
                                                bloquearBotones(vectorCapturas, tablero);
                                            }
                                            else {
                                                desbloquearBotones();
                                            }
                                        }
                                        catch (Exception ex) {
                                            System.out.println("Error: "+ex.getMessage());
                                        }
                                    }
                                }
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                    System.out.println("Presioné un espacio blanco");
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                    if(rojaPresionada) {
                                        if(vectorCapturas.size() > 0) {
                                            Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                            Vector moves = tablero.posiblesMovimientos(capturadora);
                                            for(int i = 0; i < moves.size(); i++) {
                                                Tablero temp = (Tablero) moves.get(i);
                                                if(temp.getCapturadasPiezas().size() > 0) {
                                                    Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                    tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                                }
                                            }
                                            tablero.setPiezasCapturadas(1);
                                            piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                            tablero.setPiezaMovida(piezaPresionada);
                                            rojaMovida = true;
                                        }
                                        else {
                                            piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                            tablero.setPiezaMovida(piezaPresionada);
                                            rojaMovida = true;
                                        }
                                    }
                                }
                            }
                        });

                    }
                    else {
                        casilla[i][j] = new JButton();
                        casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                        casilla[i][j].setRolloverEnabled(false);
                        casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuro.png")));
                        casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        getContentPane().add(casilla[i][j]);
                        casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")));
                        final JButton temp = casilla[i][j];
                        final Posicion pos = new Posicion(i,j);
                        casilla[i][j].addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                                if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                    System.out.println("Presioné una negra");
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                    if(!rojaPresionada) {
                                        rojaPresionada = true;
                                        //bloquearBotones(tablero);
                                        temp.setEnabled(true);
                                        piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                        piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                        pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                    }
                                    else {
                                        rojaPresionada = false;
                                        try {
                                            if(vectorCapturas.size() > 0) {
                                                bloquearBotones(vectorCapturas, tablero);
                                            }
                                            else {
                                                desbloquearBotones();
                                            }
                                        }
                                        catch (Exception ex) {
                                            System.out.println("Error: "+ex.getMessage());
                                        }
                                    }
                                }
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                    System.out.println("Presioné un espacio blanco");
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                    if(rojaPresionada) {
                                        if(vectorCapturas.size() > 0) {
                                            Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                            Vector moves = tablero.posiblesMovimientos(capturadora);
                                            for(int i = 0; i < moves.size(); i++) {
                                                Tablero temp = (Tablero) moves.get(i);
                                                if(temp.getCapturadasPiezas().size() > 0) {
                                                    Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                    tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                                }
                                            }
                                            tablero.setPiezasCapturadas(1);
                                            piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                            tablero.setPiezaMovida(piezaPresionada);
                                            rojaMovida = true;
                                        }
                                        else {
                                            piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                            tablero.setPiezaMovida(piezaPresionada);
                                            rojaMovida = true;
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
        rojaMovida = false;
        piezaPresionada = new Pieza(0, null);
        
        marco = new JLabel();
        marco.setBounds(30, 100, 520, 520);
        marco.setIcon(new ImageIcon(getClass().getResource("/recursos/marco.png")));
        getContentPane().add(marco);

        fondo = new JLabel();
        fondo.setIcon(new ImageIcon(getClass().getResource("/recursos/fondo.jpg")));
        getContentPane().add(fondo);
        

        setTitle("Damas");
        setSize(900, 700);
        setLocationRelativeTo(null);
        Image icono = new ImageIcon(getClass().getResource("/recursos/icono.png")).getImage();
        setIconImage(icono);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        
        organizarTerminado();
    }
    
    public void inicializarPersonalizado() {
        titulo = new JLabel("HOLAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        titulo.setOpaque(true);
        titulo.setForeground(Color.red);
        titulo.setBounds(150, 25, 600, 100);
        titulo.setBackground(Color.red);
        getContentPane().add(titulo);

        vectorCapturas = new Vector();
        
        casilla = new JButton[8][8];
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if(tablero.tablero[i][j].tipo == 1) { //Si la casilla hay una negra.
                    casilla[i][j] = new JButton();
                    casilla[i][j].setBounds(20+(60*j), 150+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    getContentPane().add(casilla[i][j]);
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
                    final JButton temp = casilla[i][j];
                    final Posicion pos = new Posicion(i,j);
                    casilla[i][j].addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                System.out.println("Presioné una negra");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                //System.out.println("Presioné una roja");
                                if(!rojaPresionada) {
                                    rojaPresionada = true;
                                    //bloquearBotones(tablero);
                                    temp.setEnabled(true);
                                    piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                    piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                    pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                }
                                else {
                                    rojaPresionada = false;
                                    try {
                                        if(vectorCapturas.size() > 0) {
                                            bloquearBotones(vectorCapturas, tablero);
                                        }
                                        else {
                                            desbloquearBotones();
                                        }
                                    }
                                    catch (Exception ex) {
                                        System.out.println("Error: "+ex.getMessage());
                                    }
                                }
                            }
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                System.out.println("Presioné un espacio blanco");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                //System.out.println("Presioné un espacio negro");
                                if(rojaPresionada) {
                                    //System.out.println("Presioné en ("+pos.posX+", "+pos.posY+")");
                                    if(vectorCapturas.size() > 0) {
                                        Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                        Vector moves = tablero.posiblesMovimientos(capturadora);
                                        for(int i = 0; i < moves.size(); i++) {
                                            Tablero temp = (Tablero) moves.get(i);
                                            if(temp.getCapturadasPiezas().size() > 0) {
                                                Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                            }
                                        }
                                        tablero.setPiezasCapturadas(1);
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                    else {
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                }
                            }
                        }
                    });
                }
                else if(tablero.tablero[i][j].tipo == 2) { //Si la casilla tiene una pieza roja
                    casilla[i][j] = new JButton();
                    casilla[i][j].setBounds(20+(60*j), 150+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    getContentPane().add(casilla[i][j]);
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
                    final JButton temp = casilla[i][j];
                    final Posicion pos = new Posicion(i,j);
                    casilla[i][j].addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                System.out.println("Presioné una negra");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                //System.out.println("Presioné una roja");
                                if(!rojaPresionada) {
                                    rojaPresionada = true;
                                    //bloquearBotones(tablero);
                                    temp.setEnabled(true);
                                    piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                    piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                    pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                }
                                else {
                                    rojaPresionada = false;
                                    try {
                                        if(vectorCapturas.size() > 0) {
                                            bloquearBotones(vectorCapturas, tablero);
                                        }
                                        else {
                                            desbloquearBotones();
                                        }
                                    }
                                    catch (Exception ex) {
                                        System.out.println("Error: "+ex.getMessage());
                                    }

                                }
                            }
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                System.out.println("Presioné un espacio blanco");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                //System.out.println("Presioné un espacio negro");
                                if(rojaPresionada) {
                                    //System.out.println("Presioné en ("+pos.posX+", "+pos.posY+")");
                                    if(vectorCapturas.size() > 0) {
                                        Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                        Vector moves = tablero.posiblesMovimientos(capturadora);
                                        for(int i = 0; i < moves.size(); i++) {
                                            Tablero temp = (Tablero) moves.get(i);
                                            if(temp.getCapturadasPiezas().size() > 0) {
                                                Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                            }
                                        }
                                        tablero.setPiezasCapturadas(1);
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                    else {
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                }
                            }
                        }
                    });
                }
                else if(tablero.tablero[i][j].tipo == 3) {
                    casilla[i][j] = new JButton();
                    casilla[i][j].setBounds(20+(60*j), 150+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/damaNegra.png")));
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    getContentPane().add(casilla[i][j]);
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/damaNegra.png")));
                    final JButton temp = casilla[i][j];
                    final Posicion pos = new Posicion(i,j);
                    casilla[i][j].addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                System.out.println("Presioné una negra");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                //System.out.println("Presioné una roja");
                                if(!rojaPresionada) {
                                    rojaPresionada = true;
                                    //bloquearBotones(tablero);
                                    temp.setEnabled(true);
                                    piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                    piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                    pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                }
                                else {
                                    rojaPresionada = false;
                                    try {
                                        if(vectorCapturas.size() > 0) {
                                            bloquearBotones(vectorCapturas, tablero);
                                        }
                                        else {
                                            desbloquearBotones();
                                        }
                                    }
                                    catch (Exception ex) {
                                        System.out.println("Error: "+ex.getMessage());
                                    }

                                }
                            }
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                System.out.println("Presioné un espacio blanco");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                //System.out.println("Presioné un espacio negro");
                                if(rojaPresionada) {
                                    //System.out.println("Presioné en ("+pos.posX+", "+pos.posY+")");
                                    if(vectorCapturas.size() > 0) {
                                        Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                        Vector moves = tablero.posiblesMovimientos(capturadora);
                                        for(int i = 0; i < moves.size(); i++) {
                                            Tablero temp = (Tablero) moves.get(i);
                                            if(temp.getCapturadasPiezas().size() > 0) {
                                                Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                            }
                                        }
                                        tablero.setPiezasCapturadas(1);
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                    else {
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                }
                            }
                        }
                    });
                }
                else if(tablero.tablero[i][j].tipo == 4) {
                    casilla[i][j] = new JButton();
                    casilla[i][j].setBounds(20+(60*j), 150+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")));
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    getContentPane().add(casilla[i][j]);
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")));
                    final JButton temp = casilla[i][j];
                    final Posicion pos = new Posicion(i,j);
                    casilla[i][j].addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                System.out.println("Presioné una negra");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                //System.out.println("Presioné una roja");
                                if(!rojaPresionada) {
                                    rojaPresionada = true;
                                    //bloquearBotones(tablero);
                                    temp.setEnabled(true);
                                    piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                    piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                    pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                }
                                else {
                                    rojaPresionada = false;
                                    try {
                                        if(vectorCapturas.size() > 0) {
                                            bloquearBotones(vectorCapturas, tablero);
                                        }
                                        else {
                                            desbloquearBotones();
                                        }
                                    }
                                    catch (Exception ex) {
                                        System.out.println("Error: "+ex.getMessage());
                                    }

                                }
                            }
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                System.out.println("Presioné un espacio blanco");
                            else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                //System.out.println("Presioné un espacio negro");
                                if(rojaPresionada) {
                                    //System.out.println("Presioné en ("+pos.posX+", "+pos.posY+")");
                                    if(vectorCapturas.size() > 0) {
                                        Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                        Vector moves = tablero.posiblesMovimientos(capturadora);
                                        for(int i = 0; i < moves.size(); i++) {
                                            Tablero temp = (Tablero) moves.get(i);
                                            if(temp.getCapturadasPiezas().size() > 0) {
                                                Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                            }
                                        }
                                        tablero.setPiezasCapturadas(1);
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                    else {
                                        piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                        tablero.setPiezaMovida(piezaPresionada);
                                        rojaMovida = true;
                                    }
                                }
                            }
                        }
                    });
                }
                else {
                    if((i+j)%2 == 0) {
                        casilla[i][j] = new JButton();
                        casilla[i][j].setBounds(20+(60*j), 150+(60*i), 60, 60);
                        casilla[i][j].setRolloverEnabled(false);
                        casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")));
                        casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        getContentPane().add(casilla[i][j]);
                        casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")));
                        final JButton temp = casilla[i][j];
                        final Posicion pos = new Posicion(i,j);
                        casilla[i][j].addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                                if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                    System.out.println("Presioné una negra");
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                    //System.out.println("Presioné una roja");
                                    if(!rojaPresionada) {
                                        rojaPresionada = true;
                                        //bloquearBotones(tablero);
                                        temp.setEnabled(true);
                                        piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                        piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                        pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                    }
                                    else {
                                        rojaPresionada = false;
                                        try {
                                            if(vectorCapturas.size() > 0) {
                                                bloquearBotones(vectorCapturas, tablero);
                                            }
                                            else {
                                                desbloquearBotones();
                                            }
                                        }
                                        catch (Exception ex) {
                                            System.out.println("Error: "+ex.getMessage());
                                        }
                                    }
                                }
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                    System.out.println("Presioné un espacio blanco");
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                    //System.out.println("Presioné un espacio negro");
                                    if(rojaPresionada) {
                                        //System.out.println("Presioné en ("+pos.posX+", "+pos.posY+")");
                                        if(vectorCapturas.size() > 0) {
                                            Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                            Vector moves = tablero.posiblesMovimientos(capturadora);
                                            for(int i = 0; i < moves.size(); i++) {
                                                Tablero temp = (Tablero) moves.get(i);
                                                if(temp.getCapturadasPiezas().size() > 0) {
                                                    Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                    tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                                }
                                            }
                                            tablero.setPiezasCapturadas(1);
                                            piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                            tablero.setPiezaMovida(piezaPresionada);
                                            rojaMovida = true;
                                        }
                                        else {
                                            piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                            tablero.setPiezaMovida(piezaPresionada);
                                            rojaMovida = true;
                                        }
                                    }
                                }
                            }
                        });

                    }
                    else {
                        casilla[i][j] = new JButton();
                        casilla[i][j].setBounds(20+(60*j), 150+(60*i), 60, 60);
                        casilla[i][j].setRolloverEnabled(false);
                        casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuro.png")));
                        casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        getContentPane().add(casilla[i][j]);
                        casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")));
                        final JButton temp = casilla[i][j];
                        final Posicion pos = new Posicion(i,j);
                        casilla[i][j].addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e) {
                                if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()))
                                    System.out.println("Presioné una negra");
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                                    //System.out.println("Presioné una roja");
                                    if(!rojaPresionada) {
                                        rojaPresionada = true;
                                        //bloquearBotones(tablero);
                                        temp.setEnabled(true);
                                        piezaPresionada.setAnterior(new Posicion(pos.posX, pos.posY));
                                        piezaPresionada.setTipo(tablero.tablero[pos.posX][pos.posY].tipo);
                                        pintarPosibles(pos, new Vector(tablero.posiblesMovimientos(new Pieza(tablero.tablero[pos.posX][pos.posY].tipo, pos))));
                                    }
                                    else {
                                        rojaPresionada = false;
                                        try {
                                            if(vectorCapturas.size() > 0) {
                                                bloquearBotones(vectorCapturas, tablero);
                                            }
                                            else {
                                                desbloquearBotones();
                                            }
                                        }
                                        catch (Exception ex) {
                                            System.out.println("Error: "+ex.getMessage());
                                        }
                                    }
                                }
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString()))
                                    System.out.println("Presioné un espacio blanco");
                                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                                    //System.out.println("Presioné un espacio negro");
                                    if(rojaPresionada) {
                                        //System.out.println("Presioné en ("+pos.posX+", "+pos.posY+")");
                                        if(vectorCapturas.size() > 0) {
                                            Pieza capturadora = (Pieza) vectorCapturas.get(0);
                                            Vector moves = tablero.posiblesMovimientos(capturadora);
                                            for(int i = 0; i < moves.size(); i++) {
                                                Tablero temp = (Tablero) moves.get(i);
                                                if(temp.getCapturadasPiezas().size() > 0) {
                                                    Pieza capturada = (Pieza) temp.getCapturadasPiezas().get(0);
                                                    tablero.setCapturadasPiezas(temp.getCapturadasPiezas());
                                                }
                                            }
                                            tablero.setPiezasCapturadas(1);
                                            piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                            tablero.setPiezaMovida(piezaPresionada);
                                            rojaMovida = true;
                                        }
                                        else {
                                            piezaPresionada.setPos(new Posicion(pos.posX, pos.posY));
                                            tablero.setPiezaMovida(piezaPresionada);
                                            rojaMovida = true;
                                        }
                                    }
                                }
                            }
                        });

                    }
                }
            }
                
        }
        rojaMovida = false;
        piezaPresionada = new Pieza(0, null);
        
        marco = new JLabel();
        marco.setBounds(10, 10, 520, 520);
        marco.setIcon(new ImageIcon(getClass().getResource("/recursos/marco.png")));
        getContentPane().add(marco);
        
        /*consola = new JTextArea();
        //consola.setBounds(520, 570, 0, 0);
        consola.setEditable(true);
        barra = new JScrollPane(consola);
        barra.setBounds(520, 570, 340, 60);
        getContentPane().add(barra);*/

        bug = new JLabel();
        bug.setBounds(100, 15+(20*9), 0, 0);
        getContentPane().add(bug);
        
        fondo = new JLabel();
        fondo.setBounds(0, 0, 1000, 1000);
        fondo.setIcon(new ImageIcon(getClass().getResource("/recursos/fondo.jpg")));
        getContentPane().add(fondo);
        
        setTitle("Damas");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //getContentPane().setBackground(Color.GREEN.darker().darker());
        getContentPane().setLayout(null);
    }
    
    public void pintarJugada(Tablero jugada) {
        tablero = jugada;
        Pieza movida = jugada.getPiezaMovida();
        Posicion posNueva = movida.getPos();
        Posicion posAntigua = movida.getAnterior();
        Pieza pMovida = new Pieza(tablero.tablero[posNueva.posX][posNueva.posY].tipo, new Posicion(posNueva.posX,posNueva.posY));
        movida.setAnterior(new Posicion(posAntigua.posX, posAntigua.posY));
        tablero.setPiezaMovida(movida);
        movida = pMovida;
        
        if(movida.getTipo() == 1) { //Negra
            casilla[posNueva.posX][posNueva.posY].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
            casilla[posNueva.posX][posNueva.posY].setIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
        }
        else if(movida.getTipo() == 2) { //Roja
            casilla[posNueva.posX][posNueva.posY].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
            casilla[posNueva.posX][posNueva.posY].setIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
        }
        else if(movida.getTipo() == 3) { //Dama negra
            casilla[posNueva.posX][posNueva.posY].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/damaNegra.png"))); //Falta cambiar imagen
            casilla[posNueva.posX][posNueva.posY].setIcon(new ImageIcon(getClass().getResource("/recursos/damaNegra.png"))); //Falta cambiar imagen por dama
        }
        else if(movida.getTipo() == 4){ //Dama roja
            casilla[posNueva.posX][posNueva.posY].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/damaRoja.png"))); //Cambiar por dama la imagen
            casilla[posNueva.posX][posNueva.posY].setIcon(new ImageIcon(getClass().getResource("/recursos/damaRoja.png"))); //Cambiar por dama la imagen
        }
        casilla[posAntigua.posX][posAntigua.posY].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuro.png")));
        casilla[posAntigua.posX][posAntigua.posY].setIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")));

        if(jugada.getPiezasCapturadas() > 0 || jugada.getCapturadasPiezas().size() > 0) {
            for(int i = 0; i < jugada.getCapturadasPiezas().size(); i++) {
                Pieza capturada = (Pieza) jugada.getCapturadasPiezas().get(i);
                Posicion posTemp = capturada.getPos();
                try {
                    Thread.currentThread().sleep(300);
                    casilla[posTemp.posX][posTemp.posY].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuro.png")));
                    casilla[posTemp.posX][posTemp.posY].setIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png"))); 
                }
                catch(Exception e) {}
            }
        }
    }
    
    public void bloquearBotones(Tablero jugada) {
        tablero = jugada;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                JButton temp = casilla[i][j];
                if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()) 
                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaNegra.png")).toString())) {
                    temp.setEnabled(false);
                }
                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                    if(rojaPresionada)
                        temp.setEnabled(false);
                    else
                        temp.setEnabled(true);
                        //pintarPosibles(new Posicion(i,j), new Vector(tablero.posiblesMovimientos(new Pieza(2, new Posicion(i,j)))));
                }
                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString())) 
                    temp.setEnabled(false);
                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                    if(rojaPresionada)
                        temp.setEnabled(true);
                    else
                        temp.setEnabled(false);
                        //pintarPosibles(new Posicion(i,j), new Vector(tablero.posiblesMovimientos(new Pieza(2, new Posicion(i,j)))));
                }
            }
        }
    }
    
    public void bloquearBotones(Vector piezas, Tablero jugada) {
        vectorCapturas = (Vector) piezas.clone();
        tablero = jugada;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                casilla[i][j].setEnabled(false);
            }
        }
        for(int i = 0; i < piezas.size(); i++) {
            Pieza p = (Pieza) piezas.get(i);
            Posicion pos = p.getPos();
            casilla[pos.posX][pos.posY].setEnabled(true);
        }
        
    }
    
    public void desbloquearBotones() {
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                JButton temp = casilla[i][j];
                if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/negra.png")).toString()) 
                                    || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaNegra.png")).toString())) {
                    temp.setEnabled(false);
                }
                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/roja.png")).toString()) 
                        || temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/damaRoja.png")).toString())) {
                        temp.setEnabled(true);
                }
                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")).toString())) {
                        temp.setEnabled(false);
                }
                else if(temp.getIcon().toString().equals(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")).toString())) {
                    temp.setEnabled(false);
                }
            }
        }
    }
    
    public void pintarPosibles(Posicion pieza, Vector posibles) {
        casilla[pieza.posX][pieza.posY].setEnabled(true);
        if(tablero.puedeComer(new Pieza(tablero.tablero[pieza.posX][pieza.posY].tipo, pieza))) {
            for(int i = 0; i < posibles.size(); i++) {
                Tablero temp = (Tablero) posibles.get(i);
                if(temp.getPiezasCapturadas() > 0) {
                    Posicion pos = temp.getPiezaMovida().getPos();
                    if(rojaPresionada)
                        casilla[pos.posX][pos.posY].setEnabled(true); 
                }
            }
        }
        else {
            for(int i = 0; i < posibles.size(); i++) {
                Tablero temp = (Tablero) posibles.get(i);
                Posicion pos = temp.getPiezaMovida().getPos();
                if(rojaPresionada)
                    casilla[pos.posX][pos.posY].setEnabled(true);
            }    
        }
       
        if(posibles.size() == 0) {
            rojaPresionada = false;
        }
    }

    public Tablero escucharJugada() {
        while((!rojaMovida) && (!tablasOn) && (!rendirOn));
        if(rojaMovida) {
            rojaPresionada = false;
        
            Posicion tempPos = piezaPresionada.getPos();
            Posicion antPos = piezaPresionada.getAnterior();
            tablero.modificarTabla(tempPos, piezaPresionada.getTipo());
            tablero.modificarTabla(antPos, 0); //Enviamos a que ponga Vacío en la posición anterior.
            if(tablero.getCapturadasPiezas().size() > 0) {
                Pieza capturada = (Pieza) tablero.getCapturadasPiezas().get(0);
                tablero.modificarTabla(capturada.getPos(), 0);
            }
            
            if(tablero.promoverPiezas()) {
                imprimirConsola(juego.numJugada++, true); 
            }
            else {
                imprimirConsola(juego.numJugada++, false); 
            }
            
            pintarJugada(tablero);
            rojaMovida = false;
            vectorCapturas = new Vector();
            return tablero;
        }
        else if(tablasOn) {
            return null;
        }
        else {
            return null;
        }

    }
    
    public void imprimirConsola(int num, boolean promo) {
        Tablero temp = tablero;
        Vector capTemp = tablero.getCapturadasPiezas();
        Pieza movida = tablero.getPiezaMovida();
        String movimiento = num + ": ";
        
        //Los turnos se intercalan en la siguiente condición debido a que ya se cambió el turno del tablero.
        if(tablero.getTurno().turno == 2) {
            String a1 = convertirFila(""+(movida.getAnterior().posX), false);
            String a2 = convertirFila(""+(movida.getAnterior().posY), true);
            String b1 = convertirFila(""+(movida.getPos().posX), false);
            String b2 = convertirFila(""+(movida.getPos().posY), true);
            
            if(promo) {
                movimiento = movimiento + "Máquina: "+a2+a1
                                        +" a "+b2+b1+" R";
            }
            else {
                movimiento = movimiento + "Máquina: "+a2+a1
                                        +" a "+b2+b1;
            }
            
            String caps = "  Capturas: ";
            for(int i = 0; i < capTemp.size(); i++) {
                Pieza capturada = (Pieza) capTemp.get(i);
                Posicion pos = capturada.getPos();
                String cap = convertirFila(""+pos.posY, true) + convertirFila(""+pos.posX, false);
                caps = caps + cap + " ";
            }
            
            if(capTemp.size() > 0) {
                movimiento = movimiento + "\n" + caps;
            }
            
        }
        if(tablero.getTurno().turno == 1) {
            String a1 = convertirFila(""+(movida.getAnterior().posX), false);
            String a2 = convertirFila(""+(movida.getAnterior().posY), true);
            String b1 = convertirFila(""+(movida.getPos().posX), false);
            String b2 = convertirFila(""+(movida.getPos().posY), true);
            
            if(promo) {
                movimiento = movimiento + "Jugador: "+a2+a1
                                        +" a "+b2+b1+" R";
            }
            else {
                movimiento = movimiento + "Jugador: "+a2+a1
                                        +" a "+b2+b1;
            }
            
            String caps = "  Capturas: ";
            for(int i = 0; i < capTemp.size(); i++) {
                Pieza capturada = (Pieza) capTemp.get(i);
                Posicion pos = capturada.getPos();
                String cap = convertirFila(""+pos.posY, true) + convertirFila(""+pos.posX, false);
                caps = caps + cap + " ";
            }
            
            if(capTemp.size() > 0) {
                movimiento = movimiento + "\n" + caps;
            }
        }
        actualizarConsola(movimiento);
        if(capTemp.size() > 0) {
            
        }
    }
    
    public void moverConsola() {
        if(oculta) {
            barra.setBounds(560, 560, 320, 60);
            boton.setBounds(660, 530, 80, 20);
            oculta = false;
            consola.setText(consola.getText());
        }
        else {
            barra.setBounds(1000, 1000, 340, 60);
            boton.setBounds(660, 590, 80, 20);
            consola.setText(consola.getText());
            oculta = true;
        }
    }
    
    public void actualizarConsola(String s) {
        consola.setText(consola.getText()+s+"\n");
        consola.setCaretPosition(consola.getDocument().getLength());
    }
    
    public void setVectorCapturas(Vector vector) {
        vectorCapturas = vector;
    }

    public void finJuego(String ganador) {
        if(ganador.equals("tablas")) {
            JOptionPane.showMessageDialog(null, "La máquina ha aceptado tablas\n ¡Es un empate!");
            organizarTerminado();
        }
        else if(ganador.equals("rendir")) {
            JOptionPane.showMessageDialog(null, "Te has rendido, el ganador es la máquina.");
            organizarTerminado();
        }
        else {
            JOptionPane.showMessageDialog(null, "El juego ha terminado, el ganador es " + ganador + ".");
            organizarTerminado();
        }
    }
    
    public void organizarTerminado() {
        tablas.setEnabled(false);
        tablas.setVisible(false);
        rendirse.setEnabled(false);
        rendirse.setVisible(false);
        nuevoJuego.setEnabled(true);
        nuevoJuego.setVisible(true);
        salir.setEnabled(true);
        salir.setVisible(true);
        rendirOn = false;
        tablasOn = false;
        consola.setText("");
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((i+j)%2 == 0) {
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")));
                    casilla[i][j].setEnabled(false);
                }
                else {
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuro.png")));
                    casilla[i][j].setEnabled(false);
                }
            }
        }
        salirONuevo();
    }
    
    public void salirONuevo() {
        for(int i = 0; i < 1; i++) {
            nuevoJuego.setEnabled(true);
            nuevoJuego.setVisible(true);
            salir.setEnabled(true);
            salir.setVisible(true);

            while((!nuevoJuegoOn) && (!salirOn));
            
            if(nuevoJuegoOn) {
                nuevoJuegoOn = false;
                salir.setEnabled(false);
                salir.setVisible(false);
                nuevoJuego.setEnabled(false);
                nuevoJuego.setVisible(false);
                iniciarNuevoJuego();
                System.out.println("Iniciar");
            }
            else {
                nuevoJuegoOn = false;
                salirOn = false;
                salir.setEnabled(false);
                salir.setVisible(false);
                nuevoJuego.setEnabled(false);
                nuevoJuego.setVisible(false);
                int confirm = 1;
                confirm = JOptionPane.showOptionDialog(null, "¿Seguro que desea salir?", "Salir", 2, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Salir", "Cancelar"}, null);
                if(confirm == 0) {
                    System.exit(0);
                }
                else {
                    i--;
                }
            }
        }
    }
    
    public void iniciarNuevoJuego() {
        juego.iniciarNuevo();
    }
    
    public void botonesNuevoJuego() {
        
        consola.setEditable(false);
        consola.setFont(new Font("Lucida Console", Font.PLAIN, 11));
        consola.setText("");
        
        barra.setBounds(540, 570, 340, 60);
        oculta = true;
        barra.setLocation(1000, 1000);
        
        vectorCapturas = new Vector();  
        
        boton.setBounds(660, 590, 80, 20);
        boton.setFont(new Font("Lucida Console", Font.PLAIN, 11));
        boton.setForeground(Color.WHITE);
        boton.setOpaque(false);
        boton.setSelected(false);
        boton.setFocusPainted(false);
        
        tablas.setOpaque(false);
        tablas.setEnabled(true);
        tablas.setVisible(true);
        tablas.setSelected(false);
        tablas.setFocusPainted(false);
        
        rendirse.setOpaque(false);
        rendirse.setSelected(false);
        rendirse.setEnabled(true);
        rendirse.setVisible(true);
        rendirse.setFocusPainted(false);
        
        nuevoJuego.setOpaque(false);
        nuevoJuego.setSelected(false);
        nuevoJuego.setEnabled(false);
        nuevoJuego.setVisible(false);
        nuevoJuego.setFocusPainted(false);

        salir.setOpaque(false);
        salir.setSelected(false);
        salir.setEnabled(false);
        salir.setVisible(false);
        salir.setFocusPainted(false);

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((i == 0 || i == 2) && (j%2 == 1)) {
                    casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
                }
                else if(i == 1 && j%2 == 0) {
                    casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/negra.png")));
                }
                else if((i == 5 || i == 7) && (j%2 == 0)) {
                    casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
                }
                else if((i == 6) && (j%2 == 1)) {
                    casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                    casilla[i][j].setRolloverEnabled(false);
                    casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
                    casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/roja.png")));
                }
                else {
                    if((i+j)%2 == 0) {
                        casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                        casilla[i][j].setRolloverEnabled(false);
                        casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")));
                        casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/fondoClaro.png")));
                    }
                    else {
                        casilla[i][j].setBounds(50+(60*j), 120+(60*i), 60, 60);
                        casilla[i][j].setRolloverEnabled(false);
                        casilla[i][j].setDisabledIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuro.png")));
                        casilla[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        casilla[i][j].setIcon(new ImageIcon(getClass().getResource("/recursos/fondoOscuroRoja.png")));
                    }
                }
            }
        }
        
        rojaMovida = false;
        piezaPresionada = new Pieza(0, null);
        
        marco.setBounds(30, 100, 520, 520);
        marco.setIcon(new ImageIcon(getClass().getResource("/recursos/marco.png")));
            
        fondo.setIcon(new ImageIcon(getClass().getResource("/recursos/fondo.jpg")));
    }
    
    public boolean tablas() {
        int cantFichas1 = tablero.fichasTurno();
        tablero.cambiarTurno();
        int cantFichas2 = tablero.fichasTurno();
        tablero.cambiarTurno();
        if(cantFichas1 == cantFichas2 && movimientos > 20){
            return true;
        }
        else {
            return false;
        }
    }

    public void setMovimientos(int movimientos) {
        this.movimientos = movimientos;
    }

    public int getMovimientos() {
        return movimientos;
    }
    
    public void setJuego(Juego juego) {
        this.juego = juego;
    }
    
    public String convertirFila(String s, boolean letra) {
        if(letra) {
            if(s.equals("0")) return "A";
            else if(s.equals("1")) return "B";
            else if(s.equals("2")) return "C";
            else if(s.equals("3")) return "D";
            else if(s.equals("4")) return "E";
            else if(s.equals("5")) return "F";
            else if(s.equals("6")) return "G";
            else if(s.equals("7")) return "H";
        }
        else {
            if(s.equals("0")) return "8";
            else if(s.equals("1")) return "7";
            else if(s.equals("2")) return "6";
            else if(s.equals("3")) return "5";
            else if(s.equals("4")) return "4";
            else if(s.equals("5")) return "3";
            else if(s.equals("6")) return "2";
            else if(s.equals("7")) return "1";
        }
        return "";
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }
    
}
