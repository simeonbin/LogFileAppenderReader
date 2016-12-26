/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Simeon
 */
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;

class LogFileAppenderReader extends JFrame {
  // Text file info
  private JTextField jtfFilename = new JTextField();
  private JTextArea jtaFile = new JTextArea();

  
  static JTextField jtfReaderTimeInterval = new JTextField(); 
  private JTextField jtfAppenderTimeInterval = new JTextField();
  
  static JTextArea jtaTableName = new JTextArea();

  private JButton jbtViewFile = new JButton("View Log File");
  private JButton jbtCopy = new JButton("Log Reader in Time Interval ");
  private JLabel jlblStatus = new JLabel();

  @SuppressWarnings("OverridableMethodCallInConstructor")
  public LogFileAppenderReader() {
      
      jtfReaderTimeInterval.setPreferredSize(new Dimension(200, 30) );
      jtfAppenderTimeInterval.setPreferredSize(new Dimension (190, 30) );
      jtfReaderTimeInterval.setText("60");
      jtfAppenderTimeInterval.setText("6");
      
    JPanel jPanel1 = new JPanel();
    jPanel1.setLayout(new FlowLayout());

    jtfFilename.setSize(50, 20);
    jtfFilename.setText("testLogFile.log");
    jPanel1.add(new JLabel("Log Filename")); //, BorderLayout.WEST);
    jPanel1.add(jtfFilename); //, BorderLayout.CENTER);
    jPanel1.add(jbtViewFile); //, BorderLayout.EAST);
    
    JPanel jPanel2 = new JPanel();
    jPanel2.setLayout(new BorderLayout());
    jPanel2.setBorder(new TitledBorder("Log File Reader"));
    jPanel2.add(jPanel1, BorderLayout.NORTH);
    jPanel2.add(new JScrollPane(jtaFile), BorderLayout.CENTER);

    JPanel jPanel3_1 = new JPanel();
    JPanel jPanel3_2 = new JPanel();
    JPanel jPanel3_3 = new JPanel();
    JPanel jPanel3   = new JPanel();
    
    jPanel3_1.setLayout(new FlowLayout());
    
    jPanel3_1.add(new JLabel("Reader Time Interval: "));
    jtfReaderTimeInterval.setEditable(true);
    jPanel3_1.add(jtfReaderTimeInterval);
    jPanel3_1.add(new JLabel("sec"));
    
    jPanel3_2.add(new JLabel("Appender Time Interval: "));
    jtfAppenderTimeInterval.setEditable(false);
    jPanel3_2.add(jtfAppenderTimeInterval);
    jPanel3_2.add(new JLabel("sec"));
    
    jPanel3_3.add(new JLabel("Severities in Time Interval: "));
    
    jtaTableName.setPreferredSize(new Dimension (120, 150));
    jtaTableName.setText(null);                                
     jtaTableName.append("\n");
     jtaTableName.append("INFO: " + 0 + '\n');
     jtaTableName.append("WARNING: " + 0 + '\n');
     jtaTableName.append("ERROR: " + 0 + '\n');
     jtaTableName.append("\n");
     jtaTableName.append("LogFileCounter: " + 0 + '\n');
     jtaTableName.append("\n\n");
     
    jPanel3_3.add(jtaTableName);

 // Set the Boxayout to be Y_AXIS from top to down
    BoxLayout boxlayout = new BoxLayout(jPanel3, BoxLayout.Y_AXIS);
    
    jPanel3.setLayout(boxlayout);
  //  jPanel3.setBorder(new EmptyBorder(new Insets(150, 200, 150, 200)));
    
    jPanel3.add(jPanel3_1);
    jPanel3.add(jPanel3_2);
    jPanel3.add(jPanel3_3);
    
    JPanel jPanel5 = new JPanel();
    jPanel5.setLayout(new BorderLayout());
    jPanel5.setBorder(new TitledBorder("Log File Severity Messages"));
    jPanel5.add(jbtCopy, BorderLayout.SOUTH);
    jPanel5.add(jPanel3, BorderLayout.CENTER);
 //   jPanel5.add(jPanel4, BorderLayout.CENTER);
    add(jlblStatus, BorderLayout.SOUTH);
        
    add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
      jPanel2, jPanel5), BorderLayout.CENTER);
    
    jbtViewFile.addActionListener((ActionEvent evt) -> {
        showFile();
      });

    jbtCopy.addActionListener((ActionEvent evt) -> {
        try {
            startReader();
        }
        catch (Exception ex) {
            jlblStatus.setText(ex.toString());
        }
      });
  }

  /** Display the file in the text area */
  private void showFile() {
    Scanner input = null;
    jtfFilename.setText("testLogFile.log");
    try {
      // Use a Scanner to read text from the file
  //    input = new Scanner(new File(jtfFilename.getText().trim()));
  
  input = new Scanner(new File("testLogFile.log" ) );

      // Read a line and append the line to the text area
      while (input.hasNext())
        jtaFile.append(input.nextLine() + '\n');
    }
    catch (FileNotFoundException ex) {
      System.out.println("File not found: " + jtfFilename.getText());
    }
//    catch (IOException ex) {
//      ex.printStackTrace();
//    }
    finally {
      if (input != null) input.close();
    }
  }

  private void startReader() throws Exception {

    String filePath_Test2 = System.getProperty("user.dir");  
    String filePath_Test = filePath_Test2 + "\\testLogFile.log";
// String filePath = "C:\\Users\\Simeon\\Documents\\NetBeansProjects\\LogFileAppenderReader\\testLogFile.log";
    String filePath = filePath_Test;
    
    Runnable logFileReader2 =  new LogFile_Appender_Reader.LogFileReader(filePath, 1000);
    Thread  thread3 = new Thread(logFileReader2, "Log File Reader Thread-3");
    
    thread3.start();
 
  }


  
}
