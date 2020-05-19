import java.util.*;
import java.util.function.Supplier;

public class PoolConcrete<T> implements Pool<T> {
    private int TAM_MAX = 20;
    private List<T> emUso = new ArrayList<>(20);
    private Queue<T> livres = new LinkedList<>();
    private final Supplier<T> objSupplier;


    public PoolConcrete(Supplier<T> objSupplier) {
        this.objSupplier = objSupplier;

        for (int i = 0; i < 3; i++) {
            livres.add(this.objSupplier.get());
        }
    }

    @Override
    public synchronized T acquire() {


        if (this.emUso.size() == this.TAM_MAX) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final T obj = Optional.ofNullable(livres.poll())
                .orElse(this.objSupplier.get());

        this.emUso.add(obj);

        System.out.println(this.emUso.size());

        return obj;


    }

    @Override
    public synchronized void release(T obj) {

        this.emUso.remove(obj);
        this.livres.add(obj);
        this.notify();


    }

}
