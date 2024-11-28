# language: es
Requisito: Consulta de costo de un proyecto
  # costo proyecto = suma(costo recursos trabajando en el proyecto)

  Escenario: Se consulta el costo de un proyecto exitosamente
    Dado un proyecto con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Y un rol con id '1f14a491-e26d-4092-86ea-d76f20c165d1', con costo 30 por hora
    Y un recurso con id 'ff14a491-e26d-4092-86ea-d76f20c165d1', con rol con id '1f14a491-e26d-4092-86ea-d76f20c165d1'
    Y una tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c', con proyecto con id '51089e6d-6a0c-48a3-8bde-fd9d684197da'
    Y una carga de horas con id '1cbe82ae-3f01-44b2-aa25-70013a6666b6', con tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c', cargada por el recurso con id 'ff14a491-e26d-4092-86ea-d76f20c165d1' con 8,0 horas cargadas y fecha "27/11/2024"
    Y una carga de horas con id '6c6bc100-039d-42b8-a548-88aeb1b9c489', con tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c', cargada por el recurso con id 'ff14a491-e26d-4092-86ea-d76f20c165d1' con 6,0 horas cargadas y fecha "26/11/2024"
    Cuando consulto el costo del proyecto
    Entonces la operación debe ser exitosa
    Y el costo del proyecto debe ser 420

  Escenario: No se puede consultar el costo de un proyecto inexistente
    Dado un proyecto con id inexistente '000000-0000-0000-0000-000000'
    Cuando consulto el costo del proyecto
    Entonces la operación debe ser declinada
    Y el mensaje de error debe ser 'No existe el proyecto con ID: 000000-0000-0000-0000-000000'