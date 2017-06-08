package gingerbread.savingsmanager.activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import gingerbread.savingsmanager.R;
import gingerbread.savingsmanager.data.SavingsManagerContentProvider;
import gingerbread.savingsmanager.data.SavingsProjectTable;
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

    private String mID;
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

        Map<String, Object> map = (Map<String, Object>)this.getIntent().getSerializableExtra(SavingsProjectTable.TABLE_NAME);
        if(map != null){
            this.setTitle(R.string.activity_title_edit_savings_item);
            btnCancel.setText("DELETE");
            mID = map.get(SavingsProjectTable._ID).toString();
            editBankName.setText(map.get(SavingsProjectTable.COLUMN_NAME_BANK_NAME).toString());
            mBankName = map.get(SavingsProjectTable.COLUMN_NAME_BANK_NAME).toString();
            editStartDate.setText(map.get(SavingsProjectTable.DISPLAY_START_DATE).toString());
            mStartDate = (Date)map.get(SavingsProjectTable.COLUMN_NAME_START_DATE);
            editEndDate.setText(map.get(SavingsProjectTable.DISPLAY_END_DATE).toString());
            mEndDate = (Date)map.get(SavingsProjectTable.COLUMN_NAME_END_DATE);
            editAmount.setText(map.get(SavingsProjectTable.DISPLAY_AMOUNT).toString());
            mAmount = (float)map.get(SavingsProjectTable.COLUMN_NAME_AMOUNT);
            editYield.setText(map.get(SavingsProjectTable.DISPLAY_YIELD).toString());
            mYield = (float)map.get(SavingsProjectTable.COLUMN_NAME_YIELD);
            editInterest.setText(map.get(SavingsProjectTable.DISPLAY_INTEREST).toString());
            mInterest = (float)map.get(SavingsProjectTable.COLUMN_NAME_INTEREST);
        }
        else{
            mID = null;
            btnCancel.setText("CANCEL");
            this.setTitle(R.string.activity_title_add_savings_item);
        }

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
            if (btnCancel.getText().equals("DELETE"))
            {
                getContentResolver().delete(SavingsManagerContentProvider.CONTENT_URI,"_id=" + mID, null);
            }
            onBackPressed();
        }
        else if(v == btnSave) {
            if (mInterest != 0.0f) {
                ContentValues values = new ContentValues();
                values.put(SavingsProjectTable.COLUMN_NAME_BANK_NAME, mBankName);
                values.put(SavingsProjectTable.COLUMN_NAME_AMOUNT, mAmount);
                values.put(SavingsProjectTable.COLUMN_NAME_YIELD, mYield);
                values.put(SavingsProjectTable.COLUMN_NAME_START_DATE, mStartDate.getTime());
                values.put(SavingsProjectTable.COLUMN_NAME_END_DATE, mEndDate.getTime());
                values.put(SavingsProjectTable.COLUMN_NAME_INTEREST, mInterest);

                if(mID == null){
                    getContentResolver().insert(SavingsManagerContentProvider.CONTENT_URI, values);
                }
                else{
                    getContentResolver().update(SavingsManagerContentProvider.CONTENT_URI, values, "_id=" + mID, null);
                }

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
            try {
                mAmount = Utils.getFloat(amountStr);
            } catch (ParseException e) {
                mAmount = 0.00f;
            }
        }
        else{
            mAmount = 0.00f;
        }
        String yieldStr = editYield.getText().toString();
        if(!Utils.isNullOrEmpty(yieldStr)){
            try {
                mYield = Utils.getFloat(yieldStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
