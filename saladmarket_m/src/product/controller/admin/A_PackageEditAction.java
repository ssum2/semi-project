package product.controller.admin;

import java.io.IOException;

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

public class A_PackageEditAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();
		ServletContext svlCtx = session.getServletContext();
		String imagesDir =  svlCtx.getRealPath("/img");

		MultipartRequest mtreq = null;	
		try {
			mtreq = new MultipartRequest(req, imagesDir, 10*1024*1024, "UTF-8", new DefaultFileRenamePolicy());

		} catch (IOException e) {
			req.setAttribute("msg", "파일 첨부 용량은 10MB이하여야 합니다.");
			req.setAttribute("loc", "a_packageRegister.do");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		
		}
		
		InterProductDAO pdao = new ProductDAO();

		String pacnum = mtreq.getParameter("pacnum");	
		String pacname = mtreq.getParameter("pacname");
		String pacimage = mtreq.getFilesystemName("pacimage");
		if(pacimage==null) {
			pacimage="";
		}
		String paccontents = mtreq.getParameter("paccontents");
		
		PackageVO pacvo = new PackageVO();
		pacvo.setPacnum(pacnum);
		pacvo.setPacname(pacname);
		pacvo.setPacimage(pacimage);
		pacvo.setPaccontents(paccontents);
		
		int n = pdao.updatePackage(pacvo);

		String msg = "";
		String loc = "";
		if(n==1) {
			msg = "패키지 수정 성공!";
			loc = req.getContextPath()+"/a_packageDetail.do?pacnum="+pacnum;
		}
		else {
			msg = "패키지 등록 실패!";
			loc = req.getContextPath()+"/a_packageDetail.do?pacnum="+pacnum;
		}
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/msg.jsp");
	}

}
