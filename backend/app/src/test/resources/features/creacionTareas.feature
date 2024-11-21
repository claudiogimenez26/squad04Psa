# language: es

Característica: Creación de tareas

     #Escenario: Se crea tarea para proyecto exitosamente
     #    Dado un proyecto
     #    Cuando creo una tarea asociada con ese proyecto
     #    Entonces la tarea debería tener asociado a ese proyecto como proyecto asociado

     #Escenario: No se puede crear tarea asociada a proyecto inexistente
     #    Dado un proyecto inexistente
     #    Cuando intento crear una tarea asociada con ese proyecto inexistente
     #    Entonces la operación debería fallar
     #    Y la tarea no debería existir
