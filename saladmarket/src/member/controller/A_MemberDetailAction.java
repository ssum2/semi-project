package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class A_MemberDetailAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String mnum = req.getParameter("mnum");
		
		InterMemberDAO mdao = new MemberDAO();
		MemberVO mvo = mdao.getOneMemberBymnum(mnum);

		req.setAttribute("mvo", mvo);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/a_memberDetail.jsp");
		
		
	}

}
