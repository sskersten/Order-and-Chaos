/* * * * * * * * * * * * * * * * * * *
 * William Kersten                   *
 * CSCI 4010                         *
 * Assignment 5 - Order and Chaos    *
 * November, 2017                    *
 * * * * * * * * * * * * * * * * * * */
package williamkersten.csci.orderandchaos;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startGameButton = (Button) findViewById(R.id.start_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //Log.i("info", "this should start the game!");
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        Button moreInfoButton = (Button) findViewById(R.id.moreInfo_button);
        moreInfoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Uri uri = Uri.parse("https://en.wikipedia.org/wiki/Order_and_Chaos");
                Intent intent = new Intent (Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        Button aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.i("info", "This should open an activity w/ info about the game!");
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
