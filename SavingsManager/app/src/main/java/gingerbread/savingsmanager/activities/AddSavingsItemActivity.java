package gingerbread.savingsmanager.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.text.DateFormat;
import java.util.Calendar;

import gingerbread.savingsmanager.R;

public class AddSavingsItemActivity extends AppCompatActivity
        implements View.OnClickListener {

    private Calendar calendar;

    private EditText editBankName;
    private EditText editStartDate;
    private EditText editEndDate;
    private EditText editAmount;
    private EditText editYield;
    private EditText editInterest;
    private DatePickerDialog datePickerDialogStart;
    private DatePickerDialog datePickerDialogEnd;
    private Button btnCancel;
    private Button btnSave;

    public AddSavingsItemActivity(){
        calendar = Calendar.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_savings_item);

        //find Views
        editBankName = (EditText)findViewById(R.id.edit_bank_name);
        editStartDate = (EditText)findViewById(R.id.edit_start_date);
        editStartDate.setInputType(InputType.TYPE_NULL);
        editEndDate = (EditText)findViewById(R.id.edit_end_date);
        editEndDate.setInputType(InputType.TYPE_NULL);
        editAmount = (EditText)findViewById(R.id.edit_amount);
        editYield = (EditText)findViewById(R.id.edit_yield);
        editInterest = (EditText)findViewById(R.id.edit_interest);
        btnCancel = (Button)findViewById(R.id.btn_cancel);
        btnSave = (Button)findViewById(R.id.btn_save);

        //create DatePickerDialog
        datePickerDialogStart = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        editStartDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialogEnd = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        editEndDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        //set Listeners
        editStartDate.setOnClickListener(this);
        editEndDate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
     public void onClick(View v) {
        if(v == editStartDate) {
            datePickerDialogStart.show();
        }
        else if(v == editEndDate){
            datePickerDialogEnd.show();
        }
        else if(v == btnCancel){
            finish();
        }
        else if(v == btnSave) {
            //Snackbar.make(v, "Save Successful!", Snackbar.LENGTH_LONG).setAction("Save",null).show();
            finish();
        }
    }
}
