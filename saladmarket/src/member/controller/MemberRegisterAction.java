package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class MemberRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser == null) {
		
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/member/join.jsp");
		}
		else {
			String msg = "�α׾ƿ� �� ��� ���� �մϴ�.";
        	String loc = "javascript:history.back();";
        	req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		
	}

}
