package product.controller.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import common.controller.AbstractController;
import product.model.InterProductDAO;
import product.model.ProductDAO;
import product.model.ProductVO;

public class A_ProductRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) {
			InterProductDAO pdao = new ProductDAO();
			List<HashMap<String, String>> pacnameList = pdao.getPacnameList();
			List<HashMap<String, String>> sdnameList = pdao.getSdnameList();
			List<HashMap<String, String>> ctnameList = pdao.getCtnameList();
			List<HashMap<String, String>> stnameList = pdao.getStnameList();
			List<HashMap<String, String>> etnameList = pdao.getEtnameList();
			
			req.setAttribute("pacnameList", pacnameList);
			req.setAttribute("sdnameList", sdnameList);
			req.setAttribute("ctnameList", ctnameList);
			req.setAttribute("stnameList", stnameList);
			req.setAttribute("etnameList", etnameList);
			
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/a_productRegister.jsp");
		}
		else {
			
//			1. 첨부파일이 저장될 경로 설정
			HttpSession session = req.getSession();
			ServletContext svlCtx = session.getServletContext();
			String imagesDir =  svlCtx.getRealPath("/img");

//			2. 파일을 받아옴--> cos.jar lib 사용;
			MultipartRequest mtreq = null;	
			try {
				mtreq = new MultipartRequest(req, imagesDir, 10*1024*1024, "UTF-8", new DefaultFileRenamePolicy());

			} catch (IOException e) {	// 업로드 용량이 10MB가 넘어갔을 경우
				req.setAttribute("msg", "파일 첨부 용량은 10MB이하여야 합니다.");
				req.setAttribute("loc", "a_productRegister.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				return;
			
			}
			
//		3. 파일 업로드 후 상품정보(상품명, 정가, 제품설명...)를  DB jsp_product 테이블에 insert 
			ProductDAO pdao = new ProductDAO();

//			1) 새로운 제품 등록시 HTML form 에서 입력한 값들을 얻어오기 

			String fk_pacname = mtreq.getParameter("pacname");
			String fk_sdname = mtreq.getParameter("sdname");
			String fk_ctname = mtreq.getParameter("ctname");
			String fk_stname=mtreq.getParameter("stname");
			String fk_etname=mtreq.getParameter("etname");
			String pname=mtreq.getParameter("panme");
			String price=mtreq.getParameter("price");
			String saleprice=mtreq.getParameter("saleprice");
			String point=mtreq.getParameter("point");
			String pqty=mtreq.getParameter("pqty");
			String pcontents=mtreq.getParameter("pcontents");
			String pcompanyname=mtreq.getParameter("pcompanyname");
			String pexpiredate=mtreq.getParameter("pexpiredate");
			String allergy=mtreq.getParameter("allergy");
			String weight=mtreq.getParameter("weight");
			String titleimg=mtreq.getFilesystemName("titleimg");

//			2) 시퀀스 채번
			int pnum = pdao.getPnumOfProduct();	
			
			ProductVO pvo = new ProductVO();
			pvo.setPnum(Integer.toString(pnum));
			pvo.setFk_pacname(fk_pacname);
			pvo.setFk_sdname(fk_sdname);
			pvo.setFk_ctname(fk_ctname);
			pvo.setFk_stname(fk_stname);
			pvo.setFk_etname(fk_etname);
			pvo.setPname(pname);
			pvo.setPrice(Integer.parseInt(price));
			pvo.setSaleprice(Integer.parseInt(saleprice));
			pvo.setPoint(Integer.parseInt(point));
			pvo.setPqty(Integer.parseInt(pqty));
			pvo.setPcontents(pcontents);
			pvo.setPcompanyname(pcompanyname);
			pvo.setPexpiredate(pexpiredate);
			pvo.setAllergy(allergy);
			pvo.setWeight(Integer.parseInt(weight));
			pvo.setTitleimg(titleimg);
			
//		    3) 제품객체정보를 product 테이블에 insert
			int n = pdao.productInsert(pvo);
			int m = 0;
			
//			4) 제품정보에 추가 이미지파일 정보를 insert
			String str_attachCount = mtreq.getParameter("attachCount"); // hidden input태그 안에 있는 값(파일 개수)
			if(!"".equals(str_attachCount)) {
				int attachCount = Integer.parseInt(str_attachCount);
				for(int i=0; i<attachCount; i++) {
					String attachFilename = mtreq.getFilesystemName("attach"+i);
					m = pdao.product_images_Insert(pnum, attachFilename);
					if(m==0) break;
				}// end of for
				
			} // end of if
			
			String msg = "";
			String loc = "";
			if(n*m==1) {
				msg = "제품 등록 성공!";
				loc = req.getContextPath()+"a_productList.do";
			}
			else {
				msg = "제품 등록 실패!";
				loc = req.getContextPath()+"/a_productRegister.do";
			}
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
	}

}
