package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import admin.model.AdminVO;
import member.model.MemberVO;

public abstract class AbstractController implements Command {
	// 추상클래스
	private boolean isRedirect = false;
	/*	#개발자 지정 규칙
	 	View 페이지(.jsp 페이지)에 sendRedirect 방법으로 이동 할 때; 
	 		isRedirect = true
	 	View 페이지(.jsp 페이지)에 forward 방법으로 이동 할 때; 
	 		isRedirect = false
	 */
	
	private String viewPage; // view에서 사용할 page

	public boolean isRedirect() {
		return isRedirect;
		// >> return타입이 boolean인 경우 get이 아니라 is~~
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
	
//	#현재 세션에 객체가 있는지 없는지 알려주는 메소드 (로그인 유무 검사) --> return MemberVO or null
	public MemberVO getLoginUser(HttpServletRequest req) {
		MemberVO loginuser = null;
		
		HttpSession session = req.getSession();
		loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser == null) { // 로그인 객체가 없을 때
			String msg = "로그인 후 사용 가능합니다.";
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
		
		if(admin == null) { // 로그인 객체가 없을 때
			String msg = "로그인 후 사용 가능합니다.";
        	String loc = "javascript:history.back();";
        	
        	req.setAttribute("msg", msg);
        	req.setAttribute("loc", loc);
        	
        	isRedirect = false;
        	viewPage = "/WEB-INF/msg.jsp";
		}
		return admin;
	}
}
