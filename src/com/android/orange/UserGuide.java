package com.android.orange;

import com.android.orange.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

public class UserGuide extends HomeActivity
{
    private static final int STOPSPLASH = 0;

    /**
     * Default duration for the splash screen (milliseconds)
     */
    private static final long SPLASHTIME = 5000;

    /**
     * Handler to close this activity and to start automatically {@link MainActivity}
     * after <code>SPLASHTIME</code> seconds.
     */
    private final transient Handler splashHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
        	if (msg.what == STOPSPLASH)
            {
                final Animation animation = AnimationUtils.loadAnimation(getBaseContext(),
                                                                         android.R.anim.slide_out_right);
                animation.setAnimationListener(new AnimationListener()
                {
                    public void onAnimationEnd(Animation animation)
                    {
                        ((LinearLayout)findViewById(R.id.splash)).setVisibility(View.INVISIBLE);
                        final Intent intent = new Intent(UserGuide.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    public void onAnimationRepeat(Animation animation)
                    {
                        // nothing to do ...
                    }

                    public void onAnimationStart(Animation animation)
                    {
                        // nothing to do ...
                    }
                });

                ((LinearLayout)findViewById(R.id.splash)).startAnimation(animation);
            }

            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userguide);

        final Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }
}