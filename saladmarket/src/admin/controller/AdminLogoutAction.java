package admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;

public class AdminLogoutAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		#�α׾ƿ� ó��
		HttpSession session = req.getSession();
		session.removeAttribute("admin");
//		>> session�� ����Ǿ��ִ� atrribute�� key���� ������ �ش� ��ü�� ����
//		�α����� ��ü�� �����Ͽ� �ش� ��ü�� null�� ���� ----> �α��� ���� ���¿� ��������
		super.setRedirect(true); // ������ �̵�
		super.setViewPage("adminLogin.do");

	}

}
