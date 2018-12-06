import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Parsing1 {
	
	public static String robotPath = "";

	public static void main(String[] args) throws FileNotFoundException {
		parseRobot("driveTrain","");
	}
	public static void parseRobot(String variable, String function) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(robotPath));
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
		egg(type,function);
	}
	
	public static void egg(String cl, String func) {
		File file = new File(robotPath);
		Scanner input;
		String code = "";
		try {
			input = new Scanner(file);
			String line = "";
			boolean reading = false;
			int paren = 0;
			while(input.hasNextLine()) {
				line = input.nextLine();
				if(line.contains(func) && (line.contains("{") || line.contains("}"))) {
					reading = true;
				}
				if(reading) {
					while(Character.isWhitespace(line.charAt(0))) {
						line = line.substring(1);
						code += "\t";
					}
					code += line + "\n";
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
		
		//System.out.println(code);
		//return code;
	}
} 
