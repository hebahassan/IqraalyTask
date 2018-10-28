package com.example.heba.iqraalytask.ui.book;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.heba.iqraalytask.R;
import com.example.heba.iqraalytask.databinding.ActivityBookBinding;
import com.example.heba.iqraalytask.network.model.Data;
import com.example.heba.iqraalytask.ui.audio.BookAudioActivity;

public class BookActivity extends AppCompatActivity {
    ActivityBookBinding binding;
    BookViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_book);
        viewModel = ViewModelProviders.of(this).get(BookViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        viewModel.getBookData().observe(this, new Observer<Data>() {
            @Override
            public void onChanged(@Nullable Data data) {
                if(data != null){
                    binding.setData(data);
                    binding.toolbarLayout.title.setText(data.getBook().getName());

                    openAudioActivity();
                }
            }
        });
    }

    private void openAudioActivity(){
        binding.BTPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, BookAudioActivity.class);
                intent.putExtra("Book", binding.getData().getBook());
                context.startActivity(intent);
            }
        });
    }
}
