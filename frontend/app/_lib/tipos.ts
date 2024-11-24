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
  tareaId: string;
  recursoId: string;
  cantidadHoras: number;
  fechaDeCarga: string;
};
