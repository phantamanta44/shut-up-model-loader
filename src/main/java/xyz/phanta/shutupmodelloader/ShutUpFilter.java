package xyz.phanta.shutupmodelloader;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

import javax.annotation.Nullable;

public class ShutUpFilter extends AbstractFilter {

    private final Matcher matcher;

    public ShutUpFilter(Matcher matcher) {
        this.matcher = matcher;
    }

    private Result filter(@Nullable String msg, @Nullable Throwable thrown) {
        return matcher.shouldExclude(msg, thrown) ? onMismatch : onMatch;
    }

    private Result filter(Message msg, @Nullable Throwable thrown) {
        return filter(msg.getFormattedMessage(), thrown);
    }

    @Override
    public Result filter(LogEvent event) {
        return filter(event.getMessage(), event.getThrown());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, @Nullable Throwable t) {
        return filter(msg, t);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, @Nullable Object msg, @Nullable Throwable t) {
        return (msg == null || msg instanceof String) ? filter((String)msg, t)
                : (msg instanceof Message) ? filter((Message)msg, t) : onMatch;
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, @Nullable String msg, Object... params) {
        return filter(msg, null);
    }

    @FunctionalInterface
    public interface Matcher {

        boolean shouldExclude(@Nullable String msg, @Nullable Throwable thrown);

    }

}
