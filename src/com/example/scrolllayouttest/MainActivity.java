package com.example.scrolllayouttest;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

public class MainActivity extends Activity implements OnClickListener {

	private ScrollLayout mScroll;
	private LayoutInflater inflater;
	private GridViewAdapter adt1;
	private GridViewAdapter adt2;
	Map<String, Object> categories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

		this.inflater = LayoutInflater.from(this);
		this.mScroll = (ScrollLayout) this.findViewById(R.id.scroll_test);

		initViewTest();
	}

	private void initViewTest() {
		categories = new LinkedHashMap<String, Object>();
		categories.put("居家必备", R.drawable.home_electrical);
		categories.put("宠物服务", R.drawable.home_electrical);
		categories.put("百货超市", R.drawable.home_electrical);
		categories.put("健康生活", R.drawable.home_electrical);

		categories.put("教育培训", R.drawable.home_electrical);
		categories.put("舌尖美食", R.drawable.home_electrical);
		categories.put("娱乐", R.drawable.home_electrical);
		categories.put("优惠券", R.drawable.home_electrical);
		categories.put("彩票", R.drawable.home_electrical);
		categories.put("外卖", R.drawable.home_electrical);

		mScroll.setAttributes(3, 6, CategoryAdapter.CREATE_FIND_INDEX_SCROLL1);
		mScroll.setCategory(R.layout.type_gridview_item, R.id.content);
		mScroll.setCategories(categories);
		mScroll.initScreen(this, this.inflater);
		// // add footer points
		// points.initPoints(this, alltype.getScreenCount(),
		// alltype.getCurScreen());
		//
		// // set interface
		// alltype.setChacgePoint(points);
	}

	// private void initView() {
	//
	// Map<String, Integer> categories1 = new LinkedHashMap<String, Integer>();
	// categories1.put("居家必备", R.drawable.home_electrical);
	// categories1.put("宠物服务", R.drawable.home_electrical);
	// categories1.put("百货超市", R.drawable.home_electrical);
	// categories1.put("健康生活", R.drawable.home_electrical);
	//
	// categories1.put("教育培训", R.drawable.home_electrical);
	// categories1.put("舌尖美食", R.drawable.home_electrical);
	// // categories.put("娱乐", R.drawable.home_electrical);
	// // categories.put("优惠券", R.drawable.home_electrical);
	// // categories.put("彩票", R.drawable.home_electrical);
	// // categories.put("外卖", R.drawable.home_electrical);
	// Map<String, Integer> categories2 = new LinkedHashMap<String, Integer>();
	// categories2.put("居家必备", R.drawable.home_electrical);
	// categories2.put("居家必备", R.drawable.home_electrical);
	// categories2.put("居家必备", R.drawable.home_electrical);
	// categories2.put("居家必备", R.drawable.home_electrical);
	//
	// categories2.put("居家必备", R.drawable.home_electrical);
	// categories2.put("居家必备", R.drawable.home_electrical);
	// this.adt1 = new GridViewAdapter(this, categories1);
	// this.adt2 = new GridViewAdapter(this, categories2);
	//
	// GridView child1 = (GridView) this.inflater.inflate(
	// R.layout.single_screen, null);
	// child1.setAdapter(adt1);
	//
	// this.mScroll.addView(child1);
	//
	// GridView child2 = (GridView) this.inflater.inflate(
	// R.layout.single_screen, null);
	// child2.setAdapter(adt2);
	// this.mScroll.addView(child2);
	// // GridView child3 = (GridView)
	// // this.inflater.inflate(R.layout.single_screen, null);
	// // this.mScroll.addView(child1);
	// }

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.delete_logo:
			String name = (String) arg0.getTag();

			System.out.println("delete business lgogo  name:" + name);

			this.categories.remove(name);

			mScroll.removeAllViews();
			mScroll.setAttributes(3, 6,
					CategoryAdapter.CREATE_FIND_INDEX_SCROLL1);
			mScroll.setCategory(R.layout.type_gridview_item, R.id.content);
			mScroll.setCategories(categories);
			mScroll.initScreen(this, this.inflater);
			MotionEvent evnt3 = MotionEvent.obtain(0, 1,
					MotionEvent.ACTION_MOVE, 100, 100, 0);
			Map<Integer, GridView> childs = mScroll.getmGridViewMap();
			Iterator<Integer> it = childs.keySet().iterator();
			while(it.hasNext()){
				childs.get(it.next()).dispatchTouchEvent(evnt3);
			}
			mScroll.snapToScreen(mScroll.getmCurScreen());
			
			break;
		}
	}

}
