package edu.ggc.it.rss;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.ggc.it.R;

/**
 * Activity responsible for displaying RSS events feed from ggc.edu
 * 
 * @author crystalist
 *
 */
public class EventsRSSActivity extends ListActivity {

	private List<String> titles;
	private List<String> links;
	private List<String> publishedDates;
	private List<String> descriptions;
	private Context context;
	
	public enum RSSTaskDetail 
	{
		Item("item"),
		Title("title"),
		Link("link"),
		Description("description"),
		PublishDate("pubDate");		
		
		private final String detail;       

		private RSSTaskDetail(String s) 
		{
			detail = s;
	    }

		 public String getValue(){return detail;}
	}
	

	/**
	 * Current GGC news RSS url
	 */
	public final static String rssURL = "http://www.ggc.edu/student-life/events-calendar/events-calendar/rss";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rss);

		titles = new ArrayList<String>();
		links = new ArrayList<String>();
		publishedDates = new ArrayList<String>();
		descriptions = new ArrayList<String>();
		context = this;
		new RSSTask().execute();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	   Uri uri = Uri.parse(links.get(position));
	   Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	   startActivity(intent);
	}

	
	
	
	// Types for Params, Progress and Result are all void since the task is very simple
	private class RSSTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... args) {
			try {
				URL url = new URL(rssURL);
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(false);
				XmlPullParser parser = factory.newPullParser();
				parser.setInput(url.openConnection().getInputStream(), "UTF-8");

				// get the title, link and description for all valid "item"
				boolean insideItem = false;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {
						
						if (parser.getName().equalsIgnoreCase(RSSTaskDetail.Item.toString())) {
							insideItem = true;
						} else if (parser.getName().equalsIgnoreCase(RSSTaskDetail.Title.getValue())) {
							if (insideItem)
								titles.add(parser.nextText());
						} else if (parser.getName().equalsIgnoreCase(RSSTaskDetail.Link.getValue())) {
							if (insideItem)
								links.add(parser.nextText());
						} else if (parser.getName().equalsIgnoreCase(RSSTaskDetail.Description.getValue())) {
							if (insideItem)
								descriptions.add(parser.nextText());
						} else if (parser.getName().equalsIgnoreCase(RSSTaskDetail.PublishDate.getValue())) {
							if (insideItem)
								publishedDates.add(parser.nextText());
						}
					} else if (eventType == XmlPullParser.END_TAG
							&& parser.getName().equalsIgnoreCase(RSSTaskDetail.Item.toString())) {
						insideItem = false;
					}
					eventType = parser.next();
				}
			} catch (MalformedURLException e) {
			    Log.e("GGC-CONNECT", "MalformedURL", e);
			} catch (XmlPullParserException e) {
				Log.e("GGC-CONNECT","XmlPULLParser", e);
			} catch (IOException e) {
				Log.e("GGC-CONNECT","IO",e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			NewsAdapter adapter = new NewsAdapter(context,titles,publishedDates, descriptions);
			setListAdapter(adapter);
		}
	}

}
