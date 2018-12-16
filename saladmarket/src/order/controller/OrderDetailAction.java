package order.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import product.model.ProductDAO;

public class OrderDetailAction extends AbstractController{

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		
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
			String username=super.getLoginUser(req).getName();
		    String odrdnum=req.getParameter("odrdnum");
			
		    ProductDAO pdao=new ProductDAO();
			
		    int totalprice = pdao.getTotalprice(odrdnum); //토탈프라이스를 가져오는 메소드 
			HashMap<String,String> ordermap= pdao.getOneOrderDetail(odrdnum);  //주문 상세정보를 가져오기 
			
			if(ordermap == null) {
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/store/mypage/orderList.jsp");
			}
		
			req.setAttribute("username", username);
			req.setAttribute("ordermap", ordermap);
			req.setAttribute("totalprice", totalprice);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/mypage/orderDetail.jsp");
			
		}
	}
	
}
