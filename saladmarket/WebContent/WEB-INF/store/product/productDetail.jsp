<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% String ctxPath = request.getContextPath(); %>   
<jsp:include page="../header.jsp"/>

<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.css" /> 
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/sweetalert/1.1.3/sweetalert.min.js"></script>
	

<script type="text/javascript">

	var totalprice = 0;
	
	$(document).ready(function(){
		
		$(".forspace").show();
		$(".chooseprod").hide();
		
		$.ajax({
			url:"recommdprodlist.do",
			type:"GET",
			dataType:"JSON",
			success: function(json) {
				
				var html = "";

				if(json.length > 0) {
					$.each(json, function(entryIndex, entry){
						
						html += "<div class='col-md-3 text-center'>"+
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
               			
					});
					
				}
				
				$(".recommdlist").html(html);
				
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
			
		});	

		
		$("#selectproduct").bind("change", function(){
			
			var pnum = $(this).val();
		
			$(".forspace").hide();
			$(".chooseprod").show();
			goAdd(pnum);

			
		});


	});// end of ready()--------------------------------------------------------

	
	
	function goAdd(pnum) {
		// 패키지상품에 들어있는 상품을 골랐을때 넣어주기 총금액에도 합산금액 넣어주기
		
		$.ajax({
			url:"addproduct.do",
			type:"GET",
			dataType:"JSON",
			data:{"pnum":pnum},
			success: function(json){
			
					if(json.price != -1) {

						var html = "";
						
						var price = json.price;
						var name = json.name;
						
						html +=		"<div class='form-group'>"+
									"<div class='col-md-5' style='margin-top:20px;'>"+
						            "    	<div class='selectPname'>"+
						            "			<span name='pname' id='pname' value='"+json.name+"' style='font-size: 10px;'>"+json.name+"</span>"+
						            "    	</div>"+
									"</div>"+
									"<div class='col-md-4' style='margin-top:20px;'>"+
						            "	 <input type='hidden' class='currentprice' id='currentprice' name='currentprice' value='"+json.price+"'/> "+
									"	 <input type='number' value='0' min='0' max='99' class='pqty'id='oqty'  name='oqty' style='width: 50px; height:30px;' />"+
									"	 <input type='hidden' name='pnum' id='pnum' value='"+pnum+"'/>"+
									"</div>"+
									"<div class='col-md-2' style='margin-top:20px;'>"+
						            "   <div class='selectSaleprice'>"+
						            "			<span name='price' id='price'>"+json.price.toLocaleString()+"</span>원"+
						            " 	</div>"+
									"</div>"
									"<div class='col-md-1' style='margin-top:20px;'>"+
									"	<img src='/saladmarket/store/images/xicon.png' style='vertical-align:middle;width:13px;'>test"+
									"</div>"+
									"</div>";
						
						$(".chooseprod").append(html);
						
					}
				
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		
		});
	}

	
	 $(document).on("propertychange change keyup paste input", "input[class='pqty']", function(){
		 	
		   var queryString = $("form[name=purchaseFrm]").serialize(); // 폼 안의 모든 데이터를 전송
	       // console.log(queryString);
	    	
		   $.ajax({
			   url:"getTotalPrice.do",
			   type:"GET",
			   data:queryString,
			   dataType:"JSON",
			   success: function(json){
					
				   sum = json.totalprice;
				   
				   var str_sum = sum.toLocaleString();
				   
				   $("#totalprice").empty().html(str_sum);
				   
			   },
			   error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			   }
				 
		   });
	}); 
	
	
	$(document).on("propertychange change keyup paste input", "input[class='singlePqty']", function(){
		
       var currentPqty = $(this).val();
       var saleprice = $(this).next().val();
   
       var totalprice = $("#totalprice").val();
       var html = currentPqty*saleprice;
       
       var str_html = html.toLocaleString();
       $("#totalprice").empty().html(str_html);
   
	}); 
	
	
	function goOrderForPac() {
	    event.preventDefault();

		var queryString_pac = $("form[name=purchaseFrm]").serialize(); // 폼 안의 모든 데이터를 전송
	    console.log(queryString_pac);
		
		var frm = document.purchaseFrm;
		frm.method="POST";
		frm.action="order.do";
		frm.submit();

	}
		
		
	
	function goCartForPac() {
		event.preventDefault();
		
		var queryString_pac = $("form[name=purchaseFrm]").serialize(); // 폼 안의 모든 데이터를 전송
	    console.log(queryString_pac);
	}
	
	function goLikeForPac() {

		event.preventDefault();

		var form_data = {"userid": "${ sessionScope.loginUser.userid }",
				   		 "pacnum": "${pacnum}",
				   		 "len": "${len}"}
		   
		
		   $.ajax({
			   url:"likeAdd.do",
			   type:"POST",
			   data:form_data,
			   dataType:"JSON",
			   success:function(json){
				
				   swal(json.msg);
				   
			   },
			   error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			   }
			   
		   });
	}
	
	function goOrderForSingle() {

	    event.preventDefault();

		var singlePqty = $(".singlePqty").val();
		
		var singleFrm = document.OrderFrm;
		
		singleFrm.orderPqty.value = singlePqty;
		singleFrm.sumtotalprice.value = price*singlePqty; 
		
		var queryString_p = $("form[name=OrderFrm]").serialize();
		console.log(queryString_p);
		singleFrm.method="POST";
		singleFrm.action="order.do";
		singleFrm.submit();
		
	}
		
		
	
	function goCartForSingle() {
		
		event.preventDefault();
		
		
		
		var singlePqty = $(".singlePqty").val();
		
		var singleFrm = document.OrderFrm;
		
		singleFrm.orderPqty.value = singlePqty;
		singleFrm.sumtotalprice.value = price*singlePqty; 
		
		var queryString_p = $("form[name=OrderFrm]").serialize();
		console.log(queryString_p);
		
	}
	
	function goLikeForSingle() {

		event.preventDefault();
		

		var form_data = {"userid": "${ sessionScope.loginUser.userid }",
				   		 "pnum": "${pnum}"};
		   
		   $.ajax({
			   url:"likeAdd.do",
			   type:"POST",
			   data:form_data,
			   dataType:"JSON",
			   success:function(json){
				
				   swal(json.msg);
				   
			   },
			   error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			   }
			   
		   });
	}
	
