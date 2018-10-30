package org.xlb.publish.task;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.xlb.publish.bean.PublishBean;
import org.xlb.publish.util.SFtpUtil;

/**
 * 
 * 应用发布后负责按顺序启动类
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
public class StartRemoteTask implements Runnable {

	private List<PublishBean> publishBeans;
	
	private Map<String,Runnable> jobsMap;
	
	public StartRemoteTask(List<PublishBean> publishBeans, Map<String, Runnable> jobsMap){
		this.setJobsMap(jobsMap);
		this.publishBeans = publishBeans;
	}

	@SuppressWarnings("static-access")
	public void run() {
		SFtpUtil ftplist = null;
		PublishBean publishBean = null;
		Collections.sort(publishBeans, new Comparator<PublishBean>(){
			public int compare(PublishBean o1, PublishBean o2) {
				return Integer.parseInt(o1.getPublish().getIndex())
						- Integer.parseInt(o2.getPublish().getIndex());
			}		
		});
		try {
//			Iterator itor = jobsMap.keySet().iterator();
//			Thread t= null;
//			while(itor.hasNext()){
//				t = new Thread(jobsMap.get(itor.next()));
//				t.join();
//			}
			
			for(int i =0;i< publishBeans.size();i++){
				publishBean = publishBeans.get(i);
				if (!"none".equals(publishBean.getPublish().getCommand())){
					ftplist = new SFtpUtil(publishBean.getIp(), publishBean.getUsername(), publishBean.getPassword());
					ftplist.startRemoteServer(publishBean.getPublish().getCommand());
					ftplist.close();
					Thread.currentThread().sleep(Long.parseLong(publishBean.getPublish().getSleep()));
				}
				System.out.println("Start "+publishBean.getUsername()+"@"+publishBean.getIp()+":"
						+publishBean.getProject().getName()+" ...................Complete!");
			}			
		}  catch (Exception e) {
			if (ftplist != null)
				ftplist.close();
			e.printStackTrace();
		}
	}

	public Map<String,Runnable> getJobsMap() {
		return jobsMap;
	}

	public void setJobsMap(Map<String,Runnable> jobsMap) {
		this.jobsMap = jobsMap;
	}

}
