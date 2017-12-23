package com.jajinba.pixabaydemo.utils;

import java.util.Collection;

public class ArrayUtils {

  public static int getLengthSafe(Collection<?> collection) {
    return collection == null ? 0 : collection.size();
  }

  public static boolean isEmpty(Collection<?> collection) {
    return getLengthSafe(collection) == 0;
  }

  public static boolean isNotEmpty(Collection<?> collection) {
    return getLengthSafe(collection) > 0;
  }
}
