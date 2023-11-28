package Uno;

import Uno.Modelo.*;
import java.util.Scanner;

public class UnoController {
    private Partida partida;
    private UnoView vista;
    private Scanner scanner;

    public UnoController(Partida partida) {
        this.partida = partida;
        this.scanner = new Scanner(System.in);
    }

    public void iniciarJuego(String[] nombresJugadores) {
        //this.partida = new Partida(nombresJugadores);
        this.vista = new UnoView(partida, this);

        this.partida.start(partida);

        //distribuirCartas(); //no lo creo necesario
        jugar();
    }

    private void distribuirCartas() {
        for (String jugador : partida.obtJugadores()) {
            vista.notifyError(jugador, cartasEnMano(jugador));
        }
    }
/*
    private void jugar() {
        while (!partida.TerminoElJuego()) {
            String jugadorActual = partida.obtenerJugadorActual();
            vista.notifyError(jugadorActual, " Cartas: "+ cartasEnMano(jugadorActual));

            int opcion = vista.elegirOpcion();
            if (opcion == 0) {
                tomarCarta(jugadorActual);
            } else {
                jugarCarta(opcion);
            }
        }
        scanner.close();
    }

 */
    /*
        private void jugar() {
        while (!partida.TerminoElJuego()) {
            String jugadorActual = partida.obtenerJugadorActual();
            vista.notifyError(jugadorActual, ": "+cartasEnMano(jugadorActual));

            int opcion = vista.elegirOpcion();
            if (opcion == 0) {
                tomarCarta(jugadorActual);
            } else {
                boolean jugadaExitosa = false;
                while (!jugadaExitosa) {
                    try {
                        jugarCarta(opcion-1);
                        jugadaExitosa = true;  // La jugada fue exitosa, salimos del bucle
                    } catch (InvalidColorSubmissionException | InvalidValueSubmissionException | InvalidPlayerTurnException e) {
                        vista.notifyError("Movimiento inválido", e.getMessage());
                        opcion = vista.elegirOpcion();  // Pedir al jugador que elija de nuevo
                    }
                }
            }
            partida.notifyObservers();//VER SI ESTO SOLUCIONA ALGO
        }
        scanner.close();
    }

     */


    private void jugar() {
        while (!partida.TerminoElJuego()) {
            String jugadorActual = partida.obtenerJugadorActual();
            vista.notifyError(jugadorActual, ": " + cartasEnMano(jugadorActual));

            boolean jugadaExitosa = false;
            int opcion = vista.elegirOpcion(); // Mover la obtención de opción al interior del bucle
            if (opcion == 0) {
                tomarCarta(jugadorActual);
            } else {
            while (!jugadaExitosa) {
                try {
                    jugarCarta(opcion);
                    jugadaExitosa = true;  // La jugada fue exitosa, salimos del bucle
                } catch (InvalidColorSubmissionException | InvalidValueSubmissionException |
                         InvalidPlayerTurnException | IndexOutOfBoundsException e) {
                    vista.notifyError("Movimiento inválido", e.getMessage());
                    opcion = vista.elegirOpcion();  // Pedir al jugador que elija de nuevo
                }
            }
        }

            partida.notifyObservers(); //VER SI ESTO SOLUCIONA ALGO
        }
        scanner.close();
    }




    public void tomarCarta(String jugador) {
        try {
            partida.tomarCarta(jugador);
            vista.notifyError(partida.obtenerJugadorActual(), cartasEnMano(partida.obtenerJugadorActual()));
        } catch (InvalidPlayerTurnException e) {
            vista.notifyError("Error de turno", e.getMessage());
        }
    }
/*
   public void jugarCarta(int posicion) {
        try {
            String jugadorActual = partida.obtenerJugadorActual();
            Carta cartaSeleccionada = partida.obtManoJugador(jugadorActual).get(posicion - 1);

            if (cartaSeleccionada.getColor() != Color.NEGRO) {
                partida.tomarCartaJugador(jugadorActual, cartaSeleccionada, null);
            } else {
                Color colorDeclarado = vista.elegirColor();
                partida.tomarCartaJugador(jugadorActual, cartaSeleccionada, colorDeclarado);
            }

            vista.notifyError(partida.obtenerJugadorActual(), cartasEnMano(jugadorActual));
        } catch (InvalidColorSubmissionException | InvalidValueSubmissionException | InvalidPlayerTurnException e) {
            vista.notifyError("Movimiento inválido", e.getMessage());
        }
    }
 */
public void jugarCarta(int posicion) throws InvalidColorSubmissionException, InvalidValueSubmissionException, InvalidPlayerTurnException {
    String jugadorActual = partida.obtenerJugadorActual();

    try {
        Carta cartaSeleccionada = partida.obtManoJugador(jugadorActual).get(posicion - 1);

        if (cartaSeleccionada.getColor() != Color.NEGRO) {
            partida.tomarCartaJugador(jugadorActual, cartaSeleccionada, null);
        } else {
            // Si la carta es negra, primero pedimos que elija el color
            Color colorDeclarado = vista.elegirColor();
            partida.tomarCartaJugador(jugadorActual, cartaSeleccionada, colorDeclarado);
        }
        //vista.update();
        //vista.notifyError(partida.obtenerJugadorActual(), cartasEnMano(jugadorActual));
    } catch (InvalidColorSubmissionException | InvalidValueSubmissionException | InvalidPlayerTurnException e) {
        // Lanzamos la excepción aquí si ocurre
        throw e;
    }
}



    private String cartasEnMano(String jugador) {
        StringBuilder cartas = new StringBuilder();
        for (int i = 0; i < partida.obtManoJugador(jugador).size(); i++) {
            cartas.append(i + 1).append(": ").append(partida.obtManoJugador(jugador).get(i)).append("\n");
        }
        return cartas.toString();
    }
}
