public class Runner {
    public static void main(String[] args) {
        Parser parser = new Parser();

        String codeToParse = "public void setSpeed(double leftVal, double rightVal){\n" +
                "    \tleftMotor.set(1);\n" +
                "    \trightMotor.set(1);\n"+
                "    }";

        parser.act(codeToParse,"C:\\Users\\rao_a\\Desktop\\Coding\\robotics\\CHS_SIM_active\\Simulator\\src\\org\\usfirst\\frc\\team2473\\robot\\subsystems\\DriveTrain.java");
    }
}
