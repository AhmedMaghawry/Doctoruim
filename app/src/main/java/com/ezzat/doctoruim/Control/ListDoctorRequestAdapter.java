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
import com.ezzat.doctoruim.Model.Request;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;
import com.ezzat.doctoruim.View.Admin.RequestActivity;

import java.util.List;

import static com.ezzat.doctoruim.Control.Utils.Constants.ARG_REQ;
import static com.ezzat.doctoruim.Control.Utils.Constants.PLACEHOLDER_IMG;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;

public class ListDoctorRequestAdapter extends RecyclerView.Adapter<ListDoctorRequestAdapter.UserHolder> {

    private List<Request> requests;
    private Context context;

    public ListDoctorRequestAdapter(List<Request> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, final int position) {
        DatabaseController.getElement(USER_TABLE, requests.get(position).getPhone(), User.class, new onEvent() {
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
                        args.putSerializable(ARG_REQ, requests.get(position));
                        Utils.launchActivity(context.getApplicationContext(), RequestActivity.class, args);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
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
        private TextView name, phone;
        private LinearLayout all;

        public UserHolder(View itemView) {
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
