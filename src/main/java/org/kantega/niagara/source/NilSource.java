package org.kantega.niagara.source;

import org.kantega.niagara.Source;
import org.kantega.niagara.Emitter;
import org.kantega.niagara.sink.Sink;

public class NilSource<O> implements Source<O> {
    @Override
    public Emitter build(Sink<O> emit, Done<O> done) {
        return () -> {
            done.done(this);
            return false;
        };
    }

    @Override
    public boolean isNil() {
        return true;
    }
}
