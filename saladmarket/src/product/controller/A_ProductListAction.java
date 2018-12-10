package product.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;
import product.model.ProductVO;

public class A_ProductListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		InterProductDAO pdao = new ProductDAO();
		
		
		
		List<HashMap<String, String>> ldnameList = pdao.getLdnameList();
		req.setAttribute("ldnameList", ldnameList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/a_productList.jsp");
		
	}

}
