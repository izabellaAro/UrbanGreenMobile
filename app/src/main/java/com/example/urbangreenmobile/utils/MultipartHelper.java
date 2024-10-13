package com.example.urbangreenmobile.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.example.urbangreenmobile.ui.Produto.ProdutoActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartHelper {

    public static RequestBody createPartFromString(String value) {
        return RequestBody.create(MultipartBody.FORM, value);
    }

    public static RequestBody createPartFromInt(int value) {
        return RequestBody.create(MultipartBody.FORM, String.valueOf(value));
    }

    public static RequestBody createPartFromDouble(double value) {
        return RequestBody.create(MultipartBody.FORM, String.valueOf(value));
    }

    public static Bitmap base64ToBitmap(String base64Str) {
        byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static MultipartBody.Part prepararArquivoImagem(Bitmap bitmap, ProdutoActivity context) {
        if (bitmap == null) return null;

        File file = new File(context.getCacheDir(), "imagem_produto.png");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        RequestBody requestFile = RequestBody.create(okhttp3.MediaType.parse("image/png"), file);
        return MultipartBody.Part.createFormData("imagem", file.getName(), requestFile);
    }

}
