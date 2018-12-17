package order.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.ProductDAO;

public class AjaxCartDeleteAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) { // POST 방식이 아니라면
		
			String msg = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back();";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		} 

		ProductDAO productdao = new ProductDAO();
		
		String cartno = req.getParameter("cartno");
		
		// *** jsp_cart 테이블에서 cartno인 컬럼 delete 
		int n = productdao.updateDeleteCart(cartno, "0");
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("n", n);
		
		String str_nInfo = jsonObj.toString();
         
        req.setAttribute("str_nInfo", str_nInfo);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/JSON/ajaxCartDelete.jsp");

	} // execute() ////////////////////////////////////////////////////////////////////////

}
