package product.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class AddproductAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String pnum = req.getParameter("pnum");

		JSONObject job = new JSONObject();
		
		if(Integer.parseInt(pnum) == -1) {

			job.put("price", -1  );
			job.put("name", "-1"  );
			
			String str_job = job.toString();
			
			req.setAttribute("str_job", str_job);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/JSON/jarray2.jsp");
			
			return;
		}
		else {

			InterProductDAO pdao = new ProductDAO();
			
			HashMap<String, Object> productDetail = pdao.getProductpriceNname(pnum);

			job.put("price", productDetail.get("saleprice")  );
			job.put("name", productDetail.get("pname")  );
			job.put("point", productDetail.get("point"));
			
			String str_job = job.toString();
			
			req.setAttribute("str_job", str_job);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/JSON/jarray2.jsp");
			
	
		}
				
	}

}
