package Shenkar.IRProject;


import javafx.scene.input.DataFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;


public class Document {


    public Vector<String> data;
    private String name;
    private int id;
    private String author ;
    private String desc;
    private String headline;
    private String published;

    public Document(File f, int id) throws IOException {
        data=new Vector<>();
        this.id=id;
        name=f.getName();
        BufferedReader reader = new BufferedReader(new FileReader(f));
        author = reader.readLine();
        published=reader.readLine();
        desc=reader.readLine();
        headline=reader.readLine();
    }

    public String toString() {
        return "<HTML>"
                +"<body style='width: 50%;border-bottom:2px solid black;'>"
                + "<B>Doc name :</B> " +name + " <BR> <B>Author: </B>"+ author +"<BR> <B> Description:</B> " + desc + "<BR>"+ published +"<BR> <B> Published:</B> ";
    }

    public String printContext(int pos) {
        String s="<b>"+name+ ":</b><br>";
        if(pos==0)
        {
            s+="<b>"+data.get(pos)+"</b>"+" ";
            for(int i=1;i<pos+30;i++)
            {
                s+=data.get(i)+" ";
            }
        }
        else if(pos>0 && pos<data.size())
        {
            for(int i=pos-30;i<pos;i++)
            {
                if(i>0)
                    s+=data.get(i)+" ";
            }
            s+="<b>"+data.get(pos)+"</b>"+" ";
            for (int i=pos+1;i<pos+30;i++)
            {
                if(i<data.size())
                    s+=data.get(i)+" ";
            }
        }
        else if(pos==data.size())
        {
            for(int i=pos-30;i<data.size()-1;i++)
            {
                s+=data.get(i)+" ";
            }
            s+="<b>"+data.get(pos)+"</b>"+" ";
        }
        return s;
    }
}
