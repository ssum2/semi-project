package product.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface InterProductDAO {

// 대분류-소분류 이름으로 물품 목록 가져오는 메소드(상단 메뉴에서 클릭해서 들어올 때)
	List<ProductVO> selectProductListBySdname(String fk_sdname) throws SQLException;

	
//	admin; 카테고리 태그 리스트
	List<HashMap<String, String>> getCategoryTagList(String searchWord) throws SQLException;

//	admin; 카테고리태그 추가하기
	int addCategoryTag(String ctname) throws SQLException;

//	admin; 카테고리태그 삭제하기
	int deleteCategoryTag(String ctnum) throws SQLException;

//	admin; 카테고리태그 수정하기
	int updateCtname(String ctnum, String ctname) throws SQLException;
	
	
//	admin; 물품 등록하기
//	# 패키지 및 태그 불러오기
	List<HashMap<String, String>> getPacnameList() throws SQLException;
	List<HashMap<String, String>> getSdnameList() throws SQLException;
	List<HashMap<String, String>> getCtnameList() throws SQLException;
	List<HashMap<String, String>> getStnameList() throws SQLException;
	List<HashMap<String, String>> getEtnameList() throws SQLException;

//	#제품번호(시퀀스) 채번 하는 메소드
	int getPnumOfProduct() throws SQLException;

//	#제품등록 insert 메소드
	int productInsert(ProductVO pvo) throws SQLException;

//	#제품 이미지정보를 product_images 테이블에 insert하는 메소드
	int product_images_Insert(int pnum, String attachFilename) throws SQLException;

	
	
//	admin; 물품리스트

//	#대분류명 리스트 가져오기
	List<HashMap<String, String>> getLdnameList() throws SQLException;
	
//	#대분류에 따른 소분류명 가져오기
	List<HashMap<String, String>> getSdnameListByLdname(String ldname) throws SQLException;

//	#물품 리스트 가져오기
//	1) 아무 조건 없었을 때(페이지 처음 이동 했을 때)
	List<ProductVO> getProductList(int sizePerPage, int currentShowPageNo) throws SQLException;
	
//	2) 
	List<ProductVO> getProductListByDname(int sizePerPage, int currentShowPageNo, String fk_name, String dname) throws SQLException;
	List<ProductVO> getProductListBySearch(int sizePerPage, int currentShowPageNo, String searchType, String searchWord) throws SQLException;
	List<ProductVO> getProductListBySearchWithDname(int sizePerPage, int currentShowPageNo, String fk_name, String dname
			, String searchType, String searchWord) throws SQLException;
	List<ProductVO> getProductListBySearchAll(int sizePerPage, int currentShowPageNo, String searchWord) throws SQLException;
	List<ProductVO> getProductListBySearchAllWithDname(int sizePerPage, int currentShowPageNo, String fk_name, String dname, String searchWord) throws SQLException;

	int getTotalProductCountAll(String searchWord, String ldname, String sdname) throws SQLException;
	int getTotalProductCount(String searchType, String searchWord, String ldname, String sdname) throws SQLException;

//	#상품디테일; 상품정보
	ProductVO getOneProductDetail(String pnum) throws SQLException;
//	#상품디테일; 추가이미지
	List<HashMap<String, String>> getAttachImgList(String pnum) throws SQLException;

	
//	#상품수정; 상품 추가이미지 삭제
	int deleteAttachProductImg(String pimgnum) throws SQLException;

//	#상품수정; 상품 정보 update
	int updateProduct(ProductVO pvo) throws SQLException;

//	#상품 삭제
	int deleteProductByPnum(String pnum) throws SQLException;






	


	


}
