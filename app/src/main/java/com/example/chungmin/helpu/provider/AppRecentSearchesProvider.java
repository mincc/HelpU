package com.example.chungmin.helpu.provider;

import android.content.SearchRecentSuggestionsProvider;

/**
 * @author vgrec, created on 3/18/15.
 */
public class AppRecentSearchesProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.example.chungmin.helpu";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public AppRecentSearchesProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
