package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class IdDuplicateCheckAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String method = req.getMethod();
		System.out.println("IdDuplicateCheckAction success 1/4; method: "+method);
		if("POST".equalsIgnoreCase(method)) {
			MemberDAO memberdao = new MemberDAO();
			String userid = req.getParameter("userid");
			
			System.out.println("IdDuplicateCheckAction success 2/4 userid: "+userid);
			int isUseUserid = memberdao.idDuplicateCheck(userid);
			
			System.out.println("IdDuplicateCheckAction success 3/4 isUseUserid: "+isUseUserid);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("isUseUserid", isUseUserid);
			
			String str_json = jsonObj.toString();	// 값이 하나뿐이기 때문에 바로 JSON 객체로 내보냄
			
			req.setAttribute("str_json", str_json);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/member/idCheckJSON.jsp");
		
		/*	req.setAttribute("isUseUserid", isUseUserid);
			res.getWriter().write(isUseUserid);
			res.getWriter().flush();
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/member/join.jsp");
		*/
			System.out.println("IdDuplicateCheckAction success 4/4");
		}
		else {
			req.setAttribute("msg", "비정상적인 경로로 들어왔습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
	}

}
