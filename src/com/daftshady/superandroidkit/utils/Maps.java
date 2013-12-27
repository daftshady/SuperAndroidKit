package com.daftshady.superandroidkit.utils;

import java.util.HashMap;


/**
 * Provides static methods for creating mutable instances easily.
 * @author parkilsu
 *
 */
public class Maps {
    /**
     * Creates a HashMap instance.
     *
     * @return a newly-created, initially-empty HashMap
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }
    
    /**
     * Create hashMap a little bit more smart way.
     * data param should be k,v,k,v... pairs.
     * @param data
     * @return HashMap initialized by data
     */
    public static HashMap<String, String> build(String... data){
        if (data.length % 2 != 0) 
            throw new IllegalArgumentException("Odd number of arguments");
        
        HashMap<String, String> result = new HashMap<String, String>();
        String key = null;
        int step = -1;
        for(String value : data){
            step++;
            switch(step % 2){
            case 0: 
                if(value == null)
                    throw new IllegalArgumentException("Null key value"); 
                key = value;
                continue;
            case 1:             
                result.put(key, value);
                break;
            }
        }
        return result;
    }
}
