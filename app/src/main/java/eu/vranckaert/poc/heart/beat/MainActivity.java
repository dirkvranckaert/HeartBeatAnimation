package eu.vranckaert.poc.heart.beat;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private View mImage;
    private Animator mBeatingAnimation;
    private boolean mBeating = false;
    private int mCurrentBpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImage = findViewById(R.id.image);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.bpm_35).setOnClickListener(this);
        findViewById(R.id.bpm_75).setOnClickListener(this);
        findViewById(R.id.bpm_95).setOnClickListener(this);
        findViewById(R.id.bpm_130).setOnClickListener(this);

        setBPM(75);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button) {
            mBeating = !mBeating;
            if (mBeating) {
                playHeartBeat(mCurrentBpm);
            }
        } else if (id == R.id.bpm_35) {
            setBPM(35);
        } else if (id == R.id.bpm_75) {
            setBPM(75);
        } else if (id == R.id.bpm_95) {
            setBPM(95);
        } else if (id == R.id.bpm_130) {
            setBPM(130);
        }
    }

    private void setBPM(int bpm) {
        if (bpm == 0) {
            bpm = 1;
        }
        mCurrentBpm = bpm;
        ((TextView) findViewById(R.id.current_bpm)).setText("Current BPM: " + mCurrentBpm);
    }

    private void playHeartBeat(int bpm) {
        if (mBeatingAnimation != null) {
            mBeatingAnimation.cancel();
            mBeatingAnimation = null;
        }

        long beatDuration = 60000/bpm;

        AnimatorSet animatorSet = new AnimatorSet();
        final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mImage, "scaleX", 1.4f, 1f);
        final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mImage, "scaleY", 1.4f, 1f);
        animatorSet.play(scaleXAnimator).with(scaleYAnimator);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setDuration(beatDuration);
        animatorSet.addListener(new AnimatorListener() {
            private boolean cancelled = false;
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!cancelled && mBeating) {
                    playHeartBeat(mCurrentBpm);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                cancelled = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
        mBeatingAnimation = animatorSet;
    }
}
