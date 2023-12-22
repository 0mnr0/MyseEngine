package com.svl.myseengine;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class CustomAnimation {

    public static void startCustomAnimation(final Activity activity) {
        final View view = activity.findViewById(android.R.id.content);
        final ViewGroup rootView = activity.findViewById(android.R.id.content);

        // Получите размеры экрана
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int screenWidth = rect.width();
        int screenHeight = rect.height();

        // Создайте объект AnimatorSet для объединения нескольких анимаций
        AnimatorSet animatorSet = new AnimatorSet();

        // Создайте ObjectAnimator для изменения масштаба по оси X
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f);
        scaleXAnimator.setDuration(500); // Установите продолжительность анимации

        // Создайте ObjectAnimator для изменения масштаба по оси Y
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f);
        scaleYAnimator.setDuration(500); // Установите продолжительность анимации

        // Определите начальные значения для анимации масштабирования
        view.setScaleX(0f);
        view.setScaleY(0f);

        // Определите положение и размеры для анимации масштабирования
        view.setX(screenWidth);
        view.setY(screenHeight);

        // Определите итоговые значения для анимации масштабирования
        scaleXAnimator.setFloatValues(0f, 1f);
        scaleYAnimator.setFloatValues(0f, 1f);

        // Добавьте анимации в AnimatorSet
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);

        // Установите слушатель, чтобы выполнить действия после завершения анимации
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Добавьте view в корневой контейнер (FrameLayout)
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                rootView.addView(view, layoutParams);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Удалите view из корневого контейнера
                rootView.removeView(view);

                // Действия, которые нужно выполнить после окончания анимации
                // Например, запуск следующей Activity
                // Intent intent = new Intent(activity, NextActivity.class);
                // activity.startActivity(intent);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        // Запустите анимацию
        animatorSet.start();
    }
}
