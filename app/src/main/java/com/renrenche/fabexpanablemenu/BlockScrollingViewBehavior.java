package com.renrenche.fabexpanablemenu;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jzkcan on 2016/3/21.
 */
public class BlockScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior {

    private int targetIndex = -1;

    public BlockScrollingViewBehavior() {
    }

    public BlockScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean blocksInteractionBelow(CoordinatorLayout parent, View child) {
        if (targetIndex < 0) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (parent.getChildAt(i) instanceof FabExpandableMenu) {
                    targetIndex = i;
                    break;
                }
            }
        }
        FabExpandableMenu target = (FabExpandableMenu) parent.getChildAt(targetIndex);
        if (target.isMenuOpen()) {
            return true;
        }
        return getScrimOpacity(parent, child) > 0.f;
    }
}
