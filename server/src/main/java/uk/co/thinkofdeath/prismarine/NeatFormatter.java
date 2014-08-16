package uk.co.thinkofdeath.prismarine;

import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class NeatFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(record.getMillis());

        builder.append("[");
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        padAndLimit(builder, Integer.toString(hours), '0', 2);
        builder.append(":");
        int minutes = calendar.get(Calendar.MINUTE);
        padAndLimit(builder, Integer.toString(minutes), '0', 2);
        builder.append("][");
        padAndLimit(builder, record.getLevel().getName(), ' ', 5);
        builder.append("][");
        String name = record.getLoggerName();
        if (name == null) {
            name = "AnonLogger";
        }
        padAndLimit(builder, name, ' ', 10);
        builder.append("]: ");
        builder.append(formatMessage(record));
        builder.append('\n');
        return builder.toString();
    }

    private static void padAndLimit(StringBuilder builder, String val, char padding, int length) {
        if (val.length() >= length) {
            builder.append(val, 0, length);
            return;
        }
        int count = length - val.length();
        for (int i = 0; i < count; i++) {
            builder.append(padding);
        }
        builder.append(val);
    }
}
