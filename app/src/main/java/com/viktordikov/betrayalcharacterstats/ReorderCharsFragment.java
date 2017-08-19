package com.viktordikov.betrayalcharacterstats;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.viktordikov.betrayalcharacterstats.Data.CharacterOrderProvider;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

public class ReorderCharsFragment extends ListFragment {

	private ArrayList<Integer> IdList;
	private ArrayAdapter<String> adapter;

	private boolean changed = false;

	private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			if (from != to) {
				Integer id = IdList.get(from);
				IdList.remove(from);
				IdList.add(to, id);

				String item = adapter.getItem(from);
				adapter.remove(item);
				adapter.insert(item, to);

				changed = true;
			}
		}
	};

	private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
		@Override
		public void remove(int which) {
			IdList.remove(which);
			adapter.remove(adapter.getItem(which));

			changed = true;
		}
	};

	private ActionDragSortController.ActionListener onClick = new ActionDragSortController.ActionListener() {

		@Override
		public void click(int which) {
			Integer id = IdList.get(which);
			IdList.remove(which);
			adapter.remove(adapter.getItem(which));

			id += (id % 2 == 0 ? 1 : -1);

			IdList.add(which, id);
			adapter.insert(getResources().getStringArray(R.array.titles)[id], which);

			changed = true;
		}
	};

	protected int getLayout() {
		// this DSLV xml declaration does not call for the use
		// of the default DragSortController; therefore,
		// DSLVFragment has a buildController() method.
		return R.layout.fragment_reorder_chars;
	}

	/**
	 * Return list item layout resource passed to the ArrayAdapter.
	 */
	protected int getItemLayout() {
		return R.layout.list_item_handle_left;
	}

	private DragSortListView mDslv;
	private ActionDragSortController mController;

	public int dragStartMode = DragSortController.ON_DRAG;
	public int removeMode = DragSortController.FLING_REMOVE;
	public boolean removeEnabled = true;
	public boolean sortEnabled = true;
	public boolean dragEnabled = true;

	public static ReorderCharsFragment newInstance(int headers, int footers) {
		ReorderCharsFragment f = new ReorderCharsFragment();

		Bundle args = new Bundle();
		args.putInt("headers", headers);
		args.putInt("footers", footers);
		f.setArguments(args);

		return f;
	}

	public DragSortController getController() {
		return mController;
	}

	/**
	 * Called from DSLVFragment.onActivityCreated(). Override to set a different
	 * adapter.
	 */
	public void setListAdapter() {
		CharacterOrderProvider order = new CharacterOrderProvider(getActivity());
		IdList = order.getIDs();

		String[] allTitles = getResources().getStringArray(R.array.titles);

		Integer numChars = IdList.size();
		ArrayList<String> titles = new ArrayList<String>(numChars);
		for (Integer id : IdList) {
			titles.add(allTitles[id]);
		}

		adapter = new ArrayAdapter<String>(getActivity(), getItemLayout(), R.id.text, titles);
		setListAdapter(adapter);
	}

	/**
	 * Called in onCreateView. Override this to provide a custom
	 * DragSortController.
	 */
	public ActionDragSortController buildController(DragSortListView dslv) {
		// defaults are
		// dragStartMode = onDown
		// removeMode = flingRight
		ActionDragSortController controller = new ActionDragSortController(dslv);
		controller.setDragHandleId(R.id.drag_handle);
		controller.setRemoveEnabled(removeEnabled);
		controller.setSortEnabled(sortEnabled);
		controller.setDragInitMode(dragStartMode);
		controller.setRemoveMode(removeMode);

		controller.setActionID(R.id.flip);
		controller.setActionListener(onClick);

		return controller;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mDslv = (DragSortListView) inflater.inflate(getLayout(), container, false);

		mController = buildController(mDslv);
		mDslv.setFloatViewManager(mController);
		mDslv.setOnTouchListener(mController);
		mDslv.setDragEnabled(dragEnabled);

		return mDslv;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mDslv = (DragSortListView) getListView();

		mDslv.setDropListener(onDrop);
		mDslv.setRemoveListener(onRemove);

		setListAdapter();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_reset_order) {
			Resources r = getResources();
			Integer numChars = r.getInteger(R.integer.numCharacters);
			String[] titles = r.getStringArray(R.array.titles);

			IdList.clear();
			adapter.clear();
			for (int i = 0; i < numChars; i++) {
				IdList.add(i * 2);
				adapter.add(titles[i * 2]);
			}
			changed = true;
			adapter.notifyDataSetChanged();

			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

		if (!changed)
			return;
		
		CharacterOrderProvider order = new CharacterOrderProvider(getActivity());
		order.setIDs(IdList);
		order.setChanged();
		order.apply();
	}
}
