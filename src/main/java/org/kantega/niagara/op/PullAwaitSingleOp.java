package org.kantega.niagara.op;

import fj.P;
import fj.P2;
import fj.Unit;
import org.kantega.niagara.blocks.Block;
import org.kantega.niagara.blocks.PullAwaitSingleBlock;
import org.kantega.niagara.thread.WaitStrategy;

import java.util.Queue;
import java.util.function.Supplier;

public class PullAwaitSingleOp<A> implements Op<Unit, A> {

    final Queue<A> queue;
    final Supplier<WaitStrategy> waitStrategy;

    public PullAwaitSingleOp(Queue<A> queue, Supplier<WaitStrategy> waitStrategy) {
        this.queue = queue;
        this.waitStrategy = waitStrategy;
    }

    @Override
    public P2<Scope, Block<Unit>> build(Scope scope, Block<A> block) {
        return P.p(scope, new PullAwaitSingleBlock<>(queue, waitStrategy.get(), scope.getFlag(), block));
    }
}
