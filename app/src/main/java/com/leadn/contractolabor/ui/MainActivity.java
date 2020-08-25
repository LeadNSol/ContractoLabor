package com.leadn.contractolabor.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.leadn.contractolabor.R;
import com.leadn.contractolabor.ui.contracts.ContractFragment;
import com.leadn.contractolabor.ui.workers.WorkersFragment;
import com.leadn.contractolabor.utils.UtilClass;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //Boolean variable to mark if the transaction is safe
    private boolean isTransactionSafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (isTransactionSafe)
        UtilClass.pushFragment(new ContractFragment(), this, R.id.main_frame_layout, true);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    /*
    onPostResume is called only when the activity's state is completely restored. In this we will
    set our boolean variable to true. Indicating that transaction is safe now
     */
    public void onPostResume() {
        super.onPostResume();
        isTransactionSafe = true;
    }
        /*
        onPause is called just before the activity moves to background and also before onSaveInstanceState. In this
        we will mark the transaction as unsafe
         */

    public void onPause() {
        super.onPause();
        isTransactionSafe = false;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_contract:
                UtilClass.pushFragment(new ContractFragment(), this, R.id.main_frame_layout, true);
                //item.setChecked(true);
                break;
            case R.id.nav_workers:
                UtilClass.pushFragment(new WorkersFragment(), this, R.id.main_frame_layout, true);
                //item.setChecked(true);
                break;
            case R.id.nav_profile:
//                UtilClass.pushFragment(new ProfileFragment(), this, R.id.main_frame_layout, true);
                break;
        }
        return false;
    }
}