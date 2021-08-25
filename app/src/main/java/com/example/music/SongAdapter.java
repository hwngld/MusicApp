package com.example.music;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> list;
    private OnItemClick onItemClick;

    public interface OnItemClick{
        void ItemClick(Song song, int position);
    }

    public SongAdapter(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent, false);
        return new SongViewHolder(view);
    }
    public void setData(List<Song> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
            Song song = list.get(position);
            holder.tvSinger.setText(song.getSinger());
            holder.tvName.setText(song.getName());
            holder.itemSong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.ItemClick(song, position);
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSong;
        TextView tvName, tvSinger;
        RelativeLayout itemSong;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong     = itemView.findViewById(R.id.imgSong);
            tvName      = itemView.findViewById(R.id.tv_SongName);
            tvSinger    = itemView.findViewById(R.id.tv_Singer);
            itemSong    = itemView.findViewById(R.id.itemSong);
        }
    }
}
