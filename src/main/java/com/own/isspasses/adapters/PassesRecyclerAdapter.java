package com.own.isspasses.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.own.isspasses.R;
import com.own.isspasses.models.PassDetailsModel;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PassesRecyclerAdapter extends RecyclerView.Adapter<PassesRecyclerAdapter.MyViewHolder> {

    Context mContext;

    public void setPassDetailsList(List<PassDetailsModel> passDetailsList) {
        this.passDetailsList = passDetailsList;
        notifyDataSetChanged();
    }

    List<PassDetailsModel> passDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtDuration, txtTimeStamp;

        public MyViewHolder(View view) {
            super(view);

            txtDuration = (TextView) view.findViewById(R.id.txtDuration);
            txtTimeStamp = (TextView) view.findViewById(R.id.txtTimeStamp);
        }
    }

    public PassesRecyclerAdapter(Context context, List<PassDetailsModel> passDetailsList) {
        this.mContext = context;
        this.passDetailsList = passDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.passes_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        PassDetailsModel passDetailsModel = passDetailsList.get(position);
        holder.txtDuration.setText("Duration : " + passDetailsModel.getDuration());
        Date date = new Date(passDetailsModel.getTimeStamp()*1000); // As per the API documentation multiplied with 1000. Reference: http://open-notify.org/Open-Notify-API/
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        holder.txtTimeStamp.setText("Rise time : " + dateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return passDetailsList == null ? 0 : passDetailsList.size();
    }
}
