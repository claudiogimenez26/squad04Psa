# language: es
Requisito: Consulta de cargas de horas por recurso

  Escenario: Se consultan exitosamente las cargas de hora de un recurso
    Dado un recurso con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Y una tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c'
    Y una carga de horas con id '1cbe82ae-3f01-44b2-aa25-70013a6666b6', con tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c', cargada por el recurso con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Y una carga de horas con id '6c6bc100-039d-42b8-a548-88aeb1b9c489', con tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c', cargada por el recurso con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Y una tarea con id 'd775efbd-61ec-4ff4-a80a-aa4a299d1a5d'
    Y una carga de horas con id 'b6154921-a0b3-4e5b-9ba5-b75a67f0bfa3', con tarea con id 'd775efbd-61ec-4ff4-a80a-aa4a299d1a5d', cargada por el recurso con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Cuando consulto las cargas de horas del recurso
    Entonces la operación debe ser exitosa
    Y la cantidad de cargas de horas del recurso debe ser 3
    Y una de las cargas de horas del recurso debe tener id '1cbe82ae-3f01-44b2-aa25-70013a6666b6'
    Y una de las cargas de horas del recurso debe tener id '6c6bc100-039d-42b8-a548-88aeb1b9c489'
    Y una de las cargas de horas del recurso debe tener id 'b6154921-a0b3-4e5b-9ba5-b75a67f0bfa3'

  Escenario: Se consultan exitosamente las cargas de hora de un recurso que no ha cargado horas
    Dado un recurso con id '51089e6d-6a0c-48a3-8bde-fd9d684197da' que no ha cargado horas
    Cuando consulto las cargas de horas del recurso
    Entonces la operación debe ser exitosa
    Y la cantidad de cargas de horas del recurso debe ser 0

  Escenario: No se pueden consultar las cargas de horas de un recurso inexistente
    Dado un recurso con id inexistente '99999999-9999-9999-9999-999999999999'
    Cuando consulto las cargas de horas del recurso
    Entonces la operación debe ser declinada
    Y el mensaje de error debe ser 'No existe el recurso con ID: 99999999-9999-9999-9999-999999999999'
