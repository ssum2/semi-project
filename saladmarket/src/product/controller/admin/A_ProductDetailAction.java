package product.controller.admin;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;
import product.model.ProductVO;

public class A_ProductDetailAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String pnum = req.getParameter("pnum");
		
		
		InterProductDAO pdao = new ProductDAO();
		
		ProductVO pvo = pdao.getOneProductDetail(pnum);
		List<HashMap<String, String>> imgList = pdao.getAttachImgList(pnum);
		
		
		req.setAttribute("pvo", pvo);
		req.setAttribute("pnum", pnum);
		req.setAttribute("imgList", imgList);
		
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/a_productDetail.jsp");
	}

}
