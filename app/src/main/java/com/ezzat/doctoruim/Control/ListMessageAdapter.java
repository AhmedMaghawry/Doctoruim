package com.ezzat.doctoruim.Control;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Model.Message;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;
import com.ezzat.doctoruim.View.MessageActivity;

import java.util.ArrayList;
import java.util.List;

import static com.ezzat.doctoruim.Control.Utils.Constants.ARG_MESS;
import static com.ezzat.doctoruim.Control.Utils.Constants.PLACEHOLDER_IMG;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.MessageHolder> {

    private List<Message> messages;
    private Context context;

    public ListMessageAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageHolder holder, final int position) {
        DatabaseController.getElement(USER_TABLE, messages.get(position).getPhone(), User.class, new onEvent() {
            @Override
            public void onStart(Object object) {

            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                User user = (User) object;
                holder.name.setText(user.getName());
                holder.phone.setText(user.getPhone());
                Glide.with(context).load(user.getImage()).placeholder(getImage(PLACEHOLDER_IMG)).into(holder.photo);
                holder.all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle args = new Bundle();
                        args.putSerializable(ARG_MESS, messages.get(position));
                        Utils.launchActivity(context.getApplicationContext(), MessageActivity.class, args);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_message,parent, false);
        return new MessageHolder(v);
    }

    public class MessageHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        private TextView name, phone;
        private LinearLayout all;

        public MessageHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            all = itemView.findViewById(R.id.all);
            phone = itemView.findViewById(R.id.phone);
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }

}
