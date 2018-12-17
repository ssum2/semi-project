package product.controller;

import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;

public class LikeAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// *** 로그인 유무 검사하기 *** // 
		String userid = req.getParameter("userid");
		String pnum = req.getParameter("pnum");
		String pacnum = req.getParameter("pacnum");
		String len = req.getParameter("len");
		
		System.out.println("userid : "+userid);
		System.out.println("pnum : "+pnum);
		System.out.println("pacnum : "+pacnum);
		System.out.println("len :"+len);
		
		
		JSONObject jsonObj = new JSONObject();
		
		if(userid == null || "".equals(userid)) { // 웹에서 받는 것이기 때문에 null 일수도 있고, "" 일수도 있다.
			jsonObj.put("msg", "로그인을 하신 후 \n 좋아요를 클릭하세요");
			
		}
		else {
			// 로그인을 한 경우라면 
			
			InterProductDAO pdao = new ProductDAO();
			
			if(pnum == null || "".equals(pnum)) {

				try {
					
					int n = pdao.insertLikebypacnum(userid, pacnum, len);
					
					if(n==1) {
						jsonObj.put("msg", "해당 제품에 \n 좋아요를 클릭하셨습니다.");	
					}
					else {
						jsonObj.put("msg", "좋아요 오류!!");
					}
					
				} catch (SQLIntegrityConstraintViolationException e) {
					jsonObj.put("msg", "이미 \n 좋아요를 클릭하셨습니다.");	
				}
			
			}
			else {
				
				try {
					
					int n = pdao.insertLikebypnum(userid, pnum);
					
					if(n==1) {
						jsonObj.put("msg", "해당 제품에 \n 좋아요를 클릭하셨습니다.");	
					}
					else {
						jsonObj.put("msg", "좋아요 오류!!");	
					}
					
				} catch (SQLIntegrityConstraintViolationException e) {
					jsonObj.put("msg", "이미 \n 좋아요를 클릭하셨습니다.");	
				}
				
				
			}
				
			
		}
		
		String str_jsonObj = jsonObj.toString();
		
		req.setAttribute("str_jsonObj", str_jsonObj);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/JSON/likeAddJSON.jsp");
		
	
		
	}

}
