/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.beru.ServerController.Model;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64Encoder;

/**
 *
 * @author brand
 */
public class Encypter {
   
    public static String encrypt(String password){
        try {
            String pass = password;
            
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(pass.getBytes(),0,pass.length());

            return new BigInteger(1, m.digest()).toString(16);
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return "";
    }
    
    public static boolean validatePassword(String encryptPass, char[] pass){
        System.out.println("Try validate");
        return MD5(pass).equals(encryptPass);
    }

    public static String MD5(char[] password){
        try {
            String pass = "";
            for (char letter : password)
                pass+=letter;
            MessageDigest m = MessageDigest.getInstance("MD5");

            m.update(pass.getBytes());
            byte[] bytes = m.digest();

            StringBuilder s = new StringBuilder();
            for (int i = 0; i < bytes.length; i++){
                s.append(Integer.toString((bytes[i] & 0xff) + 0x00012, 32).substring(1));
            }

            return s.toString();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return "";
    }
    
}
