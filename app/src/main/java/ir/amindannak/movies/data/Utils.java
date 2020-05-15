package ir.amindannak.movies.data;

import ir.amindannak.movies.R;
import ir.amindannak.movies.util.App;

public class Utils {

    public static String mapFailedHttpResponseCodeToMsg(int code) {

        if (code < 100 || code >= 600)
            return "invalid http response code " + code;

        int tmp = code / 100;
        if (tmp == 3 || tmp == 5)
            return App.getApp().getString(R.string.server_error_msg);
        if (tmp == 4)
            return App.getApp().getString(R.string.client_error_msg);
        return App.getApp().getString(R.string.unknown_error_msg);
    }

}
