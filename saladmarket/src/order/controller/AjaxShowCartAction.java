package order.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;
import member.model.MemberVO;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class AjaxShowCartAction extends AbstractController {

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
	         
	         InterProductDAO productdao = new ProductDAO();
	         
	         List<HashMap<String, String>> cartList = productdao.getCartList(userid);
	         JSONArray jsonArr = new JSONArray();
	         if(cartList != null) {
	            for( HashMap<String, String> map : cartList) {
	               JSONObject jsonObj = new JSONObject();
	               jsonObj.put("cartno", map.get("cartno"));
	               jsonObj.put("fk_userid", map.get("fk_userid"));
	               jsonObj.put("fk_pnum", map.get("fk_pnum"));
	               jsonObj.put("oqty", map.get("oqty"));
	               jsonObj.put("status", map.get("status"));
	               jsonObj.put("pname", map.get("pname"));
	               jsonObj.put("price", map.get("price"));
	               jsonObj.put("saleprice", map.get("saleprice"));
	               jsonObj.put("titleimg", map.get("titleimg"));
	               jsonObj.put("pacname", map.get("pacname"));
	               jsonObj.put("totalPrice", Integer.parseInt(map.get("saleprice"))* Integer.parseInt(map.get("oqty")) );
	               jsonObj.put("totalPoint", Integer.parseInt(map.get("point"))* Integer.parseInt(map.get("oqty")) );
	               jsonArr.add(jsonObj);
	            }
	         }
	         String str_jsonArr = jsonArr.toString();
	         
	         req.setAttribute("str_jsonArr", str_jsonArr);
	         
	         super.setRedirect(false);
	         super.setViewPage("/WEB-INF/store/JSON/ajaxShowCart.jsp");
		
		} 

	} // execute //////////////////////////////////////////////////////////////////////////////////////////

}
