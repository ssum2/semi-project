package product.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.PackageVO;
import product.model.ProductDAO;

public class A_PackageDetailAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String pacnum = req.getParameter("pacnum");
		InterProductDAO pdao = new ProductDAO();
		PackageVO pacvo = pdao.getOnePackage(pacnum);
		
		req.setAttribute("pacvo", pacvo);
		req.setAttribute("pacnum", pacnum);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/a_packageDetail.jsp");
	}

}
