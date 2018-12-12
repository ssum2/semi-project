<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath();
request.setCharacterEncoding("UTF-8");
%>
<jsp:include page="../header.jsp" />

<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>

<script type ="text/javascript">
	$(document).ready(function(){
		var dt = new Date();
		var year = dt.getFullYear();
		
		// 에러메세지 감추기
		$(".error").hide();
		
		// 첫번째 입력사항(아이디)에 포커스
		$("#userid").focus();
		
	    $("#join").click(function() {
	        goRegister(event);
	    }); // end of $("#join").click(---
	            
	    $(".requiredinfo").each(function() {
	         $(this).blur(function() { //blur: 다음 input태그로 커서 이동
	            var data = $(this).val().trim();
	            if(data == ""){ // 입력하지 않거나 공백만 입력했을떄
	               $(this).parent().parent().find(".error").show();
	            }
	            else{
	               $(this).parent().parent().find(".error").hide();
	            }
	         });

	      });// end of $(".useridinfo").each(---
		
// 		#포커스가 넘어갔을 때의 태스크를 반복문으로 처리; 필수입력사항의 공통 클래스 requiredInfo
		$(".requiredInfo").each(function(){
			$(this).blur(function(){
				var data = $(this).val().trim();
				if(data==""){ // 입력하지 않거나 공백만 입력했을 경우 
					$(this).parent().find(".error").show();
					
					$(this).focus();
				}
				else {	// 공백이 아닌 글자를 입력했을 경우
					$(this).parent().find(".error").hide(); // 에러 없앰
					
					$(this).next().focus();
				}
			});
		});
	
//		#아이디 중복검사; ajax사용
		$("#idcheck").click(function(){
			if($("#userid").val().trim()==""){
				$(this).parent().find(".error").show();
				$(this).focus();
				return;
			}
			$.ajax({
				url: "idDuplicateCheck.do",
				type: "POST",
				data: {"userid":$("#userid").val().trim()},
				dataType: "json",
				success: function(json){
					if(json.isUseUserid==0){
						$("#idcheckerror").empty();
						$("#idcheckgood").empty().html("사용가능한 아이디 입니다.");
						
						$("#userid").attr("readonly", true);
					}
					else if(json.isUseUserid==1) {
						$("#idcheckgood").empty();
						$("#idcheckerror").empty().html("입력하신 아이디가 이미 존재합니다. 다시 입력하세요.");
						
						$("#userid").empty().focus();
					}
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});
			
		
		});
//		#패스워드 유효성 검사
		$("#pwd").blur(function(){
			var passwd = $(this).val();
			var regExp_pw = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g;
			var isUsePasswd = regExp_pw.test(passwd);
			if(!isUsePasswd){
				$(".pwdOK").html.empty();
				$("#error_passwd").show();
				$(this).val("");
				$("#pwd").focus();
			}
			else{
				$("#error_passwd").hide();
				$(".pwdOK").html("사용 가능한 비밀번호 입니다.");
				$("#pwdcheck").focus();
			}
		});
		
		$("#pwdcheck").blur(function(){
			var password = $("#password").val();
			var pwdcheck = $(this).val();
			
			if(password != pwdcheck){ // 암호가 일치하지 않을 때
				$(".pwdcheckOK").html.empty();
				$(this).parent().find(".error").show();
				$(this).val("");
				$("#pwdcheck").focus();
			}
			else{
				$("#error_passwd").hide();
				$(".pwdcheckOK").html("비밀번호가 일치합니다.");
			}	
		});
		
		$("#email").blur(function(){
			var email = $(this).val();
			var regExp_EMAIL = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 
			var isUseEmail = regExp_EMAIL.test(email);
			if(!isUseEmail){
				$(this).parent().find(".error").show();
				$(this).val("");
				$(this).focus();
			}
			else{
				$("#error_passwd").hide();
			}	
		});
		
		$("#phone").blur(function(){
			var phone = $(this).val();
			var isUsePhone = false;
			var regExp_Phone = /^[0-9]+$/g;

			isUsePhone = regExp_Phone.test(phone);
			
			if(!isUsePhone) {
				$(this).parent().find(".error").show();
				$(this).val("");
				$(this).focus();
			}
			else{
				$(".error_phone").hide();
			}	
		});
		
		$("#zipcodeSearch").click(function() {
			new daum.Postcode({
				oncomplete: function(data) {
					$("#postnum").val(data.zonecode);
				    $("#address1").val(data.address);
				    $("#address2").focus();
				}
			}).open();
		});
		
		$(".address").blur(function(){
			var address = $(this).val().trim();
			if(address==""){
				$(this).parent().find(".error").show();
				$(this).val("");
			}
			else{
				$(this).parent().find(".error").hide();
			}
		});
		
	  $("#birthday").datepicker({
		dateFormat:"yy/mm/dd",
		dayNamesMin:["일", "월", "화", "수", "목", "금", "토"],
		monthNames:["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
		onSelect:function( d ){
 			var arr = d.split("/");
			$("#birthday").text(arr[0]);
			$("#birthday").append(arr[1]);
			$("#birthday").append(arr[2]);
		}
		});

	});
//	#최종적으로 체크박스/라디오 체크된 다음 submit
	function goRegister(){
	
		var isCheckedAgree = $("input:checkbox[id=agree]").is(":checked");
		if(!isCheckedAgree){
			alert("이용약관에 동의하셔야 가입 가능합니다.");
			return;
		}
		var frm = document.joinFrm;
		frm.method = "POST";
		frm.action = "memberRegisterEnd.do";
		frm.submit();
	}
</script>
<aside id="colorlib-hero" class="breadcrumbs">
    <div class="flexslider">
       <ul class="slides">
          <li style="background-image: url(<%=ctxPath %>/store/images/cover-img-1.jpg);">
             <div class="overlay"></div>
             <div class="container-fluid">
                <div class="row">
                   <div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
                      <div class="slider-text-inner text-center">
                         <h1>Join Us</h1>
                         <h2 class="bread"><span><a href="index.jsp">Home</a></span> <span><a href="login.jsp">Login</a></span></h2>
                      </div>
                   </div>
                </div>
             </div>
          </li>
         </ul> 
      </div>
</aside> 
      
<div class="container" style="margin-left: 30%;">      
   <div class="col-md-8">
      <form class="colorlib-form joinFrm" name="joinFrm">
            <div class="form-group">
               <div class="col-md-6">
                  <label for="userid">아이디</label>
                  <input type="text" id="userid" name="userid" class="form-control requiredinfo" placeholder="ID" required>
               </div>
               <div class="col-md-2" style="margin-top:5.5%">
                  <button type="button" id="idcheck" class="btn btn-outline" style="padding: 2px; font-size: 10pt;" >아이디 확인</button>
               </div>
               <div class="col-md-4 error" style="margin-top:5.5%">
                     <span class="error" id="iderror" style="color: blue; font-size: 12px;">아이디를 입력하세요.</span>
               </div>
               <div class="col-md-4" style="margin-top:5.5%">
               		<span id="idcheckerror" style="color: blue; font-size: 12px;"></span>
                    <span id="idcheckgood" style="color: green; font-size: 12px;"></span>
               </div>
            
            </div>
            <div class="form-group">
               <div class="col-md-6">
                  <label for="password">비밀번호</label>
                  <input type="password" id="pwd" name="pwd" class="form-control requiredinfo" placeholder="Password" required>
               </div>
                <div class="col-md-5" style="margin-top:5%">
                     <span class="error" style="color: blue; font-size: 12px;">비밀번호는 영문자,숫자,특수기호가 혼합된 8~15 글자로만 입력가능합니다.</span>
               </div>
                <div class="col-md-5" style="margin-top:0">
                     <span class="pwdOK" style="color: green; font-size: 12px;"></span>
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6">
                  <label for="pwdcheck">비밀번호확인</label>
                  <input type="password" id="pwdcheck" class="form-control requiredinfo" placeholder="Password" required>
               </div>
               <div class="col-md-4" style="margin-top:5.5%">
                     <span class="error" style="color: blue; font-size: 12px;">암호가 일치하지 않습니다.</span>
               </div>
               <div class="col-md-5" style="margin-top:0">
                     <span class="pwdcheckOK" style="color: green; font-size: 12px;"></span>
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6">
                  <label for="name">성명</label>
                  <input type="text" id="name" name="name" class="form-control requiredinfo" placeholder="마켓수" required>
               </div>
               <div class="col-md-4" style="margin-top:5.5%">
                     <span class="error" style="color: blue; font-size: 12px;">성명을 입력하세요.</span>
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6" >
                  <label for="email">이메일</label>
                  <input type="text" id="email" name="email" class="form-control requiredinfo" placeholder="marketsue@gmail.com" required>
               </div>
               <div class="col-md-5" style="margin-top:5.5%">
                     <span class="error" style="color: blue; font-size: 12px;">이메일 형식에 맞게 입력하세요.</span>
               </div>
            </div>
            <div class="form-group">   
               <div class="col-md-6">
                  <label for="phone ">연락처</label>
                  <input type="text" id="phone" name="phone" class="form-control requiredinfo" placeholder="01012345678" required>
               </div>  
               <div class="col-md-5" style="margin-top:8%">
                     <span class="error error_phone" style="color: blue; font-size: 12px;"> - 없이 입력해주세요.</span>
               </div> 
               <%-- <div class="col-md-5" style="margin-top:8.5%">
                     <span class="error error_hp2" style="color: blue; font-size: 12px;"> 휴대폰 형식이 아닙니다.</span>
               </div>--%>
            </div>
            <div class="form-group">
               <div class="col-md-3">
                  <label for="postnum">우편번호</label>
                  <input type="text" id="postnum" name="postnum" class="form-control" placeholder="우편번호" required>
               </div>
               <div class="col-md-2" style="margin-top: 5%">
                  <button type="button" class="btn btn-outline" id="zipcodeSearch" style="padding: 2px; font-size: 10pt;">우편번호</button>
               </div>
               <div class="col-md-4" style="margin-top:5.5%">
                     <span class="error" style="color: blue; font-size: 12px;">올바른 우편번호 형식이 아닙니다.(예. 12345)</span>
               </div>
            </div>
            
             <div class="form-group">
               <div class="col-md-6">
                  <label for="address">주소</label>
                  <input type="text" id="address1" name="address1" class="form-control" placeholder="우편번호 찾기를 클릭하세요" required>
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6">
                  <input type="text" id="address2" name="address2" class="form-control" placeholder="상세주소를 입력하세요" required>
               </div>
               <div class="col-md-4" style="margin-top:4%">
                     <span class="error" style="color: blue; font-size: 12px;">우편번호  찾기를 클릭하세요.</span>
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-3">
                  <label for="postnum">생년월일</label>
                  <input type="text" id="birthday" class="form-control" name="birthday" required/>      
               </div>
            </div>
            <div class="col-md-4" style="margin-top:8.5%">
               <span class="error" style="color: blue; font-size: 12px;">생년월일을 입력하세요.</span>
            </div> 
         <div class="row col-md-12">
            <label for="agree">이용약관에 동의합니다</label>&nbsp;&nbsp;<input type="checkbox" id="agree" />
         </div>
         
         <div class="row col-md-12">
            <iframe src="/saladmarket/agree/agree.html" width="100%" height="150px" class="box" ></iframe>
         </div>
         

         <div class="row">
            <div class="col-md-12" style="margin-left: 35%; margin-top: 5%;" >
               <button type="button" id="join" class="btn btn-primary" OnClick="goRegister();">가입하기</button>
            </div>
         </div>

       </form>
   </div>
</div>

<div class="gototop js-top">
   <a href="#" class="js-gotop"><i class="icon-arrow-up2"></i></a>
</div>

<jsp:include page="../footer.jsp" />