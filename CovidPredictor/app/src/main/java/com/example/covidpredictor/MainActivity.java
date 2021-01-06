package com.example.covidpredictor;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
public class MainActivity extends AppCompatActivity {
    Uri myUri;
    ImageView img;
    Bitmap bitmap,mutableBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        img=(ImageView) findViewById(R.id.imageView);
        if (extras != null) {
            String imguri = extras.getString("URI");
            myUri = Uri.parse(extras.getString("URI"));
        }
        try {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(), myUri);
                img.setImageBitmap(bitmap);
            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),  myUri);
                 bitmap = ImageDecoder.decodeBitmap(source);
                img.setImageBitmap(bitmap);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        mutableBitmap = bitmap.copy(Bitmap.Config.RGBA_F16, true);
        Module module = null;
        try {

            module = Module.load(assetFilePath(this, "model.pt"));
        } catch (IOException e) {
            Log.e("PytorchHelloWorld", "Error reading assets", e);
            finish();
        }

        // showing image on UI

        float[] mean = {0.485f, 0.456f, 0.406f};
        float[] std = {0.229f, 0.224f, 0.225f};
        // preparing input tensor
        bitmap = Bitmap.createScaledBitmap(mutableBitmap,224,224,false);
        mutableBitmap = bitmap.copy(Bitmap.Config.RGBA_F16, true);
        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(mutableBitmap,mean,std);

        // running the model
        final Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();

        // getting tensor content as java array of floats
        final float[] scores = outputTensor.getDataAsFloatArray();

        // searching for the index with maximum score
        float maxScore = -Float.MAX_VALUE;
        int maxScoreIdx = -1;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxScore) {
                maxScore = scores[i];
                maxScoreIdx = i;
            }
        }

        String className = Constants.CLASSES[maxScoreIdx];

        // showing className on UI
        TextView textView = findViewById(R.id.textview);
        textView.setText(className);
    }


    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }
}