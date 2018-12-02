show user;
-- USER��(��) "SALADMARKET"�Դϴ�.


-- ȸ�� ���(member_level) ���̺� ����; 1~3���
create table member_level 
(lvnum       number         not null -- ��޹�ȣ; 1, 2, 3
,lvname      varchar2(100)  not null -- ��޸�; bronze, silver, gold
,lvbenefit   clob           not null -- �������
,lvcontents  clob           not null -- ������ǳ���
,constraint  PK_level primary key (lvnum)
);

create sequence seq_member_level_lvnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into member_level 
values(seq_member_level_lvnum.nextval, 'bronze', 'ȸ����������(�ִ� 5000�� ����), �������� 10%(1���� �̻� ���Ž� �ִ� 3000��)', 'ȸ�����Խ�'); 

insert into member_level 
values(seq_member_level_lvnum.nextval, 'silver', 'ȸ����������(�ִ� 5000�� ����), �������� 10%(1���� �̻� ���Ž� �ִ� 3000��), �������� 15%(1��5õ�� �̻� ���Ž� �ִ� 5000��)', '10���� �̻� ���Ž�'); 

insert into member_level 
values(seq_member_level_lvnum.nextval, 'gold', 'ȸ����������(�ִ� 5000�� ����), �������� 10%(1���� �̻� ���Ž� �ִ� 3000��), �������� 15%(1��5õ�� �̻� ���Ž� �ִ� 5000��), �������� 20%(2���� �̻� ���Ž� �ִ� 7000��), ������', '30���� �̻� ���Ž�');  

commit;

select *
from member_level;

-- ����(coupon) ���̺� ���� 
create table coupon 
(cpnum          number not null             -- ������ȣ 
,cpname         varchar2(100) not null      -- ������ 
,discountper    number not null         -- ������
,cpstatus        number  default 1 not null         -- ���� ��� ����; 0:���� 1:�̻��
,cpusemoney   number not null           -- ���� ��� ����; ex. 1���� �̻� ���� ~~
,cpuselimit      number not null           -- ���� ���αݾ� ����; ex. �ִ� 5000��
,constraint PK_coupon primary key(cpnum)
,constraint CK_coupon_cpstatus check( cpstatus in(0, 1) )
);

create sequence seq_coupon_cpnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

-- ȸ����������(�ִ� 5000�� ����), �������� 10%(1���� �̻� ���Ž� �ִ� 3000��), �������� 15%(1��5õ�� �̻� ���Ž� �ִ� 5000��), �������� 20%(2���� �̻� ���Ž� �ִ� 7000��)
insert into coupon(cpnum, cpname, discountper, cpstatus, cpusemoney, cpuselimit)
values(seq_coupon_cpnum.nextval, 'ȸ����������', 100, default, 0, 5000);

insert into coupon(cpnum, cpname, discountper, cpstatus, cpusemoney, cpuselimit)
values(seq_coupon_cpnum.nextval, '�������� 10%', 10, default, 10000, 3000);

insert into coupon(cpnum, cpname, discountper, cpstatus, cpusemoney, cpuselimit)
values(seq_coupon_cpnum.nextval, '�������� 15%', 15, default, 15000, 5000);

insert into coupon(cpnum, cpname, discountper, cpstatus, cpusemoney, cpuselimit)
values(seq_coupon_cpnum.nextval, '�������� 20%', 20, default, 20000, 7000);

insert into coupon(cpnum, cpname, discountper, cpstatus, cpusemoney, cpuselimit)
values(seq_coupon_cpnum.nextval, '������', 0, default, 0, 2500);

commit;

select *
from coupon;

-- ȸ��(member) ���̺� ���� 
create table member 
(mnum               number                not null  -- ȸ����ȣ
,userid             varchar2(100)         not null  -- ȸ�����̵�
,pw                 varchar2(200)         not null  -- ��й�ȣ(SHA256 ��ȣȭ)
,name               varchar2(10)          not null  -- ȸ����
,email              varchar2(200)         not null  -- �̸���(AES256 ��ȣȭ)
,phone              varchar2(400)         not null  -- �޴���(AES256 ��ȣȭ)
,birthday           date                  not null  -- �������
,postnum            number                not null  -- �����ȣ
,address1           varchar2(200)         not null  -- �ּ�
,address2           varchar2(200)         not null  -- ���ּ�
,point              number default 0      not null  -- ����Ʈ
,registerdate       date default sysdate  not null  -- ��������
,last_logindate     date default sysdate  not null  -- �������α�������
,last_changepwdate  date default sysdate  not null  -- ��й�ȣ��������
,status             number default 1      not null  -- ȸ��Ż������ / 0:Ż�� 1:Ȱ�� 2:�޸�
,summoney           number default 0      not null  -- �������űݾ�
,fk_lvnum           number default 1      not null  -- ȸ����޹�ȣ
,constraint PK_member_mnum	primary key(mnum)
,constraint UQ_member_userid unique(userid)
,constraint FK_member_lvnum foreign key(fk_lvnum)
                                references member_level(lvnum)
,constraint CK_member_status check( status in(0, 1, 2) )
);

alter table member modify(fk_lvnum number default 1); 
alter table member modify(name varchar2(100));

-- ���̺� ��������
SELECT COLUMN_ID,COLUMN_NAME,DECODE (NULLABLE , 'N', 'NOT NULL',NULL)  "NULL" ,
       DATA_TYPE||DECODE(DATA_TYPE,'NUMBER',DECODE (DATA_PRECISION, null,'','('||DATA_PRECISION||','||DATA_SCALE||')' ) ,
                                       'CHAR', '('||DATA_LENGTH||')',
                                      'NCHAR', '('||DATA_LENGTH||')',
                                  'NVARCHAR2', '('||DATA_LENGTH||')',
                                   'VARCHAR2', '('||DATA_LENGTH||')',NULL ) "TYPE"
FROM ALL_TAB_COLUMNS
WHERE TABLE_NAME = 'MEMBER'
ORDER BY COLUMN_ID;

select *
from member;

alter table member add(pw varchar2(200));

ALTER TABLE member RENAME COLUMN pw TO pwd;

commit;

create sequence seq_member_mnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


-- ������ Ȯ�� �� �� �ִ� �̸����̶� ��ȣ �־��ּ���
insert into member(mnum,userid, pwd, name,email,phone ,birthday,postnum ,address1,address2,point,registerdate ,last_logindate ,last_changepwdate ,status,summoney ,fk_lvnum)
values(1, 'leess', '9695b88a59a1610320897fa84cb7e144cc51f2984520efb77111d94b402a8382', 
'�̼���',  'blue_christmas@naver.com', '01099821387', add_months(sysdate, -50), 14409, '��⵵ ��õ�� ����� 64���� 19', '���ٿ� 502ȣ',
default, default, default, default, default, default, default);

update member set pw ='qwer1234$' where userid='leess';

commit;

insert into member(mnum,userid, pwd, name,email,phone ,birthday,postnum ,address1,address2,point,registerdate ,last_logindate ,last_changepwdate ,status,summoney ,fk_lvnum)
values(seq_member_mnum.nextval, 'hongkd', '9695b88a59a1610320897fa84cb7e144cc51f2984520efb77111d94b402a8382', 
'ȫ�浿',  'blue_christmas@naver.com', '01099821387', add_months(sysdate, -60), 14409, '��⵵ ��õ�� ����� 64���� 19', '���ٿ� 502ȣ',
default, default, default, default, default, default, default);

select *
from member;

insert into member(mnum,userid, pwd, name,email,phone ,birthday,postnum ,address1,address2,point,registerdate ,last_logindate ,last_changepwdate ,status,summoney ,fk_lvnum)
values(seq_member_mnum.nextval, 'admin', '9695b88a59a1610320897fa84cb7e144cc51f2984520efb77111d94b402a8382', 
'������',  'blue_christmas@naver.com', '01099821387', add_months(sysdate, -60), 14409, '��⵵ ��õ�� ����� 64���� 19', '���ٿ� 502ȣ',
default, default, default, default, default, default, default);

String sql = "insert into member(mnum,userid, pwd, name,email,phone ,birthday,postnum ,address1,address2,point,registerdate ,last_logindate ,last_changepwdate ,status,summoney ,fk_lvnum)\n"+
"values(seq_member_mnum.nextval, ?, ?, \n"+
"?,  ?, ?, ?, ?, ?, ?,\n"+
"default, default, default, default, default, default, default) ";



select *
from member;

-- ��������(my_coupon) ���̺� ���� 
create table my_coupon 
(fk_userid   varchar2(100) not null  -- ȸ�����̵� 
,fk_cpnum  number not null  -- ������ȣ
,cpexpiredate date not null         -- ���� ��� ����
,constraint FK_my_coupon_userid foreign key(fk_userid)
                                  references member(userid)
,constraint FK_my_coupon_cpnum foreign key(fk_cpnum)
                                  references coupon(cpnum)
);

insert into my_coupon values('leess', 1, add_months(sysdate, 3));


drop table my_coupon purge;
drop table coupon purge;

---- �α���(login) ���̺� ���� 
--create table login 
--(fk_userid  varchar2(100) not null  -- ȸ�����̵� 
--,pw         varchar2(200) not null  -- ��ȣ(SHA256 ��ȣȭ) 
--,name       varchar2(30)  not null  -- ȸ����
--,last_logindate date default sysdate  not null  -- ��������
--,last_logindate     date default sysdate  not null -- ������ �α��� �Ͻ�
--,constraint PK_login_userid primary key(fk_userid)
--,constraint FK_login_userid foreign key(fk_userid)
--                                  references member(userid)
--);
--
--insert into login values('leess', 'qwer1234$', '�̼���');

drop table login purge;

-- ������(admin) ���̺� ���� 
create table admin 
(adminid varchar2(100) -- �����ھ��̵� 
,adminpw varchar2(200) -- �����ھ�ȣ 
);

insert into admin values('admin', 'qwer1234$');
select *
from admin;
-----------------------------------------------------------------------------

-- ��ǰ��Ű��(product_package) ���̺� ���� 
create table product_package 
(pacnum       number default 0  not null    -- ��ǰ��Ű����ȣ 
,pacname      varchar2(100)     not null    -- ��ǰ��Ű���� 
,paccontents  clob                          -- ��Ű������ 
,pacimage     varchar2(200)                 -- ��Ű���̹��� 
,constraint PK_product_package primary key(pacnum)
,constraint UQ_product_package unique(pacname)
);

