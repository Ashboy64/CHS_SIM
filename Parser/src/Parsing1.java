import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Parsing1 {

	public static void main(String[] args) throws FileNotFoundException {
		parseRobot("driveTrain","");
	}
	public static void parseRobot(String variable, String function) throws FileNotFoundException {
		Scanner scan = new Scanner(new File("/Users/Angela/git/CHS_SIM/Simulator/src/org/usfirst/frc/team2473/robot/Robot.java"));
		String type = " ";
		boolean found = false;
		while(scan.hasNextLine() && !found) {
			String line = scan.nextLine();
			//System.out.println(line);
			if(line.contains(" "+variable)) {
				StringTokenizer st = new StringTokenizer(line,"; ");
				String previous = "";
				while(st.hasMoreTokens()) {
					String current = st.nextToken();
					//System.out.println(current);
					if(current.equals(variable)) {
						type = previous;
						found = true;
						break;
					}
					previous = current;
				}
			}
		}
		System.out.println(type);
	}
	
} 