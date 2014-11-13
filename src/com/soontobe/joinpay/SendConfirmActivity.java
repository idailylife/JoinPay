package com.soontobe.joinpay;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SendConfirmActivity extends ListActivity {


	// for testing only
	private ArrayList<String[]> paymentInfo;
	private String[][] paymentInfoArray;

	private ArrayAdapter<String> adapter;
	private ListView lv;

	private final static String ACTIVITY_MSG_ID ="activity_confirm";
	private final static String AMOUNT_ID ="amount_confirm"; 
	private final static String PERSONAL_NOTE_ID ="personal_note_confirm";   

	private String transactionType;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_confirm);
		//setConstant();
		Bundle bundle = getIntent().getExtras();
		paymentInfo = (ArrayList<String[]>) bundle.get("paymentInfo");
		transactionType = getIntent().getExtras().getString("transactionType");
		setConfirmButtonText();
		updatePaneTitle();
		setListView();
		setEventListeners();
	}

	private void setConfirmButtonText() {
		Button confirmButton = (Button) findViewById(R.id.transaction_confirm_button);
		confirmButton.setText(transactionType);
	}

	private void updatePaneTitle() {
		TextView tv = (TextView) findViewById(R.id.title_transaction_confirm);
		tv.setText(transactionType);

	}

	private void setEventListeners() {
		Button transactionConfirmButton = (Button) findViewById(R.id.transaction_confirm_button);
		OnTouchListener buttonOnTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Button btn = (Button) v;
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn.setBackgroundResource(R.drawable.button_active);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					btn.setBackgroundResource(R.drawable.button_normal);
				}
				return false;
			}
		};
		transactionConfirmButton.setOnTouchListener(buttonOnTouchListener);

		Button sendEditPencil = (Button) findViewById(R.id.send_edit_pencil);
		buttonOnTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Button btn = (Button) v;
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn.setBackgroundResource(R.drawable.pencil_grey);
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					btn.setBackgroundResource(R.drawable.pencil_white);
				}
				return false;
			}
		};
		sendEditPencil.setOnTouchListener(buttonOnTouchListener);

	}


	private void setListView() {

		ListView list = getListView();
		//        List<Map<String, String>> values = new ArrayList<Map<String, String>>();
		//        Map<String, String> map = null;
		//        for (int i = 0;i< paymentInfo.length;i++) {
		//        	if (i == 0) continue;
		//            map = new HashMap<String,String>();
		//            map.put(ACTIVITY_MSG_ID, paymentInfo[i][0]);
		//            map.put(AMOUNT_ID, paymentInfo[i][1]);
		//            map.put(PERSONAL_NOTE_ID, paymentInfo[i][2]);
		//            values.add(map);
		//        }
		//        String[] from = new String[]{ACTIVITY_MSG_ID, AMOUNT_ID, PERSONAL_NOTE_ID};
		//        
		//        int[] to = new int[]{R.id.activity_confirm, R.id.amount_confirm, R.id.personal_note_confirm};
		//        //Initiliazing Adapter
		//        SimpleAdapter adapter = new SimpleAdapter(SendConfirmActivity.this,
		//                values,								//values to be displayed
		//                R.layout.confirm_page_item_with_personal_note,	//list item layout id
		//                from,								//Keys for input values
		//                to									//keys for output items
		//                );
		//        //setting Adapter to ListView
		////		list.setClickable(false);		//	not working
		//
		//        list.setAdapter(adapter);

		boolean isPending = false;	//	Don't care
		boolean isHistory = false;
		PaymentSummaryAdapter adapter = new PaymentSummaryAdapter(this, paymentInfo, isHistory);
		list.setAdapter(adapter);
	}


	private void setConstant() {
		String[][] tmp = 
			{
				{"normal", "", "Luna", "Itziar", "$ 500"},
				{"normal", "Pay one extra beer", "Patrick", "Itziar", "$ 30"},   //	name, amount, personal note
				{"normal", "", "asd", "Itziar", "$ 20"},
				{"normal", "", "Itziar", "Itziar", "$ 20"},
				{"group_note", "This is a group note"},
				{"summary", "2014-11-14", "5", "$ 130"}
			};
		paymentInfo = new ArrayList<String[]>();
		for (int i = 0;i < tmp.length;i++) {
			paymentInfo.add(tmp[i]);
		}
		paymentInfoArray = tmp;
	}

	public void backToSendInfo(View v) {
		finish();
	}

	public void proceedToConfirmSend(View v) {
		String retData = "";
		for (int i = 0;i < paymentInfo.size();i++) {
			if (i != 0) retData += "|";
			for (int j = 0;j < paymentInfo.get(i).length;j++) {
				if (j != 0) retData += ",";
				retData += paymentInfo.get(i)[j];
			}
		}
		final String paymentInfoString = retData;
		new Thread() {
			@Override
			public void run() {
				WebConnector webConnector = new WebConnector(Constants.userName);
				webConnector.postTransactionRecord(Constants.urlForPostingToFolder, paymentInfoString);
			}
		}.start();
		Intent data = new Intent();
		data.setData(Uri.parse(retData));
		setResult(RESULT_OK, data);
		finish();
	}
}