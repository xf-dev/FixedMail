package xf.fixedmail;

import jp.co.crossfinity.fixedmail.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends Activity {

	private final int REQUEST_CODE = 111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit);

		Button addressBtn = (Button) findViewById(R.id.address_button);
		addressBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(EditActivity.this,
						ContactListActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		});

		Button registerBtn = (Button) findViewById(R.id.register_button);
		registerBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				buttonInsert_Click(v);
				Intent intent = new Intent(EditActivity.this, FixedMailActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK
				&& data != null) {
			String name = data.getStringExtra("name");
			String address = data.getStringExtra("address");
			TextView nameText = (TextView) findViewById(R.id.mail_name_text);
			TextView addressText = (TextView) findViewById(R.id.delivery_address_text);
			nameText.setText(name);
			addressText.setText(address);
		}
	}

	/*
	 * InsertボタンClick処理
	 */
	private long buttonInsert_Click(View v) {
		EditText nameEditText = (EditText)findViewById(R.id.mail_name_text);
		EditText addressEditText = (EditText)findViewById(R.id.delivery_address_text);
		EditText titleEditText = (EditText)findViewById(R.id.title_text);
		EditText bodyEditText = (EditText)findViewById(R.id.body_text);

		ContentValues values = new ContentValues();
		values.put("name", nameEditText.getText().toString());
		values.put("address", addressEditText.getText().toString());
		values.put("title", titleEditText.getText().toString());
		values.put("body", bodyEditText.getText().toString());

		DatabaseHelper dbHelper = new DatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long ret;
		try {
			ret = db.insert("MAIL_INFO", null, values);
		} finally {
			db.close();
		}
		return ret;
	}
}
