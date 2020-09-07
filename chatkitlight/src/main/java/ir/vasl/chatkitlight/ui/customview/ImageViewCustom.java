package ir.vasl.chatkitlight.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.utils.AndroidUtils;

public class ImageViewCustom extends AppCompatImageView {

    boolean imageSquare = false;

    boolean imageRound = false;

    boolean imageAspectRatio = false;
    private Bitmap selectedImageBitmap;
    private Transformation<Bitmap> bitmapTransformation;

    public ImageViewCustom(Context context) {
        super(context);
    }

    public ImageViewCustom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        try {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewCustom, 0, 0);
            imageSquare = a.getBoolean(R.styleable.ImageViewCustom_square_image, false);
            imageRound = a.getBoolean(R.styleable.ImageViewCustom_round_image, false);
            imageAspectRatio = a.getBoolean(R.styleable.ImageViewCustom_aspect_ratio, false);
            a.recycle();
        } catch (Exception ex) {
            Log.e("tag", ex.getMessage() == null ? "null error" : ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        try {
            if (imageSquare) {
                //noinspection SuspiciousNameCombination
                setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
            } else if (imageAspectRatio) {
                // 16 * 9
                int width = getMeasuredWidth();

                //force a 16:9 aspect ratio
                int height = Math.round(width * .5625f);
                setMeasuredDimension(width, height);
            }
        } catch (Exception e) {
            Log.e("tag", "onMeasure: " + e.getMessage());
        }
    }

    public void setImageUrl(File file) {
        try {
            Glide.with(getContext())
                    .asFile()
                    .load(file)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(this);

        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrl(Uri url) {
        setImageUrl(url.toString());
    }

    public void setImageUrl(String url) {
        try {


            Glide.with(getContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrl(Bitmap bitmap) {
        try {
            Glide.with(getContext())
                    .load(bitmap)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrlRound(String url) {
        try {
            Glide.with(getContext())
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrlRound(Uri uri) {
        try {
            Glide.with(getContext())
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrlRound(String url, int placeHolder) {
        try {
            Glide.with(getContext())
                    .load(url)
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrl(String url, int placeHolder) {
        try {
            Glide.with(getContext())
                    .load(url)
                    .placeholder(placeHolder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrl(String url, boolean circle) {
        try {
            if (circle) {
                Glide.with(getContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false)
                        .centerCrop()
                        .optionalCircleCrop()
                        .into(this);
            } else {
                Glide.with(getContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false)
                        .centerCrop()
                        .into(this);
            }
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrl(int resource) {
        try {
            Glide.with(getContext())
                    .load(resource)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrlCurve(String url, int radius) {
        try {

            Glide.with(getContext())
                    .load(url)
                    .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(((int) AndroidUtils.convertDpToPixel(radius, getContext())))))
                    .placeholder(R.drawable.background_global_place_holder)
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrlCurve(int res, int radius) {
        try {
            Glide.with(getContext())
                    .load(res)
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(((int) AndroidUtils.convertDpToPixel(radius, getContext())))))
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageBitmapRound(Bitmap bitmap, int placeHolder) {
        try {
            Glide.with(getContext())
                    .load(bitmap)
                    .placeholder(placeHolder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUriRound(Uri uri, int placeHolder) {
        try {
            Glide.with(getContext())
                    .load(uri)
                    .placeholder(placeHolder)
                    .apply(RequestOptions.circleCropTransform().error(R.drawable.background_global_place_holder))
                    .into(this);

        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUriWithPlaceHolder(Uri uri, int placeHolder) {
        try {
            Glide.with(getContext())
                    .load(uri)
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrlRoundWithPlaceHolder(Uri uri, int placeHolder) {
        try {
            Bitmap placeholder = BitmapFactory.decodeResource(getResources(), placeHolder);
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), placeholder);
            circularBitmapDrawable.setCircular(true);
            Glide.with(getContext())
                    .load(uri)
                    .placeholder(circularBitmapDrawable)
                    .error(circularBitmapDrawable)
                    .apply(RequestOptions.circleCropTransform())
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrlWithPlaceHolder(String url, int placeHolder) {
        try {
            Glide.with(getContext())
                    .load(url)
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrlRoundWithPlaceHolder(String url, int placeHolder) {
        try {
            Bitmap placeholder = BitmapFactory.decodeResource(getResources(), placeHolder);
            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), placeholder);
            circularBitmapDrawable.setCircular(true);
            Glide.with(getContext())
                    .load(url)
                    .placeholder(circularBitmapDrawable)
                    .error(circularBitmapDrawable)
                    .apply(RequestOptions.circleCropTransform())
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrlCurveWithRadius(int res, int radius, int placeHolder) {
        try {
            Glide.with(getContext())
                    .load(res)
                    .centerCrop()
                    .placeholder(placeHolder)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(radius)))
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }

    public void setImageUrlCurveWithRadius(String url, int radius, int placeHolder) {
        try {
            Glide.with(getContext())
                    .load(url)
                    .centerCrop()
                    .placeholder(placeHolder)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(radius)))
                    .into(this);
        } catch (Exception e) {
            Log.e("tag", e.getMessage() == null ? "" : e.getMessage());
        }
    }


}
