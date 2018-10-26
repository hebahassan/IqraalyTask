package com.example.heba.iqraalytask.ui.book;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.heba.iqraalytask.R;
import com.example.heba.iqraalytask.databinding.ActivityBookBinding;
import com.example.heba.iqraalytask.helper.Const;
import com.example.heba.iqraalytask.helper.LocalHelper;
import com.example.heba.iqraalytask.model.Data;

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
                }
            }
        });
    }
}
