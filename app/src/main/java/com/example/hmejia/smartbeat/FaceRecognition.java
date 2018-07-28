package com.example.hmejia.smartbeat;


import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Detector;
import com.affectiva.android.affdex.sdk.detector.Face;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FaceRecognition extends AppCompatActivity implements Detector.ImageListener, CameraDetector.CameraEventListener, AsyncResponse {

    Bundle bundle;
    //private TensorFlowInferenceInterface inferenceInterface;
    TextView mTextField;
    ArrayList<Float> emotions = new ArrayList<Float>();

    float smileTotal = 0;
    float joyTotal = 0;
    float angerTotal = 0;
    float disgustTotal = 0;
    float contemptTotal = 0;
    float fearTotal = 0;
    float sadnessTotal = 0;
    float surpriseTotal = 0;

    TextView joyTextView;
    TextView angerTextView;
    TextView disgustTextView;
    TextView contemptTextView;
    TextView fearTextView;
    TextView sadnessTextView;
    TextView surpriseTextView;


    TextView smileTextView;
    CameraDetector detector;
    Button startSDKButton;
    SurfaceView cameraPreview;

    boolean isCameraBack = false;
    boolean isSDKStarted = false;

    int previewWidth = 0;
    int previewHeight = 0;

    RelativeLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        //inferenceInterface = new TensorFlowInferenceInterface(getAssets(), MODEL_FILE);
        bundle = getIntent().getExtras();

        smileTextView = (TextView) findViewById(R.id.smile_textview);
        //ageTextView = (TextView) findViewById(R.id.age_textview);
        //ethnicityTextView = (TextView) findViewById(R.id.ethnicity_textview);

        joyTextView = (TextView) findViewById(R.id.joy_textview);
        angerTextView = (TextView) findViewById(R.id.anger_textview);
        disgustTextView = (TextView) findViewById(R.id.disgust_textview);
        contemptTextView = (TextView) findViewById(R.id.contempt_textview);
        fearTextView = (TextView) findViewById(R.id.fear_textview);
        sadnessTextView = (TextView) findViewById(R.id.sadness_textview);
        surpriseTextView = (TextView) findViewById(R.id.surprise_textview);

        startSDKButton = (Button) findViewById(R.id.sdk_start_button);
        startSDKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSDKStarted) {
                    isSDKStarted = false;
                    stopDetector();
                    startSDKButton.setText("Start Camera");
                } else {
                    isSDKStarted = true;
                    startDetector();
                    startSDKButton.setText("Stop Camera");

                    mTextField = (TextView)findViewById(R.id.timerTextView);
                    new CountDownTimer(5000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {

                            String prediction = "?";
                            emotions.add(smileTotal);
                            emotions.add(joyTotal);
                            emotions.add(angerTotal);
                            emotions.add(disgustTotal);
                            emotions.add(contemptTotal);
                            emotions.add(sadnessTotal);
                            emotions.add(fearTotal);
                            emotions.add(surpriseTotal);

                            float totalVectorSum = 0;
                            for (int i = 0; i < emotions.size(); i++) {
                                totalVectorSum += emotions.get(i);
                            }
                            for (int i = 0; i < emotions.size(); i++) {
                                emotions.set(i, emotions.get(i) / totalVectorSum);
                            }

                            OnSubmitData(prediction);
                            /*AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(getBaseContext());

                            builder.setTitle("Mood Prediction")
                                    .setMessage("We guess you are: " + prediction)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            String type = "submitData";

                                            BackgroundWork backgroundWorker = new BackgroundWork(getBaseContext());
                                            String temp = emotions.get(0).toString();
                                            backgroundWorker.execute(type, bundle.getString("user_name"), bundle.getString("user_password"), emotions.get(0).toString(), emotions.get(1).toString(), emotions.get(2).toString(), emotions.get(3).toString(), emotions.get(4).toString(), emotions.get(5).toString(), emotions.get(6).toString(), emotions.get(7).toString(), getCurrentDate(), getCurrentTime(),"1");

                                            //startActivity(new Intent(getBaseContext(),FaceRecognition.class));
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            String type = "submitData";

                                            BackgroundWork backgroundWorker = new BackgroundWork(getBaseContext());
                                            String temp = emotions.get(0).toString();
                                            backgroundWorker.execute(type, bundle.getString("user_name"), bundle.getString("user_password"), emotions.get(0).toString(), emotions.get(1).toString(), emotions.get(2).toString(), emotions.get(3).toString(), emotions.get(4).toString(), emotions.get(5).toString(), emotions.get(6).toString(), emotions.get(7).toString(), getCurrentDate(), getCurrentTime(),"0");
                                        }
                                    })
                                    .show();
                             */
                            smileTotal = 0;
                            joyTotal = 0;
                            fearTotal = 0;
                            disgustTotal = 0;
                            contemptTotal = 0;
                            sadnessTotal = 0;
                            fearTotal = 0;
                            surpriseTotal = 0;


                        }
                    }.start();


                }
            }
        });
        startSDKButton.setText("Start Camera");

        mainLayout = (RelativeLayout) findViewById(R.id.face_recognition);
        cameraPreview = new SurfaceView(this) {
            @Override
            public void onMeasure(int widthSpec, int heightSpec) {
                int measureWidth = MeasureSpec.getSize(widthSpec);
                int measureHeight = MeasureSpec.getSize(heightSpec);
                int width;
                int height;
                if (previewHeight == 0 || previewWidth == 0) {
                    width = measureWidth;
                    height = measureHeight;
                } else {
                    float viewAspectRatio = (float)measureWidth/measureHeight;
                    float cameraPreviewAspectRatio = (float) previewWidth/previewHeight;

                    if (cameraPreviewAspectRatio > viewAspectRatio) {
                        width = measureWidth;
                        height =(int) (measureWidth / cameraPreviewAspectRatio);
                    } else {
                        width = (int) (measureHeight * cameraPreviewAspectRatio);
                        height = measureHeight;
                    }
                }
                setMeasuredDimension(width,height);
            }
        };

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        cameraPreview.setLayoutParams(params);
        mainLayout.addView(cameraPreview,0);

        detector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraPreview);
        detector.setDetectSmile(true);
        detector.setDetectAllEmotions(true);
        detector.setDetectAge(true);
        detector.setDetectEthnicity(true);
        detector.setImageListener(this);
        detector.setOnCameraEventListener(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isSDKStarted) {
            startDetector();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopDetector();
    }

    void startDetector() {
        if (!detector.isRunning()) {
            detector.start();
        }
    }

    void stopDetector() {
        if (detector.isRunning()) {
            detector.stop();
        }
    }

    @Override
    public void onImageResults(List<Face> list, Frame frame, float v) {
        if (list == null)
            return;
        if (list.size() == 0) {
            smileTextView.setText("NO FACE");
            //ageTextView.setText("");
            //ethnicityTextView.setText("");

            joyTextView.setText("");
            angerTextView.setText("");
            disgustTextView.setText("");
            contemptTextView.setText("");
            sadnessTextView.setText("");
            fearTextView.setText("");
            surpriseTextView.setText("");

        } else {
            Face face = list.get(0);

            smileTextView.setText(String.format("SMILE\n%.2f", face.expressions.getSmile()));
            joyTextView.setText(String.format("JOY\n%.2f",face.emotions.getJoy()));
            angerTextView.setText(String.format("ANGER\n%.2f",face.emotions.getAnger()));
            disgustTextView.setText(String.format("DISGUST\n%.2f",face.emotions.getDisgust()));
            contemptTextView.setText(String.format("CONTEMPT\n%.2f",face.emotions.getContempt()));
            sadnessTextView.setText(String.format("SADNESS\n%.2f",face.emotions.getSadness()));
            fearTextView.setText(String.format("FEAR\n%.2f",face.emotions.getFear()));
            surpriseTextView.setText(String.format("SURPRISE\n%.2f",face.emotions.getSurprise()));

            smileTotal += face.expressions.getSmile();
            joyTotal += face.emotions.getJoy();
            angerTotal += face.emotions.getAnger();
            disgustTotal += face.emotions.getDisgust();
            contemptTotal += face.emotions.getContempt();
            sadnessTotal += face.emotions.getSadness();
            fearTotal += face.emotions.getFear();
            surpriseTotal += face.emotions.getSurprise();

        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public void onCameraSizeSelected(int width, int height, Frame.ROTATE rotate) {
        if (rotate == Frame.ROTATE.BY_90_CCW || rotate == Frame.ROTATE.BY_90_CW) {
            previewWidth = height;
            previewHeight = width;
        } else {
            previewHeight = height;
            previewWidth = width;
        }
        cameraPreview.requestLayout();
    }

    public void OnSubmitData(String prediction) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("We guess you are: " + prediction);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String type = "submitData";

                        BackgroundWork backgroundWorker = new BackgroundWork(getBaseContext());
                        backgroundWorker.delegate = FaceRecognition.this;
                        String temp = emotions.get(0).toString();
                        backgroundWorker.execute(type, bundle.getString("user_name"), bundle.getString("user_password"), emotions.get(0).toString(), emotions.get(1).toString(), emotions.get(2).toString(), emotions.get(3).toString(), emotions.get(4).toString(), emotions.get(5).toString(), emotions.get(6).toString(), emotions.get(7).toString(), getCurrentDate(), getCurrentTime(),"1");
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String type = "submitData";

                        BackgroundWork backgroundWorker = new BackgroundWork(getBaseContext());
                        backgroundWorker.delegate = FaceRecognition.this;
                        backgroundWorker.execute(type, bundle.getString("user_name"), bundle.getString("user_password"), emotions.get(0).toString(), emotions.get(1).toString(), emotions.get(2).toString(), emotions.get(3).toString(), emotions.get(4).toString(), emotions.get(5).toString(), emotions.get(6).toString(), emotions.get(7).toString(), getCurrentDate(), getCurrentTime(),"0");
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        //emotions.clear();
    }

    public String getCurrentDate() {
        Calendar calander = Calendar.getInstance();
        int cDay = calander.get(Calendar.DAY_OF_MONTH);
        int cMonth = calander.get(Calendar.MONTH) + 1;
        int cYear = calander.get(Calendar.YEAR);
        return cYear + "-" + cMonth + "-" + cDay;
    }

    public String getCurrentTime() {
        Calendar calander = Calendar.getInstance();
        int cHour = calander.get(Calendar.HOUR);
        int cMinute = calander.get(Calendar.MINUTE);
        int cSecond = calander.get(Calendar.SECOND);
        return cHour + "-" + cMinute + "-" + cSecond;
    }

    @Override
    public void processFinish(String output) {

    }
}

