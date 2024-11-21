# language: es

Requisito: Carga de horas
  Escenario: Se cargan horas a una tarea exitosamente
    Dado un recurso con id '48655ef6-6997-407a-b544-91f91c3fde6f'
    Y una tarea activa con id '52400cce-f973-480c-b131-c038b021806e'
    Cuando el recurso realiza una carga de 8.0 horas a la tarea en la fecha '19/11/2024'
    Entonces la operación debe ser exitosa
    Y la carga de horas debe ser registrada
    Y la carga de horas debe estar asociada el recurso con id '48655ef6-6997-407a-b544-91f91c3fde6f'
    Y la carga de horas debe estar asociada a la tarea con id '52400cce-f973-480c-b131-c038b021806e'
    Y la cantidad de horas de la carga de horas debe ser 8.0 horas
    Y la fecha de carga de horas debe ser '19/11/2024'

  Escenario: Un recurso puede volver a cargar horas a una misma tarea
    Dado un recurso con id '48655ef6-6997-407a-b544-91f91c3fde6f'
    Y una tarea activa con id '52400cce-f973-480c-b131-c038b021806e'
    Y una carga de horas registradas para la tarea con id '52400cce-f973-480c-b131-c038b021806e', y recurso con id '48655ef6-6997-407a-b544-91f91c3fde6f', en la fecha '10/11/2024'
    Cuando el recurso realiza una carga de 8.0 horas a la tarea en la fecha '19/11/2024'
    Entonces la operación debe ser exitosa
    Y la carga de horas debe ser registrada

  Escenario: Diferentes recursos no pueden cargar horas a una misma tarea
    Dado un recurso con id '5395aa72-91cb-47cc-946f-94fd98896164'
    Y una tarea activa con id '52400cce-f973-480c-b131-c038b021806e'
    Y una carga de horas registradas para la tarea con id '52400cce-f973-480c-b131-c038b021806e', y recurso con id '48655ef6-6997-407a-b544-91f91c3fde6f', en la fecha '10/11/2024'
    Cuando el recurso realiza una carga de 8.0 horas a la tarea en la fecha '19/11/2024'
    Entonces la operación debe ser declinada
    Y la carga de horas no debe ser registrada
    Y el mensaje de error debe ser 'Esta tarea ya está asignada al recurso con ID: 48655ef6-6997-407a-b544-91f91c3fde6f'

  Escenario: No se pueden cargar horas a una tarea inexistente
    Dado un recurso con id '48655ef6-6997-407a-b544-91f91c3fde6f'
    Y una tarea con id inexistente '99999999-9999-9999-9999-999999999999'
    Cuando el recurso realiza una carga de 8.0 horas a la tarea en la fecha '19/11/2024'
    Entonces la operación debe ser declinada
    Y la carga de horas no debe ser registrada
    Y el mensaje de error debe ser 'No existe la tarea con ID: 99999999-9999-9999-9999-999999999999'

  Escenario: No se pueden cargar horas de un recurso inexistente
    Dado un recurso con id inexistente '99999999-9999-9999-9999-999999999999'
    Y una tarea activa con id '52400cce-f973-480c-b131-c038b021806e'
    Cuando el recurso realiza una carga de 8.0 horas a la tarea en la fecha '19/11/2024'
    Entonces la operación debe ser declinada
    Y la carga de horas no debe ser registrada
    Y el mensaje de error debe ser 'No existe el recurso con ID: 99999999-9999-9999-9999-999999999999'

  Escenario: No se pueden cargar horas negativas
    Dado un recurso con id '48655ef6-6997-407a-b544-91f91c3fde6f'
    Y una tarea activa con id '52400cce-f973-480c-b131-c038b021806e'
    Cuando el recurso realiza una carga de -8.0 horas a la tarea en la fecha '19/11/2024'
    Entonces la operación debe ser declinada
    Y la carga de horas no debe ser registrada
    Y el mensaje de error debe ser 'La cantidad de horas no puede ser negativa'

  Escenario: No se pueden cargar horas a una tarea pausada
    Dado un recurso con id '48655ef6-6997-407a-b544-91f91c3fde6f'
    Y una tarea pausada con id '52400cce-f973-480c-b131-c038b021806e'
    Cuando el recurso realiza una carga de 8.0 horas a la tarea en la fecha '19/11/2024'
    Entonces la operación debe ser declinada
    Y la carga de horas no debe ser registrada
    Y el mensaje de error debe ser 'No se pueden cargar horas a una tarea pausada'
