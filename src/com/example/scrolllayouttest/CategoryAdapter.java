package com.example.scrolllayouttest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CategoryAdapter extends BaseAdapter implements OnClickListener,
		OnLongClickListener {

	public final static int CREATE_FIND_INDEX_SCROLL1 = 0;
	public final static int CREATE_FIND_INDEX_SCROLL2 = 1;
	public final static int CREATE_TREASURE_CREATE_CARD_SCROLL = 2;
	private Activity activity;
	private LayoutInflater inflater;
	private Map<String, Object> categories;
	private List<String> categoriesName = new ArrayList<String>();
	private int categoryType = -1;
	private int category;
	private int textCategory;
	private int id = 0;

	private TextView delLogo;

	// private List<HandleCard> mCardList;

	public CategoryAdapter(Activity activity, Map<String, Object> categories,
			int categoryType, int category, int textCategory) {
		inflater = LayoutInflater.from(activity);
		this.activity = activity;
		this.categories = categories;
		this.categoryType = categoryType;
		this.category = category;
		this.textCategory = textCategory;

		for (Iterator<String> it = categories.keySet().iterator(); it.hasNext();) {
			categoriesName.add((String) it.next());
		}
	}
	
	public void setData(Map<String, Object> categories){
		this.categories = categories;
		this.categoriesName.clear();
		for (Iterator<String> it = categories.keySet().iterator(); it.hasNext();) {
			categoriesName.add((String) it.next());
		}
		
		this.notifyDataSetChanged();
	}

	public void setCategory(int category, int textCategory) {
		this.category = category;
		this.textCategory = textCategory;
	}

	public void addCategory(String categoryName, Integer categoryIconId) {
		categories.put(categoryName, categoryIconId);
		categoriesName.add(categoryName);
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (convertView == null) {
			view = inflater.inflate(this.category, null);
			// view.setTag(tag);
		}

		System.out.println("in get view!!!");
		
		switch (categoryType) {

		case 0:/* 创建发现首页第一个滑动列表 */
			view = inflater.inflate(R.layout.type_gridview_item, null);

			String catgName = this.categoriesName.get(position);

			((ImageView) view.findViewById(R.id.logo))
					.setImageResource((Integer) this.categories.get(catgName));

			((TextView) view.findViewById(R.id.content)).setText(catgName);

			view.setOnLongClickListener(this);

			delLogo = ((TextView) view.findViewById(R.id.delete_logo));
			delLogo.setTag(catgName);
			delLogo.setOnClickListener((MainActivity)this.activity);

			break;
		}

		return view;
	}

	public int getCount() {
		return categoriesName.size();
	}

	public Object getItem(int position) {
		return categoriesName.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public void onClick(View arg0) {
		

	}

	@Override
	public boolean onLongClick(View arg0) {
		
		System.out.println("in long click!");
		
		this.delLogo.setVisibility(View.VISIBLE);
		return false;
	}
}
