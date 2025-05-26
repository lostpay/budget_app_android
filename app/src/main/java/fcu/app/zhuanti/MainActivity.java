package fcu.app.zhuanti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import fcu.app.zhuanti.Adapter.historyAdapter;
import fcu.app.zhuanti.model.history;

public class MainActivity extends AppCompatActivity {

    public static final String IS_SIGNIN_FIELD="isSignIn";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences prefs=getSharedPreferences("MyPrefs",MODE_PRIVATE);
        boolean isSignIn=prefs.getBoolean("isSignIn",true);
        if(!isSignIn){
            switchToActivity(login.class);
        }

        BottomNavigationView bottomNav = findViewById(R.id.nav_bar);
        Fragment home = homeFragment.newInstance("", "");
        Fragment history = historyFragment.newInstance("", "");
        Fragment profile = profileFragment.newInstance("", "");

        setCurrentFragment(home);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_home) {
                    setCurrentFragment(home);
                } else if (item.getItemId() == R.id.menu_history) {
                    setCurrentFragment(history);
                } else {
                    setCurrentFragment(profile);
                }
                return true;
            }
        });

    }
    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_main, fragment)
                .commit();

    }
    private void switchToActivity(Class TargetActivity){
        Intent intent=new Intent(MainActivity.this,TargetActivity);
        startActivity(intent);
        finish();
    }
}