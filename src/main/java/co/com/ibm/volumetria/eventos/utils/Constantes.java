package co.com.ibm.volumetria.eventos.utils;

import java.util.regex.Pattern;

public class Constantes {
	//Constantes archivos excel
    public static final String IBM_XLSX_FILE_PATH = "./reportes/ibm.xlsx";
    public static final String CGM_XLSX_FILE_PATH = "./reportes/cgm.xlsx";
    public static final String CONDICIONES_XLSX_FILE_PATH = "./reportes/condiciones.xlsx";
    public static final String ALERTAS_XLSX_FILE_PATH = "./reportes/alertas.csv";
    public static final int INDEX_FILE = 0;
    
    //Constantes posicion archivos excel IBM
    public static final int CELDA_IDINCIDENTE_IBM = 0;
    public static final int CELDA_FECHAAPERTURA_IBM = 2;
    public static final int CELDA_USUARIOREPORTA_IBM = 4;
    public static final int CELDA_GRUPOREPORTA_IBM = 6;
    public static final int CELDA_DESCRIPCION_IBM = 7;
    public static final int CELDA_PRIORIDAD_IBM = 17;
    public static final int CELDA_APLICACION_IBM = 38;
    public static final int CELDA_TIPOFALLA_IBM = 39;
    public static final int CELDA_CLASE_IBM = 40;
    public static final int CELDA_FAMILIA_IBM = 41;   
    
    //Constantes posicion archivos excel CGM
    public static final int CELDA_IDINCIDENTE_CGM = 9;
    public static final int CELDA_FECHAAPERTURA_CGM = 3;
    public static final int CELDA_DESCRIPCION_CGM = 8;
    public static final int CELDA_TIPOFALLA_CGM = 5;
    public static final int CELDA_FAMILIA_CGM = 4;
    public static final int CELDA_APLICACION_CGM = 1;
    public static final int CELDA_VARIABLEALERTADA_CGM = 7;
    public static final int CELDA_PLATAFORMA_CGM = 11;
    public static final int CELDA_ID_CGM = 6;
    
    //Constantes posicion archivos planos Alertas
    public static final int POSICION_SERVERSERIAL = 0;
    public static final int POSICION_SERIAL = 1;
    public static final int POSICION_COMPONENT = 2;
    public static final int POSICION_SUBCOMPONENT = 3;
    public static final int POSICION_ESCALAR = 4;
    public static final int POSICION_NODE = 5;
    public static final int POSICION_NODEALIAS = 6;
    public static final int POSICION_SEVERITY = 7;
    public static final int POSICION_SUMMARY = 8;
    public static final int POSICION_FIRSTOCCURRENCE = 9;
    public static final int POSICION_LASTOCCURRENCE = 10;
    public static final int CELDA_DATA_ALERTAS = 0;
    
    //Constantes manejo general
    public static final String RESPONSABLE_IBM = "ibm";
    public static final String RESPONSABLE_CGM = "cgm";
    public static final String RESPONSABLE_ALERTAS = "smi";
    public static final int PRIORIDAD_CGM = 5;
    public static final String FORMATO_CONTRUCCION_FECHA = "yyyy-MM-dd HH:mm:ss";
    public static final String[] VARIABLES_LINEA_BASE = {"ASP","DISCO","MEMORIA","FILE SYSTEM","CPU"};    
    public static final String LINEA_BASE = "Linea Base";
    public static final String LINEA_BASE_OTROS = "Otros";
    public static final String CARACTER_SEPARADOR_ALERTAS = ",";
    public static final String NO_APLICA = "N/A";
    public static final String TIPO_FALLA_ALERTA = "alerta";
    
    //Constantes posiciones celdas reporte final
    public static final int CELDA_IDINCIDENTE_REPORTE = 0;
    public static final int CELDA_SERVIDOR_REPORTE = 1;
    public static final int CELDA_DETALLESERVIDOR_REPORTE = 2;
    public static final int CELDA_FECHAALERTA_REPORTE = 3;
    public static final int CELDA_FAMILIA_REPORTE = 4;
    public static final int CELDA_TIPOFALLA_REPORTE = 5;
    public static final int CELDA_ID_REPORTE = 6;
    public static final int CELDA_VARIABLEALERTADA_REPORTE = 7;
    public static final int CELDA_DETALLEVARIABLEALERTADA_REPORTE = 8;
    public static final int CELDA_CRITICOMAYOR_REPORTE = 9;
    public static final int CELDA_PLATAFORMA_REPORTE = 10;
    public static final int CELDA_RESPONSABLE_REPORTE = 11;
    public static final int CELDA_MES_REPORTE = 12;
    public static final int CELDA_ESCALAMIENTO_REPORTE = 13;
    public static final int CELDA_LINEA_BASE_REPORTE = 14;
    public static final int CELDA_ASIGNATARIO_REPORTE = 15;
    public static final int CELDA_GRUPOREPORTA_REPORTE = 16;
    
