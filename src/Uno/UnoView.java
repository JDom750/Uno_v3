package Uno;

import Uno.Modelo.*;

import java.util.Scanner;
import java.util.ArrayList;

public class UnoView implements Observer {
    private Partida partida;
    private UnoController controller;
    private Scanner scanner;
    private ArrayList<Observer> observers;

    public UnoView(Partida partida, UnoController controller) {
        this.partida = partida;
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        this.observers = new ArrayList<Observer>();
        this.partida.addObserver(this);
    }


    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void deleteObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyObservers(String titulo, String mensaje) {
        for (Observer observer : observers) {
            observer.notifyError(titulo, mensaje);
        }
    }
    /*
        public void actualizar() {
        System.out.println("Turno de: " + partida.obtenerJugadorActual());
        System.out.println("Cartas en mano:");
        for (String jugador : partida.obtJugadores()) {
            System.out.println(jugador + ": " + cartasEnMano(jugador));
        }
        System.out.println("\nÚltima carta jugada:");
        System.out.println(partida.levantarCarta());
        System.out.println("\nElige la posición de la carta que deseas jugar (1, 2, 3, ...) o 0 para robar:");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        if (opcion == 0) {
            controller.tomarCarta(partida.obtenerJugadorActual());
        } else {
            Carta cartaSeleccionada = partida.obtManoJugador(partida.obtenerJugadorActual()).get(opcion - 1);
            System.out.println("La carta seleccionada es: " + cartaSeleccionada.getColor().toString() + " y el número es: " + cartaSeleccionada.getNumero().toString());
            if (cartaSeleccionada.getColor() != Color.NEGRO) {
                controller.jugarCarta(opcion-1);
            } else {
                // El jugador tiene una carta negra, pregunta por el color
                System.out.println("Elige un color (ROJO, VERDE, AMARILLO, AZUL):");
                String colorElegido = scanner.next();
                Color colorDeclarado = Color.valueOf(colorElegido.toUpperCase());
                controller.jugarCarta(opcion-1);   //necesito hacer una sobrecarga
            }
        }
    }

     */


    public void actualizar() {
        try {
            System.out.println("Turno de: " + partida.obtenerJugadorActual());
            System.out.println("Cartas en mano:");
            for (String jugador : partida.obtJugadores()) {
                System.out.println(jugador + ": " + cartasEnMano(jugador));
            }
            System.out.println("\nÚltima carta jugada:");
            System.out.println(partida.levantarCarta());
            System.out.println("\nElige la posición de la carta que deseas jugar (1, 2, 3, ...) o 0 para robar:");
            int opcion = scanner.nextInt();
            scanner.nextLine();
            if (opcion == 0) {
                controller.tomarCarta(partida.obtenerJugadorActual());
            } else {
                Carta cartaSeleccionada = partida.obtManoJugador(partida.obtenerJugadorActual()).get(opcion - 1);
                System.out.println("La carta seleccionada es: " + cartaSeleccionada.getColor().toString() + " y el número es: " + cartaSeleccionada.getNumero().toString());
                if (cartaSeleccionada.getColor() != Color.NEGRO) {
                    controller.jugarCarta(opcion - 1);
                } else {
                    // El jugador tiene una carta negra, pregunta por el color
                    //System.out.println("Elige un color (ROJO, VERDE, AMARILLO, AZUL):");
                    //String colorElegido = scanner.next();
                   // Color colorDeclarado = Color.valueOf(colorElegido.toUpperCase());
                    controller.jugarCarta(opcion - 1);
                }
            }
        } catch (InvalidColorSubmissionException | InvalidValueSubmissionException | InvalidPlayerTurnException e) {
            // Manejar las excepciones aquí (puedes imprimir un mensaje, notificar al usuario, etc.)
            System.out.println("Error: " + e.getMessage());
        }
    }


