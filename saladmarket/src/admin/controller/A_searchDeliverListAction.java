package admin.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_searchDeliverListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String searchType = req.getParameter("searchType");
		String searchWord = req.getParameter("searchWord");
		String str_currentShowPage = req.getParameter("currentShowPage");
		int sizePerPage = 4;
		
		if(str_currentShowPage == null || "".equals(str_currentShowPage)) {
			str_currentShowPage = "1";
		}
		
		int currentShowPage = Integer.parseInt(str_currentShowPage);
		
		InterProductDAO ipdao = new ProductDAO();
		
		// **** 검색조건에 따른 배송상태 리스트 보기 **** //
		List<HashMap<String,String>> deleverList = ipdao.getDeliverList(searchType,searchWord,currentShowPage,sizePerPage);

		JSONArray jsonarray = new JSONArray();
		
		if(deleverList != null && deleverList.size()>0) {
			for(HashMap<String,String> dmap:deleverList) {
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("odrdnum", dmap.get("deleverList"));
				jsonobj.put("odrcode", dmap.get("odrcode"));
				jsonobj.put("odrdate", dmap.get("odrdate"));
				jsonobj.put("fk_pnum", dmap.get("fk_pnum"));
				jsonobj.put("fk_userid", dmap.get("fk_userid"));
				jsonobj.put("oqty", dmap.get("oqty"));
				jsonobj.put("odrtotalprice", dmap.get("odrtotalprice"));
				jsonobj.put("odrstatus", dmap.get("odrstatus"));
				jsonobj.put("titleimg", dmap.get("titleimg"));
				
				jsonarray.add(jsonobj);
			}
		}//end of if----------------
		
		
		String str_jsonarr = jsonarray.toString();
		req.setAttribute("str_jsonarr", str_jsonarr);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/deleverList.jsp");
		
	}

}
