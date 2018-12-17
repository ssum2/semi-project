package admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import admin.model.AdminVO;
import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_OrderListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		AdminVO adminLogin = super.getAdmin(req);
		
		if(adminLogin == null) {
			String msg = "로그인 후 사용 가능합니다.";
        	String loc = "adminLogin.do";
        	req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		
		else {
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/a_orderList.jsp");
	
		}
	}
}
