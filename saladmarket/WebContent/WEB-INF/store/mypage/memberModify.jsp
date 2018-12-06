<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String ctxPath=request.getContextPath();
%>   
<jsp:include page="../header.jsp"/>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>

<aside id="colorlib-hero" class="breadcrumbs">
	<div class="flexslider">
		<ul class="slides">
	   	<li style="background-image: url(<%=ctxPath %>/store/images/cover-img-1.jpg);">
	   		<div class="overlay"></div>
	   		<div class="container-fluid">
	   			<div class="row">
		   			<div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
		   				<div class="slider-text-inner text-center">
		   					<h1>MyPage</h1>
		   					<h2 class="bread">
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/memberInfoMain.do">회원정보수정</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/refundChange.do">환불 및 교환내역</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/myPickList.do">찜 목록 보기</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/orderList.do">주문내역보기</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/couponList.do">보유쿠폰 보기</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/myReview.do">리뷰보기</a></span>
		   					</h2>
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

//		#패스워드 유효성 검사
		$("#pwd").blur(function(){
			var passwd = $(this).val();
			var regExp_pw = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g;
			var isUsePasswd = regExp_pw.test(passwd);
			if(!isUsePasswd){
				$("#error_pwd").empty().html("비밀번호는 영문자,숫자,특수기호가 혼합된 8~15 글자로만 입력가능합니다.");
				$(this).val("");
				$("#pwd").focus();
			}
			else{
				$("#error_pwd").empty().html("사용 가능한 비밀번호 입니다.");
				$("#pwdcheck").focus();
			}
		});
		
		$("#pwdcheck").blur(function(){
			var password = $("#pwd").val();
			var pwdcheck = $(this).val();
			
			if(password != pwdcheck){ // 암호가 일치하지 않을 때
				$("#error_pwdcheck").empty().html("비밀번호가 일치하지 않습니다.");
				$(this).val("");
				$("#pwdcheck").focus();
			}
			else{
				$("#error_pwdcheck").empty().html("비밀번호가 일치합니다.");
			}	
		});
		
		$("#email").blur(function(){
			var email = $(this).val();
			var regExp_EMAIL = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 
			var isUseEmail = regExp_EMAIL.test(email);
			if(!isUseEmail){
				$("#error_email").empty().html("이메일 형식에 맞게 입력하세요.");
				$(this).val("");
				$(this).focus();
			}
			else{
				$("#error_email").empty();
			}	
		});
		
		$("#phone").blur(function(){
			var phone = $(this).val();
			var isUsePhone = false;
			var regExp_Phone = /^[0-9]+$/g;

			isUsePhone = regExp_Phone.test(phone);
			
			if(!isUsePhone) {
				$("#error_phone").empty().html("-없이 숫자만 입력하세요.");
				$(this).val("");
				$(this).focus();
			}
			else{
				$("#error_phone").empty();
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
				$("#error_addr").empty().html("우편번호 찾기를 클릭하여 주소를 입력해주세요.");
				$("#zipcodeSearch").focus();
			}
			else{
				$("#error_addr").empty();
			}
		});
		
	});
	
	function modifyMemberInfo() {
		var flagBool = false;
		
		var data = $("#pwd").val().trim();
		if(data == "") {
			flagBool = true;
			return false;
		}
		
		
		if(flagBool) {
			alert("필수입력란은 모두 입력하셔야 합니다.");
			event.preventDefault();
			return;
		}
		else {
			var bool = confirm('회원정보를 수정하시겠습니까?');
			if(bool){
				var frm = document.memberModifyFrm;
				frm.method = "post";
				frm.action = "memberModifiyEnd.do";
				frm.submit(); 
			}
		}
	}// end of modifyMemberInfo()
</script>



<div style="margin: 3%; border: 0px solid red;" align="center">
	<div style="width: 80%;margin: 0 auto;" align="center">
		<table style="width: 80%; border: 1px solid gray;">
			<tr height="100px;" style="">
				<td width="15%" style="font-size: 20pt; text-align: center; border: 1px solid gray;">
					<span id="name" class="name">${sessionScope.loginuser.name}</span>
					<span style="font-size: 12pt">님</span><br>
					<span id="level" class="level" style="font-size: 12pt; font-weight: bold;" >
					<c:if test="${membervo.fk_lvnum == 1}">Bronze</c:if>
					<c:if test="${membervo.fk_lvnum == 2}">Silver</c:if>
					<c:if test="${membervo.fk_lvnum == 3}">Gold</c:if>
					</span>
				</td>
				<td width="25%" style="padding: 0 10pt;" align="center">
					<span style="font-weight: bold; font-size: 15pt;">누적구매금액</span>
					<br>
					<span style="font-size: 17pt">
						<fmt:formatNumber pattern="###,###" value="${membervo.summoney}"/>
					</span>
					<span style="font-size: 13pt">원</span>
				</td>
				<td width="25%" style="padding: 0 10pt;" align="center">
					<span style="font-weight: bold; font-size: 15pt;">보유쿠폰</span>
					<br>
					<span style="font-size: 17pt">${cpcnt}</span><span style="font-size: 13pt">개</span>
				</td>
				<td width="25%" style="padding: 0 10pt;" align="center">
					<span style="font-weight: bold; font-size: 15pt;">보유포인트</span>
					<br>
					<span style="font-size: 17pt">
						<fmt:formatNumber pattern="###,###" value="${membervo.point}"/>
					</span>
					<span style="font-size: 13pt">Point</span>
				</td>
			</tr>
		</table>
	</div>
