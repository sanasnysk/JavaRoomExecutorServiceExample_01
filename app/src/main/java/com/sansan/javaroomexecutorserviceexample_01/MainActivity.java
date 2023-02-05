package com.sansan.javaroomexecutorserviceexample_01;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.room.Room;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private EditText userName, email, number;
    private ListView listView;
    private ListAdapter listAdapter;
    private CheckBox checkBox;

    private UserDatabase db;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDB();

        userName = findViewById(R.id.et_UserName);
        email = findViewById(R.id.et_Email);
        number = findViewById(R.id.et_Number);
        listView = findViewById(R.id.listview_data);
        checkBox = findViewById(R.id.updateCheckBox);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User) listAdapter.getItem(i);
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (checkBox.isChecked()) {
                            user.setEmail("xyz@gmail.com");
                            int rowsAffected = db.userDao().update(user);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Number of rows affected : " + rowsAffected, Toast.LENGTH_SHORT).show();

                                    if (rowsAffected > 0) {
                                        listAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        } else {
                            int rowsAffected = db.userDao().delete(user);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Number of rows affected : " + rowsAffected, Toast.LENGTH_SHORT).show();

                                    if (rowsAffected > 0) {
                                        listAdapter.removeData(i);
                                    }
                                }
                            });
                        }

                    }
                });

            }
        });

    }

    /* method to init room database*/
    private void initDB() {
        db = Room.databaseBuilder(getApplicationContext(),
                        UserDatabase.class, "my_database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public void saveData(View view) {
        String getName = userName.getText().toString().trim();
        String getEmail = email.getText().toString().trim();
        String getNumber = number.getText().toString().trim();

        if (TextUtils.isEmpty(getName) || TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getNumber)) {
            Toast.makeText(this, "Please enter all the inputs.", Toast.LENGTH_SHORT).show();
        } else {
            User user = new User();
            user.setName(getName);
            user.setEmail(getEmail);
            user.setNumber(getNumber);

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    long id = db.userDao().insert(user);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (id > 0) {
                                Toast.makeText(MainActivity.this, "Data Insertion success.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Data Insertion failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
        }
    }

    public void showData(View view) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<User> userList = db.userDao().getAllUsers();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter = new ListAdapter(MainActivity.this, userList);
                        listView.setAdapter(listAdapter);
                    }
                });
            }
        });
    }
}