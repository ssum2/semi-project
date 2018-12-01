package member.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import admin.model.AdminDAO;
import admin.model.AdminVO;
import admin.model.InterAdminDAO;
import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class LoginEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		
		MemberVO loginuser = super.getLoginUser(req);
		String userid = "";
		String pwd = "";
		String saveid = ""; 
		
		if(loginuser == null) {
			userid = req.getParameter("userid");
			pwd = req.getParameter("pwd");
			saveid=req.getParameter("saveid");
		}
		else {
			userid = loginuser.getUserid();
			pwd = loginuser.getPwd();
		}
		
		
		InterMemberDAO memberdao = new MemberDAO();
		loginuser = memberdao.loginCheck(userid, pwd);
		
		if(loginuser!=null) {
			
			HttpSession session = req.getSession();
			session.setAttribute("loginuser", loginuser);
			Cookie cookie = new Cookie("saveid", loginuser.getUserid());
			
			if(saveid != null) {
				cookie.setMaxAge(7*24*60*60);
			}
			else {
				cookie.setMaxAge(0);
			}
			
			cookie.setPath("/");
			
			res.addCookie(cookie);
		

			req.setAttribute("msg", loginuser.getName()+"님 환영합니다.");
			req.setAttribute("loc", "index.do");
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}
		else {
			req.setAttribute("msg", "로그인에 실패하였습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
	}	
	

}
