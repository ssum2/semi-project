package member.controller.mypage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import coupon.model.CouponDAO;
import coupon.model.CouponVO;
import coupon.model.InterCouponDAO;
import member.model.MemberVO;

public class CouponListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		
		
		if(loginuser==null) {
			String msg = "로그인 후 사용 가능합니다.";
        	String loc = "memberLogin.do";
        	req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {
			InterCouponDAO cpdao = new CouponDAO();
			
			String userid=loginuser.getUserid();
			List<CouponVO> myCouponList = cpdao.getMyCouponList(userid);
			
			req.setAttribute("myCouponList", myCouponList);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/store/mypage/couponList.jsp");
		}
		
	}

}
