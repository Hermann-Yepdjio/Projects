import java.sql.PreparedStatement;
import java.sql.Types;

public class Utility {
	
	protected static void setString(PreparedStatement p, int index, String s) throws Exception {
		if (s == null) {
			p.setNull(index, Types.NULL);
		}
		else {
			p.setString(index, s);
		}
	}
	
	protected static void setInt(PreparedStatement p, int index, Integer n) throws Exception {
		if (n == null) {
			p.setNull(index, Types.NULL);
		}
		else {
			p.setInt(index, n);
		}
	}
	
	protected static void setDate(PreparedStatement p, int index, java.util.Date d) throws Exception {
		if (d == null) {
			p.setNull(index, Types.NULL);
		}
		else {
			p.setDate(index, new java.sql.Date(d.getTime()));
		}
	}
	
}
