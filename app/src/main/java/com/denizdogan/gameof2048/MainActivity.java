package com.denizdogan.gameof2048;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.denizdogan.gameof2048.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int currentScore;
    private int recordScore;
    private final String recordScoreKey = "recordScore";
    private ConstraintLayout ct;

    private TextView gameOverText;
    private TextView currentScoreTextView;
    private TextView recordScoreTextView;
    private boolean running;

    private int numberOfEmptyCoordinates;

    private SharedPreferences sharedPreferences;

    private int[][] gameArray;

    private final int ROW_NUMBER = 4;

    private final int COLUMN_NUMBER = 4;

    private RandomClass random;

    private SwipeListener swipeListener;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        this.setContentView(view);

        sharedPreferences = this.getSharedPreferences("com.denizdogan.gameof2048", MODE_PRIVATE);
        recordScore = sharedPreferences.getInt(recordScoreKey, 0);
        ct = binding.constraintLayout;
        gameOverText = binding.gameOver;
        currentScoreTextView = binding.textViewOfScoreNumber;
        recordScoreTextView = binding.textViewOfRecordNumber;
        gridLayout = binding.gridLayout;

        gameOverText.setVisibility(View.INVISIBLE);
        swipeListener = new SwipeListener(ct);
        random = RandomClass.getInstance();
        gameArray = new int[ROW_NUMBER][COLUMN_NUMBER];
        running = true;
        currentScore = 0;
        numberOfEmptyCoordinates = ROW_NUMBER * COLUMN_NUMBER;
        initializeOfBox();
        createRandomBox();
        createRandomBox();

