<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String CtxPath = request.getContextPath(); %>
<jsp:include page="../header.jsp" />
<aside id="colorlib-hero" class="breadcrumbs">
   <div class="flexslider">
      <ul class="slides">
         <li style="background-image: url(<%=CtxPath %>/store/images/cover-img-1.jpg);">
            <div class="overlay"></div>
            <div class="container-fluid">
               <div class="row">
                  <div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
                     <div class="slider-text-inner text-center">
                        <h1>Find Password</h1>
                        <h2 class="bread"><span><a href="index.jsp">Home</a></span> <span><a href="login.jsp">Login</a></span></h2>
                     </div>
                  </div>
               </div>
            </div>
         </li>
        </ul> 
     </div>
</aside>

	
<script type="text/javascript">
	$(document).ready(function(){
	// 1) GET 방식으로 들어왔을 때(초기 화면) ID 출력 부분을 감춤
		var method = "${method}"; // 문자열로 받아올 때는 반드시 "~"
		console.log("method: "+method);
		
		if(method=="GET"){
			$("#div_findResult").hide();
			$("#name").val("");
			$("#phone").val("");
			$("#name").focus();
		}
		else {
			$("#name").val("${name}");
			$("#phone").val("${phone}");
			$("#div_findResult").show();
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
				$("#mobile").val("");
				$("#mobile").focus();
				return;
			}
			
		   var frm = document.idFindFrm
		   frm.action = "<%= CtxPath %>/idFind.do";
		   frm.method = "POST";
		   frm.submit();
	   });
	});
	
</script>


      
<div class="container">      
   <div class="col-md-12">
   	<div>
	    <form name="idFindFrm" class="colorlib-form">    
	       <div class="form-group" style="margin-top: 3%;">
	       	 <div class="col-md-4"></div>
	       	 <div class="col-md-1" style="margin-top: 3%;">
	             <label for="name">성명</label>
	          </div>
	          <div class="col-md-3" id="div_name">
	             <input type="text" id="name" name="name" class="form-control" placeholder="이름을 입력하세요" required />
	          </div>
	          <div class="col-md-4"></div>
	       </div>
	       <div class="form-group" style="margin-bottom: 3%;">
	       	   <div class="col-md-4"></div>
		       <div class="col-md-1" style="margin-top: 3%;">
		           <label for="phone">연락처</label>
		       </div>
		       <div class="col-md-3" id="div_phone" >
		           <input type="text" id="phone" name="phone" class="form-control" placeholder="-없이 숫자만 입력하세요" required />
		       </div>
		       <div class="col-md-4"></div>
	       </div>
	       <div class="form-group" id="div_findResult">
	       	<div class="col-md-4"></div>
		       <div class="col-md-4 text-center">
		       	${name}님의 아이디는 
		          <span style="color: red; font-size: 14pt; font-weight: bold;">${userid}</span>입니다. 
		       </div>
		       <div class="col-md-4"></div>
		   </div>  		               
	       <div class="row" style="margin-bottom: 2%">
	          <div class="col-md-12 text-center" style="margin-top: 1%;" id="div_btnFind" >
	          	<button class="btn btn-primary" id="btnFind" style="font-size: 9pt;">아이디 찾기</button>
	          </div>
	       </div>
	       
	       
	    </form>
     </div>
   </div>
</div>

<div class="gototop js-top">
   <a href="#" class="js-gotop"><i class="icon-arrow-up2"></i></a>
</div>

<jsp:include page="../footer.jsp" />