package com.example.robotklepa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<Content> contents;

    ContentAdapter(Context context, List<Content> contents) {
        this.contents = contents;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentAdapter.ViewHolder holder, int position) {
        Content content = contents.get(position);
        holder.nameView.setText(content.getName());
        holder.urlView.setText(content.getUrl());
        holder.number.setText(content.getNumber());
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, urlView, number;
        ViewHolder(View view) {
            super(view);
            nameView = view.findViewById(R.id.name);
            urlView = view.findViewById(R.id.URL);
            number = view.findViewById(R.id.number);
        }
    }
}
