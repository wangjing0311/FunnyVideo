package com.simaben.autoswaprefresh;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.socks.library.KLog;

/**
 * ClassName: AutoLoadMoreRecyclerView<p>
 * Author: oubowu<p>
 * Fuction: 添加了滑动到底部自动加载的RecyclerView<p>
 * CreateDate: 2016/2/21 16:30<p>
 * UpdateUser: <p>
 * UpdateDate: <p>
 */
public class AutoLoadMoreRecyclerView extends RecyclerView {

    private int[] mVisiblePositions;

    // 所处的状态
    public static final int STATE_MORE_LOADING = 1;
    public static final int STATE_MORE_LOADED = 2;
    public static final int STATE_ALL_LOADED = 3;

    private int mCurrentState = STATE_MORE_LOADED;

    /**
     * 是否正在加载底部
     *
     * @return
     */
    public boolean isMoreLoading() {
        return mCurrentState == STATE_MORE_LOADING;
    }

    public boolean isAllLoaded() {
        return mCurrentState == STATE_ALL_LOADED;
    }

    public AutoLoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        super.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (mCurrentState == STATE_MORE_LOADED  && calculateRecyclerViewFirstPosition() == getAdapter()
                        .getItemCount()-1 && mLoadMoreListener != null) {
                    // 之前的状态为非正在加载状态
                    KLog.e("加载更多数据");
                    mLoadMoreListener.loadMore();
                    mCurrentState = STATE_MORE_LOADING;
                }

            }
        });
    }

    /**
     * 计算RecyclerView当前第一个完全可视位置
     */
    private int calculateRecyclerViewFirstPosition() {
        // 判断LayoutManager类型获取第一个完全可视位置
        if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            if (mVisiblePositions == null) {
                mVisiblePositions = new int[((StaggeredGridLayoutManager) getLayoutManager())
                        .getSpanCount()];
            }
            ((StaggeredGridLayoutManager) getLayoutManager())
                    .findLastCompletelyVisibleItemPositions(mVisiblePositions);
            int max = -1;
            for (int pos : mVisiblePositions) {
                max = Math.max(max, pos);
            }
            return max;
            // return mVisiblePositions[0];
        } else if (getLayoutManager() instanceof GridLayoutManager) {
            return ((GridLayoutManager) getLayoutManager()).findLastCompletelyVisibleItemPosition();
        } else {
            return ((LinearLayoutManager) getLayoutManager())
                    .findLastCompletelyVisibleItemPosition();
        }
    }

    /**
     * 通知底部加载完成了
     */
    public void notifyMoreLoaded() {
        mCurrentState = STATE_MORE_LOADED;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    private OnLoadMoreListener mLoadMoreListener;

    public interface OnLoadMoreListener {
        void loadMore();
    }

    public AutoLoadMoreRecyclerView setAutoLayoutManager(LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        return this;
    }

    public AutoLoadMoreRecyclerView addAutoItemDecoration(ItemDecoration decor) {
        super.addItemDecoration(decor);
        return this;
    }

    public AutoLoadMoreRecyclerView setAutoItemAnimator(ItemAnimator anim) {
        super.setItemAnimator(anim);
        return this;
    }

    public AutoLoadMoreRecyclerView setAutoAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        return this;
    }

    public AutoLoadMoreRecyclerView setAutoHasFixedSize(boolean fixed) {
        super.setHasFixedSize(fixed);
        return this;
    }

    public AutoLoadMoreRecyclerView setAutoItemAnimatorDuration(int duration) {
        super.getItemAnimator().setAddDuration(duration);
        super.getItemAnimator().setMoveDuration(duration);
        super.getItemAnimator().setChangeDuration(duration);
        super.getItemAnimator().setRemoveDuration(duration);
        return this;
    }


}
