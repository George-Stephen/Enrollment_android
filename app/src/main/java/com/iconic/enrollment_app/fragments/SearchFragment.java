package com.iconic.enrollment_app.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.iconic.enrollment_app.R;

import java.io.IOException;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchFragment extends Fragment {
    @BindView(R.id.scanner) SurfaceView view;
    @BindView(R.id.test_text) TextView m_test;
    private BarcodeDetector detector;
    private CameraSource cameraSource;
    private static final int REQUEST_CODE = 201;
    private ToneGenerator generator;
    private String CodeData;



    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this,view);
        initialiseQr_code();
        return view;
    }
    private void initialiseQr_code() {
        detector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        cameraSource = new CameraSource.Builder(Objects.requireNonNull(getContext()),detector)
                .setRequestedPreviewSize(1920,1080)
                .setAutoFocusEnabled(true)
                .build();
        view.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                                cameraSource.start(view.getHolder());
                        }else {
                            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),new String[]{Manifest.permission.CAMERA},REQUEST_CODE);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });
        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes= detections.getDetectedItems();
                if (barcodes.size() != 0){
                    m_test.post(new Runnable() {
                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).email != null){
                                m_test.removeCallbacks(null);
                                CodeData = barcodes.valueAt(0).email.address;

                            } else{
                                CodeData = barcodes.valueAt(0).displayValue;
                                m_test.setText(CodeData);
                            }
                        }
                    });
                }
            }
        });

    }
}
