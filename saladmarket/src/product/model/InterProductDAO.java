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

//	장바구니
	// *** product, small_detai, product_images, product_package 테이블에서 상품리스트 정보 (아직)
	List<HashMap<String, Object>> getProductListInfo() throws SQLException;
	
	// *** view_productList 뷰에서 스펙태그이름별(stname) 제품 리스트
	List<HashMap<String, Object>> getStnameList(String stname) throws SQLException; 

	// *** view_productList 안에 있는 패키지번호(pacnum)로 제품 상세
	HashMap<String, Object> getProductDetailPacnum(String pacnum) throws SQLException; 
	
	// *** product 안에 있는 단품번호(pnum)로 제품 상세
	HashMap<String, Object> getProductDetailPnum(String pnum) throws SQLException; 
	
	// *** product_images 안에 있는 단품번호(pnum)로 제품이미지 
	List<HashMap<String, String>> getProductImagePnum(String pnum) throws SQLException;
	
	// *** view_productList 뷰에서 대분류별(ldname) 제품 리스트
	List<HashMap<String, Object>> getSdnameList(String sdname) throws SQLException; 
	
	// *** cart 테이블에서 로그인한 userid의 장바구니 리스트 
	List<HashMap<String, String>> getCartList(String userid) throws SQLException;
	

//	#admin; 주문 목록 관리
	// **** 검색조건에 따른 배송상태 리스트 보기 **** //
	List<HashMap<String,String>> getDeliverList(String searchType,String searchWord,int currentShowPage,int sizePerPage) throws SQLException;

	//** 검색타입별 갯수 알아오기
	int getTotalCoutBySType(String stype,String sword) throws SQLException;
	
//	결제완료로 변경시켜주는 메소드
	int changePaymentComplete(String odrcode) throws SQLException;

	// **** 배송시작으로 변경시켜주는 추상 메소드 **** //
	int changeDeliverStart(String odrcode, String invoice) throws SQLException;
	
	// **** 배송완료로 변경시켜주는 추상 메소드 **** //
	int changeDeliverEnd(String odrcode) throws SQLException;
	
	// ****주문취소로 변경시켜주는 추상 메소드 **** //
	int changeDeliverChange(String odrcode) throws SQLException;

	
//	#store; 주문상세페이지
	int getTotalprice(String odrdnum) throws SQLException; //토탈프라이스를 가져오는 메소드 
	//마이페이지에서 주문 상세 정보 가져오기 
	HashMap<String,String> getOneOrderDetail(String odrdnum) throws SQLException;

//	#store; 주문 목록
	List<HashMap<String, String>> getOrderListByUserid(String userid)
			throws SQLException;

//	#store; 주문취소
	int cancleOrderByOdrcode(String odrcode) throws SQLException;

//	#store; header 전체 검색
	// 헤더 전체 검색 시 카운트 가져오기
	int getTotalSearchCount(String totalSearchWord) throws SQLException;
		
	// 헤더 검색으로 나오는 상품 리스트
	List<HashMap<String,Object>> getSearchProduct(int sizePerPage, int currentShowPageNo, String totalSearchWord, String sort) throws SQLException;


//	#store; 상품상세
//	1) 상품 정보
	ProductVO getProductOneByPnum(int pnum) throws SQLException;
//	2) 상품 이미지
	List<String> getImagesByPnum(String pnum) throws SQLException;
//	3) 패키지번호로 상품목록 가져오기
	List<ProductVO> getProductListbyPacnum(String pacnum) throws SQLException;
//	4) 패키지번호로 상품이미지 가져오기
	List<String> getImagesByPacnum(String pacnum) throws SQLException;
//	5) 추천상품 리스트
	List<ProductVO> getRecommdProdlist() throws SQLException;
//	6) 선택상품 드롭박스 아래에 추가
	HashMap<String, Object> getProductpriceNname(String pnum) throws SQLException;
//	7)패키지에 딸려있는 상품의 갯수를 알려주는 메소드 (패키지 상품 좋아요에 씀)
	int getProductCountbyPacnum(String pacnum) throws SQLException;
//	8) 패키지 상품 좋아요 메소드 
	int insertLikebypacnum(String userid, String pacnum, String len) throws SQLException;
//	9) 단품상품 좋아요 메소드 
	int insertLikebypnum(String userid, String pnum) throws SQLException;

//	#store; 주문하기
	// *** getSeq_jsp_order() *** //
	public int getSeq_jsp_order() throws SQLException;
	// *** 주문하기 *** //
	public int addOrder(String odrcode, String userid, int odrstatus, int sumtotalprice, int sumtotalpoint
			, String[] pnumArr, String[] oqtyArr, String[] salepriceArr, String[] cartnoArr, String couponNo) throws SQLException;
	// *** 주문 완료 보여주기 *** //
	public List<HashMap<String, String>> selectOneOrder(String odrcode) throws SQLException;
	// *** 쿠폰 조회 하기 *** //
	public HashMap<String, String> selectOneCoupon(String coupon, String userid) throws SQLException;

	
//	#장바구니
	// *** product_cart 테이블에서 oqty가 0보다 크면 update, 0이면 delete 장바구니변경 
	int updateDeleteCart(String cartno, String oqty) throws SQLException;

	
}
