package com.tae.a150retrofit;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>

{
    public interface onItemClickListener {
        public void onItemClick();
    }

    private List<PojoVal> posts;
    Context context;
    public PostsAdapter(Context context) {
        this.posts = new ArrayList<>();
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PojoVal post = posts.get(position);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.post.setText(post.getTxt());
        } else {
            holder.post.setText(Html.fromHtml(post.getCc()));
        }
        // поискать решение округления
        double rate;
        rate = post.getRate();
        double rate2 = Math.round(rate);

        holder.site.setText(post.getRate().toString());
    }

    @Override
    public int getItemCount() {
        if (posts == null)
            return 0;
        System.out.println(" POSTS SIZE !!! ! ! !!!!!!!!!!!            "+posts.size());
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView post;
        TextView site;

        public ViewHolder(View itemView) {
            super(itemView);
            post = (TextView) itemView.findViewById(R.id.val);
         site = (TextView) itemView.findViewById(R.id.rate);

        }
    }

    public void setData(ArrayList<PojoVal> pojoNbu) {
       this.posts.addAll(pojoNbu);
        notifyDataSetChanged();
    }

}
