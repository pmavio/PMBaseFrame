package com.pmavio.pmbaseframe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * 作者：Mavio
 * 日期：2016/3/1.
 */
public abstract class BaseRecylcerAdapter<E> extends ArrayRecyclerAdapter<E, BaseRecylcerAdapter.ViewHolder> {
    protected Context mContext;
    protected RequestManager imager;

    protected BaseRecylcerAdapter(Context context) {
        this.mContext = context;
        imager = Glide.with(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(getLayout(viewType), parent, false);
        return new ViewHolder(layout, viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecylcerAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v, position);
            }
        });
        onBind(viewHolder.getViewHolder(), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private BaseViewHolder viewHolder;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            viewHolder = BaseViewHolder.getViewHolder(itemView, viewType);
        }

        public BaseViewHolder getViewHolder() {
            return viewHolder;
        }
    }

    protected abstract void onItemClick(View v, int position);

    public abstract void onBind(BaseViewHolder viewHolder, int Position);

    protected abstract int getLayout(int viewType);

}