create sequence seq_product_Package_pacnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into product_package values(seq_product_Package_pacnum.nextval, '����', '����', '����');

commit;

-- ��з���(large_detail) ���̺� ���� 
create table large_detail 
(ldnum   number         not null  -- ��з��󼼹�ȣ 
,ldname  varchar2(100)  not null  -- ��з��� 
,constraint PK_large_detail	primary key(ldnum)
,constraint UQ_large_detail unique (ldname)
);

create sequence seq_large_detail_ldnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into large_detail values(seq_large_detail_ldnum.nextval, '������');
insert into large_detail values(seq_large_detail_ldnum.nextval, '���彺');
insert into large_detail values(seq_large_detail_ldnum.nextval, 'DIY');

-- �Һз���(small_detail) ���̺� ����
create table small_detail 
(sdnum     number         not null  -- �Һз��󼼹�ȣ
,fk_ldname  varchar2(100)       not null  -- ��з��󼼸�
,sdname    varchar2(100)  not null  -- �Һз���
,constraint PK_small_detail	primary key(sdnum)
,constraint FK_small_detail_ldname foreign key(fk_ldname)
                                  references large_detail(ldname)
,constraint UQ_small_detaill unique (sdname)
);

create sequence seq_small_detail_sdnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;



select *
from small_detail;

insert into small_detail values(seq_small_detail_sdnum.nextval, '������', '�ø���');
insert into small_detail values(seq_small_detail_sdnum.nextval, '������', '�����嵵�ö�');
insert into small_detail values(seq_small_detail_sdnum.nextval, '������', '��/����');

insert into small_detail values(seq_small_detail_sdnum.nextval, '���彺', '��/�ֽ�');
insert into small_detail values(seq_small_detail_sdnum.nextval, '���彺', '�ǰ���');
insert into small_detail values(seq_small_detail_sdnum.nextval, '���彺', '�ǰ���');

insert into small_detail values(seq_small_detail_sdnum.nextval, 'DIY', '��ä/���');
insert into small_detail values(seq_small_detail_sdnum.nextval, 'DIY', '����');
insert into small_detail values(seq_small_detail_sdnum.nextval, 'DIY', '���/�ް�');
insert into small_detail values(seq_small_detail_sdnum.nextval, 'DIY', '����');
insert into small_detail values(seq_small_detail_sdnum.nextval, 'DIY', '�ҽ�');
insert into small_detail values(seq_small_detail_sdnum.nextval, 'DIY', '����ǰ');

commit;

select *
from small_detail;

-- �����±�(spec_tag) ���̺� ���� / (Hit, Best, New)
create table spec_tag 
(stnum   number         not null  -- �����±׹�ȣ 
,stname  varchar2(100)  not null  -- �����±׸� 
,constraint PK_spec_tag	primary key(stnum)
,constraint UQ_spec_tag unique (stname)
);

create sequence seq_spec_tag_stnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into spec_tag values(seq_spec_tag_stnum.nextval, 'HIT');
insert into spec_tag values(seq_spec_tag_stnum.nextval, 'BEST');
insert into spec_tag values(seq_spec_tag_stnum.nextval, 'NEW');

select *
from spec_tag;

-- ī�װ��±�(category_tag) ���̺� ���� 
create table category_tag 
(ctnum   number         not null  -- ī�װ���ȣ 
,ctname  varchar2(100)  not null  -- ī�װ��� 
,constraint PK_category_tag primary key(ctnum)
,constraint UQ_category_tag unique (ctname)
);

create sequence seq_category_tag_ctnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into category_tag values(seq_category_tag_ctnum.nextval, '���̾�Ʈ��');
insert into category_tag values(seq_category_tag_ctnum.nextval, '�Ļ���/���Ŀ�');
insert into category_tag values(seq_category_tag_ctnum.nextval, 'ä�������ڿ�');

select *
from category_tag;

-- �̺�Ʈ�±�(event_tag) ���̺� ���� 
create table event_tag 
(etnum   number  not null  -- �̺�Ʈ��ȣ 
,etname  varchar2(100)     -- �̺�Ʈ��
,etimagefilename varchar2(200) -- �̺�Ʈ �̹���
,constraint PK_event_tag primary key(etnum)
,constraint UQ_event_tag unique (etname)
);

create sequence seq_event_tag_etnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

insert into event_tag values(seq_event_tag_etnum.nextval, 'ũ�������� �̺�Ʈ', 'MerryChristmas.PNG');
insert into event_tag values(seq_event_tag_etnum.nextval, '���� �̺�Ʈ', 'LastSale.png');
insert into event_tag values(seq_event_tag_etnum.nextval, '���� �̺�Ʈ', 'NewYearSale.png');
insert into event_tag values(seq_event_tag_etnum.nextval, '����', null);


-- �̺�Ʈ ���̺� �̹����÷� �߰�(�̸� ����� �и� �����ϼ���)
alter table event_tag add etimagefilename varchar2(200); 
update event_tag set etimagefilename= 'MerryChristmas.PNG' where etnum=1;
update event_tag set etimagefilename= 'LastSale.png' where etnum=2;
update event_tag set etimagefilename= 'NewYearSale.png' where etnum=3;
update event_tag set etimagefilename= null where etnum=4;

commit;

select *
from event_tag;


-- ��ǰ(product) ���̺� ����
create table product 
(pnum          number  not null                -- ��ǰ��ȣ 
,fk_pacname     varchar2(100)  not null                -- ��ǰ��Ű����
,fk_sdname      varchar2(100)  not null                -- �Һз��󼼸� 
,fk_ctname      varchar2(100)  not null                -- ī�װ��±׸� 
,fk_stname      varchar2(100)  not null                -- �����±׸� 
,fk_etname      varchar2(100)  not null      -- �̺�Ʈ�±׸�
,pname         varchar2(100)  not null         -- ��ǰ�� 
,price         number default 0  not null      -- ���� 
,saleprice     number default 0  not null      -- �ǸŰ� 
,point         number default 0  not null      -- ����Ʈ 
,pqty          number default 0  not null      -- ��� 
,pcontents     clob                            -- ��ǰ���� 
,pcompanyname  varchar2(100)  not null         -- ��ǰȸ��� 
,pexpiredate varchar2(200) default '�󼼳�������'  not null -- ������� 
,allergy       clob                            -- �˷��������� 
,weight        number default 0  not null      -- �뷮 
,salecount     number default 0  not null      -- �Ǹŷ� 
,plike         number default 0  not null      -- ��ǰ���ƿ� 
,pdate         date default sysdate  not null  -- ��ǰ�������
,constraint PK_product_pnum primary key(pnum)
,constraint FK_product_pacname foreign key(fk_pacname)
                               references product_package(pacname)
,constraint FK_product_sdname foreign key(fk_sdname)
                               references small_detail(sdname)
,constraint FK_product_ctname foreign key(fk_ctname)
                               references category_tag(ctname)
,constraint FK_product_stname foreign key(fk_stname)
                               references spec_tag(stname)
,constraint FK_product_etname foreign key(fk_etname)
                               references event_tag(etname)
);

create sequence seq_product_pnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


-- ��ǰ���(Product_backup) ���̺� ���� 
create table product_backup 
(deletenum     number  not null         -- deletenum 
,pnum          number  not null                -- ��ǰ��ȣ 
,fk_pacname     number  not null                -- ��ǰ��Ű����
,fk_sdname      number  not null                -- �Һз��󼼸� 
,fk_ctname      number  not null                -- ī�װ��±׸� 
,fk_stname      number  not null                -- �����±׸� 
,fk_etname      number  default 0 not null      -- �̺�Ʈ�±׸�
,pname         varchar2(100)  not null         -- ��ǰ�� 
,price         number default 0  not null      -- ���� 
,saleprice     number default 0  not null      -- �ǸŰ� 
,point         number default 0  not null      -- ����Ʈ 
,pqty          number default 0  not null      -- ��� 
,pcontents     clob                            -- ��ǰ���� 
,pcompanyname  varchar2(100)  not null         -- ��ǰȸ��� 
,pexpiredate   date default sysdate  not null  -- ������� 
,allergy       clob                            -- �˷��������� 
,weight        number default 0  not null      -- �뷮 
,salecount     number default 0  not null      -- �Ǹŷ� 
,plike         number default 0  not null      -- ��ǰ���ƿ� 
,pdate         date default sysdate  not null  -- ?��ǰ�Ǹ�����
,constraint PK_product_backup primary key(deletenum)
);

create sequence seq_product_backup_deletenum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


-- ��(pick) ���̺� ���� 
create table pick 
(picknum    number         not null  -- ���ȣ 
,fk_userid  varchar2(100)  not null  -- ȸ�����̵� 
,fk_pnum    number         not null  -- ��ǰ��ȣ    
,constraint PK_pick_picknum primary key(picknum)
,constraint FK_pick_userid foreign key(fk_userid)
                            references member(userid)
,constraint FK_pick_pnum foreign key(fk_pnum)
                           references product(pnum)
);

create sequence seq_pick_picknum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

-- ��ٱ��� ���̺� ����
 create table product_cart
 (cartno  number               not null   --  ��ٱ��� ��ȣ
 ,fk_userid  varchar2(20)         not null   --  �����ID
 ,fk_pnum    number(8)            not null   --  ��ǰ��ȣ 
 ,oqty    number(8) default 0  not null   --  �ֹ���
 ,status  number(1) default 1             --  ��������; 0: ���� 1: ����
 ,constraint PK_product_cart_cartno primary key(cartno)
 ,constraint FK_product_cart_userid foreign key(fk_userid)
                                references member(userid) 
 ,constraint FK_product_cart_pnum foreign key(fk_pnum)
                                references product(pnum)
 ,constraint CK_product_cart_status check( status in(0,1) ) 
 );

 create sequence seq_product_cart_cartno
 start with 1
 increment by 1
 nomaxvalue
 nominvalue
 nocycle
 nocache;


-- ��ǰ�̹���(product_images) ���̺� ���� 
create table product_images 
(pimgnum       number         not null -- ��ǰ�̹�����ȣ 
,pimgfilename  varchar2(100)  not null -- ��ǰ�̹������ϸ� 
,fk_pnum       number         not null -- ��ǰ��ȣ 
,constraint PK_product_images primary key(pimgnum)
,constraint FK_product_images_ldnum foreign key(fk_pnum)
                                      references product(pnum)
);

