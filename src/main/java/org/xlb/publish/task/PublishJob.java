package org.xlb.publish.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.xlb.publish.bean.PublishBean;

/**
 * 
 * 应用发布任务核心启动类
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
public class PublishJob implements Runnable {
	
	private List<PublishBean> publishBeans;	
	
	private Map<String,Runnable> jobsMap;
	
	ThreadPoolExecutor threadPool;
	
	public PublishJob(List<PublishBean> publishBeans) {
		jobsMap = new HashMap<String,Runnable>();
		this.publishBeans = publishBeans;
		this.threadPool = new ThreadPoolExecutor(200, 250, 60,  
	                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10),  
	                new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void run() {
		Runnable t = null;
		//备份 打包
		for(int i =0;i< publishBeans.size();i++){
			PublishBean publishBean = publishBeans.get(i);
			t = new PublishTask(publishBean);
			jobsMap.put(publishBean.getUsername()+"@"+publishBean.getIp()+":"+publishBean.getProject().getName(), t);
			threadPool.execute(t);
		}
//		//启动
//		threadPool.execute(new StartRemoteTask(publishBeans,jobsMap));
		threadPool.shutdown();
	}

}
