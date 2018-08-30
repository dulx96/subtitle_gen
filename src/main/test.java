import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubParser {
	private static final String INPUT = "./sub.srt";
	private static final String OUTPUT = "./sub_passed.srt";

	private static final Pattern PATTERN_TIME = Pattern.compile("([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3}).*([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3})");
	private static final Pattern PATTERN_NUMBERS = Pattern.compile("(\\d+)");
	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public static void main(String[] args) {
		// Tokenizer tokenizer = Tokenizer.builder().build();
		// for(Token token : tokenizer.tokenize("寿司が食べたい。")) {

		// 	System.out.println(token.getSurfaceForm() + "\t" + token.getAllFeatures());
		// }
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fr = new FileReader(INPUT);
			br = new BufferedReader(fr);
			fw = new FileWriter(OUTPUT);
			bw = new BufferedWriter(fw);
			String sCurrentLine;

			while ((line = br.readLine()) != null ) {
					System.out.println('1');
				Matcher matcher = PATTERN_NUMBERS.matcher(line);
				if(matcher.find()) {
					System.out.println(line);
					line = br.readLine();

				}

				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {

				if( br != null)
					br.close();

				if(fr != null)
					fr.close();

				if(fw !=null )
					fw.close();
			} catch (IOException ex) {

				ex.printStackTrace();

		}

	}
}
}