# language: es

Requisito: Consultar los proyectos disponibles
  Escenario: Consulta de proyectos disponibles
    Dado un proyecto Carga de horas
    Y un proyecto Finanzas
    Y un proyecto de Tickets
    Cuando consulto los proyectos
    Entonces obtengo el proyecto Carga de horas
    Y el proyecto de Tickets
    Y el proyecto de Finanzas