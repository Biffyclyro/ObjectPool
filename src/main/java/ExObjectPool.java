import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class ExObjectPool {


    public static void main(String[] args) {

        final var objectPool = new PoolConcrete(Printador::new);
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
        private PoolConcrete<Printador> pool;
        Printador obj;

        public ThreadTeste(PoolConcrete pool) {
            this.pool = pool;
        }


        @Override
        public void run() {

            this.obj = pool.acquire();

            obj.printar();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            pool.release(obj);



        }
    }
}
