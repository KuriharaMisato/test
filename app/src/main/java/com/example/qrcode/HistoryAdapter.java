package com.example.qrcode;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {


    List<History> list;
    ITFOnClick itf;
    ViewBinderHelper binderHelper = new ViewBinderHelper();
    Context context;
    DAO dao;
    Handler handler;

    public HistoryAdapter(List<History> list, Context context, ITFOnClick itf) {
        this.list = list;
        this.itf = itf;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_rcv, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {

        History history = list.get(position);
        if (history == null){
            return;
        }

        holder.tv1.setText(history.strDate);
        holder.ed.setText(history.content);
        holder.ed.setTextColor(Color.parseColor(history.color));

        holder.btn_open.setOnClickListener(view -> {
            itf.onClick(history);
        });

        binderHelper.bind(holder.swipLayout, String.valueOf(history.id));
        holder.layout_del.setOnClickListener(view -> {
            if (holder.tv_del.getText().equals("xóa")){
                //Toast.makeText(context, "nhấn lại để xóa", Toast.LENGTH_SHORT).show();
                holder.tv_del.setText("xác nhận");
                YoYo.with(Techniques.FadeIn).duration(500).repeat(0).playOn(holder.tv_del);
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.tv_del.setText("xóa");
                    }
                }, 2000);
            }else if (holder.tv_del.getText().equals("xác nhận")){
                dao = new DAO(context);
                if (dao.delete(history.id) > 0){
                    list.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }else {
                    Toast.makeText(context, "không thể xóa, vui lòng thử lại!!!", Toast.LENGTH_SHORT).show();
                    holder.tv_del.setText("xóa");
                }
            }else {

            }

        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder{

        TextView tv1, btn_open, tv_del;
        EditText ed;
        LinearLayout layout_del;

        SwipeRevealLayout swipLayout;
        public HistoryViewHolder(@NonNull View v) {
            super(v);
            tv1 = v.findViewById(R.id.tv_item_1);
            tv_del = v.findViewById(R.id.tv_xoa);
            btn_open = v.findViewById(R.id.tv_open);
            ed = v.findViewById(R.id.ed_item);
            swipLayout = v.findViewById(R.id.swip_layout);
            layout_del = v.findViewById(R.id.layout_del);
        }
    }
}
