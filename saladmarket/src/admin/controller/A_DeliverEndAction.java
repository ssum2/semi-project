package admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_DeliverEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String odrdnum = req.getParameter("odrdnum");
		if(odrdnum != null) {
			InterProductDAO ipdao = new ProductDAO();
			
			// **** 배송완료로 변경시켜주는 추상 메소드 **** //
			int n = ipdao.changeDeliverEnd(odrdnum);
			
			if(n == 1) {
				req.setAttribute("msg", "배송완료로 변경되었습니다!");
				req.setAttribute("loc", "a_orderList.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}//end of if----------
		}
		else {
			req.setAttribute("msg", "변경실패!");
			req.setAttribute("loc", "a_orderList.do");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}//end of if~else------

	}//end of execute(HttpServletRequest req, HttpServletResponse res)--------

}
