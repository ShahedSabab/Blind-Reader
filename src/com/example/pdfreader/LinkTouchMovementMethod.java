package com.example.pdfreader;

import android.content.Context;
import android.os.Vibrator;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.os.Vibrator;
public class LinkTouchMovementMethod extends LinkMovementMethod
{

	public static int current_line=-1;
	int line_number=-1;
	public static int last_line=0;
	public static boolean scrollingFlag=false;
	public static boolean lineFlag=false;
	public static boolean lineBreak=false;
	long time=0;
	boolean first_touch=false;
	int last_X=0;
	int last_Y=0;
	boolean subsequentLine=true;
	boolean up,down,right,left;
	int line_start; //line character count
	int line_end;   //line character count
	int wordEnd;  //last word length
	int offset;
	public static boolean sound_on=false;
	
	 
	 
	@Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();
        
        if(first_touch && (System.currentTimeMillis() - time) <= 220 && action == MotionEvent.ACTION_DOWN )
        {
        	Log.d("tapped on:","second tap : "+String.valueOf(current_line));
        	first_touch=false;
        	lineFlag=true;           // tell line number
        	//scrollingFlag=true;

        }
        else if ((action == MotionEvent.ACTION_UP ||action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE)) {
        	
        	 Log.d("print:","(link)"+String.valueOf(last_line));
        	if(action==MotionEvent.ACTION_DOWN)
        	{
        		//scrollingFlag=false;
        		first_touch=true;
        		time=System.currentTimeMillis();
        		lineFlag=false;
        		
        	}
        	
            int x = (int) event.getX();
            int y = (int) event.getY();
            
            //direction 
            int dx=last_X-x;
            int dy=last_Y-y;
            
           /* if(Math.abs(dx)>Math.abs(dy))
            {
            	if(dx>0)
            	{
            		left=true;
            		right=up=down=false;
            		//Log.d("direction","left");
            	}
            	else 
            	{
            		right=true;
            		left=up=down=false;
            		//Log.d("direction","right");
            	}
            }
            else
            {
            	if(dy>0)
            	{
            		up=true;
            		down=right=left=false;
            		//Log.d("direction","up");
            	}
            	else
            	{
            		down=true;
            		up=right=left=false;
            		//Log.d("direction","down");
            	}
            }
            */
            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            
            // second 
            if(action==MotionEvent.ACTION_DOWN)
            {
            	//sound_on=false; 
            	 line_start =layout.getLineStart(line);
                 line_end = layout.getLineEnd(line);
               	 current_line=line;
               	 if(current_line!=last_line)
               	 {
               		lineBreak=true;
               	    
               	 }
               	 else 
               	 {
               		lineBreak=false;
               	 }
            }
            else if(action==MotionEvent.ACTION_UP)
            {
            	//sound_on=false;
            	if(wordEnd>=line_end-5 && last_line==current_line)
	            {
	            	last_line+=1;
	            }
            }
            
            Log.d("tapped on:","flag sound:"+sound_on);
            Log.d("tapped on:","line break:"+lineBreak);
            Log.d("tapped on:","current line:"+current_line);
            Log.d("tapped on:","last line:"+last_line);
            
            line_number=line;

            TouchableSpan[] link = buffer.getSpans(off, off, TouchableSpan.class);

     
            if (link.length != 0 && (current_line==line_number) )
            {
                //scrollingFlag=false;
            	// wordEnd=buffer.getSpanEnd(link[0]);
            	if (action == MotionEvent.ACTION_UP) {
                    link[0].onTouch(widget,event); //////// CHANGED HERE
                    
                } 
                else if (action == MotionEvent.ACTION_MOVE) {
	                link[0].onTouch(widget,event); //////// ADDED THIS
	                Selection.setSelection(buffer,
	                                       buffer.getSpanStart(link[0]),
	                                       buffer.getSpanEnd(link[0]));
	                wordEnd=buffer.getSpanEnd(link[0]);
	               
	            }
                else if (action == MotionEvent.ACTION_DOWN) {
                    link[0].onTouch(widget,event); //////// ADDED THIS
                    Selection.setSelection(buffer,
                                           buffer.getSpanStart(link[0]),
                                           buffer.getSpanEnd(link[0]));
                  //offset =  buffer.getSpanStart(link[0]);
                    wordEnd=buffer.getSpanEnd(link[0]);
                }
            
            	//last_line=current_line;
                return true;
            }
            else 
            {
                Selection.removeSelection(buffer);
            }
           
        }
        
        return super.onTouchEvent(widget, buffer, event);
    }
	


}