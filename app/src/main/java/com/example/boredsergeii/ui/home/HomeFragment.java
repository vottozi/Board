package com.example.boredsergeii.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.boredsergeii.databinding.FragmentHomeBinding;
import com.example.boredsergeii.models.ModelDo;
import com.example.boredsergeii.remote_data.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setUpOnBackPressed();

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnGo.setOnClickListener(v -> {

            RetrofitBuilder.getInstance().getActivities().enqueue(new Callback<ModelDo>() {
                @Override
                public void onResponse(Call<ModelDo> call, Response<ModelDo> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        binding.activity.setText(response.body().getActivity());
                        binding.price.setText(response.body().getPrice() + " dollars");
                        binding.link.setText(response.body().getLink());
                        binding.category.setText(response.body().getType());
                        binding.participants.setText(String.valueOf(response.body().getParticipants()));
                    }
                }

                @Override
                public void onFailure(Call<ModelDo> call, Throwable throwable) {
                    Toast.makeText(requireActivity(), "No data", Toast.LENGTH_SHORT).show();
                }
            });
        });

        binding.zoomBtn.setOnClickListener(v1 -> {
            if (binding.link.getText().toString() != null) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(binding.link.getText().toString()));
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(requireActivity(), "Link is not", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setUpOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (isEnabled()) {
                            requireActivity().finish();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}