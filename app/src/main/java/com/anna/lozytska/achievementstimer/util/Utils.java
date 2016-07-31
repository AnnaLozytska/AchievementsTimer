package com.anna.lozytska.achievementstimer.util;

import java.util.Collection;

/**
 * Created by alozytska on 31.07.16.
 */
public class Utils {

    public static final int getSize(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }

    public static final int getSize(Object[] array) {
        return array != null ? array.length : 0;
    }
}
