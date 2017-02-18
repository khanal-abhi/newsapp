package abhinash.io.newsfeed.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by khanal on 2/18/17.
 * This is a serializable array list.
 */

public class SerializableArrayList<T extends Serializable> implements Serializable {

    /**
     * The arraylist of serializable
     */
    private ArrayList<T> arrayList;

    public ArrayList<T> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<T> arrayList) {
        this.arrayList = arrayList;
    }
}
