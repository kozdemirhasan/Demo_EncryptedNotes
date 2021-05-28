package com.kozdemir.encryptednotes.ui.addnote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.kozdemir.encryptednotes.databinding.FragmentAddNoteBinding;

public class AddNoteFragment extends Fragment {

    private AddNoteViewModel addNoteViewModel;
    private FragmentAddNoteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addNoteViewModel =
                new ViewModelProvider(this).get(AddNoteViewModel.class);

        binding = FragmentAddNoteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textLogout;
        addNoteViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}