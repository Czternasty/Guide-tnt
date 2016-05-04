package com.example.rmi.guide_tnt.activity;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.TimeFormatException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rmi.guide_tnt.model.Program;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Rémi on 27/04/2016.
 */
public class TonightAdapter extends RecyclerView.Adapter<TonightAdapter.ViewHolder> {

    protected List<Program> programs;

    public TonightAdapter(List<Program> programs) {
        this.programs = programs;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tonight_program_card_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Program program = programs.get(position);

        holder.title.setText(program.getTitle());
        String description = program.getDescription();
        if (description.length() > 200) {
            description = description.substring(0, 200) + "..."; // TODO : split the description to a space, or a coma/dot, it will be more beautiful
        }
        holder.description.setText(description);
        holder.programImage.setImageDrawable(program.getImageThumb());

        java.text.DateFormat timeInstance = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        timeInstance.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

        holder.startTime.setText(timeInstance.format(program.getStartDate()));
    }

    @Override
    public int getItemCount() {
        return programs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView startTime;
        public CardView cardView;
        public TextView description;
        public ImageView programImage;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.cv);
            title = (TextView) view.findViewById(R.id.programTitle);
            startTime = (TextView) view.findViewById(R.id.programHour);
            description = (TextView) view.findViewById(R.id.programDescription);
            programImage = (ImageView) view.findViewById(R.id.programImage);
        }

    }
}
