import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.microBusiness.manage.dao.impl.MemberDaoImpl;
import com.microBusiness.manage.service.impl.MemberServiceImpl;

public class BeanFactoryTest {

	@Test
	public void test(){
		DefaultListableBeanFactory listableBeanFactory = new DefaultListableBeanFactory();
		MemberServiceImpl ms = listableBeanFactory.createBean(MemberServiceImpl.class);
		System.out.println(ms);
	}
	
	@Test
	public void test2(){
		DefaultListableBeanFactory listableBeanFactory = new DefaultListableBeanFactory();
		listableBeanFactory.registerSingleton("dao", new  MemberDaoImpl());
		System.out.println(listableBeanFactory.getBean("dao"));
	}
	
}
