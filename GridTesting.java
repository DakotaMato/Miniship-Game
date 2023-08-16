import java.util.*;
import java.io.*;
//Main Class
public class GridTesting{
   public static void main(String[] args){
   //number of rows in the battle grid
   int gridRows = 0;
   //number of columns in the battle grid
   int gridCols = 0;
   //level of difficulty desired by user.
   int difficulty = 0;
   //used to determine the x position of the enemy
   int randomRow;
   //used to determine the y position of the enemy
   int randomCol;
   //value that holds the row chosen by the user as a guess to where the enemy is
   int guessRow = 0;
   //value that holds the column chosen by the user as a guess to where the enemy is
   int guessCol = 0;
   //how many turns the player has remaining
   int turnsLeft = 0;
   //the score of the user
   int newScore = 0;
   //value that determines if the user wishes to play again
   char playAgain = 'y';
   //String that will hold input temporarily, and one that will hold the name the user wishes to represent themselves with
   String input, scoreName;
   //will check to make sure the user enters valid input at appropriate times
   boolean validInput = false;
   //determines whether the enemy was at a guessed location or not
   boolean enemyDetected = false;
   //checking if file/directory was created
   boolean creation = false;
   //keeps record path stored
   String recordPath = "";
   //number of turns for each difficulty
   final int HARD_TURNS = 10;
   final int MED_TURNS = 15;
   final int EASY_TURNS = 20;
   
   //We create the scorelist for the user's session
   HighScore.scoreList();
   
   //instantiate scanner object
   Scanner scan = new Scanner(System.in);
   
   //Ask user if they would like to keep a directory of their scores
   System.out.println("Before you begin playing, this program would like to keep a record of your high scores.");
   System.out.println("Please specify the directory you would like this file to be kept in");
   System.out.println("If you would prefer to not keep a record or directory, type n or N.");
   input = scan.nextLine();
   
   //check input and attempt to make directory and record keeping file
   if(!(input.charAt(0) == 'n' || input.charAt(0) == 'N')){
      //try to create the new directory
      try{
      File recordDir = new File(input);
      
      
      if(recordDir.exists()){
         System.out.println("Directory already exists");
      }else{
         creation = recordDir.mkdir();
      
      if(creation == true){
         System.out.println("Directory created");
         File record = new File(input + "\\" + "Scores.txt");
         record.createNewFile();
         recordPath = record.getAbsolutePath();
         System.out.println("Record created");
         
      }else{
      System.out.println("Record cannot be created");
      }
      }//Attempting to catch any exceptions that are thrown
      }catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
   }
   }
   
