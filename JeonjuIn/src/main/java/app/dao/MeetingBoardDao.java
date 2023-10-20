package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.dbconn.DbConn;
import app.domain.MeetingBoardVo;
import app.domain.Criteria;
import app.domain.MemberVo;
import app.domain.SearchCriteria;

//DB board0803�� �����ؼ� �����͸� �����´�
public class MeetingBoardDao {

	//������� �����ϰ� �������� Ȱ���Ѵ�
	private Connection conn;  //��������� �����ص� �ڵ��ʱ�ȭ��
	private PreparedStatement pstmt;
		
		//�����ڸ� �����
	public MeetingBoardDao() {
		DbConn dbconn = new DbConn();
		this.conn = dbconn.getConnection();
	}	
	
	public ArrayList<MeetingBoardVo>  boardSelectAll(SearchCriteria scri){
		//���ѹ迭Ŭ���� ��ü�����ؼ� �����͸� ���� �غ� �Ѵ�
		ArrayList<MeetingBoardVo> alist =new ArrayList<MeetingBoardVo>();
		ResultSet rs = null;
		
		//System.out.println("searchType"+scri.getSearchType());
		//System.out.println("keyword"+scri.getKeyword());
		String str= "";
		if (!scri.getKeyword().equals("")) {
			str =" and "+scri.getSearchType()+" like concat('%','"+scri.getKeyword()+"','%') ";
		}		
		//System.out.println("str?"+str);
		
		String sql="select mbidx, subject,viewcnt,writeday\r\n"
				+ "from meetingboard0803\r\n"
				+ "where delyn='N'\r\n"
				+ str
				+ "order by mbidx desc limit ?,?";
		try{		
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (scri.getPage()-1)*scri.getPerPageNum());
			pstmt.setInt(2, scri.getPerPageNum());
			rs = pstmt.executeQuery();
			//rs.next()�� �������� �ִ��� Ȯ���ϴ� �޼ҵ� ������true
			while(rs.next()){
				BoardVo bv = new BoardVo();
				//rs���� midx�� ������ mv�� �Űܴ�´�
				bv.setBidx( rs.getInt("bidx") ); 
				bv.setSubject( rs.getString("subject") );
				bv.setViewcnt( rs.getInt("viewcnt"));
				bv.setWriteday( rs.getString("writeday"));
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
	
	
	public int boardInsert(BoardVo bv){
		int exec = 0;
		
		String sql = "INSERT INTO board0803(originbidx, depth, level_,SUBJECT,CONTENTS,WRITER,midx,pwd,filename)\r\n"
				+ "VALUES(null,0,0,?,?,?,?,?,?)";

		try{
		conn.setAutoCommit(false);
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, bv.getSubject());
		pstmt.setString(2, bv.getContents());
		pstmt.setString(3, bv.getWriter());
		pstmt.setInt(4, bv.getMidx());	
		pstmt.setString(5, bv.getPwd());
		pstmt.setString(6, bv.getFilename());
		pstmt.executeUpdate();
		
		
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
		int value=0;  // ������� 0���� �ƴ���
		
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
		//System.out.println("DAO �ȿ� value"+value);
		return value;
	}
	
	public BoardVo boardSelectOne(int bidx) {
		BoardVo bv = null;
		MemberVo mv = null;
		String sql="select * from board0803 a join member b on a.midx = b.midx where bidx=?";
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		//���� ����Ŭ������ ������
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				//bv�����ϰ� ����� �Űܴ��
				bv = new BoardVo();
				mv = new MemberVo();
				bv.setSubject(rs.getString("subject"));
				bv.setContents(rs.getString("contents"));
				mv.setMemberId("memberid");
				mv.setNickname("nickname");
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
				+ "subject = ?,\r\n"
				+ "contents = ?,\r\n"
				+ "writer = ?,\r\n"
				+ "modifyday = now(),\r\n"
				+ "ip = ?\r\n"
				+ "where bidx = ? and pwd=?";
		
		try{		
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, bv.getSubject());	
		pstmt.setString(2, bv.getContents());
		pstmt.setString(3, bv.getWriter());
		pstmt.setString(4, bv.getIp());
		pstmt.setInt(5, bv.getBidx());
		pstmt.setString(6, bv.getPwd());
		
		exec = pstmt.executeUpdate();	
		
		}catch(Exception e){			
			e.printStackTrace();
		}
		return exec;	
	}
	
	public int boardDelete(int bidx,String pwd){
		int exec = 0;		
	
		String sql ="update board0803 set\r\n"
				+ "delyn = 'Y',modifyday=now()\r\n"
				+ "where bidx = ? and pwd=?";
		
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
		
		String sql = "update board0803 set depth = depth+1 where depth > ? and originbidx =?";
	
		String sql2 ="insert into board0803(originbidx,depth,level_,subject,contents,writer,midx,ip)\r\n"
				+ "values(?,?,?,?,?,?,?,?)";
		
		try{
		conn.setAutoCommit(false);
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
