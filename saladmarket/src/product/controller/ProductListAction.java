package product.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.PackageVO;
import product.model.ProductDAO;
import product.model.ProductVO;

public class ProductListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String fk_sdname = req.getParameter("fk_sdname");
		System.out.println("ProductAction; fk_sdname: "+fk_sdname);
		
		InterProductDAO pdao = new ProductDAO();
		List<ProductVO> productListBySdname = pdao.selectProductListBySdname(fk_sdname);
		
		
		req.setAttribute("productListBySdname", productListBySdname);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/product/productList.jsp");
	}

}
