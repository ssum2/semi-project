<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% String CtxPath = request.getContextPath(); %>
<jsp:include page="../header.jsp"/>

		<aside id="colorlib-hero" class="breadcrumbs">
			<div class="flexslider">
				<ul class="slides">
			   	<li style="background-image: url(<%=CtxPath%>/store/images/PFPI-WEBSITE-SLIDERS-1.png);">
			   		<div class="overlay"></div>
			   		<div class="container-fluid">
			   			<div class="row">
				   			<div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
				   				<div class="slider-text-inner text-center">
				   					<h1>Order Complete</h1>
				   					<h2 class="bread"><span><a href="<%= CtxPath %>/index.do">Home</a></span> <span><a href="<%= CtxPath %>/cart.do">Shopping Cart</a></span> <span>Checkout</span></h2>
				   				</div>
				   			</div>
				   		</div>
			   		</div>
			   	</li>
			  	</ul>
		  	</div>
		</aside>

		<!-- 결제 완료 메시지 -->
		<div class="colorlib-shop">
			<div class="container">
				
				<div class="row">
					<div class="col-md-10 col-md-offset-1 text-center">
						<span class="icon"><i class="icon-shopping-cart"></i></span>
						<h2>Thank you for purchasing, Your order is complete</h2>
						
					</div>
				</div>
			</div>
		</div>
		<!-- 결제 완료 끝 -->

		<!-- 결제완료된 내역 리스트 시작 -->
		<div id="colorlib-contact">
			<div class="container">
				<div class="row">
					<div class="col-md-10 col-md-offset-1">
						<div class="contact-wrap">
							<h3 align="center" style="margin-top:50px;">결제가 완료되었습니다.</h3>
							<h4 align="center">배송을 아래의 계좌로 입금하셔야 배송을 시작합니다.</h4>
							<h5 align="center">국민은행 123456-78-912345</h5>
							
							<h4 align="center">${sessionScope.loginuser.name }님의 결제내용은 다음과 같습니다.</h4>
							<form action="#">
								<table class="table" style="margin-top:50px; border: 1px solid #cfd2d6;">
								    <tbody>
										<tr>
											<th width="40%" style="padding-left: 50px;"> 주문자명 </th>
											<td width="*">${sessionScope.loginuser.name }님 </td>
										</tr>
										<tr>
											<th style="padding-left: 50px;"> 주문번호 </th>
											<td>${orderList[0].odrcode } </td>
										</tr>
									</tbody>
								</table>
								<c:set var="totalPrice" value="0"/>
								<table class="table" style="margin-top:30px; border: 1px solid #cfd2d6;">
									<tbody>
										<c:forEach var="map" items="${orderList}">
										<tr>
											<th width="40%" style="padding-left: 50px;"> 주문상품 </th>
											<td width="30%">
												${map.pname} ${map.oqty}개
											</td>
											<td>
												<span style="text-decoration: line-through;"><fmt:formatNumber value="${map.price * map.oqty}" pattern="###,###"/>원</span> → 
												<span><fmt:formatNumber value="${map.odrprice * map.oqty}" pattern="###,###"/>원</span>
											</td>
										</tr>
										<c:set var="totalPrice" value="${totalPrice + (map.price * map.oqty)}"/>
										<%-- <tr>
											<th style="padding-left: 50px;"> 주문금액 </th>
											<td>${map.odrprice * map.oqty}원</td>
										</tr>
										<tr>
											<th style="padding-left: 50px;"> 할인금액 </th>
											<c:set var="totalPrice" value="${map.price * map.oqty}"/>
											<c:set var="orderPrice" value="${map.odrprice * map.oqty}"/>
											<td>${totalPrice-orderPrice}</td>
										</tr> --%>
										</c:forEach>
									</tbody>
								</table>
								<table class="table" style="margin-top:30px; border: 1px solid #cfd2d6;">
									<tbody>
										<tr>
											<th width="40%" style="padding-left: 50px;"> 주문금액 </th>
											<td width="*"><fmt:formatNumber value="${totalPrice}" pattern="###,###"/></td>
										</tr>
										<tr>
											<th width="40%" style="padding-left: 50px;"> 할인금액 </th>
											<td width="*"><fmt:formatNumber value="${totalPrice-orderList[0].odrtotalprice}" pattern="###,###"/></td>
										</tr>
										<tr>
											<th width="40%" style="padding-left: 50px;"> 결제금액 </th>
											<td width="*"><fmt:formatNumber value="${orderList[0].odrtotalprice}" pattern="###,###"/></td>
										</tr>
										<tr>
											<th style="padding-left: 50px;"> 포인트적립혜택 </th>
											<td><fmt:formatNumber value="${orderList[0].odrtotalpoint}" pattern="###,###"/>point</td>
										</tr>
									</tbody>
								</table>
							
							
								<div class="text-center" style="padding-top:50px;">
									<p>
										<a href="<%=CtxPath %>/index.do"class="btn btn-primary">Home</a>
										<a href="<%=CtxPath %>/index.do"class="btn btn-primary btn-outline">Continue Shopping</a>
									</p>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 결제완료된 내역 리스트 끝 -->
</div>
		


<jsp:include page="../footer.jsp"/>
