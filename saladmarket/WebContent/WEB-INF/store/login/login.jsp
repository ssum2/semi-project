<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="../header.jsp" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>

<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<% String CtxPath = request.getContextPath(); %>

<script type="text/javascript">
	$(document).ready(function(){
		$("#userid").focus(); // 해당 페이지에 접근하면 즉각 실행됨
		$("#pwd").keydown(function(event){
			if(event.keyCode==13){ // keyCode 13 ; enter
				goLogin();
			}
		});
		
		
		 // 저장된 쿠키값을 가져와서 ID 칸에 넣어준다. 없으면 공백으로 들어감.
	    var userInputId = getCookie("userInputId");
	    $("input[name='userid']").val(userInputId); 
	     
	    if($("input[name='userid']").val() != ""){ // 그 전에 ID를 저장해서 처음 페이지 로딩 시, 입력 칸에 저장된 ID가 표시된 상태라면,
	        $("#saveid").attr("checked", true); // ID 저장하기를 체크 상태로 두기.
	    }
	     
	    $("#saveid").change(function(){ // 체크박스에 변화가 있다면,
	        if($("#saveid").is(":checked")){ // ID 저장하기 체크했을 때,
	            var userInputId = $("input[name='userid']").val();
	            setCookie("userInputId", userInputId, 7); // 7일 동안 쿠키 보관
	        }else{ // ID 저장하기 체크 해제 시,
	            deleteCookie("userInputId");
	        }
	    });
	     
	    // ID 저장하기를 체크한 상태에서 ID를 입력하는 경우, 이럴 때도 쿠키 저장.
	    $("input[name='userid']").keyup(function(){ // ID 입력 칸에 ID를 입력할 때,
	        if($("#saveid").is(":checked")){ // ID 저장하기를 체크한 상태라면,
	            var userInputId = $("input[name='userid']").val();
	            setCookie("userInputId", userInputId, 7); // 7일 동안 쿠키 보관
	        }
	    });

		
		$(".myclose").click(function(){
			// alert("닫는다.");
			$("#loginUserid").val("${SessionScope.loginuser.userid}");
			javascript:history.go(0);
			// 현재 페이지를 새로고침을 함으로써 모달창에 입력한 성명과 휴대폰의 값이 텍스트박스에 남겨있지 않고 삭제하는 효과를 누린다. 

		});
		
		
	});

	function goLogin(){
		
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
		
		var frm = document.memberLoginFrm;
		frm.method ="POST";
		frm.action="memberLoginEnd.do";
		frm.submit();
	}
	
	// 제이쿼리로 쿠키 사용하기
	function setCookie(cookieName, value, exdays){
	    var exdate = new Date();
	    exdate.setDate(exdate.getDate() + exdays);
	    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + exdate.toGMTString());
	    document.cookie = cookieName + "=" + cookieValue;
	}
	 
	function deleteCookie(cookieName){
	    var expireDate = new Date();
	    expireDate.setDate(expireDate.getDate() - 1);
	    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
	}
	 
	function getCookie(cookieName) {
	    cookieName = cookieName + '=';
	    var cookieData = document.cookie;
	    var start = cookieData.indexOf(cookieName);
	    var cookieValue = '';
	    if(start != -1){
	        start += cookieName.length;
	        var end = cookieData.indexOf(';', start);
	        if(end == -1)end = cookieData.length;
	        cookieValue = cookieData.substring(start, end);
	    }
	    return unescape(cookieValue);
	}

</script>





<aside id="colorlib-hero" class="breadcrumbs">
    <div class="flexslider">
       <ul class="slides">
          <li style="background-image: url(<%=CtxPath %>/store/images/cover-img-1.jpg);">
             <div class="overlay"></div>
             <div class="container-fluid">
                <div class="row">
                   <div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
                      <div class="slider-text-inner text-center">
                         <h1>Login</h1>
                         <h2 class="bread"><span><a href="index.jsp">Home</a></span> <span><a href="join.jsp">Join Us</a></span></h2>
                      </div>
                   </div>
                </div>
             </div>
          </li>
         </ul> 
      </div>
