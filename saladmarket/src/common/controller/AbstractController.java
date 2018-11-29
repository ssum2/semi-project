package common.controller;

public abstract class AbstractController implements Command {
	// 추상클래스
	private boolean isRedirect = false;
	/*	#개발자 지정 규칙
	 	View 페이지(.jsp 페이지)에 sendRedirect 방법으로 이동 할 때; 
	 		isRedirect = true
	 	View 페이지(.jsp 페이지)에 forward 방법으로 이동 할 때; 
	 		isRedirect = false
	 */
	
	private String viewPage; // view에서 사용할 page

	public boolean isRedirect() {
		return isRedirect;
		// >> return타입이 boolean인 경우 get이 아니라 is~~
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
