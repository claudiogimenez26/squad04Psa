# language: es
Requisito: Eliminar carga de horas

  Escenario: Se elimina carga de horas a una tarea exitosamente
    Dado un proyecto con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Y una tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c', con proyecto con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Y una carga de horas con id '1cbe82ae-3f01-44b2-aa25-70013a6666b6', con tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c'
    Cuando elimino la carga de horas
    Entonces la operación debe ser exitosa
    Y la tarea no debe tener horas cargadas