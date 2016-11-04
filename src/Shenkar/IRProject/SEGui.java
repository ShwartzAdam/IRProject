package Shenkar.IRProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Created by Almog on 30-Oct-16.
 */
public class SEGui extends JFrame {

    public static int HEIGHT = 1000;
    public static int WIDTH = 800;

    private JTextField queryTextField;
    private JButton runButton;
    private JButton addFileButton;
    private JPanel rootPanel;
    private JPanel queryPanel;
    private JPanel LibPanel;
    private JPanel operatorPanel;
    private JTextPane ansPanel;
    private JButton removeFileButton;
    private JLabel iconLabel;
    private JLabel IRLabel;
    private JButton cleanButton;
    private JButton aboutButton;
    private JLabel ResultsLabel;
    private JButton andButton;
    private JButton orButton;
    private JButton notButton;
    private JButton qoutButton;
    private String regex = "{}()[]'\\,-=+!@#$%^&;.*\"\":|<>?~";
    private String fileName;
    private String query;


    public SEGui() {
        super("Local Search Engine");
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        IRLabel.setFont(IRLabel.getFont().deriveFont(64.0f));
        iconLabel.setIcon(new ImageIcon("search.png"));
        ansPanel.setContentType("text/html");
        ansPanel.setEditable(false);
        rootPanel.setAutoscrolls(true);
        JScrollPane scrollPane = new JScrollPane(rootPanel);
        add(scrollPane);
        add(rootPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        queryTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (regex.indexOf(e.getKeyChar()) != -1)
                    e.consume();
            }
        });

        andButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               String str =  e.getActionCommand().toString();
                System.out.println(str);
                String appended_text = queryTextField.getText();
                queryTextField.setText(appended_text + " " + str + " ");

            }
        });
        orButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str =  e.getActionCommand().toString();
                System.out.println(str);
                String appended_text = queryTextField.getText();
                queryTextField.setText(appended_text + " " + str + " ");
            }
        });
        notButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str =  e.getActionCommand().toString();
                System.out.println(str);
                String appended_text = queryTextField.getText();
                queryTextField.setText(appended_text + " " + str + " ");
            }
        });
        qoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str =  e.getActionCommand().toString();
                System.out.println(str);
                String appended_text = queryTextField.getText();
                queryTextField.setText(appended_text + "\"");
            }
        });
        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = e.getActionCommand().toString();
                System.out.println(str);
                queryTextField.setText("");
            }
        });
    }


    public String getQuery()
    {
        return queryTextField.getText();
    }

    public void setAnswer(String answer){
        ansPanel.setText(answer);
    }

    void addRunListener(ActionListener runButton)
    {
        this.runButton.addActionListener(runButton);
        query=queryTextField.getText();
    }

    void addAddFileListener(ActionListener addFile)
    {
        addFileButton.addActionListener(addFile);
    }
    void addRemoveFileListener(ActionListener removeFile)
    {
        removeFileButton.addActionListener(removeFile);
    }

    public String getFilename() {
        return fileName;
    }




}

