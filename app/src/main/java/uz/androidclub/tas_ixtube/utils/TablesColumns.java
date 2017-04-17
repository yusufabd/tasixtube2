package uz.androidclub.tas_ixtube.utils;

import static android.provider.BaseColumns._ID;

/**
 * Created by yusufabd on 3/3/2017.
 */

public interface TablesColumns{

    String TABLE_FAVORITE = "favorite_videos";

    String COLUMN_FAVORITE_TABLE_ID = _ID;

    String COLUMN_FAVORITE_ID = "id";
    String COLUMN_FAVORITE_TITLE = "title";
    String COLUMN_FAVORITE_OWNER = "owner";
    String COLUMN_FAVORITE_LENGTH = "length";
    String COLUMN_FAVORITE_VIEWS = "views";

    String TABLE_SEARCH_HISTORY = "search_history";
    String COLUMN_SEARCH_HISTORY_TABLE_ID = _ID;

    String COLUMN_SEARCH_ID = "id";
    String COLUMN_SEARCH_QUERY = "query";
}
