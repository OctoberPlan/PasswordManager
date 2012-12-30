package com.gzc.passwordnote;

import java.io.Serializable;

import javax.crypto.SecretKey;

public class AccountData implements Serializable{
    String belongWhere;
    String accountName;
    byte[] passwordEncrypted;
    SecretKey desKey;
    AccountData(String bw,String an,byte[] pe,SecretKey dk)
    {
        belongWhere=bw;
        accountName=an;
        passwordEncrypted=pe;
        desKey=dk;
    }
    AccountData(Account src){
    	this(src.getBelongWhere(),src.getAccountName(),src.getPasswordEncrypted(),src.getDesKey());
    }
}
