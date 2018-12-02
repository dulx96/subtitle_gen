# subtitle_gen

1.Install Java 8
https://medium.com/coderscorner/installing-oracle-java-8-in-ubuntu-16-10-845507b13343

2. clone git về home

https://github.com/dulx96/subtitle_gen.git

3. Biên dịch 
cd  home 

javac -encoding UTF-8 -cp subtitle_gen:subtitle_gen/lib/kuromoji-0.7.7.jar:subtitle_gen/lib/kanatools-1.3.0.jar subtitle_gen/src/main/SubParser.java



4. Convert 

cd  home
	
java -Dfile.encoding=UTF-8 -cp subtitle_gen/lib/kuromoji-0.7.7.jar:subtitle_gen/lib/kanatools-1.3.0.jar:subtitle_gen/src/main/ SubParser [input] [output].txt



Khuyến nghị đặt ngay ở home


