package algonquin.cst2335.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NetworkController {

    public static void fetchData(Context context, String word, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
