<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="admin_header.jsp"/>


<script type="text/javascript">
	$(document).ready(function(){
		displayMemberListAppend("1");

//		#더보기 버튼 클릭액션 이벤트 
		$("#btnMoreMemberList").click(function(){
			if($(this).text()=="처음으로 △"){
				$("#displayResult").empty();
				displayMemberListAppend("1");
				$(this).text("더보기 ▽");
			}
			else{
				displayMemberListAppend($(this).val());
			//	>> 버튼에 저장되어있는 시작물품번호를 함수의 파라미터에 넣어줌			
			}
		});
	
	}); // end of ready
	

	
// #검색하기 버튼을 눌렀을 때 폼 제출
	function goSearch() {
		var searchWord = $("#searchWord").val().trim();
		
		if(searchWord == ""){
			alert("검색어를 입력하세요.");
			return;
			
		}
		else {
			var frm = document.memberFrm;
			frm.method= "GET";
			frm.action= "memberList.do";
			frm.submit();
		}
	}

	var len = $("#len").val();
	console.log(len);
	var lvnum = $("#lvnum").val();
	
	
	
	console.log(lvnum);
	function displayMemberListAppend(start){
		var form_data = {"start":start,
						 "len":len,
						 "lvnum":lvnum};
		
		$.ajax({
			url:"a_memberListJSON.do",
			type:"GET",
			data: form_data,
			dataType:"JSON",
			success:function(json){
				
				var html = "";
				
				if(json.length == 0){ // 데이터가 없는 경우
					html += "조건에 일치하는 회원이 없습니다.";
							
					$("#displayResult").html(html);
					
					// 더보기 버튼 비활성화
					$("#btnMoreMemberList").attr("disabled", true);
					$("#btnMoreMemberList").css("cursor", "not-allowed");
					
				}
				else{
					$.each(json, function(entryIndex, entry){ // entry.key값
			        	  html += "<td style='text-align: center;'><input type='checkbox' name='checknum' value="+entry.mnum+"/></td>"+
	                            "<td>"+entry.mnum+"</td>"+
	                            "<td>"+entry.userid+"</td>"+
	                            "<td>"+entry.name+"</td>"+
	                            "<td>"+entry.email+"</td>"+
	                            "<td>"+entry.summoney+"</td>"+
	                            "<td>"+entry.lvname+"</td>"+
	                            "<td>"+entry.status+"</td>"
			              } ); // end of $.each()---------------------------
			          
			         html += "<div style=\"clear: both;\">&nbsp;</div>";
					
					$("#displayNEWResult").append(html);
					
					// 더보기 버튼의 value에 페이지번호 주기(다음 페이지의 첫번째 물품번호; 1페이지의 끝 8번 -> 2페이지의 시작 9번)
					$("#btnMoreMemberList").val(parseInt(start)+lenNEW);
					
					// 웹브라우저 상에 count를 출력; 현재 페이지의 물품갯수+다음페이지 물품갯수를 countNEW에 누적
					$("#memberCnt").text(parseInt($("#memberCnt").text())+json.length);
					
					// 더보기 버튼을 눌렀을 때 countNEW의 값과 totalNEWCount값이 일치하는 경우
					// 더보기 버튼을 '처음으로'로 변경하고 countNEW에는 0으로 초기화 
					if($("#totalMemberCnt").text() == $("#memberCnt").text()){
						$("#btnMoreMemberList").text("처음으로 △");
						$("#memberCnt").text("0");
					}
					
				}
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		}); // end of ajax
	} // end of function
	
	
</script>


    
      <div class="col-lg-12 col-md-12">
          <div class="card ">
            <form name="memberFrm">
              <div class="card-header">
                <h4 class="card-title"> 회원 목록 </h4>
                	<%-- 페이지 당 회원 수 --%>
					<div class="text-left" style="display: inline;">
						<select  class="btn btn-secondary" id="len" name="len" style="padding: 5px;">
							<option class="dropdown-item" value="30">30</option>
							<option class="dropdown-item" value="15">15</option>
							<option class="dropdown-item" value="10">10</option>
						</select>
					</div>

					<%-- 회원등급별 검색 --%>
					<div class="text-left" style="display: inline;">
						<select class="btn btn-secondary" id="lvnum" name="lvnum" style="padding: 5px;">
							<option class="dropdown-item" value="-1">전체</option>
							<option class="dropdown-item" value="1">Bronze</option>
							<option class="dropdown-item" value="2">Silver</option>
							<option class="dropdown-item" value="3">Gold</option>
						</select>
					</div>
					
					<div class="text-right" style="display: inline; text-align: right;">
		                <span>총 회원수: 100</span>&nbsp;&nbsp;
		                <span>신규회원수 : 100</span>&nbsp;&nbsp;
		                <span>휴면회원수 : 100</span>&nbsp;&nbsp;
		                <span>탈퇴회원수 : 100</span>
	 				</div>
				</div>
 			
	 			<%-- 검색 버튼 --%>              
	 			<div class="form-inline" style="margin-left: 80%;">
		          <div class="form-group no-border">
		          	<select class="btn btn-secondary" id="searchType" name="searchType" style="padding: 5px;">
						<option class="dropdown-item" value="name">회원명</option>
						<option class="dropdown-item" value="userid">아이디</option>
						<option class="dropdown-item" value="email">이메일</option>
					</select>
		            <input type="text" class="form-control" placeholder="검색어를 입력하세요.">
		          </div>
		          <button type="button" class="btn btn-link btn-icon btn-round">
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
                      	<th style="text-align: center;">check</th>
                        <th>회원번호</th>
                        <th>아이디</th>
                        <th>이름</th>
                        <th>이메일</th>
                        <th>누적금액</th>
                        <th>회원등급</th>
                        <th>회원상태</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr id="displayResult">
                      	<td style="text-align: center;">
                      	<input type="checkbox" name="checknum" />
                      	</td>
                        <td>mnum</td>
                        <td>userid</td>
                        <td>name</td>
                        <td>email</td>
                        <td>summoney</td>
                        <td>fk_lvnum</td>
                        <td>status</td>
                      </tr>
                    </tbody>
                  </table>
				</div>
				
				<div class="text-center"style="margin-top: 20px; margin-bottom: 20px;">
					<button type="button" id="btnMoreMemberList" class="btn btn-default btn-simple btn-sm" value="" style="color: white;" > 더보기 ▽</button>
					<span id="memberCnt">0</span>
					<span id="totalMemberCnt">${totalMemberCnt}</span>
					
				 </div>
              </div>
            </div>
          </div>
<jsp:include page="admin_footer.jsp"/>  