package com.ezzat.doctoruim.Control.RecycleView_Products;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Control.DatabaseController;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Control.onEvent;
import com.ezzat.doctoruim.Model.Doctor;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;
import com.ezzat.doctoruim.View.Doctor.DoctorProfileActivity;

import java.util.ArrayList;
import java.util.List;

import static com.ezzat.doctoruim.Control.Utils.Constants.DOCTOR_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.PLACEHOLDER_IMG;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> implements Filterable {

    private List<User> allUsers;
    private List<User> usersListFiltered;
    private DoctorAdapterListener listener;
    private Context context;

    public DoctorsAdapter(Context context, List<User> allUsers, DoctorAdapterListener listener) {
        this.context = context;
        this.allUsers = allUsers;
        this.usersListFiltered = allUsers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctor_item,parent, false);
        return new DoctorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorViewHolder holder, int position) {
        User user = usersListFiltered.get(position);
        holder.name.setText(user.getName());
        holder.phone.setText(user.getPhone());
        holder.address.setText(user.getAddress());
        DatabaseController.getElement(DOCTOR_TABLE, user.getPhone(), Doctor.class, new onEvent() {
            @Override
            public void onStart(Object object) {

            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                Doctor d = (Doctor) object;
                holder.sepecifications.setText(Utils.getStrings(d.getSps()));
                holder.rate.setRating(Float.parseFloat(d.getRate()));
            }
        });
        try {
            Glide.with(context).load(user.getImage()).placeholder(getImage(PLACEHOLDER_IMG)).into(holder.photo);
        } catch (Exception e) {
            Glide.with(context).load(getImage(PLACEHOLDER_IMG)).into(holder.photo);
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }

    @Override
    public int getItemCount() {
        return usersListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    usersListFiltered = allUsers;
                } else {
                    List<User> filteredList = new ArrayList<>();
                    for (User row : allUsers) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    usersListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = usersListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                try {
                    usersListFiltered = (ArrayList<User>) filterResults.values;
                } catch (Exception e) {

                }
                notifyDataSetChanged();
            }
        };
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, sepecifications, address;
        ImageView photo;
        RatingBar rate;

        public DoctorViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            sepecifications = itemView.findViewById(R.id.spec);
            address = itemView.findViewById(R.id.address);
            photo = itemView.findViewById(R.id.image);
            rate = itemView.findViewById(R.id.rate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onDoctorSelected(usersListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface DoctorAdapterListener {
        void onDoctorSelected(User user);
    }

}
