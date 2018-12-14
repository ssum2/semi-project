package product.controller.admin;

import java.io.IOException;
import java.util.HashMap;

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

public class A_EventEditAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) {
			
			String etnum = req.getParameter("etnum");
			
			InterProductDAO pdao = new ProductDAO();
			
			HashMap<String, String> map = pdao.getOneEventTag(etnum);
			
			req.setAttribute("map", map);
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/admin/a_eventEdit.jsp");
		}
		else {
			HttpSession session = req.getSession();
			ServletContext svlCtx = session.getServletContext();
			String imagesDir =  svlCtx.getRealPath("/img");

			MultipartRequest mtreq = null;	
			try {
				mtreq = new MultipartRequest(req, imagesDir, 10*1024*1024, "UTF-8", new DefaultFileRenamePolicy());

			} catch (IOException e) {	
				req.setAttribute("msg", "파일 첨부 용량은 10MB이하여야 합니다.");
				req.setAttribute("loc", "a_eventEdit.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				return;
			
			}
			
//			3. 파일 업로드 후 상품정보(상품명, 정가, 제품설명...)를  DB jsp_product 테이블에 insert 
			InterProductDAO pdao = new ProductDAO();

			String etname = mtreq.getParameter("etname");
			String etnum = mtreq.getParameter("etnum");
			String etimagefilename = mtreq.getFilesystemName("etimagefilename");
			
			if(etimagefilename==null) {
				etimagefilename ="";
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("etname", etname);
			map.put("etnum", etnum);
			map.put("etimagefilename", etimagefilename);

			int n = pdao.updateEventTag(map);
			
			String msg = "";
			String loc = "";
			if(n==1) {
				msg = "수정 성공";
				loc = req.getContextPath()+"/a_eventList.do";
				String script1="window.opener.location.reload();\n" + "window.close();";
				req.setAttribute("script1", script1);
			}
			else {
				msg = "수정 실패";
				loc = req.getContextPath()+"/a_eventEdit.do?etnum="+etnum;
			}
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}
			
	}
}