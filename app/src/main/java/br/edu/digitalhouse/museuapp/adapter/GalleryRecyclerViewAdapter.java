package br.edu.digitalhouse.museuapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.edu.digitalhouse.museuapp.Interfaces.ListClickListener;
import br.edu.digitalhouse.museuapp.R;
import br.edu.digitalhouse.museuapp.model.galleryrequest.Item;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.ViewHolder> {

    private List<Item> itemList;
    private ListClickListener listener;

    public GalleryRecyclerViewAdapter(List<Item> itemList, ListClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_item_item, viewGroup, false);

        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Item item = itemList.get(i);

        viewHolder.bind(item);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void update(List<Item> records) {
        List<Item> cleanList = new ArrayList<>();
        for (Item record : records) {
            if(record.getImages() != null && (!record.getImages().isEmpty())){
                cleanList.add(record);
            }
        }

        if (this.itemList.isEmpty()) {
            this.itemList = cleanList;
        } else {
            this.itemList.addAll(cleanList);
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageSample;
        private TextView textName;
        private ListClickListener listener;

        public ViewHolder(@NonNull View itemView, ListClickListener listener) {
            super(itemView);

            this.listener = listener;

            itemView.setOnClickListener(this);

            imageSample = itemView.findViewById(R.id.img_item_list_sample);
            textName = itemView.findViewById(R.id.txt_item_list_name);
        }

        public void bind(final Item item) {

            try {
                Picasso.get()
                        .load(item.getImages().get(0).getImageUrl() + "?height=200&width=200")
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(imageSample);
                textName.setText(item.getTitle());

            } catch (Exception e) {
                Log.d("IMAGE ARRAY", "EMPTY");
            }
        }


        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());

        }
    }
}
