package gingerbread.savingsmanager.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gingerbread.savingsmanager.R;
import gingerbread.savingsmanager.data.SavingsManagerContentProvider;
import gingerbread.savingsmanager.data.SavingsProjectTable;
import gingerbread.savingsmanager.utils.Constants;
import gingerbread.savingsmanager.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private FloatingActionButton fabAddSavings;
    private ListView lvSavings;
    private float TotalInterest;
    private TextView totalinterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabAddSavings = (FloatingActionButton) findViewById(R.id.fab_add_savings);
        fabAddSavings.setOnClickListener(this);

        lvSavings = (ListView)findViewById(R.id.lv_savings);
        lvSavings.setOnItemLongClickListener(this);
        lvSavings.setOnItemClickListener(this);
        totalinterest = (TextView) findViewById(R.id.txt_total_interest_value);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor cursor = getContentResolver().query(SavingsManagerContentProvider.CONTENT_URI, null, null, null, "_id desc");
        TotalInterest = 0;
        List<Map<String, Object>> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String,Object> map = new HashMap<>();
            map.put(SavingsProjectTable._ID,
                    cursor.getLong(cursor.getColumnIndex(SavingsProjectTable._ID))
            );
            map.put(SavingsProjectTable.COLUMN_NAME_BANK_NAME,
                    cursor.getString(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_BANK_NAME))
            );
            map.put(SavingsProjectTable.COLUMN_NAME_START_DATE,
                    new Date(cursor.getLong(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_START_DATE)))
            );
            map.put(SavingsProjectTable.DISPLAY_START_DATE,
                    Utils.formatDate(cursor.getLong(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_START_DATE)))
            );
            map.put(SavingsProjectTable.COLUMN_NAME_END_DATE,
                    new Date(cursor.getLong(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_END_DATE)))
            );
            map.put(SavingsProjectTable.DISPLAY_END_DATE,
                    Utils.formatDate(cursor.getLong(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_END_DATE)))
            );
            map.put(SavingsProjectTable.COLUMN_NAME_AMOUNT,
                    cursor.getFloat(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_AMOUNT))
            );
            map.put(SavingsProjectTable.DISPLAY_AMOUNT,
                    Utils.formatFloat(cursor.getFloat(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_AMOUNT)))
            );
            map.put(SavingsProjectTable.COLUMN_NAME_YIELD,
                    cursor.getFloat(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_YIELD))
            );
            map.put(SavingsProjectTable.DISPLAY_YIELD,
                    Utils.formatFloat(cursor.getFloat(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_YIELD)))
            );
            map.put(SavingsProjectTable.COLUMN_NAME_INTEREST,
                    cursor.getFloat(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_INTEREST))
            );
            map.put(SavingsProjectTable.DISPLAY_INTEREST,
                    Utils.formatFloat(cursor.getFloat(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_INTEREST)))
            );
            TotalInterest += cursor.getFloat(cursor.getColumnIndex(SavingsProjectTable.COLUMN_NAME_INTEREST));
            list.add(map);
        }
        ListAdapter listAdapter = new SimpleAdapter(this, list, R.layout.savings_item,
                new String[]{SavingsProjectTable.COLUMN_NAME_BANK_NAME, SavingsProjectTable.DISPLAY_YIELD,
                        SavingsProjectTable.DISPLAY_START_DATE, SavingsProjectTable.DISPLAY_END_DATE,SavingsProjectTable.DISPLAY_AMOUNT,SavingsProjectTable.DISPLAY_INTEREST},
                new int[]{R.id.txt_bank_name_value, R.id.txt_yield_value,
                          R.id.txt_start_date_value, R.id.txt_end_date_value, R.id.txt_amount_value,R.id.txt_expected_interest}
        );
        lvSavings.setAdapter(listAdapter);
        totalinterest.setText(Utils.formatFloat(TotalInterest));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(getBaseContext(),AddSavingsItemActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v == fabAddSavings){
            Intent intent = new Intent(getBaseContext(),AddSavingsItemActivity.class);
            startActivity(intent);
        }
        //getSystemService();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == lvSavings) {
            Map<String,String> map = (Map<String, String>) lvSavings.getAdapter().getItem(position);
            Toast.makeText(this, "Bank Name: " + map.get(SavingsProjectTable.COLUMN_NAME_BANK_NAME) ,Toast.LENGTH_SHORT).show();
            Log.d(Constants.LOG_TAG, "Click item " + id);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(parent == lvSavings) {
            Intent intent = new Intent(getBaseContext(),AddSavingsItemActivity.class);
            intent.putExtra(SavingsProjectTable.TABLE_NAME, (Serializable) lvSavings.getAdapter().getItem(position));
            startActivity(intent);
            return true;
        }
        return false;
    }

}