create sequence seq_product_images_pimgnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


-- ����Խ���(review_board) ���̺� ���� 
create table review_board 
(rbnum        number  not null                -- �����ȣ 
,fk_pnum      number  not null                -- ��ǰ��ȣ 
,fk_userid    varchar2(100)  not null         -- ����ھ��̵� 
,rbtitle      varchar2(100)  not null         -- �������� 
,rbgrade      number default 0      not null  -- �������� 
,rbwritedate  date default sysdate  not null  -- �����ۼ����� 
,rbcontents   clob  not null                  -- ���䳻��
,rbimage    varchar2(200)                 -- �����̹���
,rbviewcount  number default 0  not null      -- ������ȸ�� 
,rblike       number default 0  not null      -- �������ƿ� 
,rbstatus     number default 1  not null   
,constraint PK_review_board primary key(rbnum)
,constraint FK_review_board_pnum foreign key(fk_pnum)
                                   references product(pnum)
,constraint FK_review_board_userid foreign key(fk_userid)
                                    references member(userid)
,constraint CK_review_board_rbstatus check( rbstatus in(0,1) )
);

create sequence seq_review_board_rbnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

-- ������ ����Խ��� ���̺� ����� �и� �����ϼ���
alter table review_borad add rbimage varchar2(200);
ALTER TABLE review_borad RENAME TO review_board;
ALTER sequence seq_review_borad_rbnum RENAME TO seq_review_board_rbnum;
commit;

drop sequence seq_review_borad_rbnum;
drop table review_board purge;
drop table review_comment purge;

-- ����Խ��Ǵ��(review_comment) ���̺� ���� 
create table review_comment 
(rcnum        number         not null         -- �����۹�ȣ
,fk_rbnum     number         not null         -- ����Խ��ǹ�ȣ
,fk_userid    varchar2(100)  not null         -- ����ھ��̵�
,name      varchar2(10) not null                -- ������̸�  
,rcwritedate  date default sysdate  not null  -- �������ۼ����� 
,rccontents   number  not null                -- �����۳��� 
,constraint PK_review_comment primary key(rcnum)
,constraint FK_review_comment_rbnum foreign key(fk_rbnum)
                                       references review_board(rbnum)
,constraint FK_review_comment_userid foreign key(fk_userid)
                                        references member(userid)
);

create sequence seq_review_comment_rcnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;





-- ��ǰ�ֹ�(product_order)  ���̺� ���� 
create table product_order 
(odrcode        varchar2(100)  not null         -- �ֹ��ڵ� / ȸ���ڵ�-�ֹ�����-seq (ex. s-20181123-1
,fk_userid      varchar2(100)  not null         -- ����ھ��̵� 
,odrtotalprice  number         not null         -- �ֹ��Ѿ� 
,odrtotalpoint  number         not null         -- �ֹ�������Ʈ 
,odrdate        date default sysdate  not null  -- �ֹ�����
,constraint PK_product_order primary key(odrcode)
,constraint FK_product_order_userid foreign key(fk_userid)
                                      references member(userid)
);

create sequence seq_product_order_odrcode
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

create table product_order_temp
as
select *
from product_order;


-- �ֹ���(product_order_detail) ���̺� ���� 
create table product_order_detail 
(odrdnum        number not null            -- �ֹ��󼼹�ȣ 
,fk_pnum        number not null            -- ��ǰ��ȣ 
,fk_odrcode     varchar2(100) not null     -- �ֹ��ڵ� 
,oqty           number not null            -- �ֹ����� 
,odrprice       number not null            -- �ֹ��� ��ǰ����
,odrstatus  number default 0 not null  -- ��ۻ��� / 0:�����(�ֹ��Ϸ�) 1:����� 2:��ۿϷ� 3: �ֹ���� 4: ��ȯȯ��
,deliverdate    date                       -- ��ۿϷ�����?
,invoice        varchar2(200)              -- ������ȣ 
,constraint PK_order_detail	primary key (odrdnum)
,constraint FK_order_detail_pnum foreign key(fk_pnum)
                                   references product(pnum)                             
,constraint FK_order_detail_odrcode foreign key(fk_odrcode) 
                                       references product_order(odrcode)  on delete cascade
,constraint CK_order_detail_odrstatus check( odrstatus in(0, 1, 2, 3, 4) )
);

create sequence seq_order_detail_odrdnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


create table product_order_detail_temp
as
select *
from product_order_detail;

-- ��ǰ���ǰԽ���(qna_borad) ���̺� ���� 
create table qna_board 
(qnanum        number not null                -- ��ǰ���ǹ�ȣ 
,fk_pnum       number not null                -- ��ǰ��ȣ 
,fk_userid     varchar2(100) not null         -- ����ھ��̵� 
,qnawritedate  date default sysdate not null  -- ��ǰ�����ۼ����� 
,qnatitle      varchar2(100) not null         -- ��ǰ�������� 
,qnacontents   clob not null                  -- ��ǰ���ǳ��� 
,qnaopencheck  number default 1 not null      -- ��ǰ���ǰ������� / 0:���� 1:���� 
,qnastatus     number default 1 not null
,constraint PK_qna_board primary key(qnanum)
,constraint FK_qna_board_pnum foreign key(fk_pnum)
                                  references product(pnum)
,constraint FK_qna_board_userid foreign key(fk_userid)
                                  references member(userid)
,constraint CK_qna_board_qnastatus check( qnastatus in(0, 1) )
);

create sequence seq_qna_board_qnanum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


drop sequence seq_qna_borad_qnanum;
drop table qna_borad purge;
drop table qna_comment purge;

-- ��ǰ���Ǵ��(qna_comment) ���̺� ����
create table qna_comment 
(qnacnum        number not null                -- ��ǰ���Ǵ�۹�ȣ 
,fk_qnanum      number not null                -- ��ǰ���ǹ�ȣ 
,qnacwritedate  date default sysdate not null  -- ��ǰ���Ǵ���ۼ����� 
,qnaccontents   clob not null                  -- ��ǰ���Ǵ�۳��� 
,constraint PK_qna_comment primary key(qnacnum)
,constraint FK_qna_comment_qnanum foreign key(fk_qnanum)
                                    references qna_board(qnanum)
);

create sequence seq_qna_comment_qnacnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


-- �ֹ����(order_cancel) ���̺� ����
create table order_cancel 
(odrcnum       number not null  -- �ֹ���ҹ�ȣ
,odrccontents  clob not null    -- �ֹ���һ���
,fk_odrcode     varchar2(100)  not null -- �ֹ��ڵ�
,constraint PK_order_cancel primary key(odrcnum)
,constraint FK_order_cancel_odrcode foreign key(fk_odrcode) 
                                       references product_order(odrcode)  on delete cascade
);

create sequence seq_order_cancel_odrcnum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;

-- �������� ���� ���̺�
--(ȸ�����̵�/�����Ͻ�/�����ݾ�/���ΰ�������/��������(����/����))
create table payment 
(paynum  number             not null    -- ���������ε���
,fk_userid varchar2(100)    not null    -- ����ھ��̵�
,paydate    date               not null    -- �����Ͻ�
,payamounts number        not null      -- �����ݾ�
,paymethod     varchar2(100)    not null    -- ��������
,paystatus      number      not null        -- ��������(0; ����/1; ����)
,constraint PK_payment primary key(paynum)
,constraint FK_payment_userid foreign key(fk_userid)
                                      references member(userid)
,constraint CK_payment check( paystatus in(0, 1) )

);

create sequence seq_payment_paynum
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;


create table payment_temp
as
select *
from payment;

commit;

alter table product modify(pexpiredate varchar2(200) default '�󼼳�������'); 

commit;



--------- ������ �μ�Ʈ ���� (product_package, product) ------------
-- ������ - �����嵵�ö�
insert into product_package(pacnum, pacname, paccontents,  pacimage)
values(seq_product_Package_pacnum.nextval
, '[����������] �����̾� ������ 3��'
, '������ ������ �ӱݾ� �ƻ��ϰ� ������ ä�ҿ� �ż��� ���Ͽ� ����ϰ� ��鿩���� �巹��. 
���⿡ ���� ���� ���� ��⳪ ������ ���� ��ᰡ ��췯����, �� �� �Ļ�ε� �ջ����� ����� �����尡 �ϼ�����. 
������� ������ �� �ƴ϶� �پ��� ����Ұ� ��� ��� �־� ���� ������� ��� ã���ϴ�. 
�� ���� �����忡 ������ ���� �������ѡ� ������ �� �� ���������. 
�ø��� �Ұ��ϴ� ������� �����, ä�ҿ����� �� �������� �ڹ��� �޾� ���� �ǰ��� ������ �ִ� �����Ƿ� �����ϰ� �����ƾ��. 
����� ��Ḧ ������ ������ ���������� ������� ���� �Ƚ��ϰ� �ǰ��ϰ� ��� �� �ִ�ϴ�. ���� ������ ���� �����̾� ������, ������������ �����带 ���� �����غ�����. '
, '1496199185281l0.jpg' );
 
insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname
, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate)
values(seq_product_pnum.nextval, '[����������] �����̾� ������ 3��', '�����嵵�ö�', '���̾�Ʈ��', 'HIT', '����', '����'||chr(38)||'����Ÿ'
, 6900, 6900, 690, 100, '����Ÿ ġ��� �ε巯�� ������ ����ϸ鼭�� ��¦ ��ŭ�� ���� ������ �����ϴ� ��ǥ �������� Į���� ���� ���ް� 3�� 6�� ǳ������. ���⿡ ��ŭ�� ���� �� Ƣ�� ��ſ��� �ȱ�� �ƷδϾ�, ��纣��, ũ������ ���� ��� Į�θ� �δ��� ������. ���������� ��Ÿ��, ����þƴ� ���� ������ ǳ���ϴ�ϴ�. ��Ż���ƻ� ���� �������� ������� �߻�� �巹���� �������� ������� ���� ���� �� �ſ���.'
,'queensfresh', '�����Ϸκ��� 5��', '����, ����, �Ұ��, �߰��, ����� ���� �ü����� ����', 245, 30, 10, sysdate);

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname
, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate)
values(seq_product_pnum.nextval, '[����������] �����̾� ������ 3��', '�����嵵�ö�', '���̾�Ʈ��', 'HIT', '����', '������'||chr(38)||'����ġ��'
, 6900, 6900, 690, 100, '�ٴ� ������ ������ ������ �ʱ��ʱ��ϰ� �ż��� ����� �������� ����� ǳ�̸� �����ϴ� ����ġ���� �������. ���쿡�� ���ް� 3, Į��, ��Ÿ�� D��, ����ġ�󿡴� Į��, �ݸ�, ��Ÿ�� ���� ������ ǳ���ؿ�. ���⿡ ���� ���ð� ���İ� �� ���� ������ ���ߴ�ϴ�. ���� ȭ�������� ����� ĥ����Ͼ� �巹���� �Ҷ��Ǵ��� ������Ŭ�� ���޻����� ���� ��ȱ⿡ ���� ǳ���� ���� ��ȭ�� �̲��� �� �ſ���.'
,'queensfresh', '�����Ϸκ��� 5��', '����, ����, �Ұ��, �߰��, ����� ���� �ü����� ����', 270, 30, 10, sysdate);

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname
, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate)
values(seq_product_pnum.nextval, '[����������] �����̾� ������ 3��', '�����嵵�ö�', '���̾�Ʈ��', 'HIT', '����', '����'||chr(38)||'�ӽ���'
, 6900, 6900, 690, 100, '�ε巴�� ����ϰ� ������ û�� ȣ�ֻ� �Ұ��� �̱��ϰ� ����� ǳ�̰� �λ����� ������ �����̹����� ��޽����� ��ȭ�� �̷���. �Ұ�⿡�� �ܹ���, ��Ÿ�� B, ���̾ƽ� ������, �����̹������� �� ���� Į���� ��Ÿ�� C�� ǳ���ϴ�ϴ�. �Բ� ���� ���ξ����� ����� ������ �ε巴�� ���� �� �ƴ϶�, �����ϰ� �޴��� ������ ����� ǳ�̸� �Ȱ��� �ſ���. ������Ż �巹���� ��鿩 ��ĥ���� ���غ�����.'
,'queensfresh', '�����Ϸκ��� 5��', '����, ����, �Ұ��, �߰��, ����� ���� �ü����� ����', 250, 30, 10, sysdate);

