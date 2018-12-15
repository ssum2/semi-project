package product.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;
import product.model.ProductDAO;

public class CartAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
			
		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
		// 로그인하지 않았을 경우
		if(loginuser == null) {
			String msg = "로그인부터 하세요!";
			String loc = "memberLogin.do";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		// 로그인 했을 경우 
		} else if(loginuser != null) {
			String userid = loginuser.getUserid();
			
			System.out.println(userid);
			
			ProductDAO productdao = new ProductDAO();
			
			List<HashMap<String, String>> cartList = productdao.getCartList(userid);
			
			req.setAttribute("cartList", cartList);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/order/cart.jsp");
		
		} 

	} // execute()
}
