package Uno.Modelo;

import Uno.Observable;
import Uno.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Partida implements Observable {
    /*
     Controlo de quien es el turno
     */
    private int jugadorActual;
    /*
    Llevo la lista de jugadores de la partida
    */
    private String[] idJugadores;
    /*
    defino el mazo
     */
    private Mazo mazo;
    /*
    Almaceno las manos de cada uno de los jugadores
     */
    private ArrayList<ArrayList<Carta>> manos;
    /*
    controlo la pila de cartas que fueron jugadasd
     */
    private ArrayList<Carta> pila;

    private Color colorValido;
    private Numero numeroValido;
    boolean gameDirection;

    private ArrayList<Observer> observers = new ArrayList<>();

    public Partida(String[] pids){
        this.mazo = new Mazo();
        this.mazo.barajar();
        this.pila = new ArrayList<Carta>();
        this.idJugadores = pids;
        this.jugadorActual = 0;
        /*
        false sera la direccion por defecto. (antihorario)
         */
        gameDirection = false;
        this.manos = new ArrayList<ArrayList<Carta>>();

        for (int i=0; i< pids.length;i++){
            ArrayList<Carta> mano = new ArrayList<Carta>(Arrays.asList(mazo.robarCartas(7)));
            manos.add(mano);
        }
    }

    public void start(Partida partida) {
        Carta carta = this.mazo.robarCarta();
        this.colorValido = carta.getColor();
        this.numeroValido = carta.getNumero();

        if (carta.getNumero() == Numero.CAMBIOCOLOR ||
                carta.getNumero() == Numero.MASCUATRO ||
                carta.getNumero() == Numero.MASDOS) {
            // Tratar eventos especiales (puedes agregar lógica aquí si es necesario)
            pila.add(carta);
            start(partida);
        } else if (carta.getNumero() == Numero.SALTARSE) {
            // Tratar evento de saltear
            if (gameDirection == false) {
                jugadorActual = (jugadorActual + 1) % idJugadores.length;
            } else {
                jugadorActual = (jugadorActual - 1) % idJugadores.length;
                if (jugadorActual == -1) {
                    jugadorActual = idJugadores.length - 1;
                }
            }
        } else if (carta.getNumero() == Numero.CAMBIOSENTIDO) {
            // Tratar evento de cambio de sentido
            gameDirection ^= true;
            jugadorActual = idJugadores.length - 1;
        }

        // Notificar a los observadores sobre el cambio
        notificarCambio();

        pila.add(carta);

        // Notificar a los observadores sobre el inicio de la partida
        //notificarCambio();
    }



    /**
     * Muestra la carta de la pila
     * @return
     */
    public Carta levantarCarta(){
        return new Carta(colorValido, numeroValido);
    }

    public ImageIcon robarImagenCarta(){
        return new ImageIcon(colorValido +"-" + numeroValido +".png");
    }

    public boolean TerminoElJuego(){
        for(String jugador : this.idJugadores){
            if(manoVacia(jugador)){
                return true;
            }
        }
        return false;
    }
    public String obtenerJugadorActual(){
        return this.idJugadores[this.jugadorActual];
    }

    public String jugadorAnterior(int i){
        int index = this.jugadorActual -1;
        if (index==-1){
            index=idJugadores.length -1;
        }
        return this.idJugadores[index];
    }
    public String[] obtJugadores(){
        return idJugadores;
    }
    public  ArrayList<Carta> obtManoJugador(String pid){
        int index= Arrays.asList(idJugadores).indexOf(pid);
        return manos.get(index);
    }

    public int tamManoJugador(String pid){
        return obtManoJugador(pid).size();
    }

    public Carta obtCartaJugador(String pid, int o){
        ArrayList<Carta> manoJ = obtManoJugador(pid);
        return manoJ.get(o);
    }
    public boolean manoVacia(String pid){
        return obtManoJugador(pid).isEmpty();
    }

    public boolean cartaValida(Carta c){
        return c.getColor() == colorValido || c.getNumero() == numeroValido;
    }
    public void esTurno(String pid) throws InvalidPlayerTurnException{
        if(this.idJugadores[this.jugadorActual] != pid){
            throw new InvalidPlayerTurnException("No es el turno de "+ pid, pid);
        }
    }
    public void tomarCarta(String pid) throws InvalidPlayerTurnException {
        esTurno(pid);

        if(mazo.estaVacio()){
            mazo.reemplazarMazoCon(pila);
            mazo.barajar();
        }
        obtManoJugador(pid).add(mazo.robarCarta());
        if(gameDirection == false) {
            jugadorActual = (jugadorActual +1) % idJugadores.length;
        }
        else if(gameDirection == true){
            jugadorActual = (jugadorActual-1)%idJugadores.length;
            if (jugadorActual ==-1){
                jugadorActual = idJugadores.length -1;
            }
        }
        notifyObservers();
    }
    public void setColorDeCarta(Color c){
        colorValido = c;
    }


    public void tomarCartaJugador(String pid, Carta carta, Color colorDeclarado)
            throws InvalidColorSubmissionException, InvalidValueSubmissionException, InvalidPlayerTurnException {
        esTurno(pid);

        ArrayList<Carta> manoJu = obtManoJugador(pid);
        //VER ACA, CREO QUE NO ES NECESARIO ESTO
        if (!cartaValida(carta)) {
            if (carta.getColor() == Color.NEGRO) {
                this.colorValido = colorDeclarado;
            } else {
                validarCarta(carta);
            }
        }

        manoJu.remove(carta);

        if (manoVacia(this.idJugadores[jugadorActual])) {
            System.out.println("El jugador actual: " + this.idJugadores[jugadorActual] + " ganó.");
            System.exit(0);
        }
        numeroValido = carta.getNumero();
        if (carta.getColor() == Color.NEGRO) {
                    colorValido = colorDeclarado;
        }else {
            colorValido = carta.getColor();
        }

        actualizarTurno();

        manejarCartaEspecial(carta);
        notifyObservers();
        pila.add(carta);

    }


    private void validarCarta(Carta carta) throws InvalidColorSubmissionException, InvalidValueSubmissionException {
        if (carta.getColor() != colorValido) {
            throw new InvalidColorSubmissionException("Movimiento inválido, color esperado: " + colorValido
                    + " pero se ingresó el color " + carta.getColor(), carta.getColor(), colorValido);
        } else if (carta.getNumero() != numeroValido) {
            throw new InvalidValueSubmissionException("Movimiento inválido, número esperado: " + numeroValido
                    + " pero se ingresó el número " + carta.getNumero(), carta.getNumero(), numeroValido);
        }
    }

    private void actualizarTurno() {
        if (gameDirection == false) {
            jugadorActual = (jugadorActual + 1) % idJugadores.length;
        } else if (gameDirection == true) {
            jugadorActual = (jugadorActual - 1) % idJugadores.length;
            if (jugadorActual == -1) {
                jugadorActual = idJugadores.length - 1;
            }
        }
    }

    private void manejarCartaEspecial(Carta carta) {
        // Lógica para cartas especiales (MASDOS, MASCUATRO, SALTARSE, CAMBIOSENTIDO)...
        if (carta.getNumero() == Numero.MASDOS) {
            manejarMasDos();
        } else if (carta.getNumero() == Numero.MASCUATRO) {
            manejarMasCuatro();
        } else if (carta.getNumero() == Numero.SALTARSE) {
            manejarSaltarse();
        } else if (carta.getNumero() == Numero.CAMBIOSENTIDO) {
            manejarCambiarSentido();
        }
    }

    private void manejarMasDos() {
        String pid = idJugadores[jugadorActual];
        obtManoJugador(pid).add(mazo.robarCarta());
        obtManoJugador(pid).add(mazo.robarCarta());
        System.out.println(pid + " tomó 2 cartas");
    }

    private void manejarMasCuatro() {
        String pid = idJugadores[jugadorActual];
        obtManoJugador(pid).add(mazo.robarCarta());
        obtManoJugador(pid).add(mazo.robarCarta());
        obtManoJugador(pid).add(mazo.robarCarta());
        obtManoJugador(pid).add(mazo.robarCarta());
        System.out.println(pid + " tomó 4 cartas");
    }

    private void manejarSaltarse() {
        System.out.println(idJugadores[jugadorActual] + " fue saltado");
        actualizarTurno();
    }

    private void manejarCambiarSentido() {
        if (gameDirection ==false) {
            System.out.println("La dirección del juego fue invertida por: " + idJugadores[jugadorActual - 1]);
        }else{
            System.out.println("La dirección del juego fue invertida por: " + idJugadores[jugadorActual +1]);
        }
        gameDirection ^= true;
        if (gameDirection == true) {
            jugadorActual = (jugadorActual - 2) % idJugadores.length;
            if (jugadorActual == -1) {
                jugadorActual = idJugadores.length - 1;
            }
            if (jugadorActual == -2) {
                jugadorActual = idJugadores.length - 2;
            }
        } else if (gameDirection == false) {
            jugadorActual = (jugadorActual + 2) % idJugadores.length;
        }
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void deleteObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
    // Método para notificar a los observadores sobre algún cambio en la partida
    private void notificarCambio() {
        notifyObservers();
    }
}

