package com.jjanonis.donatas.registermystudent.Activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjanonis.donatas.registermystudent.Adapters.AbsenceDaysAdapter;
import com.jjanonis.donatas.registermystudent.Fragments.AbsenceReasonDialogFragment;
import com.jjanonis.donatas.registermystudent.R;
import com.jjanonis.donatas.registermystudent.models.AbsenceDay;

import java.time.LocalDate;
import java.util.ArrayList;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AbsenceReasonDialogFragment.AbsenceDialogListener {

    private LocalDate selectedDate;
    private AbsenceDaysAdapter absenceDaysAdapter;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference personalReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createDatabaseConnection();
        initiateCalendar();
        initiateListOfAbsenceDays();
        initiateFab();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void createDatabaseConnection() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        personalReference = database.getReference(currentUser.getUid());
    }

    private void initiateCalendar() {
        final CalendarView calendar = (CalendarView) findViewById(R.id.main_calendar);
        if (selectedDate == null) selectedDate = LocalDate.now();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                LocalDate currentSelectedDate = LocalDate.of(year, month + 1, dayOfMonth);

//                Toast.makeText(NavigationDrawerActivity.this, currentSelectedDate.toString(), Toast.LENGTH_SHORT).show();
                setSelectedDate(currentSelectedDate);
                loadDayAbsenceList(currentSelectedDate);
            }
        });
    }

    private void initiateFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new AbsenceReasonDialogFragment();
                dialogFragment.show(getFragmentManager(), "dialog");
            }
        });
    }

    private void initiateListOfAbsenceDays() {
        ArrayList<AbsenceDay> absenceDays = new ArrayList<>();
        absenceDaysAdapter = new AbsenceDaysAdapter(this, absenceDays);

        ListView absenceDayList = (ListView) findViewById(R.id.absence_days);
        absenceDayList.setAdapter(absenceDaysAdapter);
    }

    private void saveAbsenceDayToDatabase(AbsenceDay dayToSave) {
        personalReference.child(selectedDate.toString()).push().setValue(dayToSave);
    }

    @Override
    public void onDialogAddClick(DialogFragment dialog, String lecture, String reason) {
        AbsenceDay newDay = new AbsenceDay(selectedDate.toString(), lecture, reason);
        saveAbsenceDayToDatabase(newDay);

        Snackbar.make(findViewById(R.id.fab), "Added absence day", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onDialogCancelClick(DialogFragment dialog) {
        Snackbar.make(findViewById(R.id.fab), "Action cancelled", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            signOut();
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

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(NavigationDrawerActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }

    private void loadDayAbsenceList(LocalDate selectedDate) {
        DatabaseReference dateReference = personalReference.child(selectedDate.toString());
        absenceDaysAdapter.clear();
        if (dateReference == null) return;

        dateReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                absenceDaysAdapter.clear();

                for (DataSnapshot instanceOfAbsenceDay : dataSnapshot.getChildren()) {
                    AbsenceDay selectedAbsence = instanceOfAbsenceDay.getValue(AbsenceDay.class);
                    if (selectedAbsence != null) {
                        absenceDaysAdapter.add(selectedAbsence);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        absenceDaysAdapter.add(new AbsenceDay(selectedDate));
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }
}
