<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<%
	String ctxPath=request.getContextPath();
%>
    
<jsp:include page="../header.jsp"/>

<script type="text/javascript">
$(document).ready(function(){


});

function goDel(odrcode){
	var bool = confirm("주문을 취소하시겠습니까? 취소처리에는 영업일 기준 최대 7일이 소요됩니다.");
	if(bool){
		var form_data = {"odrcode":odrcode};
			
		$.ajax({
			url: "orderCancleJSON.do",
			type: "GET",
			data: form_data,
			dataType: "JSON",
			success: function(json){
				var result = json.result;
		
				if(result==1){
					alert("취소 신청 완료");
					window.location.reload();
				}
				else{
					alert("취소 실패! 관리자에게 문의 바랍니다.");
					window.location.reload();
				}
			},// end of success
			error: function(request, status, error){
				if(request.readyState == 0 || request.status == 0) return;
				else alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});// end of $.ajax
	}
	else{
		return false;
	}
}

</script>

<aside id="colorlib-hero" class="breadcrumbs">
	<div class="flexslider">
		<ul class="slides">
	   	<li style="background-image: url(<%=ctxPath %>/store/images/PFPI-WEBSITE-SLIDERS-1.png);">
	   		<div class="overlay"></div>
	   		<div class="container-fluid">
	   			<div class="row">
		   			<div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
		   				<div class="slider-text-inner text-center">
		   					<h1>MyPage</h1>
		   					<h2 class="bread">
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/memberModify.do">회원정보수정</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/orderList.do">주문내역보기</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/couponList.do">보유쿠폰 보기</a></span>
			   					
			   					<%-- <span style="font-size: 13pt;"><a href="<%=ctxPath %>/refundChange.do">환불 및 교환내역</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/myPickList.do">찜 목록 보기</a></span>
			   					<span style="font-size: 13pt;"><a href="<%=ctxPath %>/myReview.do">리뷰보기</a></span> --%>
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
		<span style="font-weight: bold; font-size: 18pt; ">주문내역보기</span>
	</div>	
	<div class="colorlib-shop">
         <div class="container">
            <div class="row row-pb-md">
               <div class="col-md-12">
               
                  <div class="product-name">
                  	<div class="one-eight text-center">
                        <span>주문코드</span>
                     </div>
                     <div class="one-eight text-center">
                        <span>주문상품</span>
                     </div>
                     <div class="one-eight text-center">
                        <span>가격</span>
                     </div>
                     <div class="one-eight text-center">
                        <span>수량</span>
                     </div>
                     <div class="one-eight text-center">
                        <span>총가격</span>
                     </div>
                     <div class="one-eight text-center">
                        <span>주문날짜</span>
                     </div>
                     <div class="one-eight text-center">
                        <span>배송상태</span>
                     </div>
                     <div class="one-eight text-center">
                        <span>취소/환불</span>
                     </div>
                  </div>
                  
                  
                  <c:if test="${orderList != null}">
                  	<c:forEach var="map" items="${orderList}">
                  	<div class="product-cart">
                  	<div class="one-eight text-center">
                        <div class="display-tc">
                           <a href="<%=ctxPath %>/orderDetail.do?odrdnum=${map.odrdnum}">${map.odrcode}</a>
                        </div>
                     </div>
                     <div class="one-eight text-center">
                        <div class="display-tc">
                           <span style="font-weight: bold;">${map.pname}</span>
                        </div>
                     </div>
                     <div class="one-eight text-center">
                        <div class="display-tc">
                           <span class="price"><fmt:formatNumber value="${map.price}" pattern="###,###" /> 원</span>
                        </div>
                     </div>
                     <div class="one-eight text-center">
                        <div class="display-tc">
                           <span style="font-weight: bold;">${map.oqty}</span>
                        </div>
                     </div>
                     <div class="one-eight text-center">
                       <c:set var="su" value="${map.oqty}" />
				       <c:set var="danga" value="${map.saleprice}" />
				       <c:set var="totalmoney" value="${su * danga}" />
				     
                        <div class="display-tc">
                           <span class="price"><fmt:formatNumber value="${totalmoney}" pattern="###,###" /> 원</span>
                        </div>
                     </div>   
                     <div class="one-eight text-center">
                        <div class="display-tc">
                           <span class="">${map.odrdate}</span>
                        </div>
                     </div>   
                     <div class="one-eight text-center">
                        <div class="display-tc">
                           <span class="order">${map.odrstatus}</span>
                           <c:if test="${map.odrstatus=='배송중'}">
                           <br/>(KH택배: ${map.invoice})
                           </c:if>
                        </div>
                     </div>
                     <div class="one-eight text-center">
                        <div class="display-tc">
                           <a href="#" class="" onClick="goDel(${map.odrcode})">취소</a>/
                           <a href="#" class="" onClick="goDel(${map.odrcode})">환불</a>
                        </div>
                     </div>
                    </div>
                  	</c:forEach>
                  </c:if> 
                     
                  
                  
                  
               </div>
                  
            </div>
         </div>
      </div>
</div>
<jsp:include page="../footer.jsp"/>