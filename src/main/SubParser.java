import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;
import com.mariten.kanatools.KanaConverter;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SubParser {
	
	private static final Pattern PATTERN_TIME = Pattern.compile("([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3}).*([\\d]{2}:[\\d]{2}:[\\d]{2},[\\d]{3})");
	private static final Pattern PATTERN_NUMBERS = Pattern.compile("(\\d+)");
	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	private static final Pattern PATTERN_SINGLETIME = Pattern.compile("([\\d]{2}):([\\d]{2}):([\\d]{2}),([\\d]{3})");
	private static int timeMs(String text) {
			Matcher matcher = PATTERN_SINGLETIME.matcher(text);
			if(matcher.find()) {
				int[] time = new int[5];
				for(int i = 1; i < 5 ; i++) {
					time[i-1] = Integer.parseInt(matcher.group(i));
				}
			return time[0] * 3600000 + time[1] * 60000 + time[2] * 1000 + time[3];			
			}
			else return 0;
		}
	private static String cleanSub(String sub) {
		String temp = "";
		temp = sub.replaceAll("<[^>]*>", "");// clear all tags
		temp = temp.replaceAll("\"","”"); // clear \" character
		String regex1 = "\\x5C\\x4E";
		temp = temp.replaceAll(regex1," "); // clear \N from ass 
		return temp;
	}
	public static void main(String[] args) {
		Tokenizer tokenizer = Tokenizer.builder().build();
		BufferedReader br = null;
		FileReader fr = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		StringBuilder srt = null;

		String INPUT=null;
		String OUTPUT=null;
		
		int x = 0;
		int y = 0;
		
		//convert kata to hira
		int conv_op_flags = KanaConverter.OP_ZEN_KATA_TO_ZEN_HIRA | KanaConverter.OP_ZEN_ASCII_TO_HAN_ASCII;
		// int conv_op_flags = KanaConverter.OP_HAN_KATA_TO_ZEN_KATA | KanaConverter.OP_ZEN_ASCII_TO_HAN_ASCII;

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

			//start read file
			bw.write("[");
			while ((line = br.readLine()) != null ) {
				if (x++!=0) bw.write(",");
				//id
				Matcher matcher = PATTERN_NUMBERS.matcher(line);
				if(matcher.find()) {
					bw.write("{");
					bw.write("\"id\":"+x+",");
					line = br.readLine();
				}

				//time
				matcher = PATTERN_TIME.matcher(line);

				if(matcher.find()) {
					String[] parts = line.split(" ");
					bw.write("\"startTime\":"+ timeMs(parts[0])+",");
					bw.write("\"endTime\":"+ timeMs(parts[2])+",");
				}

				//text
				String aux;
				bw.write("\"text\":");
				bw.write("[");
				while((aux = br.readLine()) != null && !aux.isEmpty()) {
					srt.append(aux);
					line = srt.toString();
					if (line != null && !line.isEmpty())
						line = cleanSub(line);
						System.out.println(line);
						// line = line.replaceAll("<[^>]*>", "");// clear all tags
						// line = line.replaceAll("\"","”"); // clear \" character
					srt.setLength(0);

				
					for(Token token : tokenizer.tokenize(line)) {
						if (y!=0) bw.write(",");
						y = y + 1;
						bw.write("{");
						bw.write("\"surfaceForm\":"+"\""+token.getSurfaceForm()+"\""+",");

						if(token.getBaseForm() == null) {
							bw.write("\"baseForm\":"+"null"+",");	

						} else if(token.getBaseForm().equals("null")) {
							bw.write("\"baseForm\":"+"null"+",");	
						} else {
							bw.write("\"baseForm\":"+"\""+token.getBaseForm()+"\""+",");							
						}
						String reading = token.getReading();
						bw.write("\"reading\":"+"\""+reading+"\""+",");
						if(reading == null) {
							bw.write("\"hiragana\":"+"\""+reading+"\""+",");
						} else {
							bw.write("\"hiragana\":"+"\""+KanaConverter.convertKana(token.getReading(), conv_op_flags)+"\""+",");							
						}
						bw.write("\"partOfSpeech\":"+"\""+token.getPartOfSpeech()+"\""+",");								
						bw.write("\"isUnknown\":"+token.isUnknown());								
						bw.write("}");
						line = srt.toString();
					}
					srt.setLength(0);
				}
					y = 0;
					bw.write("]");
				bw.write("}");
			}
			bw.write("]");


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
