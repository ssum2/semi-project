<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<jsp:include page="admin_header.jsp"/> 



<script type="text/javascript">
	$(document).ready(function(){
		
		var sdnameOptions = "";
		sdnameOptions = "<option class='dropdown-item' value=''>소분류</option>";
		$("#sdname").empty().append(sdnameOptions);
		
		
		
		
		
	 	$("#ldname").bind("change", function(){
	    	var form_data = {"ldname":$(this).val()};
	    	$.ajax({
					url: "getSdnameJSON.do",
					type: "GET",
					data: form_data,  
					dataType: "JSON",
					success: function(data){
											
						if(data.length > 0) {
							var resultHTML = "<option class='dropdown-item' value=''>전체</option>";
						
							$.each(data, function(entryIndex, entry){
								resultHTML += "<option class='dropdown-item' value='"+entry.sdname+"'>"+entry.sdname+"</option>";
							});
							
							$("#sdname").empty().append(resultHTML);
						}
						else { 
							
						}

					},
					error: function(request, status, error){
						alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
					}
				});// end of $.ajax()
				
	    });
		
		
	});
	
	
	function goDetail() {
		
		var frm = document.productFrm;
		frm.method = "POST";
		frm.action = "a_productDetail.do";
		frm.submit();
		
	}// end of goDetail()----------------------------


</script>


<div class="col-lg-12 col-md-12">
<div class="card ">
  <form name="productFrm">
    <div class="card-header">
      <h4 class="card-title"> 상품 목록 </h4>

		<%-- 대분류, 소분류 검색 --%>
		<div class="text-left" style="display: inline;">
		<select class="btn btn-secondary" id="ldname" name="ldname" style="padding: 5px;">
				<option class="dropdown-item" value="">대분류</option>
				<c:forEach var="map" items="${requestScope.ldnameList}">
		   		<option class="dropdown-item" value="${map.ldname}">${map.ldname}</option>
	    		</c:forEach>
			</select>
		</div>
		<div class="text-left" style="display: inline;">
			<select class="btn btn-secondary" id="sdname" name="sdname" style="padding: 5px;">
				
			</select>
		</div>
	</div>

	<%-- 검색 버튼 --%>              
	<div class="form-inline" style="margin-left: 80%;">
		<div class="form-group no-border">
			<select class="btn btn-secondary" id="searchType" name="searchType" style="padding: 5px;">
				<option class="dropdown-item" value="">전체</option>
				<option class="dropdown-item" value="pacname">패키지명</option>
				<option class="dropdown-item" value="pname">상품명</option>
			</select>
		    <input type="text" id="searchWord" name="searchWord" class="form-control" placeholder="검색어를 입력하세요.">
		</div>
		<button type="button" class="btn btn-link btn-icon btn-round" id="btnSearch" name="btnSearch">
			<i class="tim-icons icon-zoom-split"></i>
		</button>
	</div>
  </form>
  
  
  
	 <div class="card-body">
               <div class="table-responsive">
                 <table class="table tablesorter">
                   <thead class=" text-primary">	
                     <tr>
		            <th class="text-center">상품번호</th>
		            <th>대분류</th>
		            <th>소분류</th>
		            <th>패키지명</th>
		            <th>상품명</th>
		            <th>대표이미지</th>
		            <th>판매가</th>
		            <th>재고량</th>
		            <th>좋아요수</th>
		            <th class="text-right">상세&nbsp;&nbsp;&nbsp;&nbsp;수정&nbsp;&nbsp;&nbsp;&nbsp;삭제</th>
		        </tr>
		    </thead>
		    <tbody>
		        <tr>
		            <td class="text-center">1</td>
		            <td>디톡스</td>
		            <td>물/주스</td>
		            <td>델몬트</td>
		            <td>[델몬트] 오렌지주스</td>
		            <td><img src="/saladmarket/img/111.JPG" width="100px"></td>
		            <td>2000</td>
		            <td>100</td>
		            <td>23</td>
		            <td class="td-actions text-right">
		                <button type="button" rel="tooltip" class="btn btn-info btn-sm btn-icon" OnClick="goDetail();">
		                    <i class="tim-icons icon-single-copy-04"></i>
		                </button>
		                <button type="button" rel="tooltip" class="btn btn-success btn-sm btn-icon">
		                    <i class="tim-icons icon-settings"></i>
		                </button>
		                <button type="button" rel="tooltip" class="btn btn-danger btn-sm btn-icon">
		                    <i class="tim-icons icon-simple-remove"></i>
		                </button>
		            </td>
		            
		        </tr>
		    </tbody>
		</table>
		
		</div>
		<div class="nav justify-content-center" id="pageBar">
		</div>


            </div>
          </div>
        </div>
<jsp:include page="admin_footer.jsp"/> 










