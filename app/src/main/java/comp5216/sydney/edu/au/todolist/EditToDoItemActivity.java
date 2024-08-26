package comp5216.sydney.edu.au.todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


public class EditToDoItemActivity extends AppCompatActivity
{
	public int position=0;
	EditText etItem;
	Spinner spinner;
	Button datePickerButton;
	Button timePickerButton;
	Calendar c;
	String dateTime;
	String date;
	String time;

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

		//populating spinner
		spinner=(Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,
				R.array.type,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		spinner.setAdapter(adapter);

		//populating Date picket
		c=Calendar.getInstance();
		datePickerButton=findViewById(R.id.datePickerButton);
		timePickerButton=findViewById(R.id.timePickerButton);
		setDateDeadline();
		setTimeDeadline();
	}


	private void setDateDeadline(){
		datePickerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				;//gets current date and year. Will need it later
				int year=c.get(Calendar.YEAR);
				int month=c.get(Calendar.MONTH);
				int day= c.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog datePickerDialog=new DatePickerDialog(EditToDoItemActivity.this/*Context where dialog appears*/,
						new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker datePicker, int yearNum, int monthNum, int dayNum) {

								Toast.makeText(EditToDoItemActivity.this,
										"Date:"+dayNum, Toast.LENGTH_SHORT).show();
								//save data
								String date=yearNum+":"+monthNum+":"+dayNum;
								saveDate(date);
							}
						}, year, month, day);
				datePickerDialog.show();// shows the dialogue
			}
		});

	}

	private void setTimeDeadline(){
		timePickerButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				int hour= c.get(Calendar.HOUR_OF_DAY);
				int minute=c.get(Calendar.MINUTE);

				//Making TimePicker Dialogue
				TimePickerDialog timePickerDialog=new TimePickerDialog(EditToDoItemActivity.this,
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker timePicker, int setHour, int setMinute) {

								Toast.makeText(EditToDoItemActivity.this,
										"Selected Time:"+ setHour+ ":"+String.format("%02d", minute),
										Toast.LENGTH_SHORT).show();
								//save data
								String time="T"+setHour+":"+setMinute+":"+"00:00Z";
								savetime(time);


							}
						}, hour, minute, false);
				timePickerDialog.show();
			}
		});
	}


	public void onSubmit(View v) {
		etItem = (EditText) findViewById(R.id.etEditItem);

		// Prepare data intent for sending it back
		Intent data = new Intent();

		// Pass relevant data back as a result
		data.putExtra("item", etItem.getText().toString());
		data.putExtra("position", position);
		data.putExtra("deadline", dateTime);

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

	private void saveDate(String date){
		this.date=date;
	}
	private void savetime(String time){
		this.time=time;
	}
	private void formatDeadline(){
		this.dateTime=this.date+this.time;
	}


}


