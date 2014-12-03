package com.clcoulte.gsort;

import java.util.List;

import com.clcoulte.gsort.MailUtil.GMailRunner;
import com.clcoulte.gsort.Util.Address;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "MainActivity";

	private ListView mainListView;

	private GMailRunner runner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "onCreate()");

		mainListView = (ListView) findViewById(R.id.mainListView);

		ArrayList<Address> addresses = new ArrayList<Address>();
		
		
		mainListView.setAdapter(new AddressListAdapter(this, android.R.layout.simple_list_item_1, addresses		));

		// Connects to GMail on instantiate
		runner = new GMailRunner();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class AddressListAdapter extends ArrayAdapter<Address> {

		public AddressListAdapter(Context context) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

	}
}
