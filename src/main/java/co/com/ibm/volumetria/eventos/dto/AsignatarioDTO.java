package co.com.ibm.volumetria.eventos.dto;

public class AsignatarioDTO {
	
	private String nombre;
	private String torre;
	
	public AsignatarioDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public AsignatarioDTO(String nombre, String torre) {
		super();
		this.nombre = nombre;
		this.torre = torre;
	}
	
	public String getTorre() {
		return torre;
	}
	public void setTorre(String torre) {
		this.torre = torre;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
}
