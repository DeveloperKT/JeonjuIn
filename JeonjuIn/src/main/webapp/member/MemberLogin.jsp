<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>회원로그인</title>
<link href="./css/member.css" type="text/css" rel="stylesheet">
<script type="text/javascript">
function check(){
	//alert("체크함수들어옴");
	
	//let memberId = document.frm.memberId.value;
	//alert("입력된 아이디는?"+memberId);
	var fm = document.frm; //문서객체안의 폼객체이름
	if(fm.memberId.value ==""){
		alert("아이디를 입력하세요");
		fm.memberId.focus();
		return;
	}else if (fm.memberPwd.value ==""){
		alert("비밀번호를 입력하세요");
		fm.memberPwd.focus();
		return;		
	}

	fm.action ="<%=request.getContextPath()%>/member/memberLoginAction.do";  //처리하기위해 이동하는 주소
	fm.method = "post";  //이동하는 방식  get 노출시킴 post 감추어서 전달
	fm.submit(); //전송시킴
	return;	
}

</script>

</head>
<body>
<h1>회원로그인</h1>
	<form name="frm">
		<table border="1" style="width:600px">
		<tr>
		<th style="width:200px">아이디</th>
		<td>
		<input type="text" name="memberId">	
		
		</td>
		</tr>
		<tr>
		<th>비밀번호</th>
		<td><input type="password" name="memberPwd"></td>
		</tr>		
		<tr>
		<td colspan="2" style="text-align:center;">
		<input type="button" name="smt" value="확인" onclick="check();"><!--데이터전송기능버튼 -->
		
		</td>
		</tr>
		</table>
	</form>


</body>
</html>