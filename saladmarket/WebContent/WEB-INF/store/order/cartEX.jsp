<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% String CtxPath = request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../header.jsp" />

<script>

   $(document).ready(function(){

	   
   }); // $(document).ready() ////////////////////////////////////////////////

   function goOrder() {
	   
	   var frm = document.ordFrm;
		frm.method = "POST";
		frm.action = "order.do";
		frm.submit();
		
   }
   
   
</script>



<aside id="colorlib-hero" class="breadcrumbs">
	<div class="flexslider">
		<ul class="slides">
			<li style="background-image: url(<%=CtxPath %>/store/images/cover-img-1.jpg);">
				<div class="overlay"></div>
				<div class="container-fluid">
					<div class="row">
						<div
							class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
							<div class="slider-text-inner text-center">
								<h1>Shopping Cart</h1>
								<h2 class="bread">
									<span><a href="index.html">Home</a></span> <span><a
										href="shop.html">상품</a></span> <span>장바구니</span>
								</h2>
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
		<form name="ordFrm">
			<div class="row row-pb-md">
				<div class="col-md-10 col-md-offset-1">
					<div class="process-wrap">
						<div class="process text-center active">
							<p>
								<span>01</span>
							</p>
							<h3>장바구니</h3>
						</div>
						<div class="process text-center">
							<p>
								<span>02</span>
							</p>
							<h3>주문하기</h3>
						</div>
						<div class="process text-center">
							<p>
								<span>03</span>
							</p>
							<h3>주문완료</h3>
						</div>
					</div>
				</div>
			</div>
	
			<div class="row row-pb-md">
				<div class="col-md-10 col-md-offset-1">
					<table class="table">
						<tr>
							<th><input type="checkbox"></th>
							<th>상품</th>
							<th>가격</th>
							<th>수량</th>
							<th>총액</th>
							<th>삭제</th>
						</tr><!-- 장바구니 List 윗부분 -->
						
						<c:if test="${cartList != null}" >
							<c:set var="cartTotalPrice" value="0" /> 
	                		<c:set var="cartTotalPoint" value="0" /> 
							<c:forEach var="map" items="${cartList}" varStatus="status">
								<c:set var="cartTotalPrice" value="${cartTotalPrice + (map.saleprice * map.oqty) }"/> 
	                    		<c:set var="cartTotalPoint" value="${cartTotalPoint + (map.point * map.oqty) }" /> 
								<tr>
									<td>
										<input type="checkbox" name="pnum" id="pnum${status.count }" value="${map.fk_pnum }" checked="checked">
										<%-- <input type="text" name="carno" value="${cartTotalPrice }" >
										<input type="text" name="carno" value="${cartTotalPoint }" > --%>
									</td>
									<td>
										${map.pname}
										<input type="text" name="cartno" value="${map.cartno }" >
										<input type="text" name="pname" value="${map.pname }" >
									</td>
									<td>
										<input type="text" name="saleprice" value="${map.saleprice }">
									</td>
									<td>
										<input type="number" name="oqty" value="${map.oqty }">
									</td>
									<td>
										<input type="text" name="tt" value="${map.saleprice }*${map.oqty }">
									</td>
									<td>삭제</td>
								</tr>
							</c:forEach>
								<tr>
									<td colspan="6">
										<input type="text" name="sumtotalprice" value="${cartTotalPrice }"> <%-- 주문 총액 담는 곳 --%> 
	                    				<input type="text" name="sumtotalpoint" value=${cartTotalPoint }> <%-- 주문 총 포인트 담는 곳 --%> 
									</td>
								</tr>
						</c:if>
					</table>
				</div>
			</div>
	
			<div class="row">
				<div class="col-md-10 col-md-offset-1">
					<div class="total-wrap">
						<div class="row">
							<div class="col-md-8">
								<div class="row form-group">
									<div class="col-md-5">
										<div class="form-group">
											<span style="float: left; padding-top: 10px;">적용할 쿠폰 :</span>
											<input type="text" id="coupon" name="coupon" class="form-control" placeholder="적용할 쿠폰" style="width: 60%; float: left; margin-left: 10px;">
										</div>
									</div>
									<div class="col-md-4">
										<input type="button" value="적용할 쿠폰" class="btn">
									</div>
	
									<div class="col-md-3">
										<button type="button" onclick="goOrder();" class="btn btn-primary">주문하기</button>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-md-push-1 text-center">
								<div class="total">
									<div class="sub">
										<p>
											<span>총상품금액:</span> <span>10,000원</span>
										</p>
										<p>
											<span>배송비:</span> <span>5,000원</span>
										</p>
										<p>
											<span>할인:</span> <span>10%</span>
										</p>
									</div>
									<div class="grand-total">
										<p>
											<span><strong>결제금액:</strong></span> <span>14,000원</span>
										</p>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div><!-- row -->
		</form>
	</div><!-- container -->
</div>

<jsp:include page="../footer.jsp" />