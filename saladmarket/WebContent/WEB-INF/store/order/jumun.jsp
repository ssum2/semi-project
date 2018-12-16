<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% String ctxPath = request.getContextPath(); %>
<jsp:include page="../header.jsp" />

<script>

	$(document).ready(function(){

	   
	}); // $(document).ready() ////////////////////////////////////////////////

	function goOrder(){
		
		if(!$("input:radio[name=optradio]").is(":checked")) {
			alert("결제수단을 선택하셔야 합니다.");
			return;
		} // end of if
		
		if(!$("input:checkbox[id=checkOrd]").is(":checked") ) {
			alert("주문사항 확인에 동의하셔야 합니다.");
			return;
		} 
		
		if($("input:radio[name=optradio]").attr("value") == "계좌이체") {
			
			var url = "orderPaymentGateway.do?userid=${sessionScope.loginUser.userid}&paymoney=${sumtotalprice}"; 
	        window.open(url, "paymentGateway", "left=350px, top=100px, width=820px, height=600px");
			
			//frm.submit();
		} else {
			goOrderCheck("${sessionScope.loginUser.userid}");
		}
	} // end of function goOrder()
	
	function goOrderCheck(userid){
		var frm = document.ordEndFrm;
		frm.userid.value = userid;
		frm.method = "POST";
		frm.action = "orderEnd.do";
		frm.submit();
	}
	
</script>

<aside id="colorlib-hero" class="breadcrumbs">
	<div class="flexslider">
		<ul class="slides">
			<li
				style="background-image: url(<%=ctxPath %>/store/images/cover-img-1.jpg);">
				<div class="overlay"></div>
				<div class="container-fluid">
					<div class="row">
						<div
							class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
							<div class="slider-text-inner text-center">
								<h1>Checkout</h1>
								<h2 class="bread">
									<span>
									<a href="<%=ctxPath %>/index.do">Home</a></span>
									<span><a href="<%=ctxPath %>/cart.do">Shopping Cart</a></span> <span>Checkout</span>
								</h2>
							</div>
						</div>
					</div>
				</div>
			</li>
		</ul>
	</div>
</aside>
<div class="colorlib-loader"></div>

