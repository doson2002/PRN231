package com.example.prn231.DTO;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class VolleyMultipartRequest extends Request<String> {
    private final Response.Listener<String> mListener;
    private final Map<String, DataPart> mFileParts = new HashMap<>();
    private final Map<String, String> mStringParts = new HashMap<>();

    public VolleyMultipartRequest(int method, String url, Response.Listener<String> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    protected abstract Map<String, DataPart> getByteData();

    @Override
    protected Map<String, String> getParams() {
        return mStringParts;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            for (Map.Entry<String, String> entry : mStringParts.entrySet()) {
                bos.write(("--" + BOUNDARY + "\r\n").getBytes());
                bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n").getBytes());
                bos.write((entry.getValue() + "\r\n").getBytes());
            }

            for (Map.Entry<String, DataPart> entry : mFileParts.entrySet()) {
                DataPart dataPart = entry.getValue();
                bos.write(("--" + BOUNDARY + "\r\n").getBytes());
                bos.write(("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + dataPart.fileName + "\"\r\n").getBytes());
                bos.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes());
                bos.write(dataPart.content);
                bos.write("\r\n".getBytes());
            }

            bos.write(("--" + BOUNDARY + "--\r\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String jsonResponse = null;
        try {
            jsonResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return Response.success(jsonResponse, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    public void addFilePart(String key, File file) {
        mFileParts.put(key, new DataPart(file.getName(), readFile(file)));
    }

    public void addStringPart(String key, String value) {
        mStringParts.put(key, value);
    }

    private byte[] readFile(File file) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        try (FileInputStream fis = new FileInputStream(file)) {
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    public static class DataPart {
        String fileName;
        byte[] content;

        public DataPart(String fileName, byte[] content) {
            this.fileName = fileName;
            this.content = content;
        }
    }

    private static final String BOUNDARY = "VolleyBoundary";
}
