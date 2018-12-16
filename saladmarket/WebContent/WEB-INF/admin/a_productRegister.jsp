<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String ctxPath = request.getContextPath(); %>
<jsp:include page="admin_header.jsp"/>
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
		$("#fk_pacnameSelect").children().click(function(){
			var len = $(this).html().length;
			var html = $(this).text().trim();
			console.log(html);
			$("#pacname").val(html);
			
			if(len>6){
				html = html.substring(0,10);
			}
			$("#btnPacnameSelect").empty().html(html);

		});
		
		
		$("#fk_sdnameSelect").children().click(function(){
			var len = $(this).html().length;
			var html = $(this).html();
			
			var sdname = $(this).attr('id');
			console.log(sdname);
			$("#sdname").val(sdname);
			
			if(len>8){
				html = html.substring(0,13);
			}
			$("#btnSdnameSelect").empty().html(html);
			
			
		});
		
		
		$("#fk_ctnameSelect").children().click(function(){
			var len = $(this).html().length;
			var html = $(this).text();
			console.log(html);
			$("#ctname").val(html);
			if(len>6){
				html = html.substring(0,8);
			}
			$("#btnCtnameSelect").empty().html(html);
			
		});
		
		$("#fk_stnameSelect").children().click(function(){
			var len = $(this).html().length;
			var html = $(this).text();
			console.log(html);
			$("#stname").val(html);
			if(len>6){
				html = html.substring(0,8);
			}
			$("#btnStnameSelect").empty().html(html);
			
		});
		
		$("#fk_etnameSelect").children().click(function(){
			var len = $(this).html().length;
			var html = $(this).text();
			console.log(html);
			$("#etname").val(html);
			if(len>6){
				html = html.substring(0,8);
			}
			$("#btnEtnameSelect").empty().html(html);
			
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
		$("#btnRegister").bind("click", function(){
			
			
			var flag = false;
			$(".infoData").each(function(){ 
				var val = $(this).val();
				if(val == ""){
					$(this).next().text("필수입력 사항 입니다.");
					flag = true;
					return false;
				}
				else{
					$(this).next().text("");
				}
			});
			
			if(flag){
				event.preventDefault(); // 이벤트를 가로막는다(아래 form에 기재되어 있는 action을 취하지 않음)
				return;
			}
			else {
				var frm = document.productRegisterFrm;
				frm.method="POST";
				frm.action="a_productRegister.do";
				frm.submit();
			}
		});
		
		

	});



</script>
  <div class="row">
          <div class="col-md-3"></div>
          <div class="col-md-6">
            <div class="card">
              <div class="card-header">
                <h5 class="title">상품 등록</h5>
              </div>
              <div class="card-body">
                <form name="productRegisterFrm" enctype="multipart/form-data">
                  <div class="row">
                    <div class="col-md-3 pr-md-1">
                      <div class="dropdown">
					  <button class="btn btn-primary dropdown-toggle" type="button" id="btnPacnameSelect" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					     	상품패키지명
					  </button>
					  <input type="hidden" class="infoData" name="pacname" id="pacname" value=""><span></span>
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
					 	 <input type="hidden" class="infoData" name="sdname" id="sdname" value=""><span></span>
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
					 	 <input type="hidden" class="infoData" name="ctname" id="ctname" value=""><span></span>
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
					 	 <input type="hidden" class="infoData" name="stname" id="stname" value=""><span></span>
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
					 	 <input type="hidden" class="infoData" name="etname" id="etname" value=""><span></span>
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
                        <input type="text" class="form-control infoData" name="panme" id="pname" ><span></span>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-2 pr-md-1">
                      <div class="form-group">
                        <label>원가</label>
                        <input type="text" class="form-control infoData" name="price" id="price"><span></span>
                      </div>
                    </div>
                    <div class="col-md-2 pl-md-1">
                      <div class="form-group">
                        <label>판매가</label>
                        <input type="text" class="form-control infoData" name="saleprice" id="saleprice" ><span></span>
                      </div>
                    </div>
                    <div class="col-md-2 pl-md-1">
                      <div class="form-group">
                        <label>포인트</label>
                        <input type="text" class="form-control infoData" name="point" id="point" ><span></span>
                      </div>
                    </div>
                    <div class="col-md-2 pl-md-1">
                      <div class="form-group">
                        <label>재고량</label>
                        <input id="spinnerPqty" class="form-control infoData" name="pqty" value="1"><span></span>
                      </div>
                    </div>
                  </div>
                  
                  <div class="row">
                    <div class="col-md-8 pl-md-3">
                      <div class="form-group">
                        <label>상품설명</label>
                        <textarea rows="4" cols="80" class="form-control infoData" name="pcontents" id="pcontents" placeholder="설명을 입력하세요"></textarea><span></span>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                     <div class="col-md-8 pl-md-3">
                      <div class="form-group">
                        <label>알레르기정보</label>
                        <textarea rows="2" cols="80" class="form-control infoData" name="allergy" id="allergy" placeholder="알레르기 정보를 입력하세요"></textarea><span></span>
                      </div>
                    </div>
                  </div> 
                  <div class="row">
                  	<div class="col-md-2 pr-md-1">
                      <div class="form-group">
                        <label>상품회사명</label>
                        <input type="text" class="form-control infoData" name="pcompanyname" id="pcompanyname"><span></span>
                      </div>
                    </div>
                    <div class="col-md-3 pr-md-2">
                      <div class="form-group">
                        <label>유통기한</label>
                        <input type="text" class="form-control infoData" name="pexpiredate" id="pexpiredate"><span></span>
                      </div>
                    </div>
                    <div class="col-md-2 pr-md-1">
                      <div class="form-group">
                        <label>용량</label>
                        <input type="text" class="form-control infoData" name="weight" id="weight" placeholder="숫자만 입력"><span></span>
                      </div>
                    </div>
                   </div>
                  
	              <label>제품이미지(필수)</label>	              
		              <ul style="list-style-type: none;">
		              	<li>
		              		<input type="file" name="titleimg" id="titleimg" class="infoData btn btn-primary btn-simple"/>
		              		<span></span>
		              	</li>
		              </ul>
		              
	              <label for="spinnerImgQty">추가이미지(선택) </label>
				  <input id="spinnerImgQty" class="form-control imgqty" value="0" style="width: 7%;">
				  	<ul style="list-style-type: none;" id="divfileattach"></ul>
		       		<input type="hidden" name="attachCount" id="attachCount" /> 
                </form>
              </div>
              
              <div class="card-footer">
                <button type="button" class="btn btn-fill btn-primary" id="btnRegister">등록</button>
              </div>
            </div>
          </div>
        </div>

<jsp:include page="admin_footer.jsp"/> 