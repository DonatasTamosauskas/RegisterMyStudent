package com.jjanonis.donatas.registermystudent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jjanonis.donatas.registermystudent.R;
import com.jjanonis.donatas.registermystudent.models.ChatMessage;
import com.jjanonis.donatas.registermystudent.models.ChatRoomType;

public class ChatActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ChatRoomType chatType;
    private DatabaseReference chatRoomDatabase;
    private FirebaseUser currentUser;
    private FirebaseListAdapter<ChatMessage> messageAdapter;
    private FirebaseListOptions<ChatMessage> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatType = (ChatRoomType) getIntent().getSerializableExtra("chatRoom");

        createDatabaseConnection();
        initiateDrawer();
        createChatListAdapter();
    }

    @Override
    protected void onStart() {
        super.onStart();
        messageAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        messageAdapter.stopListening();
    }

    private void createChatListAdapter() {
        ListView messageList = (ListView) findViewById(R.id.messageListView);

        Query query = chatRoomDatabase;
        options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.message_bubble)
                .build();

        messageAdapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View view, ChatMessage model, int position) {
                TextView messageText = (TextView) view.findViewById(R.id.nameTextView);
                TextView userName = (TextView) view.findViewById(R.id.messageTextView);

                userName.setText(model.getUser());
                messageText.setText(model.getText());
            }
        };

        messageList.setAdapter(messageAdapter);
    }

    private void createDatabaseConnection() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        if (chatType == ChatRoomType.GENERAL) {
            chatRoomDatabase = database.getReference("chatRooms").child("general");

        } else if (chatType == ChatRoomType.LECTURE) {
            chatRoomDatabase = database.getReference("chatRooms").child("lecture");

        } else if (chatType == ChatRoomType.EXAM) {
            chatRoomDatabase = database.getReference("chatRooms").child("exam");

        } else if (chatType == ChatRoomType.VACATION) {
            chatRoomDatabase = database.getReference("chatRooms").child("vacation");
        }
    }

    private void initiateDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

        changeActionBarText(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.chat_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_chat);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void changeActionBarText(Toolbar actionbar) {
        setSupportActionBar(actionbar);

        if (chatType == ChatRoomType.GENERAL) getSupportActionBar().setTitle("General chat");
        else if (chatType == ChatRoomType.LECTURE) getSupportActionBar().setTitle("Lectures chat");
        else if (chatType == ChatRoomType.EXAM) getSupportActionBar().setTitle("Exams & tests chat");
        else if (chatType == ChatRoomType.VACATION) getSupportActionBar().setTitle("Vacations chat");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.chat_drawer_layout);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            signOut();
        }

        return super.onOptionsItemSelected(item);
    }


    //TODO fix redirecting and database connection refreshing
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.general_chat) {
            switchChatRoom(ChatRoomType.GENERAL);

        } else if (id == R.id.lecture_chat) {
            switchChatRoom(ChatRoomType.LECTURE);

        } else if (id == R.id.exam_chat) {
            switchChatRoom(ChatRoomType.EXAM);

        } else if (id == R.id.vacation_chat) {
            switchChatRoom(ChatRoomType.VACATION);

        } else if (id == R.id.switch_calendar_activity) {
            startActivity(new Intent(ChatActivity.this, NavigationDrawerActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.chat_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void switchChatRoom(ChatRoomType chatRoom) {
        if (chatRoom != this.chatType) {
//            EditText messageText = (EditText) findViewById(R.id.messageEditText);
////            ListView messageList = (ListView) findViewById(R.id.messageListView);
//            messageText.setText("");
//
//            this.chatType = chatRoom;
//            createChatListAdapter();

            Intent newChatRoomIntent = new Intent(ChatActivity.this, ChatActivity.class);
            newChatRoomIntent.putExtra("chatRoom", chatRoom);

            startActivity(newChatRoomIntent);
        }
    }

    public void sendMessage(View view) {
        EditText messageText = (EditText) findViewById(R.id.messageEditText);
        ChatMessage newMessage = new ChatMessage(currentUser.getDisplayName(), messageText.getText().toString());

        messageText.setText("");

        chatRoomDatabase.push().setValue(newMessage);
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        startActivity(new Intent(ChatActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }
}
