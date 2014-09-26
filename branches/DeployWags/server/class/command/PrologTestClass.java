import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * This is the test class for the deleteAll Prolog Magnet Exercise
 * 
 * @file PrologTestClass.java
 *
 */
public class PrologTestClass {

    /**
     * Entry point of program
     *
     * Gets solution and student execution strings passed in as arguments
     *
     */
    public static void main(String args[]) {

        // These strings contain all necessary information for executing these files
        //  -Will just have to append any arguments needed
        String nonce = args[0];
        String solutionExecString = args[1];
        String studentExecString = args[2];
        int counter = Integer.parseInt(args[3]);
        String[] queries = args[4].split("L#L");

        new PrologTestClass(solutionExecString, studentExecString, queries, nonce, counter);
    }

    /**
     * Constructor
     *
     */
    public PrologTestClass(String solutionExecString, String studentExecString, String[] queries, String nonce, int counter)
    {
        boolean success = true;
       

        for(int i = 0; i < counter; i++){
            if (!testCase(solutionExecString+i+".pl", studentExecString+i+".pl", queries[i])){
                success = false;
            }
        }

        // Print final success/failure outcome
        if (success)
            System.out.println(nonce);
        else
            System.out.println("\nFailure");
    }
    

    /**
     * Run the prolog file on the given arguments
     *
     */
    private String runFile(String fileExecString)
    {
        String results = "";

        try {
            // get local runtime and execute both student and solution files
            Runtime runtime = Runtime.getRuntime();

            // run the file name with the list argument
            Process process = runtime.exec(fileExecString);

            // set up a stream to the get output of the file            
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String processOutput = null;

            // get the output from the file
            while ((processOutput = input.readLine()) != null)
            {
                results += processOutput + "\n";
            }

        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("There was an error running the files");
            System.exit(1);
        }

        // return resulting output from file
        return results.trim(); // trim to get rid of trailing newline
    }

    /**
     * Tests solution and student files on given list
     * 
     * Returns the boolean result of this case
     *
     */
    private boolean testCase(String solutionExecString, String studentExecString, String query)
    { 
        System.out.println("Testing Query: "+query);
        String solutionOutput = runFile(solutionExecString);
   
        String studentOutput  = runFile(studentExecString);
        
        // Determine if answer is correct
        boolean isCorrect = true;
        if (!studentOutput.equals(solutionOutput)){
            isCorrect = false;
        }

        // Print out results
        //  -Looks cleaner to print original list with commas instead of spaces
        System.out.println("Correct Result: " + solutionOutput);
        System.out.println("Your Result:    " + studentOutput + "\n");
        
        if(isCorrect)
            System.out.println("Answer: Correct! \n");
        else
            System.out.println("Answer: Incorrect! \n");
        
        // Return results of this case
        return isCorrect;
    }
    
}