    private String cartasEnMano(String jugador) {
        StringBuilder cartas = new StringBuilder();
        int i = 1;
        for (Carta carta : partida.obtManoJugador(jugador)) {
            cartas.append(i).append(": ").append(carta).append(", ");
            i++;
        }
        return cartas.toString();
    }
/*
    public void update() {
        System.out.println("Turno de: " + partida.obtenerJugadorActual());
        System.out.println("Cartas en mano:");
        for (String jugador : partida.obtJugadores()) {
            System.out.println(jugador + ": " + cartasEnMano(jugador));
        }
        System.out.println("\nÚltima carta jugada:");
        System.out.println(partida.levantarCarta());
        System.out.println("\nElige la posición de la carta que deseas jugar (1, 2, 3, ...) o 0 para robar:");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        if (opcion == 0) {
            controller.tomarCarta(partida.obtenerJugadorActual());
        } else {
            Carta cartaSeleccionada = partida.obtManoJugador(partida.obtenerJugadorActual()).get(opcion - 1);
            System.out.println("La carta seleccionada es: " + cartaSeleccionada.getColor().toString() + " y el número es: " + cartaSeleccionada.getNumero().toString());
            if (cartaSeleccionada.getColor() != Color.NEGRO) {
                controller.jugarCarta(opcion-1);
            } else {
                // El jugador tiene una carta negra, pregunta por el color
                System.out.println("Elige un color (ROJO, VERDE, AMARILLO, AZUL):");
                String colorElegido = scanner.next();
                Color colorDeclarado = Color.valueOf(colorElegido.toUpperCase());
                controller.jugarCarta(opcion-1);
            }
        }
    }
 */
@Override
public void update() {
    try {
        System.out.println("Turno de: " + partida.obtenerJugadorActual());
        System.out.println("Cartas en mano:");
        //for (String jugador : partida.obtJugadores()) {
         //   System.out.println(jugador + ": " + cartasEnMano(jugador));
        //}
        System.out.println(partida.obtenerJugadorActual() + ": " + cartasEnMano(partida.obtenerJugadorActual()));
        System.out.println("\nÚltima carta jugada:");
        System.out.println(partida.levantarCarta());
        System.out.println("\nElige la posición de la carta que deseas jugar (1, 2, 3, ...) o 0 para robar:");
        int opcion = scanner.nextInt();
        scanner.nextLine();
        if (opcion == 0) {
            controller.tomarCarta(partida.obtenerJugadorActual());
        } else {
            Carta cartaSeleccionada = partida.obtManoJugador(partida.obtenerJugadorActual()).get(opcion - 1);
            System.out.println("La carta seleccionada es: " + cartaSeleccionada.getColor().toString() + " y el número es: " + cartaSeleccionada.getNumero().toString());
            if (cartaSeleccionada.getColor() != Color.NEGRO) {
                controller.jugarCarta(opcion - 1);
            } else {
                // El jugador tiene una carta negra, pregunta por el color
                controller.jugarCarta(opcion - 1);
            }
        }
    } catch (InvalidColorSubmissionException | InvalidValueSubmissionException | InvalidPlayerTurnException e) {
        // Manejar las excepciones aquí (puedes imprimir un mensaje, notificar al usuario, etc.)
        System.out.println("Error: " + e.getMessage());
    }
}


    public Color elegirColor() {
        System.out.println("Elige un color (ROJO, VERDE, AMARILLO, AZUL):");
        String colorElegido = scanner.next();
        return Color.valueOf(colorElegido.toUpperCase());
    }
    public int elegirOpcion() {
        System.out.println("Elige la posición de la carta que deseas jugar (1, 2, 3, ...) o 0 para robar:");
        int s = scanner.nextInt();
        scanner.nextLine();
        return s;
    }
    @Override
    public void notifyError(String titulo, String mensaje) {
        // Notificar a los observadores sobre un error
        for (Observer observer : observers) {
            observer.notifyError(titulo, mensaje);
        }
    }
}
