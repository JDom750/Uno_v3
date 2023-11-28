package Uno.Modelo;

public class InvalidColorSubmissionException extends Exception {
    private Color esperado;
    private Color actual;

    public InvalidColorSubmissionException(String m, Color actual, Color esperado) {
        this.actual = actual;
        this.esperado = esperado;
    }
}
