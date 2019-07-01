//package com.kelompok11.e_stugether.viewholder;
//
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//
//import com.kelompok11.e_stugether.R;
//import com.kelompok11.e_stugether.model.MateriModel;
//
//public class MateriViewHolder extends RecyclerView.ViewHolder {
//    public TextView tvJudulMateri,tvPengupload,tvLink;
//    public MateriViewHolder(@NonNull View itemView) {
//        super(itemView);
//        tvJudulMateri = itemView.findViewById(R.id.tvJudulMateri);
//        tvPengupload = itemView.findViewById(R.id.tvPengupload);
//        tvLink = itemView.findViewById(R.id.tvLink);
//    }
//    public void bindToMateri(MateriModel materiModel, View.OnClickListener onClickListener){
//        tvJudulMateri.setText(materiModel.judul);
//        tvPengupload.setText(materiModel.pengupload);
//        tvLink.setText(materiModel.link);
//    }
//}
