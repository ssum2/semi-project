<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String ctxPath = request.getContextPath(); %>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" type="text/css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>

<!-- jQuery -->
<script src="<%=ctxPath %>/store/js/jquery.min.js"></script>

<!-- Animate.css -->
<link rel="stylesheet" href="<%=ctxPath %>/store/css/animate.css">
<!-- Icomoon Icon Fonts-->
<link rel="stylesheet" href="<%=ctxPath %>/store/css/icomoon.css">
<!-- Bootstrap  -->
<link rel="stylesheet" href="<%=ctxPath %>/store/css/bootstrap.css">

<!-- Magnific Popup -->
<link rel="stylesheet" href="<%=ctxPath %>/store/css/magnific-popup.css">

<!-- Flexslider  -->
<link rel="stylesheet" href="<%=ctxPath %>/store/css/flexslider.css">

<!-- Owl Carousel -->
<link rel="stylesheet" href="<%=ctxPath %>/store/css/owl.carousel.min.css">
<link rel="stylesheet" href="<%=ctxPath %>/store/css/owl.theme.default.min.css">

<!-- Date Picker -->
<link rel="stylesheet" href="<%=ctxPath %>/store/css/bootstrap-datepicker.css">
<!-- Flaticons  -->
<link rel="stylesheet" href="<%=ctxPath %>/store/fonts/flaticon/font/flaticon.css">

<!-- Theme style  -->
<link rel="stylesheet" href="<%=ctxPath %>/store/css/style.css">

<!-- Modernizr JS -->
<script src="<%=ctxPath %>/store/js/modernizr-2.6.2.min.js"></script>
<!-- FOR IE9 below -->
<!--[if lt IE 9]>
<script src="js/respond.min.js"></script>

<![endif]-->


<script type="text/javascript">
	
$(document).ready(function(){
	
	$("#btnUpdate").click(function(event){
		
		var pwd = $("#pwd").val();
		var pwd2 = $("#pwd2").val();
		
		var regexp_passwd = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g); 
	
		var bool = regexp_passwd.test(pwd);
		      
		if(!bool) {
			alert("암호는 8글자 이상 15글자 이하에 영문자, 숫자, 특수기호가 혼합되어야 합니다."); 
			$("#pwd").val("");
			$("#pwd2").val("");
			event.preventDefault();
			return;
		}   
		else if(pwd != pwd2) {
			alert("암호가 일치하지 않습니다.");
			$("#pwd").val("");
			$("#pwd2").val("");
			event.preventDefault();
			return;
		}
		else {
			var frm = document.pwdConfirmFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath %>/pwdConfirm.do";
			frm.submit();	
		}
	});
			
});
	
</script>


<div class="container">
	<form name="pwdConfirmFrm" class="colorlib-form">
	 <c:if test="${method.equals('GET')}">
	    <div class="form-group" >
	      <label for="pwd">암호</label> 
	      <input type="password" name="pwd" id="pwd" class="form-control" placeholder="새로운 암호를 입력하세요" required />
	   </div>
	   
	   <div class="form-group" >
	   	 <label for="pwd2">암호확인</label> 
	      <input type="password" name="pwd2" id="pwd2" class="form-control" placeholder="암호를 다시 입력해주세요" required />
	   </div>
	 </c:if>
	   <input type="hidden" name="userid" id="userid" value="${userid}" />
	   
	   <c:if test="${method.equals('POST') && n==1 }">
   		<div id="div_confirmResult" align="center" style="margin-top: 10%;">
   			<span style="font-weight: bold">[${userid}]님의 암호가 변경되었습니다.<br/></span>
   		</div>
	   </c:if>
	   
	   <c:if test="${method.equals('GET')}">
		   <div id="div_btnUpdate" align="center">
		   		<button type="button" class="btn btn-primary" id="btnUpdate">변경하기</button>
		   </div>		
	   </c:if> 
	</form>
</div>