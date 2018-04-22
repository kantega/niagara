package org.kantega.niagara.source;

import org.kantega.niagara.Source;
import org.kantega.niagara.Emitter;
import org.kantega.niagara.sink.Sink;

import java.util.Queue;

public class QueueSource<O> implements Source<O> {

    final Queue<O> queue;

    public QueueSource(Queue<O> queue) {
        this.queue = queue;
    }

    @Override
    public Emitter build(Sink<O> emit, Done<O> done) {
        return () -> {
            O v = queue.poll();
            if (v != null) {
                emit.accept(v);
                return true;
            } else
                return false;
        };
    }
}
