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
		/*AdminVO adminLogin = super.iamAdmin(req);
		
		if(adminLogin == null) return;*/
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/a_orderList.jsp");
	}

}
