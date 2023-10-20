package app.domain;

public class NoticeBoardVo {
	
	private int notibidx ;
	private String notisubject ;
	private String noticontents ;
	private String writeday;
	private int viewcnt;
	
	public int getNotibidx() {
		return notibidx;
	}
	public void setNotibidx(int notibidx) {
		this.notibidx = notibidx;
	}
	public String getNotisubject() {
		return notisubject;
	}
	public void setNotisubject(String notisubject) {
		this.notisubject = notisubject;
	}
	public String getNoticontents() {
		return noticontents;
	}
	public void setNoticontents(String noticontents) {
		this.noticontents = noticontents;
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
}
	
