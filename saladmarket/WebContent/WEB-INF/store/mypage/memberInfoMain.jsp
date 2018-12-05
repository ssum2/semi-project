<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String ctxPath=request.getContextPath();
%>
    
    
<jsp:include page="../header.jsp"/>
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
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/memberModify.do">회원정보수정</a></span>
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
	$("#btnPwdCheck").click(function(){
		
		pwdCheckFrm 
	});



</script>


<div class="container">
	<div class="row">
		<div class="col-md-12">
		<h2 >비밀번호 재확인</h2>
		<hr style="border: 1px solid gray;" >
		</div>
		<div class="col-md-12">
		회원님의 정보를 안전하게 하게 위해 비밀번호를 다시 입력해주세요.
		</div>
	</div>

	<form name="pwdCheckFrm" class="colorlib-form">
	   	<div class="form-group" >
	      <label for="userid">아이디</label>
	      <input type="text" class="form-control" id="userid" name="userid" readonly="readonly" value="${sessionScope.loginuser.userid}"/>
	    </div>
	    
	    <div class="form-group">
	      <label for="pwd">암호</label>
	      <input type="text" class="form-control" id="pwd" name="pwd" placeholder="암호를 입력하세요" required />
	    </div>
	  
	   <div id="div_btnPwdCheck" align="center" >
	   		<button type="button" class="btn btn-primary" id="btnPwdCheck">찾기</button>
	   </div>
   
	</form>
</div>
				
<jsp:include page="../footer.jsp"/>