package com.santeh.rjhonsl.samplemap.Adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.santeh.rjhonsl.samplemap.Obj.CustInfoObject;
import com.santeh.rjhonsl.samplemap.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Growouts_PondWeekLyConsumption extends ArrayAdapter<CustInfoObject> {

	Context context;
	LayoutInflater inflater;
	List<CustInfoObject> objArrayList;
	ListView listViewItem;
	int positions = 0;
	String tag = "CreateNew ArrayAdapter";
	private SparseBooleanArray mSelectedItemsIds;
	ArrayList<Boolean> positionArray;
	int[] visiblePosArray;
	private volatile int positionCheck;

//	android:id="@+id/itemlv_feedconsSummary_farmname"

	public Adapter_Growouts_PondWeekLyConsumption(Context context, int resourceId, List<CustInfoObject> items) {
		super(context, resourceId, items);
		mSelectedItemsIds = new SparseBooleanArray();
		this.context = context;
		this.objArrayList = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		positionArray = new ArrayList<Boolean>(objArrayList.size());
		for(int i = 0 ; i<objArrayList.size() ; i++){
			positionArray.add(false);
		}
	}

	private class ViewHolder {
		TextView txtweekno, txtfeedtype, txtactual, txtrecommended, txtremarks;
		CheckBox chkisVisited;
		LinearLayout weeknoHOlder;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		positions = position;

		if (view == null) {

			Log.d(tag, "if null");
			holder = new ViewHolder();

			view = inflater.inflate(R.layout.item_lv_weeklypondsummary, null);

			holder.txtweekno = (TextView) view.findViewById(R.id.itemlv_weeklyreport_pondsummary_weeknum);
			holder.txtfeedtype = (TextView) view.findViewById(R.id.itemlv_weeklyreport_pondsummary_feedtype);
			holder.txtactual = (TextView) view.findViewById(R.id.itemlv_weeklyreport_pondsummary_actual);
			holder.txtrecommended = (TextView) view.findViewById(R.id.itemlv_weeklyreport_pondsummary_recommended);
			holder.txtremarks = (TextView) view.findViewById(R.id.itemlv_weeklyreport_pondsummary_remarks);

			holder.chkisVisited = (CheckBox) view.findViewById(R.id.chk_weeklyreport_pondsummary_isVisited);


			holder.weeknoHOlder = (LinearLayout) view.findViewById(R.id.weeknoHOlder);

			view.setTag(holder);
		}
		else
		{
			Log.d(tag, "if not null");

			holder = (ViewHolder) view.getTag();
			holder.chkisVisited.setOnCheckedChangeListener(null);

		}

//		 Capture position and set to the TextViews
		holder.txtweekno.setText("" + objArrayList.get(position).getCurrentweekofStock());
		holder.txtfeedtype.setText("Feed Type: "+objArrayList.get(position).getCurrentfeedType());
		holder.txtremarks.setText("Remarks: "+objArrayList.get(position).getRemarks());

		if ((position+1) == objArrayList.get(position).getStartweekofStock() ){
			holder.txtweekno.setBackground(context.getResources().getDrawable(R.drawable.bg_lightred_box_curvebottom));}
		else if((position+1) == objArrayList.get(position).getWeek() ){
			holder.txtweekno.setBackground(context.getResources().getDrawable(R.drawable.bg_blue_box_curvebottom));
		}
		else{
			holder.txtweekno.setBackground(context.getResources().getDrawable(R.drawable.bg_darkteal_box_curvebottom));
		}

		if (Double.parseDouble(objArrayList.get(position).getRecommendedConsumption()) > 0){
			holder.txtrecommended.setText("Recommended: "+ (objArrayList.get(position).getRecommendedConsumption())+"kg");
		}else{
			holder.txtrecommended.setText("Recommended: Blind Feeding");
		}

		if (objArrayList.get(position).getActualConsumption().equalsIgnoreCase("n/a")){
			holder.txtactual.setText("Actual: "+ (objArrayList.get(position).getActualConsumption())+"");
		}else{
			holder.txtactual.setText("Actual: " +objArrayList.get(position).getActualConsumption ()+ "kg");
		}



		holder.chkisVisited.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked ){
					positionArray.set(position, true);

				}else
					positionArray.set(position, false);
			}
		});




//		holder.txtactual.setText("Weekly Consumption: "+(df.format(objArrayList.get(position).getWeeklyConsumptionInGrams() / 1000))+" kg");


		return view;
	}

	@Override
	public void remove(CustInfoObject object) {
		objArrayList.remove(object);
		notifyDataSetChanged();
	}

	public List<CustInfoObject> getAnswerList() {
		return objArrayList;
	}

	public void toggleSelection(int position) {
		selectView(position, !mSelectedItemsIds.get(position));
	}

	public void removeSelection() {
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}

	public void selectView(int position, boolean value) {
		if (value)
			mSelectedItemsIds.put(position, value);
		else
			mSelectedItemsIds.delete(position);
		notifyDataSetChanged();
	}

	public int getSelectedCount() {
		return mSelectedItemsIds.size();
	}

	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}

}
