package com.qalain.ui.util.mail;


import com.qalain.ui.config.EngineConfig;
import com.qalain.ui.core.engine.EngineProperties;
import com.qalain.ui.generator.CodeGenerator;
import com.qalain.ui.util.ReadPropertiesUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class AutoMail {

	public static void main(String[] args) throws IOException {
		MailInfo mailInfo = new MailInfo();
		String[] sendTo = readMail("sendto");
		String[] ccList = readMail("cc");

		mailInfo.setMailServerHost(ReadPropertiesUtil.getProp("mail", "MailServerHost"));
		mailInfo.setMailServerPort(ReadPropertiesUtil.getProp("mail", "MailServerPort"));
		mailInfo.setValidate(true);
		mailInfo.setUsername(ReadPropertiesUtil.getProp("mail", "Username"));
		// 邮箱密码
		mailInfo.setPassword(ReadPropertiesUtil.getProp("mail", "Password"));
		mailInfo.setFromAddress(ReadPropertiesUtil.getProp("mail", "FromAddress"));
		mailInfo.setToAddress(sendTo);
		mailInfo.setSubject(ReadPropertiesUtil.getProp("mail", "Subject"));
		mailInfo.setCcAddress(ccList);
		String[] files = {"./target/test-output/report.html"};
		mailInfo.setAttachFileNames(files);

		String mailContent = ReadHtml.readString("./target/test-output/report.html");
		String cssValueExtent = ReadHtml.readString("./target/classes/css/extent.css");
		String changeExtentStr ="<style type=\"text/css\">h1 {display : inline}"+cssValueExtent+"</style>";
		String cssValueIcon = ReadHtml.readString("./target/classes/css/icon.css");
		String changeIconStr ="<style type=\"text/css\">h1 {display : inline}"+cssValueIcon+"</style>";
		String cssValueSource = ReadHtml.readString("./target/classes/css/source.css");
		String changeSourceStr ="<style type=\"text/css\">h1 {display : inline}"+cssValueSource+"</style>";
		String jsValueExtent = ReadHtml.readString("./target/classes/js/extent.js");
		String changeExtentJsStr ="<script type='text/javascript'>" + jsValueExtent + "</script>";
		mailContent = mailContent.replace("<link href='http://extentreports.com/resx/dist/css/extent.css' type='text/css' rel='stylesheet' />", changeExtentStr);
		mailContent = mailContent.replace("<link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">", changeIconStr);
		mailContent = mailContent.replace("<link href='https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,600' rel='stylesheet' type='text/css'>", changeSourceStr);
		mailContent = mailContent.replace("<script src='http://extentreports.com/resx/dist/js/extent.js' type='text/javascript'>", changeExtentJsStr);

		mailInfo.setContent(mailContent);
		// 发送html格式邮件
		SendMail.sendHtmlMail(mailInfo);
	}

	public static String[] readMail(String mailType) throws IOException {
		Properties props = new Properties();
		props.load(new FileInputStream("./target/classes/" + mailType + ".properties"));
		String[] mailto = new String[props.size()];

		int i = 0;
		for (Enumeration enu = props.elements(); enu.hasMoreElements(); i++) {
		    String key = (String)enu.nextElement();
		    mailto[i]=key;
		}
		return mailto;
	}
	
}
