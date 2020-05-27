import java.sql.Connection;
import java.sql.SQLException;

public interface Pool {
    Connection acquire() throws SQLException;
}
