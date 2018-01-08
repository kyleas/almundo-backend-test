# Prueba Back-End Almundo

Este repositorio contiene el código fuente para la prueba Back-End proporcionada por Almundo.

## Requerimientos

Requiere:
* Java JRE 1.8 (Configurado en el nivel 1.8 del lenguaje)

## Adicional

El programa funciona haciendo uso de una estrategia de ejecución asíncrona mediante futuros completables de Java. Cada `Empleado` implementa un método que retorna un futuro completable para la atención de llamadas.

Se implementaron tres pruebas:

* `procesaDiezPeticionesConcurrentes` Evalúa que se hayan ejecutado diez peticiones de manera concurrente (atendidas por diferentes empleados).
* `procesaPeticionesMayorEmpleados` Evalúa que se ejecuten todas las peticiones cuando las llamadas superan el número de empleados, ubicando las llamadas en una cola (FIFO).
* `procesaPeticionesEnOrdenDePrioridad` Evalúa que al ejecutarse un número dado de peticiones, se atienden de acuerdo a una cola prioritaria según la prioridad de atención de llamadas para cada empleado.
