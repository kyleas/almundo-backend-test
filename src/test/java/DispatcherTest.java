import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class DispatcherTest {

    @Test
    public void procesaDiezPeticionesConcurrentes() {
        Dispatcher d = new Dispatcher();

        d.agregarEmpleado(1, Rol.OPERADOR);
        d.agregarEmpleado(2, Rol.OPERADOR);
        d.agregarEmpleado(3, Rol.OPERADOR);
        d.agregarEmpleado(4, Rol.OPERADOR);
        d.agregarEmpleado(5, Rol.OPERADOR);
        d.agregarEmpleado(6, Rol.OPERADOR);
        d.agregarEmpleado(7, Rol.OPERADOR);
        d.agregarEmpleado(8, Rol.SUPERVISOR);
        d.agregarEmpleado(9, Rol.SUPERVISOR);
        d.agregarEmpleado(10, Rol.DIRECTOR);

        try {
            d.dispatch(20);

            d.getFuture().get();

            ArrayList<Integer> empleados = new ArrayList<>();

            // Las primeras 10 llamadas deben ser atendidas por usuarios diferentes
            List<Integer> primerasLlamadas = d.getResultados()
                .stream()
                .filter(resultado -> resultado.tipo == 1)
                .filter(resultado -> {
                    if (!empleados.contains(resultado.empleado.getId())) {
                        empleados.add(resultado.empleado.getId());
                        return true;
                    }
                    return false;
                })
                .mapToInt(resultado -> resultado.llamada)
                .sorted()
                .boxed()
                .collect(Collectors.toList());

            for (int i = 0; i < 10; i++) {
                if (primerasLlamadas.get(i) != i+1) {
                    assert false;
                }
            }

            assert true;

        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void procesaPeticionesMayorEmpleados() {
        Dispatcher d = new Dispatcher();

        d.agregarEmpleado(1, Rol.OPERADOR);
        d.agregarEmpleado(2, Rol.OPERADOR);
        d.agregarEmpleado(3, Rol.OPERADOR);
        d.agregarEmpleado(4, Rol.DIRECTOR);
        d.agregarEmpleado(5, Rol.SUPERVISOR);

        try {
            d.dispatch(10);

            d.getFuture().get();

            ArrayList<Integer> empleados = new ArrayList<>();

            // Deben haberse despachado las 10 peticiones
            assert d.getResultados().stream().filter(resultado -> resultado.tipo == 1).collect(Collectors.toList()).size() == 10;

        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void procesaPeticionesEnOrdenDePrioridad() {
        Dispatcher d = new Dispatcher();

        d.agregarEmpleado(1, Rol.DIRECTOR);
        d.agregarEmpleado(2, Rol.SUPERVISOR);
        d.agregarEmpleado(3, Rol.OPERADOR);
        d.agregarEmpleado(4, Rol.OPERADOR);

        try {
            d.dispatch(10);

            d.getFuture().get();

            PriorityQueue<Empleado> p = new PriorityQueue<>();
            p.add(new Empleado(1, Rol.DIRECTOR));
            p.add(new Empleado(2, Rol.SUPERVISOR));
            p.add(new Empleado(3, Rol.OPERADOR));
            p.add(new Empleado(4, Rol.OPERADOR));

            // El resultado debe estar de acuerdo a una cola prioritaria, según la asignación y finalización de llamadas
            for (int i = 0; i < d.getResultados().size(); i++) {
                Dispatcher.Resultado resultado = d.getResultados().get(i);
                if (resultado.tipo == 0) {
                    if (p.poll().getId()!= resultado.empleado.getId()) {
                        assert false;
                        return;
                    }
                }
                else if (resultado.tipo == 1) {
                    p.add(resultado.empleado);
                }
            }

            assert true;

        } catch (Exception e) {
            e.printStackTrace();
            assert false;
        }
    }

}
