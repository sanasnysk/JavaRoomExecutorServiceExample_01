package com.sansan.javaroomexecutorserviceexample_01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private final Context context;
    private final List<User> userList;
    private LayoutInflater inflater;

    public ListAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        User user = userList.get(i);
        viewHolder.bindData(user);
        return view;

    }

    public void removeData(int index){
        userList.remove(index);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView label;

        public ViewHolder(View view) {
            label = view.findViewById(R.id.item_data);
        }

        void bindData(User user) {
            label.setText(user.getName() + " ---- " + user.getEmail() + " ---- " + user.getNumber());
        }
    }
}
