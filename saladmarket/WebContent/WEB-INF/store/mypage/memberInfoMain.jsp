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
	   	<li style="background-image: url(<%=ctxPath %>/store/images/PFPI-WEBSITE-SLIDERS-1.png);">
	   		<div class="overlay"></div>
	   		<div class="container-fluid">
	   			<div class="row">
		   			<div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
		   				<div class="slider-text-inner text-center">
		   					<h1>MyPage</h1>
		   					<h2 class="bread">
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/memberModify.do">회원정보수정</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/orderList.do">주문내역보기</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/couponList.do">보유쿠폰 보기</a></span>
			   					
			   					<%-- <span style="font-size: 13pt;"><a href="<%=ctxPath %>/refundChange.do">환불 및 교환내역</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/myPickList.do">찜 목록 보기</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/myReview.do">리뷰보기</a></span> --%>
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
		$("#pwd").keydown(function(event){
			if(event.keyCode==13){ // keyCode 13 ; enter
				var userid = $("#userid").val();
				var pwd = $("#pwd").val();
				pwdCheck(userid, pwd);
			}
		});
		
		
		$("#btnPwdCheck").click(function(){
			var userid = $("#userid").val();
			var pwd = $("#pwd").val();
			pwdCheck(userid, pwd);
		});
	});
	

	function pwdCheck(userid, pwd){
		var form_data = {"userid":userid, "pwd":pwd};
		
		$.ajax({
			url:"pwdCheck.do",
			type:"POST",
			data: form_data,
			dataType:"JSON",
			success:function(json){
				var mnum = json.mnum;
				if(mnum!=0){
					$("#pwdCheckResult").html("인증 완료되었습니다. <br/> 3초 후 정보수정 페이지로 이동됩니다.");
					setTimeout(function() { 
						location.href="<%=ctxPath %>/memberModify.do?mnum="+mnum;
					}, 2000);
				}
				else{
					$("#pwdCheckResult").html("비밀번호가 올바르지 않습니다. 다시 입력해주세요.");
					$("#pwd").val("");
					$("#pwd").focus();
					return;
				}
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		}); // end of ajax
	} // end of pwdCheck
	
	
</script>


<div class="container">
	<div class="row">
		<div class="col-md-12">
		<h2 >비밀번호 재확인</h2>
		<hr style="border: 1px solid gray;" >
		</div>
		<div class="col-md-12">
		회원님의 정보를 안전하게 하게 위해 비밀번호를 다시 입력해주세요.<br/><br/>
		</div>
	</div>

	<div>
		<form name="pwdCheckFrm" class="colorlib-form">
			<div class="row">
			   	<div class="form-group col-md-offset-4 col-md-4">
				   	<div class="col-md-1" >
				      <label for="userid">아이디</label>
				    </div>
				    <div class="col-md-3">
				    	<input type="text" class="form-control" id="userid" name="userid" size="15" readonly="readonly" value="${sessionScope.loginuser.userid}"/>
				    </div>
				</div>	
			</div>
			<div class="row"> 
			    <div class="form-group col-md-offset-4 col-md-4">
			    	<div class="col-md-1" >
				      <label for="pwd">암호</label>
				    </div>
				    <div class="col-md-3" >
				      <input type="password" class="form-control" id="pwd" name="pwd" placeholder="암호를 입력하세요" required />
				    </div>
			    </div>
			</div>     
			<div class="row">   
			    <div class="col-md-12" align="center" style="margin-bottom: 5%;">
					<span id="pwdCheckResult" style="color: green; font-size: 15px; text-align: center;"> </span>
			    </div>
			</div> 
			
			    <div id="div_btnPwdCheck" align="center" >
			   		<button type="button" class="btn btn-primary" id="btnPwdCheck">확인</button>
			    </div>
		   
		</form>
	</div>
</div>


	

<jsp:include page="../footer.jsp"/>