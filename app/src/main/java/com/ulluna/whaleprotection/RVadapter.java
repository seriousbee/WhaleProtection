package com.ulluna.whaleprotection;

import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tomaszczernuszenko on 04/03/2017.
 */

public class RVadapter extends RecyclerView.Adapter<RVadapter.MyViewHolder> {

    ArrayList<Message> messages;

    public RVadapter(ArrayList<Message> messageArrayList){
        messages = messageArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch(viewType){
            case Message.CONSULTANT_ID:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_element_consultant, parent, false);
                break;
            case Message.USER_ID:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_element_user, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_element_user, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getId();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message currentMessage = messages.get(position);
        holder.messageText.setText(currentMessage.getText());
        if(position!=0 && messages.get(position-1).getId() == messages.get(position).getId()){
            holder.imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateData(ArrayList<Message> newMessages){
        messages = (ArrayList<Message>) newMessages.clone();
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView messageText;
        public ImageView imageView;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.message);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) view.findViewById(R.id.holderLL);
        }
    }
}

