package co.com.ibm.volumetria.eventos.dto;

import java.util.regex.Pattern;

public class CondicionDTO {

	public String condicion;
	public String categoria;
	public Pattern expresion;
	
	public CondicionDTO() {
	}
	
	public CondicionDTO(String condicion, String categoria, Pattern expresion) {
		super();
		this.condicion = condicion;
		this.categoria = categoria;
		this.expresion = expresion;
	}
	
	public String getCondicion() {
		return condicion;
	}
	public void setCondicion(String condicion) {
		this.condicion = condicion;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public Pattern getExpresion() {
		return expresion;
	}

	public void setExpresion(Pattern expresion) {
		this.expresion = expresion;
	}
}
