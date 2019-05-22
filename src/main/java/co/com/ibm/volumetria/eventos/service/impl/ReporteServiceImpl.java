package co.com.ibm.volumetria.eventos.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;

import co.com.ibm.volumetria.eventos.clasificacion.Clasificacion;
import co.com.ibm.volumetria.eventos.dto.AlertaDTO;
import co.com.ibm.volumetria.eventos.dto.EventoDTO;
import co.com.ibm.volumetria.eventos.service.ReporteService;
import co.com.ibm.volumetria.eventos.utils.Constantes;

@Service("reporteService")
public class ReporteServiceImpl implements ReporteService {

	@Autowired
	Clasificacion clasificacion;
	
	List<String> lineaBase = Arrays.asList(Constantes.VARIABLES_LINEA_BASE);
	
	@Override
	public Workbook generarReporte(Sheet ibm, Sheet cgm, CSVReader alertas) throws FileNotFoundException, IOException {

		List<EventoDTO> listaEventosIBM = construirListaEventosIBM(ibm);
		List<EventoDTO> listaEventosCGM = construirListaEventosCGM(cgm);
		List<AlertaDTO> listaAlertas = construirListaAlertas(alertas);
		
		Workbook reporteVolumetriaEventos = new XSSFWorkbook();

		construirDataIBM(listaEventosIBM, reporteVolumetriaEventos);
		construirDataCGM(listaEventosCGM, reporteVolumetriaEventos);
		construirDataAlertas(listaAlertas, reporteVolumetriaEventos);
		   
		return reporteVolumetriaEventos;
	}

