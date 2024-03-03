package com.svl.myseengine;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class AutoScrollRunnable implements Runnable {
    private final RecyclerView recyclerView;
    private final RecyclerView.SmoothScroller smoothScroller;

    public AutoScrollRunnable(RecyclerView recyclerView, RecyclerView.SmoothScroller smoothScroller) {
        this.recyclerView = recyclerView;
        this.smoothScroller = smoothScroller;
    }

    @Override
    public void run() {
        Objects.requireNonNull(recyclerView.getLayoutManager()).startSmoothScroll(smoothScroller);
    }
}
