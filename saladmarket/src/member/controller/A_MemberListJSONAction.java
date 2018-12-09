package member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;

public class A_MemberListJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String lvnum = req.getParameter("lvnum");
		String searchType = req.getParameter("searchType");
		String searchWord = req.getParameter("searchWord");
		String str_currentShowPageNo = req.getParameter("currentShowPageNo");
		if(str_currentShowPageNo == null || "".equals(str_currentShowPageNo)) {
			str_currentShowPageNo = "1";
		}
		int	currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
		int sizePerPage = 10;
		
		InterMemberDAO mdao = new MemberDAO();
				
		List<MemberVO> memberList = mdao.getSearchMemberList(sizePerPage, currentShowPageNo, lvnum, searchType, searchWord);  
		
		JSONArray jsonArray = new JSONArray();
		
		if(memberList != null && memberList.size() > 0) {
			for(MemberVO mvo : memberList) {
				JSONObject jsonObj = new JSONObject();
				// JSONObject 는 JSON형태(키:값)의 데이터를 관리해 주는 클래스이다.
			    jsonObj.put("mnum", mvo.getMnum());
			    jsonObj.put("userid", mvo.getUserid());
			    jsonObj.put("name", mvo.getName());
			    jsonObj.put("email", mvo.getEmail());
			    jsonObj.put("phone", mvo.getShowPhone());
			    jsonObj.put("summoney", mvo.getSummoney());
			    jsonObj.put("lvname", mvo.getLvnameByLvnum());
			    jsonObj.put("statusname", mvo.getStatusByStatus());
			    jsonObj.put("status", mvo.getStatus());
			    jsonArray.add(jsonObj);
			}
		}
		
		String str_jsonArray = jsonArray.toString();
		
		req.setAttribute("str_jsonArray", str_jsonArray);

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/admin/JSON/a_memberListJSON.jsp");
			
	}

}
