<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String CtxPath = request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../header.jsp" />

<script>

   $(document).ready(function(){
	   showCart();
	   
   }); // $(document).ready()

   
   
   function showCart(){
	   
	   $.ajax({
	          url:"ajaxShowCart.do",
	          type:"GET",
	          dataType:"JSON",
	          success:function(json){
	        	  var html = "";
	            
            	  if (json.length > 0) {
            		  $.each(json, function(entryIndex, entry){
	            	  	html += " <div class='product-cart'> "+
								"<div class='one-forth'>"+
									" <div class='product-img' style='background-image: url(saladmarket/img/"+entry.titleimg+"););'>"+
								"	</div>"+
								"	<div class='display-tc'>"+
								"		<h3>"+entry.pacname+"<br>-"+entry.pname+"</h3>"+
								"	</div>"+
								"</div>"+
								"<div class='one-eight text-center'>"+
								"	<div class='display-tc'>"+
								"		<span style='text-decoration: line-through;'>"+entry.price+"원</span> -> "+
								"		<span class='price'>"+entry.saleprice+"원</span>"+
								"	</div>"+
								"</div>"+
								"<div class=\"one-eight text-center\">"+
								"	<div class=\"display-tc\">"+
								"		<input type=\"number\" id=\"quantity\" name=\"quantity\" class=\"form-control input-number text-center\" value=\""+entry.oqty+"\" min=\"1\" max=\"100\">"+
								"	</div>"+
								"</div>"+
								"<div class=\"one-eight text-center\">"+
								"	<div class=\"display-tc\">"+
								"		<span class=\"price\">6900원</span>"+
								"	</div>"+
								"</div>"+
								"<div class=\"one-eight text-center\">"+
								"	<div class=\"display-tc\">"+
								"		<a href=\"#\" class=\"closed\"></a>"+
								"	</div>"+
								"</div>"+
							"</div>";
            		  }); 
            	  }
            	  else {
            		  html += "상품이 없습니다.";
            	  }
            	  $("#result").empty().html(html);
	            	  
	          },
	          error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	         }
	       });
	   
   } // showCart
   
   
</script>



	<aside id="colorlib-hero" class="breadcrumbs">
			<div class="flexslider">
				<ul class="slides">
			   	<li style="background-image: url(<%=CtxPath %>/store/images/PFPI-WEBSITE-SLIDERS-1.png);">
			   		<div class="overlay"></div>
			   		<div class="container-fluid">
			   			<div class="row">
				   			<div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
				   				<div class="slider-text-inner text-center">
				   					<h1>Shopping Cart</h1>
				   					<h2 class="bread"><span><a href="index.html">Home</a></span>
				   					<span><a href="shop.html">상품</a></span>
				   					<span>장바구니</span></h2>
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
				<div class="row row-pb-md">
					<div class="col-md-10 col-md-offset-1">
						<div class="process-wrap">
							<div class="process text-center active">
								<p><span>01</span></p>
								<h3>장바구니</h3>
							</div>
							<div class="process text-center">
								<p><span>02</span></p>
								<h3>주문하기</h3>
							</div>
							<div class="process text-center">
								<p><span>03</span></p>
								<h3>주문완료</h3>
							</div>
						</div>
					</div>
				</div>
				
				
				<div class="row row-pb-md">
					<div class="col-md-10 col-md-offset-1">
						<div class="product-name">
							<div class="one-forth text-center">
								<span>상품</span>
							</div>
							<div class="one-eight text-center">
								<span>가격</span>
							</div>
							<div class="one-eight text-center">
								<span>수량</span>
							</div>
							<div class="one-eight text-center">
								<span>총액</span>
							</div>
							<div class="one-eight text-center">
								<span>삭제</span>
							</div>
						</div><%-- 장바구니 List 윗부분 --%>
						
					
						<div id="result">
						</div>
						
						
					</div>
				</div>
				
				
				<div class="row">
					<div class="col-md-10 col-md-offset-1">
						<div class="total-wrap">
							<div class="row">
								<div class="col-md-8">
									<form action="#">
										<div class="row form-group">
											<div class="col-md-5">
												<div class="form-group">
													<span style="float: left; padding-top: 10px;">적용할 쿠폰 :</span>
													<input type="text" id="cupon" class="form-control" placeholder="적용할 쿠폰" style="width: 60%; float: left; margin-left: 10px;">
												</div>
											</div>
											<div class="col-md-4">
												<input type="button" value="적용할 쿠폰" class="btn">
											</div>
											
											<div class="col-md-3">
												<input type="submit" value="주문하기" class="btn btn-primary">
											</div>
										</div>
									</form>
								</div>
								<div class="col-md-3 col-md-push-1 text-center">
									<div class="total">
										<div class="sub">
											<p><span>총상품금액:</span> <span>10,000원</span></p>
											<p><span>배송비:</span> <span>5,000원</span></p>
											<p><span>할인:</span> <span>10%</span></p>
										</div>
										<div class="grand-total">
											<p><span><strong>결제금액:</strong></span> <span>14,000원</span></p>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	
<jsp:include page="../footer.jsp" />