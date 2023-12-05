package algonquin.cst2335.myapplication;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.Collection;

/**
 * Singleton class for managing Volley requests in the application.
 */
public class MyVolley {

    private static MyVolley instance;
    private RequestQueue requestQueue;
    private static Context context;

    /**
     * Private constructor to ensure singleton pattern.
     *
     * @param ctx The application context.
     */
    private MyVolley(Context ctx) {
        context = ctx;
        requestQueue = getRequestQueue();
    }

    /**
     * Gets the instance of MyVolley.
     *
     * @param context The application context.
     * @return The instance of MyVolley.
     */
    public static synchronized MyVolley getInstance(Context context) {
        if (instance == null) {
            instance = new MyVolley(context);
        }
        return instance;
    }

    /**
     * Creates a new RequestQueue for Volley.
     *
     * @param recipeDetailsActivity The activity context.
     * @return A collection of JsonObjectRequests.
     */
    public static Collection<JsonObjectRequest> newRequestQueue(RecipeDetailsActivity recipeDetailsActivity) {
        return null; // Placeholder, actual implementation needed.
    }

    /**
     * Gets the RequestQueue instance or creates a new one if not exists.
     *
     * @return The RequestQueue instance.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = com.android.volley.toolbox.Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}
