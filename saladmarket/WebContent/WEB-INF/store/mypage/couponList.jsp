<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String ctxPath=request.getContextPath();
%>
    
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



<div style="" align="center">
	<div style="margin-top: 3%; margin-bottom: 1%;" align="center">
		<span style="font-weight: bold; font-size: 18pt; ">할인쿠폰</span>
		&nbsp;&nbsp;할인쿠폰은 구매조건에 따라 상품 결제 시 적용할 수 있습니다.
	</div>	
	<div class="colorlib-shop">
		<div class="container">
			<div class="row row-pb-md">
				<div class="col-md-10 col-md-offset-1">
					<div class="product-name">
						<div class="one-forth text-center">
						<span>쿠폰명</span>
						</div>
						<div class="one-eight text-center">
							<span>할인율</span>
						</div>
						<div class="one-eight text-center">
							<span>사용조건</span>
						</div>
						<div class="one-eight text-center">
							<span>유효기간</span>
						</div>
						<div class="one-eight text-center">
							<span>사용유무</span>
						</div>
					</div>
					
					
					<c:forEach var="cpvo" items="${myCouponList}">
					<div class="product-cart">
						<div class="one-forth">
							<div class="display-tc">
								<strong style="font-size: 15pt; "><span>${cpvo.cpname}</span></strong><br>
								<a href="#">상세보기</a>
							</div>
						</div>
						<div class="one-eight text-center">
							<div class="display-tc">
								<span class="percent" style="font-size: 12pt; color:red;" >${cpvo.discountper}%</span>
							</div>
						</div>
						<div class="one-eight text-center">
							<div class="display-tc" ">
								
								<span class="useCondition">
								<fmt:formatNumber pattern="###,###" value="${cpvo.cpusemoney}"/>원이상 구매시
								 최대 <fmt:formatNumber pattern="###,###" value="${cpvo.cpuselimit}"/> 할인
								 </span>
								
							</div>
						</div>
						<div class="one-eight text-center">
							<div class="display-tc">
								<span class="useDate">${cpvo.showCpexpiredate}까지</span>
							</div>
						</div>
						<div class="one-eight text-center">
							<div class="display-tc">
								<span class="couponUse">
								<c:if test="${cpvo.cpstatus_m==1}">사용가능</c:if>
								<c:if test="${cpvo.cpstatus_m==0}">사용완료</c:if>
								</span>
							</div>
						</div>
					</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="../footer.jsp"/>