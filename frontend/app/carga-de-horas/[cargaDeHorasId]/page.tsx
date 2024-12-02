import type { Proyecto, Tarea } from "@/_lib/tipos";
import Formulario from "./Formulario";

export default async function () {
  const [proyectos, tareas, cargas] = (await Promise.all([
    fetch(`${process.env.BACKEND_URL}/proyectos`).then((res) => res.json()),
    fetch(`${process.env.BACKEND_URL}/tareas`).then((res) => res.json()),
    fetch(`${process.env.BACKEND_URL}/carga-de-horas`).then((res) => res.json())
  ])) as [
    Proyecto[],
    Tarea[],
    {
      [recursoId: string]: {
        idCarga: string;
        nombreTarea: string;
        cantidadHoras: number;
        fechaCarga: string;
        tareaId: string;
        recursoId: string;
      }[];
    }
  ];

  return (
    <div className="space-y-6">
      <h1 className="text-4xl font-bold">Modificar carga de horas</h1>
      <Formulario
        proyectos={proyectos}
        tareas={tareas}
        cargas={Object.values(cargas).flatMap((c) => c)}
      />
    </div>
  );
}