<div id="page">

	<!--  구매 진행 상황  카트 - 결제 -주문완료  배너? -->
	<div class="colorlib-shop">
		<div class="container">
			<div class="row row-pb-md">
				<div class="col-md-10 col-md-offset-1">
					<div class="process-wrap">
						<div class="process text-center active">
							<p>
								<span>01</span>
							</p>
							<h3>Shopping Cart</h3>
						</div>
						<div class="process text-center active">
							<p>
								<span>02</span>
							</p>
							<h3>Checkout</h3>
						</div>
						<div class="process text-center">
							<p>
								<span>03</span>
							</p>
							<h3>Order Complete</h3>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
			<form name="ordEndFrm">
				<div class="col-md-7">
					<h2>결제 상세</h2>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label for="country"> 주문번호 </label>
								<div class="form-field">
									<input type="text" id="odrcode" name="odrcode" class="form-control" value="${odrcode}" placeholder="s-20181128-3" readonly="readonly">
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-6">
								<label for="fname">이름</label>
								<input type="text" id="fname" name="fname" class="form-control" value="${mvo.name}" placeholder="홍길동" readonly="readonly">
								<input type="hidden" id="userid" name="userid" class="form-control" value="${sessionScope.loginUser.userid}" placeholder="hongkd" readonly="readonly">
							</div>

						</div>
						<div class="col-md-12"></div>
						<div class="col-md-12">
							<div class="form-group">
								<div class="col-md-6" style="padding: 0;">
									<label for="fname">우편번호</label>
									<input type="text" id="zippostalcode" value="${mvo.post }" class="form-control" placeholder="123-456" readonly="readonly">
								</div>
							</div>
							<div class="form-group">
								<label for="fname">주소</label> <input type="text" id="address" class="form-control" value="${mvo.address1 }" placeholder="서울 특별시 종로구 " readonly="readonly">
							</div>
							<div class="form-group">
								<input type="text" id="address2" class="form-control" value="${mvo.address2 }" placeholder="다동 " readonly="readonly">
							</div>
						</div>

						<div class="form-group">
							<div class="col-md-6">
								<label for="email">이메일 주소</label>
								<input type="text" id="email" class="form-control" value="${mvo.email }" placeholder="abc@naver.com" readonly="readonly">
							</div>
							<div class="col-md-6">
								<label for="Phone">전화번호</label>
								<input type="text" id="zippostalcode" class="form-control" ${mvo.phone } placeholder="010-1234-5678" readonly="readonly">
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12"></div>
						</div>
					</div>
				</div>
				
				<div class="col-md-5">
					<div class="cart-detail">
						<h2>장바구니</h2>
						<ul>
							<li><span>SubtotalPrice</span> <span>${sumtotalprice}</span>
							<li><span>SubtotalPoint</span> <span>${sumtotalpoint}</span>
								<ul>
								<c:forEach var="cartno" items="${cartnoArr }" varStatus="status">
									<li>
										<span>${oqtyArr[status.index]} x ${pnameArr[status.index]}</span>
										<input type="hidden" name="pnum" value="${pnumArr[status.index]}">
										<input type="hidden" name="saleprice" value="${salepriceArr[status.index]}">
										<input type="hidden" name="oqty" value="${oqtyArr[status.index]}">
										<c:if test="${cartnoArr != null }">
										<input type="hidden" name="cartno" value="${cartnoArr[status.index]}">
										</c:if> 
										<input type="hidden" name="pname" value="${pnameArr[status.index]}"> 
										<span>${oqtyArr[status.index]*salepriceArr[status.index] }</span>
									</li>
									
									<!-- <li><span>1 x Product Name</span> <span>$78.00</span></li> -->
								</c:forEach>
								</ul></li>
							<li>
								<c:set var="deliverPay" value="0"/>
								<span>배송료</span>
								<c:if test="${sumtotalprice >= 20000}">
									<span>0</span>
									<c:set var="deliverPay" value="0"/>
								</c:if>
								<c:if test="${sumtotalprice < 20000}">
									<span>5000</span>
									<c:set var="deliverPay" value="${deliverPay+5000}"/>
								</c:if>
							</li>
							
							<li>
								<c:set var="discountMoney" value="0"/>
								<span>쿠폰</span>
								<c:if test="${couponMap == null}">
									<span>없음</span>
								</c:if>
								<c:if test="${couponMap != null}">
									<span>${couponMap.cpname}</span>
									<input type="hidden" name="couponNo" value="${couponMap.cpnum}">
								</c:if>
								<!-- 쿠폰 하는 중 ㅜㅜ 쿠폰 DB에서 조회해오기!! DB 이상한 거 수미언니에게 말하기!!
									그러고 나서 총금액에 적용시키기!! 주문 과정DB에서 mycupon status를 0으로 만드는 과정 넣기
								 -->
							</li>
							
							<li>
								<span>주문합계</span>
								<c:set var="sumtotalprice" value="${sumtotalprice + deliverPay}"/>
								<span>${sumtotalprice}</span>
								<input type="hidden" name="sumtotalprice" value="${sumtotalprice}">
								<input type="hidden" name="sumtotalpoint" value="${sumtotalpoint}">
							</li>
						</ul>
					</div>
					<div class="cart-detail">
						<h2>결제 방법</h2>
						<!-- <div class="form-group">
							<div class="col-md-12">
								<div class="radio">
									<label><input type="radio" name="optradio" value="계좌이체">계좌이체</label>
								</div>
							</div>
						</div> -->
						
						<div class="form-group">
							<div class="col-md-12">
								<div class="radio">
									<label><input type="radio" name="optradio" value="카드결제">카드결제</label>
								</div>
							</div>
						</div>
						
						<div class="form-group">
							<div class="col-md-12">
								<div class="radio">
									<label><input type="radio" name="optradio" value="무통장입금">무통장 입금</label>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="col-md-12">
								<div class="checkbox">
									<label>
									<input type="checkbox" id="checkOrd">
										위 주문 사항에 대하여 확인하였습니다.
									</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<p><a class="btn btn-primary" onclick="goOrder();">주문하기</a></p>
						</div>
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>

</div>

</div>

<div class="gototop js-top">
	<a href="#" class="js-gotop"><i class="icon-arrow-up2"></i></a>
</div>

<!-- jQuery -->
<script src="js/jquery.min.js"></script>
<!-- jQuery Easing -->
<script src="js/jquery.easing.1.3.js"></script>
<!-- Bootstrap -->
<script src="js/bootstrap.min.js"></script>
<!-- Waypoints -->
<script src="js/jquery.waypoints.min.js"></script>
<!-- Flexslider -->
<script src="js/jquery.flexslider-min.js"></script>
<!-- Owl carousel -->
<script src="js/owl.carousel.min.js"></script>
<!-- Magnific Popup -->
<script src="js/jquery.magnific-popup.min.js"></script>
<script src="js/magnific-popup-options.js"></script>
<!-- Date Picker -->
<script src="js/bootstrap-datepicker.js"></script>
<!-- Stellar Parallax -->
<script src="js/jquery.stellar.min.js"></script>
<!-- Main -->
<script src="js/main.js"></script>
</script>


<jsp:include page="../footer.jsp" />