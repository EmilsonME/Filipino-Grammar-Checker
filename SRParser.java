import java.util.*;
import java.io.File;
import java.io.IOException;


public class SRParser{

	private TreeMap<String, String[]> terminal, nonTerminal;
	private TreeMap<String, String> posTag;
	private ArrayList<TreeData> treeData;
	private ArrayList<Tree<String>> tempStore;
	private ArrayList<String> intputBuffer;
	private Stack<String> stack;
	private String startSymbol;
	private File grammar;
	private Scanner scanner;
  	
  	int gblCtr = 0;


  	private ArrayList<Tree<String>> treeNonTerminals;

	public SRParser(){

		terminal = new TreeMap<String, String[]>();
		nonTerminal = new TreeMap<String, String[]>();
		treeData = new ArrayList<TreeData>();
		posTag = new TreeMap<String, String>();
		stack = new Stack<String>();
		intputBuffer = new ArrayList<String>();
		treeNonTerminals = new ArrayList<Tree<String>>();
		tempStore = new ArrayList<Tree<String>>();

	}

	public void readGrammarFile(String grammarFile){
		grammar = null;
		scanner = null;

		try{
			grammar = new File(grammarFile);
			scanner = new Scanner(grammar);

			String[] line = scanner.nextLine().replaceAll("\\s+","").split(":=");
			startSymbol = line[0];


			do{
				if (line.length == 1) {
					line = scanner.nextLine().replaceAll("\\s+","").split(":=");
					continue;
				}else{

					String[] rightHandSide = line[1].toString().split("!!");

					if(!line[1].contains("'"))
						nonTerminal.put(line[0], rightHandSide);
					else
						terminal.put(line[0], rightHandSide);

					//initGrammar(line[0]);

					if(scanner.hasNextLine()){
						line = scanner.nextLine().replaceAll("\\s+","").split(":=");

					}else{

						line = null;
					}
				}

			}while(line != null);

			/*System.out.println("Nonterminals: ");
				for(String s: nonTerminal.keySet()){
		     		System.out.print("key: "+ s  + " value: " );
			
	         	 	for(String v: nonTerminal.get(s)){
	         			System.out.print(v +" ");
	          		}
	          		System.out.println();
	            }*/
	    //     System.out.println("terminals: ");
	    //    	    for(String s: terminal.keySet()){
			// 	  	System.out.print("key: "+ s  + " value: " );
			//
	    //       		for(String v: terminal.get(s)){
	    //       			System.out.print(v +" " );
	    //       		}
	    //       		System.out.println();
	    //     	}

		}catch(Exception exeption){
			exeption.printStackTrace();
		}

	}
	public void processInput(ArrayList<String> input){

		try{
			for(String word_tag: input){
				String[] line = word_tag.split("//");
				String strTag = getTag(line[1]);

				intputBuffer.add(line[0]);
				posTag.put(line[0], strTag);

			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public String getTag(String symbol){
		switch(symbol){
			case "NN" : return "<pangngalan>";
			case "PR" : return "<panghalip>";
			case "DT" : return "<pantukoy>";
			case "LM" : return "<pangawing>";
			case "CC" : return "<pangatnig>";
			case "VB" : return "<pandiwa>";
			case "JJ" : return "<panguri>";
			case "RB" : return "<pangabay>";
			case "CD" : return "<kardinal_Num>";
			case "TS" : return "<topicless>";
			default : return "<bantas>";
		}
	}

	public void shiftReduceParser(){
		System.out.println("INPUT SENTENCE: " + intputBuffer);
		try{
			stack.push("$");
			shift();
        	do{
	            makeFeats();
	            if(stack.size() == 2 && stack.peek().equals(startSymbol)){
	            	stack.pop();
	            	displayAction("reduce");
	            	System.out.println("final_stack:" +stack);
		        	System.out.println("final_queue:" + intputBuffer);
	            	System.out.println();
	            	System.out.println("******* The senctence is accepted. *****");

	            }

          	}while(!stack.peek().equals("$"));

	        createTree();


		}catch(Exception e){
			e.printStackTrace();
		}

	}
	public void makeFeats(){
		boolean checker;
		if(posTag.containsKey(stack.peek())){
			reduce(1, posTag.get(stack.peek()));
		}else if((checker = productionFinder())){
           // System.out.println(stack.size());
		}else{
			shift();
		}

	}
	public void shift(){
		if(intputBuffer.size() != 0){
			stack.push(intputBuffer.get(0));
			intputBuffer.remove(0);
			displayAction("shift");
		}

	}
	public void reduce(int numOfElements, String strToPush){
		ArrayList<String> arrTemp = new ArrayList<String>();

		for(int intLooper = 0; intLooper < numOfElements; intLooper++){
			String popped = stack.pop();
			arrTemp.add(0, popped);

		}
		if(strToPush != null)
			stack.push(strToPush);

		Tree<String> tree = new Tree<String>(strToPush + ++gblCtr);
		
		if(arrTemp.size() == 1 && posTag.containsKey(arrTemp.get(0))){	

			tree.addChild(arrTemp.get(0));
			tempStore.add(tree);

		}else{

			int x = tempStore.size()-1;
			int ctr = 0;

			for(; ctr < numOfElements; x--, ctr++){

				tree.addChild(tempStore.remove(x));

			}

			tempStore.add(tree);

		}
			
		
		
		treeNonTerminals.add(tree);
        displayAction("reduce");
	}
	public boolean productionFinder(){
		boolean flag = false;
		int intCtr;
		ArrayList<String> arrSubstring = new ArrayList<String>();
		for(int intLooper = stack.size() - 1  ; intLooper > -1 ; intLooper--){

			intCtr = 0;
			String substring= ""; 
			for(int intLooper2 = intLooper; intLooper2  < stack.size()  ; intLooper2++, intCtr++){
				substring =  substring +"," + stack.get(intLooper2);
			}
			StringBuilder sb = new StringBuilder(substring);
			substring = sb.deleteCharAt(0).toString();

			arrSubstring.add(substring);

		}
		for(int ctr = arrSubstring.size() - 1; ctr >= 0; ctr--){
			for(String key: nonTerminal.keySet()){
				for(String value: nonTerminal.get(key)){
					if(arrSubstring.get(ctr).equals(value)){
						flag = true;
						String[] strTemp = arrSubstring.get(ctr).split(",");
						if(lookAHead()){
							reduce(strTemp.length, key);
							return flag;
						}else{
							return false;
						}
					}

				}
			}

		}
		return flag;
	}
	public boolean lookAHead(){
		String substr2 = "";
		ArrayList<String> arrSubstring = new ArrayList<String>();

		for(int ctr = 0 ; ctr <  intputBuffer.size() - 1; ctr ++){
			String substr="";
		    substr2 =  substr2  + "," + posTag.get(intputBuffer.get(ctr));
			for(int ctr2 = 1; ctr2 < stack.size(); ctr2 ++){
				substr += "," + stack.get(ctr2) ;
			}
			substr = substr + substr2;

			StringBuilder sb = new StringBuilder(substr);

		    substr = sb.deleteCharAt(0).toString();

			arrSubstring.add(substr);
		}
		substr2 = "";

		int tempCtr = 0;
		for(int ctr = 0 ; ctr <  intputBuffer.size() - 1; ctr++){

		    substr2 =  substr2  + "," + posTag.get(intputBuffer.get(ctr));
		    if(tempCtr == 0){
				StringBuilder sb = new StringBuilder(substr2);
			    substr2 = sb.deleteCharAt(0).toString();
			    tempCtr = 1;
			}
			for(int inner = stack.size() -1; inner >=1; inner--){
				String substr="";
				for(int ctr2 = inner; ctr2 < stack.size(); ctr2++){
					substr = substr + ","  +stack.get(ctr2) ;

				}
				StringBuilder sb2 = new StringBuilder(substr);
				substr = sb2.deleteCharAt(0).toString();


				substr = substr + "," + substr2;
				arrSubstring.add(substr);
			}



		}
		for(int ctr = arrSubstring.size() - 1; ctr >= 0; ctr--){
			//System.out.println(arrSubstring.get(ctr));
			for(String key: nonTerminal.keySet()){
				for(String value: nonTerminal.get(key)){
					//System.out.println(arrSubstring.get(ctr) + "->" + value);
					if(arrSubstring.get(ctr).equals(value)){
						//System.out.println("k" + value);
						return false;
					}

				}
			}

		}

		return true;

	}

	public void displayAction(String action){

		System.out.println(stack + " || " + intputBuffer + "  [ " + action + " ]");

	}

	private void createTree(){
		/*for(Tree<String> obj: treeNonTerminals){
			System.out.println("terr    " + obj.getData() );
		}*/
	
	    treeNonTerminals.get(treeNonTerminals.size()- 1).goToChildren();	//printing the tree using BFS
		
	}
	
	
}

