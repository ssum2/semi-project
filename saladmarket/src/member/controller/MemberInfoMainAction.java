package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class MemberInfoMainAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser != null) {
		
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/mypage/memberInfoMain.jsp");
		}
		else {
			String msg = "권한이 없습니다.";
        	String loc = "javascript:history.back();";
        	req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		
		
	}

}
