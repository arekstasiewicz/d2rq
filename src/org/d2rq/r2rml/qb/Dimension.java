package org.d2rq.r2rml.qb;

import org.w3c.dom.Node;

public class Dimension {

	private String label = null;
	private String uri = null;
	private String nick = null;
	
	
	public Dimension( Node node ){
		
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

}
