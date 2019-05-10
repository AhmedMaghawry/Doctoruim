package com.ezzat.doctoruim.Control;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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

public class ListReservationAdapter extends RecyclerView.Adapter<ListReservationAdapter.UserHolder> {

    private List<Reservation> reservations;
    private Activity context;

    public ListReservationAdapter(List<Reservation> reservations, Activity context) {
        this.reservations = reservations;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, final int position) {
        holder.date.setText(reservations.get(position).getDate().toString());
        DatabaseController.getElement(USER_TABLE, reservations.get(position).getPatientID(), User.class, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(context);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                User u = (User) object;
                Glide.with(context).load(u.getImage()).placeholder(getImage(PLACEHOLDER_IMG)).into(holder.patientPhoto);
                holder.patientName.setText(u.getName());
                DatabaseController.getElement(CLINIC_TABLE, reservations.get(position).getClinicID(), Clinic.class, new onEvent() {
                    @Override
                    public void onStart(Object object) {

                    }

                    @Override
                    public void onProgress(Object object) {

                    }

                    @Override
                    public void onEnd(Object object) {
                        Clinic c = (Clinic) object;
                        holder.clinicName.setText(c.getName());
                        Utils.hideDialog();
                    }
                });
            }
        });
        holder.all.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Reservation");
                pDialog.setContentText("Do you want to accept the reservation ?");
                pDialog.setConfirmText("Yes");
                pDialog.setCancelText("No");
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        reservations.get(position).setStatus(ReservationStatus.REJECT);
                        reservations.get(position).updateReservation();
                        pDialog.dismiss();
                    }
                });
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        reservations.get(position).setStatus(ReservationStatus.ACCEPT);
                        reservations.get(position).updateReservation();
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
        return reservations.size();
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_reservation,parent, false);
        return new UserHolder(v);
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        private ImageView patientPhoto;
        private TextView patientName, clinicName, date;
        private LinearLayout all;

        public UserHolder(View itemView) {
            super(itemView);
            patientPhoto = itemView.findViewById(R.id.image);
            patientName = itemView.findViewById(R.id.name);
            clinicName = itemView.findViewById(R.id.clinic);
            date = itemView.findViewById(R.id.date);
            all = itemView.findViewById(R.id.all);
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }

}
