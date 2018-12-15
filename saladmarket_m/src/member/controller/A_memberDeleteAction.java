package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;

public class A_memberDeleteAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		InterMemberDAO mdao = new MemberDAO();
		String mnum = req.getParameter("mnum");
		int result = mdao.deleteMember(mnum);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("result", result);
		
		String str_jsonObj = jsonObj.toString();
		
		req.setAttribute("str_jsonObj", str_jsonObj);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/a_memberDeleteJSON.jsp");

	}

}
