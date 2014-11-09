/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soontobe.joinpay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Filter.FilterListener;
import android.widget.ListView;

public class ContactListActivity extends ListActivity {

	final Set<String> nameSelected = new HashSet<String>();

	private ListView lv;

	// Listview Adapter
	private ArrayAdapter<String> adapter;

	// Search EditText
	private EditText inputSearch;


	// ArrayList for Listview
	ArrayList<HashMap<String, String>> productList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_from_contact_list);

		int layoutType = android.R.layout.simple_list_item_multiple_choice;
		final ListView lv = getListView();
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		adapter = new ArrayAdapter<String>(this, layoutType, Constants.NameList);

		setListAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
				String name = (String) (lv.getItemAtPosition(position));
				if (lv.isItemChecked(position)) {
					nameSelected.add(name);
				} else {
					nameSelected.remove(name);
				}
			}
		});

		inputSearch = (EditText) findViewById(R.id.contact_search_input);
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				// When user changed the Text

				for (int i = 0;i < adapter.getCount();i++) {
					Log.d("adpater state before", adapter.getItem(i));
					if (nameSelected.contains(adapter.getItem(i))) {
						lv.setItemChecked(i, true);
					} else {
						lv.setItemChecked(i, false);
					}
				}    
				adapter.getFilter().filter(cs, new FilterListener() {
					@Override
					public void onFilterComplete(int count) {
						// TODO Auto-generated method stub
						for (int i = 0;i < adapter.getCount();i++) {
							Log.d("adpater state after", adapter.getItem(i));
							if (nameSelected.contains(adapter.getItem(i))) {
								lv.setItemChecked(i, true);
							} else {
								lv.setItemChecked(i, false);
							}
						}    
					}
				});
				
				findViewById(R.id.contact_search_input_clear_button).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						EditText contactSearchBox = (EditText) findViewById(R.id.contact_search_input);
						contactSearchBox.setText("");
					}
				});;
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub  

			}
		});
	}

	public void BackFromContactToMain(View v) {
		finish();
	}

	public void AddContactAndBackToMain(View v) {
		Intent data = new Intent();
		data.setData(Uri.parse(nameSelected.toString()));
		setResult(RESULT_OK, data);
		finish();
	}
}