package com.androidfizz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidfizz.androidcurdapplication.R;
import com.androidfizz.model.ModelPerson;

import java.util.List;

/**
 * Created by Aklesh on 1/2/2018.
 */

public class AdapterPerson extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_DELETE = 1;
    public static final int TYPE_UPDATE = 2;
    private Context context;
    private List<ModelPerson> mPersonList;

    public AdapterPerson(Context context, List<ModelPerson> mPersonList) {
        this.context = context;
        this.mPersonList = mPersonList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.single_person_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ModelPerson single = mPersonList.get(position);
        MyViewHolder mHolder = (MyViewHolder) holder;

        mHolder.tvName.setText(single.getName());
        mHolder.tvEmail.setText(single.getEmail());
        mHolder.tvAge.setText(single.getAge());
        mHolder.tvDelete.setOnClickListener(new OnCustomCLick(position, TYPE_DELETE));
        mHolder.tvUpdate.setOnClickListener(new OnCustomCLick(position,TYPE_UPDATE));
        mHolder.itemView.setOnLongClickListener(new OnCustomLongClick(position,TYPE_UPDATE));
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvEmail, tvAge, tvDelete,tvUpdate;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvDelete = itemView.findViewById(R.id.tvDelete);
            tvUpdate = itemView.findViewById(R.id.tvUpdate);
        }
    }

    private class OnCustomCLick implements View.OnClickListener {
        private int position, type;

        public OnCustomCLick(int position, int type) {
            this.position = position;
            this.type = type;
        }

        @Override
        public void onClick(View view) {
            if (mListner != null)
                mListner.onClick(position, type);
        }
    }


    //LISTNERS FOR CLICK EVENT (DELETE)
    private OnClickListner mListner;

    public interface OnClickListner {
        void onClick(int position, int type);
    }

    public void setOnCustomClickListner(OnClickListner mListner) {
        this.mListner = mListner;
    }

    private class OnCustomLongClick implements View.OnLongClickListener {
        private int position, type;

        public OnCustomLongClick(int position, int type) {
            this.position = position;
            this.type = type;
        }

        @Override
        public boolean onLongClick(View view) {
            if (mListner != null)
                mListner.onClick(position, type);
            return true;
        }
    }
}
