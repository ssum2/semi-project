package product.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class ProductListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String fk_ldname = req.getParameter("fk_ldname");
		String sdname = req.getParameter("sdname");
		
		if(sdname == null || sdname.trim().isEmpty()) {
			sdname="";
		}
		else {
			req.setAttribute("sdname", sdname);
		}
		req.setAttribute("fk_ldname", fk_ldname);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/product/productList.jsp");
	}

}
