
package com.example.sprint;

import java.util.List;

import android.annotation.SuppressLint;
import android.preference.PreferenceActivity;

public class EditPreferencesHC extends PreferenceActivity {

	@SuppressLint("NewApi")
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}
}
//figure out why double depth prefs...
