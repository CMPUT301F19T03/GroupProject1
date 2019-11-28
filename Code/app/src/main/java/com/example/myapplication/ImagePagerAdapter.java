package com.example.myapplication;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImagePagerAdapter extends PagerAdapter {
    private Context context;


    public ImagePagerAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getCount() {
        return context.getResources().getStringArray(R.array.emotes).length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Resources res = context.getResources();
        ImageView imageView = new ImageView(context);
        String curr = res.getStringArray(R.array.emotes)[position];

        int padding = res.getDimensionPixelSize(R.dimen.padding_medium);
        imageView.setPadding(padding,padding,padding,padding);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int id = res.getIdentifier(curr,"drawable",context.getPackageName());
        imageView.setImageResource(id);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

}
