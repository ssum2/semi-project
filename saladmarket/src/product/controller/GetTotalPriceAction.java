package product.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;

public class GetTotalPriceAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {


		String[] priceArr = req.getParameterValues("ordercurrentprice");
		String[] pointArr = req.getParameterValues("point");
		// String[] pnumArr = req.getParameterValues("pnum");
		String[] pqtyArr = req.getParameterValues("orderoqty");
		
		int totalprice = 0, totalpoint = 0;
		
		try {

			for(int i=0; i<priceArr.length; i++) {
				
				int pricenum = Integer.parseInt(priceArr[i]);
				int pointnum = Integer.parseInt(pointArr[i]);
				int pqtynum = Integer.parseInt(pqtyArr[i]);
				
				totalprice += pricenum*pqtynum;
				totalpoint += pointnum*pqtynum;
			}
				
		} catch (NumberFormatException e) {
			return;
		}
		
		JSONObject job = new JSONObject();
		
		job.put("totalprice", totalprice);
		job.put("totalpoint", totalpoint);
		
		String str_job = job.toString();
		
		req.setAttribute("str_job", str_job);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/JSON/totalpriceJSON.jsp");
		
	}

}
