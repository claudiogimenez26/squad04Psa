import type { Proyecto, Tarea } from "@/app/lib/types";

import Formulario from "./Formulario";

export default async function () {
  const dataProyectos = await fetch("http://localhost:8080/proyectos");
  const proyectos: Proyecto[] = await dataProyectos.json();

  const dataTareas = await fetch("http://localhost:8080/tareas");
  const tareas: Tarea[] = await dataTareas.json();

  return (
    <div className="space-y-6">
      <h1 className="text-4xl font-bold">Cargar horas</h1>
      <Formulario proyectos={proyectos} tareas={tareas} />
    </div>
  );
}
