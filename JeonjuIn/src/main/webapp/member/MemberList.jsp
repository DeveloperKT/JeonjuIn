<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.*" %>    
<%@ page import="app.domain.*" %>  
<% 
//포워드는 공유속성때문에 넘겨받을수 있다
ArrayList<MemberVo> list = null;
list = (ArrayList<MemberVo>)request.getAttribute("list");%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>회원목록</title>
<link href="./css/member.css" type="text/css" rel="stylesheet">
</head>
<body>
<h1>회원목록</h1>
<br>
<table border="1" style="width:600px">
<thead>
		<tr>
		<th>회원번호</th>
		<th>아이디</th>
		<th>이름</th>		
		<th>가입일</th>
		</tr>
</thead>
<tbody>	

	<% for(MemberVo mv : list ) {%>
		<tr>
		<td><%=mv.getMidx() %></td>
		<td><%=mv.getMemberId() %></td>
		<td><%=mv.getMemberName() %></td>		
		<td><%=mv.getInsertday() %></td>
		</tr>
	<% } %>			
</tbody>		
</table>
</body>
</html>