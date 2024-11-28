# language: es
Requisito: Consulta de costo de un recurso
  # costo recurso = (costo rol * cantidad de horas cargadas) * cantidad de cargas

  Escenario: Se consulta el costo de un recurso exitosamente
    Dado un rol con id '1f14a491-e26d-4092-86ea-d76f20c165d1', con costo 30 por hora
    Y un recurso con id 'ff14a491-e26d-4092-86ea-d76f20c165d1', con rol con id '1f14a491-e26d-4092-86ea-d76f20c165d1'
    Y una carga de horas con id '1cbe82ae-3f01-44b2-aa25-70013a6666b6', con tarea con id '49d21007-32a6-4b9f-9c11-3cd7c3992c4c', cargada por el recurso con id 'ff14a491-e26d-4092-86ea-d76f20c165d1' con 8,0 horas cargadas
    Cuando consulto el costo del recurso
    Entonces la operación debe ser exitosa
    Y el costo del recurso debe ser 240

  Escenario: No se puede consultar el costo de un recurso inexistente
    Dado un recurso con id inexistente '000000-0000-0000-0000-0000000000', con rol con id '1f14a491-e26d-4092-86ea-d76f20c165d1'
    Cuando consulto el costo del recurso
    Entonces la operación debe ser declinada
    Y el mensaje de error debe ser 'No existe el recurso con ID: 000000-0000-0000-0000-0000000000'
