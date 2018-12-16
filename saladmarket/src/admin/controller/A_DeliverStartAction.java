package admin.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_DeliverStartAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		String odrdnum = req.getParameter("odrdnum");
		if(odrdnum != null) {
			InterProductDAO ipdao = new ProductDAO();
			// **** 배송시작으로 변경시켜주는 추상 메소드 **** //
			String invoice = makeInvoiceNumber();
			int n = ipdao.changeDeliverStart(odrdnum, invoice);
			
			if(n == 1) {
				req.setAttribute("msg", "배송시작으로 변경되었습니다!");
				req.setAttribute("loc", "a_orderList.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}//end of if----------
		}
		else {
			req.setAttribute("msg", "변경실패!");
			req.setAttribute("loc", "a_orderList.do");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}//end of if~else------
		
	}//end of execute(HttpServletRequest req, HttpServletResponse res)--------
	
	
	 public String makeInvoiceNumber() {
		int certNumLength = 10;

        Random random = new Random(System.currentTimeMillis());
        
        int range = (int)Math.pow(10,certNumLength);
        int trim = (int)Math.pow(10, certNumLength-1);
        int result = random.nextInt(range)+trim;
         
        if(result>range){
            result = result - trim;
        }
        if(result<0) {
        	result = result*-1;
        }
        
        return String.valueOf(result);
	 }

}
