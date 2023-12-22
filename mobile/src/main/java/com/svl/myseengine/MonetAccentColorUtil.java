package com.svl.myseengine;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

public class MonetAccentColorUtil {

    public static int getMonetAccentColor(Context context) {
        int monetAccentColor = 0;

        try {
            // Получаем идентификатор ресурса для атрибута "monet_accent_color"
            int monetAccentColorAttr = context.getResources().getIdentifier(
                    "monet_accent_color",
                    "attr",
                    context.getPackageName()
            );

            if (monetAccentColorAttr != 0) {
                // Получаем массив атрибутов для текущей темы приложения
                TypedArray typedArray = context.obtainStyledAttributes(new int[]{monetAccentColorAttr});

                // Получаем цвет акцента Monet из атрибутов
                monetAccentColor = typedArray.getColor(0, 0);

                // Освобождаем ресурсы
                typedArray.recycle();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        return monetAccentColor;
    }
}
