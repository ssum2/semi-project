package admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_goPageDeliverBarAction extends AbstractController{

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String stype = req.getParameter("stype");//검색타입
		String sword = req.getParameter("sword");//검색단어
		String str_cursizePerPage = req.getParameter("cursizePerPage");
		int sizePerPage = 4;
		
		try {
			sizePerPage = Integer.parseInt("str_cursizePerPage");
		} catch (NumberFormatException e) {
			sizePerPage = 4;
		}
		
		InterProductDAO pdao =  new ProductDAO();
		
		int totalcount = 0 ;
		
		//** 검색타입별 갯수 알아오기
		totalcount = pdao.getTotalCoutBySType(stype,sword);
		
		// 총페이지수 알아오기
		int totalPage = (int)Math.ceil((double)totalcount/sizePerPage);
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("totalPage", totalPage);
		
		String str_json = jsonobj.toString();
		
		req.setAttribute("str_json", str_json);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/pageBardeliverJSON.jsp");
	}
		
}


