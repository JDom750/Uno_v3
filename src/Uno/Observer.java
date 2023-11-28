package Uno;

public interface Observer {
    void update();

    void notifyError(String titulo, String mensaje);
}
