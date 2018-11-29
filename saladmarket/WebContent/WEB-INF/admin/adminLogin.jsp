<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="admin.model.AdminVO" %>

<% 
	String ctxPath = request.getContextPath(); %>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,600,700,800" rel="stylesheet" />
  <link href="https://use.fontawesome.com/releases/v5.0.6/css/all.css" rel="stylesheet">
  <link href="<%= ctxPath %>/assets/css/black-dashboard.css?v=1.0.0" rel="stylesheet" />
  <link href="<%= ctxPath %>/assets/demo/demo.css" rel="stylesheet" />
  <script src="<%= ctxPath %>/assets/js/core/jquery.min.js"></script>
  <script src="<%= ctxPath %>/assets/js/core/popper.min.js"></script>
  <script src="<%= ctxPath %>/assets/js/core/bootstrap.min.js"></script>
  <script src="<%= ctxPath %>/assets/js/plugins/perfect-scrollbar.jquery.min.js"></script>
  <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
  <script src="<%= ctxPath %>/assets/js/plugins/chartjs.min.js"></script>
  <script src="<%= ctxPath %>/assets/js/plugins/bootstrap-notify.js"></script>
  <script src="<%= ctxPath %>/assets/js/black-dashboard.min.js?v=1.0.0"></script>
  <script src="<%= ctxPath %>/assets/demo/demo.js"></script>
  
<style>
body {font-family: Arial, Helvetica, sans-serif;}

/* Full-width input fields */
input[type=text], input[type=password] {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    display: inline-block;
    border: 1px solid #ccc;
    box-sizing: border-box;
}

/* Set a style for all buttons */
button {
    background-color: #4CAF50;
    color: white;
    padding: 14px 20px;
    margin: 8px 0;
    border: none; 
    cursor: pointer;
    width: 100%;
}

button:hover {
    opacity: 0.8;
}

/* Extra styles for the cancel button */
.cancelbtn {
    width: auto;
    padding: 10px 18px;
}

/* Center the image and position the close button */
.imgcontainer {
    text-align: center;
    margin-top: 20%;
    position: relative;
}

img.avatar {
    width: 40%;
    border-radius: 50%;
}

.container {
    padding: 16px;
}

span.psw {
    float: right;
    padding-top: 16px;
}

/* Modal Content/Box */
.modal-content {
    background-color: #fefefe;
    margin: 5% auto 15% auto; /* 5% from the top, 15% from the bottom and centered */
    border: 1px solid #888;
    width: 80%; /* Could be more or less, depending on screen size */
}

/* The Close Button (x) */
.close {
    position: absolute;
    right: 25px;
    top: 0;
    font-size: 35px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: red;
    cursor: pointer;
}


/* Change styles for span and cancel button on extra small screens */
@media screen and (max-width: 300px) {
    span.psw {
       display: block;
       float: none;
    }
    .cancelbtn {
       width: 100%;
    }
}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		$("#userid").focus(); // 해당 페이지에 접근하면 즉각 실행됨
		$("#pwd").keydown(function(event){
			if(event.keyCode==13){ // keyCode 13 ; enter
				goAdminLogin();
			}
		});
	});

	function goAdminLogin(){
		
		var loginUserid = $("#userid").val().trim();
		var loginPwd = $("#pwd").val().trim();
		
		if(loginUserid==""){
			alert("아이디를 입력하세요.");
			$("#userid").val("");
			$("#userid").focus();
			return;
		}
		if(loginPwd==""){
			alert("패스워드를 입력하세요.");
			$("#pwd").val("");
			$("#pwd").focus();
			return;
		}
		
		var frm = document.adminLoginFrm;
		frm.method ="POST";
		frm.action="adminLoginEnd.do";
		frm.submit();
	}

</script>
</head>
<body>
<div class="row">
	<div class="col-md-4"></div>
	<div class="col-md-4">
	  <form name="adminLoginFrm">
	    <div class="imgcontainer">
	      <img src="<%= ctxPath %>/img/main_textlogo.png"/>
	    </div>
	
	    <div class="container">
<%	
AdminVO admin = (AdminVO)session.getAttribute("admin");
	if(admin == null){
		Cookie[] cookieArr = request.getCookies();
		
		String cookie_key = "";
		String cookie_value = "";
		boolean flag = false;
		if(cookieArr != null){
			for(Cookie c :cookieArr){
				cookie_key = c.getName();
				
				if("saveid".equals(cookie_key)){
					cookie_value = c.getValue();
					flag = true;
					break;
				}
			}
		}
	
%>  
	      <label for="userid"><b>아이디</b></label>
	      <input type="text" placeholder="아이디를 입력하세요" name="userid" id="userid" required
	      <% if(flag){ %>
				value="<%=cookie_value%>"					
			<% } %>
			/>
		  
	      <label for="pwd"><b>비밀번호</b></label>
	      <input type="password" placeholder="비밀번호를 입력하세요" name="pwd" id="pwd" required>
	        
	      <button type="button" class="btn" OnClick="goAdminLogin();">Login</button>
	      	<%	// #아이디저장 체크 여부에 따라서 체크박스 표시를 달리해줌 --> flag 값 이용
						if(flag == false){ // 체크해제		%>
	      <label>
	        <input type="checkbox" name="saveid" id="saveid"/> 로그인 정보 저장
	      </label>
	       <% } else { %>
	      <label>
	        <input type="checkbox" checked="checked" name="saveid" id="saveid" checked/> 로그인 정보 저장
	      </label>
	      <% } %>
	    </div>
	
	    <div class="container">
	      <button type="button" onclick="document.getElementById('id01').style.display='none'" class="btn btn-warning cancelbtn">Cancel</button>
	      <span class="psw"><a href="#">비밀번호 찾기</a></span>
	    </div>
	  </form>
	</div>
	<div class="col-md-4"></div>
</div>
<script>
<% }%>
</script>

</body>
</html>

