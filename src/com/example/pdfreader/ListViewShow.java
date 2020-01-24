package com.example.pdfreader;

import java.util.Locale;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.app.Activity;
import android.content.Intent;

public class ListViewShow extends Activity {
	ListView list;
	String[] web = { "Browse", "Exit" };
	Integer[] imageId = { R.drawable.browse,R.drawable.exit };
	TextToSpeech tts;
	MainActivity mainact;
	public static boolean  blind_flag = false; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blindhelper);

		CustomList adapter = new CustomList(ListViewShow.this, web, imageId);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		
		//single click on list item
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(web[position]=="Browse")
				{
					Intent nextScreen = new Intent(getApplicationContext(), FileChooser.class);
			    	startActivity(nextScreen);
			    	blind_flag=true;
				}
	
				else if(web[position]=="Exit")
				{
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}

		
		});
		
		//single click on list item**************
		
	
		
		// long click on list item

		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				LongClick(parent, v, position, id);
				return true;
			}
		});

		// long click oin list item **********

		// For voice output
		tts = new TextToSpeech(ListViewShow.this,
				new TextToSpeech.OnInitListener() {

					@Override
					public void onInit(int status) {
						// TODO Auto-generated method stub
						if (status == TextToSpeech.SUCCESS) {
							int result = tts.setLanguage(Locale.US);
							if (result == TextToSpeech.LANG_MISSING_DATA
									|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
								Log.e("error", "This Language is not supported");
							} else {
								ConvertTextToSpeech("");
							}
						} else
							Log.e("error", "Initilization Failed!");
					}
				});
		// voice***********************************
	}
	

	public void LongClick(AdapterView<?> parent, View v, int position, long id) {

		ConvertTextToSpeech(web[position]);
	}

	// for voice
	protected void onPause() {
		// TODO Auto-generated method stub

		if (tts != null) {

			tts.stop();
			tts.shutdown();
		}
		super.onPause();
	}

	private void ConvertTextToSpeech(String spk) {

		tts.speak(spk, TextToSpeech.QUEUE_FLUSH, null);

	}
	// for voice ***********
}