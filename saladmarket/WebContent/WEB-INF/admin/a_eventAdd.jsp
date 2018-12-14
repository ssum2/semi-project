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
     이벤트 태그 생성
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

<script type="text/javascript">

$(document).ready(function(){
	$("#btnEdit").click(function(){
		var frm = document.etAddFrm;
		frm.method="POST";
		frm.action="a_eventAdd.do";
		frm.submit();
	});
});



</script>


</head>

<body class="container">
<div class="row">
          <div class="col-md-3"></div>
          <div class="col-md-6" style="margin-top: 3%;">
            <div class="card">
              <div class="card-header">
                <h5 class="title">이벤트 태그 수정 </h5>
              </div>

              <div class="card-body">
                <form name="etAddFrm" enctype="multipart/form-data">
                	<input type="hidden" name="etnum" value="${map.etnum}">
                   <div class="row">
                    <div class="col-md-3 pr-md-1">
                      <div class="form-group">
                        <label>이벤트명</label>
                        <input type="text" name="etname" class="form-control" placeholder="" value="${map.etname}">
                      </div>
                    </div>
                  </div>
                   <div class="row" style="margin-top: 5%;">
                    <div class="col-md-3 pr-md-1">
                    	<label>이벤트이미지</label><br/>
                    	<input type="file" name="etimagefilename" id="etimagefilename" class="infoData btn btn-primary btn-simple" />
                    	
                    </div>
                  </div>
                </form>
              </div>
              <div class="card-footer">
                <button type="button" id="btnEdit" class="btn btn-fill btn-primary">Save</button>
              </div>
            </div>
          </div>
 </div>
</body>
</html>