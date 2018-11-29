package common.controller;

public abstract class AbstractController implements Command {
	// �߻�Ŭ����
	private boolean isRedirect = false;
	/*	#������ ���� ��Ģ
	 	View ������(.jsp ������)�� sendRedirect ������� �̵� �� ��; 
	 		isRedirect = true
	 	View ������(.jsp ������)�� forward ������� �̵� �� ��; 
	 		isRedirect = false
	 */
	
	private String viewPage; // view���� ����� page

	public boolean isRedirect() {
		return isRedirect;
		// >> returnŸ���� boolean�� ��� get�� �ƴ϶� is~~
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

}
