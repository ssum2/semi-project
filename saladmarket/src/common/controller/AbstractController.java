package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import admin.model.AdminVO;
import member.model.MemberVO;

public abstract class AbstractController implements Command {
	// �߻�Ŭ����
	private boolean isRedirect = false;
	/*	#������ ���� ��Ģ
	 	View ������(.jsp ������)�� sendRedirect ������� �̵� �� ��; 
	 		isRedirect = true
	 	View ������(.jsp ������)�� forward ������� �̵� �� ��; 
	 		isRedirect = false
	 */
	
	private String viewPage; // view���� ����� page

	public boolean isRedirect() {
		return isRedirect;
		// >> returnŸ���� boolean�� ��� get�� �ƴ϶� is~~
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}
	
//	#���� ���ǿ� ��ü�� �ִ��� ������ �˷��ִ� �޼ҵ� (�α��� ���� �˻�) --> return MemberVO or null
	public MemberVO getLoginUser(HttpServletRequest req) {
		MemberVO loginuser = null;
		
		HttpSession session = req.getSession();
		loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser == null) { // �α��� ��ü�� ���� ��
			String msg = "�α��� �� ��� �����մϴ�.";
        	String loc = "javascript:history.back();";
        	
        	req.setAttribute("msg", msg);
        	req.setAttribute("loc", loc);
        	
        	isRedirect = false;
        	viewPage = "/WEB-INF/msg.jsp";
		}
		return loginuser;
	}
	
	public AdminVO getAdmin(HttpServletRequest req) {
		AdminVO admin = null;
		
		HttpSession session = req.getSession();
		admin = (AdminVO)session.getAttribute("admin");
		
		if(admin == null) { // �α��� ��ü�� ���� ��
			String msg = "�α��� �� ��� �����մϴ�.";
        	String loc = "javascript:history.back();";
        	
        	req.setAttribute("msg", msg);
        	req.setAttribute("loc", loc);
        	
        	isRedirect = false;
        	viewPage = "/WEB-INF/msg.jsp";
		}
		return admin;
	}
}
