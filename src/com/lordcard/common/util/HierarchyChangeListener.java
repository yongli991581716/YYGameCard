package com.lordcard.common.util;

import android.view.View;
import android.view.ViewGroup;

public class HierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
	MultiScreenTool mst = null;

	public HierarchyChangeListener(MultiScreenTool mst) {
		this.mst = mst;
	}

	@Override
	public void onChildViewAdded(View parent, View child) {
		if (parent != null) {
			mst.adjustView(parent);
		}
	}

	@Override
	public void onChildViewRemoved(View parent, View child) {
		//		if (child != null) {
		//			//mst.unRegisterView(child);
		//		}
	}

}
