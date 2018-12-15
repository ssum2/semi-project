package member.controller.mypage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;

public class PwdCheckAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String userid = req.getParameter("userid");
		String pwd = req.getParameter("pwd");
		
		
		InterMemberDAO mdao = new MemberDAO();
		int mnum = mdao.memberPwdCheck(userid, pwd);
		
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("mnum", mnum);
		
	
		String str_jsonObj = jsonObj.toString();
				
		req.setAttribute("str_jsonObj", str_jsonObj);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/JSON/pwdCheckJSON.jsp");

		
	}

}
