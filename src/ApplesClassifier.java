import java.io.*;
import java.util.*;

public class ApplesClassifier {

    public static void main(String[] args) throws java.io.IOException {

        FileReader fruitReader = new FileReader("apple-fruit.txt");
        FileReader companyReader = new FileReader("apple-computers.txt");
        BufferedReader fruitReaderBuffer = new BufferedReader(fruitReader);
        BufferedReader companyReaderBuffer = new BufferedReader(companyReader);

        Scanner input = new Scanner(System.in);
        int numberInput = input.nextInt();
        if (numberInput > 100){
            System.err.println("Number of lines more than 100. Try smaller values ( N should be <= 100 )");
            System.exit(1);
        }
        input.nextLine();

        int fruitCounter = 0;
        HashMap<String, Integer> computer = new HashMap<>();
        HashMap<String, Double> probabilityComputerWords = new HashMap<>();
        int computerCounter = 0;
        String lineString;

        while((lineString = companyReaderBuffer.readLine()) != null){

            if (!lineString.isEmpty()){

                lineString = replaceCharacters(lineString);
                computerCounter++;
                String[] words = lineString.split("\\s");
                Set<String> set = new HashSet<>(Arrays.asList(words));

                for (String word : set){
                    if (computer.containsKey(word)){
                        int value = computer.get(word);
                        computer.put(word, value + 1);
                    } else{
                        computer.put(word, 1);
                    }
                }
            }
        }

        double minComputerProbability = 1.0;
        for (Map.Entry<String, Integer> entry : computer.entrySet()){
            double probability = (double) entry.getValue() / computerCounter;
            minComputerProbability = Math.min(probability, minComputerProbability);
            probabilityComputerWords.put(entry.getKey(), probability);
        }

        HashMap <String, Integer> fruit = new HashMap<>();
        HashMap <String, Double> probabilityFruitWords = new HashMap<>();

        while((lineString = fruitReaderBuffer.readLine()) != null){
            if (!lineString.isEmpty()){
                lineString = replaceCharacters(lineString);
                fruitCounter++;
                String[] fruitWords = lineString.split("\\s");
                Set<String> set = new HashSet<>(Arrays.asList(fruitWords));
                for (String word : set){
                    if (fruit.containsKey(word)){
                        int val = fruit.get(word);
                        fruit.put(word, ++val);
                    } else {
                        fruit.put(word, 1);
                    }
                }
            }
        }

        double minFruitProbability = 1.0;
        for (Map.Entry<String, Integer> entry : fruit.entrySet()){
            double prob = (double) entry.getValue() / fruitCounter;
            minFruitProbability = Math.min(prob, minFruitProbability);
            probabilityFruitWords.put(entry.getKey(), prob);
        }

        double computerProbability = 0.5;
        minFruitProbability = minFruitProbability / (Math.PI * 7);
        minComputerProbability = minComputerProbability / (Math.PI * 7);

        for (int i = 0; i < numberInput; i++){

            List<Double> probabilityFruit = new ArrayList<>();
            List<Double> probabilityComputer = new ArrayList<>();
            String inputStr = input.nextLine();
            inputStr = replaceCharacters(inputStr);
            String[] words = inputStr.split("\\s");
            double fruitProbability = 0.5;

            for (String s : words) {
                double computerWordsProbability = minFruitProbability;
                double fruitWordsProbability = minComputerProbability;
                computerWordsProbability = probabilityComputerWords.containsKey(s) ? probabilityComputerWords.get(s) : computerWordsProbability;
                fruitWordsProbability = probabilityFruitWords.containsKey(s) ? probabilityFruitWords.get(s) : fruitWordsProbability;
                if (computerWordsProbability != minFruitProbability) {
                    double P_word_computer = computerWordsProbability * computerProbability /
                            (computerWordsProbability * computerProbability + fruitWordsProbability * fruitProbability);
                    probabilityComputer.add(P_word_computer);
                }
                if (fruitWordsProbability != minComputerProbability) {
                    double P_word_fruit = fruitWordsProbability * fruitProbability /
                            (fruitWordsProbability * fruitProbability + computerWordsProbability * computerProbability);
                    probabilityFruit.add(P_word_fruit);
                }
            }

            double firstComputerCount = 1.0;
            double secondComputerCount = 1.0;
            for (double a : probabilityComputer){
                firstComputerCount = firstComputerCount * a;
                secondComputerCount = secondComputerCount * (1 - a);
            }
            double firstFruitCount = 1.0;
            double secondFruitCounter = 1.0;
            for (double a : probabilityFruit){
                firstFruitCount = firstFruitCount * a;
                secondFruitCounter = secondFruitCounter * (1 - a);
            }

            double finalComputerValue;
            double finalFruitValue;
            finalFruitValue = firstFruitCount / (firstFruitCount + secondFruitCounter);
            finalComputerValue = firstComputerCount / (firstComputerCount + secondComputerCount);
            if (finalComputerValue > finalFruitValue){
                if (finalComputerValue == 1.0){
                    System.out.println("computer-company");
                } else {
                    if (finalComputerValue - finalFruitValue < (Math.PI / 18)){
                        System.out.println("fruit");
                    } else {
                        System.out.println("computer-company");
                    }
                }
            } else {
                System.out.println("fruit");
            }
        }
    }

    static String replaceCharacters(String str){
        str = str.replaceAll("[^a-zA-Z0-9]+", " ");
        str = str.replaceAll("\\bto\\b","");
        str = str.replaceAll("\\bin\\b","");
        str = str.replaceAll("\\bis\\b","");
        str = str.replaceAll("\\bthe\\b","");
        str = str.replaceAll("\\bit\\b","");
        str = str.replaceAll("\\band\\b","");
        str = str.replaceAll("\\s{2,}", " ").trim();
        return str;
    }
}