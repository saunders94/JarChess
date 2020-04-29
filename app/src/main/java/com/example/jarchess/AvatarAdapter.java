package com.example.jarchess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jarchess.match.styles.avatar.PlayerAvatarStyles;


public class AvatarAdapter extends ArrayAdapter<String> {

    //    private String[] avatarNameArray;
//    private int[] avatarArray;
    private final PlayerAvatarStyles[] avatars = PlayerAvatarStyles.values();
    private Context context;

    protected AvatarAdapter(Context context) {
        super(context, R.layout.avatar_item);
//        this.avatarNameArray = avatarNameArray;
//        this.avatarArray = avatarArray;
        this.context = context;

    }

    @Override
    public int getCount() {
        return avatars.length;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.avatar_item, parent, false);
            viewHolder.iView = convertView.findViewById(R.id.image_view_avatar);
            viewHolder.tView = convertView.findViewById(R.id.text_view_avatar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.iView.setImageResource(avatars[position].getAvatarStyle().getAvatarResourceID());
        viewHolder.tView.setText(avatars[position].getAvatarStyle().getName());

        return convertView;
    }

    static class ViewHolder {
        ImageView iView;
        TextView tView;
    }
}
