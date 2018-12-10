package product.model;

public class ProductVO {
	private String pnum;  // 상품번호 
	private String fk_pacname;	//상품패키지명
	private String fk_sdname; // 소분류상세명
	private String fk_ldname; // 대분류상세명
	private String fk_ctname; // 카테고리태그명 
	private String fk_stname; // 카테고리태그명 
	private String fk_etname; // 카테고리태그명 
	private String pname; // 카테고리태그명 
	private int price; // 원가 
	private int saleprice; // 판매가 
	private int point; // 포인트 
	private int pqty; // 재고량 
	private String pcontents; // 상품설명 
	private String pcompanyname; // 상품회사명 
	private String pexpiredate; // 유통기한 
	private String allergy; // 알레르기정보 
	private int weight; // 용량 
	private int salecount; // 판매량
	private int plike; // 상품좋아요
	private String pdate; // 상품등록일자
	private String titleimg; // 대표이미지
	
	private ProductImageVO images;	// 상세이미지
	private PackageVO pac;		// 패키지
	
	private int totalPrice;   // 주문량 * 제품판매가(할인해서 팔 것이므로)
	private int totalPoint;   // 주문량 * 포인트점수
	
	
	public ProductVO() {}
	
	public ProductVO(String pnum, String fk_pacname, String fk_sdname, String fk_ctname, String fk_stname,
			String fk_etname, String pname, int price, int saleprice, int point, int pqty, String pcontents,
			String pcompanyname, String pexpiredate, String allergy, int weight, int salecount, int plike,
			String pdate, String titleimg) {
		
		this.pnum = pnum;
		this.fk_pacname = fk_pacname;
		this.fk_sdname = fk_sdname;
		this.fk_ctname = fk_ctname;
		this.fk_stname = fk_stname;
		this.fk_etname = fk_etname;
		this.pname = pname;
		this.price = price;
		this.saleprice = saleprice;
		this.point = point;
		this.pqty = pqty;
		this.pcontents = pcontents;
		this.pcompanyname = pcompanyname;
		this.pexpiredate = pexpiredate;
		this.allergy = allergy;
		this.weight = weight;
		this.salecount = salecount;
		this.plike = plike;
		this.pdate = pdate;
		this.titleimg=titleimg;
	}

//	#상품이미지 정보까지 있는 생성자
	public ProductVO(String pnum, String fk_pacname, String fk_sdname, String fk_ctname, String fk_stname,
			String fk_etname, String pname, int price, int saleprice, int point, int pqty, String pcontents,
			String pcompanyname, String pexpiredate, String allergy, int weight, int salecount, int plike, String pdate, String titleimg,
			ProductImageVO images, PackageVO pac) {
		
		this.pnum = pnum;
		this.fk_pacname = fk_pacname;
		this.fk_sdname = fk_sdname;
		this.fk_ctname = fk_ctname;
		this.fk_stname = fk_stname;
		this.fk_etname = fk_etname;
		this.pname = pname;
		this.price = price;
		this.saleprice = saleprice;
		this.point = point;
		this.pqty = pqty;
		this.pcontents = pcontents;
		this.pcompanyname = pcompanyname;
		this.pexpiredate = pexpiredate;
		this.allergy = allergy;
		this.weight = weight;
		this.salecount = salecount;
		this.plike = plike;
		this.pdate = pdate;
		this.titleimg= titleimg;
		this.images = images;
		this.pac=pac;
	}

	public String getPnum() {
		return pnum;
	}

	public void setPnum(String pnum) {
		this.pnum = pnum;
	}

	public String getFk_pacname() {
		return fk_pacname;
	}

	public void setFk_pacname(String fk_pacname) {
		this.fk_pacname = fk_pacname;
	}

	public String getFk_sdname() {
		return fk_sdname;
	}

	public void setFk_sdname(String fk_sdname) {
		this.fk_sdname = fk_sdname;
	}
	
	public String getFk_ldname() {
		return fk_ldname;
	}

	public void setFk_ldname(String fk_ldname) {
		this.fk_ldname = fk_ldname;
	}

	public String getFk_ctname() {
		return fk_ctname;
	}

	public void setFk_ctname(String fk_ctname) {
		this.fk_ctname = fk_ctname;
	}

	public String getFk_stname() {
		return fk_stname;
	}

	public void setFk_stname(String fk_stname) {
		this.fk_stname = fk_stname;
	}

	public String getFk_etname() {
		return fk_etname;
	}

	public void setFk_etname(String fk_etname) {
		this.fk_etname = fk_etname;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(int saleprice) {
		this.saleprice = saleprice;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getPqty() {
		return pqty;
	}

	public void setPqty(int pqty) {
		this.pqty = pqty;
	}

	public String getPcontents() {
		return pcontents;
	}

	public void setPcontents(String pcontents) {
		this.pcontents = pcontents;
	}

	public String getPcompanyname() {
		return pcompanyname;
	}

	public void setPcompanyname(String pcompanyname) {
		this.pcompanyname = pcompanyname;
	}

	public String getPexpiredate() {
		return pexpiredate;
	}

	public void setPexpiredate(String pexpiredate) {
		this.pexpiredate = pexpiredate;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getSalecount() {
		return salecount;
	}

	public void setSalecount(int salecount) {
		this.salecount = salecount;
	}

	public int getPlike() {
		return plike;
	}

	public void setPlike(int plike) {
		this.plike = plike;
	}

	public String getPdate() {
		return pdate;
	}

	public void setPdate(String pdate) {
		this.pdate = pdate;
	}

	
	public String getTitleimg() {
		return titleimg;
	}

	public void setTitleimg(String titleimg) {
		this.titleimg = titleimg;
	}

	public ProductImageVO getImages() {
		return images;
	}

	public void setImages(ProductImageVO images) {
		this.images = images;
	}

	public PackageVO getPac() {
		return pac;
	}

	public void setPac(PackageVO pac) {
		this.pac = pac;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(int totalPoint) {
		this.totalPoint = totalPoint;
	}
	
	public void setTotalPriceTotalPoint(int oqty) {
		 // 총판매가(실제판매가 * 주문량) 입력하기
		 totalPrice = saleprice * oqty;
		 
		 // 총포인트(제품1개당 포인트 * 주문량) 입력하기
		 totalPoint = point * oqty;
	}
	
	// 제품의 할인률(%)을 계산해주는 메소드 생성하기 
	public int getPercent() {
		// 정가 : 실제판매가 = 100 : x
		// x = (실제판매가*100)/정가
		// 할인률 = 100 - (실제판매가*100)/정가
		// 예: 100 - (2800*100)/3000 = 6.66
		
		double per = 100 - (saleprice * 100.0)/price;
		long percent = Math.round(per);
		
		return (int)percent;
		
	}// end of getPercent()---------------
	
}
