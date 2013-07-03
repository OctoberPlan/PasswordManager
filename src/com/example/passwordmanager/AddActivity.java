package com.example.passwordmanager;

import com.example.passwordmanager.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
	
public class AddActivity extends Activity{
	Button okButton=null;
	Button cancelButton=null;
	EditText whereText=null;
	EditText accountText=null;
	EditText passwordText=null;
	EditText confirmText=null;
	@Override
	public void onCreate(Bundle icicle){
		super.onCreate(icicle);
		setContentView(R.layout.add);
		okButton=(Button)findViewById(R.id.button1);
		cancelButton=(Button)findViewById(R.id.button2);
		whereText=(EditText)findViewById(R.id.editText1);
		accountText=(EditText)findViewById(R.id.editText2);
		passwordText=(EditText)findViewById(R.id.editText3);
		confirmText=(EditText)findViewById(R.id.editText4);
	
	}
	
	public void onClick(View v){
		if (v==okButton){
			String where=whereText.getText().toString();
            String account=accountText.getText().toString();
            String password=passwordText.getText().toString();
            String confirm=confirmText.getText().toString();
			if (where.length()==0 || account.length()==0 || 
					password.length()==0||confirm.length()==0)
                Toast.makeText(this, "输入不完整", Toast.LENGTH_SHORT).show();
            else if (!password.equals(confirm)) 
            	Toast.makeText(this, "两次输入密码不同",Toast.LENGTH_SHORT).show();
            else{
            	Intent i=new Intent(this,MainActivity.class);
            	i.putExtra("where", where);
            	i.putExtra("account", account);
            	i.putExtra("password", password);
            	setResult(RESULT_OK,i);
            	finish();
            }
            
		}
		
		if(v==cancelButton){
			Intent i=new Intent(this,MainActivity.class);
			setResult(RESULT_CANCELED,i);
			finish();
		}
	}
	
}
