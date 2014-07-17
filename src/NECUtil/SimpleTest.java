package NECUtil;

import cue.lang.WordIterator;
import cue.lang.stop.StopWords;

public class SimpleTest {

	public static void main(String[] args) {
		String s = "Add to list (0)" + 
				"William meaning and name origin" + 
				"William  wi (l)- liam as a boy's name is pronounced WIL-yum. It is of Old German origin, and the meaning of William is \"will helmet , protection \". For a long time after the Norman conquest in AD 1066 , three out of four English boys were given some form of the conqueror 's name, William. Short forms and variants came into being with a common basic meaning of \"will\", \" determined \", or \" resolute \". William has remained a royal name in the UK for nearly one thousand years. The firstborn son of the current Prince of Wales is named William. Wilhelm is a German form; actor Willem Dafoe has made the Dutch form familiar since the 1980s . See also Willard . Playwright William Shakespeare ; actors William Hurt, Billy Crudup , Bill Pullman; film director Wim Wenders; poet William Blake; author William Faulkner; American Presidents William H. Harrison, William H. Taft, William McKinley; Bill Clinton." + 
				"For more information, see also the related name Bo ." + 
				"Baby names that sound like William are Eilam and Eliam ." + 
				"View a list of the 48 names that reference William ." + 
				"Popularity of William" + 
				"William is a very popular first name for men (#5 out of 1220) and also a very popular surname or last name for all people (#731 out of 88799). (1990 U.S. Census)" + 
				"Displayed below is the baby names popularity of the name William for boys. (2012 statistics)" + 
				"Compare William with its variant forms and related boy baby names. " + 
				"Discussion";
		for (String word : new WordIterator(s)) {
			word = word.toLowerCase();
			if (!StopWords.English.isStopWord(word))
				System.out.println(word);
		}

	}

}