   //loop that will allow the user to play multiple times
   while(playAgain == 'y'){
   //Intro to the game
   System.out.println("Welcome to MiniShip! Try to find the enemy's position before time runs out!");
   System.out.println("The larger your grid and the harder your game, the higher your score!");   
   
   //Gathers input from user to give the grid a vertical size 
   System.out.println("How many rows would you like the grid to be? Enter a number from 1 to 10");
   input = scan.nextLine();
   //Check to make sure the user entered a number
   validInput = inputCheck(input);
   
   //makes sure gridRows has a useful value for the while loop
   if(validInput)
       gridRows = Integer.parseInt(input);
      
      //While loop that keeps asking the user for a valid input as long as the input isn't valid and that checks to make sure the given number makes sense
      while(!(validInput) || gridRows > 10 || gridRows < 0){
         System.out.println("You entered invalid input or an invalid number. Please try again.");
         System.out.println("How many rows would you like the grid to be? Enter a number from 1 to 10");
         input = scan.nextLine();
         validInput = inputCheck(input);
         //makes sure input value is transferred to appropriate variable, even if user misinput something
         if(validInput)
            gridRows = Integer.parseInt(input);
      }
   //Gathers input from user to determine horizontal size of grid
   System.out.println("How many columns would you like the grid to be? Enter a number from 1 to 10");
   input = scan.nextLine();
   
      //Check to make sure the user entered a number
   validInput = inputCheck(input);
   
   //makes sure gridCols has a useful value for the while loop
   if(validInput)
       gridCols = Integer.parseInt(input);

      
      //While loop that keeps asking the user for a valid input as long as the input isn't valid and that checks to make sure the given number makes sense
      while(!(validInput) || gridCols > 10 || gridCols < 0){
         System.out.println("You entered invalid input or an invalid number. Please try again.");
         System.out.println("How many columns would you like the grid to be? Enter a number from 1 to 10");
         input = scan.nextLine();
         validInput = inputCheck(input);
         //makes sure input value is transferred to appropriate variable, even if user misinput something
         if(validInput)
            gridCols = Integer.parseInt(input);
      }
      
   //Explaining how difficulty works to user
   System.out.println("The difficulty affects how many turns you have to find the enemy and your score!");
   System.out.println("Easy gives you 20 turns, Medium gives you 15 turns, and Hard gives you 10 Turns");

      
      
   //Get difficulty of game from user
   System.out.println("How difficult would you like the game to be? Put 1 for Easy, 2 for Medium, and 3 for Hard.");
   input = scan.nextLine();
   
      
   //Check to make sure the user entered a number
   validInput = inputCheck(input);
   
   //makes sure gridCols has a useful value for the while loop
   if(validInput)
       turnsLeft = Integer.parseInt(input);
       
      while(!(validInput) || turnsLeft > 3 || turnsLeft < 1){
         System.out.println("You entered invalid input or an invalid number. Please try again.");
         System.out.println("How difficult would you like the game to be? Put 1 for Easy, 2 for Medium, and 3 for Hard.");
         input = scan.nextLine();
         validInput = inputCheck(input);
         if(validInput)
            turnsLeft = Integer.parseInt(input);
      }
   //Assign Turns
   switch(turnsLeft){
      case 1:
         turnsLeft = EASY_TURNS;
         difficulty = 1;
         break;
      case 2:
         turnsLeft = MED_TURNS;
         difficulty = 2;
         break;
      case 3:
         turnsLeft = HARD_TURNS;
         difficulty = 3;
         break;
   }
      

   //Explains how coordinates will be entered
   System.out.println("Coordinates start from 0 and go to 9, max");
   
   //assigns enemy to a random value on the grid
   randomRow = ((int)(Math.random() * 100) % gridRows);
   randomCol = ((int)(Math.random() * 100) % gridCols);
   //debug print
   System.out.println(randomRow + " " + randomCol);
   
   //creating the grid
   char[][] battleGrid = makeGrid(gridRows, gridCols, randomRow, randomCol);
   
   //MAIN GAME Loop to keep the game going. The loop checks to make sure the user still has turns left and that they haven't found the enemy
   while(enemyDetected == false && turnsLeft > 0){
   
      //display Grid
      displayGrid(battleGrid, gridCols, gridRows);
      System.out.println("");
      System.out.println("You have " + turnsLeft + " turns remaining");
      //Get user's guess for the row
      System.out.println("Where do you think the enemy is? Pick a row number");
      input = scan.nextLine();
      
      //Check to make sure the user entered a number
      validInput = inputCheck(input);
      //makes sure guessRows has a useful value for the while loop
      if(validInput)
         guessRow = Integer.parseInt(input);

      
      //While loop that keeps asking the user for a valid input as long as the input isn't valid and that checks to make sure the given number makes sense for the grid in question
      while(!(validInput) || guessRow >= gridRows || guessRow < 0){
         System.out.println("You entered invalid input or an invalid number. Please try again.");
         System.out.println("Where do you think the enemy is? Pick a row number");
         input = scan.nextLine();
         validInput = inputCheck(input);
         if(validInput)
            guessRow = Integer.parseInt(input);
      }

      //Get user's guess for the column
      System.out.println("Pick a column number");
      input = scan.nextLine();
      
      //Check to make sure the user entered a number
      validInput = inputCheck(input);
      
      if(validInput)
        guessCol = Integer.parseInt(input);
      
      //While loop that keeps asking the user for a valid input as long as the input isn't valid
      while(!(validInput) || guessCol > gridCols || guessCol < 0){
         System.out.println("You entered invalid input or an invalid number. Please try again.");
         System.out.println("Pick a column number");
         input = scan.nextLine();
         validInput = inputCheck(input);
         if(validInput)
            guessCol = Integer.parseInt(input);
      }
      
      //Check if enemy is at position user guessed
      enemyDetected = enemyCheck(battleGrid, guessCol, guessRow);
      
      //De-increment the turn counter
      turnsLeft = turnsLeft - 1;
      
      //Checks for win condition and displays output when satisfied
      if(enemyDetected){
         System.out.println("You win! Congratulations!");
         //Generates a new score based on different metrics
         newScore = scoreCalc(difficulty, turnsLeft, gridRows, gridCols);
         System.out.println("Your score is " + newScore);
         //Get user's name
         System.out.println("Input what name you'd like to have be recorded with your score. 3 characters or less:");
         scoreName = scan.nextLine().substring(0,3);
         //generate a new HighScore object to put into the list of scores generated earlier
         if(creation == true){
            new HighScore(newScore, scoreName, recordPath);
         }else{
            new HighScore(newScore, scoreName);
         }
         //Display the current list of high scores.
         HighScore.displayScores();
         //Allow user the chance to play again
         System.out.println("Play Again? Type 'y' or 'Y' for yes");
         playAgain = scan.nextLine().charAt(0);
         //makes sure the user types a letter
         while(!Character.isLetter(playAgain)){
            System.out.println("Sorry, try that again. Type 'y' or 'Y' for yes if you want to play again");
            playAgain = scan.nextLine().charAt(0);
         }
         //allows either y or Y to work for input
         playAgain = Character.toLowerCase(playAgain);
         //makes sure the while loop of the game has both conditions satisfied to end
         turnsLeft = 0;
         }
      else
         //miss display
         System.out.println("No luck. Guess again.");
         }
         
      //Checks if user ran out of turns   
      if(turnsLeft == 0 && enemyDetected == false){
         System.out.println("Game Over. You ran out of turns.");
         HighScore.displayScores();
         System.out.println("Play Again? Type 'y' or 'Y' for yes");
         playAgain = scan.next().charAt(0);
         while(!Character.isLetter(playAgain)){
            System.out.println("Sorry, try that again. Type 'y' or 'Y' for yes if you want to play again");
            playAgain = scan.next().charAt(0);
         }
         playAgain = Character.toLowerCase(playAgain);

         }
         //reset enemy detected
         enemyDetected = false;
         }
   }
   //Function that generates the grid for the game
   public static char[][] makeGrid(int rows, int cols, int randoRow, int randoCol){
   
      char[][] grid = new char[rows][cols];
      //Fills every row with Xs for graphic display
         for(char[] filler: grid)
            Arrays.fill(filler, 'X');
            
     //assigns enemy position
       grid[randoRow][randoCol] = '1';
      
      return grid;
   
   }
   //function that displays the battle grid every turn
   public static void displayGrid(char[][] tempGrid, int cols, int rows){
   //Display grid graphic
      System.out.print("-");
      for(int h = 0; h < cols;h++)
         System.out.print(h);
      for(int i = 0; i< rows;i++){
         System.out.println("");
         System.out.print(i + "");
         for(int j = 0; j < cols; j++){
         //makes sure user doesn't see where the enemy is by printing '1'
            if(tempGrid[i][j]=='1')
               System.out.print('X');
            else
               System.out.print(tempGrid[i][j]);
         }
      }
   
   }
   //checks if enemy is at the position the user guessed, and if not, updated the grid graphic to show the user where they've guessed before
   public static boolean enemyCheck(char[][] tempGrid, int col, int row){
      boolean status;
      if(tempGrid[row][col] == '1')
         status = true;
      else
         status = false;
         tempGrid[row][col] = 'O';
         return status;
   
   }
   //checks to make sure user enters a number when appropriate
   public static boolean inputCheck(String str){
   
   boolean goodInput;
   
   if(str == null)
      goodInput = false;
   else if(str.equals(""))
      goodInput = false;
   else if(str.matches("^[a-zA-Z]*$"))
      goodInput = false;
   else
      goodInput = true;
   
   return goodInput;
   
   }
   //function that calculates score
   public static int scoreCalc(int tempDif, int turns, int rows, int cols){
   int finalScore, difMod, turnMod, rowMod, colMod;
   difMod = tempDif * 1000;
   turnMod = tempDif * (turns * 50);
   rowMod = (tempDif * 2) * (rows * 25);
   colMod = (tempDif * 2) * (cols * 25);
   finalScore = difMod + turnMod + rowMod + colMod;
   return finalScore;
   }
}


