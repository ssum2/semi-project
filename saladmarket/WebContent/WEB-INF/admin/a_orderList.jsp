<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="admin_header.jsp"/>
<%
	String ctxPath=request.getContextPath();
%>


<script>
	$(document).ready(function(){
		
		
		goSearchPage("","","1");
		
		$("#btnClick").click(function(){
			var searchType = $("#searchType").val();
			var searchWord = $("#searchWord").val();
			
			goSearchPage(searchType,searchWord,"1");
		});//end of $("#btnClick").click--------------
		
		
	});//end of $(document).ready--------------------

	function goSearchPage(searchType,searchWord,currentShowPage){
		
		var form_data = {"searchType":searchType,
				         "searchWord":searchWord,
				         "currentShowPage":currentShowPage};
		
		$.ajax({
			url:"a_searchDeliverList.do",
			type:"GET",
			data:form_data,
			dataType:"JSON",
			success:function(json){
				
				var resultHtml = "";
				
				if(json.length > 0){
					$.each(json,function(entryIndex,entry){
						var odrstatus = entry.odrstatus;
						resultHtml +=   "<tr>"
										+"<td class='text-center'>"+(entryIndex+1)+"</td>"
					                    +"<td class='text-center'><input type='hidden' name='odrcode' value='"+entry.odrcode+"'/>"+entry.odrcode+"</td>"
					                    +"<td class='text-center'>"+entry.odrdate+"</td>"
					                    +"<td class='text-center'>"+entry.fk_pnum+"</td>"
					                    /* +"<td class='text-center'><img src='img/"+entry.titleimg+"' style='width:10%;'/></td>" */
					                    +"<td class='text-center'><a onClick=\"showUser('"+entry.fk_userid+"');\" style='cursor:pointer; color:yellow;'>"+entry.fk_userid+"</a></td>"
					                    +"<td class='text-center'>"+entry.oqty+"</td>"
					                    +"<td class='text-center'>"+entry.odrtotalprice+"</td>"
					                    +"<td class='text-center' style='font-weight:bold;'>"+entry.odrstatus+"</td>"
					                    +"<td class='text-center' >";
					                    if("주문완료"==odrstatus){
					                    	resultHtml+="<button class='btn btn-success btn-sm' onClick=\"gostart('"+entry.odrcode+"');\" >배송시작</button>"
					                    	            +"<button class='btn btn-primary btn-sm' onClick=\"goCancel('"+entry.odrcode+"');\" >주문취소</button>";
								                    	
					                    }
					                    else if("배송중"==odrstatus){
					                    	resultHtml+="<button class='btn btn-warning btn-sm' onClick=\"goEnd('"+entry.odrcode+"');\" >배송완료</button>";
					                    }
					                    else if("주문취소"==odrstatus){
					                    	resultHtml+="<button class='btn btn-primary btn-sm' onClick=\"goCancel('"+entry.odrcode+"');\" disabled>주문취소</button>"
									                    +"<button class='btn btn-success btn-sm' onClick=\"gostart('"+entry.odrcode+"');\" disabled>배송시작</button>"
									                    +"<button class='btn btn-warning btn-sm' onClick=\"goEnd('"+entry.odrcode+"');\" disabled>배송완료</button>"
					                    }
					                    else{
					                    	resultHtml+="<button class='btn btn-primary btn-sm' >배송완료</button>"
							                    	   +"</td>"
											           +"</tr>";
					                    }
					                    
					})//end of $.each-----------
					$("#result").empty().html(resultHtml);
					goPageBar(searchType,searchWord,currentShowPage);
				}
				else{
					$("#result").empty().html("조회하신 내용이 존재하지 않습니다.");
				}
				
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
			
			
		});//end of $.ajax------------
	}
	
	
	function goPageBar(searchType,searchWord,currentShowPage){
		var form_data = {"stype":searchType,//검색하는 타입
				 "sword":searchWord,//검색하는 단어
				 "cursizePerPage":currentShowPage};//보여줄 회원명수
		$.ajax({
			url:"a_goPageDeliverBar.do",
			type:"GET",
			data:form_data,
			dataType:"JSON",
			success:function(json){
				if(json.totalPage != 0){
					
					var pageBarHtml = "";//페이지바를 넣을 변수
					
					var totalPage = json.totalPage; //전체페이지 갯수
					var blockSize = 2;  //한블락당 보여줄 페이지 갯수
					// 1 2 [다음]
					var loop = 1;
					//blockSize 증가시켜주는 변수
					var pageno = Math.floor((currentShowPage-1)/blockSize)*blockSize+1;
					//한블락의 초기값 =>  1 2[다음] => pageno = 1
					
					if(pageno != 1){
						pageBarHtml += "<a class='nav-link active' href='javascript:goSearchPage(\""+searchType+"\",\""+searchWord+"\",\""+(pageno-1)+"\")'>"
										+"<i class='tim-icons icon-minimal-left'></i></a>";
					}
					while( !(loop > blockSize || pageno > totalPage) ) {
				       	 
				    	  if(pageno == currentShowPage) {
				    		  pageBarHtml += "&nbsp;<span class='nav-link active' style=\"text-decoration: underline; \">"+pageno+"</span>&nbsp;";
				    	  }
				    	  else {
				    		  pageBarHtml += "&nbsp;<a class='nav-link active' href='javascript:goSearchPage(\""+searchType+"\",\""+searchWord+"\",\""+pageno+"\")'>"+pageno+"</a>&nbsp;";
				     	  }
		          
				       	 loop++;
				       	pageno++;
				      } // end of while
				    	  
				  	  // *** [다음] 만들기 *** //
				     if( !(pageno > totalPage) ) {
				    	 pageBarHtml += "&nbsp;<a class='nav-link active' href='javascript:goSearchPage(\""+searchType+"\",\""+searchWord+"\",\""+pageno+"\")'>"
				    	 				+"<i class='tim-icons icon-minimal-right'></i></a>";
				     }
					 	
				     $("#pageBar").empty().html(pageBarHtml);
				     
				     pageBarHTML = "";
				}
				
				else { // 검색된 데이터가 없는 경우
					 $("#pageBar").empty();
				}
				
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
			
		});//end of $.ajax-------
	} 

	function goCancel(odrcode){
		
		var bool = confirm("주문취소로 변경하시겠습니까?");
		
		if(bool == true){
			var frm = document.deliverFrm;
			frm.method="POST";
			frm.action="a_DeliverChange.do?odrcode="+odrcode;
			frm.submit();
		}
		else{
			alert("변경을 취소하였습니다.");
		}
		
	}//end of goCancel-----------
	
	function gostart(odrcode){
		
		var bool = confirm("배송시작으로 변경하시겠습니까?");
		if(bool == true){
			var frm = document.deliverFrm;
			frm.method="POST";
			frm.action="a_DeliverStart.do?odrcode="+odrcode;
			frm.submit();
		}
		else{
			alert("변경을 취소하였습니다.");
		}
		
	}//end of gostart-----------
	
	function goEnd(odrcode){
		var bool = confirm("배송완료로 변경하시겠습니까?");
		if(bool == true){
			var frm = document.deliverFrm;
			frm.method="POST";
			frm.action="a_DeliverEnd.do?odrcode="+odrcode;
			frm.submit();
		}
		else{
			alert("변경을 취소하였습니다.");
		}
	}//end of goEnd----------
	
	function showUser(userid){
		var url = "a_memberInfo.do?userid="+userid;
		window.open(url, "회원 정보 및 수정", "left=150px, top=50px, width=800px, height=900px");
		
	}

</script>

<%-- 검색 버튼 --%>              
<div class="form-inline ml-auto" style="margin-left: 65%;">

  <div class="col-lg-4 col-md-4">
  
	  <select class="form-control" id="searchType">
	    <option class="form-control" value="fk_userid">아이디</option>
	    <option class="form-control" value="fk_pnum">제품번호</option>
	  </select>
	  <input type="text" class="form-control" placeholder="Search" id="searchWord" style="margin-left: 2%;">
		 <button type="button" class="btn btn-link btn-icon btn-round" >
		     <i class="tim-icons icon-zoom-split" id="btnClick"></i>
		 </button>
  </div>
</div>
<form name="deliverFrm">
<div class="content">
      <div class="col-lg-12 col-md-12">
            <div class="card ">
              	<div class="card-header">
                	<h4 class="card-title"> 주문 목록 </h4>
              	</div>
              	<div class="card-body">
                	<div class="table-responsive">
                  		<table class="table tablesorter" id="">
                  		
                    	<thead class="text-primary">	
                      		<tr>
                      			<th class="text-center">주문번호</th>
		                        <th class="text-center">주문코드</th>
		                        <th class="text-center">주문일자</th>
		                        <th class="text-center">제품번호</th>
		                        <!-- <th class="text-center">제품이미지</th> -->
		                        <th class="text-center">아이디</th>
		                        <th class="text-center">수량</th>
		                        <th class="text-center">총금액</th>
		                        <th class="text-center">배송상태</th>
		                        <th class="text-center">배송상태변경</th>
		                    </tr>
                    	</thead>
                    	
                    	<tbody id="result"></tbody>
                  	</table>
                </div>
             </div>
             
         </div>
     </div>
     <div id="pageBar" align="center" style="color:white;"></div>
</div>
</form>
<jsp:include page="admin_footer.jsp"/> 

