import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TestActomic {

	//定义原子类
    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    
    static int x = 0;
    
    public static void increase(){
        //原子操作自增
        atomicInteger.incrementAndGet();
    }       
    public static void main(String[] args){
    	final Date date = new Date();
    	try {
			Thread.sleep(1000);
		} catch (Exception e) {
			// TODO: handle exception
		}
    
        for (int i = 0; i < 5; i++){
            new Thread(new Runnable() {
                public void run() {
                			//synchronized (TestActomic.class) {
                			//System.out.println(x++);
                				System.out.println(atomicInteger.incrementAndGet());
                            	//System.out.println(Thread.currentThread().getName());
                            	//long time = new Date().getTime() - date.getTime();
//                            	boolean sta = atomicInteger.compareAndSet(1, 0);
//                            	if(sta){
//                            		System.out.println("in.....");
//                            	}
							//}
                        	
                    
                }
            }).start();
        }
        while(Thread.activeCount()>1)
            Thread.yield();
        //if(atomicInteger.get() == 5){
        	System.out.println("x:" + atomicInteger.get());
        //}
        
    }
}