//Class that will contain a list of HighScores and log every score
class HighScore{
   private static String[][] highScores;
   
   //used for file Reading
   private String tempRead;

   private int newScore;
   private String newName;

   public static void displayScores(){
   for(int i=0;i<10;i++){
      System.out.println(highScores[i][0] + " " + highScores[i][1]);
   }
   
   }
   
    //Will initialize the highscore list at the start of a session. LEFT: NAME. RIGHT: SCORE
   public static void scoreList(){
      //Creates list of high scores reminiscent of an old school arcade cabinet
      highScores = new String[10][2];
      for(int i = 0,j=1; i<10;i++){
         highScores[i][0] = "---";
         highScores[i][j] = "0";
      }
   }
   

   
   //class constructor
   public HighScore(int score, String scoreName){
      newScore = score;
      newName = scoreName;
      //Replaces low scores with higher ones
      for(int i = 0;i<10;i++){
         if(newScore > Integer.parseInt(highScores[i][1])){
            highScores[i][0] = newName;
            highScores[i][1] = Integer.toString(newScore);
            
            i = 10;
         }   
      }
      
   }
      //overloaded class constructor when needing to write to file
      public HighScore(int score, String scoreName, String recordTemp){
      newScore = score;
      newName = scoreName;
      //Replaces low scores with higher ones
      for(int i = 0;i<10;i++){
         if(newScore > Integer.parseInt(highScores[i][1])){
            highScores[i][0] = newName;
            highScores[i][1] = Integer.toString(newScore);
            try{
              //create record reader
               Scanner recordReader = new Scanner(recordTemp);
               //while the record still has lines of text, check if they are empty, and if not, append to the file instead of overwriting it
               while(recordReader.hasNextLine()){
                  tempRead = recordReader.nextLine();
                  if(tempRead != ""){
                     FileWriter recordWriter = new FileWriter(recordTemp, true);
                     BufferedWriter bufferedRecord = new BufferedWriter(recordWriter);
                     bufferedRecord.write("\n" + newName + " " + newScore);
                     bufferedRecord.close();
                  
                  }else{
                     //Creates and closes FileWriter
                     FileWriter recordWriter = new FileWriter(recordTemp);
                     recordWriter.write(newName + " " + newScore);
                     recordWriter.close();
                  
                  }
                  
               }
            recordReader.close();
            

               
            }catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            }
            
            i = 10;
            }
         }   
      }
      
   
   public void setNewScore(int score){
      newScore = score;
   }
   
   public void setNewName(String scoreName){
      newName = scoreName;
   }
   
   public int getNewScore(){
      return newScore;
   }
   
   public String getNewName(){
      return newName;
   }

}