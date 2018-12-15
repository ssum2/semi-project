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
		
		String userid = req.getParameter("userid");
		String pwd = req.getParameter("pwd");
		String saveid = req.getParameter("saveid");
		
		
		InterMemberDAO memberdao = new MemberDAO();
		MemberVO loginuser = memberdao.loginCheck(userid, pwd);
        if(loginuser==null) {
//	        a) 로그인 실패했을 경우
	        	String msg = "로그인에 실패하였습니다. 아이디 또는 패스워드를 확인해주세요.";
	        	String loc = "javascript:history.back();";
	        	req.setAttribute("msg", msg);
				req.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;
	     }
	     else if(loginuser.isRequireCertify()) {
//	        b) 마지막 로그인 일시가 오래됐을 경우 ---> 인증 요구
				String msg = "마지막 로그인 일시가 6개월 전이므로 휴면계정으로 전환 되었습니다. 관리자에게 문의하세요.";
				String loc = "index.do";
				
				req.setAttribute("msg", msg);
				req.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;
		}
	    else {
			
			HttpSession session = req.getSession();
			session.setAttribute("loginuser", loginuser);
			Cookie cookie = new Cookie("userInputId", loginuser.getUserid());
			
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
	
	}

} // end of class
