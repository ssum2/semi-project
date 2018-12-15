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
			
		$("#btnFind").click(function(){
			var frm = document.pwdFindFrm;
			frm.method = "POST";
			frm.action = "<%= ctxPath %>/pwdFind.do";
			frm.submit();
		});
		
		var method = "${method}";
		var userid = "${userid}";
		var email = "${email}";
		var n = "${n}";
		
		if(method=="POST" && userid != "" && email != "") {
			$("#userid").val(userid);
			$("#email").val(email);
		}
		
		if(method=="POST" && n==1) {
			$("#div_btnFind").hide();
			$("#div_findInput").html("");
		}
		else if(method=="POST" && (n == -1 || n == 0)) {
			$("#div_btnFind").show();
		}		
		
 		$("#btnConfirmCode").click(function(){
			var frm = document.pwdFindFrm;
			var n1 = frm.input_confirmCode.value;
			var n2 = frm.certificationCode.value;
			if(n1==n2) {
				alert("인증성공 되었습니다.");
			
				frm.method = "GET"; // 새암호와 새암호확인을 입력받기 위한 폼만을 띄워주기 때문에 GET 방식으로 한다.
				frm.action = "<%= ctxPath %>/pwdConfirm.do";
				frm.submit();
			}
			else {
				alert("인증코드가 일치하지 않습니다. 다시 입력해주세요.");
				$("#input_confirmCode").val("");
				$("#input_confirmCode").focus();
			}
			
		});

	});

</script>



<div class="container">
	<form name="pwdFindFrm" class="colorlib-form">
	   <div id="div_findInput">
	   <div class="form-group">
	      <label for="userid">아이디</label>
	      <input type="text" name="userid" id="userid" class="form-control" placeholder="아이디를 입력하세요." required />
	   </div>
	   
	   <div class="form-group" >
	   	  <label for="email">이메일</label>
	      <input type="text" name="email" id="email" class="form-control" placeholder="abd@def.com" required />
	   </div>
	   </div>
	   <div id="div_findResult" class="colorlib-form">
	   	   <c:if test="${n == 1}">
	   	      <div id="pwdConfirmCodeDiv" class="colorlib-form">
	   	      	  인증코드가 ${email}로 발송되었습니다.<br/>
	   	      	  <label for="input_confirmCode">인증코드를 입력해주세요</label>
	   	      	 <input type="text" class="form-control" name="input_confirmCode" id="input_confirmCode" required />
	   	      	 <input type="hidden" name="certificationCode" id="certificationCode" value="${certificationCode}" required />
	   	      	 <br/><br/>
	   	      	 <button type="button" class="btn btn-warning" id="btnConfirmCode">인증하기</button>    
	   	      </div>
	   	   </c:if>
	   	   
	   	   <c:if test="${n == 0}">
	   	   	  <span style="color: red;">사용자 정보가 없습니다.</span>
	   	   </c:if>
	   	   
	   	   <c:if test="${n == -1}">
	   	   	  <span style="color: red;">${sendFailmsg}</span>
	   	   </c:if>
	   	   
	   </div>
	   
	   <div id="div_btnFind" align="center">
	   		<button type="button" class="btn btn-warning" id="btnFind">찾기</button>
	   </div>
	   
	</form>
</div>