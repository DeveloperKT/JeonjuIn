package app.domain;

public class ImpoBoardVo {
	
	private int impobidx ;
	private String imposubject ;
	private String impocontents ;
	private String writeday;
	private int viewcnt ;
	
	public int getImpobidx() {
		return impobidx;
	}
	public void setImpobidx(int impobidx) {
		this.impobidx = impobidx;
	}
	public String getImposubject() {
		return imposubject;
	}
	public void setImposubject(String imposubject) {
		this.imposubject = imposubject;
	}
	public String getImpocontents() {
		return impocontents;
	}
	public void setImpocontents(String impocontents) {
		this.impocontents = impocontents;
	}
	public String getWriteday() {
		return writeday;
	}
	public void setWriteday(String writeday) {
		this.writeday = writeday;
	}
	public int getViewcnt() {
		return viewcnt;
	}
	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}
	public String getDelyn() {
		return delyn;
	}
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}
	private String delyn;
}
