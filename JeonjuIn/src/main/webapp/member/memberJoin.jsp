<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>회원가입</title>
<link href="../css/member.css" type="text/css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	
	//alert("test");	
	$("#btn").on("click",function(){
		//alert("클릭했습니다.");
		
		let memberId = $("#memberId").val();
		
		$.ajax({
			type: "post",
			url : "./memberIdCheck.jsp",	
			data : {"memberId" : memberId},
			dataType : "json", 
			success : function(data){
				//alert("성공");
				//alert(data.cnt);
				if (data.cnt ==0){
					alert("사용할수 있는 아이디입니다.");
				}else{
					alert("사용할수 없는 아이디입니다.");
				}
				
			},
			error : function(){
				alert("실패");				
			}			
		});
		
	});
	
	
});

</script>

</head>
<body>
<script>
function check(){
	//alert("체크함수들어옴");
	
	//let memberId = document.frm.memberId.value;
	//alert("입력된 아이디는?"+memberId);
	var fm = document.frm; //문서객체안의 폼객체이름
	
	//alert(document.frm.memberHobby[0].value);
	//alert(document.frm.memberHobby[1].checked);
	
	if(fm.memberId.value ==""){
		alert("아이디를 입력하세요");
		fm.memberId.focus();
		return;
	}else if (fm.memberPwd.value ==""){
		alert("비밀번호를 입력하세요");
		fm.memberPwd.focus();
		return;		
	}else if (fm.memberPwd2.value ==""){
		alert("비밀번호 확인을 입력하세요");
		fm.memberPwd2.focus();
		return;		
	}else if(fm.memberPwd.value !=fm.memberPwd2.value){
		alert("비밀번호가 일치하지 않습니다.");
		fm.memberPwd.value="";
		fm.memberPwd2.value="";
		fm.memberPwd.focus();
		return;
	}else if (fm.memberName.value ==""){
		alert("이름을 입력하세요");
		fm.memberName.focus();
		return;		
	}else if (fm.memberPhone.value ==""){
		alert("핸드폰 번호를 입력하세요");
		fm.memberPhone.focus();
		return;		
	}else if (fm.memberEmail.value ==""){
		alert("이메일을 입력하세요");
		fm.memberEmail.focus();
		return;		
	}else if (!CheckEmail(fm.memberEmail.value)){
		alert("이메일 형식이 유효하지 않습니다.");
		fm.memberEmail.value="";
		fm.memberEmail.focus();
		return;		
	}else{
		//취미체크 확인함수(취미는 값이 여러개라 memberHobby배열로 담는다)
		var tf = checkYn(fm.memberHobby);
		if(tf==false){
			return;  //결과값이 거짓이면 진행막기
		}
	}
	
	fm.action ="<%=request.getContextPath()%>/member/memberJoinAction.do";  //처리하기위해 이동하는 주소
	fm.method = "post";  //이동하는 방식  get 노출시킴 post 감추어서 전달
	fm.submit(); //전송시킴
	return;
}

//이메일이 잘못되었는지 확인하는 함수
function CheckEmail(str){ 
	//정규표현식 - 일정한 패턴에따라 해당되는 위치에 해당하는 값의 범위를 지정
     var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
     if(!reg_email.test(str)) { 
          return false;  
     }  
     else {
          return true; 
     } 
} 

//취미체크함수
function checkYn(str){
	
	var isChk =false;
	for(var i =0;i<str.length;i++){
		if (str[i].checked == true){
			isChk = true;
			break;
		}	
	}
	
	if (!isChk){
		alert("취미는 하나이상 선택하세요");
		return false;
	}	
}

</script>
<h1>회원가입페이지</h1>
	<form name="frm">
		<table border="1" style="width:600px">
		<tr>
		<th>아이디</th>
		<td>
		<input type="text" name="memberId" id="memberId" size="30" maxlength="30">
		<input type="button" name="btn" id="btn" value="아이디중복체크">
		</td>
		<th>닉네임</th>
		<td>
		<input type="text" name="nickName" id="nickName" size="30" maxlength="30">
		<input type="button" name="btn2" id="btn2" value="닉네임중복체크">
		</td>
		</tr>
		<tr>
		<th>비밀번호</th>
		<td><input type="password" name="memberPwd"></td>
		</tr>
		<tr>
		<th>비밀번호확인</th>
		<td><input type="password" name="memberPwd2"></td>
		</tr>
		<tr>
		<th>이름</th>
		<td><input type="text" name="memberName" style="width:100px;"></td>
		</tr>
		<tr>
		<th>생년월일</th>
		<td style="text-align:center;"><input type="date" name="memberBirth" style="width:250px;" min="1960-01-01" max="2005-12-31" /></td>      
		</tr>
		<tr>
		<th>성별</th>
		<td>
		<input type="radio" name="memberGender" value="남성"> 남성 
		<input type="radio" name="memberGender" value="여성" checked> 여성
		</td>
		</tr>
		<tr>
		<th>핸드폰</th>
		<td><input type="text" name="memberPhone" placeholder="010-0000-0000"></td>
		</tr>
		<tr>
		<th>이메일</th>
		<td><input type="email" name="memberEmail"></td>
		</tr>
		<tr>
		</tr>
		<tr>
		<td colspan="2" style="text-align:center;">
		<!--<input type="submit" name="smt" value="확인"> 데이터전송기능버튼 -->
		<input type="button" name="btn" value="확인" onclick="check();">
		</td>
		</tr>
		</table>
	</form>


</body>
</html>