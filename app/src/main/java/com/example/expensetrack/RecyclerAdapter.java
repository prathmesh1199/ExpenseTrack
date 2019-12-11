package com.example.expensetrack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {




        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item , parent , false);
        ViewHolder  viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.billno.setText(String.valueOf(position));

    }

    @Override
    // Returns number of rows in recycler view.
    public int getItemCount() {
        return 20;
    }


    // Managing the rows of Recycler View.
    // Keep track of view in rows.
    // All text view in individual rows.
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView billno , amount , date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            billno = itemView.findViewById(R.id.tv_head);
            date = itemView.findViewById(R.id.tv_category);
            amount = itemView.findViewById(R.id.bill_no);
        }
    }

}
