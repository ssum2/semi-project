package order.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.ProductDAO;

public class CartEditAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
			
		
		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) { // POST 방식이 아니라면
		
			String msg = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back();";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		} 

			ProductDAO productdao = new ProductDAO();
			
			String cartno = req.getParameter("cartno");
			String oqty = req.getParameter("oqty");
			
			// *** jsp_cart 테이블에서 oqty가 0보다 크면 update, 0이면 delete 장바구니변경 
			int n = productdao.updateDeleteCart(cartno, oqty);
			
			String msg = (n>0)?"장바구니에 제품수량 변경성공!!":"장바구니에 제품수량 변경실패!!";
			String loc = (n>0)?"cart.do":"javascript:history.back();";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
	      
	   } // execute() ////////////////////////////////////////////////////////////////////////

}
