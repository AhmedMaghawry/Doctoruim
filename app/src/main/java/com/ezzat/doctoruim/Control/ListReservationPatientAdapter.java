package com.ezzat.doctoruim.Control;

import android.app.Activity;
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

public class ListReservationPatientAdapter extends RecyclerView.Adapter<ListReservationPatientAdapter.UserHolder> {

    private List<Reservation> reservations;
    private Activity context;

    public ListReservationPatientAdapter(List<Reservation> reservations, Activity context) {
        this.reservations = reservations;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserHolder holder, final int position) {
        holder.date.setText(reservations.get(position).getDate());
        Log.i("Dodo", reservations.get(position).getDoctorID());
        DatabaseController.getElement(USER_TABLE, reservations.get(position).getDoctorID(), User.class, new onEvent() {
            @Override
            public void onStart(Object object) {
                Utils.showLoading(context);
            }

            @Override
            public void onProgress(Object object) {

            }

            @Override
            public void onEnd(Object object) {
                Utils.hideDialog();
                User u = (User) object;
                Utils.setPhoto(context, u.getImage(), holder.doctorPhoto);
                holder.doctorName.setText(u.getName());
                holder.address.setText(u.getAddress());
                holder.phones.setText(u.getPhone());
            }
        });
        holder.all.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                pDialog.setTitleText("Reservation");
                pDialog.setContentText("Are you sure you want to cancel this reservation ?");
                pDialog.setConfirmText("Yes");
                pDialog.setCancelText("No");
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        pDialog.dismiss();
                    }
                });
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        reservations.get(position).deleteReservation();
                        reservations.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, reservations.size());
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
                .inflate(R.layout.list_item_reservation_p,parent, false);
        return new UserHolder(v);
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        private ImageView doctorPhoto;
        private TextView doctorName, address, phones, date;
        private LinearLayout all;

        public UserHolder(View itemView) {
            super(itemView);
            doctorPhoto = itemView.findViewById(R.id.image);
            doctorName = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            phones = itemView.findViewById(R.id.phone);
            date = itemView.findViewById(R.id.date);
            all = itemView.findViewById(R.id.all);
        }
    }

    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }

}
