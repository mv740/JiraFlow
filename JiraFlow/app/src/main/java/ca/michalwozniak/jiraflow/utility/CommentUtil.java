package ca.michalwozniak.jiraflow.utility;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Michal Wozniak on 9/20/2016.
 */

public class CommentUtil {

    /**
     * Display a Comment dialog. It will add the comment to the selected issue.
     *
     * @param context
     * @param key
     */
    public static void showCommentDialog(Context context, String key)
    {

        String title = "Comment on "+ key;

        Dialog commentDialog = new MaterialDialog.Builder(context)
                .title(title)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText("Add")
                .negativeText("Cancel")
                .input("Write your comment...", null, (dialog, input) -> {

                    NetworkManager.getInstance(context).addComment(key, input.toString())
                            .subscribe(commentResponse -> {
                                        Log.d("success", commentResponse.getCreated());
                                    }
                            );

                }).inputRange(1, -1) // not empty, no max size
                .build();

        commentDialog.show();
    }
}
