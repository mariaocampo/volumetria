package co.com.ibm.volumetria.eventos.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.opencsv.CSVReader;

public interface ReporteService {

	public Workbook generarReporte(Sheet ibm, Sheet cgm, CSVReader alertas) throws FileNotFoundException, IOException;
}
