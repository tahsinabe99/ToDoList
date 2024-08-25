package comp5216.sydney.edu.au.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class EditToDoItemActivity extends AppCompatActivity
{
	public int position=0;
	EditText etItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Populate the screen using the layout
		setContentView(R.layout.activity_edit_item);
		
		// Get the data from the main activity screen
		String editItem = getIntent().getStringExtra("item");
		position = getIntent().getIntExtra("position",-1);
		
		// Show original content in the text field
		etItem = (EditText)findViewById(R.id.etEditItem);
		etItem.setText(editItem);
	}

	public void onSubmit(View v) {
		etItem = (EditText) findViewById(R.id.etEditItem);

		// Prepare data intent for sending it back
		Intent data = new Intent();

		// Pass relevant data back as a result
		data.putExtra("item", etItem.getText().toString());
		data.putExtra("position", position);

		// Activity finishes OK, return the data
		setResult(RESULT_OK, data); // Set result code and bundle data for response
		finish(); // Close the activity, pass data to parent
	}

	public void onCancel(View v){
		//Log.i("EditActivity", "Cancel item " + position);
		AlertDialog.Builder builder = new AlertDialog.Builder(EditToDoItemActivity.this);
		builder.setTitle(R.string.dialog_cancel_title)
				.setMessage(R.string.dialog_cancel_msg)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						finish();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						// User cancelled the dialog
						// Nothing happens

					}
				});

		builder.create().show();

	}
}
