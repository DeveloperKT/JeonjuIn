package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.dbconn.DbConn;
import app.domain.NoticeBoardVo;

public class NoticeBoardDao {
	//멤버변수 선언하고 전역으로 활용한다
	private Connection conn;  //멤버변수는 선언만해도 자동초기화됨
	private PreparedStatement pstmt;
		
	//생성자를 만든다
	public NoticeBoardDao() {
		DbConn dbconn = new DbConn();
		this.conn = dbconn.getConnection();
	}	
	
	public ArrayList<NoticeBoardVo>  boardSelectAll(SearchCriteria scri){
		//무한배열클래스 객체생성해서 데이터를 담을 준비를 한다
		ArrayList<NoticeBoardVo> alist =new ArrayList<NoticeBoardVo>();
		ResultSet rs = null;
		
		//System.out.println("searchType"+scri.getSearchType());
		//System.out.println("keyword"+scri.getKeyword());
		String str= "";
		if (!scri.getKeyword().equals("")) {
			str =" and "+scri.getSearchType()+" like concat('%','"+scri.getKeyword()+"','%') ";
		}		
		//System.out.println("str?"+str);
		
		String sql="select notibidx, notisubject,noticontents,writeday,viewcnt,\r\n"
				+ "from board0803 \r\n"
				+ "where delyn='N' \r\n"
				+ str
				+ "order by originbidx desc,depth asc limit ?,?";
		try{		
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (scri.getPage()-1)*scri.getPerPageNum());
			pstmt.setInt(2, scri.getPerPageNum());
			rs = pstmt.executeQuery();
			//rs.next()는 다음값이 있는지 확인하는 메소드 있으면true
			while(rs.next()){
				BoardVo bv = new BoardVo();
				//rs에서 midx값 꺼내서 mv에 옮겨담는다
				bv.setBidx( rs.getInt("bidx") ); 
				bv.setSubject( rs.getString("subject") );
				bv.setWriter( rs.getString("writer"));
				bv.setViewcnt( rs.getInt("viewcnt"));
				bv.setWriteday( rs.getString("writeday"));
				bv.setLevel_(rs.getInt("level_"));
				alist.add(bv);
			}		
			
		}catch(Exception e){
			e.printStackTrace();		
		}finally{
			try{
				rs.close();
				pstmt.close();
				//conn.close();
				}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return alist;
	} 
	
	
	public int boardInsert(NoticeBoardVo nbv){
		int exec = 0;
		
		String sql = "INSERT INTO board0803(originbidx, depth, level_,SUBJECT,CONTENTS,WRITER,midx,pwd,filename)\r\n"
				+ "VALUES(null,0,0,?,?,?,?,?,?)";
	
		String sql2 ="update board0803 set\r\n"
				+ "originbidx = (select A.bidx from (select max(bidx) as bidx from board0803)A)\r\n"
				+ "where bidx = (select A.bidx from (select max(bidx) as bidx from board0803)A)";
		
		try{
		conn.setAutoCommit(false);  //수동커밋
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, bv.getSubject());
		pstmt.setString(2, bv.getContents());
		pstmt.setString(3, bv.getWriter());
		pstmt.setInt(4, bv.getMidx());	
		pstmt.setString(5, bv.getPwd());
		pstmt.setString(6, bv.getFilename());
		pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(sql2);
		exec = pstmt.executeUpdate();
		
		conn.commit();
		
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return exec;	
	}
	
