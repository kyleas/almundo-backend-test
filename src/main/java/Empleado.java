import java.util.concurrent.CompletableFuture;

/**
 * <h1>Empleado</h1>
 * <p>Clase que representa un empleado dentro del call center, implementa la funcionalidad necesaria para atender las
 * llamadas de manera asíncrona</p>
 *
 * @author Christian Bonilla
 */
public class Empleado implements Comparable<Empleado> {

    private int id;
    private Rol rol;

    /**
     * <h2>Empleado</h2>
     * <p>Constructor de la clase, recibe un nombre de rol y crea un {@link Rol} correspondiente para asociarlo al
     * empleado.</p>
     *
     * @param id  {@link Integer} Identificador único del empleado
     * @param rol {@link String} Nombre del rol asociado al empleado
     */
    public Empleado(int id, String rol) {
        this.id = id;
        this.rol = new Rol(rol);
    }

    /**
     * <h2>atenderLlamada</h2>
     * <p>Simula la atención de llamada de parte de un {@link Empleado}.</p>
     * @return {@link CompletableFuture} Futuro responsable de la simulación asincrona.
     */
    public CompletableFuture<Empleado> atenderLlamada() {
        CompletableFuture<Empleado> f = new CompletableFuture<Empleado>();
        new Thread(() -> {
            long waitTime = Math.round(Math.random() * 50) + 50;
            try {
                Thread.sleep(waitTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
            f.complete(this);
        }).start();
        return f;
    }

    @Override
    public int compareTo(Empleado o) {
        return Integer.compare(o.getRol().getPrioridadAtencion(), this.rol.getPrioridadAtencion());
    }

    /**
     * <h2>getRol</h2>
     * <p>Obtiene el {@link Rol} de la instancia {@link Empleado} actual.</p>
     * @return {@link Rol} Rol de la instancia.
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * <h2>getRol</h2>
     * <p>Obtiene el identificador único de la instancia {@link Empleado} actual.</p>
     * @return {@link Integer} Identificador del {@link Empleado}.
     */
    public int getId() {
        return id;
    }

}
