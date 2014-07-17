package NECUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringUtil {
	
	
	public static Set<String> string2Set(String s) {
		Set<String> rets = new HashSet<String>();
		if (s != null && s != "") {
			s = s.replace("[", "");
			s = s.replace("]", "");
			String[] es = s.split("[,]");
			for (String ss : es) {
				ss = ss.trim();
				if (ss != null && ss != " ")
					rets.add(ss);
			}
		}
			
		return rets;
	}

	public static void main(String[] args) {
		Set<String> ret = StringUtil.string2Set("[payment, day, date, penalty, period, without, first]");
		for (String s : ret)
			System.out.println(s);

	}

}
