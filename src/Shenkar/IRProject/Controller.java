package Shenkar.IRProject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

/**
 * Created by Almog on 03-Nov-16.
 */
public class Controller {

    private IndexTable index;
    private SEGui gui;

    public Controller(String indexFile, SEGui seGui) throws IOException, ClassNotFoundException {
        gui= seGui;
        gui.addAddFileListener(new AddFileListener());
        gui.addRemoveFileListener(new RemoveFileListener());
        gui.addRunListener(new RunListener());
        File f= new File(indexFile);
        if(f.exists())
        {
            loadIndex(indexFile);
        }
        else
        {
            createIndex();
        }
    }

    private void loadIndex(String indexFile) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(indexFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        index = (IndexTable) ois.readObject();
        ois.close();
        return;
    }

    private void createIndex() {
        index=new IndexTable();
    }

    private List<String> ParssQuery(String query)
    {
        String[] splitted = query.split(" ");
        List<String> parsed = new ArrayList<String>();
        int index = 1;
        for (String token : splitted) {
            if(token.equals("AND") || token.equals("OR") || token.equals("NOT"))
            {
                parsed.add("1"); //  1 - symbol Oper
            }
            else if(token.equals("Quotation"))
            {
                parsed.add("2");  // 2 - symbol quot
            }
            else{
                parsed.add("3");  // 3 - word
            }
        }
        System.out.println(parsed);
        return parsed;
    }

    private class AddFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            JFileChooser chooser=new JFileChooser("./");
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if(chooser.showOpenDialog(null)== JFileChooser.APPROVE_OPTION);
            String name =chooser.getSelectedFile().getName();
            try {
                if(chooser.getSelectedFile().isFile())
                    index.indexFile(chooser.getSelectedFile());
                else
                    index.indexLib(chooser.getSelectedFile());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class RemoveFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // delete -> hide in every DOC by setting the field boolean of isHide to
            // TRUE , by transformation file name to ID file in the
            // IndexTable and loop on every Term and ID scanning .

        }
    }

    private class RunListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String query = gui.getQuery();
            List<String> parsed = new ArrayList<String>();
            parsed = ParssQuery(query);

            int len = parsed.size();
            if(len < 2)gui.setAnswer(index.searchSingle(query));
            else{
                    //gui.setAnswer(index.analyseQuery(parsed,query));
                    // need to change the function search so it will know what to the with oper
            }

            // if the query len is less then two -> search for only
            // the word in case they are words of course.
            // need to understand the query

            /*
            if(!word.isEmpty())
            {
                gui.setAnswer(index.search(word));
            }
            */

        }
    }
}
