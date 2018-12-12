<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<jsp:include page="admin_header.jsp"/> 



<script type="text/javascript">
	$(document).ready(function(){

		getProductList("", "", "", "", "1");
		
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
			var ldname = $(this).val();
	    	getProductList(ldname, "", "", "", "1");	
	    });
	 	
	 	$("#sdname").bind("change", function(){
	 		var ldname = $("#ldname").val();
	 		var sdname = $(this).val();
	 		var pageNo = "1";
	    	getProductList(ldname, sdname, "", "", pageNo);
	 	});
		
	 	$("#btnSearch").click(function(){
	 		var ldname = $("#ldname").val();
	 		var sdname = $("#sdname").val();
			var searchType= $("#searchType").val();
			var searchWord=$("#searchWord").val();
			var pageNo="1";
			getProductList(ldname, sdname, searchType, searchWord, pageNo);
		});
	});
	
	function getProductList(ldname, sdname, searchType, searchWord, pageNo){
		var form_data = {"ldname":ldname,
						"sdname":sdname,
						"searchType":searchType,
						"searchWord":searchWord,
						"currentShowPageNo":pageNo};
			
		$.ajax({
			url: "a_productListEnd.do",
			type: "GET",
			data: form_data,
			dataType: "JSON",
			success: function(data){
				if(data.length > 0) { 
				     var resultHTML = "";
				
				 	$.each(data, function(entryIndex, entry){
					
					 resultHTML += "<tr>"+
		                            "<td class='text-center'>"+entry.pnum+"</td>"+
		                            "<td>"+entry.fk_ldname+"</td>"+
		                            "<td>"+entry.fk_sdname+"</td>"+
		                            "<td>"+entry.fk_pacname+"</td>"+
		                            "<td><img src='/saladmarket/img/"+entry.titleimg+"' width='100px'></td>"+
		                            "<td>"+entry.pname+"</td>"+		                 
		                            "<td>"+(entry.saleprice).toLocaleString('en')+"</td>"+
		                            "<td>"+entry.pqty+"</td>"+
		                            "<td>"+entry.plike+"</td>"+
		                            "<td class='td-actions text-right'>"+
		                            "<button type='button' rel='tooltip' class='btn btn-info btn-sm btn-icon' OnClick='goDetail("+entry.pnum+");'>"+
		                                "<i class='tim-icons icon-single-copy-04'></i>"+
		                            "</button>"+
		                            "<button type='button' rel='tooltip' class='btn btn-danger btn-sm btn-icon' OnClick='goDelete("+entry.pnum+");'>"+
		                                "<i class='tim-icons icon-simple-remove'></i>"+
		                            "</button>"+
		                        	"</td>"+
		                            "</tr>";
		      	        
				 });// end of $.each
				 	
				 $("#displayResult").empty().html(resultHTML);
				 makePageBar(ldname, sdname, searchType, searchWord, pageNo);
			}
			else { // 검색된 데이터가 없는 경우
				 $("#displayResult").empty();
			}
			
			},// end of success
			error: function(request, status, error){
				if(request.readyState == 0 || request.status == 0) return;
				else alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});// end of $.ajax
	}
	
	function makePageBar(ldname, sdname, searchType, searchWord, currentShowPageNo) {
		
		var form_data = {"ldname":ldname,
						"sdname":sdname,
						"searchType":searchType,
						"searchWord":searchWord,
				        "len":"10"};
		
		$.ajax({
				url: "a_productListPageBarJSON.do",
				type: "GET",
				data: form_data,
				dataType: "JSON",
				success: function(json){
										
					if(json.totalPage != 0) { 
					     var totalPage = json.totalPage;
					     var pageBarHTML = "";
	
					     var blockSize = 3;
					     var loop = 1;

	                     var pageNo = Math.floor((currentShowPageNo - 1)/blockSize) * blockSize + 1; 

					      if(pageNo != 1) {	
					    	  pageBarHTML += "<a class='nav-link active' href='javascript:getProductList(\""+ldname+"\" , \""+sdname+"\", "+searchType+"\" , \""+searchWord+"\", \""+(pageNo-1)+"\")'>"+
					      					"<i class='tim-icons icon-minimal-left'></i></a>";
					      }

					      while( !(loop > blockSize || pageNo > totalPage) ) {
					       	 
					    	  if(pageNo == currentShowPageNo) {
					    		  pageBarHTML += "&nbsp;<span class='nav-link active' style=\"text-decoration: underline; \">"+pageNo+"</span>&nbsp;";
					    	  }
					    	  else {
					    	  	  pageBarHTML += "&nbsp;<a class='nav-link active' href='javascript:getProductList(\""+ldname+"\" , \""+sdname+"\", \""+searchType+"\" , \""+searchWord+"\", \""+pageNo+"\")'>"+pageNo+"</a>&nbsp;";
					     	  }
	                     
					       	 loop++;
					    	 pageNo++;
					      } // end of while

					     if( !(pageNo > totalPage) ) {
					    	 pageBarHTML += "<a class='nav-link active' href='javascript:getProductList(\""+ldname+"\" , \""+sdname+"\", \""+searchType+"\" , \""+searchWord+"\", \""+pageNo+"\")'>"+
		      					"<i class='tim-icons icon-minimal-right'></i></a>";
					     }
						 	
					     $("#pageBar").empty().html(pageBarHTML);
					     
					     pageBarHTML = "";
					}
					
					else { 
						 $("#pageBar").empty();
					}

				},// end of success: function()
				error: function(request, status, error){
					if(request.readyState == 0 || request.status == 0) return;
					else alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});// end of $.ajax()-------------------
		
	}// end of makePageBar
	
	function goDetail(pnum) {
		var url = "a_productDetail.do?pnum="+pnum;
		window.open(url, "상품 상세 정보",
				    "width=800px, height=800px, top=50px, left=800px");
		
	}
	
	function goDelete(pnum){
		var bool = confirm("해당 상품을 삭제하시겠습니까?");
		
		if(bool){
			location.href="a_deleteProduct.do?pnum="+pnum;
		}
		else{
			return false;
		}
		
	}


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
				<option class="dropdown-item" value="fk_pacname">패키지명</option>
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
		            <th>대표이미지</th>
		            <th>상품명</th>
		            <th>판매가</th>
		            <th>재고량</th>
		            <th>좋아요수</th>
		            <th class="text-right">상세&nbsp;&nbsp;&nbsp;&nbsp;삭제</th>
		        </tr>
		    </thead>
		    <tbody id="displayResult">
		        <tr>
		            <td class="text-center">1</td>
		            <td>디톡스</td>
		            <td>물/주스</td>
		            <td>델몬트</td>
		            <td>[델몬트] 오렌지주스</td>
		            <td><img src="/saladmarket/img/111.jpg" width="100px"></td>
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










