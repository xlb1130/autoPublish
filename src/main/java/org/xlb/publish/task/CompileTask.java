package org.xlb.publish.task;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.xlb.publish.bean.PublishBean;
import org.xlb.publish.compile.DynamicCompiler;
import org.xlb.publish.svn.conf.SvnConfig;
import org.xlb.publish.svn.factory.DemoSvn;
import org.xlb.publish.svn.inf.ISvn;
import org.xlb.publish.svn.inf.service.ISvnDbLog;
import org.xlb.publish.svn.tools.ConsoleLog;
import org.xlb.publish.util.SVNUtils;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 
 * 任务编译执行类
 * @author Linbo Xie
 * @since 2018 10 29
 * @version V1.0
 *
 */
public class CompileTask extends ConsoleLog implements Runnable {

	private PublishBean publishBean;
	private ISvn svn;

	public CompileTask(PublishBean publishBean) {
		this.publishBean = publishBean;
	}

	public void run() {
		super.log = true;
		// 初始化实例
		DemoSvn ts = new DemoSvn(publishBean.getSvn().getUsername(), publishBean.getSvn().getPassword()
				, publishBean.getSvn().getPath());
		try {
			this.svn = ts.execute(SvnConfig.log);
			// 得到版本库信息
			svn.createSVNRepository();
			// 得到基础操作对象
			svn.createSVNClientManager();
			File ws = new File(new File(publishBean.getProject().getPath()), publishBean.getProject().getName());
			if(!ws.exists()) ws.mkdirs();
			if(!SVNWCUtil.isVersionedDirectory(ws)){
				svn.checkOut(publishBean.getSvn().getPath()+"/"+publishBean.getProject().getName(),
						publishBean.getProject().getPath()+"/"+publishBean.getProject().getName());
				super.log("Svn checkout "+publishBean.getSvn().getPath()+"@"+publishBean.getSvn().getUsername()+":"
						+publishBean.getSvn().getPassword()+" ................... success!");
			}else{
				svn.update(publishBean.getProject().getPath()+"/"+publishBean.getProject().getName(),
						"更新", false, new ISvnDbLog<String>() {
					@Override
					public boolean addLog(String name, SvnConfig dbType, long versionId, File[] files) {
						return true;
					}
					@Override
					public List<String> getLog(String name, Date startTime, Date endTime) {
						return null;
					}
				});
				super.log("Svn update "+publishBean.getSvn().getPath()+"@"+publishBean.getSvn().getUsername()+":"
						+publishBean.getSvn().getPassword()+" ................... success!");
			}
		} catch (Exception e) {
			super.log("Svn update/checkout "+publishBean.getSvn().getPath()+"@"+publishBean.getSvn().getUsername()+":"
					+publishBean.getSvn().getPassword()+" ................... Failed! "+e.getMessage());
			e.printStackTrace();
			return;
		}
		// 编译
		try {
			String filePath = publishBean.getProject().getPath() + "\\"
					+  publishBean.getProject().getName() + "\\"+publishBean.getPublish().getSrcPath();
			String sourceDir = publishBean.getProject().getPath() + "\\"
					+  publishBean.getProject().getName() + "\\"+publishBean.getPublish().getResourcePath();
			String jarPath = publishBean.getProject().getPath() + "\\"
					+  publishBean.getProject().getName() + "\\"+publishBean.getPublish().getLibPath();
			String targetDir =publishBean.getProject().getPath() + "\\"
					+  publishBean.getProject().getName() +"\\"+ publishBean.getPublish().getWebPath();
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
			DynamicCompiler dynamicCompilerUtil = new DynamicCompiler();
			super.log("开始编译");
			boolean compilerResult = dynamicCompilerUtil.compiler(publishBean.getPublish().getCompileType(),"UTF-8", jarPath, filePath, sourceDir, targetDir, diagnostics);
			if (!compilerResult) {
				super.log("编译失败");
				for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
					System.out.println(diagnostic.getMessage(null));
				}
			}else
				super.log("编译成功");
		} catch (Exception e) {
			super.log("Compile  "+publishBean.getProject().getPath() + "/"+  publishBean.getProject().getName() + "/src"+" ................... Failed!");
			e.printStackTrace();
			return;
		}
		return;
	}

}
