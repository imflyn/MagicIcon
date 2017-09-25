package com.flyn.magicicon;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().hide();

        final ImageView bgImage = findViewById(R.id.bgImage);
        final ImageView image1 = findViewById(R.id.image1);
        final ImageView image2 = findViewById(R.id.image2);
        final ImageView bgImage2 = findViewById(R.id.bgImage2);

        int res = getIntent().getIntExtra("res", 0);
        Glide.with(this).asBitmap().load(res).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                bgImage.setImageBitmap(resource);

                int width = resource.getWidth();    //获取位图的宽
                int height = getPxFromDp(56);       //Toolbar的高度
                Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, width, height);
                bgImage2.setImageBitmap(bitmap);

                int RGBValues = getPaletteColor(bitmap); //只提取Toolbar高度图片中的主色调
                int red = Color.red(RGBValues);
                int green = Color.green(RGBValues);
                int blue = Color.blue(RGBValues);

                boolean result = isShenRGB(new int[]{red, green, blue});//判断是否为深色
                image1.setImageResource(result ? R.drawable.icon_back_white : R.drawable.icon_back_black);
                image2.setImageResource(result ? R.drawable.nav_icon_cart_white : R.drawable.nav_icon_cart_black);

//                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(RGBValues));
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public int getPxFromDp(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private boolean isShenRGB(int[] colors) {
        int grayLevel = (int) (colors[0] * 0.299 + colors[1] * 0.587 + colors[2] * 0.114);
        Log.i("grayLevel", grayLevel + "");
        return grayLevel <= 70;
    }

    public static int getPaletteColor(Bitmap bitmap) {
        int color = 0;
        Palette palette = Palette.from(bitmap).generate();
        Palette.Swatch vibrant = palette.getVibrantSwatch();
        Palette.Swatch vibrantdark = palette.getDarkVibrantSwatch();
        Palette.Swatch vibrantlight = palette.getLightVibrantSwatch();
        Palette.Swatch Muted = palette.getMutedSwatch();
        Palette.Swatch Muteddark = palette.getDarkMutedSwatch();
        Palette.Swatch Mutedlight = palette.getLightMutedSwatch();

        if (vibrant != null) {
            color = vibrant.getRgb();
        } else if (Muteddark != null) {
            color = Muteddark.getRgb();
        } else if (vibrantdark != null) {
            color = vibrantdark.getRgb();
        } else if (vibrantlight != null) {
            color = vibrantlight.getRgb();
        } else if (Mutedlight != null) {
            color = Mutedlight.getRgb();
        } else if (Muted != null) {
            color = Muted.getRgb();
        }
        return color;
    }

}
