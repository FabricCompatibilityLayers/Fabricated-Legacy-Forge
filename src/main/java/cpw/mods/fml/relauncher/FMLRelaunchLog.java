/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.relauncher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.*;

public class FMLRelaunchLog {
    public static FMLRelaunchLog log = new FMLRelaunchLog();
    static File minecraftHome;
    private static boolean configured;
    private static Thread consoleLogThread;
    private static PrintStream errCache;
    private Logger myLog;

    private FMLRelaunchLog() {
    }

    private static void configureLogging() {
        LogManager.getLogManager().reset();
        Logger globalLogger = Logger.getLogger("global");
        globalLogger.setLevel(Level.OFF);
        log.myLog = Logger.getLogger("ForgeModLoader");
        Logger stdOut = Logger.getLogger("STDOUT");
        stdOut.setParent(log.myLog);
        Logger stdErr = Logger.getLogger("STDERR");
        stdErr.setParent(log.myLog);
        FMLLogFormatter formatter = new FMLLogFormatter();
        log.myLog.setUseParentHandlers(false);
        log.myLog.addHandler(new FMLRelaunchLog.ConsoleLogWrapper());
        consoleLogThread = new Thread(new FMLRelaunchLog.ConsoleLogThread());
        consoleLogThread.start();
        FMLRelaunchLog.ConsoleLogThread.wrappedHandler.setLevel(Level.parse(System.getProperty("fml.log.level", "INFO")));
        FMLRelaunchLog.ConsoleLogThread.wrappedHandler.setFormatter(formatter);
        log.myLog.setLevel(Level.ALL);

        try {
            File logPath = new File(minecraftHome, FMLRelauncher.logFileNamePattern);
            FileHandler fileHandler = new FileHandler(logPath.getPath(), 0, 3);
            fileHandler.setFormatter(formatter);
            fileHandler.setLevel(Level.ALL);
            log.myLog.addHandler(fileHandler);
        } catch (Exception var6) {
        }

        errCache = System.err;
        System.setOut(new PrintStream(new FMLRelaunchLog.LoggingOutStream(stdOut), true));
        System.setErr(new PrintStream(new FMLRelaunchLog.LoggingOutStream(stdErr), true));
        configured = true;
    }

    public static void log(Level level, String format, Object... data) {
        if (!configured) {
            configureLogging();
        }

        log.myLog.log(level, String.format(format, data));
    }

    public static void log(Level level, Throwable ex, String format, Object... data) {
        if (!configured) {
            configureLogging();
        }

        log.myLog.log(level, String.format(format, data), ex);
    }

    public static void severe(String format, Object... data) {
        log(Level.SEVERE, format, data);
    }

    public static void warning(String format, Object... data) {
        log(Level.WARNING, format, data);
    }

    public static void info(String format, Object... data) {
        log(Level.INFO, format, data);
    }

    public static void fine(String format, Object... data) {
        log(Level.FINE, format, data);
    }

    public static void finer(String format, Object... data) {
        log(Level.FINER, format, data);
    }

    public static void finest(String format, Object... data) {
        log(Level.FINEST, format, data);
    }

    public Logger getLogger() {
        return this.myLog;
    }

    private static class LoggingOutStream extends ByteArrayOutputStream {
        private Logger log;
        private StringBuilder currentMessage;

        public LoggingOutStream(Logger log) {
            this.log = log;
            this.currentMessage = new StringBuilder();
        }

        public void flush() throws IOException {
            Class var2 = FMLRelaunchLog.class;
            synchronized (FMLRelaunchLog.class) {
                super.flush();
                String record = this.toString();
                super.reset();
                this.currentMessage.append(record.replace(FMLLogFormatter.LINE_SEPARATOR, "\n"));
                if (this.currentMessage.lastIndexOf("\n") >= 0) {
                    if (this.currentMessage.length() > 1) {
                        this.currentMessage.setLength(this.currentMessage.length() - 1);
                        this.log.log(Level.INFO, this.currentMessage.toString());
                    }

                    this.currentMessage.setLength(0);
                }

            }
        }
    }

    private static class ConsoleLogThread implements Runnable {
        static ConsoleHandler wrappedHandler = new ConsoleHandler();
        static LinkedBlockingQueue<LogRecord> recordQueue = new LinkedBlockingQueue();

        private ConsoleLogThread() {
        }

        public void run() {
            while (true) {
                try {
                    LogRecord lr = (LogRecord) recordQueue.take();
                    wrappedHandler.publish(lr);
                } catch (InterruptedException var3) {
                    var3.printStackTrace(FMLRelaunchLog.errCache);
                    Thread.interrupted();
                }
            }
        }
    }

    private static class ConsoleLogWrapper extends Handler {
        private ConsoleLogWrapper() {
        }

        public void publish(LogRecord record) {
            boolean currInt = Thread.interrupted();

            try {
                FMLRelaunchLog.ConsoleLogThread.recordQueue.put(record);
            } catch (InterruptedException var4) {
                var4.printStackTrace(FMLRelaunchLog.errCache);
            }

            if (currInt) {
                Thread.currentThread().interrupt();
            }

        }

        public void flush() {
        }

        public void close() throws SecurityException {
        }
    }
}
