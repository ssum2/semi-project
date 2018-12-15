<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<jsp:include page="admin_header.jsp"/> 



<script type="text/javascript">
	$(document).ready(function(){

		getPackageList("", "1");
		
	 	$("#btnSearch").click(function(){
			var searchWord=$("#searchWord").val();
			var pageNo="1";
			getPackageList(searchWord, pageNo);
		});
	});
	
	function getPackageList(searchWord, pageNo){
		var form_data = {"searchWord":searchWord,
						"currentShowPageNo":pageNo};
			
		$.ajax({
			url: "a_packageListEnd.do",
			type: "GET",
			data: form_data,
			dataType: "JSON",
			success: function(data){
				if(data.length > 0) { 
				     var resultHTML = "";
				
				 	$.each(data, function(entryIndex, entry){
					
					 resultHTML += "<tr>"+
		                            "<td class='text-center'>"+entry.pacnum+"</td>"+
		                            "<td>"+entry.pacname+"</td>"+
		                            "<td>";
		             if(entry.pacimage == "없음"){
		            	 resultHTML += "이미지가 없습니다.";
		             }
		             else{
		            	 resultHTML += "<img src='/saladmarket/img/"+entry.pacimage+"' width='100px'>";
		             }
		             
		             resultHTML += "</td>"+
		                            "<td>"+entry.cnt+"</td>"+
		                            "<td class='td-actions text-right'>"+
		                            "<button type='button' rel='tooltip' class='btn btn-info btn-sm btn-icon' OnClick='goDetail("+entry.pacnum+");'>"+
		                                "<i class='tim-icons icon-single-copy-04'></i>"+
		                            "</button>"+
		                            "<button type='button' rel='tooltip' class='btn btn-danger btn-sm btn-icon' OnClick='goDelete("+entry.pacnum+");'>"+
		                                "<i class='tim-icons icon-simple-remove'></i>"+
		                            "</button>"+
		                        	"</td>"+
		                            "</tr>";
		      	        
				 });// end of $.each
				 	
				 $("#displayResult").empty().html(resultHTML);
				 makePageBar(searchWord, pageNo);
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
	
	function makePageBar(searchWord, currentShowPageNo) {
		
		var form_data = {"searchWord":searchWord,
				        "len":"3"};
		
		$.ajax({
				url: "a_packageListPageBarJSON.do",
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
					    	  pageBarHTML += "<a class='nav-link active' href='javascript:getPackageList(\""+searchWord+"\", \""+(pageNo-1)+"\")'>"+
					      					"<i class='tim-icons icon-minimal-left'></i></a>";
					      }

					      while( !(loop > blockSize || pageNo > totalPage) ) {
					       	 
					    	  if(pageNo == currentShowPageNo) {
					    		  pageBarHTML += "&nbsp;<span class='nav-link active' style=\"text-decoration: underline; \">"+pageNo+"</span>&nbsp;";
					    	  }
					    	  else {
					    	  	  pageBarHTML += "&nbsp;<a class='nav-link active' href='javascript:getPackageList(\""+searchWord+"\", \""+pageNo+"\")'>"+pageNo+"</a>&nbsp;";
					     	  }
	                     
					       	 loop++;
					    	 pageNo++;
					      } // end of while

					     if( !(pageNo > totalPage) ) {
					    	 pageBarHTML += "<a class='nav-link active' href='javascript:getPackageList(\""+searchWord+"\", \""+pageNo+"\")'>"+
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
	
	function goDetail(pacnum) {
		var url = "a_packageDetail.do?pacnum="+pacnum;
		window.open(url, "패키지 상세 정보",
				    "width=800px, height=800px, top=50px, left=800px");
		
	}
	
	function goDelete(pacnum){
		var bool = confirm("해당 패키지를 삭제하시겠습니까? 패키지를 삭제하시면 관련 상품이 모두 삭제됩니다.");
		
		if(bool){
			location.href="a_deletePackage.do?pacnum="+pacnum;
		}
		else{
			return false;
		}
		
	}

	function addPackage(){
		var url = "a_packageRegister.do";
		window.open(url, "신규 패키지 등록하기",
				    "width=800px, height=550px, top=50px, left=800px");
	}

</script>


<div class="col-lg-12 col-md-12">
<div class="card ">
  <form name="productFrm">
    <div class="card-header">
      <h4 class="card-title"> 패키지 목록 </h4>
	</div>

	<%-- 검색 버튼 --%>              
	<div class="form-inline" style="margin-left: 80%;">
		<div class="form-group no-border">
		    <input type="text" id="searchWord" name="searchWord" class="form-control" placeholder="상품명을 입력하세요">
		</div>
		<button type="button" class="btn btn-link btn-icon btn-round" id="btnSearch" name="btnSearch">
			<i class="tim-icons icon-zoom-split"></i>
		</button>
	</div>
  </form>
  <div class="text-left" style="margin-left: 6%;">
		<button type="button" id="eventAdd" rel="tooltip" class="btn btn-default btn-sm btn-icon" onClick="addPackage();">
			<i class="tim-icons icon-simple-add"></i>
		</button>
	</div>
  
  
	 <div class="card-body">
               <div class="table-responsive">
                 <table class="table tablesorter">
                   <thead class=" text-primary">	
                     <tr>
		            <th class="text-center">패키지번호</th>
		            <th>패키지명</th>
		            <th>이미지</th>
		            <th>수량</th>
		            <th class="text-right">상세&nbsp;&nbsp;&nbsp;&nbsp;삭제</th>
		        </tr>
		    </thead>
		    <tbody id="displayResult">
		        
		    </tbody>
		</table>
		
		</div>
		<div class="nav justify-content-center" id="pageBar">
		</div>


      </div>
    </div>
  </div>
<jsp:include page="admin_footer.jsp"/> 










