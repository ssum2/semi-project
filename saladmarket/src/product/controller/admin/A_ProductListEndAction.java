package product.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.MemberVO;
import product.model.InterProductDAO;
import product.model.ProductDAO;
import product.model.ProductVO;

public class A_ProductListEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String fk_name = "";
		String ldname = req.getParameter("ldname");
		if(ldname==null) {
			ldname="";
		}
		
		String sdname = req.getParameter("sdname");
		if(sdname==null) {
			sdname="";
		}
		
		String searchType = req.getParameter("searchType");
		String searchType2 = "";
		if(searchType==null) {
			searchType="pname";
			searchType2="fk_pacname";
		}
		
		String searchWord = req.getParameter("searchWord");
		if(searchWord==null) {
			searchWord="";
		}
	
		String str_currentShowPageNo = req.getParameter("currentShowPageNo");
		if(str_currentShowPageNo == null || "".equals(str_currentShowPageNo)) {
			str_currentShowPageNo = "1";
		}
		int	currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
		int sizePerPage = 10;
		
		
		InterProductDAO pdao = new ProductDAO();
		List<ProductVO> productList = null;
		
		
		
		if(searchType=="" && searchWord=="") {
			if(ldname=="" && sdname=="") {
				productList = pdao.getProductList(sizePerPage, currentShowPageNo);
			}
			else if(ldname!="" && sdname=="") {
				fk_name="fk_ldname";
				productList = pdao.getProductListByDname(sizePerPage, currentShowPageNo, fk_name, ldname);
			}
			
			else {
				fk_name="fk_sdname";
				productList = pdao.getProductListByDname(sizePerPage, currentShowPageNo, fk_name, sdname);
			}
		}
		else if(searchType=="" && searchWord!=""){
			if(ldname=="" && sdname=="") {
				productList = pdao.getProductListBySearchAll(sizePerPage, currentShowPageNo, searchWord);
				
			}
			else if(ldname!="" && sdname==""){
				fk_name="fk_ldname";
				productList = pdao.getProductListBySearchAllWithDname(sizePerPage, currentShowPageNo, fk_name, ldname, searchWord);
			}
			else {
				fk_name="fk_sdname";
				productList = pdao.getProductListBySearchAllWithDname(sizePerPage, currentShowPageNo, fk_name, sdname, searchWord);
			}
			
		}
		else if(searchType!="" && searchWord!=""){
			if(ldname=="" && sdname=="") {
				productList = pdao.getProductListBySearch(sizePerPage, currentShowPageNo, searchType, searchWord);
				
			}
			else if(ldname!="" && sdname==""){
				fk_name="fk_ldname";
				productList = pdao.getProductListBySearchWithDname(sizePerPage, currentShowPageNo, fk_name, ldname, searchType, searchWord);
			}
			else {
				fk_name="fk_sdname";
				productList = pdao.getProductListBySearchWithDname(sizePerPage, currentShowPageNo, fk_name, sdname, searchType, searchWord);
			}
		}
		
		JSONArray jsonArray = new JSONArray();
		
		if(productList != null && productList.size() > 0) {
			for(ProductVO pvo : productList) {
				JSONObject jsonObj = new JSONObject();
			    jsonObj.put("pnum", pvo.getPnum());
			    jsonObj.put("fk_pacname", pvo.getFk_pacname());
			    jsonObj.put("fk_sdname", pvo.getFk_sdname());
			    jsonObj.put("fk_ldname", pvo.getFk_ldname());
			    jsonObj.put("pname", pvo.getPname());
			    jsonObj.put("saleprice", pvo.getSaleprice());
			    jsonObj.put("point", pvo.getPoint());
			    jsonObj.put("pqty", pvo.getPqty());
			    jsonObj.put("plike", pvo.getPlike());
			    jsonObj.put("salecount", pvo.getSalecount());
			    jsonObj.put("titleimg", pvo.getTitleimg());
			    jsonArray.add(jsonObj);
			}
		}
		
		String str_jsonArray = jsonArray.toString();
		
		req.setAttribute("str_jsonArray", str_jsonArray);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/productListJSON.jsp");
	}

}
