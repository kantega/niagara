package org.kantega.niagara.state;

import org.kantega.niagara.sink.Sink;
import org.kantega.niagara.thread.WaitStrategy;

import java.util.function.Supplier;

public class TopLevelScope<O, X> implements Scope<O> {

    public final Sink<O> sink;
    public final WaitStrategy waitStrategy;
    final Instruction<O, X> start;
    private Step<?> currentStep;

    public TopLevelScope(Instruction<O, X> start, Sink<O> sink, WaitStrategy waitStrategy) {
        this.sink = sink;
        this.waitStrategy = waitStrategy;
        this.start = start;
    }


    public void loop() {
        currentStep = start.eval(this);
        while (!currentStep.complete()) {
            currentStep = currentStep.step();
        }
    }


    public <T> Step<T> wait(Supplier<Step<T>> next) {
        return Step.cont(() -> {
            waitStrategy.idle();
            return next.get();
        });
    }

    public <T> Step<T> resetWait(Supplier<Step<T>> next) {
        return Step.cont(() -> {
            waitStrategy.reset();
            return next.get();
        });
    }


    @Override
    public Sink<O> sink() {
        return sink;
    }

}
