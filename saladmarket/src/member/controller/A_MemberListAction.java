package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;

public class A_MemberListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String lvnum = req.getParameter("lvnum");
		String searchType = req.getParameter("searchType");
		String searchWord = req.getParameter("searchWord");
		
		
		if(lvnum == null || searchType == null || searchWord == null) {
			lvnum = "";
			searchType ="name";
			searchWord="";
		}
		InterMemberDAO mdao = new MemberDAO();
		
		int totalMemberCount = mdao.getTotalMemberCount(searchType, searchWord, lvnum);
		int newbieMemberCount = mdao.getNewbieMemberCount(searchType, searchWord, lvnum);;
		int dormantMemberCount = mdao.getDormantMemberCount(searchType, searchWord, lvnum);
	
		req.setAttribute("totalMemberCount", totalMemberCount);
		req.setAttribute("newbieMemberCount", newbieMemberCount);
		req.setAttribute("dormantMemberCount", dormantMemberCount);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/a_memberList.jsp");
	}

}
