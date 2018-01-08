/**
 * <h1>Rol</h1>
 * <p>Clase que representa el rol correspondiente a un empleado del call center.</p>
 *
 * @author Christian Bonilla
 */
public class Rol {

    // Valores estáticos útiles para simulación de roles y prioridades

    public static String DIRECTOR = "DIRECTOR";
    public static String OPERADOR = "OPERADOR";
    public static String SUPERVISOR = "SUPERVISOR";

    public static int PRIORIDAD_DIRECTOR = 1;
    public static int PRIORIDAD_SUPERVISOR = 2;
    public static int PRIORIDAD_OPERADOR = 3;

    private int prioridadAtencion;
    private String nombreRol;

    /**
     * <h2>Rol</h2>
     * <p>Construye un objeto Rol y le asigna su prioridad de atención de llamadas de acuerdo al parámetro nombre rol.</p>
     * <p>Por defecto, la prioridad de atencion de llamadas del Rol será la misma de un operador.</p>
     * @param nombreRol {@link String} Nombre del rol para crear una instancia
     */
    public Rol(String nombreRol) {

        if (nombreRol.equalsIgnoreCase(DIRECTOR)) {
            this.prioridadAtencion = PRIORIDAD_DIRECTOR;
            this.nombreRol = DIRECTOR;

        } else if (nombreRol.equalsIgnoreCase(SUPERVISOR)) {
            this.prioridadAtencion = PRIORIDAD_SUPERVISOR;
            this.nombreRol = SUPERVISOR;

        } else {
            this.prioridadAtencion = PRIORIDAD_OPERADOR;
            this.nombreRol = OPERADOR;
        }
    }

    /**
     * <h2>getNombreRol</h2>
     * <p>Obtiene el nombre de rol de la instancia {@link Rol} actual.</p>
     * @return {@link String} Nombre de rol de la instancia.
     */
    public String getNombreRol() {
        return nombreRol;
    }

    /**
     * <h2>getPrioridadAtencion</h2>
     * <p>Obtiene la prioridadAtencion del rol para atender llamadas de la instancia {@link Rol} actual.</p>
     * @return {@link String} Nombre de rol de la instancia.
     */
    public int getPrioridadAtencion() {
        return prioridadAtencion;
    }

}
