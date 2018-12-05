<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String ctxPath = request.getContextPath();
%>
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
	// 1) GET 방식으로 들어왔을 때(초기 화면) ID 출력 부분을 감춤
		var method = "${method}"; // 문자열로 받아올 때는 반드시 "~"
		console.log("method: "+method);
		
		if(method=="GET"){
			$("#div_findResult").hide();
		}
		else {
			$("#name").val("${name}");
			$("#phone").val("${phone}");
			$("#div_findResult").show();
			$("#btnFind").hide();
		}
		
		$("#btnFind").click(function(){
			var name = $("#name").val().trim();
			var mobile = $("#phone").val().trim();
			
			if(name==""){
				alert("성명을 입력하세요.");
				
				$("#name").val("");
				$("#name").focus();
				return;
			}
			if(mobile==""){
				alert("연락처를 입력하세요.");
				$("#phone").val("");
				$("#phone").focus();
				return;
			}
			
			var regExp1 = /\d{11}/g;
			var regExp2 = /\d{10}/g;
			var isUseMobile1 = regExp1.test(mobile);
			var isUseMobile2 = regExp2.test(mobile);
			if(!isUseMobile1 || !isUseMobile2) {
				alert("10~11자리 숫자만 입력 가능합니다.");
				$("#phone").val("");
				$("#phone").focus();
				return;
			}
			
		   var frm = document.idFindFrm
		   frm.action = "<%= ctxPath %>/idFind.do";
		   frm.method = "POST";
		   frm.submit();
	   });
	});
	
</script>


<div class="container">
<form name="idFindFrm" class="colorlib-form">
   	<div class="form-group" >
      <label for="name">성명</label>
      <input type="text" class="form-control" id="name" name="name" placeholder="이름을 입력하세요" required/>
    </div>
    
    <div class="form-group">
      <label for="phone">연락처</label>
      <input type="text" class="form-control" id="phone" name="phone" placeholder="-없이 입력하세요" required>
    </div>
  
   <div id="div_findResult" align="center">
	  <span style="font-size: 12pt; font-weight: bold;">[검색 결과]</span><br/>
   	  <span style="color: red; font-size: 14pt; font-weight: bold;">${userid}</span> 
   </div>
   
   <div id="div_btnFind" align="center" >
   		<button type="button" class="btn btn-primary" id="btnFind">찾기</button>
   </div>
   
</form>
</div>
