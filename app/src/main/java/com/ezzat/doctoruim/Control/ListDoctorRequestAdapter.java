package com.ezzat.doctoruim.Control;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Model.Request;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;
import com.ezzat.doctoruim.View.RequestActivity;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.ezzat.doctoruim.Control.Utils.Constants.ARG_REQ;

public class ListDoctorRequestAdapter extends RecyclerView.Adapter<ListDoctorRequestAdapter.UserHolder> {

    private List<Request> users;
    private Context context;

    public ListDoctorRequestAdapter(List<Request> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, final int position) {
        holder.name.setText(users.get(position).getOwner().getName());
        holder.spec.setText(users.get(position).getOwner().getAddress());
        holder.phone.setText(users.get(position).getOwner().getPhone());
        Glide.with(context).load(getImage(users.get(position).getOwner().getImage_url())).into(holder.photo);
        holder.all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putSerializable(ARG_REQ, users.get(position));
                Utils.launchActivity(context.getApplicationContext(), RequestActivity.class, args);
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
                .inflate(R.layout.list_item_doctor_request,parent, false);
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

    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }

}
