package com.ezzat.doctoruim.Control;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ListDoctorAdapter extends RecyclerView.Adapter<ListDoctorAdapter.UserHolder> {

    private List<User> users;
    private Context context;

    public ListDoctorAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, final int position) {
        holder.name.setText(users.get(position).getName());
        holder.spec.setText(users.get(position).getAddress());
        holder.phone.setText(users.get(position).getPhone());
        Glide.with(context).load(getImage(users.get(position).getImage_url())).into(holder.photo);
        holder.all.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Delete");
                pDialog.setContentText("Are you sure you want delete this user ?");
                pDialog.setConfirmText("Yes");
                pDialog.setCancelText("No");
                pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        pDialog.dismiss();
                    }
                });
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        users.remove(position);
                        notifyDataSetChanged();
                        pDialog.dismiss();
                    }
                });
                pDialog.show();
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
                .inflate(R.layout.list_item_doctor,parent, false);
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
