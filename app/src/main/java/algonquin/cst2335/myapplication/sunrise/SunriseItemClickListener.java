package algonquin.cst2335.myapplication.sunrise;

import android.view.View;

public class SunriseItemClickListener implements View.OnClickListener {

    private OnItemClickListener listener;
    private int position;

    public SunriseItemClickListener(OnItemClickListener listener, int position) {
        this.listener = listener;
        this.position = position;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(position);
        }
    }
}
