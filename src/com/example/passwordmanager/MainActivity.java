package com.example.passwordmanager;


import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.example.passwordmanager.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity<item> extends Activity implements OnItemSelectedListener  {
	private final static String passwordRepository="password.dat";
	private final static int ADD_REQUEST=1;
	private final static int DELETE_REQUEST=2;
	
	private final static int DELETE_DIALOG=0;
	private final static int EXCEPTION_DIALOG=1;
	Vector<AccountData> originData;
    Vector<Account> data;
    Spinner whereSpinner=null;
    Spinner accountSpinner=null;
    EditText passwordText=null;
    private ArrayAdapter<String> whereAdapter=null;
    private ArrayAdapter<String> accountAdapter=null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        whereSpinner=(Spinner)findViewById(R.id.spinner1);
        accountSpinner=(Spinner)findViewById(R.id.spinner2);
        passwordText=(EditText)findViewById(R.id.editText1);
        //initialize accounts data
        
        originData=new Vector<AccountData>();
        try{
            FileInputStream disk=this.openFileInput(passwordRepository);
            ObjectInputStream obj=new ObjectInputStream(disk);
            try{
                while (true){ originData.add((AccountData)obj.readObject()); }
            }
            catch(EOFException ex){
                //System.out.println("Here!");
                obj.close();
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        data=new Vector<Account>();
        for (AccountData t :originData){
            data.add(new Account(t.belongWhere,t.accountName,t.passwordEncrypted,t.desKey));
        }
        
        
        List<String> wheres=new ArrayList<String>();
        for(Account a :data)
        	wheres.add(a.getBelongWhere());
        whereAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wheres);
        whereSpinner.setAdapter(whereAdapter);
        whereSpinner.setOnItemSelectedListener(this);
        whereSpinner.setSelection(0);
        /* We just set whereSpinner's first item,and then os will call whereSpinner.setOnItemSelectedListener()
         * which will contains codes processing accountSpinner.Thus we deprecated these codes below.
         * 
        Log.v("onCreate","set account spinner first");
        List<String> accounts=new ArrayList<String>();
        for(Account a:data){
        	if (a.getBelongWhere().equals(wheres.get(0))) accounts.add(a.getAccountName());
        }
        accountSpinner.setAdapter(new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item,accounts));
        accountSpinner.setOnItemSelectedListener(this);
        Log.v("onCreate","set password first time");
        
        
        passwordText.setText(data.get(0).getPassword());
        */
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
	public void onItemSelected(AdapterView<?> adapterView, View view, int position,
			long ID) {
		// TODO Auto-generated method stub
		if(adapterView==whereSpinner){
			String selected=(String)adapterView.getItemAtPosition(position);
			updateAccountSpinner(selected);
			
		}
		if(adapterView==accountSpinner){
			String whereSelected=(String)whereSpinner.getSelectedItem();
			String accountSelected=(String)accountSpinner.getItemAtPosition(position);

			for (Account a:data)
			{
				if (a.getBelongWhere().equals(whereSelected)&&a.getAccountName().equals(accountSelected)){
					passwordText.setText(a.getPassword());
					break;
				}
			}
		}

	}
	
	private void updateAccountSpinner(String where){
		List<String> accounts=new ArrayList<String>();
        for(Account a:data){
        	if (a.getBelongWhere().equals(where)) accounts.add(a.getAccountName());
        }
        accountAdapter=new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item,accounts);
        accountSpinner.setAdapter(accountAdapter);
        accountSpinner.setOnItemSelectedListener(this);
        accountSpinner.setSelection(0);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		return(applyMenuChoice(item)||
				super.onOptionsItemSelected(item));
	}
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		if (arg0==accountSpinner) passwordText.setText("");
		if (arg0==whereSpinner) Toast.makeText(this,"还没有添加账号" , Toast.LENGTH_SHORT).show();
	}
	
	private boolean applyMenuChoice(MenuItem item){
		Intent i=null;
		switch(item.getItemId()) {
		case R.id.addOption:
			i=new Intent(this,AddActivity.class);
			startActivityForResult(i,ADD_REQUEST);
			return(true);
			 
		case R.id.deleteOption:
			showDialog(DELETE_DIALOG);
			return(true);
			
		case R.id.aboutOption:
			i=new Intent(this,AboutActivity.class);
			startActivity(i);
			return(true);
		case R.id.usageOption:
			i=new Intent(this,UsageActivity.class);
			startActivity(i);
			return(true);
		}
		return false;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent i){
		if (requestCode==ADD_REQUEST){
			if (resultCode==RESULT_OK){
				Account newAccount=new Account(i.getStringExtra("where"),
											   i.getStringExtra("account"),
											   i.getStringExtra("password"));
						
				data.add(newAccount);
				originData.add(new AccountData(newAccount));
				saveOriginData();
				
				whereAdapter.add(newAccount.getBelongWhere());
				whereSpinner.setSelection(whereAdapter.getPosition(newAccount.getBelongWhere()));
				Toast.makeText(this, "账号添加成功", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void saveOriginData(){
		try{
	        FileOutputStream file=this.openFileOutput(passwordRepository, MODE_PRIVATE);
	        ObjectOutputStream out=new ObjectOutputStream(file);
	        for(AccountData a:originData) out.writeObject(a);
	        out.close();
	        }
	    catch(IOException e){
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	        }
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id){
		case DELETE_DIALOG:
			StringBuffer buf=new StringBuffer();
			String where=(String)whereSpinner.getSelectedItem();
			String account=(String)accountSpinner.getSelectedItem();
			if (where==null||account==null)
				return new AlertDialog.Builder(MainActivity.this)
				.setIcon(R.drawable.alert_dialog_icon)
				.setMessage("还未选定账号")
				.create();
			else{
				buf.append("删除").append(where).append("目录下的");
				buf.append("账号:").append(account).append("?");
				return new AlertDialog.Builder(MainActivity.this)
					.setIcon(R.drawable.alert_dialog_icon)
					.setMessage(buf)
					.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
					
						public void onClick(DialogInterface dialog, int which) {
							deleteAccount((String)whereSpinner.getSelectedItem(),(String)accountSpinner.getSelectedItem());
						
						}
					})
					.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
					
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						
						}
					})
					.create();
				}
		case EXCEPTION_DIALOG:
			return new AlertDialog.Builder(MainActivity.this)
			.setIcon(R.drawable.alert_dialog_icon)
			.setMessage("Exception occured.")
			.create();
		}
		return super.onCreateDialog(id);
	}

	protected void deleteAccount(String where, String account) {
		// TODO Auto-generated method stub
		for(int i=0;i<data.size();i++)
			if (data.elementAt(i).getBelongWhere().equals(where)&&data.elementAt(i).getAccountName().equals(account)){
				data.remove(i);
				originData.remove(i);
				break;
			}
			whereAdapter.remove(where);
			accountAdapter.remove(account);
			saveOriginData();
		
			
	

		
	}
	
	
}
