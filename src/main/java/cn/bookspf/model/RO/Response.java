package cn.bookspf.model.RO;

public class Response {
	protected boolean status;
	protected String mes;
	
	public Response() {}
	public Response(boolean status,String mes) {
		this.status=status;
		this.mes=mes;
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}

}
