package order.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class OrderEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		MemberVO loginUser = super.getLoginUser(request);
		if (loginUser == null) return;

		String method = request.getMethod();

		if (!"POST".equals(method)) {
			// GET 방식이라면 
			String msg = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back();";

			request.setAttribute("msg", msg);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");

			return;
		} else {
			String userid = request.getParameter("userid"); 
			
			if (!String.valueOf(loginUser.getUserid()).equals(userid)) {
				// 로그인을 했지만 다른 사용자의 코인 충전은 불가하도록 한다.
				request.setAttribute("msg", "비정상적인 경로로 들어왔습니다!");
				request.setAttribute("loc", "javascript:history.back();");

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				return;

			} else {
			
				String[] pnumArr = request.getParameterValues("pnum"); 
				String[] salepriceArr = request.getParameterValues("saleprice");
				String[] oqtyArr = request.getParameterValues("oqty");
				String[] cartnoArr = request.getParameterValues("cartno");
				
				String odrcode = request.getParameter("odrcode");
				String couponNo = request.getParameter("couponNo");
				String optradio = request.getParameter("optradio");
				
				String sumtotalprice = request.getParameter("sumtotalprice");
				String sumtotalpoint = request.getParameter("sumtotalpoint");
				
				/*for(int i=0; i<pnumArr.length; i++) {
					System.out.println("제품번호 : "+pnumArr[i]); 
	                System.out.println("실제 판매 단가 : "+salepriceArr[i]); 
	                System.out.println("주문 갯수 : "+oqtyArr[i]);
	                System.out.println("카트 번호 : "+cartnoArr[i]);
				}
				System.out.println("주문코드 : "+odrcode); 
				System.out.println("결제방식 : "+optradio); 
				System.out.println("주문총액 : "+sumtotalprice); 
	            System.out.println("주문총포인트 : "+sumtotalpoint);*/
				
				if("카드결제".equals(optradio)) {
					InterProductDAO pdao = new ProductDAO();
		            int n = pdao.addOrder(odrcode, userid, 1, Integer.parseInt(sumtotalprice), Integer.parseInt(sumtotalpoint), pnumArr, oqtyArr, salepriceArr, cartnoArr, couponNo);
		            
		            if(n == 1) { 
		            	
		                request.setAttribute("msg", "주문 성공!!!"); 
		                request.setAttribute("loc", "orderComplete.do?odrcode="+odrcode);
		                
		                super.setRedirect(false); 
			            super.setViewPage("/WEB-INF/msg.jsp"); 
		
		            } else { 
		                request.setAttribute("msg", "주문 실패!!!"); 
		                request.setAttribute("loc", "javascript:histroy.back();"); 
		                
		                super.setRedirect(false); 
			            super.setViewPage("/WEB-INF/msg.jsp"); 
		            } // end of if~else
				} else { // 무통장
					
					InterProductDAO pdao = new ProductDAO();
		            int n = pdao.addOrder(odrcode, userid, 0, Integer.parseInt(sumtotalprice), Integer.parseInt(sumtotalpoint), pnumArr, oqtyArr, salepriceArr, cartnoArr, couponNo);
		            
		            if(n == 1) { 
		            	
		                request.setAttribute("msg", "주문 성공!!!"); 
		                request.setAttribute("loc", "orderNoPayment.do?odrcode="+odrcode);
		                
		                super.setRedirect(false); 
			            super.setViewPage("/WEB-INF/msg.jsp"); 
		
		            } else { 
		                request.setAttribute("msg", "주문 실패!!!"); 
		                request.setAttribute("loc", "javascript:histroy.back();"); 
		                
		                super.setRedirect(false); 
			            super.setViewPage("/WEB-INF/msg.jsp"); 
		            } // end of if~else
					
				} // end of if~ else -- 카드 or 무통장
				
				 super.setRedirect(false); 
		         super.setViewPage("/WEB-INF/msg.jsp"); 
			} // end of if~else
		} // end of if~else

	} // end of public void execute(HttpServletRequest request, HttpServletResponse response)

}
