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
	List<ProductVO> getProductListAdmin(int sizePerPage, int currentShowPageNo) throws SQLException;
	
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

//	#이벤트 태그
//	1) 이벤트 태그 리스트 불러오기
	List<HashMap<String, String>> getEnvetTagList(String searchWord) throws SQLException;

//	2) 이벤트 수정; 이벤트태그 1개 가져오기
	HashMap<String, String> getOneEventTag(String etnum) throws SQLException;

//	2) 이벤트 수정; 수정 메소드
	int updateEventTag(HashMap<String, String> map) throws SQLException;

//	3) 이벤트태그 생성하기; 시퀀스 채번
	int getEtnum() throws SQLException;

//	3) 이벤트태그 생성 메소드
	int insertEventTag(HashMap<String, String> map) throws SQLException;

//	4) 이벤트태그 삭제하기
	int deleteEventTagByEtnum(String etnum) throws SQLException;

//	#패키지 목록 불러오기
	List<PackageVO> getPackageList(int sizePerPage, int currentShowPageNo, String searchWord) throws SQLException;

//	#패키지 페이징바
	int getTotalPackageCount(String searchWord) throws SQLException;

//	#페이지 시퀀스 채번
	int getPacnum() throws SQLException;

//	#패키지 추가하기
	int insertPackage(PackageVO pacvo) throws SQLException;

//	#패키지 수정하기
	int updatePackage(PackageVO pacvo) throws SQLException;

//	#패키지 1개의 정보 가져오기
	PackageVO getOnePackage(String pacnum) throws SQLException;

//	#패키지 삭제하기
	int deletePackageByPacnum(String pacnum) throws SQLException;

//	#물품 목록관련 메소드 (고은)
	int getCountByfk_ldnameNword(String fk_ldname, String searchword) throws SQLException;
	int getCountBysdnameNword(String sdname, String searchword) throws SQLException;
	List<HashMap<String, Object>> getContentListbyfk_ldname(String fk_ldname, int sizePerPage, int currentShowPageNo,
			String searchword, String orderby) throws SQLException;
	List<HashMap<String, Object>> getContentListbysdname(String sdname, int sizePerPage, int currentShowPageNo,
			String searchword, String orderby) throws SQLException;
	List<ProductVO> getProductList(String fk_ldname, String sdname, String orderby) throws SQLException;


//	이벤트
	List<ProductVO> getProductsByStnameAppend(String stname, int startRno, int endRno) throws SQLException;






	


	


}
