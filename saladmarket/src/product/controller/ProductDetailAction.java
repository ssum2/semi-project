package product.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;
import product.model.ProductVO;

public class ProductDetailAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String pnum = req.getParameter("pnum");
		String pacnum = req.getParameter("pacnum");
		String img = req.getParameter("img");
		
		InterProductDAO pdao = new ProductDAO();
		
		ProductVO pvo = null;
		List<ProductVO> pvoList = null;
		List<String> imgList = null;
		int len = 0;
		
		if("1".equals(pacnum)) {
			// 패키지가 없는 단품상품일경우 pnum으로 정보를 불러온다 
		
			try {
				
				pvo = pdao.getProductOneByPnum(Integer.parseInt(pnum));
			
				imgList = pdao.getImagesByPnum(pnum);
				
			} catch (NumberFormatException e) {
				req.setAttribute("msg", "비정상 접근입니다.");
				req.setAttribute("loc", "javascript:history.back();");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;
			}
			

		}
		else {
			// 패키지가 있는 상품일경우 
			pvoList = pdao.getProductListbyPacnum(pacnum);
			
			imgList = pdao.getImagesByPacnum(pacnum);
		
			len = pdao.getProductCountbyPacnum(pacnum);
		}
		
		req.setAttribute("pvoList", pvoList);
		req.setAttribute("pvo", pvo);
		req.setAttribute("imgList", imgList);
		req.setAttribute("img", img);
		req.setAttribute("pnum", pnum);
		req.setAttribute("pacnum", pacnum);
		req.setAttribute("len", len);
		
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/product/productDetail.jsp");
	}

}
