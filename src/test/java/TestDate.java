import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.microBusiness.manage.dao.ass.AssCustomerRelationDao;
import com.microBusiness.manage.entity.ass.AssCustomerRelation;
import com.microBusiness.manage.service.ass.AssCustomerRelationService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext-test.xml"})
public class TestDate {

	@Resource
    private AssCustomerRelationDao assCustomerRelationDao ;
	@Resource
    private AssCustomerRelationService assCustomerRelationService ;
    
    
    @Test
    @Rollback(false) 
    @Transactional
    public void Test2(){
    	try {
    		List<AssCustomerRelation> list=assCustomerRelationDao.findAll();
    		for (AssCustomerRelation assCustomerRelation : list) {
    			if (assCustomerRelation.getAssGoods().size() == 1) {
    				assCustomerRelation.setType(AssCustomerRelation.Type.single);
    			}else {
    				assCustomerRelation.setType(AssCustomerRelation.Type.many);
    			}
    			assCustomerRelationDao.merge(assCustomerRelation);
    		}
//    		AssCustomerRelation assCustomerRelation=assCustomerRelationService.find(1164l);
//    		assCustomerRelation.setType(AssCustomerRelation.Type.many);
//System.out.println(assCustomerRelationDao.isManaged(assCustomerRelation));
    		//    		if (assCustomerRelation.getAssGoods().size() == 1) {
//				assCustomerRelation.setType(AssCustomerRelation.Type.single);
//			}else {
//				assCustomerRelation.setType(AssCustomerRelation.Type.many);
//			}
//			assCustomerRelationService.update(assCustomerRelation);
			
    		System.out.println("-------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
