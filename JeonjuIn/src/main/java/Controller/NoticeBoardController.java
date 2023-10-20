package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;

import app.domain.NoticeBoardVo;



@WebServlet("/NoticeBoardController")
public class NoticeBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String location; 
	public NoticeBoardController(String location){
		this.location = location;
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//문자열 비교
		if (location.equals("noticeboardList.do")) {	
			
			String searchType = request.getParameter("searchType");
			if (searchType ==null) searchType="subject";
			String keyword = request.getParameter("keyword");
			if (keyword ==null) keyword="";			
			String page = request.getParameter("page");
			if (page ==null) page ="1";
						
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(Integer.parseInt(page));	
			scri.setSearchType(searchType);
			scri.setKeyword(keyword);
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);
			
			BoardDao bd = new BoardDao();
			ArrayList<BoardVo> alist  = bd.boardSelectAll(scri);
			int cnt = bd.boardTotalCount(scri); //전체게시물수
			//System.out.println("cnt?"+cnt);
			pm.setTotalCount(cnt);
						
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			
			String path ="/board/noticeboardList.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);	
		
		}else if(location.equals("noticeboardWrite.do")) {
						
			 String path ="/board/noticeboardWrite.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
			
		}else if(location.equals("noticeboardWriteAction.do")) {
			
			String savePath="D:\\dev0803\\mvcstudy0803_t\\src\\main\\webapp\\images";
			int sizeLimit = 15*1024*1024; //15M
			String dataTy ="UTF-8";
			
			//파일이름 중복정책
			DefaultFileRenamePolicy drp = null;
			drp = new DefaultFileRenamePolicy();
			
			//다양한 파일과 데이터를 넘겨받는 통신요청객체
			MultipartRequest multi = null;
			multi = new MultipartRequest(request,savePath,sizeLimit,dataTy,drp);
			
			
			//1.넘긴값을 받는다
			String notisubject = multi.getParameter("notisubject");
			String noticontents = multi.getParameter("noticontents");
			
			//파일넘겨받기
			//열거자에 넘어오는 여러 파일이름을 담는다
			Enumeration files =   multi.getFileNames();
			//파일 객체를 꺼낸다
			String file = (String)files.nextElement();
			//그 파일의 이름을 추출한다(실제로 저장되는 파일이름)
			String fileName = multi.getFilesystemName(file);
			//원래 파일이름 추출
			String originFileName = multi.getOriginalFileName(file);
			
			int midx = 0;
			HttpSession session = request.getSession();
			midx = (int)session.getAttribute("midx");
			
			//2. 받은값을 입력한다
			NoticeBoardVo bv = new NoticeBoardVo();
			nbv.setSubject(subject);
			nbv.setContents(contents);
			nbv.setWriter(writer);
			nbv.setFilename(fileName);
			nbv.setPwd(pwd);
			nbv.setMidx(midx);
			
			BoardDao bd = new BoardDao();
			int value = bd.boardInsert(bv);
			
			//3. 처리가 끝났으면 새롭게 이동한다
			if (value ==0) { //입력 안되었으면 입력페이지
				String path = request.getContextPath()+"/board/boardWrite.do";
				response.sendRedirect(path);
			}else {	//입력되었으면 리스트 페이지로 이동		
				String path = request.getContextPath()+"/board/boardList.do";
				response.sendRedirect(path);
			}

		}else if(location.equals("boardContents.do")) {
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			
			BoardDao bd = new BoardDao();
			int exec = bd.boardCntUpdate(bidx_int);
			BoardVo bv = bd.boardSelectOne(bidx_int);
			
			request.setAttribute("bv", bv);
			
			String path ="/board/boardContents.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
			 
		}else if(location.equals("boardModify.do")) {
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx_int);
			
			request.setAttribute("bv", bv);			
			
			String path ="/board/boardModify.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
			 
		}else if(location.equals("boardModifyAction.do")) { 
			//1.수정데이터 넘겨받고
			String bidx = request.getParameter("bidx");
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String pwd = request.getParameter("pwd");
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			//2. 받은값을 입력한다
			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);			
			bv.setPwd(pwd);
			bv.setBidx(Integer.parseInt(bidx));
			bv.setIp(ip);
			
			BoardDao bd = new BoardDao();
			int value = bd.boardModify(bv);
			
			PrintWriter out = response.getWriter();
			
			//3. 처리가 끝났으면 새롭게 이동한다
			if (value ==0) { //수정 안되었으면 수정페이지
				
				 out.println("<script>alert('비밀번호가 일치하지 않습니다.');location.href='"+request.getContextPath()+"/board/boardModify.do?bidx="+bidx+"'</script>");	 
				 				
				//String path = request.getContextPath()+"/board/boardModify.do?bidx="+bidx;
				//response.sendRedirect(path);
			}else {	//수정되었으면 내용 페이지로 이동		
				String path = request.getContextPath()+"/board/boardContents.do?bidx="+bidx;
				response.sendRedirect(path);
			}		
		
		}else if(location.equals("boardDelete.do")) {
			
			String bidx  = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx_int);
			
			request.setAttribute("bv", bv);			
		
			String path ="/board/boardDelete.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다.같은 지역이므로
			 //공유해서 꺼내 쓸수 있다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
		
		}else if (location.equals("boardDeleteAction.do")) {
		
			String bidx  = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			String pwd = request.getParameter("pwd");
		
			//처리하는 메소드를 만들어야 한다
			int value=0;
			
			BoardDao bd = new BoardDao();
			value = bd.boardDelete(bidx_int, pwd);			
			
			//처리가 되면 1이 아니고 아니면 0나오는 변수값  value
			if (value !=0) {
				String path = request.getContextPath()+"/board/boardList.do";
				response.sendRedirect(path);				
			}else {
				String path = request.getContextPath()+"/board/boardDelete.do?bidx="+bidx;
				response.sendRedirect(path);				
			}			
			
		
		}else if(location.equals("boardReply.do")) {
			
			String bidx  = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			String originbidx  = request.getParameter("originbidx");
			int originbidx_int = Integer.parseInt(originbidx);
			String depth = request.getParameter("depth");
			int depth_int = Integer.parseInt(depth);
			String level_  = request.getParameter("level_");
			int level_int = Integer.parseInt(level_);
			
			BoardVo bv = new BoardVo();
			bv.setBidx(bidx_int);
			bv.setOriginbidx(originbidx_int);
			bv.setDepth(depth_int);
			bv.setLevel_(level_int);		
			
			request.setAttribute("bv", bv);
		
			String path ="/board/boardReply.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다 같은공간에 있어 토스가능
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
		}else if(location.equals("boardReplyAction.do")) {
			
			String bidx  = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			String originbidx  = request.getParameter("originbidx");
			int originbidx_int = Integer.parseInt(originbidx);
			String depth = request.getParameter("depth");
			int depth_int = Integer.parseInt(depth);
			String level_  = request.getParameter("level_");
			int level_int = Integer.parseInt(level_);
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String pwd = request.getParameter("pwd");
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			int midx = 0;
			HttpSession session = request.getSession();
			midx = (int)session.getAttribute("midx");
			
			BoardVo bv = new BoardVo();
			bv.setBidx(bidx_int);
			bv.setOriginbidx(originbidx_int);
			bv.setDepth(depth_int);
			bv.setLevel_(level_int);		
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);			
			bv.setPwd(pwd);
			bv.setIp(ip);
			bv.setMidx(midx);
			
			int value=0;
			//처리하는 메소드를 만든다
			
			BoardDao bd = new BoardDao();  //객체생성
			value = bd.boardReply(bv);			
			
			if (value !=0) {
				String path = request.getContextPath()+"/board/boardList.do";
				response.sendRedirect(path);				
			}else {
				String path = request.getContextPath()+"/board/boardReply.do?bidx="+bidx+"&originbidx="+originbidx+"&depth="+depth+"&level_="+level_+"";
				response.sendRedirect(path);				
			}		
			
			
		}
		
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}