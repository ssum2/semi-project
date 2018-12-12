package product.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_ProductListPageBarJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String ldname = req.getParameter("ldname");
		if(ldname==null) {
			ldname="";
		}
		
		String sdname = req.getParameter("sdname");
		if(sdname==null) {
			sdname="";
		}
		
		String searchType = req.getParameter("searchType");
		if(searchType==null) {
			searchType="";
		}
		
		String searchWord = req.getParameter("searchWord");
		if(searchWord==null) {
			searchWord="";
		}
		String len = req.getParameter("len");
		
		InterProductDAO pdao = new ProductDAO();
		int totalCount = 0;
		
		if(searchType=="")
			totalCount = pdao.getTotalProductCountAll(searchWord, ldname, sdname);
		
		else 
			totalCount = pdao.getTotalProductCount(searchType, searchWord, ldname, sdname);

		int totalPage = (int)Math.ceil((double)totalCount/Integer.parseInt(len)); 

		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("totalPage", totalPage);
		
		String str_totalPage = jsonObj.toString();
		
		req.setAttribute("str_totalPage", str_totalPage);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/productListPageBarJSON.jsp");

	}

}
