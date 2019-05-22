package co.com.ibm.volumetria.eventos.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVReader;

import co.com.ibm.volumetria.eventos.service.ReporteService;
import co.com.ibm.volumetria.eventos.utils.Constantes;

@RestController
@RequestMapping("/reporte")
public class ReporteController {

	@Autowired
	@Qualifier("reporteService")
	ReporteService reporteService;
	
	@GetMapping("/prueba")
	public String prueba(){
		return "holi";
	}
	
	@RequestMapping("/generar-reporte-eventos")
	public String generarReporteEventos() throws EncryptedDocumentException, IOException {
		
		Sheet ibm = WorkbookFactory.create(new File(Constantes.IBM_XLSX_FILE_PATH)).getSheetAt(Constantes.INDEX_FILE);
		Sheet cgm = WorkbookFactory.create(new File(Constantes.CGM_XLSX_FILE_PATH)).getSheetAt(Constantes.INDEX_FILE);
		CSVReader alertas = new CSVReader(new FileReader(Constantes.ALERTAS_XLSX_FILE_PATH));
		Workbook workbookReporte = reporteService.generarReporte(ibm, cgm, alertas);
		
		try (OutputStream fileOut = new FileOutputStream("resultado.xlsx")) {
			workbookReporte.write(fileOut);
	    }
		workbookReporte.close();
		
		return "fin";
	}
}
