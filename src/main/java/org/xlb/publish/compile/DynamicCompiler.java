package org.xlb.publish.compile;

/**
 * @Auther: Jack Xie
 * @Date: 2018/10/29/029 17:47
 * @Description:
 */
import org.xlb.publish.svn.tools.ConsoleLog;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;


public class DynamicCompiler extends ConsoleLog {
    public final static String COMPILE_TYPE_WEB = "web";
    public final static String COMPILE_TYPE_JAR = "jar";
    public final static String COMPILE_TYPE_EXEC = "exec";
    private String baseDir = "";
    private String jars = "";

    public DynamicCompiler(){
        super.log = true;
    }

    /**
     * 判断字符串是否为空 有值为true 空为：false
     */
    public boolean isnull(String str) {
        if (null == str) {
            return false;
        } else if ("".equals(str)) {
            return false;
        } else if (str.equals("null")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 编译java文件
     *
     * @param encoding    编译编码
     * @param jars        需要加载的jar
     * @param filePath    java源文件存放目录
     * @param sourceDir   resources文件存放目录
     * @param targetDir   编译后class类文件存放目录
     * @param diagnostics 存放编译过程中的错误信息
     * @return
     * @throws Exception
     */
    public boolean compiler(String compileType, String encoding, String jars, String filePath,
                            String sourceDir, String targetDir, DiagnosticCollector<JavaFileObject> diagnostics)
            throws Exception {
        if(DynamicCompiler.COMPILE_TYPE_WEB.equals(compileType))
            targetDir += "/WEB-INF/classes";
        // 获取编译器实例
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 获取标准文件管理器实例
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        try {
            if (!isnull(filePath) && !isnull(sourceDir) && !isnull(targetDir)) {
                return false;
            }
            // 拷贝resources文件
            if(DynamicCompiler.COMPILE_TYPE_WEB.equals(compileType))
                this.copyFolder(sourceDir,targetDir+"/..");
            else
                this.copyFolder(sourceDir,targetDir);

            // 得到filePath目录下的所有java源文件
            File sourceFile = new File(filePath);
            List<File> sourceFileList = new ArrayList<File>();
            getSourceFiles(sourceFile, sourceFileList);
            // 没有java文件，直接返回
            if (sourceFileList.size() == 0) {
                super.log(filePath + "目录下查找不到任何java文件");
                return false;
            }
            // 获取要编译的编译单元
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFileList);
            /**
             * 编译选项，在编译java文件时，编译程序会自动的去寻找java文件引用的其他的java源文件或者class。 -sourcepath选项就是定义java源文件的查找目录， -classpath选项就是定义class文件的查找目录。
             */
            Iterable<String> options = Arrays.asList("-encoding", encoding, "-classpath", this.getJarFiles(jars), "-d", targetDir, "-sourcepath", filePath);
            CompilationTask compilationTask = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
            // 运行编译任务
            compilationTask.call();
            baseDir = targetDir;
            if(!DynamicCompiler.COMPILE_TYPE_WEB.equals(compileType))
                this.generateJar(targetDir);

            return true;
        } finally {
            fileManager.close();
        }
    }

    /**
     * 查找该目录下的所有的java文件
     *
     * @param sourceFile
     * @param sourceFileList
     * @throws Exception
     */
    private void getSourceFiles(File sourceFile, List<File> sourceFileList) throws Exception {
        if (sourceFile.exists() && sourceFileList != null) {//文件或者目录必须存在
            if (sourceFile.isDirectory()) {// 若file对象为目录
                // 得到该目录下以.java结尾的文件或者目录
                File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory()) {
                            return true;
                        } else {
                            String name = pathname.getName();
                            if (name.endsWith(".java") ? true : false) {
                                return true;
                            }
                            return false;
                        }
                    }
                });
                // 递归调用
                for (File childFile : childrenFiles) {
                    getSourceFiles(childFile, sourceFileList);
                }
            } else {// 若file对象为文件
                sourceFileList.add(sourceFile);
            }
        }
    }

    /**
     * 查找该目录下的所有的jar文件
     *
     * @param jarPath
     * @throws Exception
     */
    private String getJarFiles(String jarPath) throws Exception {
        File sourceFile = new File(jarPath);
        // String jars="";
        if (sourceFile.exists()) {// 文件或者目录必须存在
            if (sourceFile.isDirectory()) {// 若file对象为目录
                // 得到该目录下以.java结尾的文件或者目录
                File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory()) {
                            return true;
                        } else {
                            String name = pathname.getName();
                            if (name.endsWith(".jar") ? true : false) {
                                jars = jars + pathname.getPath() + ";";
                                return true;
                            }
                            return false;
                        }
                    }
                });
            }
        }
        return jars;
    }

    /**
     * 复制整个文件夹内容
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    private void copyFolder(String oldPath, String newPath) throws Exception {
        (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
        File a=new File(oldPath);
        String[] file=a.list();
        File temp=null;
        for (int i = 0; i < file.length; i++) {
            if(oldPath.endsWith(File.separator)){
                temp=new File(oldPath+file[i]);
            }
            else{
                temp=new File(oldPath+File.separator+file[i]);
            }

            if(temp.isFile()){
                FileInputStream input = new FileInputStream(temp);
                FileOutputStream output = new FileOutputStream(newPath + "/" +
                        (temp.getName()).toString());
                byte[] b = new byte[1024 * 5];
                int len;
                while ( (len = input.read(b)) != -1) {
                    output.write(b, 0, len);
                }
                output.flush();
                output.close();
                input.close();
            }
            if(temp.isDirectory()){//如果是子文件夹
                copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
            }
        }
    }

    public void generateJar(String targetPath) throws FileNotFoundException, IOException {
        baseDir = baseDir.replace("\\","/");
        baseDir = baseDir.replace("//","/");
        baseDir = baseDir.replace("/","\\");
        super.log("开始生成jar包...");
//        Manifest manifest = new Manifest();
//        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
//        JarOutputStream target = new JarOutputStream(new FileOutputStream(targetPath+".jar"),manifest);

        JarOutputStream target = new JarOutputStream(new FileOutputStream(targetPath+".jar"));
        writeClassFile(new File(targetPath), target);
        target.close();
        super.log("jar包生成完毕。");
    }

    private void writeClassFile(File source, JarOutputStream target) throws IOException {
        BufferedInputStream in = null;
        try {
            if (source.isDirectory()) {
                for (File nestedFile : source.listFiles())
                    writeClassFile(nestedFile, target);
                return;
            }else{
                String middleName = source.getPath().replace(baseDir+"\\","");
                JarEntry entry = new JarEntry(middleName);
                entry.setTime(source.lastModified());
                target.putNextEntry(entry);
                in = new BufferedInputStream(new FileInputStream(source));

                byte[] buffer = new byte[1024];
                while (true) {
                    int count = in.read(buffer);
                    if (count == -1)
                        break;
                    target.write(buffer, 0, count);
                }
                target.closeEntry();
            }
        } finally {
            if (in != null)
                in.close();
        }
    }

    public static void main(String[] args) {
        try {
//            String filePath = "E:\\workspace\\COD-MS\\src";
//            String sourceDir = "E:\\workspace\\COD-MS\\src";
//            String jarPath = "E:\\workspace\\COD-MS\\WebRoot\\WEB-INF\\lib";
//            String targetDir = "E:\\java\\project\\bin";
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
            DynamicCompiler dynamicCompilerUtil = new DynamicCompiler();
            dynamicCompilerUtil.generateJar("E:/warPublish//qhq_manager/WebContent");

//            boolean compilerResult = dynamicCompilerUtil.compiler("web","UTF-8", dynamicCompilerUtil.getJarFiles(jarPath), filePath, sourceDir, targetDir, diagnostics);
//            if (compilerResult) {
//                super.log("编译成功");
//            } else {
//                super.log("编译失败");
//                for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
//                    // System.out.format("%s[line %d column %d]-->%s%n", diagnostic.getKind(), diagnostic.getLineNumber(),
//                    // diagnostic.getColumnNumber(),
//                    // diagnostic.getMessage(null));
//                    super.log(diagnostic.getMessage(null));
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}