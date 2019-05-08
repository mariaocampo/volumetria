package co.com.ibm.volumetria.eventos.clasificacion;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	
}
