package com.renrenche.fabexpanablemenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.renrenche.filter.R;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by jzkcan on 2016/3/19.
 */
public class FabExpandableMenu extends RelativeLayout implements Observer {

    private LinearLayout menuContainer;
    private FloatingActionButton fabSwitcher;
    private MenuAdapter adapter;
    private OnItemClickListener listener;
    private boolean hasMenuOpened;
    private boolean isAnimating;
    private boolean isSwitcherShow = true;
    private int animDelayTotalTime;
    private int animDuration = 100;
    private int animDelay = 40;

    private OnClickListener touchListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isMenuOpen()) {
                //outside touch
                menuClose();
            }
        }
    };

    public FabExpandableMenu(Context context) {
        this(context, null, 0);
    }

    public FabExpandableMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FabExpandableMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FabExpandableMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        setBackgroundColor(Color.WHITE);
        getBackground().setAlpha(0);

        //Switcher
        fabSwitcher = new FloatingActionButton(getContext());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            fabSwitcher.setElevation(0);
        }
        fabSwitcher.setId(R.id.expandable_switcher_id);
        //TODO  setBackgroundTintList
        fabSwitcher.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
        addView(fabSwitcher);
        final LayoutParams switcherRlp = (LayoutParams) fabSwitcher.getLayoutParams();
        switcherRlp.addRule(ALIGN_PARENT_BOTTOM);
        switcherRlp.addRule(ALIGN_PARENT_RIGHT);

        //Container
        menuContainer = new LinearLayout(getContext());
        menuContainer.setOrientation(LinearLayout.VERTICAL);
        addView(menuContainer);
        LayoutParams containerRlp = (LayoutParams) menuContainer.getLayoutParams();
        containerRlp.addRule(ABOVE, R.id.expandable_switcher_id);
        containerRlp.addRule(ALIGN_RIGHT, R.id.expandable_switcher_id);

        fabSwitcher.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabSwitcher.getViewTreeObserver().removeOnPreDrawListener(this);
                //layout
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    switcherRlp.topMargin = -dpToPixel(10);
                } else {
                    switcherRlp.bottomMargin = dpToPixel(16);
                    switcherRlp.topMargin = dpToPixel(8);
                    switcherRlp.rightMargin = dpToPixel(16);
                    switcherRlp.leftMargin = dpToPixel(8);
                }
                return true;
            }
        });
        fabSwitcher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnimating) {
                    isAnimating = true;
                    if (!hasMenuOpened) {
                        hasMenuOpened = true;
                        menuOpen();
                        setOnClickListener(touchListener);
                    } else {
                        menuClose();
                    }
                }
            }
        });
    }

    public void menuOpen() {
        if (adapter == null || adapter.getCount() == 0) {
            return;
        }
        if (menuContainer.getChildCount() == 0) {
            final int menuImtCount = adapter.getCount();
            for (int i = 0; i < menuImtCount; i++) {
                menuContainer.addView(generateItem(adapter.getTitle(i), adapter.getSrcId(i)));
            }
            menuContainer.getChildAt(menuImtCount - 1).getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    menuContainer.getChildAt(menuImtCount - 1).getViewTreeObserver().removeOnPreDrawListener(this);
                    open();
                    return true;
                }
            });
        } else {
            open();
        }
    }

    private void setLayerTypeHardware(View view) {
        if (view.isHardwareAccelerated()) {
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    public void menuClose() {
        if (!hasMenuOpened) {
            return;
        }
        final int menuImtCount = adapter.getCount();
        fabSwitcher.animate().rotation(0).setDuration((menuImtCount - 1) * animDelay + animDuration).setInterpolator(new DecelerateInterpolator()).start();
        backgroundAlphaAnim(200, 0, (menuImtCount - 1) * animDelay + animDuration);
        for (int i = 0; i <= menuImtCount - 1; i++) {
            final int index = i;
            View childAtT = ((ViewGroup) menuContainer.getChildAt(i)).getChildAt(2);
            setLayerTypeHardware(childAtT);
            childAtT.animate().scaleX(0).scaleY(0).setDuration(animDuration).setStartDelay(i * animDelay).setInterpolator(new DecelerateInterpolator()).start();

            View childAtO = ((ViewGroup) menuContainer.getChildAt(i)).getChildAt(1);
            setLayerTypeHardware(childAtO);
            childAtO.animate().translationY(60).setDuration(animDuration).setStartDelay(i * animDelay).setInterpolator(new DecelerateInterpolator()).start();
            childAtO.animate().alpha(0).setDuration(animDuration).setStartDelay(i * animDelay).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (index == menuImtCount - 1) {
                        close();
                    }
                }
            }).start();
        }
    }

    public void setSwitcherSrcId(int srcId) {
        fabSwitcher.setImageResource(srcId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setAdapter(MenuAdapter adapter) {
        this.adapter = adapter;
        if (adapter != null && adapter.getCount() > 0) {
            adapter.addObserver(this);
            animDelayTotalTime = animDelay * (adapter.getCount() - 1);
        }
    }

    public void hideSwitcher() {
        if (isSwitcherShow) {
            fabSwitcher.animate().translationY(fabSwitcher.getHeight() + dpToPixel(16)).setInterpolator(new AccelerateInterpolator(2)).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    isSwitcherShow = false;
                }
            }).start();
        }
    }

    public void showSwitcher() {
        if (!isSwitcherShow) {
            fabSwitcher.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    isSwitcherShow = true;
                }
            }).start();
        }
    }

    public boolean isSwitcherShow() {
        return isSwitcherShow;
    }

    public boolean isMenuOpen() {
        return hasMenuOpened;
    }

    @Override
    public void update(Observable observable, Object data) {
        int count = (int) data;
        animDelayTotalTime = animDelay * (count - 1);
    }

    private View generateItem(final String title, int srcId) {

        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //menu fab
        FloatingActionButton fab = new FloatingActionButton(getContext());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            fab.setElevation(0);
        }
        if (srcId > 0) {
            fab.setImageResource(srcId);
        }
        //TODO  setBackgroundTintList
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuClose();
                //close();
                if (listener != null) {
                    listener.onItemClick(title);
                }
            }
        });
        ViewCompat.setScaleY(fab, 0);
        ViewCompat.setScaleX(fab, 0);

        //menu title
        TextView textView = new TextView(getContext());
        textView.setBackgroundColor(getResources().getColor(R.color.orange));
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        textView.setText(title);
        textView.setPadding(20, 15, 20, 15);
        ViewCompat.setAlpha(textView, 0);
        ViewCompat.setTranslationY(textView, 60);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuClose();
                //close();
                if (listener != null) {
                    listener.onItemClick(title);
                }
            }
        });

        View space = new View(getContext());

        linearLayout.addView(space);
        linearLayout.addView(textView);
        linearLayout.addView(fab);

        LinearLayout.LayoutParams llpSpace = (LinearLayout.LayoutParams) space.getLayoutParams();
        llpSpace.weight = 1;
        llpSpace.width = 0;
        llpSpace.height = 1;

        LinearLayout.LayoutParams llpTxt = (LinearLayout.LayoutParams) textView.getLayoutParams();
        llpTxt.gravity = Gravity.CENTER_VERTICAL;

        LinearLayout.LayoutParams llpFab = (LinearLayout.LayoutParams) fab.getLayoutParams();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            llpFab.bottomMargin = -dpToPixel(9);
            llpFab.topMargin = -dpToPixel(9);
        } else {
            llpFab.bottomMargin = dpToPixel(8);
            llpFab.topMargin = dpToPixel(8);
            llpFab.leftMargin = dpToPixel(8);
        }

        return linearLayout;
    }

    private void open() {
        int menuImtCount = adapter.getCount();
        fabSwitcher.animate().rotation(90).setDuration((menuImtCount - 1) * animDelay + animDuration).setInterpolator(new DecelerateInterpolator()).start();
        backgroundAlphaAnim(0, 200, (menuImtCount - 1) * animDelay + animDuration);
        for (int i = menuImtCount - 1; i >= 0; i--) {
            final int index = i;
            View childAtT = ((ViewGroup) menuContainer.getChildAt(i)).getChildAt(2);
            setLayerTypeHardware(childAtT);
            childAtT.animate().scaleX(1).scaleY(1).setDuration(animDuration).setStartDelay(animDelayTotalTime - i * animDelay).setInterpolator(new DecelerateInterpolator()).start();

            View childAtO = ((ViewGroup) menuContainer.getChildAt(i)).getChildAt(1);
            setLayerTypeHardware(childAtO);
            childAtO.animate().translationY(0).setDuration(animDuration).setStartDelay(animDelayTotalTime - i * animDelay).setInterpolator(new DecelerateInterpolator()).start();
            childAtO.animate().alpha(1).setDuration(animDuration).setStartDelay(animDelayTotalTime - i * animDelay).setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (index == 0) {
                        isAnimating = false;
                        ViewCompat.setRotation(fabSwitcher, 90);
                    }
                }
            }).start();
        }
    }

    private void close() {
        isAnimating = false;
        hasMenuOpened = false;
        //menuContainer.removeAllViews();
        getBackground().setAlpha(0);
        ViewCompat.setRotation(fabSwitcher, 0);
        setClickable(false);
    }

    private int dpToPixel(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }

    public interface OnItemClickListener {
        void onItemClick(String title);
    }

    public static class MenuAdapter extends Observable {

        private String[] titles;
        private int[] srcIds;

        public MenuAdapter(String[] titles) {
            this(titles, null);
        }

        public MenuAdapter(String[] titles, int[] srcIds) {
            this.titles = titles;
            this.srcIds = srcIds;
        }

        public String getTitle(int position) {
            return titles[position];
        }

        public int getSrcId(int position) {
            return srcIds == null ? -1 : srcIds[position];
        }

        public int getCount() {
            if (titles == null) {
                return 0;
            }
            if (srcIds == null) {
                return titles.length;
            }
            //TODO throw exception
            return Math.min(titles.length, srcIds.length);
        }

        public void refresh(String[] titles) {
            refresh(titles, null);
        }

        public void refresh(String[] titles, int[] srcIds) {
            this.titles = titles;
            this.srcIds = srcIds;
            setChanged();
            notifyObservers(getCount());
        }
    }

    private void backgroundAlphaAnim(int start, int end, long duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end).setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getBackground().setAlpha((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }
}
