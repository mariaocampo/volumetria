package co.com.ibm.volumetria.eventos.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import co.com.ibm.volumetria.eventos.clasificacion.Clasificacion;
import co.com.ibm.volumetria.eventos.dto.EventoDTO;
import co.com.ibm.volumetria.eventos.service.ReporteService;
import co.com.ibm.volumetria.eventos.utils.Constantes;

@Service("reporteService")
public class ReporteServiceImpl implements ReporteService {

	@Autowired
	Clasificacion clasificacion;
	
	@Override
	public Workbook generarReporte(Sheet ibm, Sheet cgm) throws FileNotFoundException, IOException {

		List<EventoDTO> listaEventosIBM = construirListaEventosIBM(ibm);
		List<EventoDTO> listaEventosCGM = construirListaEventosCGM(cgm);
		
		Workbook reporteVolumetriaEventos = new XSSFWorkbook();

		construirDataIBM(listaEventosIBM, reporteVolumetriaEventos);
		construirDataCGM(listaEventosCGM, reporteVolumetriaEventos);
		   
		return reporteVolumetriaEventos;
	}

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
		   row.createCell(Constantes.CELDA_PLATAFORMA_REPORTE).setCellValue(evento.getPlataforma());
		   row.createCell(Constantes.CELDA_RESPONSABLE_REPORTE).setCellValue(evento.getResponsable());
		   try {
			   String clasificacionID = clasificacion.clasificacionCategoriaVariableAlertada(evento.getDescripcion(), Constantes.SHEET_CONDICIONES_CGM);
			   row.createCell(Constantes.CELDA_ID_REPORTE).setCellValue(clasificacionID);
			   row.createCell(Constantes.CELDA_VARIABLEALERTADA_REPORTE).setCellValue(clasificacionID);	
			} catch (EncryptedDocumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		   row.createCell(Constantes.CELDA_PLATAFORMA_REPORTE).setCellValue(evento.getPlataforma());
		   row.createCell(Constantes.CELDA_RESPONSABLE_REPORTE).setCellValue(evento.getResponsable());
		   try {
			   String clasificacionID = clasificacion.clasificacionCategoriaVariableAlertada(evento.getDescripcion(), Constantes.SHEET_CONDICIONES_IBM);
			   row.createCell(Constantes.CELDA_ID_REPORTE).setCellValue(clasificacionID);
			   row.createCell(Constantes.CELDA_VARIABLEALERTADA_REPORTE).setCellValue(clasificacionID);
		   } catch (EncryptedDocumentException | IOException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
		});
	}

	private int asignarPrioridad(String prioridad) {
		int prioridadValor = Constantes.PRIORIDAD_BAJA_VALOR;
		if(prioridad == Constantes.PRIORIDAD_CRITICA) {
			prioridadValor = Constantes.PRIORIDAD_CRITICA_VALOR;
		}else if(prioridad == Constantes.PRIORIDAD_MEDIA){
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
