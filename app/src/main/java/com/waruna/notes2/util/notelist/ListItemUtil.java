package com.waruna.notes2.util.notelist;

import com.waruna.notes2.data.db.entities.Note;
import com.waruna.notes2.data.db.entities.TempNote;
import com.waruna.notes2.ui.main.main.adapter.Item;

import java.util.ArrayList;
import java.util.List;

public class ListItemUtil {

    static public void generateFromNote(List<Item> items, List<Note> notes) {
        removeItems(items, Item.ITEM_NOTE_DEFAULT);
        for (Note note : notes) {
            items.add(new Item(
                            note,
                            null,
                            Item.ITEM_NOTE_DEFAULT,
                            0
                    )
            );
        }
    }

    static public void generateFromTempNote(List<Item> items, List<TempNote> notes) {
        removeItems(items, Item.ITEM_NOTE_TEMP);
        for (TempNote note : notes) {
            items.add(new Item(
                            null,
                            note,
                            Item.ITEM_NOTE_TEMP,
                            0
                    )
            );
        }
    }

    static public void generateTitles(List<Item> items) {
        // generate title item according to note items that are already inside list
    }

    static public TempNote getTempNote(int id, List<Item> items){
        for (Item item : items) {
            if (item.getTempNote() != null) {
                if (item.getTempNote().getId() == id)
                    return item.getTempNote();
            }
        }
        return null;
    }

    static public void removeItems(List<Item> items, int type) {
        List<Item> selectItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getNoteType() == type) {
                selectItems.add(item);
            }
        }
        for (Item item : selectItems) {
            items.remove(item);
        }
    }

}
