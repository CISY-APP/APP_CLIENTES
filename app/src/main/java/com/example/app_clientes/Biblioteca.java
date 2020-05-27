//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes;
//Importamos los siguientes paquetes:
import com.example.app_clientes.pojos.Usuario;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Clase donde se crearan distintos metodos utilizados por otras clases.
//Tambien se incluiran algunas variables globales estaticas para unificar su valor en un punto.
public class Biblioteca {
    //Variables u objetos publicos estaticos:
    public static long tAnimacionesScaleInicial = 500;          //Tiempo para las animaciones de scale al entrar a la ventana.
    public static long tAnimacionesScaleBotones = 200;          //Tiempo para las animaciones de scale al des/habilitar un boton.
    public static String ip = "http://192.168.56.1:8080/";     //Ip a la que conectarse desde el cliente Retrofit.
    public static Usuario usuarioSesion;                        //Sera nuestro usuario de sesion una vez inicie sesion.
    //Metodos publicos y estaticos:
    //Metodo que comprueba si un email es valido recibido como String, y nos devuelve un booleano indicando el resultado:
    public static boolean compruebaEmailValido(String email){
        Pattern patronEmail = Pattern.compile("([a-z0-9]+(\\.?[a-z0-9])*)+@(([a-z]+)\\.([a-z]+))+");    //Expresion regular para email.
        Matcher match = patronEmail.matcher(email.toLowerCase());                                       //Email matcheado en minusculas.
        //Si hace match o coincide se devolvera true o false:
        return match.find();
    }
    //Metodo que comprueba si una cadena tiene caracteres alfanumericos y ñ solo (acentos y demas no validos), devuelve un booleano indicando el resultado:
    public static boolean compruebaSiCadenaContieneCaracteresAlfanumericos(String cadena){
        boolean prueba = true;
        cadena=cadena.toUpperCase();        //Cadena convertida a mayusculas para asi solo tener que comparar rangos de letras en mayusculas.
        //Bucle for donde se valida el rango:
        for (int i = 0 ; i < cadena.length() && prueba; i++){
            if ((cadena.charAt(i) < 'A' || cadena.charAt(i) > 'Z') && (cadena.charAt(i) < '0' || cadena.charAt(i) > '9') && (cadena.charAt(i) != 'Ñ')) {
                prueba=false;
            }
        }
        return prueba;
    }
    //Metodo que quita espacios laterales y los repetidos entre las palabras, recibe una cadena y devuelve la cadena tratada:
    public static String quitaEspaciosRepetidosEntrePalabras(String cadena){
        StringBuilder aux = new StringBuilder();
        for(int i = 0, contEspacios = 0 ; i < cadena.length() ; i++){
            if(cadena.charAt(i)!=' '){
                aux.append(cadena.charAt(i));
                contEspacios=0;
            }else{
                if(contEspacios==0){
                    aux.append(cadena.charAt(i));
                }
                contEspacios++;
            }
        }
        return aux.toString().trim();
    }
    //Metodo que comprueba si un nombre o apellidos tiene caracteres validos incluye caracteres acentuados del español , espacio y la ñ, devuelve un booleano indicando el resultado:
    public static boolean compruebaSiCadenaContieneCaracteresValidosConAcentos(String cadena){
        boolean prueba = true;
        cadena=cadena.toUpperCase();        //Cadena convertida a mayusculas para asi solo tener que comparar rangos de letras en mayusculas.
        //Bucle for donde se valida el rango:
        for (int i = 0 ; i < cadena.length() && prueba; i++){
            if ((cadena.charAt(i) < 'A' || cadena.charAt(i) > 'Z') && (cadena.charAt(i) != 'Ñ') && (cadena.charAt(i) != 'Á')&& (cadena.charAt(i) != 'É')&& (cadena.charAt(i) != 'Í')&& (cadena.charAt(i) != 'Ó')&& (cadena.charAt(i) != 'Ú') && (cadena.charAt(i) != ' ')) {
                prueba=false;
            }
        }
        return prueba;
    }
    //Metodo que nos capitaliza cada palabra de un String, recibe un String y devuelve un String:
    public static String capitalizaString(String txtOrigen){
        //Dividimos el string en las palabras segun los caracteres vacios o en blanco
        String []palabras = txtOrigen.split("\\s+");
        StringBuilder txtFinal = new StringBuilder();
        //Bucle para conseguir la primera letra en mayuscula
        for(String palabra : palabras){
            txtFinal.append(palabra.substring(0,1).toUpperCase().concat( palabra.substring(1).toLowerCase()).concat(" "));
        }
        //Antes de devolverlo realizamos un trim por si ha metido espacios a la derecha de la ultima palabra
        return txtFinal.toString().trim();
    }
    //Metodo que recoge un String con el valor de un datapickertime, y nos devuelve un String para pasar a date de ese string:
    public static String obtieneFechaDataPicker(String aux){
        String fechaElegida="";
        int diaI=0, mesI=0, anoI=0;
        //Bucle for que se encarga de coger los valores numericos de la fecha
        //y guardarlo en variables de tipo Int:
        for (int i=0, j=0;i<aux.length();i++){
            if(aux.charAt(i)>='0'&&aux.charAt(i)<='9'){
                fechaElegida+=aux.charAt(i);
            }
            else if(aux.charAt(i)=='/'&&j==0){
                diaI += Integer.parseInt(fechaElegida);
                j++;
                fechaElegida="";
            }
            else if(aux.charAt(i)=='/'&&j==1){
                mesI += Integer.parseInt(fechaElegida);
                j++;
                fechaElegida="";
            }
            if(i==aux.length()-1&&j==2){
                anoI += Integer.parseInt(fechaElegida);
                j++;
                fechaElegida="";
            }
        }
        fechaElegida=anoI+"-"+mesI+"-"+diaI;
        return fechaElegida;
    }
}