</aside>
      
<div class="container">      
   <div class="col-md-12">
      <div>
       <form name="memberLoginFrm" class="colorlib-form">    
          <div class="form-group" style="margin-top: 3%;">
            <div class="col-md-4" style="margin-top: 3%;"></div> <%-- 이부분은 칸 조정할려고 넣어놨어요ㅠㅠ --%>
              <div class="col-md-1" style="margin-top: 3%;">
                <label for="userid">아이디</label>
             </div>
             <div class="col-md-3">
                <input type="text" id="userid" name="userid" class="form-control" placeholder="ID">
             </div>
          </div>
          <div class="form-group">
             <div class="col-md-4" style="margin-top: 3%;"></div> <%-- 이부분은 칸 조정할려고 넣어놨어요ㅠㅠ --%>
             <div class="col-md-1" style="margin-top: 3%;">
                 <label for="password">비밀번호</label>
             </div>
             <div class="col-md-3">
                 <input type="password" id="pwd" name="pwd" class="form-control" placeholder="Password">
             </div>
          </div>
          <div class="form-group" align="right" style="margin: 0%;">
              <div class="col-md-8">
                 <input type="checkbox" name="saveid" id="saveid"><label for="saveid">아이디 저장</label>
             </div>
          </div>
          
          
          <div class="row" style="margin-bottom: 2%">
             <div class="col-md-12" style="margin-top: 1%; margin-left: 33%;" >
           <%-- <a class="btn btn-default" style="font-size: 9pt;" href="<%= CtxPath %>/idFind.do">아이디 찾기</a>
                <a class="btn btn-default" style="font-size: 9pt;" href="<%= CtxPath %>/pwFind.do">비밀번호 찾기</a> --%>
               	<a class="btn btn-default" style="font-size: 9pt;" data-toggle="modal" data-target="#findUserid" data-dismiss="modal">아이디 찾기</a>
                <a class="btn btn-default" style="font-size: 9pt;" data-toggle="modal" data-target="#findPwd" data-dismiss="modal">비밀번호 찾기</a>
                <button class="btn btn-primary" style="margin-left: 2%; font-size: 10pt;" OnClick="goLogin();">로그인</button>
             <div class="panel-body" style="margin-left: 5%;">
                
                <a id="kakao-login-btn"></a>
                <a href="http://developers.kakao.com/logout"></a>
             </div>
             </div>
          </div>
          
          
       </form>
     </div>
   </div>
</div>

<div class="gototop js-top">
   <a href="#" class="js-gotop"><i class="icon-arrow-up2"></i></a>
</div>


<%-- ****** 아이디 찾기 Modal ****** --%>
  <div class="modal fade" id="findUserid" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close myclose" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">아이디 찾기</h4>
        </div>
        <div class="modal-body" style="height: 300px; width: 100%;">
          <div id="idFind">
          	<iframe style="border: none; width: 100%; height: 280px;" src="<%= request.getContextPath() %>/idFind.do">
          	</iframe>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default myclose" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>   


  <%-- ****** 비밀번호 찾기 Modal ****** --%>
  <div class="modal fade" id="findPwd" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close myclose" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">비밀번호 찾기</h4>
        </div>
        <div class="modal-body">
          <div id="pwFind">
          	<iframe style="border: none; width: 100%; height: 350px;" src="<%= request.getContextPath() %>/pwdFind.do">  
          	</iframe>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default myclose" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>


<script type='text/javascript'>
  //<![CDATA[
    // 사용할 앱의 JavaScript 키를 설정해 주세요.
    Kakao.init('3c40f1157f35feedc6f97790aa5d7a3d');
    // 카카오 로그인 버튼을 생성합니다.
    Kakao.Auth.createLoginButton({
      container: '#kakao-login-btn',
      success: function(authObj) {
        alert(JSON.stringify(authObj));
      },
      fail: function(err) {
         alert(JSON.stringify(err));
      }
    });
  //]]>
</script>
<jsp:include page="../footer.jsp" />