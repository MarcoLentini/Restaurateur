package com.example.restaurateur.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.restaurateur.R;

import java.util.ArrayList;

public class CommentsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<CommentModel> commentsDate;
    private LayoutInflater mInflater;

    public CommentsListAdapter(Context context, ArrayList<CommentModel> commentsDate) {
        this.context = context;
        this.commentsDate = commentsDate;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.cardview_comments, viewGroup,false);
        CommentsViewHolder holder = new CommentsViewHolder(view);
        return holder;
     }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        CommentsViewHolder commentsViewHolder = (CommentsViewHolder) viewHolder;
        TextView textViewCustID = commentsViewHolder.textViewCustID;
        RatingBar ratingBarFoodQuality = commentsViewHolder.ratingBarFoodQuality;
        TextView textViewNotes = commentsViewHolder.textViewNotes;

        CommentModel tmpComments = commentsDate.get(position);
        textViewCustID.setText(tmpComments.getUserId());
        ratingBarFoodQuality.isIndicator();
        ratingBarFoodQuality.setRating(tmpComments.getVoteForRestaurant());
        textViewNotes.setText(tmpComments.getNotes());

    }

    @Override
    public int getItemCount() {
        return commentsDate.size();
    }

    private class CommentsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCustID;
        RatingBar ratingBarFoodQuality;
        TextView textViewNotes;

        public CommentsViewHolder(View view) {
            super(view);
            this.textViewCustID = view.findViewById(R.id.myCommentsCustID);
            this.ratingBarFoodQuality = view.findViewById(R.id.showRatingFoodQuality);
            this.textViewNotes = view.findViewById(R.id.myCommentsCustNotes);
        }
    }
}