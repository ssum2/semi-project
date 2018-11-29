package admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class AdminIndexController extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setAttribute("result", "관리자 메인");
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/admin_index.jsp");
	}
}
