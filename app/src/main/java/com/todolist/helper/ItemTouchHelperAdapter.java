package com.todolist.helper;

public interface ItemTouchHelperAdapter {
    void onItemDissmiss(int postion);
    void onItemMove(int fromPosition,int toPosition);
}
