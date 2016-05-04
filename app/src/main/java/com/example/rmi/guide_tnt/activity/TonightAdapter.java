package com.example.rmi.guide_tnt.activity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rmi.guide_tnt.model.Channel;
import com.example.rmi.guide_tnt.model.Program;

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
            for (Program program : programs.get(channel)) {
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
            ProgramViewHolder programViewHolder = (ProgramViewHolder) holder;
            Program program = (Program) items.get(position);

            programViewHolder.title.setText(program.getTitle());
            String description = program.getDescription();
            if (description.length() > 150) {
                description = description.substring(0, 150) + "..."; // TODO : split the description to a space, or a coma/dot, it will be more beautiful
            }
            programViewHolder.description.setText(description);
            programViewHolder.programImage.setImageDrawable(program.getImageThumb());

            java.text.DateFormat timeInstance = new SimpleDateFormat("HH:mm", Locale.FRANCE);
            timeInstance.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

            programViewHolder.startTime.setText(timeInstance.format(program.getStartDate()));
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
        public TextView title;
        public TextView startTime;
        public CardView cardView;
        public TextView description;
        public ImageView programImage;

        public ProgramViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cv);
            title = (TextView) view.findViewById(R.id.programTitle);
            startTime = (TextView) view.findViewById(R.id.programHour);
            description = (TextView) view.findViewById(R.id.programDescription);
            programImage = (ImageView) view.findViewById(R.id.programImage);
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
