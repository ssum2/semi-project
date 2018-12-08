package member.controller.mypage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberModifiyEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		String mnum = req.getParameter("mnum");
		String pwd = req.getParameter("pwd");
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");
		String postnum = req.getParameter("postnum");
		String address1 = req.getParameter("address1");
		String address2 = req.getParameter("address2");


		MemberVO membervo = new MemberVO();
		membervo.setMnum(Integer.parseInt(mnum));
		membervo.setPwd(pwd);
		membervo.setName(name);
		membervo.setEmail(email);
		membervo.setPhone(phone);
		membervo.setPostnum(postnum);
		membervo.setAddress1(address1);
		membervo.setAddress2(address2);

		InterMemberDAO memberdao = new MemberDAO();
		int result = memberdao.updateMemberMyInfo(membervo);
		
		if(result ==1) {	
			loginuser.setName(name);
			HttpSession session = req.getSession();
			session.setAttribute("loginuser", loginuser);
			
			req.setAttribute("msg", "회원 정보 수정 성공!");
			req.setAttribute("loc", "memberModify.do?mnum="+mnum);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {
			req.setAttribute("msg", "회원 정보 수정 실패!");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}

		
		
	}

}
