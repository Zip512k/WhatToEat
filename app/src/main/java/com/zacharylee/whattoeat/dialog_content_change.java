package com.zacharylee.whattoeat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zepli on 16-06-29.
 */
public class dialog_content_change extends DialogFragment {

    public static dialog_content_change newInstance(String type) {
        dialog_content_change frag = new dialog_content_change();
        Bundle args = new Bundle();
        args.putString("type", type);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        String type = getArguments().getString("type");

        View view = inflater.inflate(R.layout. dialog_change, null);
        final TextView mChangeContentTextView = (TextView) view.findViewById(R.id.changeContent);
        final TextView mChangeTitleTextView = (TextView) view.findViewById(R.id.changeTitle);

        mChangeContentTextView.setHint(type);
        mChangeTitleTextView.setText(type);

        builder.setView(view);
                // Add action buttons
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        String s = mChangeContentTextView.getText().toString();
                    }
                });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog_content_change.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
