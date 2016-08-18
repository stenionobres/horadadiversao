package com.horadadiversao.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.horadadiversao.R;

import java.util.ArrayList;

/**
 * 
 * @author CASSIO SOUZA
 *
 */
public class CustomGridViewAdapter extends ArrayAdapter<Item> {
	Context context;
	int layoutResourceId;
	ArrayList<Item> data = new ArrayList<Item>();

	public CustomGridViewAdapter(Context context, int layoutResourceId,
			ArrayList<Item> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RecordHolder();
			holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}

		Item item = data.get(position);
			holder.imageItem.setImageBitmap(item.getImage());
		return row;

	}

	static class RecordHolder {
				ImageView imageItem;

	}
}