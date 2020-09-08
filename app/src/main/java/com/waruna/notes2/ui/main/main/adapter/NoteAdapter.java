package com.waruna.notes2.ui.main.main.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.waruna.notes2.R;

public class NoteAdapter extends ListAdapter<Item, NoteAdapter.ItemHolder> {
    private OnItemClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item item, @NonNull Item t1) {
            if (item.getNoteType() == Item.ITEM_NOTE_DEFAULT){
                return t1.getNote().getId() == item.getNote().getId() && t1.getNoteType() == item.getNoteType() && t1.getItemType() == item.getItemType();
            } else if (item.getNoteType() == Item.ITEM_NOTE_TEMP){
                return t1.getTempNote().getId() == item.getTempNote().getId() && t1.getNoteType() == item.getNoteType() && t1.getItemType() == item.getItemType();
            }
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item item, @NonNull Item t1) {
            if (item.getNoteType() == Item.ITEM_NOTE_DEFAULT){
                return t1.getNote().getTitle().equals(item.getNote().getTitle())
                        && t1.getNote().getDescription().equals(item.getNote().getDescription())
                        && t1.getNote().getPriority() == item.getNote().getPriority();
            } else if (item.getNoteType() == Item.ITEM_NOTE_TEMP){
                return t1.getTempNote().getNote().getTitle().equals(item.getTempNote().getNote().getTitle())
                        && t1.getTempNote().getNote().getDescription().equals(item.getTempNote().getNote().getDescription())
                        && t1.getTempNote().getNote().getPriority() == item.getTempNote().getNote().getPriority();
            }
            return false;
        }
    };

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_item, viewGroup, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int i) {
        Item currentNote = getItem(i);
        if (currentNote.getNoteType() == Item.ITEM_NOTE_DEFAULT){
            itemHolder.textViewTitle.setText(currentNote.getNote().getTitle());
            itemHolder.textViewDescription.setText(String.valueOf(currentNote.getNote().getId()));
        } else if (currentNote.getNoteType() == Item.ITEM_NOTE_TEMP){
            itemHolder.textViewTitle.setText(currentNote.getTempNote().getNote().getTitle());
            itemHolder.textViewDescription.setText(String.valueOf(currentNote.getTempNote().getId()));
        }
        itemHolder.textViewPriority.setText(String.valueOf(currentNote.getNoteType()));
        Log.e("Note : ", currentNote.toString());
    }

    public Item getItemAt(int position){
        return getItem(position);
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView textViewTitle, textViewDescription, textViewPriority;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener!=null && position!= RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Item item);
    }

    public void setOnItemClickListener (OnItemClickListener listener){
        this.listener = listener;
    }
}
