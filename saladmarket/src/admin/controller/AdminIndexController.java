package admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import admin.model.AdminVO;
import common.controller.AbstractController;

public class AdminIndexController extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		AdminVO admin = super.getAdmin(req);
		
		if(admin==null) {
			String msg = "�α��� �� ��� �����մϴ�.";
        	String loc = "javascript:history.back();";
        	
        	req.setAttribute("msg", msg);
        	req.setAttribute("loc", loc);
        	
        	super.setRedirect(false);;
        	super.setViewPage("/WEB-INF/msg.jsp");
		}
		else {
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/admin_index.jsp");
		}
	}
}
