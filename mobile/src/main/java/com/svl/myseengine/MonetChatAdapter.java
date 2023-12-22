package com.svl.myseengine;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MonetChatAdapter extends RecyclerView.Adapter<MonetChatAdapter.ViewHolder> {

    private List<String> messages;
    private boolean isMe = false;
    private boolean isReader = false;
    private boolean isChoice = false;
    private boolean isChoice2 = false;
    private boolean isEmptyness = false;



    int avatar;

    private int MonetType;
    private int MonetColor;

    public MonetChatAdapter(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        // определяем, какой макет использовать в зависимости от viewType
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_dialog_me, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_dialog, parent, false);
                break;
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_diolg, parent, false);
                break;
            case 101:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.speakertext, parent, false);
                break;


            //Spawning Image
            case 202:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
                break;

            case 303:
                Log.v("BtnsIDheker", String.valueOf(MonetType));
                if (MonetType==0){

                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monet_first_button, parent, false);
                }
                else if (MonetType==1){
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monet_middle_button, parent, false);
                }
                else if (MonetType==2){
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monet_end_button, parent, false);
                }

                else if (MonetType==3){
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monet_one_button, parent, false);
                }


                break;
            case 304:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_button_disabled, parent, false);
                break;


            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_dialog, parent, false);
                break;
        }

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String messageStr = messages.get(position);
        String[] parts = messageStr.split("\\|");

        avatar = Integer.parseInt(parts[0]);

        String sender = parts[1];
        String message = parts[2];
        isMe = parts[3].equals("1");
        isReader = parts[3].equals("0");
        isEmptyness = parts[3].equals("101");
        isChoice = parts[3].equals("303");
        isChoice2 = parts[3].equals("304");




        try {
            holder.avatarImageView.setImageResource(avatar);
        } catch (Exception ignored) {}

        holder.senderTextView.setText(sender);
        holder.messageTextView.setText(message);

        // Если отправитель текущий пользователь, то текст сообщения должен быть справа

        if (isEmptyness) {
            holder.messageTextView.setGravity(Gravity.CENTER);
        } else {

            if (isMe) {
                holder.senderTextView.setTextColor(MonetColor);
            } else {
                holder.senderTextView.setTextColor(MonetColor);
                holder.senderTextView.setGravity(Gravity.START);
                holder.messageTextView.setGravity(Gravity.START);
            }
            if (isReader) {
                holder.senderTextView.setTextColor(MonetColor);
                holder.messageTextView.setGravity(Gravity.CENTER);
            }

            if (isChoice2) {holder.choise_button.setText(sender);}

            if (isChoice) {
                holder.choise_button.setText(sender);
                try {
                    GradientDrawable drawable = new GradientDrawable();
                    GradientDrawable drawable2 = new GradientDrawable();
                    drawable2.setShape(GradientDrawable.RECTANGLE);
                    drawable2.setColor(Color.TRANSPARENT);
                    drawable.setShape(GradientDrawable.RECTANGLE);

                    if (MonetType==0) {
                        drawable.setCornerRadii(new float[]{20, 20, 20, 20, 0, 0, 0, 0});
                    }
                    if (MonetType==1) {
                        drawable.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
                    }
                    if (MonetType==2) {
                        drawable.setCornerRadii(new float[]{0, 0, 0, 0, 20, 20, 20, 20});
                    }
                    if (MonetType==3){
                        drawable.setCornerRadii(new float[]{20, 20, 20, 20, 20, 20, 20, 20});
                    }


                    drawable.setSize(0, 0);
                    drawable.setStroke(5, MonetColor);

                    holder.choise_button.setBackground(drawable2);
                    holder.choise_button.setForeground(drawable);



                }catch (Exception e){Log.d("Exeption", String.valueOf(e));}
            }
        }



    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView avatarImageView;
        public TextView senderTextView;
        public TextView messageTextView;
        public Button choise_button;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatar_image_view);
            senderTextView = itemView.findViewById(R.id.sender_text_view);
            messageTextView = itemView.findViewById(R.id.message_text_view);
            choise_button = itemView.findViewById(R.id.choice_button);
        }
    }

    @Override
    public int getItemViewType(int position) {

        String messageStr = messages.get(position);

        String[] parts = messageStr.split("\\|");
        Log.d("CommandUnpack",messageStr+"                  ==="+(parts[3])+"\\"+parts[4]);
        String isMe = parts[3];
        Log.d("String IsME", isMe);

        try {
            MonetType = Integer.parseInt(parts[4]);
            Log.d("MonetUnpak", String.valueOf(Integer.parseInt(parts[4])));
            MonetColor= Integer.parseInt(parts[5]);
            if (MonetType==0){
                MonetColor=Integer.parseInt(parts[5]);
            }
        }catch (Exception ignored){}

        if (isMe.equals("0")) {
            return 0; // возвращаем значение 1, если сообщение отправлено нами
        } else {

            if (isMe.equals("1")) {
                return 1; // возвращаем значение 0, если сообщение отправлено другим пользователем
            } else {

                if (isMe.equals("-1") || isMe.equals("101")) {
                    return 101; // возвращаем значение -1, если в сообщении нужен только текст
                } else {

                    if (isMe.equals("-2") || isMe.equals("202")) {
                        return 202; // возвращаем значение -2, если нужно изображение
                    } else {

                        if (isMe.equals("-3") || isMe.equals("303")) {
                            return 303; // возвращаем значение -3, если нужно предоставить выбор
                        } else {
                            if (isMe.equals("-34") || isMe.equals("304")) {
                                return 304; // возвращаем значение -304, если нужно предоставить выбор
                            } else {

                                return 0;
                            }

                        }
                    }
                }
            }

        }

    }
}