package fcu.app.zhuanti;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static final String IS_SIGNIN_FIELD = "isSignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isSignIn = prefs.getBoolean(IS_SIGNIN_FIELD, false);
        if (!isSignIn) {
            switchToActivity(login.class);
            return;
        }
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // Not logged in, go to login
            startActivity(new Intent(this, login.class));
            finish();
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        // 初始化 BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.nav_bar);

        // 初始化 fragment 實例
        Fragment home = new homeFragment();
        Fragment history = new historyFragment();
        Fragment profile = new profileFragment();

        // 根據跳轉 intent 設定預設選項
        int selectedTab = getIntent().getIntExtra("selected_tab", R.id.menu_home);
        bottomNav.setSelectedItemId(selectedTab);

        // 預設顯示的 Fragment
        if (selectedTab == R.id.menu_history) {
            setCurrentFragment(history);
        } else if (selectedTab == R.id.menu_profile) {
            setCurrentFragment(profile);
        } else {
            setCurrentFragment(home);
        }

        // 底部導覽點選事件
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {
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

    private void switchToActivity(Class<?> TargetActivity) {
        Intent intent = new Intent(MainActivity.this, TargetActivity);
        startActivity(intent);
        finish();
    }
}
