package admin.model;

public class AdminVO {
	private String adminid;
	private String adminpw;
	
	
	public AdminVO() {}
	public AdminVO(String adminid, String adminpw) {
		this.adminid = adminid;
		this.adminpw = adminpw;
	}
	public String getAdminid() {
		return adminid;
	}
	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}
	public String getAdminpw() {
		return adminpw;
	}
	public void setAdminpw(String adminpw) {
		this.adminpw = adminpw;
	}
	
}
