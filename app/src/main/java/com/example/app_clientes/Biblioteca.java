//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes;
//Importamos los siguientes paquetes:
import com.example.app_clientes.pojos.Usuario;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Clase donde se crearan distintos metodos utilizados por otras clases.
//Tambien se incluiran algunas variables globales estaticas para unificar su valor en un punto.
public class Biblioteca {
    //Variables u objetos publicos estaticos:
    public static long tAnimacionesScaleInicial = 500;          //Tiempo para las animaciones de scale al entrar a la ventana.
    public static long tAnimacionesScaleBotones = 200;          //Tiempo para las animaciones de scale al des/habilitar un boton.
    public static String ip = "http://192.168.0.107:8080/";     //Ip a la que conectarse desde el cliente Retrofit.
    public static Usuario usuarioSesion;                        //Sera nuestro usuario de sesion una vez inicie sesion.
    //Metodos publicos y estaticos:
    //Metodo que comprueba si un email es valido recibido como String, y nos devuelve un booleano indicando el resultado:
    public static boolean compruebaEmailValido(String email){
        boolean prueba;
        Pattern patronEmail = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");    //Expresion regular para email.
        Matcher match = patronEmail.matcher(email.toLowerCase());                                       //Email matcheado en minusculas.
        //Si hace match o coincide se devolvera true o false:
        if (!match.find()) {
            prueba=false;
        }else{
            prueba=true;
        }
        return prueba;
    }
    //Metodo que comprueba si una cadena tiene caracteres alfanumericos solo (acentos y demas no validos), devuelve un booleano indicando el resultado:
    public static boolean compruebaSiCadenaContieneCaracteresAlfanumericos(String cadena){
        boolean prueba = true;
        cadena=cadena.toUpperCase();        //Cadena convertida a mayusculas para asi solo tener que comparar rangos de letras en mayusculas.
        //Bucle for donde se valida el rango:
        for (int i = 0 ; i < cadena.length() && prueba; i++){
            if ((cadena.charAt(i) < 'A' || cadena.charAt(i) > 'Z') && (cadena.charAt(i) < '0' || cadena.charAt(i) > '9')) {
                prueba=false;
            }
        }
        return prueba;
    }
}
