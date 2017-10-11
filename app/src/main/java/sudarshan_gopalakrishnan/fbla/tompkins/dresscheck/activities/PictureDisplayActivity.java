package sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.DressCheckApplication;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.R;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.Direction;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.OrderNumberGenerator;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.BitmapFilter;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.MyLog;
import sudarshan_gopalakrishnan.fbla.tompkins.dresscheck.datatypes.SwipeListener;


public class PictureDisplayActivity extends Activity {


    private String[] questions;

    private String[] fashionQuestions = {"Ask My Own Question \n(Press Next To Ask A Question)\n(Swipe left or Right for more questions!)",
            "Do these colors go well with each other?",
            "Is this tie good to wear?",
            "Does this shirt appear classy to wear?",
            "What color blazer matches this pair of pants?",
            "Does this tie look trendy?",
            "Does this garment show current fashion?"};

    private String[] attireQuestions = {"Ask My Own Question \n(Press Next To Ask A Question)\n(Swipe left or Right for more questions!)",
            "Do I look stylish?",
            "Is my outfit professional?",
            "Is my outfit within FBLA-PBL Dress Code?",
            "Do I look too flashy?",
            "Do I look too dull?",
            "Is my attire too tight?",
            "Is my attire too loose?",
            "Is my attire of perfect fit?",
            "Is my dress too colorful?",
            "Is my makeup too gaudy?",
            "Do my jewelry match with my attire?",
            "Do my shoes match my overall clothing?",
            "Am I wearing too many accessories?",
            "Does my hairstyle go with the dress?",
            "Should I wear glasses or contacts?",
            "How do I improve my outfit?"};

