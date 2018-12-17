package member.model;

import java.util.Calendar;

public class MemberVO {
	private int mnum;            // 회원번호(시퀀스로 데이터가 들어온다)
	private String userid;      // 회원아이디
	private String name;        // 회원명
	private String pwd;         // 비밀번호
	private String email;       // 이메일
	private String phone;         // 휴대폰 번호
	private String postnum;       // 우편번호
	private String address1;       // 주소
	private String address2;       // 상세주소
	private String birthday; 	// 생년월일
	private int point;          // 포인트
	private String registerdate; // 가입일자
	private int status;         // 회원활동 유무  1:사용가능(가입중) / 0:사용불능
	private String last_logindate;	// 마지막 로그인 일시
	private String last_changepwdate;	// 마지막 패스워드 변경 일시
	private boolean requirePwdChange = false; // 패스워드 변경 의무 여부 --> true인 경우 6개월 이전이여서 변경하도록 유도
	private boolean requireCertify = false; // 마지막 로그인 일시; idleStatus
	private int summoney;	// 누적구매금액
	private int fk_lvnum;	// 회원 등급 넘버
	
	
	public MemberVO() { }
	
	public MemberVO(int mnum, String userid, String name, String pwd, String email, String phone, String postnum,
			String address1, String address2, String birthday, int point, String registerdate, int status,
			String last_logindate, String last_changepwdate, boolean requirePwdChange, boolean requireCertify,
			int summoney, int fk_lvnum) {
		super();
		this.mnum = mnum;
		this.userid = userid;
		this.name = name;
		this.pwd = pwd;
		this.email = email;
		this.phone = phone;
		this.postnum = postnum;
		this.address1 = address1;
		this.address2 = address2;
		this.birthday = birthday;
		this.point = point;
		this.registerdate = registerdate;
		this.status = status;
		this.last_logindate = last_logindate;
		this.last_changepwdate = last_changepwdate;
		this.requirePwdChange = requirePwdChange;
		this.requireCertify = requireCertify;
		this.summoney = summoney;
		this.fk_lvnum = fk_lvnum;
	}

	public int getMnum() {
		return mnum;
	}

	public void setMnum(int mnum) {
		this.mnum = mnum;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPostnum() {
		return postnum;
	}

	public void setPostnum(String postnum) {
		this.postnum = postnum;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getRegisterdate() {
		return registerdate;
	}

	public void setRegisterdate(String registerdate) {
		this.registerdate = registerdate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSummoney() {
		return summoney;
	}

	public void setSummoney(int summoney) {
		this.summoney = summoney;
	}

	public int getFk_lvnum() {
		return fk_lvnum;
	}

	public void setFk_lvnum(int fk_lvnum) {
		this.fk_lvnum = fk_lvnum;
	}

	public int getShowAge() {
		Calendar currentdate = Calendar.getInstance(); // 현재날짜와 시간을 얻어온다.
		int year = currentdate.get(Calendar.YEAR);

		String myYear = birthday.substring(0, 4);
		return year - Integer.parseInt(myYear)+1;
		
	}
	
	public String getShowPhone() {
		String result ="";

		String phone1 = phone.substring(0, 3);
		String phone2 = "";
		String phone3 = "";
		if(phone.length()==10) {
			phone2 = phone.substring(3, 6);
			phone3 = phone.substring(6);
		}
		else {
			phone2 = phone.substring(3, 7);
			phone3 = phone.substring(7);
		}
		// 010 9982 1387
		// 010 888 9999
		// 012 3456 78910
		result = phone1+"- "+phone2+"-"+phone3;
		
		return result;
		
	}
	
	public String getShowBirthday() {
		String result = "";
		String birthyear = birthday.substring(0, 4);
		String birthmonth = birthday.substring(4, 6);
		String day = birthday.substring(6);
		// 19930222 
		// 01234567
		result = birthyear+"년 "+birthmonth+"월 "+day+"일";
		
		return result;
	}
	
	public String getShowRegisterdate() {
		String result = "";
		String registeryear = registerdate.substring(0, 4);
		String registerdatemonth = registerdate.substring(4, 6);
		String registerdateday = registerdate.substring(6);
		// 19930222 
		// 01234567
		result = registeryear+"년 "+registerdatemonth+"월 "+registerdateday+"일";
		
		return result;
	}
	
	public String getLvnameByLvnum() {
		if(fk_lvnum==1) {
			return "Bronze";
		}
		else if(fk_lvnum==2) {
			return "Silver";
		}
		else if(fk_lvnum==3) {
			return "Gold";
		}
		else {
			return "";
		}
	}
	
	public String getStatusByStatus() {
		if(status==0) {
			return "휴면";
		}
		else if(status==1) {
			return "활동";
		}
		else {
			return "";
		}
	}
	
	public String getShowSummoney() {
		String result=String.format("%,d", summoney);
		
		return result;
	}

	public String getLast_logindate() {
		return last_logindate;
	}

	public void setLast_logindate(String last_logindate) {
		this.last_logindate = last_logindate;
	}

	public String getLast_changepwdate() {
		return last_changepwdate;
	}

	public void setLast_changepwdate(String last_changepwdate) {
		this.last_changepwdate = last_changepwdate;
	}

	public boolean isRequirePwdChange() {
		return requirePwdChange;
	}


	public void setRequirePwdChange(boolean requirePwdChange) {
		this.requirePwdChange = requirePwdChange;
	}


	public boolean isRequireCertify() {
		return requireCertify;
	}


	public void setRequireCertify(boolean requireCertify) {
		this.requireCertify = requireCertify;
	}
}
