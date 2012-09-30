package jp.co.crossfinity.fixedmail;

import java.util.ArrayList;
import java.util.List;

import jp.co.crossfinity.fixedmail.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactListActivity extends Activity {

	List<MailInfo> mailInfoList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactlist);

		String strContact = null;
		MailInfo mailInfo = null;
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		mailInfoList = new ArrayList<MailInfo>();

		Cursor c = managedQuery(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);
		while (c.moveToNext()) {

			String id = c.getString(c
					.getColumnIndex(ContactsContract.Contacts._ID));
			String name = c.getString(c.getColumnIndex("display_name"));
			Cursor emails = managedQuery(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
							+ id, null, null);

			while (emails.moveToNext()) {
				String email = emails
						.getString(emails
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				mailInfo = new MailInfo();
				mailInfo.setName(name);
				mailInfo.setAddress(email);
				mailInfoList.add(mailInfo);
				strContact = name;
				strContact += "\n " + email;
				adapter.add(strContact);
			}
			emails.close();
		}
		c.close();
		ListView lv = (ListView) findViewById(R.id.contactListview);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long id) {
				MailInfo info = mailInfoList.get(position);
				Intent intent = new Intent();
				intent.putExtra("name", info.getName());
				intent.putExtra("address", info.getAddress());
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
