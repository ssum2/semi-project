<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="admin_header.jsp"/>

<script type="text/javascript">
	$(document).ready(function(){
		showEventList("");
	    $('[data-toggle="tooltip"]').tooltip(); 
	    
	    
	    $('.imgtooltip').tooltip({
	           items:'[data-photo]',
	            content:function(){
	                var photo = $(this).data('photo');
	                return '<img src="/saladmarket/img/'+photo+'"/>';
	            }
	    });
	    
	    
	    $("#btnSearch").click(function(){
	 		var searchWord = $("#searchWord").val();
	 		showEventList(searchWord);
	 	});
	 	

	});

	function showEventList(searchWord){
		var form_data = {"searchWord":searchWord}
		$.ajax({
			url: "a_eventTagListJSON.do",
			type: "GET",
			data: form_data,
			dataType: "JSON",
			success: function(json){
				
				var html ="";
				$.each(json, function(entryIndex, entry){	
					html += "<tr>"+
					           "<td class='text-center'>"+entry.etnum+
				               "</td>"+
				               "<td class='text-center'>"+entry.etname+
				               "</td>"+
				               "<td class='text-center'>"+entry.cnt+
				               "</td>"+
				               "<td class='td-actions text-center'>";
							     if(entry.etimagefilename==null){
							    	html+="이미지가 없습니다.";
							     }          
							     else{
							    	 html+="<img src='/saladmarket/img/"+entry.etimagefilename+"' width='200px'>";
							     }
				     
				               /*
				            	"<button type='button' rel='tooltip' class='btn btn-success btn-sm btn-icon imgtooltip' onClick='showImg("+entry.etimagefilename+")/;'>"+
				                "<i class='tim-icons icon-zoom-split'></i>"+
				                "</button>"+
				                */
				     html +=   "</td>"+
				               "<td class='td-actions text-center'>"+
				                "<button type='button' rel='tooltip' class='btn btn-success btn-sm btn-icon' onClick='goEdit("+entry.etnum+");'>"+
				                "<i class='tim-icons icon-settings'></i>"+
				                "</button>"+
				                "<button type='button' rel='tooltip' class='btn btn-danger btn-sm btn-icon' onClick='goDel("+entry.etnum+")'>"+
				                "<i class='tim-icons icon-simple-remove'></i>"+
				                "</button>"+
				               "</td>"+
			                 "</tr>";
				
				});
				
				$("#resultList").empty().html(html);
				$(".categoryeditattach").hide();
				
				$(".categoryEdit").click(function() {
			        $(this).parent().parent().next().find(".categoryeditattach").toggle('display'); 
			    });

			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
	
	function addEventTag(){
		var addCategoryName = $("#addCategoryName").val();
		var form_data={"addCategoryName":addCategoryName};
		
		$.ajax({
			url: "a_addCategoryTag.do",
			type: "POST",
			data: form_data,
			dataType: "JSON",
			success: function(json){
				if(json.result==1){
					alert("추가 성공");
					window.location.reload();
				}
				else{
					alert("추가 실패");
					window.location.reload();
				}
				
				
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
	/*
	function showImg(etimagefilename){
		var url = "a_eventImgShow.do?etimagefilename="+etimagefilename;
		window.open(url, "이벤트 태그 상세이미지", "width=800px, height=400px, top=50px, left=700px");
		
	}
	*/
	function goEdit(etnum) {
		var url = "a_eventEdit.do?etnum="+etnum;
		window.open(url, "이벤트 태그 수정",
				    "width=600px, height=500px, top=50px, left=800px");
	}
	
	
	function goDel(etnum){
		var bool = confirm("해당 이벤트 태그를 삭제하시겠습니까?");
		
		if(bool){
			location.href="a_deleteEventTag.do?etnum="+etnum;
		}
		else{
			return false;
		}
	}
	
	function addEvent(){
		var url = "a_eventAdd.do";
		window.open(url, "이벤트 태그 생성하기",
				    "width=600px, height=500px, top=50px, left=800px");
	}

</script>


 
<%-- 검색 버튼 --%>              
<div class="form-inline ml-auto" style="margin-left: 65%;">
 <div class="form-group no-border">
   <input type="text" class="form-control" id="searchWord" placeholder="Search">
 </div>
 <button type="button" id="btnSearch" class="btn btn-link btn-icon btn-round">
     <i class="tim-icons icon-zoom-split"></i>
 </button>
</div>
<div>
	<div class="text-right">
		<button type="button" id="eventAdd" rel="tooltip" class="btn btn-default btn-sm btn-icon" onClick="addEvent();">
			<i class="tim-icons icon-simple-add"></i>
		</button>
	</div>
</div>
<table class="table">
    <thead>
        <tr>
            <th class="text-center">이벤트번호</th>
            <th class="text-center">이벤트명</th>
            <th class="text-center">수량</th>
            <th class="text-center">이벤트이미지</th>
            <th class="text-center">수정&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;삭제</th>
        </tr>
    </thead>
    <tbody id="resultList">
       
    </tbody>
</table>

<jsp:include page="admin_footer.jsp"/> 

