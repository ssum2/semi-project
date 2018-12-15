package product.controller.admin;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.PackageVO;
import product.model.ProductDAO;
import product.model.ProductVO;

public class A_PackageListEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

			
			String searchWord = req.getParameter("searchWord");
			if(searchWord==null) {
				searchWord="";
			}
		
			String str_currentShowPageNo = req.getParameter("currentShowPageNo");
			if(str_currentShowPageNo == null || "".equals(str_currentShowPageNo)) {
				str_currentShowPageNo = "1";
			}
			int	currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
			int sizePerPage = 3;
			
			InterProductDAO pdao = new ProductDAO();
			List<PackageVO> packageList = pdao.getPackageList(sizePerPage, currentShowPageNo, searchWord);
			
			JSONArray jsonArray = new JSONArray();
			
			if(packageList != null && packageList.size() > 0) {
				for(PackageVO pvo : packageList) {
					JSONObject jsonObj = new JSONObject();
				    jsonObj.put("pacnum", pvo.getPacnum());
				    jsonObj.put("pacname", pvo.getPacname());
				    jsonObj.put("pacimage", pvo.getPacimage());
				    jsonObj.put("cnt", pvo.getCnt());
				  
				    jsonArray.add(jsonObj);
				}
			}
			
			String str_jsonArray = jsonArray.toString();
			
			req.setAttribute("str_jsonArray", str_jsonArray);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/JSON/packageListJSON.jsp");
	}
}
