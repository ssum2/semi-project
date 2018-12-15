package member.controller.mypage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberModifyAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser == null) {
			String msg = "로그인 후 사용 가능합니다.";
        	String loc = "memberLogin.do";
        	req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {
			String mnum = req.getParameter("mnum");
			
			InterMemberDAO mdao = new MemberDAO();
			MemberVO membervo = mdao.getOneMemberBymnum(mnum);
			int cpcnt = mdao.getMyCouponCnt(loginuser.getUserid());

			req.setAttribute("membervo", membervo);
			req.setAttribute("cpcnt", cpcnt);
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/mypage/memberModify.jsp");
			
		} // end of if~else
	
	} // end of execute

}
