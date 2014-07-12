package com.example.scrolllayouttest;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup {

	private static final String TAG = "ScrollLayout";
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	private int mCurScreen;
	private int mDefaultScreen = 0;

	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;

	private static final int SNAP_VELOCITY = 600;

	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private Map<String, Object> categories;

	private int mNumColumn = 1;
	private int mCategoryCountPerScreen = 1;
	private int mCategoryType = 1;
	private int mCategory = R.layout.type_gridview_item;
	private int mTextCategory = R.id.content;

	private Context context = null;
	private Map<Integer, OnClickListener> map = null;

	private Map<Integer, GridView> mGridViewMap;

//	private GridView child1;
//	private GridView child2;

	private CategoryAdapter adt1;
	private CategoryAdapter adt2;

	public int getmCurScreen() {
		return mCurScreen;
	}

	public void setmCurScreen(int mCurScreen) {
		this.mCurScreen = mCurScreen;
	}

	public Map<Integer, GridView> getmGridViewMap() {
		return mGridViewMap;
	}

	public void setmGridViewMap(Map<Integer, GridView> mGridViewMap) {
		this.mGridViewMap = mGridViewMap;
	}

	public void setOnClickListenerMap(Map<Integer, OnClickListener> attrs) {
		this.map = attrs;
	}

	public ScrollLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		this.context = context;

	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mScroller = new Scroller(context);
		// Ĭ����ʾ��һ��
		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();

			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft + childWidth,
							childView.getMeasuredHeight());
					childLeft += childWidth;
					// ������ 0 - 320 | 320 - 640 | 640 - 960
					// ...��������Ļ��320��
				}
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Log.e(TAG, "onMeasure");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// if (widthMode != MeasureSpec.EXACTLY) {
		// throw new IllegalStateException(
		// "ScrollLayout only canmCurScreen run at EXACTLY mode!");
		// }

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		// if (heightMode != MeasureSpec.EXACTLY) {
		// throw new IllegalStateException(
		// "ScrollLayout only can run at EXACTLY mode!");
		// }

		// The children are given the same width and height as the scrollLayout
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		// Log.e(TAG, "moving to screen "+mCurScreen);
		// x��� y���
		// �ƶ����ڼ���
		scrollTo(mCurScreen * width, 0);
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page. �жϻ�����λ�� �����ڵ�ǰ���м��λ�� ���� ���� ��Ȼ������� getScrollX
	 * x�����ƫ����
	 */

	public void snapToDestination() {
		final int screenWidth = getWidth();
		// �ж�
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen) {
		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {

			final int delta = whichScreen * getWidth() - getScrollX();
			// ��ʼ����
			// x��y��x�����ƶ�����y�����ƶ�������������ʱ�䣬���������
			mScroller.startScroll(getScrollX(), 0, delta, 0,
					Math.abs(delta) * 2);
			mCurScreen = whichScreen;
			invalidate(); // Redraw the layout

			// changePoint.changeSelectedPoint(mCurScreen);
		}
	}

	public void setToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		mCurScreen = whichScreen;
		scrollTo(whichScreen * getWidth(), 0);
	}

	public int getCurScreen() {
		return mCurScreen;
	}

	public int getScreenCount() {
		return getChildCount();
	}

	// ֻ�е�ǰLAYOUT�е�ĳ��CHILD����SCROLL����������Ż���ʹ�Լ���COMPUTESCROLL������
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		Log.e(TAG, "computeScroll");
		// ����true����ʾ������û�н���
		// ��Ϊǰ��startScroll������ֻ����startScroll���ʱ �Ż�Ϊfalse

		if (mScroller.computeScrollOffset()) {
			Log.e(TAG, mScroller.getCurrX() + "======" + mScroller.getCurrY());
			// �����˶���Ч�� ÿ�ι���һ��
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onTouchEvent start");
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.e(TAG, "event down! " + mScroller.isFinished());
			// �����Ļ�Ĺ�������û�н��� ��Ͱ����� �ͽ���
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			break;

		case MotionEvent.ACTION_MOVE:
			Log.e(TAG, "event move!");
			// ȡ��ƫ����
			int deltaX = (int) (mLastMotionX - x);
			Log.e(TAG, "x: " + x + "detaX: " + deltaX);
			mLastMotionX = x;
			// x�����ƫ���� y�����ƫ����
			scrollBy(deltaX, 0);

		case MotionEvent.ACTION_UP:
			Log.e(TAG, "event : up");
			// if (mTouchState == TOUCH_STATE_SCROLLING) {
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(500);
			int velocityX = (int) velocityTracker.getXVelocity();
			Log.e(TAG, "velocityX:" + velocityX);

			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
				Log.e(TAG, "snap left");
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				// Fling enough to move right
				Log.e(TAG, "snap right");
				snapToScreen(mCurScreen + 1);
			} else {
				// һ�㶼�������
				snapToDestination();

			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			// }
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	// ����о�ûʲô���� ����true����false ���ǻ�ִ��onTouchEvent��
	// ��Ϊ��view����onTouchEvent����false��
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);

		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			Log.e(TAG, "onInterceptTouchEvent move");
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			if (xDiff > mTouchSlop) {
				mTouchState = TOUCH_STATE_SCROLLING;

			}
			break;

		case MotionEvent.ACTION_DOWN:
			Log.e(TAG, "onInterceptTouchEvent down");
			mLastMotionX = x;
			mLastMotionY = y;
			Log.e(TAG, mScroller.isFinished() + "");
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			Log.e(TAG, "onInterceptTouchEvent up or cancel");
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		Log.e(TAG, mTouchState + "====" + TOUCH_STATE_REST);
		return mTouchState != TOUCH_STATE_REST;
	}

	/**
	 * @param chacgePoint
	 *            the chacgePoint to set
	 */
	// public void setChacgePoint(SelectPoint changePoint) {
	// this.changePoint = changePoint;
	// }

	public void setCategories(Map<String, Object> categories) {
		this.categories = categories;
	}

	public void initScreen(Activity activity, LayoutInflater inflater) {
		if (categories == null) {
			return;
		}

		if (null == mGridViewMap) {
			mGridViewMap = new Hashtable<Integer, GridView>();
		}

		Set<Entry<String, Object>> entrySet = categories.entrySet();
		int i = 0;
		int totalScreenNum = categories.size() / mCategoryCountPerScreen;
		int currentScreen = 0;
		int screen = 0;
		Map<String, Object> oneScreenCategories = null;

		for (Iterator<Entry<String, Object>> it = entrySet.iterator(); it
				.hasNext();) {

			// 9 category icons in one screen
			if (i % mCategoryCountPerScreen == 0) {
				if (oneScreenCategories != null) {
					if (mCategoryCountPerScreen != 1) {

						GridView child = this.mGridViewMap.get(screen);
						if (child == null) {
							child = (GridView) inflater.inflate(
									R.layout.single_screen, null);
							this.mGridViewMap.put(screen, child);
						}

						// if (child1 == null) {
						// child1 = (GridView) inflater.inflate(
						// R.layout.single_screen, null);
						//
						// }
						child.setNumColumns(mNumColumn);
						if (null == adt1) {
							adt1 = new CategoryAdapter(activity,
									oneScreenCategories, mCategoryType,
									this.mCategory, this.mTextCategory);
						} else {
							adt1.setData(oneScreenCategories);
						}

						child.setAdapter(adt1);
						addView(child, LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT);
						screen++;
					}
				}
				currentScreen++;
				oneScreenCategories = new LinkedHashMap<String, Object>();
			}

			// create map for last category screen
			if (currentScreen > totalScreenNum + 1) {
				oneScreenCategories = new LinkedHashMap<String, Object>();
			}

			// put every 9 category objects into one map
			Map.Entry<String, Object> entry = (Entry<String, Object>) it.next();
			oneScreenCategories.put(entry.getKey(), entry.getValue());

			// add last category screen
			if (i == categories.size() - 1) {
				if (mCategoryCountPerScreen != 1) {

					GridView child = this.mGridViewMap.get(screen);
					if (child == null) {

						child = (GridView) inflater.inflate(
								R.layout.single_screen, null);
						this.mGridViewMap.put(screen, child);
					}
					// child.postInvalidate();
					// if (null == child2) {
					// child2 = (GridView) inflater.inflate(
					// R.layout.single_screen, null);
					//
					// }
					child.setNumColumns(mNumColumn);
					if (null == adt2) {
						adt2 = new CategoryAdapter(activity,
								oneScreenCategories, mCategoryType,
								this.mCategory, this.mTextCategory);
					} else {
						adt2.setData(oneScreenCategories);
					}
					child.setAdapter(adt2);
					addView(child, LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
				}
			}

			i++;
		}
	}

	public void addCategory(Activity activity, LayoutInflater inflater,
			String categoryName, Integer categoryIconId) {

		categories.put(categoryName, categoryIconId);

		int count = getChildCount();
		CategoryAdapter adapter = (CategoryAdapter) ((GridView) getChildAt(count - 1))
				.getAdapter();
		int categoryCount = adapter.getCount();

		if (categoryCount < mCategoryCountPerScreen) {
			// add to last screen
			adapter.addCategory(categoryName, categoryIconId);
			adapter.notifyDataSetChanged();
		} else {
			// create a new screen
			Map<String, Object> categories = new LinkedHashMap<String, Object>();
			categories.put(categoryName, categoryIconId);
			GridView child = (GridView) inflater.inflate(
					R.layout.single_screen, null);

			child.setNumColumns(mNumColumn);
			child.setAdapter(new CategoryAdapter(activity, categories,
					mCategoryType, this.mCategory, this.mTextCategory));
			child.setBackgroundResource(R.drawable.ic_launcher);
			addView(child);
		}

		bringToFront();
	}

	public void setCountNumber(int width, int height) {
		this.mCategoryCountPerScreen = width * height;
		this.mNumColumn = width;
	}

	/*
	 * setNumColumn - The column of each screen. setCategoryCountPerScreen - The
	 * category count of each screen. mCategoryType - Set the category type .
	 */
	public void setAttributes(int setNumColumn, int setCategoryCountPerScreen,
			int mCategoryType) {

		this.mNumColumn = setNumColumn;
		this.mCategoryCountPerScreen = setCategoryCountPerScreen;
		this.mCategoryType = mCategoryType;
	}

	public void setCategory(int category, int textCategory) {
		this.mCategory = category;
		this.mTextCategory = textCategory;
	}

	public int getNumColumn() {
		return mNumColumn;
	}

	public void setNumColumn(int mNumColumn) {
		this.mNumColumn = mNumColumn;
	}

	public int getCategoryCountPerScreen() {
		return mCategoryCountPerScreen;
	}

	public void setCategoryCountPerScreen(int mCategoryCountPerScreen) {
		this.mCategoryCountPerScreen = mCategoryCountPerScreen;
	}

	public int isSetCategoryName() {
		return mCategoryType;
	}

	public void setSetCategoryName(int mCategoryType) {
		this.mCategoryType = mCategoryType;
	}

}