	public int boardTotalCount(SearchCriteria scri){
		int value=0;  // 결과값이 0인지 아닌지
		
		String str= "";
		if (!scri.getKeyword().equals("")) {
			str =" and "+scri.getSearchType()+" like concat('%','"+scri.getKeyword()+"','%') ";
		}		
		
		String sql="select count(*) as cnt from board0803 where delyn='N' "+str;
		ResultSet rs = null;
		try{
			pstmt = conn.prepareStatement(sql);			
			rs = pstmt.executeQuery();
			
			if (rs.next()){
				value =	rs.getInt("cnt");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//System.out.println("DAO 안에 value"+value);
		return value;
	}
	
	public BoardVo boardSelectOne(int bidx) {
		BoardVo bv = null;
		String sql="select * from board0803 where bidx=?";
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		//향상된 구문클래스를 꺼낸다
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				//bv생성하고 결과값 옮겨담기
				bv = new BoardVo();
				bv.setSubject(rs.getString("subject"));
				bv.setContents(rs.getString("contents"));
				bv.setWriter(rs.getString("writer"));
				bv.setBidx(rs.getInt("bidx"));
				bv.setViewcnt(rs.getInt("viewcnt"));
				bv.setOriginbidx(rs.getInt("originbidx"));
				bv.setDepth(rs.getInt("depth"));
				bv.setLevel_(rs.getInt("level_"));
			}			
		} catch (SQLException e) {			
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}	
		
		return bv;
	}
	
	public int boardCntUpdate(int bidx){
		int exec = 0;		
	
		String sql ="update board0803 set\r\n"
				+ "viewcnt = viewcnt+1\r\n"
				+ "where bidx = ?";
		
		try{		
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bidx);		
		exec = pstmt.executeUpdate();	
		
		}catch(Exception e){			
			e.printStackTrace();
		}
		return exec;	
	}
	
	
	public int boardModify(BoardVo bv){
		int exec = 0;		
	
		String sql ="update board0803 set\r\n"
				+ "subject = ?, \r\n"
				+ "contents = ?,\r\n"
				+ "writer = ?, \r\n"
				+ "modifyday = now(), \r\n"
				+ "ip = ? \r\n"
				+ "where bidx = ? and pwd = ? ";
		
		try{		
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, bv.getSubject());	
		pstmt.setString(2, bv.getContents());
		pstmt.setString(3, bv.getWriter());
		pstmt.setString(4, bv.getIp());
		pstmt.setInt(5, bv.getBidx());
		pstmt.setString(6, bv.getPwd());		
		exec = pstmt.executeUpdate();	
		//수정이 되면 1이 리턴된다.
		}catch(Exception e){			
			e.printStackTrace();
		}
		return exec;	
	}
	
	public int boardDelete(int bidx,String pwd){
		int exec = 0;		
	
		String sql ="update board0803 set \r\n"
				+ "delyn = 'Y', modifyday=now() \r\n"
				+ "where bidx = ? and pwd= ? ";
		
		try{		
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bidx);	
		pstmt.setString(2, pwd);
		exec = pstmt.executeUpdate();	
		
		}catch(Exception e){			
			e.printStackTrace();
		}
		return exec;	
	}
	
	
	public int boardReply(BoardVo bv){
		int exec = 0;
		
		String sql = "update board0803 set depth = depth+1 where depth > ? and originbidx = ?";
	
		String sql2 ="insert into board0803(originbidx,depth,level_,subject,contents,writer,midx,ip) \r\n"
				+ "values(?,?,?,?,?,?,?,?)";
		
		try{
		conn.setAutoCommit(false);  //수동커밋
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bv.getDepth());
		pstmt.setInt(2, bv.getOriginbidx());
		exec = pstmt.executeUpdate();
		
		pstmt = conn.prepareStatement(sql2);
		pstmt.setInt(1, bv.getOriginbidx());
		pstmt.setInt(2, bv.getDepth()+1);
		pstmt.setInt(3, bv.getLevel_()+1);
		pstmt.setString(4, bv.getSubject());
		pstmt.setString(5, bv.getContents());
		pstmt.setString(6, bv.getWriter());
		pstmt.setInt(7, bv.getMidx());
		pstmt.setString(8, bv.getIp());
		exec = pstmt.executeUpdate();
		
		conn.commit();
		
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {				
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return exec;	
	}
}
