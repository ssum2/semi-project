<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="admin_header.jsp"/>


<script type="text/javascript">
	$(document).ready(function(){
		goSearch("", "name", "", "1");      // 초기치 설정

		// lvnum, searchtype, searchword, startpageno
		
//		#lvnum		
		$("#lvnum").bind("change", function(){
			var lvnum = $("#lvnum").val();
			var searchType= $("#searchType").val();
			var searchWord=$("#searchWord").val();
			var pageNo="1";
			goSearch(lvnum, searchType, searchWord, pageNo);
			
		});
		
		$("#btnSearch").click(function(){
			var lvnum = $("#lvnum").val();
			var searchType= $("#searchType").val();
			var searchWord=$("#searchWord").val();
			var pageNo="1";
			goSearch(lvnum, searchType, searchWord, pageNo);
		});
		
		/* $("#searchWord").keydown(function(event){
			if(event.keyCode==13){ 
				var lvnum = $("#lvnum").val();
				var searchType= $("#searchType").val();
				var searchWord=$("#searchWord").val();
				var pageNo="1";
				goSearch(lvnum, searchType, searchWord, pageNo);
			}
		}); */
		
	}); // end of ready

	
	function goSearch(lvnum, searchType, searchWord, pageNo) {
		
		var form_data = {lvnum:lvnum,
						searchType:searchType,
						searchWord:searchWord,
				        currentShowPageNo:pageNo};
		
		$.ajax({
				url: "a_memberListJSON.do",
				type: "GET",
				data: form_data,
				dataType: "JSON",
				success: function(data){
										
					if(data.length > 0) { 
					     var resultHTML = "";
					
						 $.each(data, function(entryIndex, entry){
							
							 resultHTML += "<tr>"+
				                            "<td>"+entry.mnum+"</td>"+
				                            "<td>"+entry.userid+"</td>"+
				                            "<td>"+entry.name+"</td>"+
				                            "<td>"+entry.email+"</td>"+
				                            "<td>"+entry.summoney+"</td>"+
				                            "<td>"+entry.lvname+"</td>"+
				                            "<td id='statusname"+entry.mnum+"'>"+entry.statusname+"</td>"+
				                            "<td class='td-actions text-right'>"+
				                            "<button type='button' rel='tooltip' class='btn btn-info btn-sm btn-icon' OnClick='goDetail("+entry.mnum+");'>"+
				                                "<i class='tim-icons icon-single-copy-04'></i>"+
				                            "</button>"+
				                            
				                            "<button type='butto' rel='tooltip' class='btn btn-success btn-sm btn-icon' OnClick='editStatus("+entry.mnum+", "+entry.status+")'>"+
				                            "<i class='tim-icons icon-settings'></i>"+
				                            "</button>"+
				                            "<button type='button' rel='tooltip' class='btn btn-danger btn-sm btn-icon' OnClick='goDelete("+entry.mnum+");'>"+
				                                "<i class='tim-icons icon-simple-remove'></i>"+
				                            "</button>"+
				                        	"</td>"+
				                            "</tr>";
				      	        
						 });// end of $.each
						 	
						 $("#displayResult").empty().html(resultHTML);
						 makePageBar(lvnum, searchType, searchWord, pageNo);
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
	}// end of goSearch
	
	
	function makePageBar(lvnum, searchType, searchWord, currentShowPageNo) {
		
		var form_data = {lvnum:lvnum,
						searchType:searchType,
						searchWord:searchWord,
				        len:"10"};
		
		$.ajax({
				url: "a_memberListPageBarJSON.do",
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

					      if(pageNo != 1) {	lvnum, searchType, searchWord, pageNo
					    	  pageBarHTML += "<a class='nav-link active' href='javascript:goSearch(\""+lvnum+"\" , \""+searchType+"\" , \""+searchWord+"\", \""+(pageNo-1)+"\")'>"+
					      					"<i class='tim-icons icon-minimal-left'></i></a>";
					      }

					      while( !(loop > blockSize || pageNo > totalPage) ) {
					       	 
					    	  if(pageNo == currentShowPageNo) {
					    		  pageBarHTML += "&nbsp;<span class='nav-link active' style=\"text-decoration: underline; \">"+pageNo+"</span>&nbsp;";
					    	  }
					    	  else {
					    	  	  pageBarHTML += "&nbsp;<a class='nav-link active' href='javascript:goSearch(\""+lvnum+"\" , \""+searchType+"\" , \""+searchWord+"\", \""+pageNo+"\")'>"+pageNo+"</a>&nbsp;";
					     	  }
	                     
					       	 loop++;
					    	 pageNo++;
					      } // end of while

					     if( !(pageNo > totalPage) ) {
					    	 pageBarHTML += "<a class='nav-link active' href='javascript:goSearch(\""+lvnum+"\" , \""+searchType+"\" , \""+searchWord+"\", \""+pageNo+"\")'>"+
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
	
	
	
	
//	#회원정보 수정하기
 	function goDetail(mnum){
		var url = "a_memberDetail.do?mnum="+mnum;
		window.open(url, "회원 정보 및 수정", "left=150px, top=50px, width=800px, height=900px");
		
	}
	
//	#회원 활성화/비활성화
	function editStatus(mnum, status){
		var editStatus = "";
		var statusMnum = "statusname"+mnum;
		
		if(status=="1"){
			editStatus = "0";
		}
		if(status=="0"){
			editStatus = "1";
		}
		
		var form_data = {mnum:mnum,
						status:editStatus};
	
		var bool = confirm(mnum + "번 회원을 활성화/비활성화 하시겠습니까?"); 
	    if(bool) {
	    	$.ajax({
	    		url: "a_memberStatusEdit.do",
	    		type: "GET",
	    		data: form_data,
	    		dataType: "JSON",
				success: function(json){
					if(json.result=="1"){
						alert("활성화 완료");
					//	$("#"+statusMnum+"").empty().html("활동");
						window.location.reload();
					}
					else if(json.result=="2"){
						alert("비활성화 완료");
					//	$("#"+statusMnum+"").empty().html("휴면");
						window.location.reload();
					}
					else{
						alert("회원 상태 변경 실패"+json.result);
						window.location.reload();
					}
				},
				error: function(request, status, error){
					if(request.readyState == 0 || request.status == 0) return;
					else alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
	    	});
	    }	
	}


	
	function goDelete(mnum){
		var form_data = {mnum:mnum};
		
		var bool = confirm(mnum + "번 회원을 강퇴 하시겠습니까?"); 
	
	    if(bool) {
	    	$.ajax({
	    		url: "a_memberDelete.do",
	    		type: "GET",
				data: form_data,
				dataType: "JSON",
				success: function(json){
	    			if(json.result==1){
	    				alert("회원 강퇴 완료");
	    				window.location.reload();
	    			}
	    			else{
	    				alert("회원 강퇴 실패");
	    				window.location.reload();
	    			}
				},
				error: function(request, status, error){
					if(request.readyState == 0 || request.status == 0) return;
					else alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
	    	});
	    
	    }
	}
</script>


    
      <div class="col-lg-12 col-md-12">
          <div class="card ">
            <form name="memberFrm">
              <div class="card-header">
                <h4 class="card-title"> 회원 목록 </h4>
                	<%-- 페이지 당 회원 수 
					<div class="text-left" style="display: inline;">
						<select  class="btn btn-secondary" id="len" name="len" style="padding: 5px;">
							<option class="dropdown-item" value="30">30</option>
							<option class="dropdown-item" value="15">15</option>
							<option class="dropdown-item" value="10">10</option>
						</select>
					</div>
					--%>
					<%-- 회원등급별 검색 --%>
					<div class="text-left" style="display: inline;">
						<select class="btn btn-secondary" id="lvnum" name="lvnum" style="padding: 5px;">
							<option class="dropdown-item" value="">전체</option>
							<option class="dropdown-item" value="1">Bronze</option>
							<option class="dropdown-item" value="2">Silver</option>
							<option class="dropdown-item" value="3">Gold</option>
						</select>
					</div>
					
					<div class="text-right" style="display: inline; text-align: right;">
		                <span>총 회원수: ${totalMemberCount}</span>&nbsp;&nbsp;
		                <span>신규회원수 : ${newbieMemberCount}</span>&nbsp;&nbsp;
		                <span>휴면회원수 : ${dormantMemberCount}</span>
		           
	 				</div>
				</div>
 			
	 			<%-- 검색 버튼 --%>              
	 			<div class="form-inline" style="margin-left: 80%;">
		          <div class="form-group no-border">
		          	<select class="btn btn-secondary" id="searchType" name="searchType" style="padding: 5px;">
						<option class="dropdown-item" value="name">회원명</option>
						<option class="dropdown-item" value="userid">아이디</option>
					</select>
		            <input type="text" id="searchWord" name="searchWord" class="form-control" placeholder="검색어를 입력하세요.">
		          </div>
		          <button type="button" class="btn btn-link btn-icon btn-round" id="btnSearch" name="btnSearch">
		              <i class="tim-icons icon-zoom-split"></i>
		          </button>
		        </div>
	      </form>
	         
	         
	         <%-- 출력 --%> 
              <div class="card-body">
                <div class="table-responsive">
                  <table class="table tablesorter">
                    <thead class=" text-primary">	
                      <tr>
                      	
                        <th>회원번호</th>
                        <th>아이디</th>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>누적금액</th>
                        <th>회원등급</th>
                        <th>회원상태</th>
                        <th class="text-right">상세&nbsp;&nbsp;&nbsp;&nbsp;수정&nbsp;&nbsp;&nbsp;&nbsp;강퇴</th>
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