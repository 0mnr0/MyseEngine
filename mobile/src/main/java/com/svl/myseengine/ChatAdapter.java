package com.svl.myseengine;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.svl.myseengine.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<String> messages;
    private boolean isMe = false;
    private boolean isReader = false;
    private boolean isChoice = false;
    private boolean isChoice2 = false;
    private boolean isEmptyness = false;

    public ChatAdapter(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        // определяем, какой макет использовать в зависимости от viewType
        Log.w("VuiewTyttpe", String.valueOf(viewType));
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_button, parent, false);
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
        Log.w("OutMessage",messageStr);
        String[] parts = messageStr.split("\\|");
        int avatar = Integer.parseInt(parts[0]);

        String sender = parts[1];
        String message = parts[2];
        Log.d("parts[3].equals('0')", String.valueOf(parts[3].equals("0")));
        isMe = parts[3].equals("1");
        isReader = parts[3].equals("0");
        isEmptyness = parts[3].equals("101");
        isChoice = parts[3].equals("303");
        isChoice2 = parts[3].equals("304");

        holder.avatarImageView.setImageResource(avatar);
        holder.senderTextView.setText(sender);
        holder.messageTextView.setText(message);

        // Если отправитель текущий пользователь, то текст сообщения должен быть справа
        if (isEmptyness) {
            holder.messageTextView.setGravity(Gravity.CENTER);
        } else {

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
        String isMe = parts[3];
        Log.d("String IsME", isMe);
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