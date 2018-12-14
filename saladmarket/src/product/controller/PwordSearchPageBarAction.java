package product.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class PwordSearchPageBarAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String fk_ldname = req.getParameter("fk_ldname");
		String sdname = req.getParameter("sdname");
		String searchword = req.getParameter("searchword");
		String sizePerPage = req.getParameter("sizePerPage");
		
		InterProductDAO pdao = new ProductDAO();
		
		int totalCount = 0;

		if("".equals(sdname)) {
			totalCount = pdao.getCountByfk_ldnameNword(fk_ldname, searchword);
		}
		else {
			totalCount = pdao.getCountBysdnameNword(sdname, searchword);
		}
		
		int totalPage = (int)Math.ceil((double)totalCount/Integer.parseInt(sizePerPage)); 
		

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage);
		
		String str_totalPage = jsonObj.toString();
		
		req.setAttribute("str_totalPage", str_totalPage);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/JSON/listajax.jsp");
		
		
	}

}
