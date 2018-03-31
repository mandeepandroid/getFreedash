package com.getfreedash.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getfreedash.DashBoardActivity;
import com.getfreedash.POJO.POJOGetReferredUser;
import com.getfreedash.R;

import java.util.ArrayList;

/**
 * Created by Bytenome-01 on 3/16/2018.
 */

public class GetReferredUserAdapter extends RecyclerView.Adapter<GetReferredUserAdapter.Holder>{
    ArrayList<POJOGetReferredUser> arrayList;
    Context context;
    LayoutInflater inflater;

    public GetReferredUserAdapter(Context context, ArrayList<POJOGetReferredUser> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
        inflater = inflater.from(context);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View viewONE = inflater.inflate(R.layout.design_get_referred_user, parent, false);
GetReferredUserAdapter.Holder mholder=new GetReferredUserAdapter.Holder(viewONE);
        return mholder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        POJOGetReferredUser pojoGetReferredUser=arrayList.get(position);
        holder.tv_name.setText(pojoGetReferredUser.getName());
        holder.tv_date.setText(pojoGetReferredUser.getDate());
        holder.tv_bouns_given.setText(pojoGetReferredUser.getBonus_given());
        holder.tv_time.setText(pojoGetReferredUser.getTime());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_date,tv_bouns_given,tv_time;
        public Holder(View itemView) {
            super(itemView);
            tv_bouns_given=itemView.findViewById(R.id.tv_bouns);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_time=itemView.findViewById(R.id.tv_time);

        }
    }
}
