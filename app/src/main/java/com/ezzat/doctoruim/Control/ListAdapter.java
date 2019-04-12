package com.ezzat.doctoruim.Control;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.UserHolder> {

    private List<User> users;

    public ListAdapter(List<User> users) {
        this.users = users;

    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, final int position) {
        holder.name.setText(users.get(position).getName());
        holder.spec.setText(users.get(position).getAddress());
        holder.phone.setText(users.get(position).getPhone());
        holder.all.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                users.remove(position);
                notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent, false);
        return new UserHolder(v);
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        private TextView name, spec, phone;
        private LinearLayout all;

        public UserHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            spec = itemView.findViewById(R.id.sp);
            all = itemView.findViewById(R.id.all);
            phone = itemView.findViewById(R.id.phone);
        }
    }

}
