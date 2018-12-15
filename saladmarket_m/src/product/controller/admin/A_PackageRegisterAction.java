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
import product.model.PackageVO;
import product.model.ProductDAO;
import product.model.ProductVO;

public class A_PackageRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) {
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/a_packageRegister.jsp");
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
				req.setAttribute("loc", "a_packageRegister.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				return;
			
			}
			
//		3. 파일 업로드 후 상품정보(상품명, 정가, 제품설명...)를  DB jsp_product 테이블에 insert 
			InterProductDAO pdao = new ProductDAO();

//			1) 새로운 제품 등록시 HTML form 에서 입력한 값들을 얻어오기 
			String pacname = mtreq.getParameter("pacname");
			String pacimage = mtreq.getFilesystemName("pacimage");
			String paccontents = mtreq.getParameter("paccontents");
			
			
//			2) 시퀀스 채번
			int pacnum = pdao.getPacnum();	
			
			PackageVO pacvo = new PackageVO();
			pacvo.setPacnum(Integer.toString(pacnum));
			pacvo.setPacname(pacname);
			pacvo.setPacimage(pacimage);
			pacvo.setPaccontents(paccontents);
			
			
//		    3) 제품객체정보를 product 테이블에 insert
			int n = pdao.insertPackage(pacvo);

			String msg = "";
			String loc = "";
			if(n==1) {
				msg = "패키지 등록 성공!";
				loc = req.getContextPath()+"/a_packageList.do";
				String script1="window.opener.location.reload();\n" + "window.close();";
				req.setAttribute("script1", script1);
			}
			else {
				msg = "패키지 등록 실패!";
				loc = req.getContextPath()+"/a_packageRegister.do";
			}
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
	}

}
