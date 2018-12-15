package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;

public class A_memberListPageBarJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String lvnum = req.getParameter("lvnum");
		String searchType = req.getParameter("searchType");
		if(searchType==null) {
			searchType="name";
		}
		String searchWord = req.getParameter("searchWord");
		String len = req.getParameter("len");
		
		InterMemberDAO mdao = new MemberDAO();
				
		int totalCount = mdao.getTotalMemberCount(searchType, searchWord, lvnum);

		int totalPage = (int)Math.ceil((double)totalCount/Integer.parseInt(len)); 

		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("totalPage", totalPage);
		
		String str_totalPage = jsonObj.toString();
		
		req.setAttribute("str_totalPage", str_totalPage);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/a_memberListPageBarJSON.jsp");

	}

}
