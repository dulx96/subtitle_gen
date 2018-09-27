import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubParser {
	
	private static final Pattern PATTERN_TIME = Pattern.compile("([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3}).*([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3})");
	private static final Pattern PATTERN_NUMBERS = Pattern.compile("(\\d+)");
	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	public static void main(String[] args) {
		Tokenizer tokenizer = Tokenizer.builder().build();
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		StringBuilder srt = null;

		String INPUT=null;
		String OUTPUT=null;

		if(args.length < 2)
			System.out.println("Nhap file dau vao, dau ra!!!");
		else {
			INPUT = args[0];
			OUTPUT = args[1];
		}
		

		try {
			fr = new FileReader(INPUT);
			br = new BufferedReader(fr);
			fw = new FileWriter(OUTPUT);
			bw = new BufferedWriter(fw);
			String line;
			srt = new StringBuilder();

			while ((line = br.readLine()) != null ) {
				Matcher matcher = PATTERN_NUMBERS.matcher(line);
				if(matcher.find()) {
					bw.write(line);
					bw.newLine();
					line = br.readLine();

				}
				matcher = PATTERN_TIME.matcher(line);

				if(matcher.find()) {
					bw.write(line);
					bw.newLine();
				}

				String aux;

				while((aux = br.readLine()) != null && !aux.isEmpty()) {
					srt.append(aux);
					line = srt.toString();
					if (line != null && !line.isEmpty())
						line = line.replaceAll("<[^>]*>", "");// clear all tags
					srt.setLength(0);

					for(Token token : tokenizer.tokenize(line)) {
						srt.append("<span data-baseform=\"");
						srt.append(token.getBaseForm());
						srt.append("\">");
						srt.append(token.getSurfaceForm());
						srt.append("</span>");	
						line = srt.toString();
					}
					bw.write(line);
					bw.newLine();
					srt.setLength(0);
				}
				bw.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {

				if( br != null)
					br.close();

				if(fr != null)
					fr.close();

				if(bw !=null )
					bw.close();

				if(fw != null )
					fw.close();

				
			} catch (IOException ex) {

				ex.printStackTrace();
				System.exit(1);

		}

	}
}
}
