package com.soontobe.joinpay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SendConfirmActivity extends ListActivity {


    // for testing only
    private String[][] paymentInfo;
    
    private ArrayAdapter<String> adapter;
    private ListView lv;

    private final static String ACTIVITY_MSG_ID ="activity_confirm";
    private final static String AMOUNT_ID ="amount_confirm"; 
    private final static String PERSONAL_NOTE_ID ="personal_note_confirm";   
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_confirm);
		setConstant();
		setListView();
	}
	
	private void setListView() {
		
		ListView list = getListView();
        List<Map<String, String>> values = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;
        for (int i = 0;i< paymentInfo.length;i++) {
        	if (i == 0) continue;
            map = new HashMap<String,String>();
            map.put(ACTIVITY_MSG_ID, paymentInfo[i][0]);
            map.put(AMOUNT_ID, paymentInfo[i][1]);
            map.put(PERSONAL_NOTE_ID, paymentInfo[i][2]);
            values.add(map);
        }
        String[] from = new String[]{ACTIVITY_MSG_ID, AMOUNT_ID, PERSONAL_NOTE_ID};
        
        int[] to = new int[]{R.id.activity_confirm, R.id.amount_confirm, R.id.personal_note_confirm};
        //Initiliazing Adapter
        SimpleAdapter adapter = new SimpleAdapter(SendConfirmActivity.this,
                values,								//values to be displayed
                R.layout.confirmation_page_item,	//list item layout id
                from,								//Keys for input values
                to									//keys for output items
                );
        //setting Adapter to ListView
//		list.setClickable(false);		//	not working

        list.setAdapter(adapter);
	}
	
	private void setConstant() {
		String[][] tmp = {
				{"5", "130", "Helloween Party"}, 	//	total # of payers, total amount, group note
				{"Patrick", "30", "Pay one extra beer"},   //	name, amount, personal note
				{"Benny", "20", ""},
				{"Kate", "20", ""},
				{"Jason", "25", ""},
				{"Melissa", "20", ""},
				{"Kate", "20", ""},
				{"Jason", "25", ""},
				{"Melissa", "20", ""},
				{"Kate", "20", ""},
				{"Jason", "25", ""},
				{"Melissa", "20", ""},
				{"Itziar", "15", ""}
		};
		paymentInfo = tmp;
	}
	
	public void backToSendInfo(View v) {
		finish();
	}
}