package com.clcoulte.gsort;

import java.util.ArrayList;
import java.util.List;

import com.clcoulte.gsort.MailUtil.GMailRunner;
import com.clcoulte.gsort.Util.Address;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		mainListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(
						parent.getContext(),
						((AddressListAdapter) parent.getAdapter()).addresses
								.get(position).getBaseDomain()
								+ " - "
								+ position, Toast.LENGTH_LONG).show();

				ArrayList<Address> addresses = new ArrayList<Address>();
				addresses.add(new Address("asdf@asdf.fdsf.csdc", 5));
				addresses.add(new Address("test@com.clcoulte.div", 6));

				mainListView.setAdapter(new AddressListAdapter(parent
						.getContext(), addresses));
			}

		});
		ArrayList<Address> addresses = new ArrayList<Address>();
		addresses.add(new Address());
		addresses.add(new Address());

		mainListView.setAdapter(new AddressListAdapter(this, addresses));

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

		private final Context context;
		private final ArrayList<Address> addresses;

		public AddressListAdapter(Context context, ArrayList<Address> addresses) {
			super(context, R.id.listDataModel, addresses);
			this.context = context;
			this.addresses = addresses;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			if (rowView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.listdatamodel, parent,
						false);
			}
			TextView nameTextView = (TextView) rowView
					.findViewById(R.id.nameTextView);
			TextView countTextView = (TextView) rowView
					.findViewById(R.id.countTextView);
			nameTextView.setText(addresses.get(position).getBaseDomain());
			countTextView.setText("" + addresses.get(position).getCount());
			return rowView;
		}
	}
}
