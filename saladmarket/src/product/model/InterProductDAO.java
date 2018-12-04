package product.model;

import java.sql.SQLException;
import java.util.List;

public interface InterProductDAO {

	// 대분류-소분류 이름으로 물품 목록 가져오는 메소드(상단 메뉴에서 클릭해서 들어올 때)
	List<ProductVO> selectProductListBySdname(String fk_sdname) throws SQLException;

}
