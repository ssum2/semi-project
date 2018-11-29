package admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import admin.model.AdminDAO;
import admin.model.InterAdminDAO;
import common.controller.AbstractController;

public class AdminLoginEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String adminid = req.getParameter("userid");
		String adminpw = req.getParameter("pwd");
		
		InterAdminDAO adao = new AdminDAO();
		Boolean bool = adao.AdminLoginCheck(adminid, adminpw);
		
		if(bool) {
			req.setAttribute("msg", adminid+"님 환영합니다.");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/admin_index.jsp");
		}
		else {
			req.setAttribute("msg", "로그인에 실패하였습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
		
	}

}
