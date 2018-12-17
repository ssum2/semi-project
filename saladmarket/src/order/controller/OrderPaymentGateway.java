package order.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;

public class OrderPaymentGateway extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		MemberVO loginUser = super.getLoginUser(request);
		if (loginUser == null)
			return;

		  String userid = request.getParameter("userid"); 
	      String paymoney = request.getParameter("paymoney");
	      //System.out.println("userid : " + userid);
	      //System.out.println("paymoney : " + paymoney);
		
		if (!String.valueOf(loginUser.getUserid()).equals(userid)) {
			// 로그인을 했지만 다른 사용자의 코인 충전은 불가하도록 한다.
			request.setAttribute("msg", "비정상적인 경로로 들어왔습니다!");
			request.setAttribute("loc", "javascript:history.back();");

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;

		} else {

			request.setAttribute("paymoney", paymoney);
			request.setAttribute("userid", userid);
			request.setAttribute("name", loginUser.getName());

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/order/paymentGateway.jsp");
		} // end of if~else

	} // end of

}
