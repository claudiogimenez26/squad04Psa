# language: es
Requisito: Eliminar carga de horas

  Escenario: Se elimina carga de horas exitosamente
    Dada una carga de horas con id '1cbe82ae-3f01-44b2-aa25-70013a6666b6'
    Cuando se elimina la carga de horas con id '1cbe82ae-3f01-44b2-aa25-70013a6666b6'
    Entonces la operación debe ser exitosa
    Y la tarea no debe tener horas cargadas

  Escenario: No se pueden eliminar carga de horas inexistentes
    Dada una tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c', con proyecto con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Cuando se elimina la carga de horas con id inexistente '99999999-9999-9999-9999-999999999999'
    Entonces la operación debe ser declinada
    Y el mensaje de error debe ser 'No existe la carga de horas con ID: 99999999-9999-9999-9999-999999999999'
