# language: es
Requisito: Consulta de cargas de horas en un proyecto

  Escenario: Se consultan las cargas de hora en un proyecto con tareas con carga de horas exitosamente
    Dado un proyecto con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Y una tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c', con proyecto con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Y una carga de horas con id '1cbe82ae-3f01-44b2-aa25-70013a6666b6', con tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c'
    Y una carga de horas con id '6c6bc100-039d-42b8-a548-88aeb1b9c489', con tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c'
    Cuando consulto las cargas de horas del proyecto
    Entonces la operación debe ser exitosa
    Y la cantidad de cargas de horas del proyecto debe ser 2
    Y una de las cargas de horas del proyecto debe tener id '1cbe82ae-3f01-44b2-aa25-70013a6666b6'
    Y una de las cargas de horas del proyecto debe tener id '6c6bc100-039d-42b8-a548-88aeb1b9c489'

  Escenario: Se consultan las cargas de hora en un proyecto sin tareas con carga de horas exitosamente
    Dado un proyecto sin tareas con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Cuando consulto las cargas de horas del proyecto
    Entonces la operación debe ser exitosa
    Y la cantidad de cargas de horas del proyecto debe ser 0

  Escenario: No se pueden consultar las cargas de horas en un proyecto inexistente
    Dado un proyecto con id inexistente '99999999-9999-9999-9999-999999999999'
    Cuando consulto las cargas de horas del proyecto
    Entonces la operación debe ser declinada
    Y el mensaje de error debe ser 'No existe el proyecto con ID: 99999999-9999-9999-9999-999999999999'
