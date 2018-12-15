package product.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_modifyCategoryTagAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String ctnum = req.getParameter("ctnum");
		String ctname = req.getParameter("ctname");
		
		System.out.println("ctname: "+ctname);
		
		InterProductDAO pdao = new ProductDAO();
				
		int result = pdao.updateCtname(ctnum, ctname);

		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", result);
		
		req.setAttribute("jsonObj", jsonObj);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/updateCategoryTagJSON.jsp");
		
		
		
	}

}
