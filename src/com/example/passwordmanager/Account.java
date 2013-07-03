package com.example.passwordmanager;

import java.security.NoSuchAlgorithmException;
//import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

//import com.sun.crypto.provider.SunJCE;
public class Account
{
	private String belongWhere;
	private String accountName;
	private byte[] passwordEncrypted;
	private SecretKey desKey;
	private String password;
	public int similarity;
	Account(String bw,String an,byte[] pe,SecretKey dk,String pw)
	{
		belongWhere=bw;
		accountName=an;
		passwordEncrypted=pe;
		desKey=dk;
		password=pw;
	}
	// pass belongWhere,Account,passwordEncrypted,desKey to construct
	Account(String bw,String an,byte[] pe,SecretKey dk)
	{
		belongWhere=bw;
		accountName=an;
		passwordEncrypted=pe;
		desKey=dk;

		//Security.addProvider(new SunJCE());
		String Algorithm="DES"; // 定义 加密算法 , 可用 DES,DESede,Blowfish
		try{
		Cipher cipher=Cipher.getInstance(Algorithm);
		cipher.init(Cipher.DECRYPT_MODE,desKey);
		password=new String(cipher.doFinal(passwordEncrypted));
		}
		catch(NoSuchAlgorithmException e){ e.printStackTrace();}
        catch(Exception e){ e.printStackTrace(); }

	}
	//pass belongWhere,Account,Password to construct.
	Account(String bw,String an,String pw)
	{
		belongWhere=bw;
		accountName=an;
		password=pw;

		//Security.addProvider(new SunJCE());
		String Algorithm="DES"; // 定义 加密算法 , 可用 DES,DESede,Blowfish

		try{
		KeyGenerator keygen = KeyGenerator.getInstance(Algorithm);
   		desKey = keygen.generateKey();

		Cipher cipher=Cipher.getInstance(Algorithm);
		cipher.init(Cipher.ENCRYPT_MODE,desKey);
		passwordEncrypted=cipher.doFinal(password.getBytes());
		}
		catch(NoSuchAlgorithmException e){ e.printStackTrace();}
        catch(Exception e){ e.printStackTrace(); }
	}
	public void match(String pattern)
	{
		if (belongWhere.contains((CharSequence)pattern)) similarity=1;
			else similarity=0;
	}
	public String getBelongWhere()
	{
		return belongWhere;
	}
	public void setBelongWhere(String bw)
	{
		belongWhere=bw;
	}
	public String getAccountName()
	{
		return accountName;
	}
	public void setAccountName(String an)
	{
		accountName=an;
	}
	public byte[] getPasswordEncrypted()
	{
		return passwordEncrypted;
	}
	public void setPasswordEncrypted(byte[] pe)
	{
		passwordEncrypted=pe;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String pw)
	{
		password=pw;
	}
	public SecretKey getDesKey()
    {
        return desKey;
    }
    public void setDesKey(SecretKey dk)
    {
        desKey=dk;
    }
    public static String byte2hex(byte[] b) // 二行制转字符串
    {
        String hs="";
        String stmp="";
        for (int n=0;n<b.length;n++)
        {
            stmp=(java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;
                else hs=hs+stmp;
            if (n<b.length-1)  hs=hs+":";
        }
        return hs.toUpperCase();
    }
	public static void main(String[] arguments)
    {
        Account a1=new Account("dangdang","gzc","beyond2123");
        String s1=Account.byte2hex(a1.getDesKey().getEncoded());
        System.out.println(a1.getPasswordEncrypted());
        System.out.println(s1);
        //System.out.println(a1.getDesKey().getEncoded().length);


    }
}
