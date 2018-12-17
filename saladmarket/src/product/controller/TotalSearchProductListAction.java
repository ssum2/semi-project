package product.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;
import util.MyUtil;

public class TotalSearchProductListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		InterProductDAO pdao = new ProductDAO();
		
		String str_sizePerPage = req.getParameter("sizePerPage");
		int sizePerPage = 0; 
		try {
			sizePerPage = Integer.parseInt(str_sizePerPage);
		} catch (NumberFormatException e) {
			sizePerPage = 4;
		}
		
		String totalSearchWord = req.getParameter("totalSearchWord");
				
		if(totalSearchWord == null) {
			totalSearchWord = "";
		}
		
		String sort = req.getParameter("sort");
		
		if(sort == null) {
			sort = "pacname";
		}
		if(!("pacname".equalsIgnoreCase(sort)||"plike".equalsIgnoreCase(sort)||"pdate".equalsIgnoreCase(sort)||"saleprice".equalsIgnoreCase(sort))) {
			sort = "pacname";
		}
		
		int totalCount = 0;
		totalCount = pdao.getTotalSearchCount(totalSearchWord);
		
		int totalPage = (int)Math.ceil((double)totalCount / sizePerPage);
		
		String str_currentShowPageNo = req.getParameter("currentShowPageNo");
		int currentShowPageNo = 0;
		
		if(str_currentShowPageNo == null) { // 처음 검색하고 들어왔을 때
			currentShowPageNo = 1;
		}
		else { // 사용자가 보고자 하는 페이지번호를 설정한 경우(목록보기를 통해 돌아왔을 때)
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				if(currentShowPageNo < 1 || currentShowPageNo > totalPage ) {
					currentShowPageNo = 1;
				}
			} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
		// 4. 검색 조건에 맞는 상품 정보를 select한 결과물을 가져와서 담는다.
		
		List<HashMap<String,Object>> productList = null;
		productList = pdao.getSearchProduct(sizePerPage, currentShowPageNo, totalSearchWord, sort);
		
		// 되돌아갈 URL
		String currentURL = MyUtil.getCurrentURL(req);
					
		// 5. 페이지 바 만들기
		String url = "totalSearchProductList.do";
		int blockSize = 3;
		String pageBar = MyUtil.getSearchPageBar(url, currentShowPageNo, sizePerPage, totalPage, blockSize, totalSearchWord);
		
		// 6. 지금까지 작성한 데이터값들을 VIEW단으로 넘긴다
		
		req.setAttribute("sizePerPage", sizePerPage);
		req.setAttribute("totalSearchWord", totalSearchWord);
		req.setAttribute("productList", productList);
		req.setAttribute("currentURL", currentURL);
		req.setAttribute("pageBar", pageBar);
		req.setAttribute("currentShowPageNo", currentShowPageNo);
		req.setAttribute("totalCount", totalCount);
		req.setAttribute("totalPage", totalPage);
		
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/store/product/totalSearchProductList.jsp");

	}

}
