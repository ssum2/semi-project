<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="admin_header.jsp"/>
<script>
$(document).ready(function(){
	showCategoryList("");
	
	$('#divfileattach').hide();
	$(".categoryeditattach").hide();
	
    $('[data-toggle="popover"]').popover();

    
    $("#categoryAdd").click(function() {
        
        $('#divfileattach').toggle('display'); 
    });
    
 	$(".categoryEdit").click(function() {
        $(this).parent().parent().parent().find(".categoryeditattach").toggle('display'); 
    });
 	
 	$("#btnSearch").click(function(){
 		var searchWord = $("#searchWord").val();
 		showCategoryList(searchWord);
 	});
 	
 	
    
});

	function showCategoryList(searchWord){
		var form_data = {"searchWord":searchWord}
		$.ajax({
			url: "a_categoryTagListJSON.do",
			type: "GET",
			data: form_data,
			dataType: "JSON",
			success: function(json){
				
				var html ="";
				$.each(json, function(entryIndex, entry){	
					html += "<tr>"+
						           "<td class='text-center'>"+entry.ctnum+
					               "</td>"+
					               "<td class='text-center'>"+entry.ctname+
					               "</td>"+
					               "<td class='text-center'>"+entry.pqty+
					               "</td>"+
					             
					               "<td class='td-actions text-center'>"+
					               "<button type='button' rel='tooltip' class='btn btn-default btn-sm btn-icon categoryEdit'>"+
					                   "<i class='tim-icons icon-pencil'></i>"+
					               "</button>"+
					               "<button type='button' rel='tooltip' class='btn btn-default btn-sm btn-icon categoryDelete' onClick='goDel("+entry.ctnum+")'>"+
					                   "<i class='tim-icons icon-simple-delete'></i>"+
					               "</button>"+
					  			   "</td>"+
				               "</tr>"+
				      		   "<tr class=''>"+
					      			"<td colspan='4'>"+
					      			"<div class='col-md-3 form-inline ml-auto categoryeditattach'>"+
					      				"<br/>"+
										"<input type='text' id='categoryedit"+entry.ctnum+"' name='categoryedit' class='form-control'/>&nbsp;&nbsp;"+
										"<button type='button' rel='tooltip' class='btn btn-default btn-md btn-icon' onClick='categoryEdit("+entry.ctnum+");'>수정</button>"+
					  				"</div>"+
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
	
	function addCategoryTag(){
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
	
	function categoryEdit(ctnum){
		var categoryedit = $("#categoryedit"+ctnum).val();
		
		var form_data={"ctnum":ctnum,
				       "ctname":categoryedit};
		
		var bool = confirm("해당 카테고리태그명을 변경하시겠습니까?");
		
		if(bool){
			$.ajax({
				url: "a_modifyCategoryTag.do",
				type: "POST",
				data: form_data,
				dataType: "JSON",
				success: function(json){
					if(json.result==1){
						alert("수정 성공");
						window.location.reload();
					}
					else{
						alert("수정 실패");
						window.location.reload();
					}
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});
		}
		else{
			return false;
		}
		
		
	}
	
	function goDel(ctnum){
		
		var form_data={"ctnum":ctnum};
		
		var bool = confirm("해당 카테고리태그를 삭제하시겠습니까?");
		
		if(bool){
			$.ajax({
				url: "a_deleteCategoryTag.do",
				type: "POST",
				data: form_data,
				dataType: "JSON",
				success: function(json){
					if(json.result==1){
						alert("삭제 성공");
						window.location.reload();
					}
					else{
						alert("삭제 실패");
						window.location.reload();
					}
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});
		}
		else{
			return false;
		}
		
		
	}

</script>

<div class="container"> 
	<div class="row">
         <div class="col-md-12">
           <div class="card-plain ">
             <div class="card-header">
               <h4 class="card-title">카테고리 태그 관리</h4>
             </div>
             
             <%-- 검색 버튼 --%>              
 			<div class="form-inline ml-auto text-right">
	          <div class="form-group no-border">
	            <input type="text" class="form-control" id="searchWord" placeholder="Search">
	          </div>
	          <button type="button" id="btnSearch" class="btn btn-link btn-icon btn-round">
	              <i class="tim-icons icon-zoom-split"></i>
	          </button>
	         </div>
             
             <div class="card-body">
               <div class="table-responsive">
                 <table class="table tablesorter" id="">
                   <thead class=" text-primary">
                   	<%-- 카테고리 태그 추가하기 버튼 --%>
                     <tr class="text-right" style="padding-right: 15%;  border-top-style: none;">
                   		<td colspan="4" style="border-top-style: none;">
       					<button type="button" id="categoryAdd" rel="tooltip" class="btn btn-default btn-sm btn-icon">
                   		<i class="tim-icons icon-simple-add"></i>
               			</button>
               		<%-- 추가 버튼을 눌렀을 때 뜨는 input 박스 --%>
           				<div class="col-md-3 form-inline ml-auto" id="divfileattach">
	           				<br/>
							<input type='text' id="addCategoryName" name='categoryadd' class='form-control'/>&nbsp;&nbsp;
							<button type='button' rel='tooltip' class='btn btn-default btn-md btn-icon' onClick='addCategoryTag();'>추가</button>
           				</div>
               			</td>
                     </tr>
                     
                     <tr>
                       <th class="text-center">
                         NO
                       </th>
                       <th class="text-center">
                       	  카테고리명
                       </th>
                       <th class="text-center">
                      	   수량
                       </th>
                       <th class="text-center">
                     	    관리
                       </th>
                     </tr>
                   </thead>
                   <tbody id="resultList"></tbody>
                 </table>
               </div>
             </div>
           </div>
         </div>
	</div>
</div>
<jsp:include page="admin_footer.jsp"/> 

