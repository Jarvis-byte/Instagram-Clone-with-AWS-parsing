package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnKeyListener{
    EditText editTextUsername;
    Button buttonSignIn;
    EditText editTextTextPassword;
    TextView logInTextView;
    ImageView logoImageView;
    ConstraintLayout backgroundLayout;
      Boolean LogInModeActive=true;


public void showUserList()
{
    Intent intent=new Intent(getApplicationContext(),UserListActivity.class);
    startActivity(intent);
}

       //for changing enter text event with the keyboard and at last  we can directly login with
    //the keyboard enter key.
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent KeyEvent) {
if(keyCode==KeyEvent.KEYCODE_ENTER && KeyEvent.getAction()== android.view.KeyEvent.ACTION_DOWN)
{
signUpClicked(v);
}
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.loginTextView)
        {
if(LogInModeActive)
{
    LogInModeActive=false;
    buttonSignIn.setText("SIGN UP");
    logInTextView.setText("Log In Here");
    }
else
{
    LogInModeActive=true;
    buttonSignIn.setText("LOGIN");
    logInTextView.setText("Sign Up Here");

}

        }
       else if (view.getId()==R.id.logoimageView||view.getId()==R.id.backgroundLayout)
        {
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }



public void signUpClicked(View view)
{

         if(editTextUsername.getText().toString().isEmpty()||editTextTextPassword.getText().toString().isEmpty())
         {
             Toast.makeText(this, "Please Enter Username Or Password", Toast.LENGTH_SHORT).show();
         }
         else {
             if (LogInModeActive){
                 ParseUser.logInInBackground(editTextUsername.getText().toString(), editTextTextPassword.getText().toString(), new LogInCallback() {
                     @Override
                     public void done(ParseUser user, ParseException e) {
                         if (e == null) {
                             Toast.makeText(MainActivity.this, "LogIn Successfull", Toast.LENGTH_SHORT).show();

                             showUserList();

                         } else {

                             Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
         }
             else
             { //new acc making.
                         ParseUser user=new ParseUser();
        user.setUsername(editTextUsername.getText().toString());
        user.setPassword(editTextTextPassword.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(MainActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    LogInModeActive=true;
                    buttonSignIn.setText("LOGIN");
                    logInTextView.setText("Sign Up Here");
                    showUserList();
                }
                else
                {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

             }

}
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("INSTAGRAM");
        editTextUsername=findViewById(R.id.editTextUsername);
        editTextTextPassword=findViewById(R.id.editTextTextPassword);
        buttonSignIn=findViewById(R.id.buttonSignIn);
         logInTextView=findViewById(R.id.loginTextView);
        logInTextView.setOnClickListener(this);
        logoImageView=findViewById(R.id.logoimageView);
        backgroundLayout=findViewById(R.id.backgroundLayout);
        //for changing enter text event with the keyboard and at last  we can directly login with
        //the keyboard enter key.
        editTextTextPassword.setOnKeyListener(this);
/*
ths is for vanishing a keyboard if we click anywhere in the display

 */
        logoImageView.setOnClickListener(this);
        backgroundLayout.setOnClickListener(this);



        if(ParseUser.getCurrentUser()!=null)
        {
            showUserList();
        }
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }
}