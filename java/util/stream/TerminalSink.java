package java.util.stream;

import java.util.function.*;

interface TerminalSink<T, R> extends Sink<T>, Supplier<R>
{
}
