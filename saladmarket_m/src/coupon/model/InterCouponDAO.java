package coupon.model;

import java.sql.SQLException;
import java.util.List;

public interface InterCouponDAO {

//	#나의 보유 쿠폰 리스트
	List<CouponVO> getMyCouponList(String userid) throws SQLException;

}
