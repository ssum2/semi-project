package member.model;

import java.util.Calendar;

public class MemberVO {
	private int mnum;            // ȸ����ȣ(�������� �����Ͱ� ���´�)
	private String userid;      // ȸ�����̵�
	private String name;        // ȸ����
	private String pwd;         // ��й�ȣ
	private String email;       // �̸���
	private String phone;         // �޴��� ��ȣ
	private String postnum;       // �����ȣ
	private String address1;       // �ּ�
	private String address2;       // ���ּ�
	private String birthday; 	// �������
	private int coin;           // ���ξ�
	private int point;          // ����Ʈ
	private String registerday; // ��������
	private int status;         // ȸ��Ż������   1:��밡��(������) / 0:���Ҵ�(Ż��)
	private String lastlogindate;	// ������ �α��� �Ͻ�
	private String lastpwdchangedate;	// ������ �н����� ���� �Ͻ�
	private boolean requirePwdChange = false; // �н����� ���� �ǹ� ���� --> true�� ��� 6���� �����̿��� �����ϵ��� ����
	private boolean requireCertify = false; // ������ �α��� �Ͻ�; idleStatus
	private int summoney;	// �������űݾ�
	private int fk_lvnum;	// ȸ�� ��� �ѹ�
	
	public MemberVO() { }
	
	public MemberVO(int mnum, String userid, String name, String pwd, String email, String phone, String postnum,
			String address1, String address2, String birthday, int coin, int point, String registerday, int status,
			String lastlogindate, String lastpwdchangedate, boolean requirePwdChange, boolean requireCertify,
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
		this.coin = coin;
		this.point = point;
		this.registerday = registerday;
		this.status = status;
		this.lastlogindate = lastlogindate;
		this.lastpwdchangedate = lastpwdchangedate;
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

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getRegisterday() {
		return registerday;
	}

	public void setRegisterday(String registerday) {
		this.registerday = registerday;
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
		Calendar currentdate = Calendar.getInstance(); // ���糯¥�� �ð��� ���´�.
		int year = currentdate.get(Calendar.YEAR);

		String myYear = birthday.substring(0, 4);
		return year - Integer.parseInt(myYear)+1;
		
	}
	
	public String getShowBirthday() {
		String result = "";
		String birthyear = birthday.substring(0, 4);
		String birthmonth = birthday.substring(4, 6);
		String day = birthday.substring(6);
		
		result = birthyear+"�� "+birthmonth+"�� "+day+"��";
		
		return result;
	}

	public String getLastlogindate() {
		return lastlogindate;
	}

	public void setLastlogindate(String lastlogindate) {
		this.lastlogindate = lastlogindate;
	}

	public String getLastpwdchangedate() {
		return lastpwdchangedate;
	}

	public void setLastpwdchangedate(String lastpwdchangedate) {
		this.lastpwdchangedate = lastpwdchangedate;
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
