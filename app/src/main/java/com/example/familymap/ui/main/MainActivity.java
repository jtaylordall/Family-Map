package com.example.familymap.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.familymap.R;
import com.example.familymap.data.DataStash;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static com.example.familymap.data.Values.REQ_CODE_ORDER_INFO;


public class MainActivity extends AppCompatActivity {

    private RegisterFragment registerFragment;
    private DataStash dataStash;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Iconify.with(new FontAwesomeModule());

        dataStash = DataStash.getInstance();

        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (registerFragment == null && !dataStash.isLoggedIn()) {
            registerFragment = RegisterFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.main_activity_frame_layout, registerFragment).commit();
        } else {
            MapFragment mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.main_activity_frame_layout, mapFragment).commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if (dataStash.isLoggedIn()) {
            createMenu();
            return true;    // return true to display menu
        } else {
            return true;
        }
    }

    public void createMenu() {
        if (menu != null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_main, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.search:
                intent = SearchActivity.newIntent(this.getApplicationContext());
                startActivityForResult(intent, REQ_CODE_ORDER_INFO);
                return true;
            case R.id.settings:
                intent = SettingsActivity.newIntent(this.getApplicationContext());
                startActivityForResult(intent, REQ_CODE_ORDER_INFO);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    public static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public class Message {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}




