package com.example.study.studyingenglish;

//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<MyData> mDataList;
    private OnRecyclerListener mListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView scoreTextView;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnRecyclerItemClicked(view, getAdapterPosition());
                }
            });

            titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            scoreTextView = (TextView) itemView.findViewById(R.id.score_text_view);
        }

        TextView getTitleTextView() {
            return titleTextView;
        }

        TextView getScoreTextView() {
            return scoreTextView;
        }
    }

    CustomAdapter(ArrayList<MyData> dataSet, OnRecyclerListener listener) {
        mDataList = dataSet;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyData myData = mDataList.get(position);
        holder.getTitleTextView().setText(myData.getTitle());
        holder.getScoreTextView().setText(String.format("%d / %d",
                myData.getCorrectNum(), myData.getQuesNum()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
