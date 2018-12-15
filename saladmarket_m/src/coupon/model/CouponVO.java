package coupon.model;

public class CouponVO {

	private int cpnum;			// 쿠폰번호
	private String cpname;		// 쿠폰명 
	private int discountper;	// 할인율
	private int cpusemoney;		// 쿠폰 사용 조건; ex. 1만원 이상 사용시 ~~
	private int cpuselimit;		// 쿠폰 할인금액 제한; ex. 최대 5000원
	private int cpstatus_c;		// 쿠폰 자체적인 사용할 수 있는 지 여부
	private String fk_userid;	// 쿠폰 보유자 아이디
	private String cpexpiredate;	// 쿠폰 사용기한
	private int cpstatus_m;		// 보유 쿠폰 사용여부
	
	
	public CouponVO() {}
	public CouponVO(int cpnum, String cpname, int discountper, int cpusemoney, int cpuselimit, int cpstatus_c,
			String fk_userid, String cpexpiredate, int cpstatus_m) {
		super();
		this.cpnum = cpnum;
		this.cpname = cpname;
		this.discountper = discountper;
		this.cpusemoney = cpusemoney;
		this.cpuselimit = cpuselimit;
		this.cpstatus_c = cpstatus_c;
		this.fk_userid = fk_userid;
		this.cpexpiredate = cpexpiredate;
		this.cpstatus_m = cpstatus_m;
	}
	public int getCpnum() {
		return cpnum;
	}
	public void setCpnum(int cpnum) {
		this.cpnum = cpnum;
	}
	public String getCpname() {
		return cpname;
	}
	public void setCpname(String cpname) {
		this.cpname = cpname;
	}
	public int getDiscountper() {
		return discountper;
	}
	public void setDiscountper(int discountper) {
		this.discountper = discountper;
	}
	public int getCpusemoney() {
		return cpusemoney;
	}
	public void setCpusemoney(int cpusemoney) {
		this.cpusemoney = cpusemoney;
	}
	public int getCpuselimit() {
		return cpuselimit;
	}
	public void setCpuselimit(int cpuselimit) {
		this.cpuselimit = cpuselimit;
	}
	public int getCpstatus_c() {
		return cpstatus_c;
	}
	public void setCpstatus_c(int cpstatus_c) {
		this.cpstatus_c = cpstatus_c;
	}
	public String getFk_userid() {
		return fk_userid;
	}
	public void setFk_userid(String fk_userid) {
		this.fk_userid = fk_userid;
	}
	public String getCpexpiredate() {
		return cpexpiredate;
	}
	public void setCpexpiredate(String cpexpiredate) {
		this.cpexpiredate = cpexpiredate;
	}
	public int getCpstatus_m() {
		return cpstatus_m;
	}
	public void setCpstatus_m(int cpstatus_m) {
		this.cpstatus_m = cpstatus_m;
	}
	
	public String getShowCpexpiredate() {
		String result = "";
		String cpexpiredateyear = cpexpiredate.substring(0, 4);
		String cpexpiredatemonth = cpexpiredate.substring(4, 6);
		String cpexpiredateday = cpexpiredate.substring(6);
		// 19930222 
		// 01234567
		result = cpexpiredateyear+"년 "+cpexpiredatemonth+"월 "+cpexpiredateday+"일";
		
		return result;
	}
	
	
	
	
	
	
	
}
