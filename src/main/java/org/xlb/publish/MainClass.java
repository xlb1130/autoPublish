package org.xlb.publish;

import java.util.List;
import java.util.Scanner;

import org.xlb.publish.bean.PublishBean;
import org.xlb.publish.task.CompileJob;
import org.xlb.publish.task.PublishJob;
import org.xlb.publish.task.StartJob;
import org.xlb.publish.util.ImageToAscii;

/**
 * 程序入口
 * @author Linbo Xie
 * @since 2015 03 11
 * @version V1.0
 *
 */
public class MainClass {
	private static void printWelcome(String option,String configFile){
		try {
			ImageToAscii.printImage();
		} catch (Exception e) {e.printStackTrace();}
		 System.out.println("----------------------------------------------------------------------");
		 System.out.println("|                         QUICK   PUBLISH TOOL                       |");
		 System.out.println("|                        Design by Lingbo Xie V1.1                   |");
		 System.out.println("----------------------------------------------------------------------");
		 System.out.println("Use config file: "+System.getProperty("user.dir")+configFile+"   to complete "+option+".....");
		 System.out.println("Please input yes/no to confirm/cancel your operation!");
	}
	
	private static void printWelcomeNoConfirm(){
		try {
			ImageToAscii.printImage();
		} catch (Exception e) {e.printStackTrace();}
		 System.out.println("----------------------------------------------------------------------");
		 System.out.println("|                         QUICK   PUBLISH TOOL                       |");
		 System.out.println("|                        Design by Lingbo Xie V1.1                   |");
		 System.out.println("----------------------------------------------------------------------");
	}
	
	public static void main(String[] args) throws Exception{
		String configFile = "";
		String para = "";
		args = new String[]{"-compile","src/main/resources/config.xml"};
		if (args.length>0 && args.length == 2){
			para = args[0];
			configFile = args[1];
		}else{
			System.out.println("Usage: java -jar [jarName] [-start|-check|-compile|-publish|-all]  configFilePath.");
			System.exit(0);
		}
		InitSvr init  = null;
		if (configFile == null || "".equals(configFile)){
			 init = InitSvr.getInstance();
			 Thread t = new Thread(new StartJob(init.getBeans()));
			 t.start();
		}else{
			if ("-check".equals(para)){
				printWelcomeNoConfirm();
				init = InitSvr.getInstance(configFile);
				List<PublishBean> beans = init.getBeans();
				for (int i=0; i<beans.size();i++){
					System.out.println(beans.get(i));
				}
				 System.exit(0); 
			}else if ("-start".equals(para)){
				 printWelcome("start",configFile);
				 Scanner scan = new Scanner(System.in);
				 String input = "";
				 while ("".equals(input)){
					 input=scan.nextLine().trim();
				 }
				 if(input.toUpperCase().equals("YES")){
					 System.out.println("Starting..... Please wait......");
					 init = InitSvr.getInstance(configFile);
					 Thread t = new Thread(new StartJob(init.getBeans()));
					 t.start();
				 }	else{
					 System.out.println("Operation canceled by user!");
					 System.exit(0); 
				 }
			}else if ("-publish".equals(para)){
				 printWelcome("publish",configFile);
				 Scanner scan = new Scanner(System.in);
				 String input = "";
				 while ("".equals(input)){
					 input=scan.nextLine().trim();
				 }
				 if(input.toUpperCase().equals("YES")){
					 System.out.println("Publishing..... Please wait......");
					 init = InitSvr.getInstance(configFile);
					 Thread t = new Thread(new PublishJob(init.getBeans()));
					 t.start();
				 }	else{
					 System.out.println("Operation canceled by user!");
					 System.exit(0); 
				 }
			}else if ("-compile".equals(para)){
				printWelcome("compile",configFile);
				Scanner scan = new Scanner(System.in);
				String input = "";
				while ("".equals(input)){
					input=scan.nextLine().trim();
				}
				if(input.toUpperCase().equals("YES")){
					System.out.println("Compiling..... Please wait......");
					init = InitSvr.getInstance(configFile);
					Thread t = new Thread(new CompileJob(init.getBeans()));
					t.start();
				}	else{
					System.out.println("Operation canceled by user!");
					System.exit(0);
				}
			}else if ("-all".equals(para)){
				 printWelcome("publish and start",configFile);
				 Scanner scan = new Scanner(System.in);
				 String input = "";
				 while ("".equals(input)){
					 input=scan.nextLine().trim();
				 }
				 if(input.toUpperCase().equals("YES")){
					 init = InitSvr.getInstance(configFile);
					 Thread t = new Thread(new PublishJob(init.getBeans()));
					 t.start();
					 Thread t1 = new Thread(new StartJob(init.getBeans()));
					 t1.start();
				 }	else{
					 System.out.println("Operation canceled by user!");
					 System.exit(0); 
				 }
			}else{
				printWelcomeNoConfirm();
				System.out.println("Ivalid parameters ["+para+"]!");
				System.out.println("Usage: java -cp XlbPublish.jar org.xlb.publish.MainClass [-start|-check|-publish|-all]  path.");
			}		
		}
	}
}
