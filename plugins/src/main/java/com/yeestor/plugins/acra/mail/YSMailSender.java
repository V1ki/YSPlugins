package com.yeestor.plugins.acra.mail;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yeestor.plugins.acra.mail.config.YSMailSenderConfig;

import org.acra.config.ConfigUtils;
import org.acra.config.CoreConfiguration;
import org.acra.config.MailSenderConfiguration;
import org.acra.data.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.acra.util.IOUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class YSMailSender implements ReportSender {

    private final CoreConfiguration config;
    private final YSMailSenderConfig mailConfig;

    public YSMailSender(CoreConfiguration config) {
        this.config = config;
        this.mailConfig = ConfigUtils.getPluginConfiguration(config,YSMailSenderConfig.class);
    }

    @Override
    public void send(Context context, CrashReportData errorContent) throws ReportSenderException {
        // Iterate over the CrashReportData instance and do whatever
        // you need with each pair of ReportField key / String value
        final PackageManager pm = context.getPackageManager();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");

        String subject = "Report " ;
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(context.getPackageName() , 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            String packageName = packageInfo.packageName ;
            String appName = context.getResources().getString(labelRes);
            String versionName = packageInfo.versionName ;
            int versionCode = packageInfo.versionCode ;

            subject = "Report "+appName+"("+packageName+") v:"+versionName+"("+versionCode+")" + " "+dateFormat.format(new Date());

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



        Properties props = new Properties();
        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", mailConfig.smtpHost());
        props.put("mail.smtp.socketFactory.port", mailConfig.socketFactoryPort());
        props.put("mail.smtp.socketFactory.class", mailConfig.socketFactoryClass());
        props.put("mail.smtp.auth", mailConfig.auth());
        props.put("mail.smtp.port", mailConfig.smtpPort());

        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(mailConfig.username(), mailConfig.password());
                    }
                });
        final String reportText;
        File cache ;
        try {
            reportText = config.reportFormat().toFormattedString(errorContent, config.reportContent(), "\n", "\n\t", false);

            cache = new File(context.getCacheDir(), "YS-Report.log");
            IOUtils.writeStringToFile(cache,reportText);



        } catch (Exception e) {
            throw new ReportSenderException("Failed to convert Report to text", e);
        }
        String message = "test" ;
        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);
            //Setting sender address
            mm.setFrom(new InternetAddress(mailConfig.mailFrom()));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(mailConfig.mailTo()));
            //Adding subject
            mm.setSubject(subject);

            if(cache != null) {
                // Create a multipar message

                DataSource dataSource = new FileDataSource(cache);
                DataHandler dataHandler = new DataHandler(dataSource);

                Multipart multipart = new MimeMultipart();

                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setText("报错信息：");
                multipart.addBodyPart(messageBodyPart);

                // attachment
                messageBodyPart = new MimeBodyPart();

                messageBodyPart.setDataHandler(dataHandler);
                messageBodyPart.setFileName("YS-Report.log");

                multipart.addBodyPart(messageBodyPart);

                // Send the complete message parts
                mm.setContent(multipart);
            }
            else {

                mm.setText(message);
            }


            //Sending email
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
//

    }
}
