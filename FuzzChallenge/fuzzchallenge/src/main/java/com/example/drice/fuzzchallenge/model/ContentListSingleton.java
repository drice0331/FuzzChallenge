package com.example.drice.fuzzchallenge.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Singleton for Content to populate listviews with
 * Created by DrIce on 4/29/15.
 */
public class ContentListSingleton {
    private static final String TAG = "UserPhotoList";

    /**
     * Map provides easy access when list with one type is needed
     */
    private HashMap<String, ArrayList<ListviewContent>> map;

    private ArrayList<ListviewContent> items;

    /**
     * Using HashSet for efficiency when checking for duplicates - a contains operation with this is O(1) time as opposed to an
     * ArrayList contains which is O(n)
     */
    private HashSet<ListviewContent> itemHashSet;

    private static ContentListSingleton sPhotoList;

    private ContentListSingleton() {
        items = new ArrayList<>();
        map = new HashMap<>();
        itemHashSet = new HashSet<>();
    }

    public static ContentListSingleton get() {
        if(sPhotoList == null) {
            sPhotoList = new ContentListSingleton();
        }
        return sPhotoList;
    }

    /**
     * Add one item to structures of this singleton if it does not already contain it, including
     * hashmap which places item in there using it's "type" field as the key
     * @param item
     * @param type
     */
    public void addListviewItem(ListviewContent item, String type) {
        if(!itemHashSet.contains(item)) {
            itemHashSet.add(item);
            items.add(item);
            if(map.containsKey(type)) {
                map.get(type).add(item);
            } else {
                ArrayList<ListviewContent> newList = new ArrayList<>();
                newList.add(item);
                map.put(type, newList);
            }
        }
    }

    /**
     * add entire collection to structures of this singleton
     * @param itemList
     */
    public void addListviewItemList(Collection<ListviewContent> itemList) {
        for(ListviewContent item : itemList) {
            addListviewItem(item, item.getType());
        }
    }

    public void deleteListviewItem(ListviewContent item) {
        items.remove(item);
    }

    public ArrayList<ListviewContent> getCompleteList() {
        return items;
    }

    public ArrayList<ListviewContent> getListForType(String type) {
        ArrayList<ListviewContent> listForType = new ArrayList<>();
        for(ListviewContent item : items) {
            if(item.getType().equals(type)) {
                listForType.add(item);
            }
        }
        return listForType;
    }

    /**
     * retrieve list of content for one given type from hashmap - used in non-all tabs/fragments
     * @param type
     * @return
     */
    public ArrayList<ListviewContent> getListFromMap(String type) {
        if(map.containsKey(type)) {
            return map.get(type);
        }
        return null;
    }
}