package util;

/*import java.util.HashMap;
import javax.servlet.http.HttpSession;
import member.model.*;*/

import javax.servlet.http.HttpServletRequest;

public class MyUtil {
//	#���� URL�ּ�(���������ͱ��� ����)�� �˷��ִ� �޼ҵ�
	public static String getCurrentURL(HttpServletRequest request) {
		String currentURL = request.getRequestURL().toString(); // ?������ �ּ�
		String queryString = request.getQueryString(); // ? ������ �������ּ�
		
		currentURL += "?"+queryString;
		
		String ctxPath = request.getContextPath(); // �ش� jsp������ �ִ� ���� ��� (/MyWeb)
		int beginIndex = currentURL.indexOf(ctxPath) + ctxPath.length();
		// currentURL���� /MyWeb�� �����ϴ� �κ� -> 21 + /MyWeb�� ���� -> 6 ==> 27 -> /MyWeb ���� ���۰�
		
		currentURL = currentURL.substring(beginIndex+1); // member/memberList.jsp~~
		
		
		return currentURL;
	} // end of getCurrentURL(HttpServletRequest request);
	
	
/*//	#���� ���ǿ� ��ü�� �ִ��� ������ �˷��ִ� �޼ҵ� (�α��� ���� �˻�) --> return HashMap
	public static HashMap<String, Object> checkLoginUser(HttpServletRequest req) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser == null) {
			String msg = "�α��� �� ��� �����մϴ�.";
        	String loc = "javascript:history.back();";
        	
        	map.put("msg", msg);
        	map.put("loc", loc);
        	map.put("loginuser", null);
		}
		else {
			map.put("loginuser", loginuser);
		}
		return map;
	} // end of HashMap<String, Object> checkLoginUser(HttpServletRequest req)
*/

//	#�˻���, ��¥����(period)�� ���Ե� �������� �����
	public static String getSearchPageBar(String url, int currentShowPageNo, int sizePerPage, int totalPage,
			int blockSize, String searchType, String searchWord, int period) {
		String pageBar ="";
		
		int pageNo = 1;
		int loop = 1;
		
		// ���������� ����
		pageNo = ( (currentShowPageNo - 1)/blockSize)*blockSize +1;
		
		if(pageNo!=1) {
			pageBar += "<a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord
						+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"&period="+period+"'>[����]</a>&nbsp";
		}				
		while(!(pageNo > totalPage || loop > blockSize)) {
			if(pageNo==currentShowPageNo) {
				pageBar += "&nbsp;<span style='color: #B43846; font-size: 13pt; font-weight: bold; text-decoration: underline;'>"+pageNo+"</span>";
			}
			else if(pageNo!=currentShowPageNo) {
				pageBar += "&nbsp;<a href='"+url+"?sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo
							+"&searchType="+searchType+"&searchWord="+searchWord+"&period="+period+"'>"+pageNo+"</a>";
			}
			pageNo++;
			loop++;
		}
		if(pageNo <= totalPage) {
			pageBar += "<a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord
						+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"&period="+period+"'>[����]</a>&nbsp";
		}
		
		return pageBar;
	}



}
