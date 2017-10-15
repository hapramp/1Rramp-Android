package bxute.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import bxute.FontManager;
import bxute.fcm.FirebaseDatabaseManager;

/**
 * Created by Ankit on 10/7/2017.
 */

public class OnlineIndicatorView extends android.support.v7.widget.AppCompatTextView {

    private String mOnlineSymbol = "\uF26D";

    public OnlineIndicatorView(Context context) {
        super(context);

    }

    public OnlineIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnlineIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUserId(String userId , Typeface typeface) {

        setText(mOnlineSymbol);
        this.setTypeface(typeface);
        FirebaseDatabaseManager.getUserOnlineRef(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String status = dataSnapshot.getValue().toString();
                            Log.d("__DEBUG",status);
                            int onlineSymbolColor = status.equals("Online") ? Color.parseColor("#FF2FBC04") : Color.parseColor("#bebfbd");
                            setTextColor(onlineSymbolColor);
                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
