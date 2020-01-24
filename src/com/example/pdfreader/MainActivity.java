package com.example.pdfreader;


import java.io.File;
import java.io.FileFilter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    protected static final int REQUEST_SAVE = 0;
	EditText fname, fcontent, fnameread;
    Button write, read, browse;
    TextView filecon;
    public static String readfilename,filecontent;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fname = (EditText) findViewById(R.id.fname);
        fcontent = (EditText) findViewById(R.id.ftext);
        fnameread = (EditText) findViewById(R.id.fnameread);
        write = (Button) findViewById(R.id.btnwrite);
        read = (Button) findViewById(R.id.btnread);
        browse = (Button) findViewById(R.id.browse);
        
        fnameread.setText(FileChooser.Choosen);
        
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                writemethod();
            }
        });
        
        read.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
            public void onClick(View arg0) {
        		readmethod();
            }
        });
        
        browse.setOnClickListener(new View.OnClickListener() {
        	 
       	@Override
           public void onClick(View arg0) {
       			browsemethod();
           }
       });
       
     }
  
    public void readmethod()
    {
    	Intent nextScreen = new Intent(getApplicationContext(), OpenPdf.class);
    	readfilename = fnameread.getText().toString();
		 FileOperations fop = new FileOperations();
        String text = fop.read(readfilename);
        if (text != null) {
       	 	startActivity(nextScreen);
        }
        else {
            Toast.makeText(getApplicationContext(), "File not Found",Toast.LENGTH_SHORT).show();
        }
        fnameread.setText("");
    }
    
    public void writemethod()
    {
    	if(fcontent.getText().toString().matches("") || fname.getText().toString().matches(""))
    	{
    		 Toast.makeText(getApplicationContext(), "Please enter file content and file name",
                     Toast.LENGTH_LONG).show();
    	}
    	else
    	{
            String filename = fname.getText().toString();
            filecontent = fcontent.getText().toString();
            FileOperations fop = new FileOperations();
            fop.write(filename, filecontent);
            if (fop.write(filename, filecontent)) {
                Toast.makeText(getApplicationContext(),
                        filename + ".pdf created", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(), "I/O error",
                        Toast.LENGTH_SHORT).show();
            }
            fname.setText("");
            fcontent.setText("");
    	}
    }
    
    public void browsemethod()
    {
    	Intent nextScreen = new Intent(getApplicationContext(), FileChooser.class);
    	startActivity(nextScreen);
    }
    
    
    
    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_settings, menu);
        return super.onCreateOptionsMenu(menu);

    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
         
            case R.id.action_blindread:
            	Blindread();
                return true;
            case R.id.action_exit:
            	Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				 return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //menu****************
    
    public void Blindread()
    {
    	 Intent nextScreen = new Intent(getApplicationContext(), ListViewShow.class);
    	 startActivity(nextScreen);
    }
   

}
