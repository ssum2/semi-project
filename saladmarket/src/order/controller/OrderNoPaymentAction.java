package order.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class OrderNoPaymentAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MemberVO loginUser = super.getLoginUser(request);
		if (loginUser == null) return;

		String odrcode = request.getParameter("odrcode");
		//System.out.println("odrcode: "+odrcode);
		
		InterProductDAO pdao = new ProductDAO();
		List<HashMap<String, String>> orderList = pdao.selectOneOrder(odrcode);
		
		request.setAttribute("orderList", orderList);
		super.setRedirect(false); 
        super.setViewPage("/WEB-INF/store/order/orderNopayment.jsp"); 

	} // end of public void execute(HttpServletRequest request, HttpServletResponse response)

}
