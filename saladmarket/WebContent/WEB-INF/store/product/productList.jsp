<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String CtxPath = request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../header.jsp"/>

<style>
.tab button {
       background-color: inherit;
       float: center;
       border: none;
       outline: none;
       cursor: pointer;
       padding: 14px 16px;
       transition: 0.3s;
       font-size: 17px;
   }
   
   /* Change background color of buttons on hover */
   .tab button:hover {
       /*background-color: #ddd;*/
       border-bottom: 5px solid #FFC300;
   }
   
   /* Create an active/current tablink class */
   .tab button.active {
        border-bottom: 5px solid #FFC300;
   }
   
   /* Style the tab content */
   .tabcontent {
       display: none;
       padding: 6px 12px;
       border: 0px solid #ccc;
       border-top: none;
   }


</style>

<script>


	$(document).ready(function(){
		
		
		var fk_ldname = "${fk_ldname}";
		var sdname = "${sdname}";
		var word = $("#searchWord").val();

		goSearch(fk_ldname, sdname, "", "1", "");      // 초기치 설정
		
		$("#HIT").hide();
		$("#NEW").hide();
	
		getList(fk_ldname,sdname,"","");
		
		$("#submit").click(function() {
	    	
			goSearchbyword();
			
	    });
		
		$("#searchWord").keydown(function(event){
			if(event.KeyCode == 13) {

				goSearchbyword();
				
			}
			
		});

		
		$(".BEST").hover(function(){
			$("#BEST").show();
			$("#HIT").hide();
			$("#NEW").hide();
			
		},function(){
			
			
		});
		
		$(".HIT").hover(function(){
			$("#HIT").show();
			$("#BEST").hide();
			$("#NEW").hide();
			
			// getList(fk_ldname,sdname,"HIT", "");
			
		},function(){
			
		});
		
		$(".NEW").hover(function(){
			$("#NEW").show();
			$("#HIT").hide();
			$("#BEST").hide();
			
			// getList(fk_ldname,sdname,"NEW","");
			
		},function(){
			
		});
	});


	function goSearchbyword() {
		
		event.preventDefault();
		
		var searchWord = $("#searchWord").val();
		var fk_ldname = "${fk_ldname}";
		var sdname = "${sdname}";
		
    	goSearch(fk_ldname, sdname, searchWord, "1", "");

		$("#searchWord").val("");
    	
	}
	
	
	
	function goSearch(fk_ldname, sdname, word, pageNo, orderby) {
		
		var form_data = {fk_ldname:fk_ldname,
						 sdname:sdname,                // 키:밸류
				         searchword:word,            // 키:밸류
				         currentShowPageNo:pageNo,
				         orderby:orderby};  // 키:밸류
		
		$.ajax({
				url: "wordSearchResult.do",
				type: "GET",
				data: form_data,  // 위의 URL 페이지로 사용자가 보내는 ajax 요청 데이터.
				dataType: "JSON", // ajax 요청에 의해 URL 페이지 서버로 부터 리턴받는 데이터 타입. xml, json, html, script, text 이 있음.
				success: function(data){
										
					if(data.length > 0) { // 검색된 데이터가 있는 경우임. 만약에 조회된 데이터가 없을 경우 if(data == null) 이 아니고 if(data.length == 0) 이라고 써야 한다. 
						                  // 왜냐하면  넘겨준 값이 new JSONArray() 이므로 null 이 아니기 때문이다..
					     var resultHTML = "";
					
						 $.each(data, function(entryIndex, entry){
							
							 resultHTML += "<div class='col-md-3 text-center'>"+
		                  		"<div class='product-entry'>"+
		                  		"<div class='product-img' style='background-image: url(/saladmarket/img/"+entry.pimgfilename+");'>"+
		                  		"<p class='tag'><span class='sale'>"+entry.stname+"</span></p>"+
		                  		"<div class='cart'>"+
		                  		"<p>"+
		                  		"<span class='addtocart'><a href='jumun.do'><i class='icon-shopping-cart'></i></a></span>"+
		                  		"<span><a href='productDetail.do?pacnum="+entry.pacnum+"&pnum="+entry.pnum+"&img="+entry.pimgfilename+"'><i class='icon-eye'></i></a></span>"+ 
		                        "<span><a href='#'><i class='icon-heart3'></i></a></span>"+
		                        "<span><a href='like.do'><i class='icon-bar-chart'></i></a></span>"+
		                        "</p>"+
		                        "</div>"+
		                        "</div>"+
		                     	"<div class='desc'>"+
		                        "<h3><a href='productDetail.do?pacnum="+entry.pacnum+"&pnum="+entry.pnum+"'>"+entry.pacname+
		                        "</a></h3>"+
		                        "<p class='price'><span>"+Number(entry.saleprice).toLocaleString()+"원</span></p>"+
		                     	"</div>"+
		                  		"</div>"+
		               			"</div>";
		               			
						 });// end of $.each(data, function(entryIndex, entry){ })-------------------
						 	
						 $(".result").empty().html(resultHTML);
						 
						 makePageBar(fk_ldname, sdname, word, pageNo, orderby);
					}
					else { // 검색된 데이터가 없는 경우
						 $(".result").empty();
					}

				},// end of success: function()------
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});// end of $.ajax()------------------------
		
	}// end of goSearch(pageNo)-----------------------------

	function getList(fk_ldname, sdname, orderby) {
		
		var form_data = {fk_ldname:fk_ldname, sdname:sdname, orderby:orderby};

		$.ajax({
			url:"detailproductList.do",
			type:"GET",
			data:form_data,
			dataType:"JSON",
			success: function(json){

				var hithtml = "", newhtml = "", besthtml = "", totalhtml = "";
				
				if(json.length > 0) {
					$.each(json, function(entryIndex, entry){
						
						if(entry.stname == "HIT") {
							hithtml += "<div class='col-md-3 text-center'>"+
	                  		"<div class='product-entry'>"+
	                  		"<div class='product-img' style='background-image: url(/saladmarket/img/"+entry.pimgfilename+");'>"+
	                  		"<p class='tag'><span class='sale'>"+entry.stname+"</span></p>"+
	                  		"<div class='cart'>"+
	                  		"<p>"+
	                  		"<span class='addtocart'><a href='jumun.do'><i class='icon-shopping-cart'></i></a></span>"+
	                  		"<span><a href='productDetail.do?pacnum="+entry.pacnum+"&pnum="+entry.pnum+"&img="+entry.pimgfilename+"'><i class='icon-eye'></i></a></span>"+ 
	                        "<span><a href='#'><i class='icon-heart3'></i></a></span>"+
	                        "<span><a href='like.do'><i class='icon-bar-chart'></i></a></span>"+
	                        "</p>"+
	                        "</div>"+
	                        "</div>"+
	                     	"<div class='desc'>"+
	                        "<h3><a href='productDetail.do?pacnum="+entry.pacnum+"&pnum="+entry.pnum+"'>"+entry.pacname+
	                        "</a></h3>"+
	                        "<p class='price'><span>"+Number(entry.saleprice).toLocaleString()+"원</span></p>"+
	                     	"</div>"+
	                  		"</div>"+
	               			"</div>";

						}
						else if(entry.stname == "BEST") {
							besthtml += "<div class='col-md-3 text-center'>"+
	                  		"<div class='product-entry'>"+
	                  		"<div class='product-img' style='background-image: url(/saladmarket/img/"+entry.pimgfilename+");'>"+
	                  		"<p class='tag'><span class='sale'>"+entry.stname+"</span></p>"+
	                  		"<div class='cart'>"+
	                  		"<p>"+
	                  		"<span class='addtocart'><a href='jumun.do'><i class='icon-shopping-cart'></i></a></span>"+
	                  		"<span><a href='productDetail.do?pacnum="+entry.pacnum+"&pnum="+entry.pnum+"&img="+entry.pimgfilename+"'><i class='icon-eye'></i></a></span>"+ 
	                        "<span><a href='#'><i class='icon-heart3'></i></a></span>"+
	                        "<span><a href='like.do'><i class='icon-bar-chart'></i></a></span>"+
	                        "</p>"+
	                        "</div>"+
	                        "</div>"+
	                     	"<div class='desc'>"+
	                        "<h3><a href='productDetail.do?pacnum="+entry.pacnum+"&pnum="+entry.pnum+"'>"+entry.pacname+
	                        "</a></h3>"+
	                        "<p class='price'><span>"+Number(entry.saleprice).toLocaleString()+"원</span></p>"+
	                     	"</div>"+
	                  		"</div>"+
	               			"</div>";

						}
						else {
							newhtml += "<div class='col-md-3 text-center'>"+
	                  		"<div class='product-entry'>"+
	                  		"<div class='product-img' style='background-image: url(/saladmarket/img/"+entry.pimgfilename+");'>"+
	                  		"<p class='tag'><span class='sale'>"+entry.stname+"</span></p>"+
	                  		"<div class='cart'>"+
	                  		"<p>"+
	                  		"<span class='addtocart'><a href='jumun.do'><i class='icon-shopping-cart'></i></a></span>"+
	                  		"<span><a href='productDetail.do?pacnum="+entry.pacnum+"&pnum="+entry.pnum+"&img="+entry.pimgfilename+"'><i class='icon-eye'></i></a></span>"+ 
	                        "<span><a href='#'><i class='icon-heart3'></i></a></span>"+
	                        "<span><a href='like.do'><i class='icon-bar-chart'></i></a></span>"+
	                        "</p>"+
	                        "</div>"+
	                        "</div>"+
	                     	"<div class='desc'>"+
	                        "<h3><a href='productDetail.do?pacnum="+entry.pacnum+"&pnum="+entry.pnum+"'>"+entry.pacname+
	                        "</a></h3>"+
	                        "<p class='price'><span>"+Number(entry.saleprice).toLocaleString()+"원</span></p>"+
	                     	"</div>"+
	                  		"</div>"+
	               			"</div>"; 
						}
						/* 
						totalhtml += "<div class='col-md-3 text-center'>"+
                  		"<div class='product-entry'>"+
                  		"<div class='product-img' style='background-image: url(/saladmarket/img/"+entry.pimgfilename+");'>"+
                  		"<p class='tag'><span class='sale'>"+entry.stname+"</span></p>"+
                  		"<div class='cart'>"+
                  		"<p>"+
                  		"<span class='addtocart'><a href='jumun.do'><i class='icon-shopping-cart'></i></a></span>"+
                  		"<span><a href='productDetail.do?pacnum="+entry.pacnum+"&pnum="+entry.pnum+"&img="+entry.pimgfilename+"'><i class='icon-eye'></i></a></span>"+ 
                        "<span><a href='#'><i class='icon-heart3'></i></a></span>"+
                        "<span><a href='like.do'><i class='icon-bar-chart'></i></a></span>"+
                        "</p>"+
                        "</div>"+
                        "</div>"+
                     	"<div class='desc'>"+
                        "<h3><a href='productDetail.do?pacnum="+entry.pacnum+"&pnum="+entry.pnum+"'>"+entry.pacname+
                        "</a></h3>"+
                        "<p class='price'><span>"+Number(entry.saleprice).toLocaleString()+"원</span></p>"+
                     	"</div>"+
                  		"</div>"+
               			"</div>"; 
 */						
					});

					$(".hitdata").empty().html(hithtml);
					$(".bestdata").empty().html(besthtml);
					$(".newdata").empty().html(newhtml);
					/* $(".result").empty().html(totalhtml); */

				}
				
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
			
		});
		
	}

	
	function makePageBar(fk_ldname, sdname, word, currentShowPageNo, orderby) {
		
		var form_data = {fk_ldname:fk_ldname,
						 sdname:sdname,     // 키:밸류
				         searchword:word,   // 키:밸류
				         sizePerPage:"4",
				         orderby: orderby};  // 키:밸류
		
		$.ajax({
				url: "pwordSearchPageBar.do",
				type: "GET",
				data: form_data,  // 위의 URL 페이지로 사용자가 보내는 ajax 요청 데이터.
				dataType: "JSON", // ajax 요청에 의해 URL 페이지 서버로 부터 리턴받는 데이터 타입. xml, json, html, script, text 이 있음.
				success: function(json){
										
					if(json.totalPage != 0) { 
						
					     var totalPage = json.totalPage;
					     var pageBarHTML = "";
						 
					     /////////////////////////
					     
					     var blockSize = 3;
					     // blockSize 은 1개 블럭(토막)당 보여지는 페이지번호의 갯수이다.
					     /*
					         1 2 3   -- 1개블럭
					         4 5 6   -- 1개블럭
					         7 8 9   -- 1개블럭
					     */
	      
					     var loop = 1;
					      /* 
					         loop 는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 갯수(지금은 3개)까지만 
					                증가하는 용도이다.
					      */
	                     // 자바스크립트에서는 1/3 이 0 이 아니라 0.33333 이므로 소수점을 버리기 위해 Math.floor(0.33333) 을 하면 소수점을 버린 정수만 나온다.
	                     var pageNo = Math.floor((currentShowPageNo - 1)/blockSize) * blockSize + 1; 
					     //**** !!! 공식이다. !!! ****//
					      
					     /*
					         1 2 3   -- 첫번째 블럭의 페이지번호 시작값(pageNo)은 1 이다. 
					         4 5 6   -- 두번째 블럭의 페이지번호 시작값(pageNo)은 4 이다.
					         7 8 9   -- 세번째 블럭의 페이지번호 시작값(pageNo)은 4 이다.
					         
					          currentShowPageNo    pageNo
					         -----------------------------
					                1                1  == ((1 - 1)/3) * 3 + 1
					                2                1  == ((2 - 1)/3) * 3 + 1  
					                3                1  == ((3 - 1)/3) * 3 + 1 
					               
					                4                4  == ((4 - 1)/3) * 3 + 1 
					                5                4  == ((5 - 1)/3) * 3 + 1 
					                6                4  == ((6 - 1)/3) * 3 + 1
					                
					                7                7  == ((7 - 1)/3) * 3 + 1 
					                8                7  == ((8 - 1)/3) * 3 + 1 
					                9                7  == ((9 - 1)/3) * 3 + 1    
					      */
    	                	
/* 					     var orderbyby = $("#orderbyname").val();
					     

					      console.log(orderbyby);
					      
					      */
						 // *** [이전] 만들기 *** //
					      if(pageNo != 1) {
					    	  pageBarHTML += "<a href='javascript:goSearch(\""+fk_ldname+"\" ,\""+sdname+"\" , \""+word+"\" , \""+(pageNo-1)+"\", \""+orderby+"\")'>[이전]</a>";
					      }
					     
	                     /////////////////////////////////////////////////
					      while( !(loop > blockSize || pageNo > totalPage) ) {
					       	 
					    	  if(pageNo == currentShowPageNo) {
					    		  pageBarHTML += "&nbsp;<span style=\"color: red; font-size: 12pt; font-weight: bold; text-decoration: underline; \">"+pageNo+"</span>&nbsp;";
					    	  }
					    	  else {
					    	  	  pageBarHTML += "&nbsp;<a href='javascript:goSearch(\""+fk_ldname+"\" ,\""+sdname+"\" , \""+word+"\" , \""+pageNo+"\", \""+orderby+"\")'>"+pageNo+"</a>&nbsp;";
					     	  }
	                     
					       	 loop++;
					    	 pageNo++;
					    	 
					      } // end of while-----------------------------------
                         /////////////////////////////////////////////////

					  	  // *** [다음] 만들기 *** //
					     if( !(pageNo > totalPage) ) {
					    	 pageBarHTML += "&nbsp;<a href='javascript:goSearch(\""+fk_ldname+"\" ,\""+sdname+"\" , \""+word+"\" , \""+pageNo+"\", \""+orderby+"\")'>[다음]</a>";
					     }
						 
					     $("#pageBar").empty().html(pageBarHTML);
					     
					     pageBarHTML = "";
					}
					
					else { // 검색된 데이터가 없는 경우
						 $("#pageBar").empty();
					}

				},// end of success: function()------
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});// end of $.ajax()-------------------
		
	}// end of makePageBar(startPageNo)--------------------------

	
	function orderby(orderby) {
		
		var fk_ldname = "${fk_ldname}";
		var sdname = "${sdname}";
		var word = $("#searchWord").val();
		
		$("#orderbyname").val(orderby);
		
		goSearch(fk_ldname, sdname, word, "1", orderby);
		
	}// end of function orderby()----------------------------------
	
	
	

   

</script>

<aside id="colorlib-hero" class="breadcrumbs">
   <div class="flexslider">
      <ul class="slides">
         <li style="background-image: url(<%=CtxPath %>/store/images/cover-img-1.jpg);">
            <div class="overlay"></div>
            <div class="container-fluid">
               <div class="row">
                  <div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
                     <div class="slider-text-inner text-center">
                        <h1>Shop</h1>
                        <h2 class="bread">
                           <span style="font-size: 13pt;"><a href="<%=CtxPath %>/index.do">Home</a></span>
                           <span style="font-size: 13pt;">ProductList</span>
                        </h2>
                     </div>
                  </div>
               </div>
            </div>
         </li>
        </ul>
     </div>
</aside>

<div style="overflow: hidden; width: 100%; border-bottom: 0px solid #b7b7b7;">



<%-- 스펙태그 시작 --%>
   <div align="center" style=" margin-top: 50px;">
      <div style="width: 70%;">
			<div class="tab" style="margin-bottom: 50px;"><!-- 각 태그별 상품 이미지는 8개씩!!!! -->
		        <button class="tablinks BEST" >BEST</button>
		        <button class="tablinks HIT" >HIT</button>
		        <button class="tablinks NEW" >NEW</button>
			</div>
      
      
			<div id="HIT" class="tabcontent" style="display: block;">
				<div id="page">
					<div class="colorlib-shop">
						<div class="row hitdata">
						
      					</div>
      				</div>
      			</div>
      		</div>
      		
      		
			<div id="NEW" class="tabcontent" style="display: block;">
				<div id="page">
					<div class="colorlib-shop">
						<div class="row newdata">
						
      					</div>
      				</div>
      			</div>
      		</div>
      
      
			<div id="BEST" class="tabcontent" style="display: block;">
				<div id="page">
					<div class="colorlib-shop">
						<div class="row bestdata">

            			</div>
         			</div>
      			</div>
   			</div><!-- Best Product -->
		</div>
	</div> <%-- 스펙태그  끝 --%>
</div> <!-- horizontal -->
<!-- Navbar Area-->


<div class="classy-nav-container breakpoint-off" >
   <div class="container">
      <!-- Menu -->
      <nav class="classy-navbar" id="foodeNav">
         <!-- Navbar Toggler -->
         <div class="classy-navbar-toggler">
            <span class="navbarToggler"><span></span><span></span><span></span></span>
         </div>
      
         <!-- Nav Start -->
         <div class="classynav">
            <div class="col-md-offset-3 col-md-6 search-form">
               <form action="#" method="get">
                  <ul style="border: 0px solid gray;">
                     <li style="float: center; ">
                        <input type="search" id="searchWord" placeholder="검색할 상품명을 입력하세요" style="border: 2px solid #b7b7b7; border-radius: 0; width: 80%; height: 50px; font-size: 15px; position: relative; top: 30%;" >
                        <button type="submit" value="Submit" id="submit" style="height: 50px; border: none;" >
                           <img src="<%=CtxPath%>/store/images/search.png" style="width:20px; height:20px;"alt="">
                        </button>
                     </li>
                  </ul>
               </form>
               
            </div>
            <ul>
               <li style="float: right;">
                  <div class="dropdown">
                     <button class="btn dropdown-toggle" type="button" data-toggle="dropdown" style="border-radius: 0; height: 50px; background-color: #FFC300;">정렬<span class="caret"></span></button>
                     <ul class="dropdown-menu">
                        <li id="name" onclick="orderby('pacname');"><a>이름순</a></li>
                        <li id="like" onclick="orderby('plike');"><a>인기순</a></li>
                        <li id="new" onclick="orderby('pdate');"><a>신상품순</a></li>
                        <li id="price" onclick="orderby('saleprice');"><a>가격낮은순</a></li>
                     </ul>
                     <input type="hidden" id="orderbyname"/>
                  </div>
               </li>
            </ul>
         </div>
      </nav>
   </div>
</div><!-- 검색창 -->



<!-- 상품 List  -->
<div align="center" class=" col-md-offset-2 col-md-8" style="margin-top: 50px; display: block;">
    <div id="page">
         <div class="colorlib-shop">
            <div class="row result">
            
            </div>
            <div id="pageBar"></div>
         </div>
      </div>
</div>
<!--   -->
   
<jsp:include page="../footer.jsp"/>