package org.xlb.publish.svn.factory;


import org.xlb.publish.svn.conf.ErrorVal;
import org.xlb.publish.svn.conf.SvnConfig;
import org.xlb.publish.svn.impl.SvnBaseImpl;
import org.xlb.publish.svn.inf.ISvn;
import org.xlb.publish.svn.model.SvnLinkPojo;

/**
 * DemoSvn主体
 * 
 * @author Allen
 * @date 2016年8月8日
 */
public final class DemoSvn {

	SvnLinkPojo svnLink;

	public SvnLinkPojo getSvnLink() {
		return svnLink;
	}

	/**
	 * 私有构造
	 */
	public DemoSvn() {
	}

	public DemoSvn(String svnAccount, String svnPassword, String repoPath) {
		this.svnLink = new SvnLinkPojo(repoPath, svnAccount, svnPassword);
	}

	/**
	 * 获取SVN操作
	 * 
	 * @param val
	 *            default 不设置日志状态 log 开启console日志状态
	 * @throws Exception 没有操作匹配
	 * @return {@link ISvn}
	 */
	public ISvn execute(SvnConfig val) throws Exception {
		ISvn is = null;
		if (val == null)
			throw new Exception(ErrorVal.SvnConfig_is_null);
		switch (val.getVal()) {
		case "normal":
			is = new SvnBaseImpl(svnLink.getSvnAccount(), svnLink.getSvnPassword(), false, svnLink.getRepoPath());
			break;
		case "log":
			is = new SvnBaseImpl(svnLink.getSvnAccount(), svnLink.getSvnPassword(), true, svnLink.getRepoPath());
			break;
		default:
			throw new Exception(ErrorVal.SvnConfig_is_null);
		}
		return is;
	}
}
