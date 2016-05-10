package com.example.rmi.guide_tnt.activity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rmi.guide_tnt.model.Channel;
import com.example.rmi.guide_tnt.model.Program;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Rémi on 27/04/2016.
 */
public class TonightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int CHANNEL_VIEW = 0;
    private static final int PROGRAM_VIEW = 1;

    List<Object> items = new ArrayList<>(); // either Program of Channel

    public TonightAdapter(Map<Channel, List<Program>> programs) {
        for (Channel channel : programs.keySet()) {
            items.add(channel);
            for (Program program : programs.get(channel).subList(0, Math.min(2, programs.get(channel).size()))) {
                items.add(program);
            }
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == PROGRAM_VIEW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tonight_program_card_layout, parent, false);
            viewHolder = new ProgramViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_section, parent, false);
            viewHolder = new ChannelViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ProgramViewHolder) {
            final ProgramViewHolder programViewHolder = (ProgramViewHolder) holder;
            final Program program = (Program) items.get(position);

            // Start date
            java.text.DateFormat timeInstance = new SimpleDateFormat("HH:mm", Locale.FRANCE);
            timeInstance.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

            programViewHolder.startTime.setText(timeInstance.format(program.getStartDate()));

            // Title
            programViewHolder.title.setText(program.getTitle());

            // Season (and episode info) if available
            StringBuffer seasonData = new StringBuffer();
            if (program.getSeason() != null && program.getSeason().length() > 0)
                seasonData.append("S" + program.getSeason());
            if (program.getEpisode() != null && program.getEpisode().length() > 0)
                seasonData.append("E" + program.getEpisode());

            if (seasonData.length() > 0)
                programViewHolder.season.setText(seasonData.toString());
            else
                programViewHolder.season.setVisibility(View.GONE);

            // display the program's category if available
            if (program.getCategory() != null && program.getCategory().length() > 0)
                programViewHolder.category.setText(program.getCategory());


//            // Rating
//            if (program.getRating() != null) {
//                for (int i = 0; i < program.getRating(); i++) {
//                    ImageView imageView = new ImageView(((ProgramViewHolder) holder).cardView.getContext());
//                    imageView.setImageResource(R.drawable.rate_heart);
//
//                    ((ProgramViewHolder) holder).programHeaderLayout.addView(imageView, 2 + i);
//                }
//            }


            // Description
            programViewHolder.descriptionShort.setText(program.getShortDescription());
            programViewHolder.descriptionFull.setText(program.getDescription());
            programViewHolder.descriptionFull.setVisibility(View.GONE);

            // Review
            if (program.getReview() != null && program.getReview().length() > 0)
                programViewHolder.review.setText(program.getReview());
            else {
                programViewHolder.review.setText(null);
                programViewHolder.reviewLayout.setVisibility(View.GONE);
            }


            // Image
            if (program.getImageURL() != null)
                Picasso.with(((ProgramViewHolder) holder).cardView.getContext())
                        .load(program.getImageURL())
                        .resize(200, 200)
                        .centerCrop()
                        .into(programViewHolder.programImage);


            programViewHolder.cardView.setClickable(true);
            programViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (programViewHolder.isCollapsed())
                        programViewHolder.displayExpanded();
                    else
                        programViewHolder.displayCollapsed();
                }
            });


        } else {
            ChannelViewHolder channelViewHolder = (ChannelViewHolder) holder;
            Channel channel = (Channel) items.get(position);
            channelViewHolder.channelName.setText(channel.getName());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Channel)
            return CHANNEL_VIEW;
        else {
            return PROGRAM_VIEW;
        }
    }

    public static class ProgramViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;

        public TextView title;
        public TextView startTime;
        public TextView season;
        public TextView category;
        public TextView descriptionShort;
        public TextView descriptionFull;
        public ImageView programImage;
        public RelativeLayout programHeaderLayout;
        public TextView review;
        public RelativeLayout reviewLayout;

        private boolean collapsed = true;

        public ProgramViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cv);
            title = (TextView) view.findViewById(R.id.programTitle);
            startTime = (TextView) view.findViewById(R.id.programHour);
            descriptionShort = (TextView) view.findViewById(R.id.programDescriptionShort);
            descriptionFull = (TextView) view.findViewById(R.id.programDescriptionFull);
            programImage = (ImageView) view.findViewById(R.id.programImage);
            programHeaderLayout = (RelativeLayout) view.findViewById(R.id.programHeaderLayout);
            season = (TextView) view.findViewById(R.id.season);
            category = (TextView) view.findViewById(R.id.category);
            reviewLayout = (RelativeLayout) view.findViewById(R.id.telerama_layout);
            review = (TextView) view.findViewById(R.id.telerama_text);

            programImage.setClickable(true);
            programImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    descriptionShort.setVisibility(View.INVISIBLE);
                }
            });
        }

        public void displayExpanded() {
            this.descriptionFull.setVisibility(View.VISIBLE);
            this.descriptionShort.setVisibility(View.GONE);
            if (this.review.getText() != null && this.review.getText().length() > 0)
                this.reviewLayout.setVisibility(View.VISIBLE);
            collapsed = false;
        }

        public void displayCollapsed() {
            this.descriptionFull.setVisibility(View.GONE);
            this.descriptionShort.setVisibility(View.VISIBLE);
            this.reviewLayout.setVisibility(View.GONE);
            collapsed = true;
        }

        public boolean isCollapsed() {
            return collapsed;
        }
    }

    public static class ChannelViewHolder extends RecyclerView.ViewHolder {
        public TextView channelName;

        public ChannelViewHolder(View view) {
            super(view);
            channelName = (TextView) view.findViewById(R.id.channelName);
        }
    }


}
