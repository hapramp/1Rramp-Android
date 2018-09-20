package com.hapramp.utils;

import java.util.ArrayList;

public class BackstackManager {
  public static ArrayList<Integer> stack = new ArrayList<>();

  public static void pushItem(int id) {
    if (stack.contains(id)) {
      while (true) {
        if (stack.get(0) != id) {
          stack.remove(0);
        } else {
          break;
        }
      }
    } else {
      stack.add(0, id);
    }
  }

  public static void popItem() {
    if (stack.size() > 0) {
      stack.remove(0);
    }
  }

  public static int getTop() {
    if (stack.size() > 0) {
      return stack.get(0);
    }
    return -1;
  }
}
