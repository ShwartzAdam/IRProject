package Shenkar.IRProject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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

        }
    }

    private class RunListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String word=gui.getQuery();
            if(!word.isEmpty())
            {
                gui.setAnswer(index.search(word));
            }

        }
    }
}
