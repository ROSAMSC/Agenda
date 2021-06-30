package no4mat.no4mat.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import no4mat.no4mat.agenda.api.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String[] CATEGORY_DATA = new String[]{
            "Actividad Física",
            "Trabajo",
            "Compras",
            "Recreativo",
            "Otros"
    };

    ArrayList<AData> listAgenda = new ArrayList<>();
    SQLiteDatabase db;

    Spinner category;
    EditText etDate, etTime, etName, etLastName, etPhone;
    ImageButton ibDate, ibTime;
    Button saveButton;
    Button cancelButton;
    LinearLayout listLayout;
    LinearLayout inputLayout;
    ListView list;

    AData AData;

    boolean status_menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status_menu = true;

        BDLocal lcdb = new BDLocal(this);
        db = lcdb.getWritableDatabase();

        etDate = (EditText) findViewById(R.id.editTextDate);
        etTime = (EditText) findViewById(R.id.editTextTime);
        etName = (EditText) findViewById(R.id.editTextName);
        etName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etLastName = (EditText) findViewById(R.id.editTextLastName);
        etLastName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        etPhone = (EditText) findViewById(R.id.editTextPhone);
        ibDate = (ImageButton) findViewById(R.id.imageButtonDate);
        ibTime = (ImageButton) findViewById(R.id.imageButtonTime);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        listLayout = (LinearLayout) findViewById(R.id.listLayout);
        inputLayout = (LinearLayout) findViewById(R.id.LinearLayout);
        list = (ListView) findViewById(R.id.list);

        ibDate.setOnClickListener(this);
        ibTime.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        AData = new AData();
        updateEditTextDateTime();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CATEGORY_DATA);
        category = (Spinner) findViewById(R.id.sp_category);
        category.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteMessage(listAgenda.get(position));
            }
        });

        readDatabase();
        updateList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.getItem(0).setVisible(status_menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAddElement:
                changeStatus();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void temp () {
        for (int i = 0; i < 20; i++) {
            AData entry = new AData();
            entry.name = "Nombre " + i;
            listAgenda.add(entry);
        }
    }

    private void updateList () {
        String[] names = new String[listAgenda.size()];
        String[] lastNames = new String[listAgenda.size()];
        String[] dates = new String[listAgenda.size()];
        String[] times = new String[listAgenda.size()];

        for (int i = 0; i < listAgenda.size(); i++ ){
            names[i] = listAgenda.get(i).name;
            lastNames[i] = listAgenda.get(i).lastName;
            dates[i] = listAgenda.get(i).getDateFormat();
            times[i] = listAgenda.get(i).getTimeFormat();
        }

        ListViewAdapter listAdapter = new ListViewAdapter(this, names, lastNames, dates, times);
        list.setAdapter(listAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonDate:
                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        AData.year = year;
                        AData.month = month + 1;
                        AData.day = dayOfMonth;
                        updateEditTextDateTime();
                    }
                };
                dateFragment.show(getSupportFragmentManager(), "Fecha");
                break;
            case R.id.imageButtonTime:
                TimePickerFragment timeFragment = new TimePickerFragment();
                timeFragment.listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        AData.hour = hourOfDay;
                        AData.minute = minute;
                        updateEditTextDateTime();
                    }
                };
                timeFragment.show(getSupportFragmentManager(), "Hora");
                break;
            case R.id.saveButton:
                AData.name = etName.getText().toString();
                AData.lastName = etLastName.getText().toString();
                AData.phoneNumber = etPhone.getText().toString();

                listAgenda.add(AData);
                insertDatabase(AData);
                AData = new AData();
                updateList();
                Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
                changeStatus();
                clearAllEditText();
                break;
            case R.id.cancelButton:
                changeStatus();
                AData = new AData();
                clearAllEditText();
                break;
        }
    }

    private void changeStatus () {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation b = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        if (status_menu) {
            listLayout.startAnimation(b);
            inputLayout.startAnimation(a);
            inputLayout.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.GONE);
            status_menu = false;
        } else {
            inputLayout.startAnimation(b);
            listLayout.startAnimation(a);
            inputLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
            status_menu = true;
        }
        this.invalidateOptionsMenu();
        readDatabase();
    }

    private void updateEditTextDateTime () {
        etDate.setText(AData.getDateFormat());
        etTime.setText(AData.getTimeFormat());
    }

    private void clearAllEditText () {
        etName.setText(null);
        etLastName.setText(null);
        etPhone.setText(null);
    }


    private void insertDatabase (AData AData) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<AData> call = apiInterface.addEntry(AData);
        call.enqueue(new Callback<AData>() {
            @Override
            public void onResponse(Call<AData> call, Response<AData> response) {
                Toast.makeText(getApplicationContext(),"Guardado", Toast.LENGTH_SHORT).show();
                updateList();
            }

            @Override
            public void onFailure(Call<AData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Encontramos inconvenientes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readDatabase () {
        listAgenda = new ArrayList<AData>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<List<AData>> call = apiInterface.getEntries();
        call.enqueue(new Callback<List<AData>>() {
            @Override
            public void onResponse(Call<List<AData>> call, Response<List<AData>> response) {
                if(!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
                } else {
                    List<AData> list = response.body();
                    for (AData entry: list) {
                        listAgenda.add(entry);
                    }
                    updateList();
                }
            }

            @Override
            public void onFailure(Call<List<AData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Problema de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteMessage (AData AData) {

        DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiInterface.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiInterface apiInterface = retrofit.create(ApiInterface.class);
                Call<AData> call = apiInterface.deleteEntry(AData.id);
                call.enqueue(new Callback<AData>() {
                    @Override
                    public void onResponse(Call<AData> call, Response<AData> response) {
                        Toast.makeText(getApplicationContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                        readDatabase();
                        updateList();
                    }

                    @Override
                    public void onFailure(Call<AData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        DialogInterface.OnClickListener negative = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        };

        DeleteAlert adl = new DeleteAlert(positive, negative);
        adl.show(getSupportFragmentManager(), "¿Eliminar?");

    }
}