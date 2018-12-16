package order.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.InterMemberDAO;
import member.model.MemberDAO;
import member.model.MemberVO;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class OrderAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		MemberVO loginUser = super.getLoginUser(request);
		if (loginUser == null) return;

		String method = request.getMethod();

		if (!"POST".equals(method)) {
			// GET 방식이라면 
			String msg = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back();";

			request.setAttribute("msg", msg);
			request.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");

			return;
		} else {
			String[] pnumArr = request.getParameterValues("pnum"); 
			String[] salepriceArr = request.getParameterValues("currentprice");
			String[] oqtyArr = request.getParameterValues("oqty");
			String[] cartnoArr = request.getParameterValues("cartno");
			String[] pnameArr = request.getParameterValues("pname");
			
			String coupon = request.getParameter("coupon");
			int sumtotalprice = Integer.parseInt(request.getParameter("sumtotalprice"));
			int sumtotalpoint = Integer.parseInt(request.getParameter("sumtotalpoint"));
			
			if(pnumArr == null || salepriceArr == null || oqtyArr == null || cartnoArr == null) {
				String msg = "비정상적인 경로로 들어왔습니다.";
				String loc = "javascript:history.back();";

				request.setAttribute("msg", msg);
				request.setAttribute("loc", loc);

				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");

				return;
			}

			System.out.println("coupon : "+coupon); // 쿠폰 널처리!!
			
             //  배송지 및 정보를 나타내기 위한
			InterMemberDAO mdao = new MemberDAO();
			MemberVO mvo = mdao.getOneMemberByUserid(loginUser.getUserid());
			
			request.setAttribute("mvo", mvo);
			request.setAttribute("pnumArr", pnumArr);
			request.setAttribute("salepriceArr", salepriceArr);
			request.setAttribute("oqtyArr", oqtyArr);
			request.setAttribute("cartnoArr", cartnoArr);
			//System.out.println("확인용 : "+cartnoArr[0]+", "+cartnoArr[1]+", "+cartnoArr[2]);
			request.setAttribute("pnameArr", pnameArr);
			//request.setAttribute("sumtotalprice", sumtotalprice);
			request.setAttribute("sumtotalpoint", sumtotalpoint);
			
			HashMap<String, String> couponMap = null;
			if(coupon != null || !"".equals(coupon)) {
				InterProductDAO pdao = new ProductDAO();
				couponMap = pdao.selectOneCoupon(coupon, loginUser.getUserid());
				
				if(sumtotalprice >= Integer.parseInt(couponMap.get("cpusemoney"))) {
					
					double d_discountMoney = (sumtotalprice * (Integer.parseInt(couponMap.get("discountper")) / 100.0));
					int discountMoney = (int)Math.floor(d_discountMoney);
					//System.out.println("discountMoney : "+discountMoney);
					if(discountMoney > Integer.parseInt(couponMap.get("cpuselimit"))) {
						discountMoney = Integer.parseInt(couponMap.get("cpuselimit"));
					} // end of if
					
					sumtotalprice = sumtotalprice - discountMoney;
				} // end of if
			} // end of if
			//System.out.println("sumtotalprice : "+sumtotalprice);
			String odrcode = getOdrcode();
			request.setAttribute("odrcode", odrcode);
			request.setAttribute("couponMap", couponMap);
			request.setAttribute("sumtotalprice", sumtotalprice);
		}

		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/order/jumun.jsp");

	} // end of public void execute(HttpServletRequest request, HttpServletResponse
		// response)

	private String getOdrcode() throws SQLException { 

        // === 주문코드 생성하기 === // 
        // 주문코드 형식 : s+날짜+sequence ==> s20180430-1 

        // 날자 생성 
        Date now = new Date(); 
        SimpleDateFormat smdatefm = new SimpleDateFormat("yyyyMMdd"); 
        String today = smdatefm.format(now); 
         
        InterProductDAO pdao = new ProductDAO(); 

        int seq = pdao.getSeq_jsp_order(); // pdao.getSeq_jsp_order();는 시퀀스 seq_jsp_order 값을 채번해오는 것 

        return "s"+today+"-"+seq; 

    } // end of private String getOdrcode() 
}
