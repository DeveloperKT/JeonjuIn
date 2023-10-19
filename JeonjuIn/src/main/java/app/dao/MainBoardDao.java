package app.dao;
// hiu
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.dbconn.DbConn;
import app.domain.Criteria;
import app.domain.MainBoardVo;
import app.domain.MemberVo;
import app.domain.SearchCriteria;

//DB board0803占쏙옙 占쏙옙占쏙옙占쌔쇽옙 占쏙옙占쏙옙占싶몌옙 占쏙옙占쏙옙占승댐옙
public class MainBoardDao {

	//private Connection conn;  //占쏙옙占쏙옙占쏙옙占쏙옙占� 占쏙옙占쏙옙占쌔듸옙 占쌘듸옙占십깍옙화占쏙옙
	//private PreparedStatement pstmt;
	
	//占쏙옙占쏙옙占쌘몌옙 占쏙옙占쏙옙占�
	//public MainBoardDao() {
		//DbConn dbconn = new DbConn();
		this.conn = dbconn.getConnection();
	}
	
	public ArrayList<MainBoardVo>  boardSelectAll(SearchCriteria scri){
		//占쏙옙占싼배열클占쏙옙占쏙옙 占쏙옙체占쏙옙占쏙옙占쌔쇽옙 占쏙옙占쏙옙占싶몌옙 占쏙옙占쏙옙 占쌔븝옙 占싼댐옙
		ArrayList<MainBoardVo> alist =new ArrayList<MainBoardVo>();
		ResultSet rs = null;
		
		
		String str="";
		if(!scri.getKeyword().equals("")) {
			str=" and "+scri.getSearchType()+" like concat('%','"+scri.getKeyword()+"','%')";
		}
		String sql="select bidx,subject,writer,viewcnt,writeday,level_\r\n"
				+ "from board0803 \r\n"
				+ "where delyn='N'\r\n"
				+ str
				+ "order by originbidx DESC,depth limit ?,?";
		try{
			//1.창占쏙옙(占시뤄옙占쏙옙)占쏙옙 占쏙옙占쏙옙占�
			//2.占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쌔쇽옙 占쏙옙占쏙옙占싶몌옙 占쏙옙占쎈객체占쏙옙 占쏙옙틸쨈占� 
			//3.占쏙옙占쎈객체占쏙옙 占쌍댐옙 占쏙옙占쏙옙占싶몌옙 회占쏙옙占쏙옙체(MemberVo)占쏙옙 占신겨댐옙쨈占� 
			//4.회占쏙옙占쏙옙체占쏙옙 창占쏙옙 占쏙옙占쏙옙獵쨈占�	
			
			//占쏙옙占쏙옙(占쏙옙占쏙옙)占쏙옙체
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,(scri.getPage()-1)*scri.getPerPageNum());
			pstmt.setInt(2,scri.getPerPageNum());
			
			//DB占쏙옙 占쌍댐옙 占쏙옙占쏙옙 占쏙옙틸占쏙옙占� 占쏙옙占쎈객체
			rs = pstmt.executeQuery();
			//rs.next()占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쌍댐옙占쏙옙 확占쏙옙占싹댐옙 占쌨소듸옙 占쏙옙占쏙옙占쏙옙true
			while(rs.next()){
				BoardVo bv = new BoardVo();
				//rs占쏙옙占쏙옙 midx占쏙옙 占쏙옙占쏙옙占쏙옙 mv占쏙옙 占신겨댐옙쨈占�
				bv.setBidx( rs.getInt("Bidx") ); 
				bv.setSubject( rs.getString("Subject") );
				bv.setWriter( rs.getString("Writer"));
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
			//	conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return alist;
	}
	public int BoardInsert(BoardVo bv) {
			
		int exec = 0;
		
		String sql = "INSERT INTO board0803(originbidx,depth,level_,SUBJECT,CONTENTS,WRITER,midx,pwd,filename)\r\n"
				+ "VALUES(Null,0,0,?,?,?,?,?,?)";
		String sql2 = "UPDATE board0803 set \r\n"
				+ "originbidx =(SELECT a.bidx from(select max(bidx) as bidx from board0803 )a)\r\n"
				+ "where bidx=(SELECT a.bidx from(select max(bidx) as bidx from board0803 )a)";
		
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
		int value=0;  // 占쏙옙占쏙옙占쏙옙占� 0占쏙옙占쏙옙 占싣댐옙占쏙옙
		
		String str="";
		if(!scri.getKeyword().equals("")) {
			str=" and "+scri.getSearchType()+" like concat('%','"+scri.getKeyword()+"','%')";
		}
		
		String sql="select count(*) as cnt from board0803 where delyn='N'"+str;
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
		
		return value;
	}
	
	public BoardVo boardSelectOne(int bidx) {
		BoardVo bv = null;
		String sql="select * from board0803 where bidx=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//占쏙옙占쏙옙 占쏙옙占쏙옙 클占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				//bv占쏙옙占쏙옙占싹곤옙 占쏙옙占쏙옙占� 占신겨댐옙占�
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
	
	public int BoardCntUpdate(int bidx) {
		
		int exec = 0;
		
		String sql = "UPDATE board0803 set \r\n"
				+ "viewcnt =viewcnt+1\r\n"
				+ "where bidx=?";
		
		try{
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1,bidx);
		exec = pstmt.executeUpdate();

		}catch(Exception e){
			e.printStackTrace();
		}	
			return exec;			
	}
	
	public int BoardModify(BoardVo bv) {
		
		int exec = 0;
		
		
		String sql = "UPDATE board0803 set \r\n"
				+ "subject =?, \r\n"
				+ "contents =?, \r\n"
				+ "writer =?, \r\n"
				+ "modifyday = now(), \r\n"
				+ "ip =? \r\n"
				+ "where bidx = ? and pwd = ? ";
		
		try{
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,bv.getSubject());
		pstmt.setString(2,bv.getContents());
		pstmt.setString(3,bv.getWriter());
		pstmt.setString(4,bv.getIp());
		pstmt.setInt(5,bv.getBidx());
		pstmt.setString(6,bv.getPwd());
		
		exec = pstmt.executeUpdate();
		//占쏙옙占쏙옙占쏙옙 占실몌옙 1占쏙옙 占쏙옙占쏙옙
		}catch(Exception e){
			e.printStackTrace();
		}	
			return exec;			
	}
	public int boardDelete(int bidx,String pwd) {
		
		int exec = 0;
		
		
		String sql ="UPDATE board0803 set \r\n"
				+ "delyn='Y', modifyday=now() \r\n"
				+ "where bidx=? and pwd=? ";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,bidx);
			pstmt.setString(2,pwd);
			exec = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return exec;
	}
	public int boardReply(BoardVo bv) {
		
		int exec = 0;
		
		String sql = "UPDATE board0803 set \r\n"
				+ "depth =depth+1\r\n"
				+ "where depth>?";
		String sql2 = "INSERT INTO board0803(originbidx,depth,level_,SUBJECT,CONTENTS,WRITER,midx,ip)\r\n"
				+ "VALUES(?,?,?,?,?,?,?,?)";
		
		try{
		conn.setAutoCommit(false);		//占쏙옙占쏙옙커占쏙옙
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bv.getDepth());
		pstmt.executeUpdate();
		
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
		
		conn.setAutoCommit(true);
		
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return exec ;
	}

	private String pwd() {
		// TODO Auto-generated method stub
		return null;
	}

	private int bidx() {
		// TODO Auto-generated method stub
		return 0;
	}
}
