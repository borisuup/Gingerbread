package gingerbread.savingsmanager.activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

import gingerbread.savingsmanager.R;
import gingerbread.savingsmanager.data.SavingsManagerContentProvider;
import gingerbread.savingsmanager.data.SavingsProjectEntity;
import gingerbread.savingsmanager.utils.Constants;
import gingerbread.savingsmanager.utils.Utils;

public class AddSavingsItemActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher{

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

    private String mBankName;
    private Date mStartDate;
    private Date mEndDate;
    private float mAmount;
    private float mYield;
    private float mInterest;

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
                        calendar.set(year, month, dayOfMonth);
                        mStartDate = calendar.getTime();
                        editStartDate.setText(Utils.formatDate(calendar.getTime()));
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
                        calendar.set(year, month, dayOfMonth);
                        mEndDate = calendar.getTime();
                        editEndDate.setText(Utils.formatDate(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        //set Listeners
        editBankName.addTextChangedListener(this);
        editStartDate.addTextChangedListener(this);
        editEndDate.addTextChangedListener(this);
        editAmount.addTextChangedListener(this);
        editYield.addTextChangedListener(this);

        editStartDate.setOnClickListener(this);
        editEndDate.setOnClickListener(this);

        editStartDate.setOnFocusChangeListener(this);
        editEndDate.setOnFocusChangeListener(this);
        editAmount.setOnFocusChangeListener(this);
        editYield.setOnFocusChangeListener(this);

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
            onBackPressed();
        }
        else if(v == btnSave) {
            if (mInterest != 0.0f) {
                ContentValues values = new ContentValues();
                values.put(SavingsProjectEntity.COLUMN_NAME_BANK_NAME, mBankName);
                values.put(SavingsProjectEntity.COLUMN_NAME_AMOUNT, mAmount);
                values.put(SavingsProjectEntity.COLUMN_NAME_YIELD, mYield);
                values.put(SavingsProjectEntity.COLUMN_NAME_START_DATE, mStartDate.getTime());
                values.put(SavingsProjectEntity.COLUMN_NAME_END_DATE, mEndDate.getTime());
                values.put(SavingsProjectEntity.COLUMN_NAME_INTEREST, mInterest);
                Uri uri = getContentResolver().insert(SavingsManagerContentProvider.CONTENT_URI, values);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.missing_savings_information ,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(v == editStartDate && hasFocus) {
            datePickerDialogStart.show();
        }
        else if(v == editEndDate && hasFocus){
            datePickerDialogEnd.show();
        }
        else if(v == editAmount && !hasFocus){
            editAmount.setText(Utils.formatFloat(mAmount));
        }
        else if(v == editYield && !hasFocus){
            editYield.setText(Utils.formatFloat(mYield));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mBankName = editBankName.getText().toString().trim();
        String amountStr = editAmount.getText().toString();
        if(!Utils.isNullOrEmpty(amountStr)){
            mAmount = Float.valueOf(editAmount.getText().toString());
        }
        else{
            mAmount = 0.00f;
        }
        String yieldStr = editYield.getText().toString();
        if(!Utils.isNullOrEmpty(yieldStr)){
            mYield = Float.valueOf(editYield.getText().toString());
        }
        else{
            mYield = 0.00f;
        }

        if (!Utils.isNullOrEmpty(mBankName) && mStartDate != null && mEndDate != null){
            int days = Utils.getDiffDays(mStartDate, mEndDate);
            float interest = mAmount * (mYield / 100) * days / Constants.DAYS_OF_ONE_YEAR;
            mInterest = Utils.roundFloat(interest);
            editInterest.setText(Utils.formatFloat(mInterest));
            Log.d(Constants.LOG_TAG, "start = " + mStartDate.toString() + "\nend = " + mEndDate.toString()
                    + "\ndays = " + days + "\namount = " + mAmount + "\nyield = " + mYield + "\ninterest = " + mInterest);
        }
    }
}
