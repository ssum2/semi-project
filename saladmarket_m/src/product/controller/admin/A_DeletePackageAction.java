package product.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_DeletePackageAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String pacnum = req.getParameter("pacnum");
		
		InterProductDAO pdao = new ProductDAO();
		
		int result = pdao.deletePackageByPacnum(pacnum);
		
		String msg = "";
		String loc = "";
		if(result==1) {
			msg = "삭제 완료";
			loc = req.getContextPath()+"/a_packageList.do";
		}
		else {
			msg = "삭제 실패";
			loc = req.getContextPath()+"/a_packageList.do";
		}
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/msg.jsp");
	}

}
