package com.soontobe.joinpay;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class ConfirmPageArrayAdapter extends ArrayAdapter<String[]> {
	private final Context context;

	/*
	 * [][0]: type
	 * 
	 *  type: normal/normal_pn
	 *  	[1] personal note
	 *  	[2] payer
	 *  	[3] payee
	 *  	[4] amount
	 *  
	 *  type: summary
	 *  	[1] date (and maybe time)
	 *  	[2] # of ppl
	 *  	[3] total amount
	 *  
	 *  type: group_note
	 */
	private final ArrayList<String[]> values;

	public ConfirmPageArrayAdapter(Context context, List<String[]> values) {
		super(context, R.layout.confirm_page_item, values);		//	dummy?
		this.context = context;
		this.values = (ArrayList<String[]>) values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView;
		if (values.get(position)[0].equals("normal")) {
			rowView = inflater.inflate(R.layout.confirm_page_item, parent, false);
			TextView personalNoteView = (TextView) rowView.findViewById(R.id.confirm_personal_note_normal);
			TextView payerView = (TextView) rowView.findViewById(R.id.activity_confirm_payer);
			TextView payeeView = (TextView) rowView.findViewById(R.id.activity_confirm_payee);
			TextView amountView = (TextView) rowView.findViewById(R.id.amount_confirm);
			personalNoteView.setText(values.get(position)[1]);
			payerView.setText(values.get(position)[2]);
			payeeView.setText(values.get(position)[3]);
			amountView.setText(values.get(position)[4]);
			if (values.get(position)[2].equals(values.get(position)[3])) {
				payerView.setText("You paid");
				payerView.setTextColor(Color.rgb(0xb3, 0xb3, 0xb3));
				amountView.setTextColor(Color.rgb(0xb3, 0xb3, 0xb3));
				LinearLayout item = (LinearLayout) rowView.findViewById(R.id.activity_confirm_pay_item_layout);
				item.removeView(item.findViewById(R.id.activity_confirm_pay_text));
				item.removeView(payeeView);
				item.requestLayout();
			}
			if (values.get(position)[1].length() == 0) {	//	without personal note
				TableLayout tr = (TableLayout) rowView;
				tr.removeView(tr.findViewById(R.id.confirm_personal_note_row_elm));
				tr.requestLayout();
			}
		} else if (values.get(position)[0].equals("summary")) {
			rowView = inflater.inflate(R.layout.confirm_page_total, parent, false);
			TextView dateView = (TextView) rowView.findViewById(R.id.activity_confirm_date);
			TextView numOfPeopleView = (TextView) rowView.findViewById(R.id.activity_confirm_num_ppl);
			TextView totalAmountView = (TextView) rowView.findViewById(R.id.activity_confirm_total);
			dateView.setText(values.get(position)[1]);
			numOfPeopleView.setText(values.get(position)[2]);
			totalAmountView.setText(values.get(position)[3]);
		} else if (values.get(position)[0].equals("group_note")) {
			rowView = inflater.inflate(R.layout.confirm_page_group_note, parent, false);
			TextView groupNoteView = (TextView) rowView.findViewById(R.id.confirm_group_note);
			groupNoteView.setText(values.get(position)[1]);
		} else {	// hopefully won't happen
			Log.e("ConfirmPageArrayAdapter", "Wrong value type");
			rowView = inflater.inflate(R.layout.confirm_page_item, parent, false);
		}

		return rowView;
	}
} 
