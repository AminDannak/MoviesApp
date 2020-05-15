package ir.amindannak.movies.ui.shared;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ir.amindannak.movies.R;

public class NoInternetConnectionDialog extends DialogFragment {

    private InternetDisconnectionRetryHandler retryHandler;

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.no_internet_dialog, null);
        dialogView.findViewById(R.id.btn_retry).setOnClickListener(
                v -> {
                    retryHandler.internetConnectionRetry();
                    this.dismiss(); // is this oK??
                });
        return builder.setView(dialogView).create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            retryHandler = (InternetDisconnectionRetryHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement "
                    + InternetDisconnectionRetryHandler.class.getSimpleName());
        }
    }

    public interface InternetDisconnectionRetryHandler {
        void internetConnectionRetry();
    }
}
