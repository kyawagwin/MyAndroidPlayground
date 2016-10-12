package com.passioncreativestudio.myandroidplayground.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.passioncreativestudio.myandroidplayground.R;
import com.passioncreativestudio.myandroidplayground.models.Album;

import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {
    private Context context;
    private List<Album> albumList;

    public AlbumsAdapter(Context context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(context)
                .inflate(R.layout.list_item_album, parent, false);

        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumViewHolder holder, int position) {
        Album album = albumList.get(position);

        holder.title.setText(album.getName());
        holder.numOfSongs.setText(album.getNumOfSongs() + " song(s)");
        Glide.with(context).load(album.getThumbnail()).into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.menu_album_list_item, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenuItemClickListener());
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        public TextView title, numOfSongs;
        public ImageView thumbnail, overflow;

        public AlbumViewHolder(View view) {
            super(view);

            title = (TextView) view.findViewById(R.id.list_item_album_title);
            numOfSongs = (TextView) view.findViewById(R.id.list_item_album_numOfSongs);
            thumbnail = (ImageView) view.findViewById(R.id.list_item_album_thumbnail);
            overflow = (ImageView) view.findViewById(R.id.list_item_album_overflow);
        }
    }

    private class PopupMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_album_list_item_addFavourite:
                    Toast.makeText(context, "Added to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_album_list_item_playNext:
                    Toast.makeText(context, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }

            return false;
        }
    }
}
