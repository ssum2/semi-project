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

public class A_GetSdnameJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {


		String ldname = req.getParameter("ldname");
		
		if(ldname==null) {
			ldname="";
		}
		
		InterProductDAO pdao = new ProductDAO();
		
		List<HashMap<String, String>> sdnameList = pdao.getSdnameListByLdname(ldname);
		
		JSONArray jsonArray = new JSONArray();
		
		if(sdnameList != null && sdnameList.size() > 0) {
			for(HashMap<String, String> map : sdnameList) {
				JSONObject jsonObj = new JSONObject();
				// JSONObject 는 JSON형태(키:값)의 데이터를 관리해 주는 클래스이다.
				
			    jsonObj.put("sdname", map.get("sdname"));
			    jsonObj.put("fk_ldname", map.get("fk_ldname"));
			    			    	    
			    jsonArray.add(jsonObj);
			}
		}
		
		String str_jsonArray = jsonArray.toString();
		
		req.setAttribute("str_jsonArray", str_jsonArray);
				
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/getSdnameJSON.jsp");
		
	}

}
