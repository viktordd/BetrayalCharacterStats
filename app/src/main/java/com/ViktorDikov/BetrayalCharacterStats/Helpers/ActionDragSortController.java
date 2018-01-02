package com.ViktorDikov.BetrayalCharacterStats.Helpers;

import android.view.MotionEvent;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

public class ActionDragSortController extends DragSortController {

    public interface ActionListener {
        public void click(int which);
    }
    private DragSortListView mDslv;
    private ActionListener mActionListener;
    private int mActionID = 0;
	private int mActionHitPos = MISS;
    
	public ActionDragSortController(DragSortListView dslv) {
		super(dslv);
		mDslv = dslv;
	}
	public ActionDragSortController(DragSortListView dslv, int dragHandleId, int dragInitMode, int removeMode) {
		super(dslv, dragHandleId, dragInitMode, removeMode);
		mDslv = dslv;
	}
	public ActionDragSortController(DragSortListView dslv, int dragHandleId, int dragInitMode, int removeMode, int clickRemoveId) {
		super(dslv, dragHandleId, dragInitMode, removeMode, clickRemoveId);
		mDslv = dslv;
	}
	public ActionDragSortController(DragSortListView dslv, int dragHandleId, int dragInitMode, int removeMode, int clickRemoveId, int flingHandleId) {
		super(dslv, dragHandleId, dragInitMode, removeMode, clickRemoveId, flingHandleId);
		mDslv = dslv;
	}
	
	

    public void setActionListener(ActionListener l) {
    	mActionListener = l;
    }
	
    /**
     * Set the resource id for the View that represents click action
     *
     * @param id An android resource id.
     */
    public void setActionID(int id) {
    	mActionID = id;
    }
	
    @Override
    public boolean onDown(MotionEvent ev) {
    	if (mActionID > 0 && mActionListener != null)
    		mActionHitPos = viewIdHitPosition(ev, mActionID);
    	
    	return super.onDown(ev);
    }
    
    @Override
    public boolean onSingleTapUp(MotionEvent ev) {
	    if (mActionHitPos != MISS && mActionListener != null) {
	    	mActionListener.click(mActionHitPos -  mDslv.getHeaderViewsCount());
	    }
        return super.onSingleTapUp(ev);
    }
}
