package com.example.pdfreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class FileOperations {
    public FileOperations() {
    }

    public Boolean write(String fname, String fcontent) {
    	
        try {
            String fpath = "/sdcard/Blind Reader/" + fname + ".pdf";
            File file = new File(fpath);
            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
         
            Document document = new Document();
            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
            document.open();
            document.add(new Paragraph(MainActivity.filecontent));
            document.close();

            Log.d("Suceess", "Sucess");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public String read(String fname) {
        BufferedReader br = null;
        String response = null;
        try {
            StringBuffer output = new StringBuffer();
            String fpath = FileChooser.Path+"/"+ fname;

            PdfReader reader = new PdfReader(new FileInputStream(fpath));
            PdfReaderContentParser parser = new PdfReaderContentParser(reader);

            StringWriter strW = new StringWriter();

            TextExtractionStrategy strategy;
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = parser.processContent(i,
                        new SimpleTextExtractionStrategy());

                strW.write(strategy.getResultantText());

            }

            response = strW.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }
}