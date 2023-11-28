package Uno.Modelo;

public class InvalidValueSubmissionException extends Exception {
    private Numero esperado;
    private Numero actual;

    public InvalidValueSubmissionException(String m, Numero actual, Numero esperado) {
        this.actual = actual;
        this.esperado = esperado;
    }
}
