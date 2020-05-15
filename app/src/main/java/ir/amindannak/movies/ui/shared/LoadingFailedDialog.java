package ir.amindannak.movies.ui.shared;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ir.amindannak.movies.R;

public class LoadingFailedDialog extends DialogFragment {

    public static final String KEY_MSG = "message key";
    private String msg;
    private FailedLoadingRetryHandler retryHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.loading_failed_dialog, null);
        TextView textView = dialogView.findViewById(R.id.tv_msg);
        Bundle args = getArguments();
        if (args != null && args.containsKey(KEY_MSG)) {
            textView.setText(args.getString(KEY_MSG));
        }
        dialogView.findViewById(R.id.btn_retry).setOnClickListener(v -> {
            retryHandler.failedLoadingRetry();
            this.dismiss(); // is this oK??
        });
        return builder.setView(dialogView).create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            retryHandler = (FailedLoadingRetryHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement "
                    + FailedLoadingRetryHandler.class.getSimpleName());
        }
    }

    public interface FailedLoadingRetryHandler {
        void failedLoadingRetry();
    }

}
