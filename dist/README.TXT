========================
BUILD OUTPUT DESCRIPTION
========================

When you build an Java application project that has a main class, the IDE
automatically copies all of the JAR
files on the projects classpath to your projects dist/lib folder. The IDE
also adds each of the JAR files to the Class-Path element in the application
JAR files manifest file (MANIFEST.MF).

To run the project from the command line, go to the dist folder and
type the following:

java -jar "LogFileAppenderReader.jar" 

To distribute this project, zip up the dist folder (including the lib folder)
and distribute the ZIP file.

Press 'View Log File' Button to View the current Log file in the text area.
Press 'Log Reader in Time Interval' to show number of Severities {INFO, WARNING, ERROR} during last Time
Interval, initialized to 60 seconds. It is configurable from text field 'Reader Time Interval'.
The text field 'Appender Time Interval' is not yet configurable.

There are three (3) Threads triggered by the application. 
One is the Appender Thread, and it is synchronized.
There is a second Thread that is triggered immediately by the 'Log Reader in Time Interval' button, 
with a couple of seconds sleep-over. It updates the Severities numbers in text area. This is synchronized too.
There is a third Thread running every 150 seconds (2.5 minutes) that runs through a "Reading" session to update 
the Severities in text area within the current time interval set. Therefore if you don't press the "Log Reader"
button, the update of Severities' numbers happens anyway every 150 seconds. It is not configurable yet, although
it could easily be made so. There is plenty (real estate) space left for that reason in the Swing window.

The Log File name is testLogFile.log and it resides at the root of the Netbeans project. It could be anywhere 
and it could be configurable. It gets created at first, if it does not exist.

The Reader is somewhat delayed in the text area update by the fact that prints info to Console about results 
found in log file.


Notes:

* If two JAR files on the project classpath have the same name, only the first
JAR file is copied to the lib folder.
* Only JAR files are copied to the lib folder.
If the classpath contains other types of files or folders, these files (folders)
are not copied.
* If a library on the projects classpath also has a Class-Path element
specified in the manifest,the content of the Class-Path element has to be on
the projects runtime path.
* To set a main class in a standard Java project, right-click the project node
in the Projects window and choose Properties. Then click Run and enter the
class name in the Main Class field. Alternatively, you can manually type the
class name in the manifest Main-Class element.
