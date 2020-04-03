import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class BatchGenerator {

    public String generateBatchFromUserInput() {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        String generatedBatch = "";

        while(!line.equals("F")) {
            generatedBatch += line + "\n";
            line = sc.nextLine();
        }
        generatedBatch += line;
        return generatedBatch;
    }

    public String generateBatchRandomly(int percentageOfWrites, int numOfNodes, int batchesCountLimit) {
        String generatedBatch = "";
        int numOfBatches = generateRandomNumber(1, batchesCountLimit);

        int i = numOfBatches;

        while(i >= 0) {
            String currentOperation = generateRandomOperation(percentageOfWrites);
            int num1 = generateRandomNumber(0, numOfNodes - 1);
            int num2 = generateRandomNumber(0, numOfNodes - 1);
            String singleQuery = currentOperation + " " + num1 + " " + num2 + '\n';
            generatedBatch += singleQuery;
            i--;
        }
        generatedBatch += "F";
        return generatedBatch;
    }

    private String generateRandomOperation(int percentageOfWrites) {
        int countOfWrites = percentageOfWrites/10;
        Random r = new Random();
        String[] pool = new String[10];

        for(int i = 0; i < 10; i++) {
            if(countOfWrites > 0) {
                if (r.nextInt((1 - 0) + 1) == 0) pool[i] = "A";
                else pool[i] = "D";
                countOfWrites--;
            }
            else {
                pool[i] = "Q";
            }
        }
        List<String> poolList = Arrays.asList(pool);
        Collections.shuffle(poolList);
        poolList.toArray(pool);
        int opID = r.nextInt((9 - 0) + 1);
        return pool[opID];
    }

    private int generateRandomNumber(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public String generateBatchFromInputFile(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}
