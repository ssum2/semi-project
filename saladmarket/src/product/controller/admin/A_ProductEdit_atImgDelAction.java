package product.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_ProductEdit_atImgDelAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String pimgnum = req.getParameter("pimgnum");

		InterProductDAO pdao = new ProductDAO();
		
		int result = pdao.deleteAttachProductImg(pimgnum);
		
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("result", result);
		
		req.setAttribute("jsonObj", jsonObj);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/deleteAttachProductImgJSON.jsp");

		
		
	}

}
