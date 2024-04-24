package com.svl.myseengine;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MonetChatAdapterReworked extends RecyclerView.Adapter<MonetChatAdapterReworked.ViewHolder> {
    private final List<String> messages;
    public String MonetColor;
    public Integer MonetType;

    public MonetChatAdapterReworked(List<String> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_dialog_me, parent, false);
                break;
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_diolg, parent, false);
                break;
            case 101:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.speakertext, parent, false);
                break;
            case 202:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image, parent, false);
                break;

            case 303:
                if (MonetType==0){
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monet_first_button, parent, false);
                }
                else if (MonetType==1){
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monet_middle_button, parent, false);
                }
                else if (MonetType==2){
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monet_end_button, parent, false);
                }
                else {
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.monet_one_button, parent, false);
                }
                break;

            case 304:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_button_disabled, parent, false);
                break;
            case 1001:
            case 1002:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_roundedvideo_other, parent, false);
                break;

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_dialog, parent, false);
                break;
        }

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String messageStr = messages.get(position);

        ArrayList<String> VideoMessages = new ArrayList<>();
        boolean PlayThis=false;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).contains("#!(rounded_video)")) {
                VideoMessages.add(messages.get(i));
                if (position == messages.size()-1){
                    PlayThis=true;
                }
            }
        }
        String[] parts = messageStr.split("\\|");
        int avatar = Integer.parseInt(parts[0]);

        String sender = parts[1];
        String message = parts[2];
        boolean isMe = parts[3].equals("1");
        boolean isReader = parts[3].equals("0");
        boolean isEmptyness = parts[3].equals("101");
        boolean isChoice = parts[3].equals("303");
        boolean isChoice2 = parts[3].equals("304");

        holder.avatarImageView.setImageResource(avatar);
        holder.senderTextView.setText(sender);
        holder.messageTextView.setText(message);

        // Если отправитель текущий пользователь, то текст сообщения должен быть справа
        if (isEmptyness) {
            holder.messageTextView.setGravity(Gravity.CENTER);
        } else {

            try{holder.senderTextView.setTextColor(Integer.parseInt(MonetColor));}
            catch(Exception i) {
                Log.e("TextPainting", String.valueOf(i));}

            if (!isMe) {
                holder.senderTextView.setGravity(Gravity.START);
                holder.messageTextView.setGravity(Gravity.START);
            }
            if (isReader) {
                holder.messageTextView.setGravity(Gravity.CENTER);
            }
            if (isChoice || isChoice2) {
                holder.choise_button.setText(sender);
            }

            if (isChoice) {
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
                    drawable.setStroke(5, Integer.parseInt(MonetColor));

                    holder.choise_button.setBackground(drawable2);
                    holder.choise_button.setForeground(drawable);



                }catch (Exception e){Log.d("Exeption", String.valueOf(e));}
                }
        }

        if (parts[3].equals("1001") || parts[3].equals("1002")) {
            holder.video.setVideoURI(Uri.parse("android.resource://com.svl.myseengine/"+parts[0]));
            if (PlayThis) {
                holder.video.start();
                holder.video.setTag("false");
            } else {
                holder.video.seekTo(1);
            }
            if (parts[3].equals("1002")){
                CardView cardView = holder.RoundedCard;
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                cardView.setLayoutParams(layoutParams);
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
        public VideoView video;
        public CardView RoundedCard;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatar_image_view);
            senderTextView = itemView.findViewById(R.id.sender_text_view);
            messageTextView = itemView.findViewById(R.id.message_text_view);
            choise_button = itemView.findViewById(R.id.choice_button);
            video = itemView.findViewById(R.id.RoundedVideoView);
            RoundedCard = itemView.findViewById(R.id.RoundedCardID);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String messageStr = messages.get(position);
        String[] parts = messageStr.split("\\|");
        String isMe = parts[3];

        try{MonetType = Integer.parseInt(parts[5]);}catch(Exception ignored){MonetType=0;}

        try {
            MonetColor = parts[4];
        }catch (Exception e){Log.e("MonetColorError", String.valueOf(e)); Log.d("messageStr", messageStr);}


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
                                if (isMe.equals("1001") || isMe.equals("1002")) {
                                    return 1001;
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
}
