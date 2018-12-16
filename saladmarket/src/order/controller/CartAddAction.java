package order.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class CartAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String goBackURL = req.getParameter("goBackURL");

//		2) 로그인 유무 검사; 로그인하지 않은 상태에서 장바구니 담기를 시도한 경우에 다시 그 제품페이지로 돌아가야함 --> goBackURL
		MemberVO loginuser = super.getLoginUser(req);
		if (loginuser == null) {
//				#돌아갈 페이지를 파라미터로 받아와서 세션에 저장
			HttpSession session = req.getSession();
			session.setAttribute("returnPage", goBackURL);
			return;
		} else {
			String[] pnumArr = req.getParameterValues("orderpnum");
			String[] oqtyArr = req.getParameterValues("orderoqty");

			InterProductDAO pdao = new ProductDAO();

			int result = pdao.addCart(loginuser.getUserid(), pnumArr, oqtyArr);
//				-> result==1; 성공 / result==0; 실패
			if (result == 1) {
				req.setAttribute("msg", "장바구니 담기 성공!");
				req.setAttribute("loc", "cart.do");

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			} else {
				req.setAttribute("msg", "장바구니 담기 실패!");
				req.setAttribute("loc", goBackURL);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}

		}

	}

}
