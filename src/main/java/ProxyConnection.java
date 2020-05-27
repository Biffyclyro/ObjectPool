import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.function.Consumer;

public class ProxyConnection implements InvocationHandler {
    private final Connection connection;
    private final Consumer<Connection> callback;

    public ProxyConnection(Connection connection, Consumer<Connection> callback) {
        this.connection = connection;
        this.callback = callback;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println(method.getName());

        if (method.getName() == "close") this.callback.accept(connection);

        return method.invoke(connection, args);
    }
}
