package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;

public class LoginAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser != null) {
			req.setAttribute("msg", "로그아웃 하세요.");
			req.setAttribute("loc", "javascript:history.back();"); 
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/login/login.jsp");
	
		}
	}
}
