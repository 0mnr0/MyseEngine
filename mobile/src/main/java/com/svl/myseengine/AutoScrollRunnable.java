package com.svl.myseengine;

import androidx.recyclerview.widget.RecyclerView;

public class AutoScrollRunnable implements Runnable {
    private RecyclerView recyclerView;
    private RecyclerView.SmoothScroller smoothScroller;

    public AutoScrollRunnable(RecyclerView recyclerView, RecyclerView.SmoothScroller smoothScroller) {
        this.recyclerView = recyclerView;
        this.smoothScroller = smoothScroller;
    }

    @Override
    public void run() {
        recyclerView.getLayoutManager().startSmoothScroll(smoothScroller);
    }
}
