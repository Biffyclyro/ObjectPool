import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExObjectPool {


    public static void main(String[] args) throws SQLException {

        final var ds = new PGSimpleDataSource();
        ds.setDatabaseName("");
        ds.setUser("");
        ds.setPassword("");

        final var objectPool = new PoolConcrete(ds);
        final int NUM_THREADS = 30;
        final List<Thread> threads = new ArrayList<>(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++){

            ThreadTeste t = new ThreadTeste(objectPool);
            var thread = new Thread(t);
            thread.start();

            threads.add(thread);
        }

        while (true) {
            threads.forEach(t -> {
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }

    }



    static class ThreadTeste implements Runnable{
        private PoolConcrete pool;
        Connection conn;

        public ThreadTeste(PoolConcrete pool) {
            this.pool = pool;
        }


        @Override
        public void run() {

            try {
                this.conn = pool.acquire();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }



            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                this.conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }
    }
}
