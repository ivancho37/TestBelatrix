package com.belatrix.practicaltest;

import com.belatrix.practicaltest.dao.LogDao;
import com.belatrix.practicaltest.exception.LogException;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.logging.*;

public class JobLogger {

    private static final Logger logger = Logger.getLogger(JobLogger.class.getName());

    private boolean logToFile;
    private boolean logToConsole;
    private boolean logMessage;
    private boolean logWarning;
    private boolean logError;
    private boolean logToDatabase;
    private Map dbParams;


    public JobLogger(boolean logToFileParam, boolean logToConsoleParam,
                     boolean logToDatabaseParam, boolean logMessageParam,
                     boolean logWarningParam, boolean

                             logErrorParam, Map dbParamsMap) {

        logError = logErrorParam;
        logMessage = logMessageParam;
        logWarning = logWarningParam;
        logToDatabase = logToDatabaseParam;
        logToFile = logToFileParam;
        logToConsole = logToConsoleParam;
        dbParams = dbParamsMap;
    }

    public void logMessage(String messageText, boolean message, boolean
            warning, boolean error) throws Exception {

        messageText = Objects.nonNull(messageText) ? messageText.trim() : "";
        if (messageText == null || messageText.length() == 0) {
            return;
        }
        if (!logToConsole && !logToFile && !logToDatabase) {
            throw new LogException("Invalid configuration");
        }
        if ((!logError && !logMessage && !logWarning)
                || (!message && !warning && !error)) {
            throw new LogException(" Error or Warning or Message must be specified ");
        }

        int logType = obtainLogType(message, error, warning);
        String messageToLog = obtainMessage(message, error, warning, messageText);

        File logFile = new File(dbParams.get(" logFileFolder ") + "/logFile.txt ");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        FileHandler fh = new FileHandler(dbParams.get(" logFileFolder ") + "/logFile.txt ");

        ConsoleHandler ch = new ConsoleHandler();

        if (logToFile) {
            logger.addHandler(fh);
            logger.log(Level.INFO, messageToLog);
        }
        if (logToConsole) {
            logger.addHandler(ch);
            logger.log(Level.INFO, messageToLog);
        }
        if (logToDatabase) {
            LogDao logDao = new LogDao();
            logDao.setDbParams(dbParams);
            logDao.writeLog(messageToLog, logType);

        }
    }

    private int obtainLogType(boolean message, boolean error, boolean warning) {
        int logType = 0;
        if (message && logMessage) {
            logType = 1;
        }
        if (error && logError) {
            logType = 2;
        }
        if (warning && logWarning) {
            logType = 3;
        }
        return logType;
    }

    private String obtainMessage(boolean message, boolean error, boolean warning, String messageText){
        String messageToLog = "";
        if (error && logError) {
            messageToLog = " error ";
        }
        if (warning && logWarning) {
            messageToLog = " warning ";
        }
        if (message && logMessage) {
            messageToLog = " message ";
        }
        return messageToLog + " message " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + messageText;
    }

}