package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class A_memberEditAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String mnum = req.getParameter("mnum");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");
		String postnum = req.getParameter("postnum");
		String address1 = req.getParameter("address1");
		String address2 = req.getParameter("address2");
		String lvname	= req.getParameter("lvname");
		int fk_lvnum = 0;
		
		if("Bronze".equals(lvname)) {
			fk_lvnum=1;
		}
		else if("Silver".equals(lvname)) {
			fk_lvnum=2;
		}
		else if("Gold".equals(lvname)) {
			fk_lvnum=3;
		}
		
		
		MemberVO mvo = new MemberVO();
		mvo.setMnum(Integer.parseInt(mnum));
		mvo.setEmail(email);
		mvo.setPhone(phone);
		mvo.setPostnum(postnum);
		mvo.setAddress1(address1);
		mvo.setAddress2(address2);
		mvo.setFk_lvnum(fk_lvnum);

		InterMemberDAO mdao = new MemberDAO();
		int result = mdao.updateMemberInfo(mvo);
		
		if(result ==1) {
			req.setAttribute("msg", "회원 정보 수정 성공!");
			req.setAttribute("script1", "window.opener.location.reload();\n" + "window.close();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {
			req.setAttribute("msg", "회원 정보 수정 실패!");
			req.setAttribute("loc", "a_memberList.do");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}

		
		
		
	}

}
