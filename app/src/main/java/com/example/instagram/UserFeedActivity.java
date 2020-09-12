package com.example.instagram;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserFeedActivity extends AppCompatActivity {
    LinearLayout linlayout;

    String email_sent="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        Intent intent=getIntent();
        String username= intent.getStringExtra("username");
                 setTitle(username+"'s Photos");
         linlayout=findViewById(R.id.linLayout);


        ParseQuery<ParseObject>query=new ParseQuery<ParseObject>("Image");
        query.whereEqualTo("username",username);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null&&objects.size()>0)
                {
                    for(ParseObject object:objects)
                    {
                        ParseFile file= (ParseFile) object.get("image");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null&&data!=null)
                                {
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView imageView=new ImageView(getApplicationContext());
                                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT
                                    ));

                                    imageView.setImageBitmap(bitmap);

                                    imageView.setPadding(0, 30, 0, 30);
                                    linlayout.addView(imageView);

                                }
                            }
                        });
                    }

                }
                else
                {

                    AlertDialog.Builder alert=new AlertDialog.Builder(UserFeedActivity.this);
                    alert.setMessage("Sorry User Have Not Uploaded Any Imagees")
                            .setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent1=new Intent(getApplicationContext(),UserListActivity.class);
                                    startActivity(intent1);
                                }
                            }).setNegativeButton("Stay",null);
                    AlertDialog builder=alert.create();
                    builder.show();

                }
            }
        });

    }
}