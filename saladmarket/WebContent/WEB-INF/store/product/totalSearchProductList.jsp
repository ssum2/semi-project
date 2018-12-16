<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String CtxPath = request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
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

	
});
		
	function orderby(orderby) {
		
		var frm = document.orderByFrm;
		frm.sizePerPage.value = '${sizePerPage}';
		frm.totalSearchWord.value = '${totalSearchWord}';
		frm.sort.value = orderby;
		frm.method = "GET";
		frm.action = "totalSearchProductList.do";
		frm.submit();
		
	}// end of function orderby()----------------------------------

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
            <ul>
               <li style="float: right;">
                  <div class="dropdown">
                     <button type="button" class="btn dropdown-toggle" type="button" data-toggle="dropdown" style="border-radius: 0; height: 50px; background-color: #FFC300;">정렬<span class="caret"></span></button>
                     <ul class="dropdown-menu">
                        <li id="name" onclick="orderby('pacname');"><a>이름순</a></li>
                        <li id="like" onclick="orderby('plike');"><a>인기순</a></li>
                        <li id="new" onclick="orderby('pdate');"><a>신상품순</a></li>
                        <li id="price" onclick="orderby('saleprice');"><a>가격낮은순</a></li>
                     </ul>
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
            <div class="row">
               <div class = "row" align="center"></div>
					<c:forEach var="map" items="${productList}" varStatus="status" >
						<div class="col-md-3 text-center">
							<div class="product-entry">
					      		<div class="product-img" style="background-image: url(/saladmarket/img/${map.pimgfilename});">
					      			<p class="tag">
					      				<span class="sale">${map.stname}</span>
					      			</p>
					      		    <div class="cart">
					      		    <p>
						      			<span class="addtocart"><a href="jumun.do"><i class='icon-shopping-cart'></i></a></span>
					      		        <span><a href="productDetail.do?pacnum='${map.pacnum}'&pnum='${map.pnum}'&img='${map.pimgfilename}'"><i class="icon-eye"></i></a></span>
					      		        <span><a href="like.do"><i class="icon-heart3"></i></a></span>
					      	            </p>
					      	        </div>
					      	    </div>
					      	    <div class="desc"> 
					      	    	<h3><a href="javascript:goDetail('${map.pnum}','${currentURL}');">${map.pacname}</a></h3>
					      	        <p class="price" style="font-weight: bold;"><span><fmt:formatNumber value="${map.saleprice}" pattern="###,###"/>원</span>
					      	        &nbsp;<span class="sale"><fmt:formatNumber value="${map.price}" pattern="###,###"/>원</span></p>
					      	    </div>
					      	 </div>
						</div>					      	 
				      	 <c:if test="${(status.count)%4 == 0}">
							 <tr>
								<td colspan="4">&nbsp;&nbsp;</td>
					         </tr>
						 </c:if>
				      	 </c:forEach>
				     
               <div id="pageBar" class="col-md-12 text-center">
               ${pageBar}
               </div>
            </div>
		</div>
	</div>
</div>
<!--   -->

<form name="orderByFrm">
	<input type="hidden" name="sizePerPage" />
	<input type="hidden" name="totalSearchWord" />
	<input type="hidden" name="sort" />
</form>
   
<jsp:include page="../footer.jsp"/>