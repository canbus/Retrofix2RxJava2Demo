package com.bao.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bao.myapplication.Entry.Top250SubjectEntry;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

class FragmentCont1Adapter extends RecyclerView.Adapter {
    private List<Top250SubjectEntry.Subject> mSubjectsList = null;
    private Context mContext;

    public FragmentCont1Adapter(Context context) {
        this.mContext = context;
    }
    public void setData(List<Top250SubjectEntry.Subject> subjects)
    {
        this.mSubjectsList =subjects;
    }
    public void addData(Top250SubjectEntry.Subject subject,boolean down) {
        if(mSubjectsList == null)
            mSubjectsList = new ArrayList<>();
        if(down)
            mSubjectsList.add(0,subject);
        else
            mSubjectsList.add(subject);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MyViewHolder viewHolder = new MyViewHolder(View.inflate(mContext,R.layout.recyclerview_item,null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Top250SubjectEntry.Subject subject = mSubjectsList.get(i);
        ((MyViewHolder)viewHolder).tv_title.setText(subject.getTitle());
        String strCasts = "";
        for(int j=0;j<subject.getCasts().size();j++)
        {
            strCasts+=subject.getCasts().get(j).getName();
        }
        ((MyViewHolder)viewHolder).tv_casts.setText(strCasts);
        ((MyViewHolder)viewHolder).tv_genres.setText(new Gson().toJson(subject.getGenres()));
        String uri = subject.getImages().getLarge();
        ImageLoader.getInstance().displayImage(uri,((MyViewHolder)viewHolder).imageView);
    }

    @Override
    public int getItemCount() {
        return mSubjectsList==null?0:mSubjectsList.size();
    }



    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_genres,tv_casts;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageVew);
            tv_title = itemView.findViewById(R.id.id_tv_title);
            tv_genres = itemView.findViewById(R.id.id_tv_genres);
            tv_casts = itemView.findViewById(R.id.id_tv_casts);
        }
    }
}
