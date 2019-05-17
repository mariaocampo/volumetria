package co.com.ibm.volumetria.eventos.clasificacion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import co.com.ibm.volumetria.eventos.dto.CondicionDTO;
import co.com.ibm.volumetria.eventos.utils.Constantes;

@Component
public class Clasificacion  {

	public String clasificacionCategoriaVariableAlertada(String descripcionVariable, int hojaCondiciones) throws EncryptedDocumentException, IOException {
		String categoria = Constantes.CLASIFICACION_VARIABLES_OTRO;
		Sheet fileCondiciones = WorkbookFactory.create(new File(Constantes.CONDICIONES_XLSX_FILE_PATH)).getSheetAt(hojaCondiciones);
		List<CondicionDTO> condiciones = new ArrayList<>();
		fileCondiciones.forEach(row -> {
			CondicionDTO condicion = new CondicionDTO();
			condicion.setCondicion(row.getCell(Constantes.CELDA_EXPRESION_REGULAR).getStringCellValue());
			condicion.setCategoria(row.getCell(Constantes.CELDA_CATEGORIA).getStringCellValue());
			condicion.setExpresion(Pattern.compile(row.getCell(Constantes.CELDA_EXPRESION_REGULAR).getStringCellValue().toLowerCase()));
			condiciones.add(condicion);
		});
		
		for(CondicionDTO cond: condiciones) {
			if(cond.getExpresion().matcher(descripcionVariable.toLowerCase()).find()) {
				categoria = cond.categoria;
				break;
			}
		}
		
		fileCondiciones.getWorkbook().close();
		
		return categoria;
	}
	
	public String asignacionPlataformaPorAsignatario(String asignatario, String grupoReporta) throws EncryptedDocumentException, IOException {
		Sheet fileAsignatarios = WorkbookFactory.create(new File(Constantes.CONDICIONES_XLSX_FILE_PATH)).getSheetAt(Constantes.SHEET_CONDICIONES_ASIGNATARIOS);
		Map<String, String> mapAsignatarios = new HashMap<String, String>();
		fileAsignatarios.forEach(row -> {
			mapAsignatarios.put(row.getCell(Constantes.CELDA_ASIGNATARIO).getStringCellValue().toLowerCase(), row.getCell(Constantes.CELDA_TORRE_ASIGNATARIO).getStringCellValue());
		});
		
		fileAsignatarios.getWorkbook().close();
		String plataforma = mapAsignatarios.getOrDefault(asignatario.toLowerCase(), Constantes.ASIGNATARIO_INVALIDO);
		
		return !plataforma.equals(Constantes.ASIGNATARIO_INVALIDO) ? plataforma : validarPlataformaPorGrupo(grupoReporta);
	}

	private String validarPlataformaPorGrupo(String grupoReporta) throws EncryptedDocumentException, IOException {
		Sheet fileGrupos = WorkbookFactory.create(new File(Constantes.CONDICIONES_XLSX_FILE_PATH)).getSheetAt(Constantes.SHEET_CONDICIONES_GRUPOS);
		Map<String, String> mapGrupos = new HashMap<String, String>();
		fileGrupos.forEach(row -> {
			mapGrupos.put(row.getCell(Constantes.CELDA_GRUPO).getStringCellValue().toLowerCase(), row.getCell(Constantes.CELDA_TORRE_GRUPO).getStringCellValue());
		});
		
		fileGrupos.getWorkbook().close();
		
		return mapGrupos.getOrDefault(grupoReporta.toLowerCase(), Constantes.ASIGNATARIO_INVALIDO );
	}
	
	public String asignarEscalamientoAlertas(String descripcion) {
		String escalamiento = Constantes.ESCALAMIENTO_ADMIN;
		
		if(Constantes.VALIDACION_SL.matcher(descripcion).find()) {
			escalamiento = Constantes.ESCALAMIENTO_SL;
		}else if(Constantes.VALIDACION_SLL.matcher(descripcion).find()) {
			escalamiento = Constantes.ESCALAMIENTO_SLL;
		}
		
		return escalamiento;
	}
}
