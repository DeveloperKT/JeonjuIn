package app.domain;

public class MainBoardVo {
	
	private int bidx;
	private String subject;
	private String contents;
	private int viewcnt;
	private int recommend;
	private String writeday;
	private String ip;
	
	public int getBidx() {
		return bidx;
	}
	public void setBidx(int bidx) {
		this.bidx = bidx;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getViewcnt() {
		return viewcnt;
	}
	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}
	public int getRecommend() {
		return recommend;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	public String getWriteday() {
		return writeday;
	}
	public void setWriteday(String writeday) {
		this.writeday = writeday;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDelyn() {
		return delyn;
	}
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}
	public int getMidx() {
		return midx;
	}
	public void setMidx(int midx) {
		this.midx = midx;
	}
	public String getModifyday() {
		return modifyday;
	}
	public void setModifyday(String modifyday) {
		this.modifyday = modifyday;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	private String delyn;
	private int midx;
	private String modifyday;
	private String filename;
	
}
