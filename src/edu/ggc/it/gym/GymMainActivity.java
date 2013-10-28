package edu.ggc.it.gym;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.ggc.it.R;

public class GymMainActivity extends Activity {


	private Button contract;
	private Button schedule;
	private Button groups;
	private Button buddiesButton;
	private Button magazine;
	private Button HomeButton;
	private Context myContext;
	private TextView quote;
	private Intent intent;
	public final static String EXTRA_MESSAGE = "edu.ggc.it.directory.MESSAGE";
	@Override

	/**
	 * This method creates all of the buttons according to their names and 
	 * locations of the button
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gym_main);
		
		myContext = this;
		contract = (Button) findViewById(R.id.firstTime);
		contract.setOnClickListener(new ButtonListener());

		schedule = (Button) findViewById(R.id.gymSchedule);
		schedule.setOnClickListener(new ButtonListener());

		groups = (Button) findViewById(R.id.Group);
		groups.setOnClickListener(new ButtonListener());

		magazine = (Button) findViewById(R.id.healthMagazine);
		magazine.setOnClickListener(new ButtonListener());
		
		quote = (TextView) findViewById(R.id.quoteTextView);
		
		try {
			// insert text file in the assets folder
			// you can't write to the assests folder, it is read-only

			AssetManager am = myContext.getAssets();
			InputStream in = am.open("quotes.txt");			
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String str;
			ArrayList <String> quotes = new ArrayList<String>();
			while ((str = reader.readLine()) != null) {
				quotes.add(str);
				Collections.shuffle(quotes);
			}
			in.close();
			
			quote.setText(quotes.get(0).toString());
			//in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_gym_main, menu);
		return true;
	}


	/**
	 * this method creates the listeners for all of the buttons individually
	 * 
	 */
	public class ButtonListener implements OnClickListener {
		public void onClick(View view) {

			if (contract.isPressed()){
				//TODO: First time and renewal feature does not do anything.
				
				Toast.makeText(myContext, "Sorry, this function is not working at the moment \n Please try again later.", Toast.LENGTH_LONG)
				.show();
			}
			else if (view.getId() == R.id.gymSchedule){
				startActivity(new Intent (myContext, GymScheduleActivity.class));
				//Toast.makeText(myContext, "What a cute cat!", Toast.LENGTH_LONG)
				//.show();
			}
			else if (groups.isPressed()){
				startActivity( new Intent ( myContext, GroupsActivity.class));
			}
			
			
			else if (magazine.isPressed()){
				String url = "http://readsh101.com/ggc.html";
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(browserIntent);
				
			}
		}

	}
}