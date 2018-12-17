 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% 
	String ctxPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link rel="apple-touch-icon" sizes="76x76" href="../assets/img/apple-icon.png">
  <link rel="icon" type="image/png" href="<%= ctxPath %>/assets/img/favicon.png">
  <title>
    회원 상세정보
  </title>
  <!--     Fonts and icons     -->
  <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,600,700,800" rel="stylesheet" />
  <link href="https://use.fontawesome.com/releases/v5.0.6/css/all.css" rel="stylesheet">
  <!-- Nucleo Icons -->
  <link href="<%= ctxPath %>/assets/css/nucleo-icons.css" rel="stylesheet" />
  <!-- CSS Files -->
  <link href="<%= ctxPath %>/assets/css/black-dashboard.css?v=1.0.0" rel="stylesheet" />
  <!-- CSS Just for demo purpose, don't include it in your project -->
  <link href="<%= ctxPath %>/assets/demo/demo.css" rel="stylesheet" />

   <!--   Core JS Files   -->
  <script src="<%= ctxPath %>/assets/js/core/jquery.min.js"></script>
  <script src="<%= ctxPath %>/assets/js/core/popper.min.js"></script>
  <script src="<%= ctxPath %>/assets/js/core/bootstrap.min.js"></script>
  <script src="<%= ctxPath %>/assets/js/plugins/perfect-scrollbar.jquery.min.js"></script>
  <!--  Google Maps Plugin    -->
  <!-- Place this tag in your head or just before your close body tag. -->
  <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
  <!-- Chart JS -->
  <script src="<%= ctxPath %>/assets/js/plugins/chartjs.min.js"></script>
  <!--  Notifications Plugin    -->
  <script src="<%= ctxPath %>/assets/js/plugins/bootstrap-notify.js"></script>
  <!-- Control Center for Black Dashboard: parallax effects, scripts for the example pages etc -->
  <script src="<%= ctxPath %>/assets/js/black-dashboard.min.js?v=1.0.0"></script>
  <!-- Black Dashboard DEMO methods, don't include it in your project! -->
  <script src="<%= ctxPath %>/assets/demo/demo.js"></script>

<script>

	function editMemberInfo() {		
		var frm = document.memberEditFrm;
		frm.method = "POST";
		frm.action = "a_memberEdit.do";
		frm.submit();
	}
	
</script>

</head>

<body class="">
<div class="container" style="margin-top: 3%;">
  	<div class="row">
          
          <div class="col-md-12">
            <div class="card">
              <div class="card-header">
                <h5 class="title">회원 상세 정보/수정</h5>
              </div>
              <div class="card-body">
                <form name="memberEditFrm">
                
                   <div class="row">
                    <div class="col-md-3 pr-md-1">
                      <div class="form-group">
                        <label>회원번호</label>
                      	<input type="text" class="form-control" id="mnum" name="mnum" value="${mvo.mnum}" readonly>
                      </div>
                    </div>
                    <div class="col-md-3 pr-md-1">
                      <div class="form-group">
                        <label>이름</label>
                        <input type="text" class="form-control" name="name" value="${mvo.name }" readonly>
                      </div>
                    </div>
                  </div>
                  
                  <div class="row">
                    <div class="col-md-3 pr-md-1">
                      <div class="form-group">
                        <label>아이디</label>
                        <input type="text" class="form-control" name="userid" value="${mvo.userid }" readonly>
                      </div>
                    </div>
                    <div class="col-md-4 pl-md-1">
                      <div class="form-group">
                        <label for="exampleInputEmail1">이메일</label>
                        <input type="email" class="form-control" name="email" value="${mvo.email}">
                      </div>
                    </div>
                  </div>
                  
                  <div class="row"> 
                    <div class="col-md-3 pl-md-3">
                      <div class="form-group">
                        <label>생년월일</label>
                        <input type="text" class="form-control" name="birthday" value="${mvo.showBirthday}" readonly>
                      </div>
                    </div>
                    <div class="col-md-3 pl-md-1">
                      <div class="form-group">
                        <label>나이</label>
                        <input type="number" class="form-control" name="age" value="${mvo.showAge }" readonly>
                      </div>
                    </div>
                  </div>
                  
                  <div class="row">
                    <div class="col-md-3 pr-md-1">
                      <div class="form-group">
                        <label>전화번호</label>
                        <input type="tel" class="form-control" name="phone" value="${mvo.showPhone}">
                      </div>
                    </div>
                  </div>
                    
                  <div class="row">
                  <div class="col-md-3 pr-md-1">
                      <div class="form-group">
                        <label>우편번호</label>
                        <input type="number" class="form-control" name="postnum" value="${mvo.postnum}">
                      </div>
                    </div>
                    <div class="col-md-12">
                      <div class="form-group">
                        <label>주소</label>
                        <input type="text" class="form-control" name="address1" value="${mvo.address1}">
                      </div>
                    </div>
                     <div class="col-md-12">
                      <div class="form-group">
                        <input type="text" class="form-control" name="address2" value="${mvo.address2}">
                      </div>
                    </div>
                  </div>
                  
                  <div class="row">
                    <div class="col-md-3 pr-md-1">
                      <div class="form-group">
                        <label>가입일자</label>
                        <input type="text" class="form-control" name="registerdate" value="${mvo.showRegisterdate }" readonly >
                      </div>
                    </div>
                    <div class="col-md-3 pl-md-1">
                      <div class="form-group">
                        <label>누적금액</label>
                        <input type="text" class="form-control" name="summoney" value="${mvo.showSummoney}" readonly >
                      </div>
                    </div>
                    <div class="col-md-3 pl-md-1">
                      <div class="form-group">
                        <label>회원등급</label>
                        <input type="text" class="form-control" name="lvname" value="${mvo.lvnameByLvnum}">
                      </div>
                    </div>
                    <div class="col-md-3 pl-md-1">
                      <div class="form-group">
                        <label>회원상태</label>
                        <input type="text" class="form-control" name="status" value="${mvo.statusByStatus}">
                      </div>
                    </div>
                  </div>
                
                 
                  <div class="row">
                    <div class="col-md-8">
                      <div class="form-group">
                        <label>코멘트</label>
                        <textarea rows="1" cols="80" class="form-control" name="textarea" value=""></textarea>
                      </div>
                    </div>
                  </div>
                </form>
              </div>
              <div class="card-footer">
                <button type="button" id="btnEditMemberInfo" class="btn btn-fill btn-primary" onClick="editMemberInfo()">Save</button>
              </div>
            </div>
          </div>
    	</div>
	</div>
</body>
</html>
