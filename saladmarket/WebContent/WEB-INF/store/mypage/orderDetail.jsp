<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<% String ctxPath=request.getContextPath(); %>
<jsp:include page="../header.jsp"/>

<aside id="colorlib-hero" class="breadcrumbs">
	<div class="flexslider">
		<ul class="slides">
	   	<li style="background-image: url(<%=ctxPath %>/store/images/cover-img-1.jpg);">
	   		<div class="overlay"></div>
	   		<div class="container-fluid">
	   			<div class="row">
		   			<div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
		   				<div class="slider-text-inner text-center">
		   					<h1>MyPage</h1>
		   					<h2 class="bread">
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/memberModify.do">회원정보수정</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/refundChange.do">환불 및 교환내역</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/myPickList.do">찜 목록 보기</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/orderList.do">주문내역보기</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/couponList.do">보유쿠폰 보기</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/myReview.do">리뷰보기</a></span>
		   					</h2>
		   				</div>
		   			</div>
		   		</div>
	   		</div>
	   	</li>
	  	</ul>
  	</div>
</aside>
<div id="colorlib-contact">
	<div class="container">
			<div class="row">
				<div class="col-md-10 col-md-offset-1">
					<div class="contact-wrap">
						<h4 align="center">${username}님의 ${ordermap.pname} 상품 결제 내역</h4>
						
							<table class="table" style="margin-top:50px;">
							    <tbody>
									<tr>
										<th style="padding-left: 50px;"> 주문자명 </th>
										<td > ${username}</td>
									</tr>
									<tr>
										<th style="padding-left: 50px;"> 주문번호 </th>
										<td >${ordermap.odrcode}</td>
									</tr>
									<tr style="border-bottom: hidden;">
										<th style="padding-left: 50px;"> 주문상품 </th>
										<td> ${ordermap.pname}</td>
									</tr>
									<tr style="border-top: hidden;">
										<th style="padding-left: 50px;"></th>
										<td><img src="/saladmarket/img/${ordermap.titleimg}" width="300px"></td>
									</tr>
									
									<tr>
										<th style="padding-left: 50px;"> 주문수량 </th>
										<td>${ordermap.oqty}</td>
									</tr>
									<tr>
										<th style="padding-left: 50px;"> 상품가 </th>
										<td><fmt:formatNumber value="${ordermap.price}" pattern="###,###"/>원</td>
									</tr>
									<tr>
										<th style="padding-left: 50px;"> 주문금액 </th>
										 <c:set var="su" value="${ordermap.oqty}" />
									       <c:set var="danga" value="${ordermap.price}" />
									       <c:set var="totalmoney" value="${su * danga}" />
										<td><fmt:formatNumber value="${totalmoney}" pattern="###,###"/>원</td>
									</tr>
									
									<tr>
										<th style="padding-left: 50px;"> 할인금액 </th>
										<td><fmt:formatNumber value="${totalmoney-(ordermap.odrprice)}" pattern="###,###"/>원 </td>
									</tr>
									<tr>
										<th style="padding-left: 50px;"> 결제금액 </th>
										<td ><fmt:formatNumber value="${ordermap.odrprice}" pattern="###,###"/>원</td>
									</tr>
									<tr>
										<th style="padding-left: 50px;"> 포인트적립혜택 </th>
										<td ><fmt:formatNumber value="${ordermap.point}" pattern="###,###"/>point</td>
									</tr>
								</tbody>
							</table>
						
							<div class="text-center" style="padding-top:50px;">
								<p>
									<a href="<%=ctxPath %>/index.do"class="btn btn-primary">Home</a>
									<a href="shop.html"class="btn btn-primary btn-outline">Continue Shopping</a>
								</p>
							</div>
							
					</div>
				</div>
			</div>
		</div>
	</div>
<jsp:include page="../footer.jsp"/>