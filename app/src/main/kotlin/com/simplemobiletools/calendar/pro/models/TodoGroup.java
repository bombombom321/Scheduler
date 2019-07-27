package com.simplemobiletools.calendar.pro.models;

import com.simplemobiletools.calendar.pro.activities.TodoActivity;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import java.util.List;

public class TodoGroup extends ExpandableGroup<Task> {

  private int iconResId;

  public TodoGroup(String title, List<Task> items, int iconResId) {
    super(title, items);
    this.iconResId = iconResId;
  }

  public int getIconResId() {
    return iconResId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof TodoGroup)) return false;

    TodoGroup todoGroup = (TodoGroup) o;

    return getIconResId() == todoGroup.getIconResId();

  }

  @Override
  public int hashCode() {
    return getIconResId();
  }
}

