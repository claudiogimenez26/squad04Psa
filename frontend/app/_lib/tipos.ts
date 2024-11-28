export type Proyecto = {
  id: string;
  nombre: string;
  descripcion: string;
};

export type Tarea = {
  id: string;
  proyectoId: string;
  nombre: string;
  descripcion: string;
};

export type Recurso = {
  id: string;
  nombre: string;
  apellido: string;
  dni: number;
  rolId: string;
};

export type Rol = {
  id: string;
  nombre: string;
  experiencia: string;
};

export type CargaDeHoras = {
  id: string;
  tareaId: string;
  tareaNombre: string;
  cantidadHoras: number;
  fechaCarga: string;
  nombreProyecto: string;
};

export type CostosDeRecursosPorProyecto = {
  [recursoId: string]: {
    recursoId: string;
    rolId: string;
    costo: number;
    nombreRecurso: string;
    nombreRol: string;
    horasTrabajadas: number;
    costoRol: number;
    mes: string;
  }[];
};
