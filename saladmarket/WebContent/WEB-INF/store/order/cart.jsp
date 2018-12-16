<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String CtxPath = request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../header.jsp" />

<script>

   $(document).ready(function(){
     
	   showCart();

	   var couponUse = "";
	   	
   		couponUse += '<option value="">==적용할 쿠폰 선택==</option>';
		<c:forEach var="cpvo" items="${couponList}">
			couponUse += '<option data-goodsno="26217" id="cpnum" value="${cpvo.discountper},${cpvo.cpusemoney},${cpvo.cpuselimit}" >${cpvo.cpname} (${cpvo.cpusemoney}원 이상 사용시 최대 ${cpvo.cpuselimit}원)</option>';
	    </c:forEach>
	
	   $("#selectCoupon").empty().append(couponUse);
	   
	   var opvalue = 0;
	   var total = 0;
	   var sale = 0;
	   var saleResult = 0;
	   
	   // 쿠폰 드롭박스 선택시
	   $("#selectCoupon").bind("change", function(){
	   	    opvalue = $("select option:selected").val();
	   		var couponValArr = opvalue.split(',');
	   	  
		   	var discountper = parseInt(couponValArr[0]);
		   	var cpusemoney= parseInt(couponValArr[1]);
		   	var cpuselimit= parseInt(couponValArr[2]);

	   	   total = parseInt($("#totalResult").text());
	   	   sale = parseInt((total+5000)*(opvalue/100));
	   	   saleResult = parseInt((total+5000)*((100-parseInt(opvalue))/100));
	   	   cpResult = parseInt((total+5000)-cpuselimit);
	   	   if(opvalue!=""){
	   		   
	   		   var payResult = "";
	   		   
   		   		$("#salePercent").empty().html(discountper+"% <br/>("+cpusemoney+"원 이상 사용시 최대 "+cpuselimit+"원)");
		   		   
   		   		if(cpuselimit > sale) {
		   			payResult += saleResult;
		   		}
		   		else {
		   			payResult += cpResult;
		   		}
	   	        $("#totalPay").empty().html(payResult+"원");
	   	   }
	   	   else{
	   	      return false;
	   	   }
	   	   
	   }); // selectCoupon
	   

	    
   }); // $(document).ready() ////////////////////////////////////////////////

 
   function showCart(){
	   
	   $.ajax({
	          url:"ajaxShowCart.do",
	          type:"GET",
	          dataType:"JSON",
	          success:function(json){
	        	  
	        	  var html = "", cartTotalHtml = "";
	        	  var cartTotalPrice = 0;
	        	  
	              $.each(json, function(entryIndex, entry){
	            	  
	            	  if (json.length > 0) {
	
	            		cartTotalPrice = cartTotalPrice + entry.sumtotalprice;
	            		
	            	  	html += " <div class='product-cart'> "+
			            	  	"	<div class='one-eight text-center' style='width: 80px; height: 100px; display: table; float: left; '>"+
								"		<div class='display-tc' >"+
								"			<input type='checkbox' id='pnum_"+entryIndex+"' class='checkboxpnum' value='"+entry.pnum+"' />"+
								"		</div>"+
								"	</div>"+
								"	<div class='one-forth' style='width: calc(100% - 600px);'>"+
								" 		<div class='product-img' style='background-image: url(img/"+entry.titleimg+");'></div>"+
								"		<div class='display-tc'>"+
								"			<h3>"+entry.pacname+"<br>-"+entry.pname+"</h3>"+
								"		</div>"+
								"	</div>"+
								"	<div class='one-eight text-center'>"+
								"		<div class='display-tc'>"+
								"			<span style='text-decoration: line-through;'>"+entry.price+"원</span> -> "+
								"			<span class='price'>"+entry.saleprice+"원</span>"+
								"		</div>"+
								"	</div>"+
								"	<div class=\"one-eight text-center\">"+
								"		<div class=\"display-tc\">"+
								"			<input type='number' id='quantity_"+entryIndex+"' name='oqty' class='form-control input-number text-center oqtyClass' value='"+entry.oqty+"' min='1' max='100' //>"+
								" 			<input type='hidden' id='cartno_"+entryIndex+"' name='cartno' value='"+entry.cartno+"' //>"+
								"			<input type='hidden' id='pqty' name='pqty' value='"+entry.pqty+"'/> "+		
								" 			<button type='button' onClick=\"goOqtyEdit('cartno_"+entryIndex+"', 'quantity_"+entryIndex+"');\" > 수정 </button>"+
								"		</div>"+
								"	</div>"+
								"	<div class=\"one-eight text-center\">"+
								"		<div class=\"display-tc\">"+
								"			<span class='price' id='totalprice' name='sumtotalprice'>"+entry.sumtotalprice+"</span>원<br/>"+
								"			(<span class='price' id='totalpoint' name='sumtotalpoint'>"+entry.sumtotalpoint+"</span>point)"+
								"		</div>"+
								"	</div>"+
								"	<div class=\"one-eight text-center\">"+
								"		<div class=\"display-tc\">"+
								"			<a href='#' class='closed' onClick=\"cartDelete('cartno_"+entryIndex+"');\" ></a>"+
								"		</div>"+
								"	</div>"+
								"</div>";
								
	            	  totalHtml = cartTotalPrice+"원"; 
	            	  //totalPayHtml = cartTotalPrice*5000*percent;
	            	  }
	            	  else {
	            		  html += "상품이 없습니다.";
	            	  }
	            	  $("#result").html(html);
	            	  $("#totalResult").html(totalHtml);
	            	  //$("#totalPay").html(totalPayHtml);
	              }); // each
	          }, // success
	          error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	         }
	       });
	   
   } // showCart --------------------------------------------------------------------------------------------------
   
  function goOqtyEdit(cartnoID, oqtyID) {
	 
		var form_data = {"cartno":$("#"+cartnoID).val(), "oqty":$("#"+oqtyID).val()};
		
		$.ajax({
          url:"ajaxCartEdit.do",
          type:"POST",
          data:form_data,
          dataType:"JSON",
          success:function(json){
             
        	  var sumtotalprice = 0;
        	  
              if(json.n > 0) {
            	 alert("장바구니에 제품수량 변경성공!!");
            	 showCart();
              }
              else {
            	 alert("장바구니에 제품수량 변경실패!!");
            	 showCart();
              }
        	  
          },
          error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
         }
       });

	} // goOqtyEdit(cartnoID, oqtyID)
	
	
	function cartDelete(cartnoID) {
		
		var form_data = {"cartno":$("#"+cartnoID).val()};
		
		$.ajax({
          url:"ajaxCartDelete.do",
          type:"POST",
          data:form_data,
          dataType:"JSON",
          success:function(json){
             
              if(json.n > 0) {
            	 alert("장바구니에서 삭제 성공!!");
            	 showCart();
              }
              else {
            	 alert("장바구니에서 삭제 실패!!");
            	 showCart();
              }
        	  
          },
          error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
         }
       });
		
	} // cartDelete(cartnoID)
	
	
	
	
	function goOrder() {
			
		
	var flag = false;
	$(".checkboxpnum").each(function(){
		if($(this).is(':checked') == true) {
			flag = true;
			return false;
		}
	}); 
	
	// 체크박스가 선택되지 않은 경우
	if(flag == false) {
		alert("주문할 상품을 최소한 1개이상 선택하셔야 합니다.");
		return;
	}
	
	// 주문할 제품에 대해 체크박스가 최소한 1개이상 선택한 경우
	else { 
		
		var YN = confirm("선택한 상품을 주문하시겠습니까?");
		
		if(YN) { // 주문하는 경우
			
			
			var frm = document.ordFrm;
			frm.method = "POST";
			
			var sumtotalprice = 0;
			var sumtotalpoint = 0;
			
			var bool = false;
			var pname;
			
			$(".checkboxpnum").each(function(){
				if($(this).is(':checked') == true) {
				
					var totalprice = $(this).parent().parent().parent().find("#totalprice").text();
					sumtotalprice += parseInt(totalprice);
					
					var totalpoint = $(this).parent().parent().parent().find("#totalpoint").text();
					sumtotalpoint += parseInt(totalpoint);
				}
				
				var pqty = $(this).parent().parent().parent().find("#pqty").val();
				var oqty = $(this).parent().parent().parent().find(".oqtyClass").val();
				
				// 2. 주문량이 재고량보다 큰 경우
				if(parseInt(oqty) > parseInt(pqty)) {
					bool = true;
					return false;
				}
				
			}); // $(".checkboxpnum").each()
			
			frm.sumtotalprice.value=sumtotalprice;
			frm.sumtotalpoint.value=sumtotalpoint;
			
			
			// 2. 주문량이 재고량보다 큰 경우
			if(bool){
				alert("제품의 재고가 부족합니다.");
				return;
			}
			
			frm.action="order.do";
			frm.submit();
			
			var index = 0;
			$(".checkboxpnum").each(function(){
				if(!$(this).is(':checked'))  {	
					$(this).parent().parent().parent().find(":input").attr("disabled",true);
				}
				index++;
				
			}); // $(".checkboxpnum").each()			
			
				
		}
		else { // 주문을 취소하는 경우
			alert("상품 주문을 취소하셨습니다.");
			return;
		} // if-else (confirm YN)
		
	} // if-else(checkboxpnum flag)
		
	

}// end of goOrder()----------------------------------------

	
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
							<div class="one-eight text-center"  style='width: 80px; height: 100px; display: table; float: left; '>
								<span>선택</span>
							</div>
							<div class="one-forth text-center" style='width: calc(100% - 600px);'>
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
						</div><!-- 장바구니 List 윗부분 -->
						
					
						<div id="result">
						</div>
				
					
					</div>
				</div>
				
				<form name="orderFrm">
				<div class="row">
					<div class="col-md-10 col-md-offset-1">
						<div class="total-wrap">
							<div class="row">
							
							
								<div class="col-md-8">
									
										<div class="row form-group">
										
											<div class="col-md-8">
												<div class="form-group">
													<c:if test="${couponList!=null}">
													 	<label for="sel" style="padding-right:30px;">적용할 쿠폰</label>
			                                    		<select name="addopt[]" id="selectCoupon">
					                                    </select>
					                                 </c:if>
												</div>
											</div>
									       
											<%--
											<div class="col-md-3">
												<input type="button" value="적용할 쿠폰"  class="btn" />
												<span id="selectCouponOne"> </span>
											</div> 
											--%>
											
											<div class="col-md-3">
												<input type="submit" value="주문하기"  onClick="goOrder();" style="cursor: pointer;"  class="btn btn-primary" />
											</div>
											
										</div>
									
								</div>
								
								
								
								
								<div class="col-md-4 text-center">
									<div class="total">
										<div class="sub">
											<p><span>총상품금액:</span> <span id="totalResult"></span></p>
											<p><span>배송비:</span> <span id="cartFee" >5,000원</span></p>
											<p><span>할인:</span> <span id="salePercent">0%</span></p>
										</div>
										<div class="grand-total">
											<p><span><strong>결제금액:</strong></span> <span id="totalPay">원</span></p>
										</div>
									</div>
								</div>
								
							</div>
						</div>
					</div>
				</div>
			  </form>
			</div>
		</div>
	
	
<%-- 장바구니에 담긴 제품수량을 수정하는 form
<form name="updateOqtyFrm">
	<input type="text" name="cartno" />
	<input type="text" name="oqty" />
</form>
--%>
 	
<jsp:include page="../footer.jsp" />