//        gameArray[0][0] = 2;
//        gameArray[1][0] = 4;
//        gameArray[2][0] = 8;
//        gameArray[3][0] = 16;
//
//        gameArray[0][1] = 32;
//        gameArray[1][1] = 64;
//        gameArray[2][1] = 128;
//        gameArray[3][1] = 256;
//
//        gameArray[0][2] = 512;
//        gameArray[1][2] = 1024;
//        gameArray[2][2] = 2048;
//        gameArray[3][2] = 4096;
//
//        gameArray[0][3] = 8192;
//        gameArray[1][3] = 16384;
//        gameArray[2][3] = 32768;
//        gameArray[3][3] = 65536;
//        numberOfEmptyCoordinates = 0;

        refreshTheScreen();
    }

    private void createRandomBox() {
        System.out.println(numberOfEmptyCoordinates);
        if(numberOfEmptyCoordinates != 0){
            int randomNum = random.getRandomNumber();
            int[] coordinateOfRandomNum = random.getRandomCoordinate(ROW_NUMBER);
            while (gameArray[coordinateOfRandomNum[0]][coordinateOfRandomNum[1]] != 0){
                coordinateOfRandomNum = random.getRandomCoordinate(ROW_NUMBER);
            }
            gameArray[coordinateOfRandomNum[0]][coordinateOfRandomNum[1]] = randomNum;
            refreshTheScreen();
            numberOfEmptyCoordinates--;
        }
        else{


        }

    }
    private void gameOver(){
        //gridLayout.setBackgroundResource(R.color.constraintBackground);
        running = false;
        gameOverText.setVisibility(View.VISIBLE);
    }
    private boolean compressBoxes(String direction) {

        int emptyCoordinateX, emptyCoordinateY;
        boolean haveAnEmptyCoordinate, isItCompressed = false;

        if(direction.equals("up")){
            for(int i = 0;i < COLUMN_NUMBER; i++){
                emptyCoordinateX = - 1;
                emptyCoordinateY = - 1;
                haveAnEmptyCoordinate = false;
                for(int j = 0;j < ROW_NUMBER; j++){
                    if(gameArray[j][i] == 0 && !haveAnEmptyCoordinate){
                        emptyCoordinateX = i;
                        emptyCoordinateY = j;
                        haveAnEmptyCoordinate = true;
                    }
                    else if(gameArray[j][i] != 0 && haveAnEmptyCoordinate) {
                        int tempNum = gameArray[j][i];
                        gameArray[j][i] = 0;
                        gameArray[emptyCoordinateY][emptyCoordinateX] = tempNum;
                        emptyCoordinateX = i;
                        emptyCoordinateY = j;
                        while (emptyCoordinateY >= 0 && gameArray[emptyCoordinateY][emptyCoordinateX] == 0){
                            emptyCoordinateY--;
                        }
                        emptyCoordinateY++;
                        haveAnEmptyCoordinate = true;
                        isItCompressed = true;
                    }
                }
            }
        }
        else if(direction.equals("down")){
            for(int i = 0;i < COLUMN_NUMBER; i++){
                emptyCoordinateX = - 1;
                emptyCoordinateY = - 1;
                haveAnEmptyCoordinate = false;
                for(int j = ROW_NUMBER - 1;j >= 0; j--){
                    if(gameArray[j][i] == 0 && !haveAnEmptyCoordinate){
                        emptyCoordinateX = i;
                        emptyCoordinateY = j;
                        haveAnEmptyCoordinate = true;
                    }
                    else if(gameArray[j][i] != 0 && haveAnEmptyCoordinate) {
                        int tempNum = gameArray[j][i];
                        gameArray[j][i] = 0;
                        gameArray[emptyCoordinateY][emptyCoordinateX] = tempNum;
                        emptyCoordinateX = i;
                        emptyCoordinateY = j;
                        while (emptyCoordinateY <= ROW_NUMBER - 1 && gameArray[emptyCoordinateY][emptyCoordinateX] == 0){
                            emptyCoordinateY++;
                        }
                        emptyCoordinateY--;
                        haveAnEmptyCoordinate = true;
                        isItCompressed = true;
                    }
                }
            }
        }
        else if(direction.equals("right")){
            for(int i = 0;i < ROW_NUMBER; i++){
                emptyCoordinateX = - 1;
                emptyCoordinateY = - 1;
                haveAnEmptyCoordinate = false;
                for(int j = COLUMN_NUMBER - 1;j >= 0; j--){
                    if(gameArray[i][j] == 0 && !haveAnEmptyCoordinate){
                        emptyCoordinateX = j;
                        emptyCoordinateY = i;
                        haveAnEmptyCoordinate = true;
                    }
                    else if(gameArray[i][j] != 0 && haveAnEmptyCoordinate) {
                        int tempNum = gameArray[i][j];
                        gameArray[i][j] = 0;
                        gameArray[emptyCoordinateY][emptyCoordinateX] = tempNum;
                        emptyCoordinateX = j;
                        emptyCoordinateY = i;
                        while (emptyCoordinateX <= COLUMN_NUMBER - 1 && gameArray[emptyCoordinateY][emptyCoordinateX] == 0){
                            emptyCoordinateX++;
                        }
                        emptyCoordinateX--;
                        haveAnEmptyCoordinate = true;
                        isItCompressed = true;
                    }
                }
            }
        }
        else if(direction.equals("left")){
            for(int i = 0;i < ROW_NUMBER; i++){
                emptyCoordinateX = - 1;
                emptyCoordinateY = - 1;
                haveAnEmptyCoordinate = false;
                for(int j = 0;j < COLUMN_NUMBER; j++){
                    if(gameArray[i][j] == 0 && !haveAnEmptyCoordinate){
                        emptyCoordinateX = j;
                        emptyCoordinateY = i;
                        haveAnEmptyCoordinate = true;
                    }
                    else if(gameArray[i][j] != 0 && haveAnEmptyCoordinate) {
                        int tempNum = gameArray[i][j];
                        gameArray[i][j] = 0;
                        gameArray[emptyCoordinateY][emptyCoordinateX] = tempNum;
                        emptyCoordinateX = j;
                        emptyCoordinateY = i;
                        while (emptyCoordinateX >= 0 && gameArray[emptyCoordinateY][emptyCoordinateX] == 0){
                            emptyCoordinateX--;
                        }
                        emptyCoordinateX++;
                        haveAnEmptyCoordinate = true;
                        isItCompressed = true;
                    }
                }
            }
        }
        else {
            System.out.println("Unexpected direction!");
        }
        boolean isItMerged = mergeSameBoxesInAllGameArray(direction);
        refreshTheScreen();
        //printGameArray();
        return isItCompressed || isItMerged;
    }
    private void printGameArray(){
        System.out.println("naer");
        for(int i = 0;i < ROW_NUMBER; i++){
            for(int j = 0;j < COLUMN_NUMBER; j++){
                System.out.print(gameArray[i][j] + "  ");
            }
            System.out.println();
        }

    }
    private boolean mergeSameBoxesInAllGameArray(String direction) {
        boolean isItMerged = false;
        if(direction.equals("up")){
            for(int i = 0;i < COLUMN_NUMBER; i++){
                for(int j = 1;j < ROW_NUMBER; j++){
                    if(gameArray[j][i] != 0 && gameArray[j-1][i] != 0 && gameArray[j][i] == gameArray[j-1][i]){
                        numberOfEmptyCoordinates++;
                        isItMerged = true;
                        gameArray[j-1][i] = gameArray[j][i] + gameArray[j-1][i];
                        currentScore +=gameArray[j-1][i];
                        int tempJ = j;
                        while (tempJ < ROW_NUMBER){
                            if(tempJ + 1 < ROW_NUMBER){
                                gameArray[tempJ][i] = gameArray[tempJ + 1][i];
                            }
                            tempJ++;
                        }
                        gameArray[--tempJ][i] = 0;
                    }
                }
            }
        }
        else if(direction.equals("down")){
            for(int i = 0;i < COLUMN_NUMBER; i++){
                for(int j = ROW_NUMBER - 2;j >= 0; j--){
                    if(gameArray[j][i] != 0 && gameArray[j+1][i] != 0 && gameArray[j][i] == gameArray[j+1][i]){
                        numberOfEmptyCoordinates++;
                        isItMerged = true;
                        gameArray[j+1][i] = gameArray[j+1][i] + gameArray[j][i];
                        currentScore +=gameArray[j+1][i];
                        int tempJ = j;
                        while (tempJ >= 0){
                            if(tempJ - 1 >= 0){
                                gameArray[tempJ][i] = gameArray[tempJ - 1][i];
                            }
                            tempJ--;
                        }
                        gameArray[++tempJ][i] = 0;
                    }
                }
            }
        }
        else if(direction.equals("right")){
            for(int i = 0;i < ROW_NUMBER; i++){
                for(int j = COLUMN_NUMBER - 2;j >= 0; j--){
                    if(gameArray[i][j] != 0 && gameArray[i][j+1] != 0 && gameArray[i][j] == gameArray[i][j+1]){
                        numberOfEmptyCoordinates++;
                        isItMerged = true;
                        gameArray[i][j+1] = gameArray[i][j+1] + gameArray[i][j];
                        currentScore += gameArray[i][j+1];
                        int tempJ = j;
                        while(tempJ >= 0){
                            if(tempJ - 1 >= 0){
                                gameArray[i][tempJ] = gameArray[i][tempJ - 1];
                            }
                            tempJ--;
                        }
                        gameArray[i][++tempJ] = 0;
                    }
                }
            }
        }
        else if(direction.equals("left")){
            for(int i = 0;i < ROW_NUMBER; i++){
                for(int j = 1;j < COLUMN_NUMBER; j++){
                    if(gameArray[i][j] != 0 && gameArray[i][j-1] != 0 && gameArray[i][j] == gameArray[i][j-1]){
                        numberOfEmptyCoordinates++;
                        isItMerged = true;
                        gameArray[i][j-1] = gameArray[i][j-1] + gameArray[i][j];
                        currentScore += gameArray[i][j-1];
                        int tempJ = j;
                        while(tempJ < COLUMN_NUMBER){
                            if(tempJ + 1 < COLUMN_NUMBER){
                                gameArray[i][j] = gameArray[i][j+1];
                            }
                            tempJ++;
                        }
                        gameArray[i][--tempJ] = 0;
                    }
                }
            }
        }
        else {
            System.out.println("Unexpected direction!");
        }
        if(numberOfEmptyCoordinates == 0){
            gameOver();
        }
        return isItMerged;
    }
    @SuppressLint("ResourceAsColor")
    private void refreshTheScreen() {
        if(currentScore >= recordScore){
            recordScore = currentScore;
            sharedPreferences.edit().putInt(recordScoreKey, recordScore).apply();
        }
        recordScoreTextView.setText(Integer.toString(recordScore));
        currentScoreTextView.setText(Integer.toString(currentScore));
        TextView tempTextView = null;
        boolean lowerThan8;
        for(int i = 0;i < ROW_NUMBER; i++){
            for (int j = 0;j < COLUMN_NUMBER; j++){
                tempTextView = decodeCoordinates(i , j);
                if(gameArray[i][j] != 0){
                    switch (gameArray[i][j]){
                        case 2:
                            tempTextView.setBackgroundResource(R.color.backgroundOf2);
                            lowerThan8 = true;
                            break;
                        case 4:
                            tempTextView.setBackgroundResource(R.color.backgroundOf4);
                            lowerThan8 = true;
                            break;
                        case 8:
                            tempTextView.setBackgroundResource(R.color.backgroundOf8);
                            lowerThan8 = false;
                            break;
                        case 16:
                            tempTextView.setBackgroundResource(R.color.backgroundOf16);
                            lowerThan8 = false;
                            break;
                        case 32:
                            tempTextView.setBackgroundResource(R.color.backgroundOf32);
                            lowerThan8 = false;
                            break;
                        case 64:
                            tempTextView.setBackgroundResource(R.color.backgroundOf64);
                            lowerThan8 = false;
                            break;
                        case 128:
                            tempTextView.setBackgroundResource(R.color.backgroundOf128);
                            lowerThan8 = false;
                            break;
                        case 256:
                            tempTextView.setBackgroundResource(R.color.backgroundOf256);
                            lowerThan8 = false;
                            break;
                        case 512:
                            tempTextView.setBackgroundResource(R.color.backgroundOf512);
                            lowerThan8 = false;
                            break;
                        case 1024:
                            tempTextView.setBackgroundResource(R.color.backgroundOf1024);
                            lowerThan8 = false;
                            break;
                        case 2048:
                            tempTextView.setBackgroundResource(R.color.backgroundOf2048);
                            lowerThan8 = false;
                            break;
                        default:
                            tempTextView.setBackgroundResource(R.color.backgroundOfGreaterThan2048);
                            lowerThan8 = false;
                            break;

                    }
                    if (lowerThan8){
                        tempTextView.setTextColor(getResources().getColor(R.color.textColorOfLowerThan8));
                    }
                    else{
                        tempTextView.setTextColor(getResources().getColor(R.color.textColorOfGreaterThan8));
                    }
                    tempTextView.setText(Integer.toString(gameArray[i][j]));
                }
                else{
                    tempTextView.setBackgroundResource(R.color.boxBackground);
                    tempTextView.setText("");
                }
            }
        }

    }
    private class SwipeListener implements  View.OnTouchListener {

        //Initialize Variables

        GestureDetector gestureDetector;

        //Creating Constructor

        public SwipeListener(View view){
            //Initialize Threshold valur

            int threshold = 100;
            int velocity_threshold = 100;

            //Initialize simple gesture listener
            GestureDetector.SimpleOnGestureListener listener =
                    new GestureDetector.SimpleOnGestureListener(){

                        @Override
                        public boolean onDown(MotionEvent e){
                            return true;
                        }

                        @Override
                        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                            //Get x and y difference
                            float xDiff = e2.getX() - e1.getX();
                            float yDiff = e2.getY() - e1.getY();

                            try {
                                //Check Condition
                                if(Math.abs(xDiff) > Math.abs(yDiff)) {
                                    //When x is greater than y
                                    //Check Condition
                                    if(Math.abs(xDiff) > Math.abs(threshold) && Math.abs(velocityX) > velocity_threshold){
                                        //When x difference is greater than threshold
                                        //When x velocity is greater than velocity threshold
                                        //CheckCondition
                                        if(xDiff > 0){
                                            //When swiped right
                                            if(running && compressBoxes("right")){
                                                createRandomBox();
                                            }
                                        }
                                        else {
                                            //When swiped left
                                            if(running && compressBoxes("left")){
                                                createRandomBox();
                                            }
                                        }
                                        return true;
                                    }
                                }
                                else {
                                    //When y is greater than x
                                    //Check Condition
                                    if(Math.abs(yDiff) > Math.abs(threshold) && Math.abs(velocityY) > velocity_threshold){
                                        //When y difference is greater than threshold
                                        //When y velocity is greater than velocity threshold
                                        //CheckCondition
                                        if(yDiff > 0){
                                            //When swiped down
                                            if(running && compressBoxes("down")){
                                                createRandomBox();
                                            }
                                        }
                                        else {
                                            //When swiped up
                                            if(running && compressBoxes("up")){
                                                createRandomBox();
                                            }
                                        }
                                        return true;
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            return false;
                            //return super.onFling(e1, e2, velocityX, velocityY);
                        }
                    };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //return gesture event
            return gestureDetector.onTouchEvent(event);
        }
    }
    private TextView decodeCoordinates(int i, int j){

        if(i == 0 && j == 0){
            return binding.box1;
        }
        else if (i == 0 && j == 1) {
            return binding.box2;
        }
        else if (i == 0 && j == 2) {
            return binding.box3;
        }
        else if (i == 0 && j == 3) {
            return binding.box4;
        }
        else if (i == 1 && j == 0) {
            return binding.box5;
        }
        else if (i == 1 && j == 1) {
            return binding.box6;
        }
        else if (i == 1 && j == 2) {
            return binding.box7;
        }
        else if (i == 1 && j == 3) {
            return binding.box8;
        }
        else if (i == 2 && j == 0) {
            return binding.box9;
        }
        else if (i == 2 && j == 1) {
            return binding.box10;
        }
        else if (i == 2 && j == 2) {
            return binding.box11;
        }
        else if (i == 2 && j == 3) {
            return binding.box12;
        }
        else if (i == 3 && j == 0) {
            return binding.box13;
        }
        else if (i == 3 && j == 1) {
            return binding.box14;
        }
        else if (i == 3 && j == 2) {
            return binding.box15;
        }
        else {
            return binding.box16;
        }

    }
    private void initializeOfBox(){
        binding.box1.setBackgroundResource(R.color.boxBackground);
        binding.box2.setBackgroundResource(R.color.boxBackground);
        binding.box3.setBackgroundResource(R.color.boxBackground);
        binding.box4.setBackgroundResource(R.color.boxBackground);
        binding.box5.setBackgroundResource(R.color.boxBackground);
        binding.box5.setBackgroundResource(R.color.boxBackground);
        binding.box6.setBackgroundResource(R.color.boxBackground);
        binding.box7.setBackgroundResource(R.color.boxBackground);
        binding.box8.setBackgroundResource(R.color.boxBackground);
        binding.box9.setBackgroundResource(R.color.boxBackground);
        binding.box10.setBackgroundResource(R.color.boxBackground);
        binding.box11.setBackgroundResource(R.color.boxBackground);
        binding.box12.setBackgroundResource(R.color.boxBackground);
        binding.box13.setBackgroundResource(R.color.boxBackground);
        binding.box14.setBackgroundResource(R.color.boxBackground);
        binding.box15.setBackgroundResource(R.color.boxBackground);
        binding.box16.setBackgroundResource(R.color.boxBackground);
    }
}