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
  <script>

	function goEdit(pnum) {
		location.href="a_productEdit.do?pnum="+pnum;
	}
	
	function goDelete(pnum){
		var bool = confirm("해당 상품을 삭제하시겠습니까?");
		
		if(bool){
			var frm = document.productDetailFrm;
			frm.method="GET";
			frm.action="a_deleteProduct.do";
			frm.submit();
		}
		else{
			return false;
		}
		
	}
</script>
<style>
.typography-line span {
	
}
</style>
  
  
</head>

<body class="">
<div class="container" style="margin-top: 3%;">
        <div class="row">
          <div class="col-md-12">
          
            <div class="card">
              <div class="card-header mb-5">
                <h3 class="card-title">상품 상세 정보</h3>
              </div>
              <div class="card-body">
                <div class="typography-line">
                  <h4>
                    <span>상품번호</span>${pvo.pnum}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>상품패키지명</span>${pvo.fk_pacname}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>대분류</span>${pvo.fk_ldname}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>소분류</span>${pvo.fk_sdname}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>카테고리태그</span>${pvo.fk_ctname}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>스펙태그</span>${pvo.fk_stname}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>이벤트태그</span>${pvo.fk_etname}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>상품명</span>${pvo.pname}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>원가</span> ${pvo.showPrice}원</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>판매가</span>${pvo.showSaleprice}원</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>포인트</span>${pvo.showPoint}point</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>재고량</span>${pvo.showPqty}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>납품회사</span>${pvo.pcompanyname}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>유통기한</span>${pvo.pexpiredate}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>용량</span>${pvo.showWeight}g</h4>
                </div>
                <div class="typography-line"></div>
                <div class="typography-line">
                  <h4>
                    <span>판매량</span>${pvo.showSalecount}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>좋아요수</span>${pvo.plike}</h4>
                </div>
                <div class="typography-line">
                  <h4>
                    <span>상품등록일자</span>${pvo.showPdate}</h4>
                </div>
                <div class="typography-line">
                  <p>
                  <span>알레르기 정보</span>
                   	 ${pvo.allergy}
                  </p>
                </div>
                <div class="typography-line">
                 
                  <p>
                   <span style="padding-bottom: 20%;">상품 상세 설명</span>
                      	${pvo.pcontents}
                  </p>
               </div>
               <div class="typography-line">   
                  <p> 		
                   	<span>대표 이미지</span>
                   		<img src="<%= ctxPath %>/img/${pvo.titleimg}" style="width: 30%;"><br/>
                  </p>
                </div>
                <div class="typography-line">   
                  <p> 		
                   	<span>추가 이미지</span>
                   		<c:if test="${imgList==null}">
                   				이미지가 없습니다.
                   		</c:if>
                   		<c:if test="${imgList!=null}">
                   		<c:forEach var="map" items="${imgList}" >
                 			<img src="<%= ctxPath %>/img/${map.pimgfilename}" style="width: 30%;"><br/>
                   		</c:forEach>
                   		</c:if>
                  </p>
                </div>
                <div class="button" align="center">
                	<button class="btn btn-primary animation-on-hover" type="button" OnClick="goEdit(${pnum});">수정하기</button>
                	<button class="btn btn-danger animation-on-hover" type="button" OnClick="goDelete(${pnum});">삭제하기</button>
                </div>
              </div>
            </div>
            <form name="productDetailFrm">
            	<input type="hidden" id="pnum" name="pnum" value="${pvo.pnum}">
            </form>
          </div>
        </div>
      </div>
</body>
</html>