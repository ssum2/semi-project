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
		var fk_pacname = "${pvo.fk_pacname}";
		var fk_sdname = "${pvo.fk_sdname}";
		var fk_ctname = "${pvo.fk_ctname}";
		var fk_stname = "${pvo.fk_stname}";
		var fk_etname = "${pvo.fk_etname}";
		
		$("#pacname").val(fk_pacname);
		$("#btnPacnameSelect").empty().text(fk_pacname);
		
		$("#sdname").val(fk_sdname);
		$("#btnSdnameSelect").empty().html(fk_sdname);
		
		$("#ctname").val(fk_ctname);
		$("#btnCtnameSelect").empty().html(fk_ctname);
		
		$("#stname").val(fk_stname);
		$("#btnStnameSelect").empty().html(fk_stname);
		
		$("#etname").val(fk_etname);
		$("#btnEtnameSelect").empty().html(fk_etname);

		
		$("#fk_pacnameSelect").children().click(function(){
			var len = $(this).html().length;
			var html = $(this).html();
			if(len>6){
				html = html.substring(0,10);
			}
			$("#btnPacnameSelect").empty().html(html);


			$("#pacname").val(html);
		});
		
		
		$("#fk_sdnameSelect").children().click(function(){
			var len = $(this).html().length;
			var html = $(this).html();
			if(len>8){
				html = html.substring(0,13);
			}
			$("#btnSdnameSelect").empty().html(html);
			
			var sdname = $(this).attr('id');
			console.log(sdname);
			$("#sdname").val(sdname);
		});
		
		
		$("#fk_ctnameSelect").children().click(function(){
			var len = $(this).html().length;
			var html = $(this).html();
			if(len>6){
				html = html.substring(0,8);
			}
			$("#btnCtnameSelect").empty().html(html);
			$("#ctname").val(html);
		});
		
		$("#fk_stnameSelect").children().click(function(){
			var len = $(this).html().length;
			var html = $(this).html();
			if(len>6){
				html = html.substring(0,8);
			}
			$("#btnStnameSelect").empty().html(html);
			$("#stname").val(html);
		});
		
		$("#fk_etnameSelect").children().click(function(){
			var len = $(this).html().length;
			var html = $(this).html();
			if(len>6){
				html = html.substring(0,8);
			}
			$("#btnEtnameSelect").empty().html(html);
			$("#etname").val(html);
		});
		
		
		$("#spinnerPqty").spinner().parent().addClass("custom-spinner-style");
		$("#spinnerImgQty").spinner().parent().addClass("custom-spinner-style");
		// 제품 수량 스피너
		$("#spinnerPqty").spinner({
			spin: function(event, ui){
				if(ui.value > 100){
					// Max값 100으로 한정
					$(this).spinner("value", 100); 
					return false;
				}
				else if(ui.value < 1){
					// Min값 1으로 한정
					$(this).spinner("value", 1); 
					return false;
				}
			}
		});
		
		// 파일 개수 스피너
		$("#spinnerImgQty").spinner({
			spin: function(event, ui){
				if(ui.value > 10){
					// Max값 100으로 한정
					$(this).spinner("value", 10); 
					return false;
				}
				else if(ui.value < 0){
					// Min값 1으로 한정
					$(this).spinner("value", 0); 
					return false;
				}
			}
		});
		
		// 파일 개수 스피너에서 개수를 선택했을 때 첨부한 파일 넣어주기
		$("#spinnerImgQty").bind("spinstop", function(){
			// 스피너는 이벤트가 "change"가 아니라 "spinstop"이다.
			var html ="";
			var spinnerImgQtyVal = $("#spinnerImgQty").val();
			
			if(spinnerImgQtyVal == "0"){
				$("#divfileattach").empty();
				$("#attachCount").val("");
				return;
			}
			else {
				for(var i=0; i<parseInt(spinnerImgQtyVal); i++){
					html += "<li>";
					html += "<input type='file' name='attach"+i+"' class=\"infodata btn btn-primary btn-simple\" /></li>";
				}
				$("#divfileattach").empty();
				$("#divfileattach").append(html);
				$("#attachCount").val(spinnerImgQtyVal);
			}

		});
		
		
		// 상품등록하기 버튼을 눌렀을 때
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
				event.preventDefault(); // 이벤트를 가로막는다(아래 form에 기재되어 있는 action을 취하지 않음)
				return;
			}
			else {
				var frm = document.productEditFrm;
				frm.method="POST";
				frm.action="a_productEditEnd.do";
				frm.submit();
			}
		});
		
		

	});
	
	function goDeleteAttachImg(pimgnum){
		var bool = confirm("해당 이미지를 삭제하시겠습니까?");
		
		if(bool){
			var form_data = {pimgnum:pimgnum};
			
			$.ajax({
				url: "a_productEdit_atImgDel.do",
				type: "GET",
				data: form_data,
				dataType: "JSON",
				success: function(json){
					var result = json.result;
					if(result==1){
						alert("삭제되었습니다.");
						window.location.reload();
					}
					else{
						alert("삭제 실패");
						
					}
	
				},// end of success
				error: function(request, status, error){
					if(request.readyState == 0 || request.status == 0) return;
					else alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});// end of $.ajax
		}
		else{
			return false;
		}
	}

</script>  

  
  
</head>

<body class="">
	<div class="container" style="margin-top: 3%;">
		<div class="row">
          <div class="col-md-12">
            <div class="card">
              <div class="card-header">
                <h5 class="title">상품 등록</h5>
              </div>
              <div class="card-body">
                <form name="productEditFrm" enctype="multipart/form-data">
                  <div class="row">
                    <div class="col-md-3 pr-md-1">
                      <div class="dropdown">
					  <button class="btn btn-primary dropdown-toggle" type="button" id="btnPacnameSelect" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					     	상품패키지명
					  </button>
					  <input type="hidden" name="pacname" id="pacname" value="">
						  <div class="dropdown-menu" id="fk_pacnameSelect" aria-labelledby="btnPacnameSelect">
							<c:forEach var="map" items="${requestScope.pacnameList}">
		   						<a class="dropdown-item" id="${map.pacnum}">${map.pacname}</a>
	    					</c:forEach>
						  </div>
					 	  
					  </div>
					  
                    </div>
                    <div class="col-md-3 pr-md-1">
                        <div class="dropdown">
					  	<button class="btn btn-primary dropdown-toggle" type="button" id="btnSdnameSelect" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					     	소분류상세명
					 	 </button>
					 	 <input type="hidden" name="sdname" id="sdname" value="">
						  <div class="dropdown-menu" id="fk_sdnameSelect" aria-labelledby="btnSdnameSelect">
						    <c:forEach var="map" items="${requestScope.sdnameList}">
		   						<a class="dropdown-item" id="${map.sdname}">${map.fk_ldname} > ${map.sdname}</a>
	    					</c:forEach>
						  </div>
					  </div>
                    </div>
                    <div class="col-md-3 pr-md-1">
                        <div class="dropdown">
					  	<button class="btn btn-primary dropdown-toggle" type="button" id="btnCtnameSelect" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					     	카테고리태그명
					 	 </button>
					 	 <input type="hidden" name="ctname" id="ctname" value="">
						  <div class="dropdown-menu" id="fk_ctnameSelect" aria-labelledby="btnCtnameSelect">
						    <c:forEach var="map" items="${requestScope.ctnameList}">
		   						<a class="dropdown-item" id="${map.ctnum}">${map.ctname}</a>
		   						
	    					</c:forEach>
						  </div>
					  </div>
                    </div>
                    <div class="col-md-3 pr-md-1">
                        <div class="dropdown">
					  	<button class="btn btn-primary dropdown-toggle" type="button" id="btnStnameSelect" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					     	스펙태그명
					 	 </button>
					 	 <input type="hidden" name="stname" id="stname" value="">
						  <div class="dropdown-menu" id="fk_stnameSelect" aria-labelledby="btnStnameSelect">
						    <c:forEach var="map" items="${requestScope.stnameList}">
		   						<a class="dropdown-item" id="${map.stnum}">${map.stname}</a>
		   						
	    					</c:forEach>
						  </div>
					  </div>
                    </div>
                    <div class="col-md-3 pr-md-1">
                        <div class="dropdown">
					  	<button class="btn btn-primary dropdown-toggle" type="button" id="btnEtnameSelect" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					     	이벤트태그명
					 	 </button>
					 	 <input type="hidden" name="etname" id="etname" value="">
						  <div class="dropdown-menu" id="fk_etnameSelect" aria-labelledby="btnEtnameSelect">
						    <c:forEach var="map" items="${requestScope.etnameList}">
		   						<a class="dropdown-item" id="${map.etnum}">${map.etname}</a>
	    					</c:forEach>
						  </div>
					  </div>
                    </div>
                    <div class="col-md-12 pl-md-8">
                      <div class="form-group">
                        <label>상품명</label>
                        <input type="text" class="form-control infoData" name="panme" id="pname" value="${pvo.pname}" ><span></span>
                        <input type="hidden" id="pnum" name="pnum" value="${pnum}" />
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-2 pr-md-1">
                      <div class="form-group">
                        <label>원가</label>
                        <input type="text" class="form-control infoData" name="price" id="price" value="${pvo.price}"><span></span>
                      </div>
                    </div>
                    <div class="col-md-2 pl-md-1">
                      <div class="form-group">
                        <label>판매가</label>
                        <input type="text" class="form-control infoData" name="saleprice" id="saleprice" value="${pvo.saleprice}" ><span></span>
                      </div>
                    </div>
                    <div class="col-md-2 pl-md-1">
                      <div class="form-group">
                        <label>포인트</label>
                        <input type="text" class="form-control infoData" name="point" id="point" value="${pvo.point}"><span></span>
                      </div>
                    </div>
                    <div class="col-md-2 pl-md-1">
                      <div class="form-group">
                        <label>재고량</label>
                        <input id="spinnerPqty" class="form-control infoData" name="pqty" value="${pvo.pqty}"><span></span>
                      </div>
                    </div>
                  </div>
                  
                  <div class="row">
                    <div class="col-md-8 pl-md-3">
                      <div class="form-group">
                        <label>상품설명</label>
                        <textarea rows="4" cols="80" class="form-control infoData" name="pcontents" id="pcontents" >${pvo.pcontents}</textarea><span></span>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                     <div class="col-md-8 pl-md-3">
                      <div class="form-group">
                        <label>알레르기정보</label>
                        <textarea rows="2" cols="80" class="form-control" name="allergy" id="allergy" >${pvo.allergy}</textarea><span></span>
                      </div>
                    </div>
                  </div> 
                  <div class="row">
                  	<div class="col-md-2 pr-md-1">
                      <div class="form-group">
                        <label>상품회사명</label>
                        <input type="text" class="form-control infoData" name="pcompanyname" id="pcompanyname" value="${pvo.pcompanyname}"><span></span>
                      </div>
                    </div>
                    <div class="col-md-3 pr-md-2">
                      <div class="form-group">
                        <label>유통기한</label>
                        <input type="text" class="form-control infoData" name="pexpiredate" id="pexpiredate" value="${pvo.pexpiredate}"><span></span>
                      </div>
                    </div>
                    <div class="col-md-2 pr-md-1">
                      <div class="form-group">
                        <label>용량</label>
                        <input type="text" class="form-control infoData" name="weight" id="weight" value="${pvo.weight}"><span></span>
                      </div>
                    </div>
                   </div>
                	<hr style="background-color: #2b3553; border-color: #2b3553;"/>
                  	<label>대표 이미지</label>
                  	<ul style="list-style-type: none;">
                  		<li><img src="<%= ctxPath %>/img/${pvo.titleimg}" style="width: 30%;">
                  		</li>
                  	</ul>
                  	
                  	<label>대표 이미지 수정</label>	              
		              <ul style="list-style-type: none;">
		              	<li>
		              		<input type="file" name="titleimg" id="titleimg" class="btn btn-primary btn-simple"/>
		              		<span></span>
		              	</li>
		              </ul>
                  	<hr style="background-color: #2b3553; border-color: #2b3553;"/>
                  	<label>추가이미지 </label>
                  	<ul style="list-style-type: none;">
                  		<c:if test="${imgList==null}">
                   				이미지가 없습니다.
                   		</c:if>
                   		<c:if test="${imgList!=null}">
                   		<c:forEach var="map" items="${imgList}" >
                 			<img src="<%= ctxPath %>/img/${map.pimgfilename}" style="width: 30%;"><br/>
                   		</c:forEach>
                   		</c:if>
                  	</ul>
                  	<label for="spinnerImgQty">추가이미지 수정</label>
				  		<input id="spinnerImgQty" class="form-control imgqty" value="0" style="width: 7%;">
				  		<ul style="list-style-type: none;" id="divfileattach"></ul>
		       			<input type="hidden" name="attachCount" id="attachCount" />
                </form>
              </div>
              
              <div class="card-footer">
                <button type="button" class="btn btn-fill btn-primary" id="btnEdit">수정하기</button>
              </div>
            </div>
          </div>
        </div>
       </div>
</body>
</html>