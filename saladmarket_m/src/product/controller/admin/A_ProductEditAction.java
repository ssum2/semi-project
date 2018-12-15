package product.controller.admin;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;
import product.model.ProductVO;

public class A_ProductEditAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String pnum = req.getParameter("pnum");
		
		InterProductDAO pdao = new ProductDAO();
		
		ProductVO pvo =  pdao.getOneProductDetail(pnum);
		List<HashMap<String, String>> imgList = pdao.getAttachImgList(pnum);
		List<HashMap<String, String>> pacnameList = pdao.getPacnameList();
		List<HashMap<String, String>> sdnameList = pdao.getSdnameList();
		List<HashMap<String, String>> ctnameList = pdao.getCtnameList();
		List<HashMap<String, String>> stnameList = pdao.getStnameList();
		List<HashMap<String, String>> etnameList = pdao.getEtnameList();
		
		
		
		req.setAttribute("pvo", pvo);
		req.setAttribute("pnum", pnum);
		req.setAttribute("imgList", imgList);
		
		req.setAttribute("pacnameList", pacnameList);
		req.setAttribute("sdnameList", sdnameList);
		req.setAttribute("ctnameList", ctnameList);
		req.setAttribute("stnameList", stnameList);
		req.setAttribute("etnameList", etnameList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/a_productEdit.jsp");
	}

}
