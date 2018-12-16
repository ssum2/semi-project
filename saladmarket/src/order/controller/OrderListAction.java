package order.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class OrderListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser != null) {
			String userid = loginuser.getUserid();
			InterProductDAO pdao = new ProductDAO();
			
			List<HashMap<String, String>> orderList = pdao.getOrderListByUserid(userid);
			req.setAttribute("orderList", orderList);
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/mypage/orderList.jsp");
		}
		else {
			String msg = "로그인 후 사용 가능합니다.";
        	String loc = "memberLogin.do";
        	req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		
	}

}