commit;

select *
from product;

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'berry_ricotta.jpg', 1); 
insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'shirimp_franchbean.jpg', 2); 
insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'beef.jpg', 3); 

select *
from product_images;

select *
from product_package;

rollback;
commit;

--------������:�ø��� insert
insert into product_package(pacnum, pacname, paccontents ,pacimage)
values( seq_product_Package_pacnum.nextval, '[�������н�] ����� �ø��� 4��','����ϰ� ������ ����� ���� ���� �ǰ��ϰ� ���� �ø���','1508744357840l0.jpg');

--product ������ 4��
insert into product (pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
                                    ,pqty, pcontents, pcompanyname, pexpiredate
                                    ,allergy, weight, salecount, plike, pdate )
values(seq_product_pnum.nextval,  '[�������н�] ����� �ø��� 4��','�ø���','�Ļ���/���Ŀ�','NEW','ũ�������� �̺�Ʈ','�츮Ƽ�� �÷���ũ',8000,7800,780,300,'��, ����, ����Ƹ� ����� ���� ������ ����� ȥ�յǾ� ����� ǳ�̸� ���ؿ�. ����� ���� ÷���Ǿ� ��¦ ������ ���� ���� ���������� ���߽��� ���� �������� �� �ǰ��� ���Ͽ���. �� ������ ���� �İ��� �� ��� �ִ�ϴ�.'
,'Nature Path Foods Inc.','��ǰ�� ���� ǥ��� ��¥����(�д¹�:��.��.�� ��)','�� ����',375,568,84,default);

--product ������ 5��
insert into product (pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
                                    ,pqty, pcontents, pcompanyname, pexpiredate
                                    ,allergy, weight, salecount, plike, pdate )
values(seq_product_pnum.nextval,  '[�������н�] ����� �ø��� 4��','�ø���','�Ļ���/���Ŀ�','NEW','ũ�������� �̺�Ʈ','�ھ˶� ũ������ ',8000,7800,780,0,'�ǰ��� ���� ���̸� ������ ���ھƷ� ���� �ھ˶� ũ�����Ǵ� �ٻ�ٻ��� �İ��� ��ǰ�̿���. ������ ����԰� ���ھ��� �޴��� ǳ�̰� ��ȭ�Ӱ� ��췯������. �Ը� ���� ��ħ�� ��������ϴ�.'
,'Nature Path Foods Inc.','��ǰ�� ���� ǥ��� ��¥����(�д¹�:��.��.�� ��)','����',325,215,43,default);

--product ������ 6��
insert into product (pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
                                    ,pqty, pcontents, pcompanyname, pexpiredate
                                    ,allergy, weight, salecount, plike, pdate )
values(seq_product_pnum.nextval,  '[�������н�] ����� �ø��� 4��','�ø���','�Ļ���/���Ŀ�','NEW','ũ�������� �̺�Ʈ','��纣�� �ó��� �÷��� ',8000,7800,780,50,'��а� �б���� ����� �Ƹ���, ���, �͸� �� �پ��� ��� ��ŭ�� ��纣��, ����� �ó���, ������ ������������ ���� ��������. ��ä�ο� ��ᰡ ��췯���⿡ ������ �İ��� ǳ���ϰ�, ����ϸ鼭�� ����� ǳ�̸� ��������.'
,'Nature Path Foods Inc.','��ǰ�� ���� ǥ��� ��¥����(�д¹�:��.��.�� ��)','��, ��� ����',400,100,20,default);

--product ������ 7��
insert into product (pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
                                    ,pqty, pcontents, pcompanyname, pexpiredate
                                    ,allergy, weight, salecount, plike, pdate )
values(seq_product_pnum.nextval,  '[�������н�] ����� �ø��� 4��','�ø���','�Ļ���/���Ŀ�','NEW','ũ�������� �̺�Ʈ','���� ħ�� ',8000,7800,780,165,'Ȱ¦ ���� �ִ� �Ʊ� ������ź�� �׷��� �־� ���̵��� ��̸� ���� ���� ħ������. ��������� �ڶ� �������� ����� ���ھƷ� ������� �޴��ϸ� ����� �α� ������ �ø����̶��ϴ�.'
,'Nature Path Foods Inc.','��ǰ�� ���� ǥ��� ��¥����(�д¹�:��.��.�� ��)','����',284,86,4,default);


insert into product_images(pimgnum,pimgfilename,fk_pnum) 
values(seq_product_images_pimgnum.nextval,'HERTAGE FLAKES.PNG',4 ); 

insert into product_images(pimgnum,pimgfilename,fk_pnum) 
values(seq_product_images_pimgnum.nextval,'KOALA CRISP.PNG',5);

insert into product_images(pimgnum,pimgfilename,fk_pnum) 
values(seq_product_images_pimgnum.nextval,'Blueberry Cinnamon Flax.PNG',6);

insert into product_images(pimgnum,pimgfilename,fk_pnum) 
values(seq_product_images_pimgnum.nextval,'Choco Chimps.PNG', 7);

commit;



select *
from product;

select *
from product_images;

select *
from product_package;

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname
, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate)
values(seq_product_pnum.nextval, '����', '��/����', '�Ļ���/���Ŀ�', 'HIT', '����', '���� ���� Ŭ�����'
, 3900, 3900, 390, 100, 'ȫ���̳� �߱��� ���� �±��� ��Ÿ������� ���� ���Ը� ���� �߰��� �� �־��. �˱����� �𿩾ɾ� ���� �� �׸��� ���� ������� �±��� �ϻ����� ǳ���̱⵵ ����. ���� �� ������ �� ����� ������ �������� �ʱ��ʱ� Ǯ������ ������ �� ��. ���� �� ��, ���� �� �� �Դ� ���� �ݹ� �ٴ��� ���� �Ǵµ���. ���� �Ұ��ϴ� ���� ���� Ŭ���� ������ �±����� �ղ����� �Ը��� ���깰 ���� ��ü�� ���Ͽ츦 ��� ������ ���� �״�� ��Ƴ� ��ǰ�Դϴ�. �ż��� ������ ������ ������ �����ϰ� �����Ͽ� ���� �ʰ� ����� ���� ������. ��ŷᰡ ���ϰ� ���� �ʰ� ������ ���� ����� ��� �԰� �Ǵ� �ŷ��� ������. ��� �� ����� ������ �� ǥ�ü����� ���� �װ� ���ڷ������� �� �и� �����ּ���. �����ϰ� �̱����� ���� �丮�� ���� �� ���� �ſ���.'
,'OKEANOSFOOD COMPANY LIMITED', '�����Ϸκ��� 18����', '����, �а���(��), ��� ����', 150, 50, 10, sysdate);

insert into product_images(pimgnum,pimgfilename,fk_pnum) 
values(seq_product_images_pimgnum.nextval,'hobakjook.png', 8);

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname
, price, saleprice, point, pqty, pcontents, pcompanyname, pexpiredate, allergy, weight, salecount, plike, pdate)
values(seq_product_pnum.nextval, '����', '��/����', '�Ļ���/���Ŀ�', 'HIT', '����', '[���̰�Űģ] ��ȣ�� ��'
, 6000, 6000, 600, 200, '���̰�Űģ�� ���� �丮, ���� �ѽ��� ����ϱ⺸�ٴ� �ô뿡 �´� ������ ��ȭ�� �ƿ췯 ������ ģ���ϰ� ��� �� �ִ� ���� �����Ǹ� �����մϴ�. ����� ȭ������ �ʾƵ� ��ᰡ ��Ƶ� ������ ���� ���� ���� �丮�������� ���ٸ� ������ ��������. �쿬���� �丮�� ����ġ�� ������ ���̰� �丮�������� ����� �丮 �λ� 30���� ���� ������ ����, ���ѹα��� ��ǥ�ϴ� ���� �������� Ȱ���ϰ� �ִµ���. ��� �丮 �п��� �ѽ� ���� ����, �̹��� �丮 ��������, ���̰� ���ö ���� ��ǥ��� źź�� Ÿ��Ʋ�� �׳ฦ ���������. ������ ���ְ� �����ϰ� ������ �� �ִ� ���� �����Ǹ� ���� ��۰� å �۾�, ���� �� ���� ���뿡�� �˸��� ������. ���� �ø��� ���� ����������. ������ ��ſ����� ����, ������ ��� ��췯���� ���� �����Ǹ� �������� ������ �� �ִ�ϴ�. �����밡 ��췯���� �ູ�� ��Ź�� ���ĺ�����.'
,'(��)���̰�Űģ', '�����Ϸκ��� 7��', '��, ����, ����, ����, ����, ���, ����, ��, �޹�, �������, �丶��, ȣ��, ��, Ű��, �߰��, ������(��,����,ȫ������), ��¡��, ����, ������ ���� �ü����� ����', 350, 30, 10, sysdate);

