package com.david.incubator.ui.user.usermodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.david.R;
import com.david.common.data.SelectedUser;

import java.util.List;

import javax.inject.Inject;

public class FileAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> itemList;
    private List<String> pathList;
    private int iconId;

    public FileAdapter(Context context, List<String> itemList, List<String> pathList, int iconId) {
        inflater = LayoutInflater.from(context);
        this.itemList = itemList;
        this.pathList = pathList;
        this.iconId = iconId;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.view_file_item, null);
        TextView textView = convertView.findViewById(R.id.text);
        textView.setText(itemList.get(position));
        ImageView icon = convertView.findViewById(R.id.icon);
        icon.setImageResource(iconId);
        convertView.setTag(pathList.get(position));
        return convertView;
    }
}