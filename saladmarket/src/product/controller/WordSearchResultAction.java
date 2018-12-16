package product.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;


public class WordSearchResultAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String fk_ldname = req.getParameter("fk_ldname");
		String sdname = req.getParameter("sdname");
		
		String searchword = req.getParameter("searchword");
		
		String str_currentShowPageNo = req.getParameter("currentShowPageNo");
		if(str_currentShowPageNo == null || "".equals(str_currentShowPageNo)) {
			str_currentShowPageNo = "1";
		}
		
		String orderby = req.getParameter("orderby");
		
		if(orderby == null || orderby.trim().isEmpty()) {
			orderby = "pdate";
		}
		
		int	currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
		int sizePerPage = 4;
		
		InterProductDAO pdao = new ProductDAO();
		
		List<HashMap<String, Object>> contentList = null;
		if("".equals(sdname)) {
			contentList = pdao.getContentListbyfk_ldname(fk_ldname, sizePerPage, currentShowPageNo, searchword, orderby);
		}
		else {
			contentList = pdao.getContentListbysdname(sdname, sizePerPage, currentShowPageNo, searchword, orderby);
		}

		JSONArray jsonArray = new JSONArray();
		
		if(contentList != null && contentList.size() > 0) {
			for(HashMap<String, Object> map : contentList) {
				JSONObject job = new JSONObject();
				// JSONObject 는 JSON형태(키:값)의 데이터를 관리해 주는 클래스이다.

				job.put("pacname", map.get("pacname"));
				job.put("pacnum", map.get("pacnum"));
				job.put("pnum", map.get("pnum"));
				job.put("pimgfilename", map.get("pimgfilename"));
				job.put("saleprice", map.get("saleprice"));
				job.put("stname", map.get("stname"));
				job.put("price", map.get("price"));
			    jsonArray.add(job);
			}
		}
		
		String str_jsonArray = jsonArray.toString();
		
		req.setAttribute("str_jsonArray", str_jsonArray);
				
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/JSON/wordSearchResultJSON.jsp");
		
	}

}
