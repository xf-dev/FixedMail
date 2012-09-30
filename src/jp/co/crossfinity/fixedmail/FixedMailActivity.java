package jp.co.crossfinity.fixedmail;

import jp.co.crossfinity.fixedmail.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class FixedMailActivity extends Activity implements OnItemClickListener {

	private DatabaseHelper helper = new DatabaseHelper(this);

	private static SQLiteDatabase db;

	private MailInfoArrayAdapter adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.adapter = new MailInfoArrayAdapter(this, R.layout.row);

		// データベースをオープン
		db = helper.getWritableDatabase();

		// メール情報を取得
		String[] columns = { "id", "name", "address", "title", "body" };
		Cursor c = db.query("MAIL_INFO", columns, null, null, null, null, null);
		startManagingCursor(c);
		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				MailInfo info = new MailInfo();
				info.setId(c.getInt(c.getColumnIndex("id")));
				info.setName(c.getString(c.getColumnIndex("name")));
				info.setTitle(c.getString(c.getColumnIndex("title")));
				info.setAddress(c.getString(c.getColumnIndex("address")));
				info.setBody(c.getString(c.getColumnIndex("body")));
				adapter.add(info);
			}
		}
		ListView lv = (ListView) findViewById(R.id.listview);
		lv.setAdapter(adapter);
	}

	/** リストビューが選択された */
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long id) {
		this.adapter.select(position);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 0, 0, R.string.option_add);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(FixedMailActivity.this,
					EditActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
		return true;
	}
}

// リストビュー内の項目と関連付けるデータ
class ViewHolder {
	TextView nameText;
	TextView titleText;
	Button editButton;
	Button sendButton;
}

class MailInfoArrayAdapter extends ArrayAdapter<MailInfo> {
	private LayoutInflater mInflater; // レイアウトXMLファイルからビューを作ってくれるもの
	private int mLayoutId; // このアダプタのレイアウトID
	private int mSelectedItem; // 選択されているアイテム

	/** コンストラクタ */
	public MailInfoArrayAdapter(Context context, int layoutId) {
		super(context, layoutId);

		// ビューを作るためのサービスを取り出す
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mLayoutId = layoutId;

		this.mSelectedItem = -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder vh = null;

		// 初期状態
		if (view == null) {
			// カスタムビューをリストビューに登録する
			view = this.mInflater.inflate(this.mLayoutId, parent, false);
			// ビューホルダーとしてビューにリストビューの項目を登録する
			vh = new ViewHolder();
			vh.nameText = (TextView) view.findViewById(R.id.name);
			vh.titleText = (TextView) view.findViewById(R.id.title);
			vh.editButton = (Button) view.findViewById(R.id.edit_button);
			vh.editButton.setTag(this.getItem(position));
			vh.editButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					MailInfo info = (MailInfo) v.getTag();
					Intent intent = new Intent(v.getContext(), EditActivity.class);
					intent.putExtra("mailInfo", info);
					v.getContext().startActivity(intent);
				}
			});

			vh.sendButton = (Button) view.findViewById(R.id.send_button);

			vh.sendButton.setTag(this.getItem(position));
			vh.sendButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					MailInfo info = (MailInfo) v.getTag();
					Intent intent = new Intent();

					// アクションを指定
					intent.setAction(Intent.ACTION_SENDTO);
					// データを指定
					intent.setData(Uri.parse("mailto:" + info.getAddress()));

					// 件名を指定
					intent.putExtra(Intent.EXTRA_SUBJECT, info.getTitle());
					// 本文を指定
					intent.putExtra(Intent.EXTRA_TEXT, info.getBody());
					// Intentを発行
					v.getContext().startActivity(intent);
				}
			});
		} else {
			vh = (ViewHolder) view.getTag();
			vh.sendButton.setTag(this.getItem(position));
		}

		// 今設定しないといけないアイテムを取り出す
		MailInfo info = this.getItem(position);
		vh.nameText.setText(info.getName());
		vh.titleText.setText(info.getTitle());
		return view;
	}

	/** 選択されているものを設定する */
	public void select(int position) {
		// 新しく選択されたものをセットする
		this.mSelectedItem = position;
		// 変更を通知する
		this.notifyDataSetChanged();
	}
}