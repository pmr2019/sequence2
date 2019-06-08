package com.todolist.MyTouchHelper;

public interface ItemTouchHelperAdapter {
    void onItemDissmiss(int postion);
    void onItemMove(int fromPosition,int toPosition);
}
