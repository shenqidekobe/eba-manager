import com.microBusiness.manage.dao.AreaDao;
import com.microBusiness.manage.dao.MemberDao;
import com.microBusiness.manage.dao.SupplierDao;
import com.microBusiness.manage.dao.SupplierSupplierDao;
import com.microBusiness.manage.entity.*;
import com.microBusiness.manage.entity.Order;
import com.microBusiness.manage.entity.SupplyType;
import com.microBusiness.manage.service.CategoryService;
import com.microBusiness.manage.service.SupplierService;
import com.microBusiness.manage.util.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.eclipse.jetty.util.StringUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import java.net.URLEncoder;

/**
 * Created by mingbai on 2017/1/22.
 * 功能描述：
 * 修改记录：
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-test.xml"})
public class Testaa {
    @Test
    public void test1(){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Predicate restrictions1 = criteriaBuilder.conjunction();
        Predicate restrictions = criteriaBuilder.conjunction();

        Root<Order> root = criteriaQuery.from(Order.class);
        Join<Order , OrderItemLog> orderOrderItemLogJoin = root.join("orderItemLogs" , JoinType.LEFT);
        restrictions = criteriaBuilder.and(restrictions , criteriaBuilder.equal(orderOrderItemLogJoin.get("id") , 1L));
        orderOrderItemLogJoin.on(restrictions);

        criteriaQuery.select(root);
        criteriaQuery.where(restrictions1);

        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);

        List<Order> orderList = query.getResultList();
        System.out.println(1);

    }

    @PersistenceContext
    protected EntityManager entityManager;
    public static void main(String[] args) {

        DateTime nowDay = new DateTime().withTimeAtStartOfDay() ;
        System.out.println(nowDay);
        System.exit(1);


        List<Long> longTest = null ;
        for(Long t : longTest){
            System.out.println(t);
        }



        int x = 10;

        do{
            System.out.print("value of x : " + x );
            x++;
            System.out.print("\n");
        }while( x < 10 );


        String random = RandomStringUtils.random(6 , true , true);

        System.out.println(random);

        String a = RandomStringUtils.random(3 , 65 , 90 , true , false);
        String b = RandomStringUtils.randomNumeric(3);

        System.out.println(a+b);

        Character[] characters = new Character[6];

        char[] chars = a.toCharArray();
        char[] chars2 = b.toCharArray() ;
        char[] chars3 = new char[6];


        System.arraycopy(chars , 0 , characters , 0, 3);
        System.arraycopy(chars2 , 0 , characters , 3, 3);
        List list = Arrays.asList(characters);

        Collections.shuffle(list);

        Character[] chars4 = new Character[6];

        list.toArray(chars4);

        System.out.println(2);
        /*DateTime now = new DateTime();
        System.out.println();


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY , 0);
        calendar.set(Calendar.MINUTE , 0);
        calendar.set(Calendar.SECOND , 0);
        System.out.println(calendar.getTime());


        System.out.println(Days.daysBetween(DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime("2012-12-26 19:27:39") , DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime("2012-12-27 19:26:39")).getDays());*/
        /*System.out.println(RandomStringUtils.random(2));
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

        Root<Order> buildDetailsTable = criteriaQuery.from(Order.class);

        Join<Order, OrderItemLog> qualityJoin = buildDetailsTable.join(Order_.orderItemLogs, JoinType.INNER);
        Join<BuildDetails, DeploymentDetails> deploymentJoin = buildDetailsTable.join(DeploymentDetails_.build, JoinType.INNER);
        Join<BuildDetails, TestDetails> testJoin = buildDetailsTable.join(TestDetails_.build, JoinType.INNER);*/
    }

    @Resource
    private SupplierDao supplierDao ;
    @Test
    public void Test2(){
        Supplier supplier = supplierDao.find(1L);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        //产生cross join
        restrictions = criteriaBuilder.and(criteriaBuilder.or(criteriaBuilder.equal(root.<String>get("sn") , "%有限公司%") , criteriaBuilder.like(root.get("supplier").<String>get("name"), "%有限公司%"), criteriaBuilder.equal(root.get("member").get("need").<String>get("name"), "%有限公司%")));

        criteriaQuery.where(restrictions);
        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        List<Order> orders = query.getResultList();
        System.out.println(1);
    }



    @Test
    public void test3(){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        Order.Type type = null ;
        Date startDate = new Date();
        String searchName = "有限公司";

        if(StringUtils.isNotEmpty(searchName)){
            Join<Order , Supplier> supplierJoin = root.join("supplier" , JoinType.LEFT);
            Join<Order , Member> memberJoin = root.join("member" , JoinType.LEFT);
            Join<Member , Need> needJoin = memberJoin.join("need" , JoinType.LEFT);

            restrictions = criteriaBuilder.and(criteriaBuilder.or(criteriaBuilder.equal(root.<String>get("sn") , "%有限公司%") , criteriaBuilder.like(supplierJoin.<String>get("name"), "%有限公司%"), criteriaBuilder.equal(needJoin.<String>get("name"), "%有限公司%")));

        }

        if (type != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        }


        if (startDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("reDate"), startDate));
        }

        criteriaQuery.where(restrictions);
        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        List<Order> orders = query.getResultList();
        System.out.println(1);


        System.out.println(URLEncoder.encode("/b2b/goodsList.html?needSupplierId=3&supplyType=temporary"));
        System.out.println(RandomStringUtils.random(2));

        System.out.println(SupplyType.formal.name());
    }

    @Resource
    private MemberDao memberDao ;
    @Test
    public void test4(){
        List<Object[]> members = memberDao.getMemberToNotice(0 , 100 , new Date());
        System.out.println("");
    }

    @Resource
    private SupplierService supplierService ;
    @Test
    public void getSupplierByInivateCode(){
        Supplier supplier = supplierService.getSupplierByInviteCode("bbb123") ;
        System.out.println("");
    }


    @Test
    public void testBase(){
        Character[] chars = new Character[]{'a' , 'b' , 'c'};


        List<Character> characters = Arrays.asList(chars);

        System.out.println(1);


        int[] aaa = new int[]{1,2,3,4,5,6};
        List ints = Arrays.asList(aaa);

        System.out.println(2);



    }

    @Test
    public void testAs(){
        Query query = entityManager.createNativeQuery("select need.* , supplier.name as supplierName from t_need need inner join t_supplier supplier on need.supplier=supplier.id" , Need.class);

        List<Need> needs = query.getResultList();

        Session session = entityManager.unwrap(Session.class);

        System.out.println(1);
    }
    @Test
    public void teatJpql(){
        Supplier supplier = supplierDao.getSupplierByNeed(1L);
        System.out.println(1);
    }

    @Resource
    private AreaDao areaDao ;
    @Transactional
    @Test
    public void dealArea(){
        HSSFWorkbook workbook = new HSSFWorkbook() ;
        String sheetName = "areas";
        HSSFSheet sheet;
        if (StringUtils.isNotEmpty(sheetName)) {
            sheet = workbook.createSheet(sheetName);
        } else {
            sheet = workbook.createSheet();
        }


        int rowStart = 2 ;
        int cityStart = 2 ;
        int areaStart = 2 ;
        //省
        List<Area> pros = areaDao.findRoots(null);
        for(Area pro : pros){
            //for(int i = rowStart , len = pros.size() ; i <len ; i++ ){
            Row row = sheet.getRow(rowStart);
            if(null == row){
                row = sheet.createRow(rowStart) ;
            }
            Cell cell = row.createCell(0);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(pro.getName());
            //}
            rowStart++;
            Set<Area> citys = pro.getChildren() ;
            StringBuffer sb = new StringBuffer() ;
            //市
            for(Area city : citys){
                Set<Area> areas =  city.getChildren() ;
                if(StringUtils.isNotEmpty(sb.toString())){
                    sb.append(",");
                }
                sb.append(city.getName());

                Row tempRow = sheet.getRow(cityStart) ;
                if(null ==tempRow){
                    tempRow = sheet.createRow(cityStart);
                }
                Cell tempCity = tempRow.createCell(3);
                tempCity.setCellType(Cell.CELL_TYPE_STRING);
                tempCity.setCellValue(city.getName());

                cityStart ++ ;
                //区
                StringBuffer sb2 = new StringBuffer() ;
                for (Area area : areas){
                    if(StringUtils.isNotEmpty(sb2.toString())){
                        sb2.append(",");
                    }
                    sb2.append(area.getName());
                }

                Cell areaCell = tempRow.createCell(4);
                areaCell.setCellType(Cell.CELL_TYPE_STRING);
                areaCell.setCellValue(sb2.toString());

            }

            Cell cityCell = row.createCell(1);
            cityCell.setCellType(Cell.CELL_TYPE_STRING);
            cityCell.setCellValue(sb.toString());

        }

        try {
            workbook.write(new FileOutputStream("/Users/afei/demo.xls"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Resource
    private JdbcTemplate jdbcTemplate ;

    @Transactional
    @Test
    public void testTran(){
        Sms sms = new Sms();
        sms.setCode("test");
        sms.setMobile("13512565697");
        sms.setSendTime(new Date());
        sms.setStatus(Sms.Status.USED);
        sms.setType(Sms.SmsType.BINDING);
        entityManager.persist(sms);

        jdbcTemplate.update("insert into t_member values (? , ? ,?,? , ? ,?)" , new Date() , new Date() , 1L , "23123" , "dddd" , 1L);


        System.out.println(1);
    }

    @Resource
    private SupplierSupplierDao supplyRelationDao ;

    @Test
    public void testJoinOn(){
        SupplierSupplier supplyRelation = supplyRelationDao.find(1L);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Need> criteriaQuery = criteriaBuilder.createQuery(Need.class);
        Root<Need> root = criteriaQuery.from(Need.class);

        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        Predicate restrictions2 = criteriaBuilder.conjunction();


        Join<Need, SupplierAssignRelation> relationJoin = root.join("supplierAssignRelations", JoinType.LEFT);
        //Join<SupplierAssignRelation, SupplierSupplier> relationJoin1 = relationJoin.join("supplyRelation", JoinType.LEFT);
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(relationJoin.get("supplyRelation").get("id"), supplyRelation.getId()));
        relationJoin.on(restrictions);

        TypedQuery<Need> query = entityManager.createQuery(criteriaQuery);

        query.getResultList();

        System.out.println(1);

    }

    @Resource
    private CategoryService categoryService ;

    @Test
    @Transactional
    public void testCategory(){
        List<Category> categories = categoryService.findRoots() ;
        /*List<Map<String , Object>> res = new ArrayList<>() ;


        for(Category level1 : categories){
            Map<String , Object> level1Map = new HashMap<>();

            level1Map.put("value" , level1.getId());
            level1Map.put("label" , level1.getName());
            List<Map<String , Object>> level2List = new ArrayList<>() ;

            for (Category level2 : level1.getChildren()){
                Map<String , Object> level2Map = new HashMap<>();
                level2Map.put("value" , level2.getId());
                level2Map.put("label" , level2.getName());
                level2List.add(level2Map) ;

                List<Map<String , Object>> level3List = new ArrayList<>() ;
                for(Category level3 : level2.getChildren()){

                    Map<String , Object> level3Map = new HashMap<>();
                    level3Map.put("value" , level3.getId());
                    level3Map.put("label" , level3.getName());

                    level3List.add(level3Map) ;
                }
                level2Map.put("children" , level3List) ;

            }

            level1Map.put("children" , level2List) ;

            res.add(level1Map);

        }*/


        List res = this.categoryFor(new HashSet<Category>(categories)) ;

        String jsons = JsonUtils.toJson(res);

        System.out.println(jsons);

        System.out.println(1);
    }


    public List<Map<String , Object>> categoryFor(Set<Category> categories){
        List<Map<String , Object>> resut = new ArrayList<>();
        if(CollectionUtils.isEmpty(categories)){
            return resut ;
        }

        for(Category category : categories){
            Map<String , Object> values = new HashMap<>() ;
            values.put("value" , category.getId());
            values.put("label" , category.getName());
            values.put("children" , categoryFor(category.getChildren())) ;
            resut.add(values) ;
        }

        return resut ;
    }


    @Test
    public void testSpringCache(){
        List<Category> categories = categoryService.findRoots() ;
        System.out.println(1);
    }



    @Test
    public void testDate(){
        DateTime dateTime = new DateTime() ;
        DateTime.Property week = dateTime.weekOfWeekyear();
        System.out.println(dateTime.withDayOfWeek(1).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(0));
        System.out.println(dateTime.withDayOfWeek(7).withHourOfDay(0).withMillisOfDay(0).withSecondOfMinute(0));

        DateTime next = dateTime.plusWeeks(1);
        System.out.println(next.withDayOfWeek(1).withHourOfDay(0).withMillisOfDay(0).withSecondOfMinute(0));
        System.out.println(next.withDayOfWeek(7).withHourOfDay(0).withMillisOfDay(0).withSecondOfMinute(0));



        System.out.println(dateTime.dayOfMonth().withMinimumValue());
        System.out.println(dateTime.dayOfMonth().withMaximumValue());

        System.out.println(dateTime.dayOfWeek().withMinimumValue());
        System.out.println(dateTime.dayOfWeek().withMaximumValue());


        System.out.println(dateTime.plusMonths(1).dayOfMonth().withMinimumValue());

        System.out.println(dateTime.plusMonths(1).dayOfMonth().withMaximumValue());


        DateTime dateTime1 = new DateTime(new Date()) ;

        dateTime1.toDate() ;
    }

}
