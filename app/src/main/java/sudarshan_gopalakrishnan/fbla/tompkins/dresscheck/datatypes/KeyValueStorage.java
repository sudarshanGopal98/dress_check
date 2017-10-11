package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Sudarshan on 1/30/2016.
 */
public class KeyValueStorage<K extends Serializable, V extends Serializable> implements Serializable{
    ArrayList<K> keys = new ArrayList<>();
    ArrayList<V> values = new ArrayList<>();

    public void add(K key, V value){
        keys.add(key);
        values.add(value);
    }

    public V remove(int position){
        keys.remove(position);
        V value = values.remove(position);
        return value;
    }

    public K getKeyAt(int position){
        return keys.get(position);
    }

    public V getValueAt(int position){
        return values.get(position);
    }

    public int size(){
        return keys.size();
    }

    public String createStringStorageForParse(){
        String toReturn = "[";
        for(int pos = 0; pos < keys.size(); pos++){
            toReturn += "["+keys.get(pos)+","+values.get(pos)+"]";

            if(!(pos == keys.size()-1))
                toReturn += ";";
        }
        toReturn+= "]";
        return toReturn;
    }

    public String toString(){
        return values.toString();
    }

}
