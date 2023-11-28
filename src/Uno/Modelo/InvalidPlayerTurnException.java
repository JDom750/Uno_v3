package Uno.Modelo;

//---------------------------------------
public class InvalidPlayerTurnException extends Exception {
    String id_jugador;

    public InvalidPlayerTurnException(String m, String pid) {
        super(m);
        id_jugador = pid;
    }

    public String getPid() {
        return id_jugador;
    }
}