</script>	
	
	
	
	<aside id="colorlib-hero" class="breadcrumbs">
			<div class="flexslider">
				<ul class="slides">
			   	<li style="background-image: url(<%=ctxPath %>/store/images/cover-img-1.jpg);">
			   		<div class="overlay"></div>
			   		<div class="container-fluid">
			   			<div class="row">
				   			<div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
				   				<div class="slider-text-inner text-center">
				   					<h1>Product Detail</h1>
				   					<h2 class="bread"><span><a href="index.html">Home</a></span> <span><a href="shop.html">Product</a></span> <span>Product Detail</span></h2>
				   				</div>
				   			</div>
				   		</div>
			   		</div>
			   	</li>
			  	</ul>
		  	</div>
		</aside>
    
   <div class="colorlib-shop">
			<div class="container">
				<div class="row row-pb-lg">
					<div class="col-md-10 col-md-offset-1">
						<div class="product-detail-wrap">
							<div class="row">
								<div class="col-md-5">
									<div class="product-entry">
										<div class="product-img" style="background-image: url(<%=ctxPath %>/img/${ img });"> <!-- 상품 대표이미지 -->
													<!-- <p class="tag"><span class="sale">Sale</span></p> -->
										</div>
									
										<div class="thumb-nail"><!-- 상품 상세이미지 -->
											<c:forEach var="i" begin="1" end="3" items="${ imgList }">
												<a href="#" class="thumb-img" style="background-image: url(<%=ctxPath %>/img/${i});"></a>
											</c:forEach>
										</div>
									</div>
								</div>
								<div class="col-md-7">
									<div class="desc">	
		  					 		   <div class="col-md-12">
											<div class="desc">
										<h3>
											<c:if test="${ pvoList eq null }">
												${ pvo.pname }
											</c:if>
											<c:if test="${ pvo eq null }">
												${ pvoList.get(0).fk_pacname }
											</c:if>
										</h3>
										
										<table style="margin-top:10px">
											<tbody>
											  <tr>
											   <th style="width: 100px; height:50px;">판매가</th>
											   <td>
											 	 <c:if test="${ pvoList eq null }">
												   <span style="text-decoration: line-through; font-size: 9pt;"><fmt:formatNumber value="${ pvo.price }" pattern="#,###"></fmt:formatNumber> 원</span> 
											 	   <span style="font-weight: bold; font-size: 12pt;"><fmt:formatNumber value="${ pvo.saleprice }" pattern="#,###"></fmt:formatNumber></span> 원
											     </c:if>
											     <c:if test="${ pvo eq null }">
												   <span style="text-decoration: line-through; font-size: 9pt;"><fmt:formatNumber value="${ pvoList.get(0).price }" pattern="#,###"></fmt:formatNumber> 원</span> 
											 	   <span style="font-weight: bold; font-size: 12pt;"><fmt:formatNumber value="${ pvoList.get(0).saleprice }" pattern="#,###"></fmt:formatNumber></span> 원
											     </c:if>
											   </td>
											   </tr>
												<tr>
												<th style="width: 100px; height:50px;">중량/용량</th>
												<td>
												<c:if test="${ pvoList eq null }">
													${ pvo.weight }
												</c:if>
												<c:if test="${ pvo eq null }">
													${ pvoList.get(0).weight }
												</c:if> g(그램)/ml(밀리리터)
												</td>
											   </tr>
											   <tr>
											   <th style="width: 100px; height:50px;">포장타입</th>
												<td>냉장/에코포장
												<span>택배배송은 에코포장이 스티로폼으로 대체됩니다.</span>
												</td>
												</tr>	
												<tr>
											<th style="width: 100px; height:50px;">알레르기 정보</th>
											<td>
											<c:if test="${ pvoList eq null }">
												${ pvo.allergy }
											</c:if>
											<c:if test="${ pvo eq null }">
												${ pvoList.get(0).allergy }
											</c:if>
											</td>
											
											<tr>
												<th style="width: 100px; height:50px;">유통기한</th>
												<td >
												<c:if test="${ pvoList eq null }">
													${ pvo.pexpiredate }
												</c:if>
												<c:if test="${ pvo eq null }">
													${ pvoList.get(0).pexpiredate }
												</c:if>
												</td>
											</tr>
											
											<c:if test="${ pvoList eq null }">
												<tr>
												<th style="width: 100px; height:50px;"> 구매 수량 </th>
												<td> 
													<input type="number" class="singlePqty" value="1" min="1" max="100" style="width: 80px; height: 32px;"/>
													<input type="hidden" value="${ pvo.saleprice }" />
												</td>
												</tr>
											</c:if>
											<c:if test="${ pvo eq null }">
												<tr>
												<th style="width: 100px; height:50px;"> 상품 선택</th>
												<td>
												<select id="selectproduct" style="width: 280px; height: 32px;" label="단품골라담기">
													<option value="-1">&nbsp;&nbsp;&nbsp;&nbsp;==단품골라담기 ==</option>
													<c:forEach items="${ pvoList }" var="i">
														<option value="${ i.pnum }">${ i.pname }</option>
													</c:forEach>
												</select>
												</td>
											    </tr>
											</c:if>
											
										 <c:if test="${ pvo eq null }"> 
										 <tr>
											 <th style="width: 100px; height:50px;">총 금액 </th>
											 <td><span id="totalprice">0</span>원</td>
										 </tr>
										 </c:if>
										 <c:if test="${ pvoList eq null }"> 
										 <tr>
											 <th style="width: 100px; height:50px;">총 금액 </th>
											 <td><span id="totalprice"><fmt:formatNumber value="${ pvo.saleprice }" pattern="#,###"></fmt:formatNumber></span>원</td>
										 </tr>
										 </c:if>
										 
										</tbody>
									</table>
										
									<c:if test="${ pvo eq null }">
									<form name="purchaseFrm"> 
										<div class="row row-pb-sm forspace" style="margin-top:20px; margin-bottom:20px; ">
											
										</div>
										<div class="row row-pb-sm chooseprod" style="margin-top:20px; margin-bottom:20px; border: 1px dotted #d9d9d9;">
											
										</div>
									</form>
									</c:if>
									<c:if test="${ pvoList eq null }">
										<div class="row row-pb-sm">
										</div>
									</c:if>
									
									
									<c:if test="${ pvo eq null }">
									
										<div class="col-md-4">
								 			<button onclick="goCartForPac();" class="btn btn-primary btnsubmit" > 장바구니 담기</button>
										</div>
										<div class="col-md-4">
											<button onclick="goOrderForPac();" class="btn btn-primary btnsubmit"> 바로 주문하기 </button>
										</div>
										<div class="col-md-4">
											<button onclick="goLikeForPac();" class="btn btn-primary btnsubmit">좋아요</button>
										</div>
										
									</c:if>
									<c:if test="${ pvoList eq null }">
									
										<div class="col-md-4">
								 			<button onclick="goCartForSingle();" class="btn btn-primary btnsubmit" > 장바구니 담기</button>
										</div>
										<div class="col-md-4">
											<button onclick="goOrderForSingle();" class="btn btn-primary btnsubmit"> 바로 주문하기 </button>
										</div>
										<div class="col-md-4">
											<button onclick="goLikeForSingle();" class="btn btn-primary btnsubmit">좋아요</button>
										</div>
										
									</c:if>
									
									
									</div>
									
								</div>
									
							 
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10 col-md-offset-1">
						<div class="row">
							<div class="col-md-12 tabulation">
								<ul class="nav nav-tabs">
									<li class="active"><a data-toggle="tab" href="#description">상세 설명</a></li>
									<li><a data-toggle="tab" href="#review">리뷰</a></li>
									<li><a data-toggle="tab" href="#qna">문의</a></li>
									<%-- --%>
								</ul>
								<div class="tab-content"> <!-- 상품 상세 설명 내용  -->
									<div id="description" class="tab-pane fade in active" align="center">
									<c:if test="${ pvo eq null }">
									<c:forEach var="pvo" items="${ pvoList }" varStatus="status">
										<c:if test="${status.first}">
											<p style="margin-top: 3%; margin-bottom: 10%;">${ pvo.paccontents }</p>
										</c:if>
										<c:set var="imgnum" value="${ status.index }"></c:set>
										<i class="icon-heart" aria-hidden="true"></i><h3>&nbsp;${ pvo.pname }<br/></h3>
										<c:forEach var="i" items="${ imgList }" begin="${ imgnum }" end="${ imgnum }">
										<img src="/saladmarket/img/${i}" style="width: 700px; height: 500px; margin-top: 3%; margin-bottom: 8%;" />
										</c:forEach>
										<p style="margin-bottom: 30%;">${ pvo.pcontents }</p>
									</c:forEach>
									</c:if>
									<c:if test="${ pvoList eq null }">
										<c:forEach var="i" items="${ imgList }">
											<img src="/saladmarket/img/${i}" style="width: 700px; height: 500px; margin-top: 3%; margin-bottom: 8%;" />	
										</c:forEach>
										<p style="margin-bottom: 30%;">${ pvo.pcontents }</p>
									</c:if>
						         </div>
						         <!--  리뷰  -->
								   <div id="review" class="tab-pane fade">
								   	<div class="row" style="margin-left: 5%;">
								   		<div class="col-md-11">
								   			<h3>23 Reviews</h3>
								   			<div class="review"> <!--background-image: 리뷰상품 이미지가 들어갑니다.  -->
										   		<div class="user-img" style="background-image: url(<%=ctxPath %>/store/images/person3.jpg);"></div>
										   		<div class="desc">
										   			<h4>
										   				<span class="text-left">Jacob Webb</span>
										   				<span class="text-right">14 March 2018</span>
										   			</h4>
										   			<p class="star">
										   				<span>
										   					<i class="icon-star-full"></i>
										   					<i class="icon-star-full"></i>
										   					<i class="icon-star-full"></i>
										   					<i class="icon-star-half"></i>
										   					<i class="icon-star-empty"></i>
									   					</span>
									   					<span class="text-right"><a href="#" class="reply"><i class="icon-reply"></i></a></span>
										   			</p>
										   			<p>When she reached the first hills of the Italic Mountains, she had a last view back on the skyline of her hometown Bookmarksgrov</p>
										   		</div>
										   	</div>
										   	<div class="review">
										   		<div class="user-img" style="background-image: url(<%=ctxPath %>/store/images/person2.jpg);"></div>
										   		<div class="desc">
										   			<h4>
										   				<span class="text-left">Jacob Webb</span>
										   				<span class="text-right">14 March 2018</span>
										   			</h4>
										   			<p class="star">
										   				<span>
										   					<i class="icon-star-full"></i>
										   					<i class="icon-star-full"></i>
										   					<i class="icon-star-full"></i>
										   					<i class="icon-star-half"></i>
										   					<i class="icon-star-empty"></i>
									   					</span>
									   					<span class="text-right"><a href="#" class="reply"><i class="icon-reply"></i></a></span>
										   			</p>
										   			<p>When she reached the first hills of the Italic Mountains, she had a last view back on the skyline of her hometown Bookmarksgrov</p>
										   		</div>
										   	</div>
										   	<div class="review">
										   		<div class="user-img" style="background-image: url(<%=ctxPath %>/store/images/person1.jpg);"></div>
										   		<div class="desc">
										   			<h4>
										   				<span class="text-left">Jacob Webb</span>
										   				<span class="text-right">14 March 2018</span>
										   			</h4>
										   			<p class="star">
										   				<span>
										   					<i class="icon-star-full"></i>
										   					<i class="icon-star-full"></i>
										   					<i class="icon-star-full"></i>
										   					<i class="icon-star-half"></i>
										   					<i class="icon-star-empty"></i>
									   					</span>
									   					<span class="text-right"><a href="#" class="reply"><i class="icon-reply"></i></a></span>
										   			</p>
										   			<p>When she reached the first hills of the Italic Mountains, she had a last view back on the skyline of her hometown Bookmarksgrov</p>
										   		</div>
										   	</div>
								   		</div>
								   		
								   	</div>
								   </div>
					         	   <div id="qna" class="tab-pane fade">
					         	   	<p>Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar.</p>
										<p>When she reached the first hills of the Italic Mountains, she had a last view back on the skyline of her hometown Bookmarksgrove, the headline of Alphabet Village and the subline of her own road, the Line Lane. Pityful a rethoric question ran over her cheek, then she continued her way.</p>
								      
					         	   </div>
					         </div>
				         </div>
						</div>
					</div>
				</div>
			</div>
		</div>
 <!--  추천 상품이 들어 갑니다.  -->
		<div class="colorlib-shop">
			<div class="container">
				<div class="row">
					<div class="col-md-6 col-md-offset-3 text-center colorlib-heading">
						<h2><span>RECOMMENDATIONS</span></h2>
						<p>다른 고객님들이 많이 구매하신 제품을 추천해드립니다.</p>
					</div>
				</div>
				<div class="row recommdlist">
					
					
				</div>
			</div>
		</div>

	<form name="OrderFrm">
		<c:if test="${ pvoList eq null }">				
				<input type="hidden" name="orderPnum" value="${ pvo.pnum }" />
				<input type="hidden" name="orderPqty" />
				<input type="hidden" name="saleprice" value="${ pvo.saleprice }" />
				<input type="hidden" name="sumtotalprice" /> <%-- 바로주문하기에 사용됨 --%>
		</c:if>
	</form>

	<div class="gototop js-top">
		<a href="#" class="js-gotop"><i class="icon-arrow-up2"></i></a>
	</div>
	

<jsp:include page="../footer.jsp"/>