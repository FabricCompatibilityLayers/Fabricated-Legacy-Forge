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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

final class FMLLogFormatter extends Formatter {
    static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    FMLLogFormatter() {
    }

    public String format(LogRecord record) {
        StringBuilder msg = new StringBuilder();
        msg.append(this.dateFormat.format(record.getMillis()));
        Level lvl = record.getLevel();
        if (lvl == Level.FINEST) {
            msg.append(" [FINEST] ");
        } else if (lvl == Level.FINER) {
            msg.append(" [FINER] ");
        } else if (lvl == Level.FINE) {
            msg.append(" [FINE] ");
        } else if (lvl == Level.INFO) {
            msg.append(" [INFO] ");
        } else if (lvl == Level.WARNING) {
            msg.append(" [WARNING] ");
        } else if (lvl == Level.SEVERE) {
            msg.append(" [SEVERE] ");
        } else if (lvl == Level.SEVERE) {
            msg.append(" [" + lvl.getLocalizedName() + "] ");
        }

        if (record.getLoggerName() != null) {
            msg.append("[" + record.getLoggerName() + "] ");
        } else {
            msg.append("[] ");
        }

        msg.append(record.getMessage());
        msg.append(LINE_SEPARATOR);
        Throwable thr = record.getThrown();
        if (thr != null) {
            StringWriter thrDump = new StringWriter();
            thr.printStackTrace(new PrintWriter(thrDump));
            msg.append(thrDump.toString());
        }

        return msg.toString();
    }
}
