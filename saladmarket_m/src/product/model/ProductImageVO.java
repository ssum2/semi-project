package product.model;

public class ProductImageVO {
	private String pimgnum;		// 상품이미지번호
	private String pimgfilename; // 상품이미지파일명 
	private String fk_pnum;		// 상품번호
	
	
	public ProductImageVO() {}
	
	public ProductImageVO(String pimgnum, String pimgfilename, String fk_pnum) {
		super();
		this.pimgnum = pimgnum;
		this.pimgfilename = pimgfilename;
		this.fk_pnum = fk_pnum;
	}
	public ProductImageVO(String pimgfilename, String fk_pnum) {
		this.pimgfilename = pimgfilename;
		this.fk_pnum = fk_pnum;
	}

	public String getPimgnum() {
		return pimgnum;
	}
	public void setPimgnum(String pimgnum) {
		this.pimgnum = pimgnum;
	}
	public String getPimgfilename() {
		return pimgfilename;
	}
	public void setPimgfilename(String pimgfilename) {
		this.pimgfilename = pimgfilename;
	}
	public String getFk_pnum() {
		return fk_pnum;
	}
	public void setFk_pnum(String fk_pnum) {
		this.fk_pnum = fk_pnum;
	}
	
	
}
