package product.model;



public class PackageVO {
	private String pacnum; 			// 상품패키지번호 
	private String pacname; 		// 상품패키지명 
	private String paccontents; 	// 패키지내용 
	private String pacimage;		// 상품패키지 대표 이미지명
	
//	private ProductVO items;		// 패키지에 딸려있는 물품 객체

	private String cnt; // 물품갯수
	
	public PackageVO() {}

	public PackageVO(String pacnum, String pacname, String paccontents, String pacimage) {
		
		this.pacnum = pacnum;
		this.pacname = pacname;
		this.paccontents = paccontents;
		this.pacimage = pacimage;
		
	}

	
	public String getPacnum() {
		return pacnum;
	}

	public void setPacnum(String pacnum) {
		this.pacnum = pacnum;
	}

	public String getPacname() {
		return pacname;
	}

	public void setPacname(String pacname) {
		this.pacname = pacname;
	}

	public String getPaccontents() {
		return paccontents;
	}

	public void setPaccontents(String paccontents) {
		this.paccontents = paccontents;
	}

	public String getPacimage() {
		return pacimage;
	}

	public void setPacimage(String pacimage) {
		this.pacimage = pacimage;
	}

	public String getCnt() {
		return cnt;
	}

	public void setCnt(String cnt) {
		this.cnt = cnt;
	}

	
	
	
	
}
