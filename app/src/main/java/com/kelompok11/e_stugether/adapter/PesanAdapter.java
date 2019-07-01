package com.kelompok11.e_stugether.adapter;

import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kelompok11.e_stugether.PelajaranActivity;
import com.kelompok11.e_stugether.R;
import com.kelompok11.e_stugether.fragment.ReplyFragment;
import com.kelompok11.e_stugether.model.PesanModel;
import com.kelompok11.e_stugether.model.UsersModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PesanAdapter extends RecyclerView.Adapter<PesanAdapter.PesanViewHolder> {
    private ArrayList<PesanModel> pesanModels;
    private String getUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String pelajaranKey;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public PesanAdapter(ArrayList<PesanModel> pesanModels,String pelajaranKey) {
        this.pesanModels = pesanModels;
        this.pelajaranKey = pelajaranKey;
    }

    @NonNull
    @Override
    public PesanViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        if(i == 1){
            View rootView = layoutInflater.inflate(R.layout.item_pesan_right,viewGroup,false);
            rootView.findViewById(R.id.cvPesan).setBackgroundResource(R.drawable.cv_itempesanright);
            return new PesanViewHolder(rootView);
        }else{
            View rootView = layoutInflater.inflate(R.layout.item_pesan,viewGroup,false);
            rootView.findViewById(R.id.cvPesan).setBackgroundResource(R.drawable.cv_itempesan);
            return new PesanViewHolder(rootView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final PesanViewHolder pesanViewHolder, final int i) {
            pesanViewHolder.btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((PelajaranActivity)pesanViewHolder.itemView.getContext()).idReply = pesanModels.get(i).getIdPesan();
                    final ReplyFragment replyFragment = new ReplyFragment();
                    databaseReference.child("Users").child(pesanModels.get(i).getIdPengirim()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                        replyFragment.setNamaPengirim(usersModel.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                    replyFragment.setPembahasan(pesanModels.get(i).getPesan());
                    ((PelajaranActivity)pesanViewHolder.itemView.getContext()).loadFragment(replyFragment);
                }
            });
        pesanViewHolder.tvPesan.setText(pesanModels.get(i).getPesan());
        // milliseconds
        long waktuKirim = pesanModels.get(i).getWaktuKirim();
        DateFormat simple = new SimpleDateFormat("HH.mm");
        Date result = new Date(waktuKirim);
//        System.out.println(simple.format(result));
        pesanViewHolder.tvWaktuKirim.setText(simple.format(result));
        RelativeLayout.MarginLayoutParams layoutParams = (RelativeLayout.MarginLayoutParams) pesanViewHolder.relativeLayout.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin, -13, layoutParams.rightMargin, layoutParams.bottomMargin);
        pesanViewHolder.relativeLayout.setLayoutParams(layoutParams);
        if(i == 0){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pesanViewHolder.tvPesan.getLayoutParams();
            params.setMargins(params.leftMargin, 20, params.rightMargin, params.bottomMargin);
            pesanViewHolder.tvPesan.setLayoutParams(params);
            databaseReference.child("Users").child(pesanModels.get(i).getIdPengirim()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                    pesanViewHolder.tvNamaPengirim.setText(usersModel.getUsername());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            if (pesanModels.get(i).getIdPengirim().equals(pesanModels.get(i - 1).getIdPengirim())|| pesanModels.get(i).getIdPengirim().equals(getUid)) {
                pesanViewHolder.tvNamaPengirim.setText(null);
                layoutParams.setMargins(layoutParams.leftMargin, -25, layoutParams.rightMargin, layoutParams.bottomMargin);
                pesanViewHolder.relativeLayout.setLayoutParams(layoutParams);
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pesanViewHolder.tvPesan.getLayoutParams();
//                params.setMargins(2, 2, 2, 2);
//                pesanViewHolder.tvPesan.setLayoutParams(params);

            } else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pesanViewHolder.tvPesan.getLayoutParams();
                params.setMargins(params.leftMargin, 20, params.rightMargin, params.bottomMargin);
                pesanViewHolder.tvPesan.setLayoutParams(params);
                databaseReference.child("Users").child(pesanModels.get(i).getIdPengirim()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UsersModel usersModel = dataSnapshot.getValue(UsersModel.class);
                        pesanViewHolder.tvNamaPengirim.setText(usersModel.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return (pesanModels != null)? pesanModels.size():0;
    }
    public class PesanViewHolder extends RecyclerView.ViewHolder{
        private TextView tvPesan;
        private TextView tvNamaPengirim;
        private Button btnReply;
        private CardView cardView;
        private RelativeLayout relativeLayout;
        private TextView tvWaktuKirim;

        public PesanViewHolder(@NonNull View itemView) {
            super(itemView);
            btnReply = itemView.findViewById(R.id.btnReply);
            relativeLayout = itemView.findViewById(R.id.relativeLayoutPesan);
            cardView = itemView.findViewById(R.id.cvPesan);
            tvNamaPengirim = itemView.findViewById(R.id.tvNamaPengirim);
            tvPesan = itemView.findViewById(R.id.tvPesan);
            tvWaktuKirim = itemView.findViewById(R.id.tvWaktuKirim);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(pesanModels.get(position).getIdPengirim().equals(getUid)){
            return 1;
        }
        else{
            return 0;
        }
    }
}