insert into product_images(pimgnum,pimgfilename,fk_pnum) 
values(seq_product_images_pimgnum.nextval,'shirimp_soup.png', 9);

update product_images set pimgnum = 9 where pimgnum =10;
commit;

select *
from product;


-- ***** �ǰ��� insert
-- �ڸ�û
insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
             , pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '�ǰ���', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ܿ����] �ڸ�û 950g', 10000, 8800, 880
     , 100, '�� ���� ������ �Ʒ����� ��, ������ �������� ȣ���� ���ô� ������ �� �����ؿ�. 
     �� ���� ���� ���� ǳ���� ������� �� �� ������ �������� ������ �ص� ����� ��������. 
     �ø��� �ڸ� ������ ǳ���ϰ� �� �ڸ�û�� �غ��߽��ϴ�. �����Ϸ� ���� ���� ���� �Խθ��� ���� �پ��� �Ӱ� ���� ���ٸ��� ����Ǵ� 
     ���� ���� �ŷ����̿���. ��Ÿī��ƾ�� ��Ÿ��A ���� ǳ���� �ڸ��� �������� �־ ���� �ǰ��� ����û����. 
     ������ ��� ��� ���� ���п� �İ��� ��������鼭�� �ôµ� �δ��� �����. 
     �ܿ￡�� ������ ����, �������� ź����� �־� û���� ź������� Ȱ���غ�����.'
     , '�ż���������', 950);
     
-- ����û
insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
             , pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '�ǰ���', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ܿ����] ����û 1kg', 10000, 7700, 770
     , 100, '�� ���� ������ �Ʒ����� ��, ������ �������� ȣ���� ���ô� ������ �� �����ؿ�.
     �� ���� ���� ���� ǳ���� ������� �� �� ������ �������� ������ �ص� ����� ��������.
     �ø��� ���� �������� ��ǥ������ ���� ������ ǳ���ϰ� �� ����û�� �غ��߽��ϴ�.
     ��Ÿ��C�� ������, ���� ���� ǳ���� ���ڿ� �������� �־ ���� �ǰ��� ����û����.
     ������ ��� ��� ���� ���п� �İ��� ��������鼭�� �ôµ� �δ��� �����.
     �ܿ￡�� ������ ����, �������� ź����� �־� û���� ź������� Ȱ���غ�����.'
     , '�ż���������', 1000);
     
     
-- ����û
insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
             , pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '�ǰ���', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ܿ����] ����û 950g', 10000, 8800, 880
     , 100, '�� ���� ������ �Ʒ����� ��, ������ �������� ȣ���� ���ô� ������ �� �����ؿ�. 
     �� ���� ���� ���� ǳ���� ������� �� �� ������ �������� ������ �ص� ����� ��������. 
     �ø��� ���� ������ ǳ���ϰ� �� ����û�� �غ��߽��ϴ�. ��Ÿ��C�� ������ ���� ǳ���� ���� �������� �־ ���� �ǰ��� ����û����.
     ������ ��� ��� ���� ���п� �İ��� ��������鼭�� �ôµ� �δ��� �����. 
     �ܿ￡�� ������ ����, �������� ź����� �־� û���� ź������� Ȱ���غ�����.'
     , '�ż���������', 950);
     
-- �Ѷ��û
insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
             , pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '�ǰ���', '�Ļ���/���Ŀ�', 'BEST', '����', '[�ܿ����] �Ѷ��û 950g', 10000, 8800, 880
     , 100, '�� ���� ������ �Ʒ����� ��, ������ �������� ȣ���� ���ô� ������ �� �����ؿ�. 
     �� ���� ���� ���� ǳ���� ������� �� �� ������ �������� ������ �ص� ����� ��������. 
     �ø��� �Ѷ�� ������ ǳ���ϰ� �� �Ѷ��û�� �غ��߽��ϴ�. ��Ÿ��C�� ������ ���� ǳ���� �Ѷ���� �������� �־ ���� �ǰ��� ����û����.
     ������ ��� ��� ���� ���п� �İ��� ��������鼭�� �ôµ� �δ��� �����. 
     �ܿ￡�� ������ ����, �������� ź����� �־� û���� ź������� Ȱ���غ�����.'
     , '�ż���������', 950);

update product set fk_sdname = '�ǰ���';

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'grapefruitjuice.jpg', 10);
insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'citronjuice.jpg', 11);
insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'lemonjuice.jpg', 12);
insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'hanrabongjuice.jpg', 13);

select *
from product;

select *
from product_images;

commit;



---------------------------------
-- ***** �ǰ��� insert
-- ����������
insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
             , pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '�ǰ���', '�Ļ���/���Ŀ�', 'BEST', '����', '[����ǰ�] ���̴��� ���������� (20��)', 30000, 25000, 2500
     , 100, 'ȯ���� ��ǰ�� ������ �������� ��. ���� ȯ���� �Ӹ� �ƴ϶� ���ÿ��� ã�� �ſ�.
     �������̳� ����� ������ �̼��������� ����ؾ� �� �͵� ì�� �͵� ���� ��������. �׷��� ���̿��� ������������ ���̷��� �ϴ� �е��� ������ �ٵ���. 
     ����ǰ��� ���̰� �԰� ���� �Ŷ�� �� �Ĳ����� ���ۿ� ���� �θ��� �������� ���� ��� �������� ������ �� ��ҽ��ϴ�.'
     , '����ǰ�����', 100);
     
-- ������
insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
             , pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '�ǰ���', '�Ļ���/���Ŀ�', 'BEST', '����', '[����̾߱�] ������ ������ 10��', 20000, 16000, 1600
     , 100, '����̾߱�� ������ �����Ӹ� �ƴ϶� ������ ǳ���� ������ ������ ��°�� �����Ͽ� ������ �������� ��������ϴ�. 
     �������� ������ �������� ���� ū �ŷ����δ� ������ ������ ���� �� �ִµ���. ���� ��Ḧ ���� �ð� ���� ���� ��� �Ͱ� �޸� ����̾߱�� ���� ���� 
     ����Ұ� �ı��Ǵ� ���� ���� ���Ͽ� ��ٷӰ� ������ �����ϴ� ������ �������� �ʽ��ϴ�. �׷��� ������� ������ �������� �� �� �����Ѻ���, 
     ��ġ �������� �Դ� �� �ż��� ���� ���������ϴ�. ���� ���� ������ ����� ǳ�̸� ������ ����̾߱� ������ ������Ʈ�� ������ ���ο��� �ǰ��� �����غ�����.'
     , '����̾߱�', 120);
     
-- ȣ����
insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
             , pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '�ǰ���', '�Ļ���/���Ŀ�', 'HIT', '����', '[��ƺ�] ����� ȣ����', 35000, 32000, 3200
     , 100, '���� ������ ���� ���� ���� ���߰� ������ �ֽ� ������� �� ���� ���� ����Ű�� �� ������. ���� ������ �Ծ�� �ϴ� ������ ��Ŵٸ�,
     ��ƺ��� ����� ȣ������ �غ��� ������. �� � ��ǰ÷������ ���� �ʰ� ��������� ����� ���� ȣ�ڿ� �ؼҷ��� ����� �������� �־� ��������ϴ�.
     �����ϰ� �� ���� ���� ȣ���� �״�� �����߱⿡ �ŷ����� ȣ���� ǳ�̸� ���� �� �־��. 30���� �ڽ� ��ǰ����, �ֺ� �е鿡�� �����ϱ⿡�� �����̿���.'
     , '��ƺ�', 80);
     
-- ����
insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point
             , pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '�ǰ���', '�Ļ���/���Ŀ�', 'BEST', '����', '[�׶���] 100% ���� �����ֽ�', 20000, 18000, 1800
     , 100, '������ ���κ��� ���ε�� ���� �̾߱Ⱑ ���Ҿ��. ��ͺ�� Ŭ������Ʈ�� ��� �Ծ��ٴ� ��ȭ�� ���� ��������, �ϵ����� �丣�����׸� ������ 
     ��Ȥ�ߴٰ� ����. Ÿ�� �� ���� ���� �ӿ��� ����ó�� ������ �˰��̰� �˾��� ���� ���� ���� �˳��µ���. ������ �������� �Ĺ��� ����Ʈ�ΰհ� �ʼ� �ƹ̳����, 
     ���ѿ��� ���̼����� ǳ���ؼ� �������� ��õ�ϱ⿡�� ������ ���� ��ǰ�̱⵵ �մϴ�. '
     , '�׶���', 100);
     
insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'bellflowerjuice.jpg', 14);
insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'grapejuice.jpg', 15);
insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'pumpkinjuice.jpg', 16);
insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'pomegranatejuice.jpg', 17);

commit;

select *
from product;

select *
from product_images;

select *
from product_package;

---- ��Ű�� -> ��/�ֽ�


-- 1. ��Ű���ִ� �ֽ�
insert into product_package(pacnum, pacname, paccontents,  pacimage)
values(seq_product_Package_pacnum.nextval
, '[�ڿ�����] ���� �ֽ� 8��'
, '������ ǳ���� ���� ������ ���� �����ϰ� ���� �� �ִ� �ֽ�. ���̿��� ���� �ż��� �ֽ��� ������ְ� ������, ����Ⱑ ���ŷ���. 
�ø��� ���� �״�θ� ������ �ڿ����� �����ֽ� 8���� �����Ծ��. 
�ø����� �Ǹŵǰ� �ִ� �پ��� ������ ������ ������ �������ִ� �ڿ��������� ���� ��ǰ�̿���. 
���� ������ ����� �ֽ��� �ƴ�, ��ǰ ÷������ ���� �������� ���� ���� ���� �ֽ����ϴ�. 
�������� ������ ������ ��, ��Ÿ���̳� ȿ�� ���� �ı����� ���� ������ ���� ��� ������ ���� ���� ������ ���� ���� �״�� ��� �� �־��. 
�Ա� ���� ���Ŀ�Ʈ �Ŀ�ġ�� �޴��ϱ⵵ �������� ������, �뷮�� 100ml�� ���̰� ���ñ� �����ؿ�. 
�׳� ���ŵ� ��������, �õ��ǿ� �󸮸� �ÿ��� ����Ʈ�� ��� �� �־��.'
, 'fruitjuice2.jpg' );


insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '[�ڿ�����] ���� �ֽ� 8��', '��/�ֽ�', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ڿ�����] �赵���� �ֽ� 1��', 1400, 990, 99, 100, '������ �踦 ����° ��°�� �����ϰ�, �������� ����� ��ǰ ������� : 2019�� 11�� 26�ϱ���', '�ڿ�����', 100);

commit;

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '[�ڿ�����] ���� �ֽ� 8��', '��/�ֽ�', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ڿ�����] �����ֽ� 1��', 1400, 990, 99, 100, '������ ������ ������ �״�� �����Ͽ� ������ ���� �ı��� �ּ�ȭ�� ��ǰ ������� : 2019�� 11�� 26�ϱ���', '�ڿ�����', 100);

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '[�ڿ�����] ���� �ֽ� 8��', '��/�ֽ�', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ڿ�����] �����ֽ� 1��', 1400, 990, 99, 100, '����� ������ ���� ���ֵ� ������ �״�� �����Ͽ� ������ ���� �ı��� �ּ�ȭ�� ��ǰ ������� : 2019�� 11�� 26�ϱ���', '�ڿ�����', 100);

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '[�ڿ�����] ���� �ֽ� 8��', '��/�ֽ�', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ڿ�����] �� �ֽ� 1��', 1400, 990, 99, 100, '������ ������ �踦 �״�� �����Ͽ� ������ ���� �ı��� �ּ�ȭ�� ��ǰ ������� : 2019�� 11�� 26�ϱ���', '�ڿ�����', 100);

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '[�ڿ�����] ���� �ֽ� 8��', '��/�ֽ�', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ڿ�����] ���� �ֽ� 1��', 1400, 990, 99, 100, '����� ������ ���� ���⸦ ���� ����� �������� �����Ͽ� ������ ���� �ı��� �ּ�ȭ�� ��ǰ. ������� : 2019�� 11�� 26�ϱ���', '�ڿ�����', 100);

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '[�ڿ�����] ���� �ֽ� 8��', '��/�ֽ�', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ڿ�����] ������ �ֽ� 1��', 1400, 990, 99, 100, '������ ����� ����� �״�� �����Ͽ� ������ ���� �ı��� �ּ�ȭ�� ��ǰ ������� : 2019�� 11�� 26�ϱ���', '�ڿ�����', 100);

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '[�ڿ�����] ���� �ֽ� 8��', '��/�ֽ�', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ڿ�����] ��� �ֽ� 1��', 1400, 990, 99, 100, '������ ������ ����� �״�� �����Ͽ� ������ ���� �ı��� �ּ�ȭ�� ��ǰ ������� : 2019�� 11�� 26�ϱ���', '�ڿ�����', 100);

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '[�ڿ�����] ���� �ֽ� 8��', '��/�ֽ�', '�Ļ���/���Ŀ�', 'NEW', '����', '[�ڿ�����] ����ƷδϾ� �ֽ� 1��', 1400, 990, 99, 100, '������ ������ ����� ����� ������ ���� �ƷδϾƸ� �״�� �����Ͽ� ������ ���� �ı��� �ּ�ȭ�� ��ǰ ������� : 2019�� 11�� 26�ϱ���', '�ڿ�����', 100);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice1.jpg', 18);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice2.jpg', 18);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice3.jpg', 18);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice4.jpg', 18);


insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice1.jpg', 19);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice2.jpg', 19);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice3.jpg', 19);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice4.jpg', 19);


insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice1.jpg', 20);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice2.jpg', 20);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice3.jpg', 20);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice4.jpg', 20);



insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice1.jpg', 21);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice2.jpg', 21);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice3.jpg', 21);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice4.jpg', 21);



insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice1.jpg', 22);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice2.jpg', 22);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice3.jpg', 22);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice4.jpg', 22);




insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice1.jpg', 23);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice2.jpg', 23);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice3.jpg', 23);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice4.jpg', 23);



insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice1.jpg', 24);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice2.jpg', 24);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice3.jpg', 24);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice4.jpg', 24);


insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice1.jpg', 25);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice2.jpg', 25);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice3.jpg', 25);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'fruitjuice4.jpg', 25);


commit;

select *
from product;

select *
from product_images;

select *
from product_package;

-- 2. ��Ű�� ���� ��
insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '��/�ֽ�', '�Ļ���/���Ŀ�', 'BEST', '����', '[�̳׶�����] ���� �ؾ������ 2L', 2000, 1800, 180, 100, '5�� ������� �ϳ��� õ�� �̳׶��� ǳ���� ���� �ؾ������', '(��)�۷ι�������', 2000);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'water1.jpg', 26);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'water2.jpg', 26);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'water3.jpg', 26);



-- 3. ��Ű������ ������ �ֽ�

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '��/�ֽ�', '���̾�Ʈ��', 'HIT', '����', '[�ݸ����׸�] �� ������', 18000, 15600, 1560, 100, '�� �ѹ�� ���� �ʰ� ������ ������ ������ ¥�� 100% ���� ������ �ֽ� ������� : �����Ϸκ��� 2~3�ϱ��� ', '�ݸ����׸�', 1000);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'orangejuice1.jpg', 27);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'orangejuice2.jpg', 27);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'orangejuice3.jpg', 27);




-- 4. ��Ű������ ����Ʈ Ǫ�� �ֽ� 


insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '��/�ֽ�', 'ä�������ڿ�', 'HIT', '����', '[����Ʈ] �����ֽ�', 10000, 8500, 850, 100, '���� ���� �ڵθ� ���ϴ� ������ ���̼���, ��Ÿ��, ö���� ǳ���ϰ� �����ϰ� �־��. 
�̹��� �ø��� �ڵ��� ������ ��°�� ���� ����Ʈ�� ���� �ֽ��� �����Դ�ϴ�. 
�� ��ǰ�� 120�� ������ �۷ι� ���� �귣�� ����Ʈ�� ������ ���ϰ� �絵�� ���� ���� �ڵθ� �����Ͽ� ���� ���̿���. 
������, ����, ���̷� �� ��ǰ÷������ ���� ���� �ʰ� ���� �ڵ�(����)�� ���� ǫ ������ ���� 100% ���� �ֽ�������. 
����Ʈ ���� �ֽ��� �� �� ���ź���, �ΰ����� ������ ��� ���� ������ �����ϰ� �̱׷��� ǳ�̸� �����ϰ� ���� �� �ִ�ϴ�. 
���� �����ϰ� ��ħ�� ������ ��, ���Ŀ� �԰����� ��, ����Ʈ�� ���� �ֽ��� ���ź�����. 
900ml�� �Ѵ� �˳��� ���̱� ������ ����� �ΰ� �� ������ �Բ� ���� ���ñ⿡�� ���� �ſ���.
������� : ���籤���� ���ϰ� ������ ���� ����. ���� �Ŀ��� �ݵ�� ���庸���Ͻð� ������ ���� ��ñ� �ٶ��ϴ�.', '����Ʈ', 946);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'prunejuice1.jpg', 28);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'prunejuice2.jpg', 28);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'prunejuice3.jpg', 28);


commit;

select *
from product;

select *
from product_images;

select *
from product_package;


-- 5. ��Ű�� ���� �͸�����

insert into product(pnum, fk_pacname, fk_sdname, fk_ctname, fk_stname, fk_etname, pname, price, saleprice, point, pqty, pcontents, pcompanyname, weight)
values(seq_product_pnum.nextval, '����', '��/�ֽ�', 'ä�������ڿ�', 'NEW', '����', '[��������] ��Ʈ�븮', 4000, 3500, 350, 100, '�Ĺ��� ������ ����ó�� ����� ��� ������ ������ ����ϴ� ��ǰ���� �ָ�ް� �־��. 
Ư�� �پ��� ������ ������ �������� ���ϴ� �е��� �� �Ĺ��� ������ ��� ã���� �ٵ���. 
�Ĺ��� ������ ���� ���� ���̴� �͸��� ���̼����� ���� ���� ����� ����� ����� �����Ұ� �� ���� �� �ִ� �����. 
30���� �Ѵ� ���� ���� �����ϰ� ��ǰ�� ������ �������� ���� �͸� ���Ḧ �����Դϴ�. 
ĳ���ٻ� �͸��� ����� ��Ʈ�븮 ��Ʈ��ũ���� �͸� Ư���� ����� ���� ���� �� ��� �־��. 
�ݷ����׷Ѱ� ��ȭ������ ���� ��ǰ÷������ ���� �ʾ� ���� ���ذ� ����� ���̿��� �ּŵ� �����ϴ�. 
���̺��� �����, �� ������ ��ħ���� �� �ܾ� ���������. �δ� ���� ����� �Ϸ縦 �����Ͻ� �� ���� �ſ���.', '��������', 1000);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'oatmilk1.jpg', 29);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'oatmilk2.jpg', 29);

insert into product_images(pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval, 'oatmilk3.jpg', 29);


commit;

----------------------------'DIY', '��ä/���----------------------------------------------
insert into product_package(pacnum, pacname, paccontents,  pacimage)
values(seq_product_Package_pacnum.nextval
, '����� ���� ������ 6��'
, '£�� ����� ��ä�Ҵ� ���̼������� �̳׶�, ö��, ��Ÿ��, ���� �� ���� ������� ǳ���ѵ���. ��ä���� ������� ȿ�������� �����ϴ� ����� �����ϱ��? ������ �ʰ� ������ �Դ� ������. �ø��� �������� �ǰ��� ���� �����ϰ� ��� �� �ִ� �����带 �غ��߽��ϴ�. ���ݾ� �ٸ� ���� �����, ������� ���� ��ä�Ҹ� �� ������ ����������.'
, '1470792312213l0.jpg' );
--1
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'����� ���� ������ 6��','��ä/���','���̾�Ʈ��','NEW','ũ�������� �̺�Ʈ','���� �θ��� 40g(1,800��)',2000,1800,180,10
,'�θ��ô� �θ��ε��� ��� �Դ� ���߶�� �Ͽ� �̸��� ���� �θ����� ���߰��� ä������ ���߿� �޸� ������ ���� �ô� ���� �ƻ��� �İ��� �پ�ϴ�. ������ �ణ ������ ���� ������ �ٵ��� �� �ް� ����մϴ�.'
,'���ȸ����� �̷���(��)',default,'�θ��λ���'
,40,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'gv40000001409_1.jpg', 30);


insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'����� ���� ������ 6��','��ä/���','���̾�Ʈ��','NEW','ũ�������� �̺�Ʈ','���� �׸��ͽ� 40g(1,800��)',2000,1800,180,default
,'�پ��� ������ŭ ������ �ٸ� ���� ���� ��ä�ҵ�. �� ������ � ä�ҵ���, ��� �ͽ��ұ� ����� �ǰ� �ϴµ���. �ø��� �ణ�� �ܸ��� �Խ����� ��췯���Բ� �θ���, ���ٴ�, ġĿ���� �ϳ��� ��Ҿ��. ������ �� ������ �پ��� ��ä���� ��ȭ�� ��ܺ�����.'
,'���ȸ����� �̷���(��)',default,'�θ��λ���,���ٴ�,ġĿ��'
,40,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'gv00000001408_1.jpg', 31);

insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'����� ���� ������ 6��','��ä/���','���̾�Ʈ��','NEW','ũ�������� �̺�Ʈ','���� ����� 60g(1,850��)',2100,1850,185,default
,'�츮���� �ʹ����� ģ���ϰ� �ͼ��� ä���� ����ߴ� ���� ���̳� ���� ������ ������ ��ü�� 94~95%�� �����ϰ� �־� �̱׷����� �״�� �������ϴ�. ���� �Ծ�� ���� �ս��� ���� �� �����Ƿ� ������ġ�� �����带 ����� �Դ� ���� ���ƿ�. �ƻ��ϰ� �ô� ���� ���� �����忣 ������ �ʰ� ���� ä������.'
,'���ȸ����� �̷���(��)',default,'�����'
,60,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'gv30000001410_1.jpg', 32);

insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'����� ���� ������ 6��','��ä/���','���̾�Ʈ��','NEW','ũ�������� �̺�Ʈ','���� �����+��ä 80g (1,700��)',2000,1700,170,default
,'����߿� ��ä ������� ��⸦ ���� �� ���� �������. ���� ������� ��ä�� ����ߴ� ������ ������, �ܹ���, ź��ȭ�� ���� ���� �սǵǰ� ��Ȳ�̶�� ������ �ֹ߼����� ���� ���� �������� ������ ������ �Դ� ���� ���ƿ�. ���� ���ڻ��� ���� ��ä�� ��Ư�� ���� ���п� �����带 ���� ���򽺷��� ���̰� ����. �� �����尡 �ƴϾ ������̳� �ø��� ���, ��� ���� Ȱ���ص� ���ƿ�.'
,'���ȸ����� �̷���(��)',default,'��ä,�����'
,80,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'gv10000001411_1.jpg', 33);
--5
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'����� ���� ������ 6��','��ä/���','���̾�Ʈ��','NEW','ũ�������� �̺�Ʈ','������ī �����̽� 100g (2,700��)',3000,2700,270,default
,'����߿� ��ä ������� ��⸦ ���� �� ���� �������. ���� ������� ��ä�� ����ߴ� ������ ������, �ܹ���, ź��ȭ�� ���� ���� �սǵǰ� ��Ȳ�̶�� ������ �ֹ߼����� ���� ���� �������� ������ ������ �Դ� ���� ���ƿ�. ���� ���ڻ��� ���� ��ä�� ��Ư�� ���� ���п� �����带 ���� ���򽺷��� ���̰� ����. �� �����尡 �ƴϾ ������̳� �ø��� ���, ��� ���� Ȱ���ص� ���ƿ�.'
,'���ȸ����� �̷���(��)',default,'��ä,�����'
,100,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'gv40000001412_1.jpg', 34);
--6
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'����� ���� ������ 6��','��ä/���','���̾�Ʈ��','NEW','ũ�������� �̺�Ʈ','���ä�� �ͽ� 40g (1,900��)',2500,1900,190,default
,'�ε巴�� �Ʊ��ڱ��� �İ��� ��������� �������Դϴ�. ä�Ұ� ������ �ڶ� ���������� �� ��Ȯ�� ����� ���� ������, ���忡 �ʿ��� ����Ҹ� �����ϰ� �ִ� ���� Ư¡�̿���. �������� ���� �ʾ� ä�Ҹ� �������� �ʴ� ����鵵 �δ� ���� ��� �� �ֽ��ϴ�..'
,'���ȸ����� �̷���(��)',default,'���'
,40,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'gv40000001413_1.jpg', 35);

commit;



select *
from product;

select *
from product_images;

select *
from product_package;

---------------------- 'DIY', '����'----------------------------------
insert into product_package(pacnum, pacname, paccontents,  pacimage)
values(seq_product_Package_pacnum.nextval
, '���� ����� ���� ���� 3��'
, '���ٶ��� �ұ� �����ϸ� ���� �����Դϴ�. �ܿ쳻 �̺Ҽӿ��� Ƽ��� �Բ� ������ ��Ÿ���� �����ϼ���. �ٸ� ���Ͽ� ���� ���� ģȯ������ ����� ���� ���� ������ ����ϴ�.'
, '1510036387296l0.jpg' );
--1
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'���� ����� ���� ���� 3��','����','�Ļ���/���Ŀ�','BEST','���� �̺�Ʈ','���� ����� ���� ���� 800g(4,900��)',5000,4900,490,default
,'���ٶ��� �ұ� �����ϸ� ���� �����Դϴ�. �ܿ쳻 �̺Ҽӿ��� Ƽ��� �Բ� ������ ��Ÿ���� �����ϼ���. �ٸ� ���Ͽ� ���� ���� ģȯ������ ����� ���� ���� ������ ����ϴ�.'
,'�ڿ�����',default,'����'
,800,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'111.jpg', 36);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'7f7322460c4afb3ed2a4.jpg', 36);
--2
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'���� ����� ���� ���� 3��','����','�Ļ���/���Ŀ�','BEST','���� �̺�Ʈ','���� ����� ���� ���� 2kg(10,800��)',11000,10800,1080,default
,'���ٶ��� �ұ� �����ϸ� ���� �����Դϴ�. �ܿ쳻 �̺Ҽӿ��� Ƽ��� �Բ� ������ ��Ÿ���� �����ϼ���. �ٸ� ���Ͽ� ���� ���� ģȯ������ ����� ���� ���� ������ ����ϴ�.'
,'�ڿ�����',default,'����'
,2000,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'222.jpg', 37);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'7f7322460c4afb3ed2a45.jpg', 37);
--3
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'���� ����� ���� ���� 3��','����','�Ļ���/���Ŀ�','BEST','���� �̺�Ʈ','���� ����� ���� ���� 1kg(4,400��)',5000,4400,440,default
,'���ٶ��� �ұ� �����ϸ� ���� �����Դϴ�. �ܿ쳻 �̺Ҽӿ��� Ƽ��� �Բ� ������ ��Ÿ���� �����ϼ���. �ٸ� ���Ͽ� ���� ���� ģȯ������ ����� ���� ���� ������ ����ϴ�.'
,'�ڿ�����',default,'����'
,1000,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'333.jpg', 38);
-------------------------'���/�ް�'-------------------------------
---------------------------------------------------------------
insert into product_package(pacnum, pacname, paccontents,  pacimage)
values(seq_product_Package_pacnum.nextval
, '[�����] �� �Ƚɻ�'
, '����ϰ� ����� ���� ���� ���� ���� ����Ұ� �ٷ� �����Ǿ� ������ �δ� ���� ��� �� �ִ� ������ ���, �߰��! ��ܹ� ��Į�θ� ��ǰ���� �� �˷��� ����Ŀ� ������ �� ���� �ͼ��� ��ǰ����. ���� ���� ���� �ٸ� ���� �İ� ���п�, ���⺰�� ��� �Ա⵵ ��������. ���߿����� ���� �߶� �ʿ� ���� �� �� ���ڱⰡ �ִ� ������ ���� �е��� ����, �Ա� ���� ������ �� �Ƚɻ��� �غ��߾��. ���� ���� ��� ���ļ� �����ϰ� �����忡 ����̴� �ͺ��� ��¦ ���� �Ա� ���� ��� ������ ������ �پ��ϰ� Ȱ�� ������ �� �Ƚɻ�� Ǫ���� �� �� �Ļ縦 ����������!'
, '1472727892110l0.jpg' );


insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'[�����] �� �Ƚɻ�','���/�ް�','�Ļ���/���Ŀ�','BEST','���� �̺�Ʈ','���׻��� ģȯ�� �߰�� �� �Ƚɻ� (300g)',5000,4100,410,default
,'����ϰ� ����� ���� ���� ���� ���� ����Ұ� �ٷ� �����Ǿ� ������ �δ� ���� ��� �� �ִ� ������ ���, �߰��! ��ܹ� ��Į�θ� ��ǰ���� �� �˷��� ����Ŀ� ������ �� ���� �ͼ��� ��ǰ����. ���� ���� ���� �ٸ� ���� �İ� ���п�, ���⺰�� ��� �Ա⵵ ��������. ���߿����� ���� �߶� �ʿ� ���� �� �� ���ڱⰡ �ִ� ������ ���� �е��� ����, �Ա� ���� ������ �� �Ƚɻ��� �غ��߾��. ���� ���� ��� ���ļ� �����ϰ� �����忡 ����̴� �ͺ��� ��¦ ���� �Ա� ���� ��� ������ ������ �پ��ϰ� Ȱ�� ������ �� �Ƚɻ�� Ǫ���� �� �� �Ļ縦 ����������!'
,'(��)ü���η�',default,'��'
,300,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'gv00000001159_1.jpg', 39);

