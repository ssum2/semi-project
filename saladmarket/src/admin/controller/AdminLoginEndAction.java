package admin.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import admin.model.AdminDAO;
import admin.model.AdminVO;
import admin.model.InterAdminDAO;
import common.controller.AbstractController;

public class AdminLoginEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String method = req.getMethod();
		if(!"POST".equalsIgnoreCase(method)) {
			req.setAttribute("msg", "비정상적인 경로로 들어왔습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {
			String adminid = req.getParameter("userid");
			String adminpw = req.getParameter("pwd");
			
			String saveid = req.getParameter("saveid");
			
			
			InterAdminDAO adao = new AdminDAO();
			AdminVO admin = adao.AdminLoginCheck(adminid, adminpw);
			
			if(admin!=null) {
				
				HttpSession session = req.getSession();
				
				session.setAttribute("admin", admin);
				Cookie cookie = new Cookie("saveid", admin.getAdminid());
				
				if(saveid != null) {
					cookie.setMaxAge(7*24*60*60);
				}
				else {
					cookie.setMaxAge(0);
				}
				
				cookie.setPath("/");
				
				res.addCookie(cookie);
				

				req.setAttribute("msg", adminid+"님 환영합니다.");
				req.setAttribute("loc", "admin_index.do");
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

}