</div>
<div style="margin-top: 3%; margin-bottom: 1%;" align="center">
		<span style="font-weight: bold; font-size: 18pt; ">회원정보 수정</span>
		&nbsp;&nbsp;
</div>	
<div class="container">      
   <div class="col-md-12">
      <form name="memberModifyFrm" class="colorlib-form" style="margin-bottom: 0px;">
      		<div class="form-group">
      			<input type="hidden" name="mnum" value="${membervo.mnum}" readonly />
      		</div>
      		 <div class="form-group">
               <div class="col-md-6">
                  <label for="name">성명</label>
                  <input type="text" id="name" name="name" class="form-control" value="${membervo.name}" />
               </div>
               <div class="col-md-5" style="margin-top:4.5%">
                     <span class="error" id="error_name" style="color: blue; font-size: 12px;"></span>
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6">
                  <label for="pwd">비밀번호</label>
                  <input type="password" id="pwd" name="pwd" class="form-control" placeholder="비밀번호를 입력하세요.">
               </div>
               <div class="col-md-5" style="margin-top:4.5%">
                     <span class="error" id="error_pwd" style="color: blue; font-size: 12px;"></span>
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6">
                  <label for="pwdcheck">비밀번호확인</label>
                  <input type="password" id="pwdcheck" name="pwdcheck" class="form-control" placeholder="비밀번호를 다시 한번 입력하세요.">
               </div>
               <div class="col-md-4" style="margin-top:4.5%">
                     <span class="error" id="error_pwdcheck"style="color: blue; font-size: 12px;"></span>
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6" >
                  <label for="email">이메일</label>
                  <input type="text" id="email" name="email" class="form-control" value="${membervo.email}">
               </div>
               <div class="col-md-5" style="margin-top:4.5%;">
                     <span class="error" id="error_email" style="color: blue; font-size: 12px;"></span>
               </div>
            </div>
            <div class="form-group">   
               <div class="col-md-6">
                  <label for="phone ">연락처</label>
                  <input type="text" id="phone" name="phone" class="form-control" value="${membervo.phone}">
               </div>
               <div class="col-md-5" style="margin-top: 4.5%;">
                     <span class="error" id="error_phone" style="color: blue; font-size: 12px;"></span>
               </div>   
            </div>
            <div class="form-group">
               <div class="col-md-3">
                  <label for="postnum">우편번호</label>
                  <input type="text" id="postnum" name="postnum" class="form-control" value="${membervo.postnum}" readonly>
               </div>
               <div class="col-md-2" style="margin-top: 4.5%">
                  <button type="button" class="btn btn-outline" id="zipcodeSearch" style="padding: 2px; font-size: 10pt;">우편번호</button>
               </div>
            </div>
            <div class="col-md-6">
               <div class="form-group">
                  <label for="address1">주소</label>
                  <input type="text" id="address1" name="address1" class="form-control" value="${membervo.address1}"readonly >
               </div>
               <div class="form-group">
                  <input type="text" id="address2" name="address2" class="form-control" value="${membervo.address2}">
               </div>
               <div class="col-md-4" style="margin-top:4%">
                  <span class="error" id="error_addr" style="color: blue; font-size: 12px;"></span>
               </div>
           </div>
       </form>
       <div class="colorlib-form">
	       <div class="form-group">
		       <div class="col-md-6">
		          <label for="birthday">생년월일</label><br/>
		          <span id="birthday">${membervo.showBirthday}</span>
		       </div>
	       </div>
	       <div class="form-group">
		       <div class="col-md-6">
		          <label for="registerdate">가입일자</label><br/>
		          <span id="registerdate">${membervo.showRegisterdate}</span>
		       </div>
	       </div>
	               
		   <div class="row">
		      <div class="col-md-12" style="text-align: center; margin-top: 5%;" >
		         <p><button type="button" class="btn btn-primary" id="btnMemberModify" onClick="modifyMemberInfo();">수정하기</button></p>
		      </div>
		   </div>
       </div>
   </div>
</div>

<div class="gototop js-top">
   <a href="#" class="js-gotop"><i class="icon-arrow-up2"></i></a>
</div>

<jsp:include page="../footer.jsp" />