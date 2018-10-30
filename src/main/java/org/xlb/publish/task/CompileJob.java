package org.xlb.publish.task;

import org.xlb.publish.bean.PublishBean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 应用编译任务任务核心启动类
 * @author Linbo Xie
 * @since 2018 10 29
 * @version V1.0
 *
 */
public class CompileJob implements Runnable {

	private List<PublishBean> publishBeans;

	ThreadPoolExecutor threadPool;

	private Map<String, Runnable> jobsMap;

	public CompileJob(List<PublishBean> publishBeans) {
		this.publishBeans = publishBeans;
		this.threadPool = new ThreadPoolExecutor(200, 250, 60,  
	                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10),  
	                new ThreadPoolExecutor.CallerRunsPolicy());
	}

	public void run() {
		//Runnable t = null;
		//编译
		for(int i =0;i< publishBeans.size();i++){
			threadPool.execute(new CompileTask(publishBeans.get(i)));
		}
		threadPool.shutdown();
	}

}
