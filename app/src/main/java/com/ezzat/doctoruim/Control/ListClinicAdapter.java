package com.ezzat.doctoruim.Control;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezzat.doctoruim.Control.Utils.Utils;
import com.ezzat.doctoruim.Model.Clinic;
import com.ezzat.doctoruim.Model.Reservation;
import com.ezzat.doctoruim.Model.ReservationStatus;
import com.ezzat.doctoruim.Model.User;
import com.ezzat.doctoruim.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.ezzat.doctoruim.Control.Utils.Constants.CLINIC_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Constants.PLACEHOLDER_IMG;
import static com.ezzat.doctoruim.Control.Utils.Constants.USER_TABLE;
import static com.ezzat.doctoruim.Control.Utils.Utils.getStrings;

public class ListClinicAdapter extends RecyclerView.Adapter<ListClinicAdapter.UserHolder> {

    private List<Clinic> clinics;
    private Activity context;

    public ListClinicAdapter(List<Clinic> clinics, Activity context) {
        this.clinics = clinics;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, final int position) {
        final Clinic c = clinics.get(position);
        holder.name.setText(c.getName());
        holder.loc.setText(c.getLocation());
        holder.days.setText(getStrings(c.getDays()));
        holder.time.setText(c.getStartTime() + " - " + c.getEndTime());
        holder.phones.setText(getStrings(c.getPhones()));
        holder.all.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Delete Clinic");
                pDialog.setContentText("Are you sure you want to delete this Clinic ?");
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
                        c.deleteClinic();
                        clinics.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, clinics.size());
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
        return clinics.size();
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_clinic,parent, false);
        return new UserHolder(v);
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        private TextView name, loc, days, time, phones;
        private LinearLayout all;

        public UserHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            loc = (TextView) itemView.findViewById(R.id.loc);
            days = (TextView) itemView.findViewById(R.id.days);
            time = (TextView) itemView.findViewById(R.id.time);
            phones = (TextView) itemView.findViewById(R.id.phones);
            all = itemView.findViewById(R.id.all);
        }
    }

}
