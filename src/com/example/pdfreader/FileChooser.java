package com.example.pdfreader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract.Directory;
import android.speech.tts.TextToSpeech;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileChooser extends ListActivity  {
	private File currentDir;
	private FileArrayAdapter adapter;
	public static String Choosen,Path;
	
	TextToSpeech tts;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentDir = new File("/sdcard/");
		fill(currentDir);
		ListView ls;
		ls=this.getListView();
		
		//long click oin list item
		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				LongClick(parent, v, position, id);
				return true;
			}
		});
		
		//long click oin list item **********
		
		//v.setOnClickListener((OnClickListener) this); 
		
		
        //For voice output 
        tts=new TextToSpeech(FileChooser.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        ConvertTextToSpeech("");
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
        //voice***********************************
	}

	private void fill(File f) {
		File[] dirs = f.listFiles();
		this.setTitle("Current Dir: " + f.getName());
		List<Option> dir = new ArrayList<Option>();
		List<Option> fls = new ArrayList<Option>();
		
		
	    try {
			for (File ff : dirs) {
				if (ff.isDirectory()) {
					
					dir.add(new Option(ff.getName(), "Folder", ff.getAbsolutePath()));
					
				} else
				{
					fls.add(new Option(ff.getName(), "File Size: "+ ff.length(), ff.getAbsolutePath()));
				}

			}
		} catch (Exception e)
		{
		} finally {
		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new Option("..", "Parent Directory", f.getParent()));
		adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view,dir);
		this.setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		Option o = adapter.getItem(position);

		if (o.getData().equalsIgnoreCase("folder")
				|| o.getData().equalsIgnoreCase("parent directory")) {
			currentDir = new File(o.getPath());
			fill(currentDir);
		}
		else
		{
			onFileClick(o);
		}
		
	}
	
	public void LongClick(AdapterView<?> parent, View v, int position, long id) {
		
		// TODO Auto-generated method stub
		Option o = adapter.getItem(position);
		
		
		if (o.getData().equalsIgnoreCase("folder")
				|| o.getData().equalsIgnoreCase("parent directory")) {
			ConvertTextToSpeech(o.getName());
		}
		else
		{
			ConvertTextToSpeech(o.getName());
		}
	}
	
	

	private void onFileClick(Option o)
	{
		Choosen=o.getName();
		Path=currentDir.getAbsolutePath();
		Intent prevScreen = new Intent(getApplicationContext(), MainActivity.class);     //for normal
		Intent forward=new Intent(getApplicationContext(), OpenPdf.class);               //for blind
		Toast.makeText(this, "File Clicked: " + Choosen +"  Path:"+Path, Toast.LENGTH_SHORT).show();
		if(ListViewShow.blind_flag==false)
		{
			startActivity(prevScreen);
		}
		else if(ListViewShow.blind_flag==true)
		{
			MainActivity.readfilename = Choosen;
	        startActivity(forward);
	        ListViewShow.blind_flag=false;
		}
	}
	
	
	// for voice 
    protected void onPause() {
        // TODO Auto-generated method stub

        if(tts != null){

            tts.stop();
            tts.shutdown();
        }
        super.onPause();
    }


    private void ConvertTextToSpeech(String spk) {
    
            tts.speak(spk, TextToSpeech.QUEUE_FLUSH, null);
    
    }
    //for voice ***********

}
