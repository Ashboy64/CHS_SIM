import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Parser {

    static String[] availableTypes = new String[]{"Spark"};
    Map<String, String> config;
    Writer w;

    public Parser() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("config.yaml");
        config = (Map<String, String>) yaml.load(inputStream);
        System.out.println(config);

        w = new Writer(config);
    }


    public void act(String code, String subsystemFileName) {
        // Read DriveTrain.java to get attributes mapped to their type. attributeToType is this map.

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

//            System.out.println(attributeToType);

            // Parse code and write to c
            Scanner codeScan = new Scanner(code);

            while (codeScan.hasNext()) {
                String line = codeScan.nextLine().trim();
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

            writeToC();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToC() {
        w.writeToFile();
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
                    buffer += "                axleInfo." + config.get(component) + ".motorTorque = " + params[0] + ";\n";
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

            f.close();
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
}