package co.com.ibm.volumetria.eventos.dto;

import java.util.Date;

public class AlertaDTO {

	private String serverSerial;
	private String serial;
	private String component;
	private String subComponent;
	private String escalar;
	private String node;
	private String nodeAlias;
	private String severity;
	private String summary;
	private Date firstOccurrence;
	private Date lastOccurrence;
	
	public AlertaDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AlertaDTO(String serverSerial, String serial, String component, String subComponent, String escalar,
			String node, String nodeAlias, String severity, String summary, Date firstOccurrence, Date lastOccurrence) {
		super();
		this.serverSerial = serverSerial;
		this.serial = serial;
		this.component = component;
		this.subComponent = subComponent;
		this.escalar = escalar;
		this.node = node;
		this.nodeAlias = nodeAlias;
		this.severity = severity;
		this.summary = summary;
		this.firstOccurrence = firstOccurrence;
		this.lastOccurrence = lastOccurrence;
	}

	public String getServerSerial() {
		return serverSerial;
	}

	public void setServerSerial(String serverSerial) {
		this.serverSerial = serverSerial;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getSubComponent() {
		return subComponent;
	}

	public void setSubComponent(String subComponent) {
		this.subComponent = subComponent;
	}

	public String getEscalar() {
		return escalar;
	}

	public void setEscalar(String escalar) {
		this.escalar = escalar;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getNodeAlias() {
		return nodeAlias;
	}

	public void setNodeAlias(String nodeAlias) {
		this.nodeAlias = nodeAlias;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getFirstOccurrence() {
		return firstOccurrence;
	}

	public void setFirstOccurrence(Date firstOccurrence) {
		this.firstOccurrence = firstOccurrence;
	}

	public Date getLastOccurrence() {
		return lastOccurrence;
	}

	public void setLastOccurrence(Date lastOccurrence) {
		this.lastOccurrence = lastOccurrence;
	}
	
}