    private int questionIndex = 0;
    private String currentQuestionText = "";
    private TextView questionHolder;
    private Button nextPageButton;
    private ImageButton arrowLeft, arrowRight;
    private GestureDetector gestureDetector, gestureDetectorImageDisplay;
    private ImageView imageDisplay;
    private OrderNumberGenerator filterSequence, questionSequence;
    private Switch switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_picture_display);

        questions = attireQuestions;

        switchButton = (Switch) findViewById(R.id.postSwitch);
        switchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE;
            }
        });
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchButton.isChecked()){
                    questions = attireQuestions;
                    questionIndex = 0;
                    questionSequence = new OrderNumberGenerator(questions.length);
                    refreshText();
                }else{
                    questions = fashionQuestions;
                    questionIndex = 0;
                    questionSequence = new OrderNumberGenerator(questions.length);
                    refreshText();
                }
            }
        });

        filterSequence = new OrderNumberGenerator(BitmapFilter.TOTAL_FILTER_NUM);
        questionSequence = new OrderNumberGenerator(questions.length);


        questionHolder = (TextView) findViewById(R.id.spacer4);
        if(questionIndex != 0)
            questionHolder.setText("<\t\t" + questions[questionIndex] + "\t\t>");
        else
            questionHolder.setText("<\t\t"+"Ask My Own Question"+"\t\t>\n"+"(Press Next To Ask)\n(Swipe left or Right for more questions!)");
        questionHolder.setTextColor(Color.parseColor("white"));


        currentQuestionText = questions[questionIndex];

        nextPageButton = (Button) findViewById(R.id.nextPageButton);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentQuestionText.equals(questions[0])) {
                    LayoutInflater li = LayoutInflater.from(PictureDisplayActivity.this);
                    View promptsView = li.inflate(R.layout.layout_dialog_box_question, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PictureDisplayActivity.this);
                    alertDialogBuilder.setView(promptsView);
                    final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                    alertDialogBuilder
                            .setCancelable(true)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            questionHolder.setText(userInput.getText());
                                            currentQuestionText = userInput.getText().toString();

                                            Intent i = new Intent(getApplicationContext(), PeopleToSendSubactivity.class);
                                            i.putExtra("messageToPost", currentQuestionText);
                                            i.putExtra("isAttire", switchButton.isChecked());
                                            startActivity(i);
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            finish();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Intent i = new Intent(getApplicationContext(), PeopleToSendSubactivity.class);
                    i.putExtra("messageToPost", currentQuestionText);
                    i.putExtra("isAttire", switchButton.isChecked());
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }


            }
        });

        gestureDetector = new GestureDetector(new SwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                if(direction.equals(Direction.right)){
                    questionIndex = questionSequence.next(false);
                    if(questionIndex < 0)
                        questionIndex+=questions.length;
                    refreshText();
                }

                if(direction.equals(Direction.left)){
                    questionIndex = questionSequence.next(true);
                    if(questionIndex < 0)
                        questionIndex+=questions.length;
                    refreshText();
                }

                if(direction.equals(Direction.up)){
                    ((BitmapDrawable) imageDisplay.getDrawable()).getBitmap().recycle();
                    imageDisplay.setImageBitmap(BitmapFilter.changeStyle(DressCheckApplication.CMP.getRecentImage(), filterSequence.next(true)));
                }

                if(direction.equals(Direction.down)){
                    ((BitmapDrawable) imageDisplay.getDrawable()).getBitmap().recycle();
                    imageDisplay.setImageBitmap(BitmapFilter.changeStyle(DressCheckApplication.CMP.getRecentImage(), filterSequence.next(false)));
                }

                if(direction.equals(Direction.up)   ||  direction.equals(Direction.down))
                    return false;
                return true;
            }
        });

        gestureDetectorImageDisplay = new GestureDetector(new SwipeListener() {
            @Override
            public boolean onSwipe(Direction direction) {
                if(direction.equals(Direction.down)){
                    questionIndex++;
                    if (questionIndex > questions.length - 1) {
                        questionIndex = 0;
                    }
                    refreshText();
                }


                if(direction.equals(Direction.right)){
                    ((BitmapDrawable) imageDisplay.getDrawable()).getBitmap().recycle();
                    imageDisplay.setImageBitmap(BitmapFilter.changeStyle(DressCheckApplication.CMP.getRecentImage(), filterSequence.next(true)));
                }
                if(direction.equals(Direction.left)){
                    ((BitmapDrawable) imageDisplay.getDrawable()).getBitmap().recycle();
                    imageDisplay.setImageBitmap(BitmapFilter.changeStyle(DressCheckApplication.CMP.getRecentImage(), filterSequence.next(false)));
                }


                if(direction.equals(Direction.up)   ||  direction.equals(Direction.down))
                    return false;
                return true;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                DressCheckApplication.CMP.updateUsers();
            }
        }).start();

        arrowLeft = (ImageButton) findViewById(R.id.arrowLeft2);
        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionIndex = questionSequence.next(false);
                if(questionIndex < 0)
                    questionIndex+=questions.length;
                refreshText();
            }
        });
        arrowRight = (ImageButton) findViewById(R.id.arrowRight2);
        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionIndex = questionSequence.next(true);
                if(questionIndex < 0)
                    questionIndex+=questions.length;
                refreshText();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        imageDisplay = (ImageView) findViewById(R.id.imageView);
        imageDisplay.setImageBitmap(DressCheckApplication.CMP.getRecentImage());
        imageDisplay.setRotation(270);
        imageDisplay.setRotationY(180);
        imageDisplay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetectorImageDisplay.onTouchEvent(event);
            }
        });

        if(DressCheckApplication.tutorialCount_picturedisplay > 0){
            createDialogAlert();
            DressCheckApplication.tutorialCount_picturedisplay--;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    private void refreshText(){
        try {
            currentQuestionText = questions[questionIndex];

            if(questionIndex != 0)
                questionHolder.setText("<\t\t" + questions[questionIndex] + "\t\t>");
            else
                questionHolder.setText("<\t\t"+"Ask My Own Question"+"\t\t>\n"+"(Press Next To Ask)\n" +
                        "(Swipe left or Right for more questions!)");

        }catch (Exception e){
            e.printStackTrace();
            MyLog.print("Current Index:"+currentQuestionText);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), CameraActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    private void createDialogAlert(){
        LayoutInflater li = LayoutInflater.from(PictureDisplayActivity.this);
        View promptsView = li.inflate(R.layout.layout_dialog_box_information_camera_picturedisplay, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PictureDisplayActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();

    }
}