    //Constantes nombres headers reporte final
    public static final String HEADER_IDINCIDENTE_REPORTE = "Evento_Critico";
    public static final String HEADER_SERVIDOR_REPORTE = "Servidor";
    public static final String HEADER_DETALLESERVIDOR_REPORTE = "Detalle.Servidor";
    public static final String HEADER_FECHAALERTA_REPORTE = "Fecha.Alerta";
    public static final String HEADER_FAMILIA_REPORTE = "Familia";
    public static final String HEADER_TIPOFALLA_REPORTE = "Tipo.Falla";
    public static final String HEADER_ID_REPORTE = "ID";
    public static final String HEADER_VARIABLEALERTADA_REPORTE = "Variable.Alertada";
    public static final String HEADER_DETALLEVARIABLEALERTADA_REPORTE = "Detalle.Variable.Alertada";
    public static final String HEADER_CRITICOMAYOR_REPORTE = "Critico_Mayor";
    public static final String HEADER_PLATAFORMA_REPORTE = "Plataforma";
    public static final String HEADER_RESPONSABLE_REPORTE = "Responsable";
    public static final String HEADER_MES_REPORTE = "mes";
    public static final String HEADER_ESCALAR_REPORTE = "escalar";
    public static final String HEADER_LINEA_BASE_REPORTE = "lineabase";
    public static final String NOMBRE_SHEET_IBM = "DATA IBM";
    public static final String NOMBRE_SHEET_CGM = "DATA CGM";
    public static final String FORMATO_CELDA_FECHA = "dd/MM/yyyy HH:mm:ss";
    public static final String NOMBRE_SHEET_ALERTAS = "DATA SMI";
    
    //Constantes file expresiones regulares
    public static final int SHEET_CONDICIONES_IBM = 0;
    public static final int SHEET_CONDICIONES_CGM = 1;
    public static final int SHEET_CONDICIONES_ALERTAS = 5;
    public static final int CELDA_EXPRESION_REGULAR = 0;
    public static final int CELDA_CATEGORIA = 1;
    public static final String CLASIFICACION_VARIABLES_OTRO = "OTRO";
    
    //Constantes prioridad
    public static final String PRIORIDAD_CRITICA = "Critica";
    public static final String PRIORIDAD_MEDIA = "Media";
    public static final int PRIORIDAD_CRITICA_VALOR = 5;
    public static final int PRIORIDAD_MEDIA_VALOR = 4;
    public static final int PRIORIDAD_BAJA_VALOR = 3;
    
    //Constantes file Asignatarios/Torres
    public static final String ASIGNATARIO_INVALIDO = "Valide la existencia del asignatario";
    public static final int SHEET_CONDICIONES_ASIGNATARIOS = 3;
    public static final int CELDA_ASIGNATARIO = 0;
    public static final int CELDA_TORRE_ASIGNATARIO = 1;
    public static final int SHEET_CONDICIONES_GRUPOS = 4;
    public static final int CELDA_GRUPO = 0;
    public static final int CELDA_TORRE_GRUPO = 1;
    
    //Constantes escalamiento
    public static final Pattern VALIDACION_SL = Pattern.compile("escalar\\W+(?:\\w+\\W+){0,3}?.*coordinador\\b|informar\\W+(?:\\w+\\W+){0,3}?.*coordinador\\b");
    public static final String ESCALAMIENTO_SL = "SL";
    public static final Pattern VALIDACION_SLL = Pattern.compile("escalar\\W+(?:\\w+\\W+){0,3}?.*team leader\\b|informar\\W+(?:\\w+\\W+){0,3}?.*team leader\\b");
    public static final String ESCALAMIENTO_SLL = "SLL";
    public static final String ESCALAMIENTO_ADMIN = "ADMINISTRADOR";
}
