package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;


public class LogoutAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		#로그아웃 처리
		HttpSession session = req.getSession();
		session.removeAttribute("loginuser");

		
		super.setRedirect(true); // 페이지 이동
		super.setViewPage("index.do");
		
	}

}
