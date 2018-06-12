import java.util.*;

public class ParserMain{
	public static void main(String args[]){

		ArrayList<String> input = new ArrayList<String>(Arrays.asList(
			"Si//DT",
			"Rizal//NN",
			"ay//LM",
			"ang//DT",
			"pambansang//JJ",
			"bayani//NN",
			"ng//CC",
			"Pilipinas//NN",
			".//."
		));
	   
		/*"Si//DT",
			"Rizal//NN",
			"ay//LM	",
			"ang//DT",
			"pambansang//JJ",
			"bayani//NN",
			"ng//CC",
			"Pilipinas//NN",
			".//."*/

		/*"Siya//PR",
			"at//CC",
			"si//DT",
			"kankong//NN",
			"ay//LM",
			"Malakas//JJ",
			".//."*/

		/*	"Siya//PR",
			"at//CC",
			"ako//PR",
			"ay//LM",
			"Malakas//JJ",
			".//."*/


			/*"Magtanim//VB",
			"ay//LM",
			"hindi//RB",
			"biro//noun",
			".//."*/

		SRParser parser = new SRParser();

		parser.readGrammarFile("grammar-rules3.txt");
		parser.processInput(input);
		parser.shiftReduceParser();
	}
}
