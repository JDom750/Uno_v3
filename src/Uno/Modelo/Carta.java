package Uno.Modelo;


public class Carta {


    private final Color color;
    private final Numero numero;

    public Carta (final Color color, final Numero numero){
        this.numero = numero;
        this.color = color;
    }

    public Color getColor(){
        return this.color;
    }

    public Numero getNumero(){
        return this.numero;
    }

    public String toString(){
        return color + "_" + numero;
    }
}
