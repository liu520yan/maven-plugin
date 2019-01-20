package com.plugin.test;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;


@Mojo(name = "aopparamchange", defaultPhase = LifecyclePhase.COMPILE)
public class MyMojo extends AbstractMojo {

    @Parameter(property = "filePath")
    private String filePath;

    @Parameter(property = "source")
    private String source;

    @Parameter(property = "target")
    private String target;

    public void execute() throws MojoExecutionException {
        File file = new File(filePath);
        if (!file.isFile()) {
            throw new MojoExecutionException(filePath + "not a file");
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            //内存流
            CharArrayWriter caw = new CharArrayWriter();

            //替换
            String line = null;

            //以行为单位进行遍历
            while ((line = br.readLine()) != null) {
                //替换每一行中符合被替换字符条件的字符串
                line = line.replaceAll(source, target);
                //将该行写入内存
                caw.write(line);
                //添加换行符，并进入下次循环
                caw.append(System.getProperty("line.separator"));
            }
            //关闭输入流
            br.close();

            //将内存中的流写入源文件
            FileWriter fw = new FileWriter(file);
            caw.writeTo(fw);
            fw.close();
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage());
        }

    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
    