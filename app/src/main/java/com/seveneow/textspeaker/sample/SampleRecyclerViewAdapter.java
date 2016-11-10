package com.seveneow.textspeaker.sample;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SampleRecyclerViewAdapter extends RecyclerView.Adapter {
  private Context context;
  private ArrayList<String> messageList;
  private OnClickListener clickListener = null;

  public SampleRecyclerViewAdapter(Context context, ArrayList<String> messageList) {
    this.context = context;
    this.messageList = messageList;
  }

  public void setClickListener(OnClickListener clickListener){
    this.clickListener = clickListener;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.list_item_view, null);
    RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    view.setLayoutParams(params);
    ViewHolder holder = new ViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    ViewHolder viewHolder = (ViewHolder) holder;
    viewHolder.textView.setText(messageList.get(position));
    viewHolder.data = messageList.get(position);
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textView;
    public String data;

    public ViewHolder(View itemView) {
      super(itemView);
      textView = (TextView) itemView.findViewById(R.id.text_view);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      if(clickListener != null){
        clickListener.onItemClick(data);
      }
    }
  }

  @Override
  public int getItemCount() {
    return messageList.size();
  }

  public static interface OnClickListener{
    public void onItemClick(String message);
  }
}
