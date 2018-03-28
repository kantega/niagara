package org.kantega.niagara.op;

import org.kantega.niagara.blocks.Block;
import org.kantega.niagara.blocks.OfferQueueRetryBlock;
import org.kantega.niagara.thread.WaitStrategy;

import java.util.Queue;

public class RetryOfferOp<A> implements Op<A, A> {

    final Queue<A> queue;
    final WaitStrategy strategy;

    public RetryOfferOp(Queue<A> queue, WaitStrategy strategy) {
        this.queue = queue;
        this.strategy = strategy;
    }


    @Override
    public Block<A> build(Scope scope, Block<A> block) {
        return new OfferQueueRetryBlock<>(queue, strategy, scope, block);
    }
}
