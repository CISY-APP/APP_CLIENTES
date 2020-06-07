//Indicamos a que paquete pertenece esta clase:
package com.example.app_clientes;
//Importamos los siguientes paquetes:
import com.example.app_clientes.pojos.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Clase donde se crearan distintos metodos utilizados por otras clases.
//Tambien se incluiran algunas variables globales estaticas para unificar su valor en un punto.
public class Biblioteca {
    //Variables u objetos publicos estaticos:
    public static long tAnimacionesScaleInicial = 500;          //Tiempo para las animaciones de scale al entrar a la ventana.
    public static long tAnimacionesScaleBotones = 200;          //Tiempo para las animaciones de scale al des/habilitar un boton.
    public static String ip = "http://192.168.0.103:8080";     //Ip a la que conectarse desde el cliente Retrofit.  http://167.99.80.138:8080/rest/
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
    //Metodo que calcula tu edad respecto a tu fecha de nacimiento y te la devuelve:
    public static String obtieneEdad(Date fechaNac_Date){
        //Declaramos distintos formateadores de fechas para obtener anio, y anio-mes-dia:
        SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatMes = new SimpleDateFormat("MM");
        SimpleDateFormat formatDia = new SimpleDateFormat("dd");
        SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy-MM-dd");
        //Obtenemos la fecha actual:
        Date fechaAct_date = new Date(Calendar.getInstance().getTime().getTime());
        //Obtenmos los anios, mes y dia de los date anteriores:
        String anioNacRv_string=formatAnio.format(fechaNac_Date);
        String anioActRv_string=formatAnio.format(fechaAct_date);
        String mesNacRv_string=formatMes.format(fechaNac_Date);
        String diaNacRv_string=formatDia.format(fechaNac_Date);
        //Esos anios los parseamos a enteros, para poder restar y sacar el numero de anios:
        int a = Integer.parseInt(anioNacRv_string);
        int b = Integer.parseInt(anioActRv_string);
        int anios=b-a;
        //Una vez ya tenemos los años que cumpliria el usuario en ese año, veamos si ya cumplio o no:
        //Para eso comparamos los dates, pero primero los parseamos a string en el formato fecha:
        String fechaNac_string=anioActRv_string+"-"+mesNacRv_string+"-"+diaNacRv_string;
        String fechaAct_string=formatFecha.format(fechaAct_date);
        //Y ahora los obtenemos en date:
        try {
            fechaNac_Date=formatFecha.parse(fechaNac_string);
            fechaAct_date=formatFecha.parse(fechaAct_string);
            //Y ahora hacemos la comparacion entre ellas, si la fecha de nacimiento con el anio de la actual es mayor que la actual, significa que todavia no ha cumplido anios, y se le resta uno:
            if(fechaNac_Date.compareTo(fechaAct_date)>0&&anios>0){
                anios--;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Devolvemos los anios contados.
        return anios+"";
    }
    //Metodo que calcula nos devuelve la fecha con hora para viajes encontrados, y le suma 2 horas mas por error en emulador, ademas el String viene formateado:
    public static String obtieneHoraViajesEncontrados(Date fecha){
        //Declaramos distintos formateadores de fechas para obtener anio, y anio-mes-dia:
        SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatMes = new SimpleDateFormat("MM");
        SimpleDateFormat formatDia = new SimpleDateFormat("dd");
        SimpleDateFormat formatHora = new SimpleDateFormat("HH");
        SimpleDateFormat formatMinuto = new SimpleDateFormat("mm");
        //Obtenemos el anio, mes, dia , hora, minuto:
        String anio_string = formatAnio.format(fecha);
        String mes_string = formatMes.format(fecha);
        String dia_string = formatDia.format(fecha);
        String hora_string = formatHora.format(fecha);
        String minuto_string = formatMinuto.format(fecha);
        //Parseamos los valores a numero para poder sumarle 2 horas a la fecha, y si sobrepasa los limites de horas, haga cadena y actualize todoo y se la volvemos a asignar a la cadena hora:
        /*int anio=Integer.parseInt(anio_string);int mes=Integer.parseInt(mes_string);int dia=Integer.parseInt(dia_string);int hora = Integer.parseInt(hora_string);
        //Obtenemos los dias que tiene el mes pasado:
        int diasMes=31;
        if(mes==1) {diasMes=31;}
        else if(mes==2) {	//En el mes de febrero calculamos si el anio es bisiesto para poder asignarle bien los dias:
            boolean pruebaBisiesto=false;
            if(anio%4==0) {
                if(anio%100!=0) {
                    pruebaBisiesto=true;
                }else if(anio%400==0){
                    pruebaBisiesto=true;
                }
            }
            if(pruebaBisiesto) {	//Si es anio bisiesto:
                diasMes=29;
            }else {	//Si no es bisiesto:
                diasMes=28;
            }
        }
        else if(mes==3) {diasMes=31;}else if(mes==4) {diasMes=30;}
        else if(mes==5) {diasMes=31;}else if(mes==6) {diasMes=30;}
        else if(mes==7) {diasMes=31;}else if(mes==8) {diasMes=31;}
        else if(mes==9) {diasMes=30;}else if(mes==10) {diasMes=31;}
        else if(mes==11) {diasMes=30;}else if(mes==12) {diasMes=31;}
        //Ahora con un bucle for le aumentamos las horas que necesitemos de una en una para que siga el siguiente algoritmo y si se desborda la hora, se reinicie y haga cadena consecutivamente:
        for (int i = 0; i < 2; i++) {
            hora+=1;
            if(hora==24) {
                hora=0;
                dia++;
                if(dia>diasMes) {
                    dia=1;
                    mes++;
                    if(mes>12) {
                        mes=1;
                        anio++;
                    }
                }
            }
        }
        anio_string = anio+"";
        mes_string = mes+"";
        dia_string = dia+"";
        hora_string = hora+"";*/
        //Ahora que tenemos la hora pasamos a reconstruir la fecha, pero en formato string:
        String fechaSalida_string = dia_string+" / "+mes_string+" / "+anio_string+" a las "+hora_string+":"+minuto_string;
        //Devolvemos el string con el formato de fecha que necesitabamos:
        return fechaSalida_string;
    }
    //Metodo que calcula nos devuelve la fecha con hora para viajes encontrados, y le suma 2 horas mas por error en emulador, ademas el String viene formateado:
    /*public static Date obtieneHoraMas2(Date fecha){
        //Declaramos distintos formateadores de fechas para obtener anio, y anio-mes-dia:
        SimpleDateFormat formatAnio = new SimpleDateFormat("yyyy");
        SimpleDateFormat formatMes = new SimpleDateFormat("MM");
        SimpleDateFormat formatDia = new SimpleDateFormat("dd");
        SimpleDateFormat formatHora = new SimpleDateFormat("HH");
        SimpleDateFormat formatMinuto = new SimpleDateFormat("mm");
        SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Obtenemos el anio, mes, dia , hora, minuto:
        String anio_string = formatAnio.format(fecha);
        String mes_string = formatMes.format(fecha);
        String dia_string = formatDia.format(fecha);
        String hora_string = formatHora.format(fecha);
        String minuto_string = formatMinuto.format(fecha);
        //Parseamos los valores a numero para poder sumarle 2 horas a la fecha, y si sobrepasa los limites de horas, haga cadena y actualize todoo y se la volvemos a asignar a la cadena hora:
        int anio=Integer.parseInt(anio_string);int mes=Integer.parseInt(mes_string);int dia=Integer.parseInt(dia_string);int hora = Integer.parseInt(hora_string);
        //Obtenemos los dias que tiene el mes pasado:
        int diasMes=31;
        if(mes==1) {diasMes=31;}
        else if(mes==2) {	//En el mes de febrero calculamos si el anio es bisiesto para poder asignarle bien los dias:
            boolean pruebaBisiesto=false;
            if(anio%4==0) {
                if(anio%100!=0) {
                    pruebaBisiesto=true;
                }else if(anio%400==0){
                    pruebaBisiesto=true;
                }
            }
            if(pruebaBisiesto) {	//Si es anio bisiesto:
                diasMes=29;
            }else {	//Si no es bisiesto:
                diasMes=28;
            }
        }
        else if(mes==3) {diasMes=31;}else if(mes==4) {diasMes=30;}
        else if(mes==5) {diasMes=31;}else if(mes==6) {diasMes=30;}
        else if(mes==7) {diasMes=31;}else if(mes==8) {diasMes=31;}
        else if(mes==9) {diasMes=30;}else if(mes==10) {diasMes=31;}
        else if(mes==11) {diasMes=30;}else if(mes==12) {diasMes=31;}
        //Ahora con un bucle for le aumentamos las horas que necesitemos de una en una para que siga el siguiente algoritmo y si se desborda la hora, se reinicie y haga cadena consecutivamente:
        for (int i = 0; i < 2; i++) {
            hora+=1;
            if(hora==24) {
                hora=0;
                dia++;
                if(dia>diasMes) {
                    dia=1;
                    mes++;
                    if(mes>12) {
                        mes=1;
                        anio++;
                    }
                }
            }
        }
        anio_string = anio+"";
        mes_string = mes+"";
        dia_string = dia+"";
        hora_string = hora+"";
        //Ahora que tenemos la hora pasamos a reconstruir la fecha, pero en formato string:
        String fechaSalida_string = anio_string+"-"+mes_string+"-"+dia_string+" "+hora_string+":"+minuto_string+":00";
        Date fechaFinal = null;
        try {
            fechaFinal = formatFecha.parse(fechaSalida_string);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Devolvemos el Date con el formato de fecha que necesitabamos y las dos horas sumadas de manera correcta:
        return fechaFinal;
    }*/
}
