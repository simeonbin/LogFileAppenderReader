/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Simeon
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JFrame;
 
/**
 * 
 * @author simeon
 */
 public class LogFile_Appender_Reader {
    // static LogFileReader logFileReader = null;
     
     public static void main(String argv[]) {
            
             JFrame frame = new LogFileAppenderReader();
                 frame.setTitle("LogFileAppenderReader");
                frame.setSize(900, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
 
		String filePath_Test2 = System.getProperty("user.dir");  
                String filePath_Test = filePath_Test2 + "\\testLogFile.log";
// String filePath = "C:\\Users\\Simeon\\Documents\\NetBeansProjects\\LogFileAppenderReader\\testLogFile.log";
                String filePath = filePath_Test;
                
                Runnable logFileTailer = new LogFileTailer(filePath, 1000);
                Runnable logFileReader =  new LogFileReader(filePath, 150000);

                Thread  thread1 = new Thread(logFileTailer, "Log File Appender Thread");
                Thread  thread2 = new Thread(logFileReader, "Log File Reader Thread-2");
                    
                thread1.start();
                thread2.start();

                LogFileTailer.appendData(filePath, true, 5000);
   
	}

     
     public static class LogFileTailer implements Runnable {
 
	private boolean debug = false;
 
	private int logFileRunEveryNSeconds = 2000;
	private long lastKnownPosition = 0;
	private boolean shouldIRun = true;
	private File logFile = null;
	private static int logFileCounter = 0;
 
	public LogFileTailer(String myFile, int myInterval) {
		logFile = new File(myFile);
		this.logFileRunEveryNSeconds = myInterval;
	}
 
	private void printLine(String message) {
		System.out.println(message);
	}
 
	public void stopRunning() {
		shouldIRun = false;
	}
 
        @Override
	public void run() {
            
		try {
                    synchronized(this) {
			while (shouldIRun) {
                    System.out.println(Thread.currentThread().getName());
                            Thread.sleep(logFileRunEveryNSeconds);
				long fileLength = logFile.length();
				if (fileLength > lastKnownPosition) {
 
					// Reading and writing file
					RandomAccessFile readWriteFileAccess = new RandomAccessFile(logFile, "rw");
					readWriteFileAccess.seek(lastKnownPosition);
					String logFileLine = null;
					while ((logFileLine = readWriteFileAccess.readLine()) != null) {
						this.printLine(logFileLine);
						logFileCounter++;
					}
					lastKnownPosition = readWriteFileAccess.getFilePointer();
					readWriteFileAccess.close();
                                        Thread.sleep(1000);
				} else {
					if (debug)
						this.printLine("Hmm.. Couldn't found new line after line # " + logFileCounter);
				}
			}
                      
                    } // synchronized
                    } catch (Exception e) {
                            stopRunning();
                          }
                            if (debug)
                                this.printLine("Exit the program...");
        }
        

        
        /**
	 * Use appendData method to add new line to file, so above tailer method can print the same in Netbeans (Eclipse) Console
	 * 
	 * @param filePath
	 * @param shouldIRun
	 * @param logFileRunEveryNSeconds
	 */
	private static synchronized void appendData(String filePath, boolean shouldIRun, int logFileRunEveryNSeconds) {
		FileWriter fileWritter;
                String strSeverity;
 
      		try {
			while (shouldIRun) {
				Thread.sleep(logFileRunEveryNSeconds);
				fileWritter = new FileWriter(filePath, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                                double rand = Math.random();
                            //Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
                            
                            Instant timestamp = Instant.now();
                                if (rand <= 0.33) strSeverity="INFO";
                                else if (rand <= 0.66) strSeverity="WARNING";
                                     else strSeverity = "ERROR";                                
                                
			      	String data = "\n" + timestamp + " " + strSeverity + "  " + System.currentTimeMillis();
                                
				bufferWritter.write(data);
				bufferWritter.close();
                                Thread.sleep(1000);
			}
 
		} catch (Exception e) {
			e.printStackTrace();
		}
 	}
     }
     

public static class LogFileReader implements Runnable {
 
	private boolean debug = false;
 
	int logFileRunEveryNSecondsRead = 2000;
	long lastKnownPositionRead = 0;
	boolean shouldIRunReader = true;
	File logFile = null;
	int logFileCounterRead = 0;
        int infoCounter, warningCounter, errorCounter;
        String severityLevelString;
        int readerTimeInterval = 60;
 
	public LogFileReader(String myFile, int myInterval) {
		logFile = new File(myFile);
		logFileRunEveryNSecondsRead = myInterval;
                infoCounter=0; warningCounter=0; errorCounter=0;
                severityLevelString = ""; 
                readerTimeInterval = Integer.valueOf(LogFileAppenderReader.jtfReaderTimeInterval.getText());
	}

        LogFileReader(File filepath, int i) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
 
	private void printLine(String message) {
		System.out.println(message);
	}
 
	public void stopRunning() {
		shouldIRunReader = false;
	}
                
            public void run() {
                
                try {
                    synchronized(this) {
			while (shouldIRunReader) {
                    System.out.println(String.format("execute thread: %s",
                        Thread.currentThread().getName()) );
                                Thread.sleep(logFileRunEveryNSecondsRead);
				long fileLength = logFile.length();
                                infoCounter=0; warningCounter=0; errorCounter=0;
                                
				if (fileLength > lastKnownPositionRead) {
                                                                     
                                    RandomAccessFile readFileAccess = new RandomAccessFile(logFile, "r") ;
                              
                                        readFileAccess.seek(0);
                                        String logFileLine; //= null;
                                        
                                        logFileLine = readFileAccess.readLine();

                                        while ( (logFileLine = readFileAccess.readLine()) != null) {
                                            System.out.println(logFileLine);
                                            if (logFileLine == " ") continue;
                                            logFileCounterRead++;
                                            
                                            String lineOfLog = logFileLine;
                                            
                                            String[] arrayOfLineOfLog = lineOfLog.split("\\s+");
                                            double timeDiff  = ( System.currentTimeMillis() - Double.valueOf(arrayOfLineOfLog[2]) ) / 1000;
                                            severityLevelString = arrayOfLineOfLog[1];
                                            
                                            if (timeDiff <= readerTimeInterval )  {         //(10*86400)
                                                switch (severityLevelString) {
                                                    case "INFO":
                                                        infoCounter++;
                                                        break;
                                                    case "WARNING":
                                                        warningCounter++;
                                                        break;
                                                    default:
                                                        errorCounter++;
                                                        break;
                                                }
                                            };
                      
                                            System.out.println ("Milliseconds: " + arrayOfLineOfLog[2] + " TimeDiff: " + timeDiff + " SeverityLevel: " + severityLevelString +
                                                    " DateTime Now: " + Instant.now() ) ;
                                            
                                            System.out.println ();
                                            
                                        }
                                       lastKnownPositionRead = readFileAccess.getFilePointer();
                                // lastKnownPositionRead = 0;
                                        
                                System.out.println("INFO: " + infoCounter);
                                System.out.println("WARNING: " + warningCounter);
                                System.out.println("ERROR: " + errorCounter);
                                
                                System.out.println("LogFileCounter: " + logFileCounterRead);
                                
                              
                                LogFileAppenderReader.jtaTableName.setText(null);
                                
                                LogFileAppenderReader.jtaTableName.append("\n");
                                LogFileAppenderReader.jtaTableName.append("INFO: " + infoCounter + '\n');
                                LogFileAppenderReader.jtaTableName.append("WARNING: " + warningCounter + '\n');
                                LogFileAppenderReader.jtaTableName.append("ERROR: " + errorCounter + '\n');
                                LogFileAppenderReader.jtaTableName.append("\n");
                                LogFileAppenderReader.jtaTableName.append("LogFileCounter: " + logFileCounterRead + '\n');
                                LogFileAppenderReader.jtaTableName.append("\n\n");
                                shouldIRunReader = false;
                            }
                        } 
                }
    
            } catch (Exception e) {
                stopRunning();
                }
		if (debug)
			this.printLine("Exit the program...");
            }
            
        }  // LogFileReader
 }
