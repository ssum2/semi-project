package product.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;
import product.model.ProductVO;

public class RecommdprodlistAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		InterProductDAO pdao = new ProductDAO();
		
		List<ProductVO> pvoList = pdao.getRecommdProdlist();

		JSONArray jarray = new JSONArray();
		
		if(pvoList != null && pvoList.size() > 0) {
			for( ProductVO pvo : pvoList ) {
				
				JSONObject job = new JSONObject();
				
				job.put("pacname", pvo.getFk_pacname());
				job.put("pacnum", pvo.getPacnum());
				job.put("pnum", pvo.getPnum());
				job.put("pimgfilename", pvo.getPimgfilename());
				job.put("saleprice", pvo.getSaleprice());
				job.put("stname", pvo.getFk_stname());
				
				jarray.add(job);
				
			}
		}

		String str_jsonArray = jarray.toString();
		
		//System.out.println(str_jsonArray);
		
		req.setAttribute("str_jsonArray", str_jsonArray);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/JSON/jarray.jsp");
		
		
		
	}

}