---------------------------'����'------------------------------------
insert into product_package(pacnum, pacname, paccontents,  pacimage)
values(seq_product_Package_pacnum.nextval
, '[Sea to Table] FROYA SALMON �븣���� ������ 2��'
, '����� ��� �ڸ��Ŀ� ���� �� �İ��� ǳ�̰� �޶����� ��ǥ���� ���� �� �ϳ�����. �̹��� �Ұ��ϴ� FROYA SALMON�� ���� ���ϴ� �β��� ���� �߶� ��� �� �ִ� ���� �������Դϴ�. �븣������ û���� �ٴٿ��� ��� ��ȹ�� �� ��ٷ� �������� �����ϰ� ����������. �� ��� ������ �ϼ��ϴµ� �ɸ��� �ð��� 2�ð� �̳�. ��ó�� �ż��� ��� �ø��� �װ����� �̿��� ���� ��ſԽ��ϴ�. 

���� �� ���������� ����Ǵ� �ð��� ���� ���� �� �ѹ��� �õ� ���� ���� ��ٷӰ� ���� �����̱� ������ ź�� �ְ� ������ ������ �״�� �����ϰ� �־��. �� �� �Ծ��, ���� ������ ����� ���� �� ����ִٴ� ���� ������ �� ���� �ſ���. FROYA SALMON�� ����� �����̽� ���� ���� �ʷ�(���) �����̱� ������ �� �̱׷��� ȸ�� ���� ���� ���� ���� ��� ���� ������ũ�� Ȱ���ص� �ջ��� ����ϴ�.'
, '153925309868l0.jpg' );

--1
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'[Sea to Table] FROYA SALMON �븣���� ������ 2��','����','�Ļ���/���Ŀ�','HIT','���� �̺�Ʈ','[Sea to Table]FROYA SALMON �븣���� ���� ��� 800g(17,800��)',19000,17800,1780,default
,'���� �� ���������� ����Ǵ� �ð��� ���� ���� �� �ѹ��� �õ� ���� ���� ��ٷӰ� ���� �����̱� ������ ź�� �ְ� ������ ������ �״�� �����ϰ� �־��. �� �� �Ծ��, ���� ������ ����� ���� �� ����ִٴ� ���� ������ �� ���� �ſ���. FROYA SALMON�� ����� �����̽� ���� ���� �ʷ�(���) �����̱� ������ �� �̱׷��� ȸ�� ���� ���� ���� ���� ��� ���� ������ũ�� Ȱ���ص� �ջ��� ����ϴ�.'
,'��羾Ǫ��',default,'����'
,300,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'gv30000033309_1.jpg', 40);

--2
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'[Sea to Table] FROYA SALMON �븣���� ������ 2��','����','�Ļ���/���Ŀ�','HIT','���� �̺�Ʈ','[Sea to Table]FROYA SALMON �븣���� ���� ��� 180g(13,0000��)',15000,13000,1300,default
,'���� �� ���������� ����Ǵ� �ð��� ���� ���� �� �ѹ��� �õ� ���� ���� ��ٷӰ� ���� �����̱� ������ ź�� �ְ� ������ ������ �״�� �����ϰ� �־��. �� �� �Ծ��, ���� ������ ����� ���� �� ����ִٴ� ���� ������ �� ���� �ſ���. FROYA SALMON�� ����� �����̽� ���� ���� �ʷ�(���) �����̱� ������ �� �̱׷��� ȸ�� ���� ���� ���� ���� ��� ���� ������ũ�� Ȱ���ص� �ջ��� ����ϴ�.'
,'��羾Ǫ��',default,'����'
,180,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'gv30000033313_1.jpg', 41);
--3
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'[Sea to Table] FROYA SALMON �븣���� ������ 2��','����','�Ļ���/���Ŀ�','HIT','���� �̺�Ʈ','����(1,500��)',1500,1500,150,default
,'���� �� ���������� ����Ǵ� �ð��� ���� ���� �� �ѹ��� �õ� ���� ���� ��ٷӰ� ���� �����̱� ������ ź�� �ְ� ������ ������ �״�� �����ϰ� �־��. �� �� �Ծ��, ���� ������ ����� ���� �� ����ִٴ� ���� ������ �� ���� �ſ���. FROYA SALMON�� ����� �����̽� ���� ���� �ʷ�(���) �����̱� ������ �� �̱׷��� ȸ�� ���� ���� ���� ���� ��� ���� ������ũ�� Ȱ���ص� �ջ��� ����ϴ�.'
,'��羾Ǫ��',default,'����'
,120,default,default,default);


insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'aaaa1234.jpg', 42);


--------------------------'�ҽ�'------------------------------------
insert into product_package(pacnum, pacname, paccontents,  pacimage)
values(seq_product_Package_pacnum.nextval
, '[�ƺ���Ʈ] �ƺ�ī�� ���� �巹�� 3��'
,'�� ���迡 ���� �ִ� �ƺ�ī���� ���� 100�� ��! �̷��� �پ��� �ƺ�ī�� �� �츮�� �ַ� �����ϴ� �ƺ�ī���� �ٷ� �Ͻ�(Hass) ���Դϴ�. �Ͻ� �ƺ�ī���� �̱� ���� Ķ�����Ͼƿ��� �絹�� �Ͻ��� ó�� ���� �� �뷮 ��迡 ������ �� �̸��� �������, ���� ����� ������ ��û�ö ����ް� ����. �ε巯�� ������ ���� �Է��� ���� ������ ������, �� ������ ��κ��� �츮 ���� �ǰ��� �����ϴ� ���� ������ �Ǵ� ����ȭ������̶� �ƺ�ī���� ��ǥ���� ����Ǫ��ε� �ղ����µ���. �ƺ���Ʈ�� Hass ǰ���� �ƺ�ī���� �þ����Ͽ� ǰ���� ���� �ƺ�ī�� ������ ���� �� �� ���Ϸ� ���ִ� �巹���� ��������ϴ�.'
, '1531731343385l0.jpg' );



--1
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'[�ƺ���Ʈ] �ƺ�ī�� ���� �巹�� 3��','�ҽ�','ä�������ڿ�','NEW','���� �̺�Ʈ','[�ƺ���Ʈ]������� (6,600��)',7000,6600,660,default
,'���̽��� ��Ʈ : ����� ���� ǳ�̴� ������ ��ä�� ���� ������ �̷���. ����ϰ� ����� �巹���̶� �η�η� ����Ͻñ⿡ ���ƿ�.'
,'HINKLE FINE FOODS',default,'�ƺ�ī��,����'
,237,default,default,default);


insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'oil1.jpg', 43);
--2
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'[�ƺ���Ʈ] �ƺ�ī�� ���� �巹�� 3��','�ҽ�','ä�������ڿ�','NEW','���� �̺�Ʈ','[�ƺ���Ʈ]������ �߻�� (6,600��)',7000,6600,660,default
,'���̽��� ��Ʈ : �߻���� �����ϸ鼭�� ���� ���� ���� �������� ���� �����ϰ� ������ ���ٸ� ������ ��� �� �־��.'
,'HINKLE FINE FOODS',default,'������'
,237,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'oil2.jpg', 44);
--3
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'[�ƺ���Ʈ] �ƺ�ī�� ���� �巹�� 3��','�ҽ�','ä�������ڿ�','NEW','���� �̺�Ʈ','[�ƺ���Ʈ]   ���� ��Ʈ���� (6,600��)',7000,6600,660,default
,'���̽��� ��Ʈ : �ƺ�ī�� ���� Ư���� ����� ���� ������ �����, ��Ʈ������ ��ŭ���� �����ϰ� ��췯���ϴ�.'
,'HINKLE FINE FOODS',default,'�ƺ�ī��,����'
,237,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'oil3.jpg', 45);

----------------------'����ǰ'-----------------------------------------
insert into product_package(pacnum, pacname, paccontents,  pacimage)
values(seq_product_Package_pacnum.nextval
, '[���ϸ���] ����� ����'
,'���׽��� ������ ������������ ������ û���� ��â, �ʸ� ��� ������ �ٶ� �ӿ� �ູ�� ���Ұ� �ڶ��ϴ�. �ϴð� ���� ����� �����ϰ� ��Ƴ��� ���ϸ����� �������� �� �ູ�� ���� ������. �����ֵ��� ���� ���������� ���� �ҵ��� ���̿� �� �ϳ������� ��Ȧ�� ���� �ʽ��ϴ�. �̼��� ���ͷ� ���� �̻����� 99.9% �ɷ����� ����ũ������ ������ ����� �µ��� �ð��� �ΰ��� ������ �� �����ϰ� ��Ƴ���. õ���� ȯ�濡�� �ູ�ϰ� ������� ������ ������ �������ε�, �ٻ� ��ħ�� ����� �Ļ�ε� �����ϴ�. ���ϸ����� ����� ������ ������ ������ ���� ������ �ǰ��ϰ� ì�ܺ�����.'
, '1502350543293l0.jpg' );

--1
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'[���ϸ���] ����� ����','����ǰ','�Ļ���/���Ŀ�','NEW','���� �̺�Ʈ','����� ���� 750ml(4,300��)',4500,4300,430,default
,'���ϸ��� ����� �������� ����� ��� ���� ���� ���缺�б��� ǳ���մϴ�. ���� ���忡�� ������ ����� �������� �� ¥�� ������ �ż����� ������ �������. ����� ���� �� ������ �ູ�� �ڿ��� �� �״�θ� �����غ�����'
,'��������',default,'����'
,750,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'milk1.jpg', 46);

--2
insert into product(pnum,fk_pacname,fk_sdname,fk_ctname,fk_stname,fk_etname,pname,price,saleprice,point,pqty,pcontents,pcompanyname,pexpiredate,allergy,weight,salecount,plike,pdate)
values(seq_product_pnum.nextval,'[���ϸ���] ����� ����','����ǰ','�Ļ���/���Ŀ�','NEW','���� �̺�Ʈ','����� ��������� 750ml(4,400��)',4500,4300,430,default
,'������ �����Է��� �δ㽺������ �е��� ������ ������ �����غ�����. �����Է��� ���̰�, ������ ǳ���� ���缺���� �״�� ��Ƴ� ����� ������ ������ ������ ����� ���� �ڶ��մϴ�.'
,'��������',default,'����'
,750,default,default,default);

insert into product_images (pimgnum, pimgfilename, fk_pnum)
values(seq_product_images_pimgnum.nextval,'milk2.jpg', 47);
--------------------------------------------------------------------------------


select *
from product
order by pnum asc;


select *
from product_images
order by pimgnum asc;

select *
from product_package
order by pacnum asc;

commit;

