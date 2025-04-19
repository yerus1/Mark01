package com.connect.lib;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.security.MessageDigest;

public class lib {
    public String getRandomhash(int c){
        String st="qwetyuioplkjhgfszxcvbnm1234567890QWERTYUIOPMNBVCXZASDFGHJKL";
        StringBuilder stb=new StringBuilder();
        for(int i=0;i<c;i++){
            int index=(int)(st.length()*Math.random());
            stb.append(st.charAt(index));
        }
        return stb.toString();
    }
    public void sendMail(String recp,int otp) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        final String username = "surendarraja420@gmail.com";
        final String password = "kmcizoikmilzdcwy";
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        Message message = prepareMessage(session,username,recp,otp);

        try {
            Transport.send(message);
        } catch (Exception var6) {
        }

    }

    public static Message prepareMessage(Session session, String username,String recp,int otp) {
        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(username));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recp));
            message.setSubject("Verify your identity");
            message.setText(" Help us protect your account\n\nBefore you sign in, we need to verify your identity. Enter the following code on the sign-in page.\n "+otp);
        } catch (Exception var4) {
        }

        return message;
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        String salt="qwertyuiopoigfdsacvbnm,237890!@#$%^&*QWERTYUIKJHGFDSCVHJITRESXCVBL:";
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest((password+salt).getBytes());
        StringBuilder builder=new StringBuilder(new BigInteger(1, messageDigest).toString(16));
        return builder.reverse().toString();
    }
    public static void main(String[] args) throws NoSuchAlgorithmException{
        lib lb=new lib();
        System.out.println( Integer.parseInt(lb.getRandomhash(6)));
    }
}
