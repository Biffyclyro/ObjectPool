import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

public class PoolConcrete implements Pool {
    private final DataSource dataSource;
    private int TAM_MAX = 20;
    private List<Connection> emUso = new ArrayList<>(20);
    private Queue<Connection> livres = new LinkedList<>();


    public PoolConcrete(DataSource dataSource) throws SQLException {

        this.dataSource = dataSource;

        for (int i = 0; i < 3; i++) {
            livres.add((Connection) Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    new Class[]{Connection.class},
                    new ProxyConnection(this.dataSource.getConnection(),
                                        this::release)));
        }
    }

    @Override
    public synchronized Connection acquire() throws SQLException {


        if (this.emUso.size() == this.TAM_MAX) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final Connection conn = Optional.ofNullable(livres.poll())
                .orElse((Connection) Proxy.newProxyInstance(
                        this.getClass().getClassLoader(),
                        new Class[]{Connection.class},
                        new ProxyConnection(dataSource.getConnection(), this::release)

                ));

        this.emUso.add(conn);

        System.out.println(this.emUso.size());

        return conn;


    }

    private synchronized void release(Connection obj) {

        this.emUso.remove(obj);
        this.livres.add(obj);
        this.notify();


    }

}
