package co.com.ibm.volumetria.eventos.dto;

import java.util.Date;

public class EventoDTO {

	private int idIncidente;
	private String aplicacion;
	private String clase;
	private Date fechaApertura;
	private String descripcion; 
	private String prioridad;
	private String usuarioReporta;
	private String grupoReporta;
	private String tipoFalla;
	private String familia;
	private String responsable;
	private String variableAlertada;
	private String plataforma;
	private String id;
	
	public EventoDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventoDTO(int idIncidente, String aplicacion, String clase, Date fechaApertura, String descripcion,
			String prioridad, String usuarioReporta, String grupoReporta, String tipoFalla, String familia,
			String responsable, String variableAlertada, String plataforma, String id) {
		super();
		this.idIncidente = idIncidente;
		this.aplicacion = aplicacion;
		this.clase = clase;
		this.fechaApertura = fechaApertura;
		this.descripcion = descripcion;
		this.prioridad = prioridad;
		this.usuarioReporta = usuarioReporta;
		this.grupoReporta = grupoReporta;
		this.tipoFalla = tipoFalla;
		this.familia = familia;
		this.responsable = responsable;
		this.variableAlertada = variableAlertada;
		this.plataforma = plataforma;
		this.id = id;
	}

	public int getIdIncidente() {
		return idIncidente;
	}

	public void setIdIncidente(int idIncidente) {
		this.idIncidente = idIncidente;
	}

	public String getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public Date getFechaApertura() {
		return fechaApertura;
	}

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}

	public String getUsuarioReporta() {
		return usuarioReporta;
	}

	public void setUsuarioReporta(String usuarioReporta) {
		this.usuarioReporta = usuarioReporta;
	}

	public String getGrupoReporta() {
		return grupoReporta;
	}

	public void setGrupoReporta(String grupoReporta) {
		this.grupoReporta = grupoReporta;
	}

	public String getTipoFalla() {
		return tipoFalla;
	}

	public void setTipoFalla(String tipoFalla) {
		this.tipoFalla = tipoFalla;
	}

	public String getFamilia() {
		return familia;
	}

	public void setFamilia(String familia) {
		this.familia = familia;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public String getVariableAlertada() {
		return variableAlertada;
	}

	public void setVariableAlertada(String variableAlertada) {
		this.variableAlertada = variableAlertada;
	}

	public String getPlataforma() {
		return plataforma;
	}

	public void setPlataforma(String plataforma) {
		this.plataforma = plataforma;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
