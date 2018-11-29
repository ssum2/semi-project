<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String CtxPath = request.getContextPath(); %>
<jsp:include page="../header.jsp" />
<aside id="colorlib-hero" class="breadcrumbs">
    <div class="flexslider">
       <ul class="slides">
          <li style="background-image: url(<%=CtxPath %>/store/images/cover-img-1.jpg);">
             <div class="overlay"></div>
             <div class="container-fluid">
                <div class="row">
                   <div class="col-md-6 col-md-offset-3 col-sm-12 col-xs-12 slider-text">
                      <div class="slider-text-inner text-center">
                         <h1>Join Us</h1>
                         <h2 class="bread"><span><a href="index.jsp">Home</a></span> <span><a href="login.jsp">Login</a></span></h2>
                      </div>
                   </div>
                </div>
             </div>
          </li>
         </ul> 
      </div>
</aside> 
      
<div class="container" style="margin-left: 30%;">      
   <div class="col-md-8">
      <form method="post" class="colorlib-form">
            <div class="form-group">
                  <div class="col-md-6">
                  <label for="userid">아이디</label>
                  <input type="text" id="userid" class="form-control" placeholder="ID">
               </div>
               <div class="col-md-3" style="margin-top: 8.5%">
                  <button class="btn" style="width: 80px; height: 20px; padding: 0%;"><span style="font-size: 2px;">아이디 확인</span></button>
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6">
                  <label for="password">비밀번호</label>
                  <input type="password" id="password" class="form-control" placeholder="Password">
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6">
                  <label for="pwdcheck">비밀번호확인</label>
                  <input type="password" id="pwdcheck" class="form-control" placeholder="Password">
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6">
                  <label for="username">성명</label>
                  <input type="text" id="username" class="form-control" placeholder="Name">
               </div>
            </div>
            <div class="form-group">
               <div class="col-md-6" >
                  <label for="email">이메일</label>
                  <input type="text" id="email" class="form-control" placeholder="abc@gmail.com">
               </div>
            </div>
            <div class="form-group">   
               <div class="col-md-6">
                  <label for="phone ">연락처</label>
                  <input type="text" id="phone " class="form-control" placeholder="Phone Number">
               </div>   
            </div>
            <div class="form-group">
               <div class="col-md-3">
                  <label for="postnum">우편번호</label>
                  <input type="text" id="postnum" class="form-control" placeholder="PostNum">
               </div>
               <div class="col-md-3" style="margin-top: 4%">
                  <input type="text" id="zippostalcode" class="form-control" placeholder="PostNum">
               </div>
               <div class="col-md-3" style="margin-top: 8.5%">
                  <button class="btn" style="width: 80px; height: 20px; padding: 0%;"><span style="font-size: 2pt;">우편번호찾기</span></button>
               </div>
            </div>
            <div class="col-md-6">
               <div class="form-group">
                  <label for="fname">주소</label>
                      <input type="text" id="address" class="form-control" placeholder="Enter Your Address">
                   </div>
                  <div class="form-group">
                         <input type="text" id="address2" class="form-control" placeholder="Second Address">
                  </div>
               </div>
               <div class="form-group">
               <div class="col-md-4">
                  <label for="postnum">생년월일</label>
                  <input type="number" id="birthyyyy" class="form-control" name="birthyyyy" min="1950" max="2050" step="1" value="1995"/>      
               </div>
               <div class="col-md-3" style="margin-top: 4%">
                  <select id="birthmm" name="birthmm" class="form-control">
                     <option value ="01">01</option>
                     <option value ="02">02</option>
                     <option value ="03">03</option>
                     <option value ="04">04</option>
                     <option value ="05">05</option>
                     <option value ="06">06</option>
                     <option value ="07">07</option>
                     <option value ="08">08</option>
                     <option value ="09">09</option>
                     <option value ="10">10</option>
                     <option value ="11">11</option>
                     <option value ="12">12</option>
                  </select> 
               </div>
               <div class="col-md-3" style="margin-top: 4%">
                  <select id="birthdd" name="birthdd" class="form-control">
                  <option value ="01">01</option>
                  <option value ="02">02</option>
                  <option value ="03">03</option>
                  <option value ="04">04</option>
                  <option value ="05">05</option>
                  <option value ="06">06</option>
                  <option value ="07">07</option>
                  <option value ="08">08</option>
                  <option value ="09">09</option>
                  <option value ="10">10</option>
                  <option value ="11">11</option>
                  <option value ="12">12</option>
                  <option value ="13">13</option>
                  <option value ="14">14</option>
                  <option value ="15">15</option>
                  <option value ="16">16</option>
                  <option value ="17">17</option>
                  <option value ="18">18</option>
                  <option value ="19">19</option>
                  <option value ="20">20</option>
                  <option value ="21">21</option>
                  <option value ="22">22</option>
                  <option value ="23">23</option>
                  <option value ="24">24</option>
                  <option value ="25">25</option>
                  <option value ="26">26</option>
                  <option value ="27">27</option>
                  <option value ="28">28</option>
                  <option value ="29">29</option>
                  <option value ="30">30</option>
                  <option value ="31">31</option>
               </select> 
            </div>      
         </div>

         <div class="row">
            <div class="col-md-12" style="margin-left: 35%; margin-top: 5%;" >
               <p><a href="#" class="btn btn-primary">가입하기</a></p>
            </div>
         </div>

       </form>
   </div>
</div>

<div class="gototop js-top">
   <a href="#" class="js-gotop"><i class="icon-arrow-up2"></i></a>
</div>

<jsp:include page="../footer.jsp" />