package com.example.prn231.Services;
import static androidx.core.content.ContentProviderCompat.requireContext;

import android.net.Uri;
import android.widget.Toast;

import com.example.prn231.Api.ApiService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

