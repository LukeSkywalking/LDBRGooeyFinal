package algonquin.cst2335.myapplication;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Collection;

public class MyVolley {
    private static MyVolley instance;
    private RequestQueue requestQueue;
    private static Context context;

    private MyVolley(Context ctx) {
        context = ctx;
        requestQueue = getRequestQueue();
    }

    public static synchronized MyVolley getInstance(Context context) {
        if (instance == null) {
            instance = new MyVolley(context);
        }
        return instance;
    }

    public static Collection<JsonObjectRequest> newRequestQueue(RecipeDetailsActivity recipeDetailsActivity) {
        return null;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}