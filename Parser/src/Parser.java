import com.fathzer.soft.javaluator.DoubleEvaluator;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.*;


public class Parser {

    static String[] availableTypes = new String[]{"Spark"};
    Map<String, String> config;
    Writer w;
    boolean goingToWriteToFile = false;
    Map<String, Object> varValues;
    ArrayList<String> supportedTypes = new ArrayList<>();
    DoubleEvaluator doubleEvaluator = new DoubleEvaluator();


    public Parser() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("config.yaml");
        config = (Map<String, String>) yaml.load(inputStream);
        System.out.println(config);

        w = new Writer(config);
        supportedTypes.addAll(Arrays.asList(new String[]{"int", "double", "boolean"}));

        varValues = new HashMap<String, Object>();
    }

    public void updateVars(String line){
        String[] lineArr = line.trim().split(" ");
        if(lineArr.length > 0) {
            String name = lineArr[0];

            if (varValues.containsKey(name)){
                if (lineArr[1] == "=") {

                }


            }
        }
    }

    public void initVars(String func, String robotPath){
        File file = new File(robotPath);
        Scanner input;
        try {
            input = new Scanner(file);
            String line;
            boolean reading = false;
            int paren = 0;
            while(input.hasNextLine()) {
                line = input.nextLine();
                if(line.contains(func) && (line.contains("{") || line.contains("}"))) {
                    reading = true;
                }
                if(reading) {
                    line = line.trim();
                    String[] declaration = line.split(" ");
                    // int i = 10;
                    // int i;
                    if(supportedTypes.contains(declaration[0])){
                        if(declaration.length > 2){
                            String value = declaration[3].substring(0, declaration[3].length()-1);
                            String type = declaration[0];

                            switch(type){
                                case "int":
                                    varValues.put(declaration[1], Integer.parseInt(value));
                                    break;
                                case "double":
                                    varValues.put(declaration[1], Double.parseDouble(value));
                                    break;
                                case "boolean":
                                    varValues.put(declaration[1], Boolean.parseBoolean(value));
                                    break;
                                default:
                                    varValues.put(declaration[1], new Object());
                            }

                        } else {
                            String type = declaration[0];
                            String name = declaration[1].substring(0, declaration[1].length()-1);
                            switch(type){
                                case "int":
                                    varValues.put(name, 0);
                                    break;
                                case "double":
                                    varValues.put(name, 0.0);
                                    break;
                                case "boolean":
                                    varValues.put(name, false);
                                    break;
                                default:
                                    varValues.put(name, new Object());
                            }
                        }
                    }

                    for(char c : line.toCharArray()) {
                        if(c == '{') {
                            paren++;
                        }else if(c == '}') {
                            paren--;
                        }
                    }
                    if(paren == 0) {
                        break;
                    }
                }
            }
            input.close();
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(varValues.toString());
    }

    public void encodeFunction(String func, String robotPath) {
        initVars(func, robotPath);
        File file = new File(robotPath);
        Scanner input;
        String code = "";
        try {
            input = new Scanner(file);
            String line;
            boolean reading = false;
            int paren = 0;
            while(input.hasNextLine()) {
                line = input.nextLine();
                if(line.contains(func) && (line.contains("{") || line.contains("}"))) {
                    reading = true;
                }
                if(reading) {
                    if (!line.isEmpty()) {
                        while (Character.isWhitespace(line.charAt(0))) {
                            line = line.substring(1);
                            code += "\t";
                        }
                        code += line + "\n";
                        for (char c : line.toCharArray()) {
                            if (c == '{') {
                                paren++;
                            } else if (c == '}') {
                                paren--;
                            }
                        }
                        if (paren == 0) {
                            break;
                        }
                    }
                }
            }
            input.close();
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }

        if(!goingToWriteToFile) {
            act(code, robotPath, true);
            goingToWriteToFile = true;
        } else {
            writeFunction(code, robotPath, new ArrayList<String>(), false);
        }
    }

    // TODO
    public void writeFunction(String code, String subsystemFilePath, ArrayList<String> params, boolean writeToFile) {
        for(String param : params) {
            evalVariable(param);
        }
        act(code, subsystemFilePath, writeToFile);
    }

    // TODO
    public Object evalVariable(String param){
        return new Object();
    }

    //TODO
    public Object evalExpr(ArrayList<String> expr){
        String expr_string = "";

        for(String token : expr){
            expr_string += " " + token;
        }

        return doubleEvaluator.evaluate(expr_string);
    }

    // TODO
    public boolean evaluateBooleanExpr(String expr) {
        Scanner scan = new Scanner(expr);

        // model: expr = i > 1
        ArrayList<String> left = new ArrayList<>();
        ArrayList<String> right = new ArrayList<>();
        String operator = "";

        while(scan.hasNext()){
            String token = scan.next();
            if(token.equals(">") || token.equals("<") || token.equals("==") || token.equals("!=") || token.equals(">=") || token.equals("<=")){
                operator = token;
                break;
            } else {
                left.add(token);
            }
        }

        while(scan.hasNext()){
            String token = scan.next();
            right.add(token);
        }

        if(!operator.equals("")){
            Double left_value = (Double)evalExpr(left);
            Double right_value = (Double)evalExpr(right);

            switch (operator){
                case ">":
                    return left_value > right_value;
                case "<":
                    return left_value < right_value;
                case "==":
                    return left_value.equals(right_value);
                case ">=":
                    return left_value >= right_value;
                case "<=":
                    return left_value <= right_value;
            }
        }

        return false;
    }

    public void act(String code, String subsystemFileName, boolean writeToFile) {
        // Read DriveTrain.java to get attributes mapped to their type. attributeToType is this map.

        System.out.println("Code: " + code);

        HashMap<String, String> attributeToType = new HashMap<>();

        try {
            Scanner scan = new Scanner(new File(subsystemFileName));

            while (scan.hasNext()) {
                String line = scan.nextLine();

                for (String type : availableTypes) {
                    if (!line.contains("(") && !line.contains(")") && (line.contains("public") || line.contains("private")) && line.contains(type)) {
                        Scanner lineScan = new Scanner(line);

                        String prevWord = "";

                        while (lineScan.hasNext()) {
                            String word = lineScan.next();

                            if (prevWord.equals(type)) {
                                attributeToType.put(word.replaceAll("[^a-zA-Z ]", ""), type);
                                break;
                            }

                            prevWord = word;
                        }
                    }
                }
            }

            // System.out.println(attributeToType);

            // Parse code and write to c
            Scanner codeScan = new Scanner(code);

            while (codeScan.hasNext()) {
                String line = codeScan.nextLine().trim();

                if(line.matches(".*for.*")){
                    Scanner forScanner = new Scanner(line);

                    while(forScanner.hasNext()){
                        if(forScanner.next().equals("for")){
                            break;
                        }
                    }

                    // now at the while loop. we need to call act the required number of times.
                    // we assume the for loop to be structured as for (int i = 0; i < k; i++)

                    int firstBound = 0;

                    while(forScanner.hasNext()){
                        try{
                            String x = forScanner.next();
                            firstBound = Integer.parseInt(x);
                            break;
                        } catch (NumberFormatException e){
                        }
                    }

                    int secondBound = 0;

                    while(forScanner.hasNext()){
                        try{
                            secondBound = Integer.parseInt(forScanner.next());
                            break;
                        } catch (NumberFormatException e){
                        }
                    }

                    // Extract block of code inside the for loop
                    String forBody = "";
                    while(codeScan.hasNext()){
                        int numParantheses = 1;

                        String scanLineRaw = codeScan.nextLine();
                        String scanLine = scanLineRaw.trim();

//                        System.out.println("scanLine: " + scanLine);

                        for(int i = 0; i < scanLine.length(); i++){
                            if(scanLine.charAt(i) == '}'){
                                numParantheses--;
                            }

                            if(scanLine.charAt(i) == '{'){
                                numParantheses++;
                            }
                        }
                        if(numParantheses == 0){
                            break;
                        } else {
                            forBody+=scanLine;
                        }
                    }

                    System.out.println("First bound: " + firstBound);
                    System.out.println("Second bound: " + secondBound);
                    for(int i = firstBound; i < secondBound; i++){
                        act(forBody, subsystemFileName, !writeToFile);
                    }
                }

                for (String attribute : attributeToType.keySet()) {
                    if (line.contains(attribute)) {

                        int startIdx = 0;
                        int endIdx = 0;
                        int paramEndIdx = 0;

                        // Get start of function string
                        for (int i = 0; i < line.length(); i++) {
                            if (line.charAt(i) == '.') {
                                startIdx = i;
                                break;
                            }
                        }

                        // Get end of function string
                        for (int j = startIdx + 1; j < line.length(); j++) {
                            if (line.charAt(j) == '(') {
                                endIdx = j;
                                break;
                            }
                        }

                        for (int k = endIdx + 1; k < line.length(); k++) {
                            if (line.charAt(k) == ')') {
                                paramEndIdx = k;
                                break;
                            }
                        }

                        String function = line.substring(startIdx + 1, endIdx);
                        String[] params = new String[]{line.substring(endIdx + 1, paramEndIdx)};
                        w.set(function, attributeToType.get(attribute), attribute, params);
                    }
                }
            }

            if(writeToFile) {
                writeToC();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToC() {
        w.writeToFile();
        try {
            w.getF().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class Writer {

    private FileWriter f;
    ArrayList<Map<String, Object>> sparksSet = new ArrayList<>();
    Map<String, String> config;

    public Writer(Map<String, String> config) {
        this.config = config;
        try {
            f = new FileWriter(new File("Controller1.cs"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile() {
        try {

            String buffer = "";

            f.append("using UnityEngine;\n" +
                    "using System.Collections;\n" +
                    "using System.Collections.Generic;\n" +
                    "\n" +
                    "public class Controller1 : MonoBehaviour {\n" +
                    "    public List<AxleInfo> axleInfos; // the information about each individual axle\n" +
                    "    public float maxMotorTorque; // maximum torque the motor can apply to wheel\n" +
                    "    public float maxSteeringAngle; // maximum steer angle the wheel can have\n" +
                    "    public float torqueMultiplier; // Multiplier for torque\n" +
                    "    public float rpmMultiplier; // Multiplier for torque\n" +
                    "\n" +
                    "    public void FixedUpdate()\n" +
                    "    {\n" +
                    "        float motor = maxMotorTorque * Input.GetAxis(\"Vertical\");\n" +
                    "        float steering = maxSteeringAngle * Input.GetAxis(\"Horizontal\");\n" +
                    "\n" +
                    "        foreach (AxleInfo axleInfo in axleInfos) {\n" +
                    "            if (axleInfo.motor) {\n");

            for(Map<String, Object> sparkToSet: sparksSet){
                String component = (String) sparkToSet.get("component");
                String function = (String) sparkToSet.get("function");

                if(function.equals("set")) {
                    String[] params = (String[]) sparkToSet.get("params");

                    buffer += "                float target" + config.get(component) + "RPM = " + params[0] + "; // fill in with code\n" +
                    "                if (axleInfo." + config.get(component) + ".rpm <= rpmMultiplier*target" + config.get(component) + "RPM){\n"+
                    "                    axleInfo." + config.get(component) + ".motorTorque = maxMotorTorque;\n" +
                    "                } else {\n"+
                    "                    axleInfo." + config.get(component) + ".motorTorque = 0;\n" +
                    "                }\n";
                }
            }


            f.append(buffer +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "}\n" +
                    "\n" +
                    "[System.Serializable]\n" +
                    "public class AxleInfo {\n" +
                    "    public WheelCollider leftWheel;\n" +
                    "    public WheelCollider rightWheel;\n" +
                    "    public bool motor; // is this wheel attached to motor?\n" +
                    "    public bool steering; // does this wheel apply steer angle?\n" +
                    "}");

//            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void set(String function, String type, String component, String[] params) {

        System.out.print(function + " ");
        System.out.print(type + " ");
        System.out.print(component + " ");
        System.out.println(params[0] + " ");

        if (type.equals("Spark")) {
            Map<String, Object> toAppend = new HashMap<>();

            toAppend.put("function", function);
            toAppend.put("type", type);
            toAppend.put("component", component);
            toAppend.put("params", params);

            sparksSet.add(toAppend);
        }
    }

    public FileWriter getF(){
        return f;
    }
}