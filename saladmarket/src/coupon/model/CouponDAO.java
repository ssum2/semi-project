package coupon.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class CouponDAO implements InterCouponDAO{
//	#아파치톰캣이 제공하는 DBCP 객체변수 ds 생성 
	private DataSource ds = null; // import javax.sql.DataSource
	
//	#Connection, preparedStatement, ResultSet객체 생성
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public CouponDAO() {
		try {
			Context initContext = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			ds = (DataSource)envContext.lookup("jdbc/myoracle");

		} catch (NamingException e) {
			e.printStackTrace();
		} 
	} 
	public void close() {
		try {
			if(rs != null) {
				rs.close();
				rs = null;
			}
			if(pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
			if(conn != null) {
				conn.close();
				conn = null;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 특정 회원의 보유 쿠폰 리스트를 가져오는 메소드
	 * @param userid
	 * @return List<CouponVO> myCouponList
	 */
	@Override
	public List<CouponVO> getMyCouponList(String userid) throws SQLException{
		List<CouponVO> myCouponList = null;
		
		try {
			conn = ds.getConnection();
			
			String sql = " select cpnum, cpname, discountper, cpusemoney, cpuselimit, fk_userid, fk_cpnum, to_char(cpexpiredate, 'yyyymmdd') as cpexpiredate, b.cpstatus "+
						 " from coupon a join my_coupon b "+
						 " on cpnum = fk_cpnum "+
						 " where fk_userid=? and cpexpiredate>sysdate "; 
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userid);
			
			rs = pstmt.executeQuery();
			
			int cnt=0;
			while(rs.next()) {
				cnt++;
				
				if(cnt==1) {
					myCouponList = new ArrayList<CouponVO>();
				}
				int cpnum = rs.getInt("cpnum");
				String cpname = rs.getString("cpname");
				int discountper = rs.getInt("discountper");
				int cpusemoney = rs.getInt("cpusemoney");
				int cpuselimit = rs.getInt("cpuselimit");
				String fk_userid = rs.getString("fk_userid");
				String cpexpiredate = rs.getString("cpexpiredate");
				int cpstatus = rs.getInt("cpstatus");

				CouponVO cpvo = new CouponVO();
				
				cpvo.setCpnum(cpnum);
				cpvo.setCpname(cpname);
				cpvo.setDiscountper(discountper);
				cpvo.setCpusemoney(cpusemoney);
				cpvo.setCpuselimit(cpuselimit);
				cpvo.setFk_userid(fk_userid);
				cpvo.setCpexpiredate(cpexpiredate);
				cpvo.setCpstatus_m(cpstatus);
				
				myCouponList.add(cpvo);
			}
			
		} finally {
			close();
		}
		
		return myCouponList;
	} // List<CouponVO> getMyCouponList(String userid) ---------------------------------------
	
	
	
	
	
	
	
	
}
