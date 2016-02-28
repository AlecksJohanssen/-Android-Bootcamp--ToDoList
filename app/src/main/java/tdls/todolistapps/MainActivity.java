package tdls.todolistapps;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.widget.DatePicker;
import android.widget.Toolbar;
import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    DatabaseHelper TDdb;
    ArrayList<String> todoItems;
    ArrayAdapter<String> aTodoAdapter;
    ListView lvItems;
    EditText editTD;
    Button btnAddItems;
    Button btn1;
    int pos2;
    public void setDate(View view )
    {
        PickerDialog pickerDialogs = new PickerDialog();
        pickerDialogs.show(getFragmentManager(), "date_picker");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTD = (EditText) findViewById(R.id.tdEditText);
        lvItems = (ListView) findViewById(R.id.lvItems);
        btnAddItems = (Button) findViewById(R.id.tdAddItems);
        populateArrayItems();
        AddData();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.list_ingredients);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        lvItems.setAdapter(aTodoAdapter);
        final Context context = this;
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                todoItems.remove(position);
                writeItems();
                aTodoAdapter.notifyDataSetChanged();
                return true;
            }

        });
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pos2 = position;

                        Intent intent = new Intent(context, Edit.class);
                        writeItems();
                        startActivityForResult(intent, 2);
                    }
                }
        );
        TDdb = new tdls.todolistapps.DatabaseHelper(this);
    }

    public void AddData()
    {
        btnAddItems.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isInserted = TDdb.insertData(editTD.getText().toString());
                        if (isInserted = true) {
                            todoItems.add(editTD.getText().toString());
                            writeItems();
                            aTodoAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Items Inserted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Items not Inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
    public void populateArrayItems()
    {
        todoItems = new ArrayList<String>();
        readItems();
        aTodoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);

    }
    public void readItems(){
        File filedir = getFilesDir();
        File todoFile = new File(filedir,"todo.txt");
        try{
            todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch (IOException e){
            todoItems = new ArrayList<String>();
        }
    }

    public void writeItems(){
        File filedir = getFilesDir();
        File todoFile = new File(filedir,"todo.txt");
        try{
            FileUtils.writeLines(todoFile,todoItems);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add("Email");
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            String message=data.getStringExtra("MESSAGE");
            if(!message.equals("0"))
            {
                todoItems.set(pos2,message);
                writeItems();
                aTodoAdapter.notifyDataSetChanged();
            }
        }


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

    public void onAddItem(View v) {
        aTodoAdapter.add(editTD.getText().toString());
        writeItems();
        editTD.setText("");
    }


}
