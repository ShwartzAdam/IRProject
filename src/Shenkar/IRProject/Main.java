package Shenkar.IRProject;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Adam on 10/28/2016.
 */
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Controller controller = new Controller("index.sbl",new SEGui());
    }
}