	private void construirDataAlertas(List<AlertaDTO> listaAlertas, Workbook reporteVolumetriaEventos) {
		Sheet hojaAlerta = reporteVolumetriaEventos.createSheet(Constantes.NOMBRE_SHEET_ALERTAS);
		Row header = hojaAlerta.createRow(hojaAlerta.getLastRowNum());
		crearHeaderReporte(header,reporteVolumetriaEventos);
		CreationHelper createHelper = reporteVolumetriaEventos.getCreationHelper();
		CellStyle cellStyleDate = reporteVolumetriaEventos.createCellStyle();
	    cellStyleDate.setDataFormat(
		        createHelper.createDataFormat().getFormat(Constantes.FORMATO_CELDA_FECHA));
	    listaAlertas.forEach(alerta ->{
	    	Row row = hojaAlerta.createRow(hojaAlerta.getLastRowNum()+1);
	    	row.createCell(Constantes.CELDA_IDINCIDENTE_REPORTE).setCellValue(alerta.getServerSerial());
	    	row.createCell(Constantes.CELDA_SERVIDOR_REPORTE).setCellValue(alerta.getNode());
	    	row.createCell(Constantes.CELDA_DETALLESERVIDOR_REPORTE).setCellValue(alerta.getNodeAlias());
	    	row.createCell(Constantes.CELDA_FECHAALERTA_REPORTE).setCellValue(alerta.getFirstOccurrence());
		    row.getCell(Constantes.CELDA_FECHAALERTA_REPORTE).setCellStyle(cellStyleDate);
		    row.createCell(Constantes.CELDA_FAMILIA_REPORTE).setCellValue(Constantes.NO_APLICA);
		    row.createCell(Constantes.CELDA_TIPOFALLA_REPORTE).setCellValue(Constantes.TIPO_FALLA_ALERTA);
		    String clasificacionID;
			try {
				clasificacionID = alerta.getSubComponent().isEmpty() ? clasificacion.clasificacionCategoriaVariableAlertada(alerta.getSubComponent(), Constantes.SHEET_CONDICIONES_ALERTAS) : alerta.getSubComponent();
				System.out.println(clasificacionID);
				row.createCell(Constantes.CELDA_ID_REPORTE).setCellValue(clasificacionID);
				row.createCell(Constantes.CELDA_VARIABLEALERTADA_REPORTE).setCellValue(clasificacionID);
			} catch (EncryptedDocumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			row.createCell(Constantes.CELDA_DETALLEVARIABLEALERTADA_REPORTE).setCellValue(alerta.getSummary());
			row.createCell(Constantes.CELDA_CRITICOMAYOR_REPORTE).setCellValue(alerta.getSeverity());
			row.createCell(Constantes.CELDA_PLATAFORMA_REPORTE).setCellValue(alerta.getComponent());
			row.createCell(Constantes.CELDA_RESPONSABLE_REPORTE).setCellValue(Constantes.RESPONSABLE_ALERTAS);
			LocalDate localDateFirstOccurrence = alerta.getFirstOccurrence().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			row.createCell(Constantes.CELDA_MES_REPORTE).setCellValue(localDateFirstOccurrence.getMonthValue() + "-" + localDateFirstOccurrence.getYear());
			row.createCell(Constantes.CELDA_ESCALAMIENTO_REPORTE).setCellValue(clasificacion.asignarEscalamientoAlertas(alerta.getSummary().toLowerCase()));
			String pertenece = lineaBase.contains(row.getCell(Constantes.CELDA_ID_REPORTE).getStringCellValue().toUpperCase()) ? Constantes.LINEA_BASE : Constantes.LINEA_BASE_OTROS;
		    row.createCell(Constantes.CELDA_LINEA_BASE_REPORTE).setCellValue(pertenece);
	    });
		
	}

	private List<AlertaDTO> construirListaAlertas(CSVReader alertas) {
	    SimpleDateFormat formatterFechaAlerta = new SimpleDateFormat(Constantes.FORMATO_CONTRUCCION_FECHA);  
		List<AlertaDTO> listaAlertas = new ArrayList<AlertaDTO>();
		alertas.forEach(parametro ->{
			AlertaDTO alerta = new AlertaDTO();
			alerta.setServerSerial(parametro[Constantes.POSICION_SERVERSERIAL]);
			alerta.setSerial(parametro[Constantes.POSICION_SERIAL]);
			alerta.setComponent(parametro[Constantes.POSICION_COMPONENT]);
			alerta.setSubComponent(parametro[Constantes.POSICION_SUBCOMPONENT]);
			alerta.setEscalar(parametro[Constantes.POSICION_ESCALAR]);
			alerta.setNode(parametro[Constantes.POSICION_NODE]);
			alerta.setNodeAlias(parametro[Constantes.POSICION_NODEALIAS]);
			alerta.setSeverity(parametro[Constantes.POSICION_SEVERITY]);
			alerta.setSummary(parametro[Constantes.POSICION_SUMMARY]);
			try {
				alerta.setFirstOccurrence(formatterFechaAlerta.parse(parametro[Constantes.POSICION_FIRSTOCCURRENCE].replace("\"", "")));
				alerta.setLastOccurrence(formatterFechaAlerta.parse(parametro[Constantes.POSICION_LASTOCCURRENCE].replace("\"", "")));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(alerta);
			listaAlertas.add(alerta);
		});
		
		return listaAlertas;
	}
	
	private Function<String, AlertaDTO> mapearAlertaDTO = (linea) -> {
	
	    SimpleDateFormat formatterFechaIBM = new SimpleDateFormat(Constantes.FORMATO_CONTRUCCION_FECHA);  

		String[] parametro = linea.split(Constantes.CARACTER_SEPARADOR_ALERTAS);
		AlertaDTO alerta = new AlertaDTO();
		
		alerta.setServerSerial(parametro[Constantes.POSICION_SERVERSERIAL]);
		alerta.setSerial(parametro[Constantes.POSICION_SERIAL]);
		alerta.setComponent(parametro[Constantes.POSICION_COMPONENT]);
		alerta.setSubComponent(parametro[Constantes.POSICION_SUBCOMPONENT]);
		alerta.setEscalar(parametro[Constantes.POSICION_ESCALAR]);
		alerta.setNode(parametro[Constantes.POSICION_NODE]);
		alerta.setNodeAlias(parametro[Constantes.POSICION_NODEALIAS]);
		alerta.setSeverity(parametro[Constantes.POSICION_SEVERITY]);
		alerta.setSummary(parametro[Constantes.POSICION_SUMMARY]);
		try {
			alerta.setFirstOccurrence(formatterFechaIBM.parse(parametro[Constantes.POSICION_FIRSTOCCURRENCE]));
			alerta.setLastOccurrence(formatterFechaIBM.parse(parametro[Constantes.POSICION_LASTOCCURRENCE]));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return alerta;
	};

	private void construirDataCGM(List<EventoDTO> listaEventosCGM, Workbook reporteVolumetriaEventos) {
		Sheet hojaCGM = reporteVolumetriaEventos.createSheet(Constantes.NOMBRE_SHEET_CGM);
		Row header = hojaCGM.createRow(hojaCGM.getLastRowNum());
		crearHeaderReporte(header,reporteVolumetriaEventos);
		CreationHelper createHelper = reporteVolumetriaEventos.getCreationHelper();
		CellStyle cellStyleDate = reporteVolumetriaEventos.createCellStyle();
	    cellStyleDate.setDataFormat(
		        createHelper.createDataFormat().getFormat(Constantes.FORMATO_CELDA_FECHA));
	    listaEventosCGM.forEach(evento -> {
	    	Row row = hojaCGM.createRow(hojaCGM.getLastRowNum()+1);
  		    row.createCell(Constantes.CELDA_IDINCIDENTE_REPORTE).setCellValue(evento.getIdIncidente());
		    row.createCell(Constantes.CELDA_SERVIDOR_REPORTE).setCellValue(evento.getAplicacion());
		    row.createCell(Constantes.CELDA_DETALLESERVIDOR_REPORTE).setCellValue(evento.getClase());
		    row.createCell(Constantes.CELDA_FECHAALERTA_REPORTE).setCellValue(evento.getFechaApertura());;
		    row.getCell(Constantes.CELDA_FECHAALERTA_REPORTE).setCellStyle(cellStyleDate);
		    row.createCell(Constantes.CELDA_FAMILIA_REPORTE).setCellValue(evento.getFamilia());
		    row.createCell(Constantes.CELDA_TIPOFALLA_REPORTE).setCellValue(evento.getTipoFalla());
		    row.createCell(Constantes.CELDA_DETALLEVARIABLEALERTADA_REPORTE).setCellValue(evento.getDescripcion());
		    row.createCell(Constantes.CELDA_CRITICOMAYOR_REPORTE).setCellValue(Constantes.PRIORIDAD_CRITICA_VALOR);
		    row.createCell(Constantes.CELDA_PLATAFORMA_REPORTE).setCellValue(evento.getPlataforma().toUpperCase());
		    row.createCell(Constantes.CELDA_RESPONSABLE_REPORTE).setCellValue(evento.getResponsable());
		    try {
		    	String clasificacionID = clasificacion.clasificacionCategoriaVariableAlertada(evento.getDescripcion(), Constantes.SHEET_CONDICIONES_CGM);
			    row.createCell(Constantes.CELDA_ID_REPORTE).setCellValue(clasificacionID);
			    row.createCell(Constantes.CELDA_VARIABLEALERTADA_REPORTE).setCellValue(clasificacionID);	
			} catch (EncryptedDocumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    LocalDate localDateApertura = evento.getFechaApertura().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		    row.createCell(Constantes.CELDA_MES_REPORTE).setCellValue(localDateApertura.getMonthValue() + "-" + localDateApertura.getYear());
		    String pertenece = lineaBase.contains(row.getCell(Constantes.CELDA_ID_REPORTE).getStringCellValue().toUpperCase()) ? Constantes.LINEA_BASE : Constantes.LINEA_BASE_OTROS;
		    row.createCell(Constantes.CELDA_LINEA_BASE_REPORTE).setCellValue(pertenece);
			row.createCell(Constantes.CELDA_ESCALAMIENTO_REPORTE).setCellValue(Constantes.ESCALAMIENTO_ADMIN);
	    });
	}

	private void construirDataIBM(List<EventoDTO> listaEventosIBM, Workbook reporteVolumetriaEventos) {
		Sheet hojaIBM = reporteVolumetriaEventos.createSheet(Constantes.NOMBRE_SHEET_IBM);
		Row header = hojaIBM.createRow(hojaIBM.getLastRowNum());
		crearHeaderReporte(header, reporteVolumetriaEventos);
		CreationHelper createHelper = reporteVolumetriaEventos.getCreationHelper();
		CellStyle cellStyleDate = reporteVolumetriaEventos.createCellStyle();
		cellStyleDate.setDataFormat(
				createHelper.createDataFormat().getFormat(Constantes.FORMATO_CELDA_FECHA));
		listaEventosIBM.forEach(evento -> {
		   Row row = hojaIBM.createRow(hojaIBM.getLastRowNum()+1);
		   row.createCell(Constantes.CELDA_IDINCIDENTE_REPORTE).setCellValue(evento.getIdIncidente());
		   row.createCell(Constantes.CELDA_SERVIDOR_REPORTE).setCellValue(evento.getAplicacion());
		   row.createCell(Constantes.CELDA_DETALLESERVIDOR_REPORTE).setCellValue(evento.getClase());
		   row.createCell(Constantes.CELDA_FECHAALERTA_REPORTE).setCellValue(evento.getFechaApertura());
		   row.getCell(Constantes.CELDA_FECHAALERTA_REPORTE).setCellStyle(cellStyleDate);
		   row.createCell(Constantes.CELDA_FAMILIA_REPORTE).setCellValue(evento.getFamilia());
		   row.createCell(Constantes.CELDA_TIPOFALLA_REPORTE).setCellValue(evento.getTipoFalla());
		   row.createCell(Constantes.CELDA_DETALLEVARIABLEALERTADA_REPORTE).setCellValue(evento.getDescripcion());
		   row.createCell(Constantes.CELDA_CRITICOMAYOR_REPORTE).setCellValue(asignarPrioridad(evento.getPrioridad()));
		   try {
				row.createCell(Constantes.CELDA_PLATAFORMA_REPORTE).setCellValue(clasificacion.asignacionPlataformaPorAsignatario(evento.getUsuarioReporta(), evento.getGrupoReporta()));
			} catch (EncryptedDocumentException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		   row.createCell(Constantes.CELDA_RESPONSABLE_REPORTE).setCellValue(evento.getResponsable());
		   try {
			   String clasificacionID = clasificacion.clasificacionCategoriaVariableAlertada(evento.getDescripcion(), Constantes.SHEET_CONDICIONES_IBM);
			   System.out.println(clasificacionID);
			   row.createCell(Constantes.CELDA_ID_REPORTE).setCellValue(clasificacionID);
			   row.createCell(Constantes.CELDA_VARIABLEALERTADA_REPORTE).setCellValue(clasificacionID);
		   	} catch (EncryptedDocumentException | IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   	}
		   LocalDate localDateApertura = evento.getFechaApertura().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		   row.createCell(Constantes.CELDA_MES_REPORTE).setCellValue(localDateApertura.getMonthValue() + "-" + localDateApertura.getYear());
		   String pertenece = lineaBase.contains(row.getCell(Constantes.CELDA_ID_REPORTE).getStringCellValue().toUpperCase()) ? Constantes.LINEA_BASE : Constantes.LINEA_BASE_OTROS;
		   row.createCell(Constantes.CELDA_LINEA_BASE_REPORTE).setCellValue(pertenece);
		   row.createCell(Constantes.CELDA_ESCALAMIENTO_REPORTE).setCellValue(Constantes.ESCALAMIENTO_ADMIN);
		});
	}

	private int asignarPrioridad(String prioridad) {
		int prioridadValor = Constantes.PRIORIDAD_BAJA_VALOR;
		if(prioridad.equals(Constantes.PRIORIDAD_CRITICA)) {
			prioridadValor = Constantes.PRIORIDAD_CRITICA_VALOR;
		}else if(prioridad.equals(Constantes.PRIORIDAD_MEDIA)){
			prioridadValor = Constantes.PRIORIDAD_MEDIA_VALOR;
		}
		return prioridadValor;
	}

	private void crearHeaderReporte(Row header, Workbook reporteVolumetriaEventos) {
		CellStyle styleHeader = reporteVolumetriaEventos.createCellStyle();
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		styleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
		styleHeader.setBorderBottom(BorderStyle.THIN);
		styleHeader.setFillBackgroundColor(IndexedColors.WHITE1.getIndex());
		styleHeader.setFillPattern(FillPatternType.FINE_DOTS);
		Font fontHeader = reporteVolumetriaEventos.createFont();
		fontHeader.setFontHeightInPoints((short)12);
		fontHeader.setBold(true);
		styleHeader.setFont(fontHeader);

		header.createCell(Constantes.CELDA_IDINCIDENTE_REPORTE).setCellValue(Constantes.HEADER_IDINCIDENTE_REPORTE);
		header.getCell(Constantes.CELDA_IDINCIDENTE_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_SERVIDOR_REPORTE).setCellValue(Constantes.HEADER_SERVIDOR_REPORTE);
		header.getCell(Constantes.CELDA_SERVIDOR_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_DETALLESERVIDOR_REPORTE).setCellValue(Constantes.HEADER_DETALLESERVIDOR_REPORTE);
		header.getCell(Constantes.CELDA_DETALLESERVIDOR_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_FECHAALERTA_REPORTE).setCellValue(Constantes.HEADER_FECHAALERTA_REPORTE);
		header.getCell(Constantes.CELDA_FECHAALERTA_REPORTE).setCellStyle(styleHeader);;
		header.createCell(Constantes.CELDA_FAMILIA_REPORTE).setCellValue(Constantes.HEADER_FAMILIA_REPORTE);
		header.getCell(Constantes.CELDA_FAMILIA_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_TIPOFALLA_REPORTE).setCellValue(Constantes.HEADER_TIPOFALLA_REPORTE);
		header.getCell(Constantes.CELDA_TIPOFALLA_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_ID_REPORTE).setCellValue(Constantes.HEADER_ID_REPORTE);
		header.getCell(Constantes.CELDA_ID_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_VARIABLEALERTADA_REPORTE).setCellValue(Constantes.HEADER_VARIABLEALERTADA_REPORTE);
		header.getCell(Constantes.CELDA_VARIABLEALERTADA_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_DETALLEVARIABLEALERTADA_REPORTE).setCellValue(Constantes.HEADER_DETALLEVARIABLEALERTADA_REPORTE);
		header.getCell(Constantes.CELDA_DETALLEVARIABLEALERTADA_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_CRITICOMAYOR_REPORTE).setCellValue(Constantes.HEADER_CRITICOMAYOR_REPORTE);
		header.getCell(Constantes.CELDA_CRITICOMAYOR_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_PLATAFORMA_REPORTE).setCellValue(Constantes.HEADER_PLATAFORMA_REPORTE);
		header.getCell(Constantes.CELDA_PLATAFORMA_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_RESPONSABLE_REPORTE).setCellValue(Constantes.HEADER_RESPONSABLE_REPORTE);
		header.getCell(Constantes.CELDA_RESPONSABLE_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_MES_REPORTE).setCellValue(Constantes.HEADER_MES_REPORTE);
		header.getCell(Constantes.CELDA_MES_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_ESCALAMIENTO_REPORTE).setCellValue(Constantes.HEADER_ESCALAR_REPORTE);
		header.getCell(Constantes.CELDA_ESCALAMIENTO_REPORTE).setCellStyle(styleHeader);
		header.createCell(Constantes.CELDA_LINEA_BASE_REPORTE).setCellValue(Constantes.HEADER_LINEA_BASE_REPORTE);
		header.getCell(Constantes.CELDA_LINEA_BASE_REPORTE).setCellStyle(styleHeader);
	}

	private List<EventoDTO> construirListaEventosCGM(Sheet cgm) {
		List<EventoDTO> listaEventos = new ArrayList<EventoDTO>();
		SimpleDateFormat formatterFechaCGM = new SimpleDateFormat(Constantes.FORMATO_CELDA_FECHA);

		cgm.forEach(row -> {
			EventoDTO evento = new EventoDTO();
			evento.setIdIncidente(Integer.parseInt(row.getCell(Constantes.CELDA_IDINCIDENTE_CGM).getStringCellValue().replaceAll("\\s+","")));
			evento.setAplicacion(row.getCell(Constantes.CELDA_APLICACION_CGM).getStringCellValue());
			evento.setDescripcion(row.getCell(Constantes.CELDA_DESCRIPCION_CGM).getStringCellValue());
			evento.setFamilia(row.getCell(Constantes.CELDA_FAMILIA_CGM).getStringCellValue());
			try {
				evento.setFechaApertura(formatterFechaCGM.parse(row.getCell(Constantes.CELDA_FECHAAPERTURA_CGM).getStringCellValue()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			evento.setTipoFalla(row.getCell(Constantes.CELDA_TIPOFALLA_CGM).getStringCellValue());
			evento.setResponsable(Constantes.RESPONSABLE_CGM);
			evento.setVariableAlertada(row.getCell(Constantes.CELDA_VARIABLEALERTADA_CGM).getStringCellValue());
			evento.setPlataforma(row.getCell(Constantes.CELDA_PLATAFORMA_CGM).getStringCellValue());
			listaEventos.add(evento);
		});
		
		return listaEventos;
	}

	private List<EventoDTO> construirListaEventosIBM(Sheet ibm) {
		List<EventoDTO> listaEventos = new ArrayList<EventoDTO>();
	    SimpleDateFormat formatterFechaIBM = new SimpleDateFormat(Constantes.FORMATO_CONTRUCCION_FECHA);  

	    ibm.forEach(row -> {
			EventoDTO evento = new EventoDTO();
			evento.setIdIncidente(Integer.parseInt(row.getCell(Constantes.CELDA_IDINCIDENTE_IBM).getStringCellValue()));
			evento.setAplicacion(row.getCell(Constantes.CELDA_APLICACION_IBM).getStringCellValue());
			evento.setClase(row.getCell(Constantes.CELDA_CLASE_IBM).getStringCellValue());
			evento.setDescripcion(row.getCell(Constantes.CELDA_DESCRIPCION_IBM).getStringCellValue());
			evento.setFamilia(row.getCell(Constantes.CELDA_FAMILIA_IBM).getStringCellValue());
			try {
				evento.setFechaApertura(formatterFechaIBM.parse(row.getCell(Constantes.CELDA_FECHAAPERTURA_IBM).getStringCellValue()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			evento.setGrupoReporta(row.getCell(Constantes.CELDA_GRUPOREPORTA_IBM).getStringCellValue());
			evento.setPrioridad(row.getCell(Constantes.CELDA_PRIORIDAD_IBM).getStringCellValue());
			evento.setTipoFalla(row.getCell(Constantes.CELDA_TIPOFALLA_IBM).getStringCellValue());
			evento.setUsuarioReporta(row.getCell(Constantes.CELDA_USUARIOREPORTA_IBM).getStringCellValue());
			evento.setResponsable(Constantes.RESPONSABLE_IBM);
			evento.setId(row.getCell(Constantes.CELDA_ID_CGM).getStringCellValue());
			listaEventos.add(evento);
		});
	    
	    return listaEventos;
	}

}
