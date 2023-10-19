package app.domain;

//페이징을 하기 위한 기준이 되는 데이터 클래스
public class Criteria {
	
	private int page; //페이지 번호를 담는 변수
	private int perPageNum; // 리스트의 게시물 수  몇개씩 노출시킬지
	
	public Criteria() {
		this.page=1;
		this.perPageNum=15;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerPageNum() {
		return perPageNum;
	}

	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}
	
	
	
	
	
	
	
}
