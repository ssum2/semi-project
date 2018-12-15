package product.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class A_DeleteEventTagAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String etnum = req.getParameter("etnum");
		
		InterProductDAO pdao = new ProductDAO();
		
		int result = pdao.deleteEventTagByEtnum(etnum);
		
		String msg = "";
		String loc = "";
		if(result==1) {
			msg = "삭제 완료";
			loc = req.getContextPath()+"/a_eventList.do";
		}
		else {
			msg = "삭제 실패";
			loc = req.getContextPath()+"/a_eventList.do";
		}
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/msg.jsp");
		
	}


}
