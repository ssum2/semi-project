package admin.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import admin.model.AdminVO;
import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_MemberInfoAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String userid = req.getParameter("userid");
		
		if(userid != null) {
			InterMemberDAO mdao = new MemberDAO();
			
			// *** 회원한명의 정보를 보여주는 메소드 *** //
			MemberVO mvo = mdao.getOneMemberByUserid(userid);
			
			req.setAttribute("mvo", mvo);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/a_memberDetail.jsp");
		}
		else {
			req.setAttribute("msg", "회원정보가 없습니다!");
			req.setAttribute("loc", "javascript:history.back()");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}//end of if~else------------------
		
	}//end of execute(HttpServletRequest req, HttpServletResponse res)-----------------

}
