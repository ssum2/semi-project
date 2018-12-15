package product.controller.admin;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_categoryTagListJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String searchWord = req.getParameter("searchWord");
		if(searchWord==null) {
			searchWord="";
		}
		
		InterProductDAO pdao = new ProductDAO();
		List<HashMap<String, String>> categoryTagList = pdao.getCategoryTagList(searchWord);
		
		
		JSONArray jsonArr = new JSONArray();
		
		
		for(HashMap<String ,String> map : categoryTagList) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("ctnum", map.get("ctnum"));
			jsonObj.put("ctname", map.get("ctname"));
			jsonObj.put("pqty", map.get("pqty"));
			
			jsonArr.add(jsonObj);
		}
		
		String str_jsonArray = jsonArr.toString();
		
		req.setAttribute("str_jsonArray", str_jsonArray);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/a_categoryTagListJSON.jsp");
		
	}

}
