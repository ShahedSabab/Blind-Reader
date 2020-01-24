package com.example.pdfreader;

import java.text.BreakIterator;
import java.util.Locale;


import android.speech.tts.TextToSpeech;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.AvoidXfermode;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;
import android.speech.tts.*;
import android.os.Vibrator;

public class OpenPdf extends Activity{
		TextView filecon;
		TextToSpeech tts;
		String speakable;
		public Vibrator vibrator;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
            setContentView(R.layout.pdfview);
           
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            filecon = (TextView) findViewById(R.id.filecon);
            Log.d("tapped on:",String.valueOf(LinkTouchMovementMethod.scrollingFlag));
           
            
            //For voice output 
            tts=new TextToSpeech(OpenPdf.this, new TextToSpeech.OnInitListener() {

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
            
            //pdf content 
            writeToScreen();
            //pdf content ****************
        }
        public void writeToScreen()
        {
        	 String fetch=MainActivity.readfilename;
             FileOperations fop = new FileOperations();
             String text = fop.read(fetch);
             filecon.setText(text);       
          	 filecon.setMovementMethod(new ScrollingMovementMethod());
          	 init(text);
        }
        private void init(String text) {
            
        	String definition = text;
  
            TextView definitionView = (TextView) findViewById(R.id.filecon);
            definitionView.setMovementMethod(new LinkTouchMovementMethod());
            
            
            definitionView.setText(definition, BufferType.SPANNABLE);
            
            
            
            SpannableString spans = (SpannableString) definitionView.getText();
            BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
            iterator.setText(definition);
            int start = iterator.first();
          
            for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator
                    .next()) {

                String possibleWord = definition.substring(start, end);
             
                if (Character.isLetterOrDigit(possibleWord.charAt(0))) {                	   
                    TouchableSpan clickSpan = getTouchableSpan(possibleWord);
                	spans.setSpan(clickSpan, start, end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            
        }
        public TouchableSpan getTouchableSpan(final String word)
        {
        	
        	
        	TouchableSpan touchableSpan = new TouchableSpan() {

        	    private Object context;

				public boolean onTouch(View widget, MotionEvent m) {

        	    	 if(LinkTouchMovementMethod.lineFlag==true)
        	    	 {
        	    		 ConvertTextToSpeech("Line Number "+String.valueOf(LinkTouchMovementMethod.current_line));
        	    	 }
        	    	 else if(LinkTouchMovementMethod.lineBreak==true)
        	    	 {
        	    		 vibrator.vibrate(200);
        	    		 Log.d("print:",String.valueOf(LinkTouchMovementMethod.last_line));
        	    		 LinkTouchMovementMethod.lineBreak=false;
        	    	 }
        	    	 else if(speakable!=word && LinkTouchMovementMethod.lineFlag==false && !LinkTouchMovementMethod.lineBreak)
        	    	 {         	    		 
        	    		 speakable=word;
        	    		 Log.d("check:",  String.valueOf(speakable.length())); 
        	    		 ConvertTextToSpeech(speakable);
        	    	 }
        	    	 
    				return true;
           
        	    }

        	    public void updateDrawState(TextPaint ds) {
        	        ds.setUnderlineText(false);
        	        ds.setAntiAlias(true);
        	    }

        	};
    		return touchableSpan;
        	
        }
        protected void onPause() {
            // TODO Auto-generated method stub

            if(tts != null){

                tts.stop();
                tts.shutdown();
            }
            super.onPause();
        }


        public void ConvertTextToSpeech(String spk) {
        
                tts.speak(spk, TextToSpeech.QUEUE_FLUSH, null);
                //LinkTouchMovementMethod.lineBreak=false;
        
        }
        public void onBackPressed()
        {
            super.onBackPressed(); 
            startActivity(new Intent(OpenPdf.this, MainActivity.class));
            finish();

        }
        
        //menu
        
        public boolean onCreateOptionsMenu(Menu menu) {
            	ConvertTextToSpeech("Expected line "+LinkTouchMovementMethod.last_line);
				return false;
         }
}
