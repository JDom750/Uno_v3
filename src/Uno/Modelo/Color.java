package Uno.Modelo;

public   enum Color
{
    ROJO, VERDE, AMARILLO, AZUL, NEGRO;

    private static final Color[] colores = Color.values();
    public static Color getColor(int i){
        return Color.colores[i]; //VER ESTO
    }
}