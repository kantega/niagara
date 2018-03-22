package org.kantega.niagara.op;

import fj.P2;
import org.kantega.niagara.blocks.Block;

public interface Op<A, B> {

    default <C> Op<A, C> fuse(Op<B, C> other) {
        return new ComposedOp<>(this, other);
    }

    P2<Scope,Block<A>> build(Scope scope, Block<B> block);

}
