package com.example.scrolllayouttest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {

	private Activity context;
	private Map<String, Integer> categories;
	private List<String> categoriesName;
	private LayoutInflater inflater;

	public GridViewAdapter(Activity context, Map<String, Integer> categories) {
		this.context = context;
		this.categories = categories;
		this.categoriesName = new ArrayList<String>();
		inflater = LayoutInflater.from(context);

		Iterator<String> it = this.categories.keySet().iterator();
		while (it.hasNext()) {
			this.categoriesName.add(it.next());
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.categoriesName.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		View view = inflater.inflate(R.layout.type_gridview_item, null);

		String catgName = this.categoriesName.get(arg0);

		((ImageView) view.findViewById(R.id.logo))
				.setImageResource(this.categories.get(catgName));

		((TextView) view.findViewById(R.id.content)).setText(catgName);
		return view;
	}

}
