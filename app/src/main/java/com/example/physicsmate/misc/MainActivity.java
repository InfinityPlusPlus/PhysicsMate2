package com.example.physicsmate.misc;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Menu;
import android.widget.ScrollView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.physicsmate.R;
import com.example.physicsmate.ui.CustomKeyboard;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.physicsmate.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static CustomKeyboard keyboard;
    private static ScrollView linkedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

//        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null)
//                        .setAnchorView(R.id.fab).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_notes, R.id.nav_settings).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //link with the keyboard
        ConstraintLayout cl = findViewById(R.id.cl_content_main);
        keyboard = new CustomKeyboard(this, null);
        cl.addView(keyboard);
        cl.post(() -> {
            keyboard = new CustomKeyboard(this, null);
            cl.addView(keyboard);
            //set constraints
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginStart(0);
            layoutParams.setMarginEnd(0);
            layoutParams.bottomToBottom = cl.getId();
            layoutParams.leftToLeft = cl.getId();
            layoutParams.rightToRight = cl.getId();
            keyboard.setLayoutParams(layoutParams);
            keyboard.hideKeyboard();
        });


            new Handler(Looper.getMainLooper()).postDelayed(() -> new Thread(() -> {
                LoggerFix.fix();
                Custom_methods.calculateNthDerivative("3x", "x", 1);
            }).start(), 500); // Delay by 500ms after activity shows
        }

        public static void setLinkedScrollView (ScrollView sv){
            linkedScrollView = sv;
        }

        @Override public void onBackPressed () {
            if (keyboard != null && keyboard.getVisibility() == View.VISIBLE) {
                keyboard.setVisibility(View.GONE);
                if (linkedScrollView != null) {
                    linkedScrollView.setPadding(0, 0, 0, 0);
                }
            } else {
                super.onBackPressed();
            }
        }

        public static CustomKeyboard getKeyboard () {
            return keyboard;
        }

        @Override public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }

        @Override public boolean onSupportNavigateUp () {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
        }
    }