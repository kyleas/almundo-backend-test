import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h1>Dispatcher</h1>
 * <p>Clase que despacha las llamadas entrantes del call center y las distribuye entre los empleados dada la prioridad
 * de cada uno.</p>
 *
 * @author Christian Bonilla
 */
public class Dispatcher {

    // Clase Provisional para almacenar los resultados de la ejecución
    public class Resultado {

        Empleado empleado;
        int llamada;
        // Tipo de resultado: 0 asignación, 1 finalización
        int tipo;

        Resultado(Empleado e, int l, int t) {
            empleado = e;
            llamada = l;
            tipo = t;
        }
    }

    private CompletableFuture future;

    private PriorityBlockingQueue<Empleado> empleados;
    private Queue<Integer> llamadas;
    private ArrayBlockingQueue<Integer> llamadasEnCurso;
    private AtomicInteger contador;

    // Arreglos provisionales para probar la concurrencia
    private ArrayList<Resultado> resultados;

    /**
     * <h2>Dispatcher</h2>
     * <p>Construye un objeto {@link Dispatcher}</p>
     */
    Dispatcher() {
        empleados = new PriorityBlockingQueue<>();
        llamadas = new LinkedList<>();
        llamadasEnCurso = new ArrayBlockingQueue<>(10);
        contador = new AtomicInteger(0);
        resultados = new ArrayList<>();
        future = new CompletableFuture();
    }

    /**
     * <h2>agregarEmpleado</h2>
     * <p>Agrega un nuevo {@link Empleado} a la cola prioritaria para atender llamadas.</p>
     *
     * @param id {@link Integer} Identificador del empleado
     * @param rol {@link String} Nombre de rol del empleado
     */
    public void agregarEmpleado(int id, String rol) {
        this.empleados.offer(new Empleado(id, rol));
    }

    /**
     * <h2>procesarLlamada</h2>
     * <p>Función que procesa la siguiente llamada en cola. Si hay algún {@link Empleado} disponible.</p>
     */
    private void procesarLlamada() {
        if (!empleados.isEmpty() && !llamadas.isEmpty()) {
            Empleado e = empleados.poll();
            int llamada = llamadas.poll();
            llamadasEnCurso.add(llamada);
            Resultado r = new Resultado(e, llamada, 0);
            resultados.add(r);

            CompletableFuture<Empleado> f = e.atenderLlamada();
            f.thenAccept(empleado -> this.onLlamadaDespachada(empleado, llamada));
        }
    }

    /**
     * <h2>onLlamadaDespachada</h2>
     * <p>Función callback que se ejecuta cuando un {@link Empleado} ha finalizado la atención de una llamada.</p>
     * @param e {@link Empleado} El empleado que ha finalizado la atención
     * @param llamada {@link Integer} El número de llamada despachada
     */
    private void onLlamadaDespachada(Empleado e, int llamada) {
        empleados.offer(e);
        llamadasEnCurso.remove(llamada);
        Resultado r = new Resultado(e, llamada, 1);
        resultados.add(r);
        if (!llamadas.isEmpty()) {
            procesarLlamada();
        }
        if (llamadas.isEmpty() && !future.isDone() && llamadasEnCurso.isEmpty()) {
            future.complete(null);
        }
    }

    /**
     * <h2>dispatchCall</h2>
     * <p>Función que se encarga de despachar una nueva llamada.</p>
     */
    private void dispatchCall() {
        llamadas.add(contador.incrementAndGet());
        procesarLlamada();
    }

    /**
     * <h2>dispatch</h2>
     * <p>Función provisional que despacha {@code num} número de llamadas.</p>
     * @param num Número de llamadas a despachar
     * @throws InterruptedException Arroja excepciones debdio a la naturaleza asíncrona de los futuros
     * @throws ExecutionException Arroja excepciones debdio a la naturaleza asíncrona de los futuros
     */
    public void dispatch(int num) throws InterruptedException, ExecutionException {
        for (int i = 0; i < num; i++) {
            dispatchCall();
        }

        future.get();
    }

    /**
     * <h2>getFuture</h2>
     * <p>Retorna el futuro asociado a la clase que se cumple cuando se despachan todas las llamdas</p>
     * @return {@link CompletableFuture} Futuro completable
     */
    public CompletableFuture getFuture() {
        return future;
    }

    /**
     * <h2>getResultados</h2>
     * <p>Obtiene los resultados de la ejecución del progrma.</p>
     * @return {@link ArrayList} Lista con los resultados de la ejecución
     */
    public ArrayList<Resultado> getResultados() {
        return resultados;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException  {

        Dispatcher d = new Dispatcher();

        d.agregarEmpleado(1, Rol.SUPERVISOR);
        d.agregarEmpleado(2, Rol.DIRECTOR);
        d.agregarEmpleado(3, Rol.OPERADOR);
        d.agregarEmpleado(4, Rol.OPERADOR);

        for (int i = 0; i < 100; i++) {
            d.dispatchCall();
        }

        d.future.get();

        d.resultados
            .stream()
            .forEach(resultado -> System.out.println(resultado.empleado.getId() + ", " + resultado.llamada));

    }

}
