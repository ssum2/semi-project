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
     상품 상세정보
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
  
  
  
  
<script type="text/javascript" src="<%= ctxPath %>/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script> 

<style>
/* spinner 컨테이너 요소에 적용될 스타일 */
.custom-spinner-style
{
    /* 입력상자와 버튼이 떨어져 보이도록 하기 위해, 컨테이너의 테두리를 제거한다 */
    border: none !important;
     bottom: -100px;
    
}

/* spinner의 <input> 요소에 적용될 스타일 */
.custom-spinner-style > input
{
    border: solid 1px #2b3553 !important;
    /* Spinner 위젯이 적용되면서 input 요소에 margin이 생기는데, 이 여백을 제거한다 */
    margin: 0px;
    display: inline;
}

.custom-spinner-style > a
{
    display: inline;
}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		
		// 패키지 수정하기 버튼을 눌렀을 때
		$("#btnEdit").bind("click", function(){
			
			
			var flag = false;
			$(".infoData").each(function(){ 
				var val = $(this).val();
				if(val == ""){
					$(this).next().text("필수입력 사항 입니다.");
					flag = true;
					return false;
				}
			});
			
			if(flag){
				event.preventDefault();
				return;
			}
			else {
				var frm = document.packageEditFrm;
				frm.method="POST";
				frm.action="a_packageEdit.do";
				frm.submit();
			}
		});
		
	});

</script>
</head>

<body class="">
<div class="container" style="margin-top: 3%;">
  	<div class="row">
            <div class="card">
              <div class="card-header">
                <h5 class="title">패키지 상세 및 수정</h5>
              </div>
              <div class="card-body">
                <form name="packageEditFrm" enctype="multipart/form-data">
                  <div class="row">
                    <div class="col-md-12 pl-md-8">
                      <div class="form-group">
                        <label>패키지명</label>
                        <input type="text" class="form-control infoData" name="pacname" id="pacname" value="${pacvo.pacname}" ><span></span>
                        <input type="hidden" value="${pacvo.pacnum}" name="pacnum">
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-8 pl-md-3">
                      <div class="form-group">
                        <label>패키지 상세설명</label>
                        <textarea rows="4" cols="80" class="form-control infoData" name="paccontents" id="paccontents">${pacvo.paccontents}</textarea><span></span>
                      </div>
                    </div>
                  </div>
                  <label>패키지 대표이미지</label>	              
	              <ul style="list-style-type: none;">
	              	<li>
	              		<img src="<%= ctxPath %>/img/${pacvo.pacimage}" style="width: 30%;">
	              		<span></span>
	              	</li>
	              </ul>
	              <label>패키지 대표이미지 수정</label>	              
	              <ul style="list-style-type: none;">
	              	<li>
	              		<input type="file" name="pacimage" id="pacimage" class="btn btn-primary btn-simple"/>
	              		<span></span>
	              	</li>
	              </ul>
                </form>
              </div>
              
              <div class="card-footer">
                <button type="button" class="btn btn-fill btn-primary" id="btnEdit">수정</button>
              </div>
            </div>
          </div>
        </div>
 </body>
</html>